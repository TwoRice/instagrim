<%-- 
    Index pafe for the website. Displays either all public images or images for a
    specific user's following

    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator    
--%>



<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.datastax.driver.core.Cluster" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.models.PicModel" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts" %>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>

    <body>
        
        <%@include file="header.jsp" %>
        
        <article>         
            <%
                //Gets the list of pictures to be displayed by the servlet
                LinkedList<Pic> lsPics = (LinkedList<Pic>) session.getAttribute("Pics");
                if(lsPics == null){
            %>
            <p>nothing here</p>
            <div class="container">
                <%
                    }else{
                        //Iterates through each picture adding it to a div
                        Iterator<Pic> i;
                        i = lsPics.iterator();
                        while(i.hasNext()){
                            Pic p = (Pic) i.next();
                %>
                <div class="row" style="padding-bottom:50px;">
                    <div class="thumbnail" style="height:400px;width:400px;">
                        <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
                        <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>
                    </div>
                    <div class="commentLink" style="font-size: 20px;">
                        <a href="/Instagrim/Comments/<%=p.getSUUID()%>">Comments</a>
                    </div>
                </div>
                <%}}%>
            </div>
            
        </article>
        
            
    </body>
    
</html>
