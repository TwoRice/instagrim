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
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Servlet to handle requests for the Login page
 * @author Administrator
 */
@WebServlet(name = "Login", urlPatterns = {"/Login","/Login/*"})
public class Login extends HttpServlet {

    Cluster cluster=null;


    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * Get method called when the page is loaded which forwards the user to the login.jsp page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
        rd.forward(request, response);
        
    }
    
    /**
     * Post method which is run when the user submits the login form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Gets the login and password from the form
        String login=request.getParameter("login");
        String password=request.getParameter("password");
        
        User us=new User();
        boolean isValid;
        us.setCluster(cluster);
        isValid=us.loginWithUsername(login, password);
        if(!isValid){
            //Checks
            login = us.loginWithEmail(login, password);
            if(login != null){
                isValid = true;
            }
        }
        HttpSession session=request.getSession();
        System.out.println("Session in servlet "+session);
        //Sets the LoggedIn object for the user
        if (isValid){
            LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(login);
            
            session.setAttribute("LoggedIn", lg);
            System.out.println("Session in servlet "+session);
            response.sendRedirect("/Instagrim/");
            
        }else{
            request.setAttribute("invalidLogin", "Invalid Username or Password");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);           
        }
        
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
