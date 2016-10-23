<%--
    Jsp page used to display search results from a search for user profiles

    Document   : searchResults
    Created on : 20-Oct-2016, 23:13:31
    Author     : Big Cheesy B
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
        
        <div class="container">
            <div class="results">
                <%
                    //Gets a list of users returned from the search from the servlet
                    LinkedList<LoggedIn> searchResults = (LinkedList<LoggedIn>) request.getAttribute("searchResults");
                    if(searchResults == null){
                %>
                <div class="result">
                    No Results
                </div>
                <%}else{
                    //Iterates through each user and displays their name and profile picture in a div
                    Iterator<LoggedIn> i = searchResults.iterator();
                    while(i.hasNext()){
                        LoggedIn result = i.next();
                %>
                <a href="/Instagrim/Profile/<%=result.getUsername()%>">
                    <div class="result">
                        <div class="row">
                            <div class="col-sm-3">
                                <%
                                    //Displays the default profile picture if the user has not set their own
                                    if(result.getProfilePic() != null){
                                %>
                                <div class="thumbnail" style="height:150px;width:150px;">
                                    <img src="/Instagrim/Thumb/<%=result.getProfilePic().toString()%>">
                                </div>
                                <%}else{%>
                                <div class="thumbnail" style="height:150px;width:150px;">
                                    <img src="/Instagrim/defaultProfile.gif">
                                </div>
                                <%}%>
                            </div>
                            <div class="col-sm-9">    
                                <div class="username">
                                    <%=result.getUsername()%>
                                </div>    
                            </div>
                        </div>
                    </div>
                </a>    
                <%}}%>
            </div>
        </div>
        
    </body>
    
</html>
