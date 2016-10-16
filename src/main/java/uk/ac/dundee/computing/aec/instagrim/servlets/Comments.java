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
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;


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
        
        String args[] = Convertors.SplitRequestPath(request);
        PicModel pm = new PicModel();
        //Comments cm = new Comments()
        
        //LinkedList commentIDs = new LinkedList()
        //LinkedList comments = new LinkedList()
        //commentIDs = pm.getCommentIDs(arg[2]);
        //comments = cm.getComments(commentIDs)
        
        
        System.out.println("Opening Comments...");
        RequestDispatcher rd = request.getRequestDispatcher("comments.jsp");
        rd.forward(request, response);
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        
    }
    
    
}
