<%--
    Simple jsp page to log a user out of the website

    Document   : logout
    Created on : 06-Oct-2016, 18:12:46
    Author     : Big Cheesy B
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
        
        <%
            //Sets the Logged In object to null, invalidates the session and then redirects to
            //the index page
            currUser = null;
            session.invalidate();
            response.sendRedirect("/Instagrim/");       
        %>   
    </body>
</html>
