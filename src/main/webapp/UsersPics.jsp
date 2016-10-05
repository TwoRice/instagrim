<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
         
        <article>
            <h1>Your Images</h1>
            <%
                //Gets the linked list of user's pictures from the servlet's request
                java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
                if (lsPics == null) {
            %>
            <p>No Images found</p>
            <%
                //Iterates through the linked list of pictures displays them on the web page
                } else {
                Iterator<Pic> iterator;
                iterator = lsPics.iterator();
                while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
            %>
            <!--Creates link to picture -->
            <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
            <!--Displays picture thumbnail on page --> 
            <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>
            <%
                }
                }
            %>
        
        </article>
                
    </body>
    
</html>
