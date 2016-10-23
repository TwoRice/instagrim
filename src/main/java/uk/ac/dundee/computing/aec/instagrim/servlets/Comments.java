package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.UUID;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;



/**
 * Servlet to handle requests for Comment pages
 * @author Big Cheesy B
 */
@WebServlet(name = "postComment", urlPatterns = {"/Comments","/Comments/*"})
public class Comments extends HttpServlet {
    
    private Cluster cluster = null;
    
    public Comments(){
        
    }
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
    
    /**
     * Get method for when the page is loaded
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{     
        //Splits up the url into an array of strings with / delimiter
        String args[] = Convertors.SplitRequestPath(request);
        displayComments(args[2], request, response);
    }
    
    /**
     * Post method for the page, called when the user submits the Post new comment form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        //Gets the object for the currently logged in user
        LoggedIn activeUser = (LoggedIn) session.getAttribute("LoggedIn");
        String args[] = Convertors.SplitRequestPath(request);
        //Gets the pic UUID from the URL
        UUID picID = UUID.fromString(args[2]);
        
        //Redirects the user to the login page if they are no logged in
        if( activeUser == null || activeUser.getlogedin() == false){
            response.sendRedirect("/Instagrim/Login");
        }
        else{
            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            //Gets the comment text from the form
            String commentText = request.getParameter("commentEntry");
            System.out.println(commentText);
            //Gets the logged in user's username
            String user = activeUser.getUsername();
            //Passes the pic UUID, the username and the comment text to the comment model
            cm.postNewComment(picID, user, commentText);
            //Reloads the page
            response.sendRedirect("/Instagrim/Comments/"+args[2]);
        }
     
    }
    
    /**
     * Gets comments for an aimge from the model then forwards the comments to the jsp page
     */
    public void displayComments(String picID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{       
            UUID uuid = UUID.fromString(picID);

            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            LinkedList<UUID> lsCommentIDs;
            LinkedList<Comment> lsComments;

            //Gets the list of comment IDs from the model
            lsCommentIDs = cm.getCommentsFromPic(uuid);
            //Sends the IDs to the models to process the list of comments
            lsComments = cm.getCommentsFromID(lsCommentIDs);

            RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp");
            //Sets request attribute to the Linked List of comments for the image
            request.setAttribute("comments", lsComments);
            rd.forward(request, response);     
    }
    
        
}
