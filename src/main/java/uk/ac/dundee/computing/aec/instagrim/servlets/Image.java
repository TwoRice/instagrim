package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */

//urlPatterns which use this servlet
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Upload/",
    "/Upload/*",
    "/Home",
    "/Home/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        //Maps url patterns "Image", "Images", "Thumb" and "Home" to an integer in a hash map
        CommandsMap.put("Image", 1);
        CommandsMap.put("Thumb", 2);
        CommandsMap.put("Upload", 3);
        CommandsMap.put("Home", 4);

    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     * 
     * Get Method for this servlet. Run upon user using one of the url patterns for this servlet
     * 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Splits Url arguments up with / delimiter
        String args[] = Convertors.SplitRequestPath(request);
        RequestDispatcher rd;
        int command;
        try {
            //Gets first argument from url and converts it to its integer value (see public Image())
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        //Switch statement to test which of the three url patterns the first argument is
        switch (command) {
            //Displays Image using the picid given in second argument of url
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            //Displays thumbnail for picture using the picid given in the second argument of url 
            case 2:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                break;
            case 3:
                rd = request.getRequestDispatcher("/upload.jsp");
                //Tells the jsp page that the upload page is being used to upload a profile picture
                //if the url contain profile picture
                if(args.length == 3){
                    if(args[2].equals("ProfilePicture")){
                        request.setAttribute("profilePicture", "profilePicture");
                    }
                    else{
                        error("Bad Operator", response);
                    }
                }
                rd.forward(request, response);
            case 4:
                DisplayHome(args[2], request, response);
            default:
                error("Bad Operator", response);
        }
    }

    /**
     * Requests 
     * 
     * @param type - int to specify if the image is a processed image or a thumbnail
     * @param Image - string for the UUID of the image taken from the url
     */
    private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        
        //Requests the picture with the UUID from the url from the pic model
        Pic p = pm.getPic(type,java.util.UUID.fromString(Image));
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
    
    /**
     * Method
     * @param filter - Determines whether to display all pictures or following pictures
     */
    private void DisplayHome(String filter, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        LinkedList<Pic> lsPics = new LinkedList<>();
        HttpSession session = request.getSession();
        RequestDispatcher rd;
        //Gets the object for the currently logged in user
        LoggedIn activeUser = (LoggedIn) session.getAttribute("LoggedIn");
        
        //Gets all the public pictures from the model if the filter is All
        if(filter.equals("All")){
            lsPics = pm.getRecentPics();
        }
        //Gets all the pictures for the uer's the user is following if the filter is Following contains
        else if(filter.equals("Following")){
            lsPics = pm.getFollowingPics(activeUser.getUsername());
        }
        else{
            error("Bad Operator", response);
        }
        
        //Sends the list of pics back to the jsp page
        session.setAttribute("Pics", lsPics);
        response.sendRedirect("/Instagrim/");
    
    }
            

    /**
     * Post method for the upload form which sends the relevant data to the Pic Model to be uploaded to
     * the database
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);
        //Gets whether or not the picture being uploaded is a profile picture
        String profilePicture = request.getParameter("profilePicture");
        //Gets whether or not the image is being uploaded as private
        String privacy = request.getParameter("Privacy");
        
        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username="majed";
            if (lg.getlogedin()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                //Uploads the picture as a profile picture
                if(profilePicture != null){
                    User us = new User();
                    us.setCluster(cluster);
                    UUID picid = tm.insertPic(b, type, filename, username, privacy, true);
                    us.setProfilePic(username, picid);
                }
                //Uploads the picture as a regular image
                else{
                    tm.insertPic(b, type, filename, username, privacy, false);
                }

                is.close();
            }
             RequestDispatcher rd = request.getRequestDispatcher("upload.jsp");
             rd.forward(request, response);
        }
                
    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
