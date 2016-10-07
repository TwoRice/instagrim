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
            <h1>Recent</h1>
            <% 
                Cluster cluster = CassandraHosts.getCluster();
                PicModel pm = new PicModel();
                pm.setCluster(cluster);
                
                java.util.LinkedList<Pic> lsPics = pm.getRecentPics();
                if(lsPics == null){
                    System.out.println("lspic is null hello");
            %>
            <p>nothing here</p>
            <%
                }else{
                    Iterator<Pic> iterator;
                    iterator = lsPics.iterator();
                    while (iterator.hasNext()) {
                        Pic p = (Pic) iterator.next();
            %>
            <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
            <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>
            <%
                }
                    }
            %>
}
            
        </article>
        
            
    </body>
    
</html>
