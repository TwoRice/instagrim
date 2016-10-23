/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet to handle requests for Profile pages
 * @author Big Cheesy B
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile", "/Profile/*", "/Follow", "/Follow/*"})
public class Profile extends HttpServlet {

    private Cluster cluster = null;
    
    public Profile(){
    
    }
    
    public void init(ServletConfig config) throws ServletException {
    cluster = CassandraHosts.getCluster();
    }
    
    /**
     * Get method which is called when the page is loaded
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Splits the url up into an array
        String args[] = Convertors.SplitRequestPath(request);
        displayProfile(args[2], request, response);
        
    }
    
    /**
     * Post method for which is run when the follow form is submitted.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
        //Gets the follwer's username and followee user's from the form
        String userToFollow = request.getParameter("userToFollow");
        String activeUser = request.getParameter("activeUser");
        User us = new User();
        us.setCluster(cluster);
        
        //Unfollows the user if the user is already following them
        if(us.checkIfFollowing(userToFollow, activeUser)){
           us.unFollowUser(userToFollow, activeUser);
        }
        //Follows the user if the user is not following them
        else{
            us.followUser(userToFollow, activeUser);  
        }
        
        //Reloads the page
        response.sendRedirect(userToFollow); 

    }
  
    /**
     * Requests a linked list filled with a user's pictures, the user's profile picture and if the active
     *  user is following this user from Picture and User Model and forwards the data to the jsp page
     * 
     * @param user - string for username taken from the url
     */
    private void displayProfile(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel pm = new PicModel();
        User us = new User();
        pm.setCluster(cluster); 
        us.setCluster(cluster);
        HttpSession session = request.getSession();
        LinkedList<Pic> lsPics;
        Pic profilePic = new Pic();
        RequestDispatcher rd;
        
        //Gets the object for the currently logged in user
        LoggedIn activeUser = (LoggedIn) session.getAttribute("LoggedIn");
        boolean following = false;
        //Checks if there is a user currently logged in
        if(activeUser != null){
            if(activeUser.getlogedin() == true){
                //Sets whether or not the current user is following the user
                following = us.checkIfFollowing(User, activeUser.getUsername());
            }
            //Sends the user to the seperate jsp page for their own profile
            if(activeUser.getUsername().equals(User)){
                rd = request.getRequestDispatcher("/userProfile.jsp");
            }
            //Sends the user to the generic profile jsp page
            else{
                rd = request.getRequestDispatcher("/UsersPics.jsp");
            }
        }
        //Sends the user to the generic profile jsp page
        else{
            rd = request.getRequestDispatcher("/UsersPics.jsp");
        }

        //Gets the user's profile picture from the user model
        profilePic = us.getProfilePic(User);
        //Gets the list of the user's images from the Pic Model
        lsPics = pm.getPicsForUser(User);   
        
        //Sets all the request attributes for the jsp page
        request.setAttribute("User", User);
        request.setAttribute("profilePic", profilePic);
        request.setAttribute("following", following);
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);

    }
    
}
