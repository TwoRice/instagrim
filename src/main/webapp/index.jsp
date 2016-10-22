<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator

    TODO

    Fix Error when logging in with no username
    Find difference between PROCESSED and IMAGE type and comment accordingly
    Add 404 page, see Protect pages
    Ask Andy how to find python version to work with Cassandra

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
                LinkedList<Pic> lsPics = (LinkedList<Pic>) session.getAttribute("Pics");
                if(lsPics == null){
            %>
            <p>nothing here</p>
            <%
                }else{
                    Iterator<Pic> i;
                    i = lsPics.iterator();
                    while(i.hasNext()){
                        Pic p = (Pic) i.next();
            %>
            <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
            <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>
            <%}}%>
            
        </article>
        
            
    </body>
    
</html>
