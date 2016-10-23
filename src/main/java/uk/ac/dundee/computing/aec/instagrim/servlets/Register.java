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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;

/**
 * Servlet which handles requests for the register page
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register", "/Register/*"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
    
    /**
     * Get method which forwards the user to the register jsp page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        rd.forward(request, response);
        
    }
    
    /**
     * Post method for the register form. Sends the user's details to the user model and registers the user
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
               
        //Gets the user details from the various inputs on the form
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String passConfirm=request.getParameter("passConfirm");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String email=request.getParameter("email");
        String emailConfirm=request.getParameter("emailConfirm");
                
        User us=new User();
        us.setCluster(cluster);
        //Checks whether all the data was input correctly
        boolean uniqueUsername = us.isValidUserName(username);
        boolean uniqueEmail = us.isValidEmail(email);
        boolean validPassword = password.equals(passConfirm);
        boolean validEmail = email.equals(emailConfirm);
                
        //Registers the user if there were no input errors
        if((uniqueUsername==true) && (uniqueEmail==true) && (validPassword==true) && (validEmail==true)){
           us.RegisterUser(username, password, firstName, lastName, email); 
           response.sendRedirect("/Instagrim");
        }
        else{
          
            //Concatinates the errors to a string containing a 0 for an error and a 1 for no error
            String errors = (toNumericStr(uniqueUsername) + toNumericStr(uniqueEmail) + toNumericStr(validPassword) + toNumericStr(validEmail));
           
            //Sends the errors string back to the jsp page
            request.setAttribute("inputError", errors);
            RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
            rd.forward(request, response);
            
        }
	    
    }
    
    /**
     * Converts a boolean value (true, false) to its numeric equivalent (1,0)
     * @param b - boolean to be converted
     * @return - numeric boolean as a string 
     */
    public static String toNumericStr(final Boolean b){
        return b.booleanValue() ? "1" : "0";
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
