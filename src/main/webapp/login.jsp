<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
               
        <article>
                               
            </br></br></br></br></br></br>
            <div class="container">

                <div class="col-xs-12">

                    <div class="col-xs-6">
                        <form method="POST" action="Login">
                            <div class="panel panel-default">

                                <div class="panel-heading" style="background-color:#555;color:#FFF">Login</div>

                                <div class="panel-body">
                                    <div class="form-group">

                                        <div class="row">
                                            <div class="col-xs-12">
                                                <%
                                                    String invalidLogin = "";
                                                    invalidLogin = (String) request.getAttribute("invalidLogin");
                                                    if(invalidLogin == null){
                                                        invalidLogin = "";
                                                    }
                                                %>
                                                <span class="logRegError"><%=invalidLogin%></span></br>
                                                <label class="control-label" for="login">Username/Email Address</label>
                                                <input type="text" class="form-control" id="login" name="login" required="required">
                                            </div>
                                        </div>    
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <label class="control-label" for="password">Password</label>
                                                <input type="password" class="form-control" id="password" name="password" required="required">                                       
                                            </div> 
                                        </div>

                                        </br><input type="submit" class="btn btn default" value="Login">

                                    </div>
                                </div>

                            </div>
                        </form>    
                    </div>

                    <div class="col-xs-6">
                        <div class="instaPromo"></div>
                    </div>

                </div>
            
            </div>   
            
        </article>
                                    
    </body>
    
</html>
