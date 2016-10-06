<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
               
        <article>
            
            <h3>Register as user</h3>
            
            <form method="POST"  action="Register">
                <ul>
                    <li>
                        User Name <input type="text" name="username">
                        <%
                            boolean isValid;
                            
                            try{
                                isValid = (boolean) request.getAttribute("validUsername");
                            }
                            catch(NullPointerException e){
                                isValid = true;
                            }
                            
                            if(!isValid){
                        %>
                        <span class="logRegError">Username already exists</span>
                        <%}%>
                    </li>
                    
                    <li>
                        Password <input type="password" name="password">
                    </li>
                </ul>
                
                <br/>
                <input type="submit" value="Regidter"> 
            </form>

        </article>
        
    </body>
    
</html>
