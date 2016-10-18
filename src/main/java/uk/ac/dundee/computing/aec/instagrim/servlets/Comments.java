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
import java.math.BigInteger;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;



/**
 *
 * @author Big Cheesy B
 */
@WebServlet(name = "Comments", urlPatterns = {"/Comments","/Comments/*"})
public class Comments extends HttpServlet {
    
    private Cluster cluster = null;
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        //Splits up the url into an array of strings with / delimiter
        String args[] = Convertors.SplitRequestPath(request);
        if(args[2] == "comments.jsp"){
        }
        else{
            System.out.println(args[2]);
            //Extracts the image's id from the args
            UUID picid = UUID.fromString(args[2]);

            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            LinkedList<Comment> lsComments;

            lsComments = cm.getCommentsFromPic(picid);

            //Sets request attribute to the Linked List of comments ids for the image
            request.setAttribute("comments", lsComments);
            RequestDispatcher rd = request.getRequestDispatcher("comments.jsp");
            rd.forward(request, response);
        }
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        
    }
        
}
