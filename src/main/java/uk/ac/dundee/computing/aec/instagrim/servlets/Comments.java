/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{     
        //Splits up the url into an array of strings with / delimiter
        String args[] = Convertors.SplitRequestPath(request);
        displayComments(args[2], request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        LoggedIn activeUser = (LoggedIn) session.getAttribute("LoggedIn");
        String args[] = Convertors.SplitRequestPath(request);
        UUID picID = UUID.fromString(args[2]);
        
        if( activeUser == null || activeUser.getlogedin() == false){
            response.sendRedirect("/Instagrim/Login");
        }
        else{
            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            String commentText = request.getParameter("commentEntry");
            System.out.println(commentText);
            String user = activeUser.getUsername();
            cm.postNewComment(picID, user, commentText);
            response.sendRedirect("/Instagrim/Comments/"+args[2]);
        }
     
    }
    
    public void displayComments(String picID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{       
            UUID uuid = UUID.fromString(picID);

            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            LinkedList<UUID> lsCommentIDs;
            LinkedList<Comment> lsComments;

            lsCommentIDs = cm.getCommentsFromPic(uuid);
            lsComments = cm.getCommentsFromID(lsCommentIDs);

            RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp");
            //Sets request attribute to the Linked List of comments ids for the image
            request.setAttribute("comments", lsComments);
            rd.forward(request, response);     
    }
    
        
}
