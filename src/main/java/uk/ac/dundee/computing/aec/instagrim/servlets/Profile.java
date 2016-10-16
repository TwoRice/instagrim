/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
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
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Big Cheesy B
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile", "/Profile/*"})
public class Profile extends HttpServlet {

    private Cluster cluster = null;
    
    public Profile(){
    
    }
    
    public void init(ServletConfig config) throws ServletException {
    cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       
        String args[] = Convertors.SplitRequestPath(request);
        displayProfile(args[2], request, response);
        
    }

    
    /**
     * Requests a linked list filled with a user's pictures from Picture Model
     * 
     * @param user - string for username taken from the url
     */
    private void displayProfile(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel pm = new PicModel();
        pm.setCluster(cluster);        
        java.util.LinkedList<Pic> lsPics;
        Pic profilePic = new Pic();
        RequestDispatcher rd;
        
        //requests the user's profile picture from the pic model
        profilePic = pm.getProfilePic(User);
        //Requests a linked list of the user's pictures from the pic model
        lsPics = pm.getPicsForUser(User);
        //sets request for UserPics.jsp page
        rd = request.getRequestDispatcher("/UsersPics.jsp");        
        //seta a request attribute to the username for the profile
        request.setAttribute("User", User);
        //sets a request attribute to the user's profile picture
        request.setAttribute("profilePic", profilePic);
        //sets an additional request attribute to the linked list of the user's pictures
        request.setAttribute("Pics", lsPics);
        //forwards request to UserPics.jsp page
        rd.forward(request, response);

    }
    
}
