<%-- 
    Document   : comments
    Created on : 16-Oct-2016, 18:59:39
    Author     : Big Cheesy B
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.Comment"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
        
        <div class="container">  
            <div class="comment">
                <%
                    java.util.LinkedList<Comment> lsComments;
                    lsComments = (java.util.LinkedList<Comment>) request.getAttribute("comments");
                    if(lsComments == null){                             
                %>
                <span>No Comments</span>
                <%}else{%>
                <%
                    
                %>
            </div>
            
            <form method="POST" action="Commm">
                <div class="panel panel-default">         
                    <div class="panel-heading" style="background-color:#555;color:#FFF">Comments</div>     
                        <div class="panel-body">
                            <div class="form-group">
                                <label for="commentEntry">New Comment:</label>
                                <textarea class="form-control" rows="5" id="commentEntry"></textarea>
                                <input type="submit" class="btn btn default" value="Post">
                            </div>
                        </div>
                </div>   
            </form>  
        </div>
        
    </body>

</html>
