/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.util.LinkedList;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Big Cheesy B
 */
@WebServlet(name = "Search", urlPatterns = {"/Search", "/Search/*"})
public class Search extends HttpServlet {

    private Cluster cluster = null;
    
    public Search(){
        
    }
    
    public void init(ServletConfig config) throws ServletException{
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
        String searchString = request.getParameter("searchQuery");
        User us = new User();
        us.setCluster(cluster);
        LinkedList<LoggedIn> lsUsers = us.searchUsers(searchString);
        request.setAttribute("searchResults", lsUsers);
        
        RequestDispatcher rd = request.getRequestDispatcher("/searchResults.jsp");
        rd.forward(request, response);
        
    }


}
