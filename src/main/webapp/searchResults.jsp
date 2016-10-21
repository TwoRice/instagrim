<%-- 
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
                    LinkedList<LoggedIn> searchResults = (LinkedList<LoggedIn>) request.getAttribute("searchResults");
                    if(searchResults == null){
                %>
                <div class="result">
                    No Results
                </div>
                <%}else{
                    Iterator<LoggedIn> i = searchResults.iterator();
                    while(i.hasNext()){
                        LoggedIn result = i.next();
                %>
                <a href="/Instagrim/Profile/<%=result.getUsername()%>">
                    <div class="result">
                        <div class="row">
                            <div class="col-sm-3">
                                <%
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
