<%-- 
    Document   : header
    Created on : 28-Sep-2016, 17:26:11
    Author     : Big Cheesy B
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    
    <header>  
        <div class="headerRow">
            <div class="col-xs-7">
                <h2>
                    <a href="/Instagrim/">InstaGrim <img src="/Instagrim/logo.gif" style ="height:50px;width:50px;"></a>
                </h2>
                
            </div>
            
            <div class="col-xs-2">
                <div class="form-group" style="padding-top:20px;">
                    <form class="form-inline" method="GET" action="/Instagrim/Search">
                        <input type="text" class="form-control" name="searchQuery" required="required" placeholder="Search Users...">
                        <input type="submit" class="btn btn default">
                    </form>
                </div>
            </div>

            <div class="col-xs-3" style="text-align:right;padding-top:20px;"> 
                
                <%                     
                    LoggedIn currUser = (LoggedIn) session.getAttribute("LoggedIn");
                    if (currUser != null) {
                        String UserName = currUser.getUsername();
                        if (currUser.getlogedin()) {                           
                }%>
                
                <div class="col-sm-4"><a href="/Instagrim/Upload">Upload</a></div>
                <div class="col-sm-4"><a href="/Instagrim/Profile/<%=currUser.getUsername()%>">Profile</a></div>
                <div class="col-sm-4"><a href="/Instagrim/logout.jsp">Logout</a></div>
                
                <% }else{ %>
                
                <div class="col-sm-6"><a href="/Instagrim/Login">Login</a></div>
                <div class="col-sm-6"><a href="/Instagrim/Register">Register</a></div>
                
                <% } %>

            </div>
        </div>
    </header>
    
</html>
