<%-- 
    JSP page for displaying all comments related to a single photo. Each comment
    displays the content, the username of the poster with a link to their profile
    and a timestamp for the comment


    Document   : comments
    Created on : 16-Oct-2016, 18:59:39
    Author     : Big Cheesy B
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.Comment"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
        
        <div class="container">  

            <div class="comments">
                <%
                    //Gets the list of comments from the servlet
                    LinkedList<Comment> lsComments;
                    lsComments = (LinkedList<Comment>) request.getAttribute("comments");
                    if(lsComments == null){ 
                %>
                <div class="comment">
                    <span>No Comments</span>
                </div>    
                <%}else{
                    //Iterates through the list adding each comment to a div
                    Iterator<Comment> i = lsComments.iterator();
                    while(i.hasNext()){
                        Comment comment =i.next();
                %>
                <div class="comment">
                    
                    <div class="text">
                        <%=comment.getComment()%>
                    </div>
                    
                    <div class="row">
                        <div class="col-sm-6">
                            
                            <div class="user">
                                <a href="/Instagrim/Profile/<%=comment.getUser()%>"><%=comment.getUser()%></a>
                            </div> 
                        </div>
                            
                        <div class="col-sm-6">
                            <div class="timestamp">
                                <%=comment.getTimestamp()%>
                            </div>
                        </div>
                            
                    </div>
                            
                </div>
                <%}}%>
            </div>

            
            <%--Form for posting a new comment to the photo--%>
            <form method="POST">
                <div class="panel panel-default">         
                    <div class="panel-heading" style="background-color:#555;color:#FFF">Comments</div>     
                        <div class="panel-body">
                            <div class="form-group">
                                <label for="commentEntry">New Comment:</label>
                                <textarea class="form-control" rows="5" name="commentEntry" id="commentEntry" required="required"></textarea>
                                <input type="submit" class="btn btn default" value="Post">
                            </div>
                        </div>
                </div>   
            </form>  
        </div>
        
    </body>

</html>
