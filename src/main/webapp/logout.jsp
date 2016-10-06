<%-- 
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
            sessionUser = null;
            session.invalidate();
            response.sendRedirect("index.jsp");       
        %>   
    </body>
</html>
