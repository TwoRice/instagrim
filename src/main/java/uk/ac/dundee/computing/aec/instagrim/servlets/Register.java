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
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register", "/Register/*"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        rd.forward(request, response);
        
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
               
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String passConfirm=request.getParameter("passConfirm");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String email=request.getParameter("email");
        String emailConfirm=request.getParameter("emailConfirm");
                
        User us=new User();
        us.setCluster(cluster);
        boolean uniqueUsername = us.isValidUserName(username);
        boolean uniqueEmail = us.isValidEmail(email);
        boolean validPassword = password.equals(passConfirm);
        boolean validEmail = email.equals(emailConfirm);
                
        if((uniqueUsername==true) && (uniqueEmail==true) && (validPassword==true) && (validEmail==true)){
           us.RegisterUser(username, password, firstName, lastName, email); 
           response.sendRedirect("/Instagrim");
        }
        else{
          
            String errors = (toNumericStr(uniqueUsername) + toNumericStr(uniqueEmail) + toNumericStr(validPassword) + toNumericStr(validEmail));
            
            switch(errors){
                
                case "0000":
                    request.setAttribute("inputError", errors);
                    break;
                case "0001":
                    request.setAttribute("inputError", errors);
                    break;
                case "0010":
                    request.setAttribute("inputError", errors);
                    break;
                case "0011":
                    request.setAttribute("inputError", errors);
                    break;
                case "0100":
                    request.setAttribute("inputError", errors);
                    break;
                case "0101":
                    request.setAttribute("inputError", errors);
                    break;
                case "0110":
                    request.setAttribute("inputError", errors);
                    break;
                case "0111":
                    request.setAttribute("inputError", errors);
                    break;
                case "1000":
                    request.setAttribute("inputError", errors);
                    break; 
                case "1001":
                    request.setAttribute("inputError", errors);
                    break;
                case "1010":
                    request.setAttribute("inputError", errors);
                    break;
                case "1011":
                    request.setAttribute("inputError", errors);
                    break;  
                case "1100":
                    request.setAttribute("inputError", errors);
                    break;
                case "1101":
                    request.setAttribute("inputError", errors);
                    break;
                case "1110":
                    request.setAttribute("inputError", errors);
                    break;
                default:
                    System.out.println("This definitely shouldn't happen");
                    System.out.println(errors);
                    break;
                
            }

            RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
            rd.forward(request, response);
            
        }
	    
    }
    
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
