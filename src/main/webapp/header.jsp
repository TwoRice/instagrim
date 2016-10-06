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
        <div class="row">
            <div class="col-xs-8">
                <h2>
                    <a href="/Instagrim/index.jsp">InstaGrim <img src="/Instagrim/logo.gif" style ="height:50px;width:50px;"></a>
                </h2>
                
            </div>    

            <div class="col-xs-4" style="text-align:right;padding-top:20px;"> 
                
                <%                     
                    LoggedIn sessionUser = (LoggedIn) session.getAttribute("LoggedIn");
                    if (sessionUser != null) {
                        String UserName = sessionUser.getUsername();
                        if (sessionUser.getlogedin()) {                           
                }%>
                
                <div class="col-sm-4"><a href="/Instagrim/upload.jsp">Upload</a></div>
                <div class="col-sm-4"><a href="/Instagrim/Images/<%=sessionUser.getUsername()%>">Images</a></div>
                <div class="col-sm-4"><a href="/Instagrim/logout.jsp">Logout</a></div>
                
                <% }else{ %>
                
                <div class="col-sm-6"><a href="login.jsp">Login</a></div>
                <div class="col-sm-6"><a href="register.jsp">Register</a></div>
                
                <% } %>

            </div>
        </div>
    </header>
    
</html>
