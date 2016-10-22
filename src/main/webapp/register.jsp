<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
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
                        <form method="POST" action="Register">
                            <div class="panel panel-default">

                                <div class="panel-heading" style="background-color:#555;color:#FFF">Login</div>

                                <div class="panel-body">
                                    <div class="form-group">
                                        <%
                                            String inputError = "";
                                            char[] arrInputError = {1,1,1,1};

                                            inputError = (String) request.getAttribute("inputError");

                                            if(inputError == null){  
                                                inputError = ""; 
                                            }
                                            else{
                                                arrInputError = inputError.toCharArray();
                                            }

                                        %>
                                        <div class="row">
                                            <div class="col-lg-6">
                                                <label class="control-label" for="firstName">First Name</label>
                                                <input type="text" class="form-control" id="firstName" name="firstName" required="required">
                                            </div>                                    
                                            <div class="col-lg-6">
                                                <label class="control-label" for="lastName">Last Name</label>
                                                <input type="text" class="form-control" id="lastName" name="lastName" required="required">
                                            </div>
                                        </div>

                                        <div class="row ">
                                            <div class="col-xs-12">
                                                <label class="control-label" for="username">User Name</label>
                                                <input type="text" class="form-control" id="username" name="username" required="required">
                                                <%if(arrInputError[0] == '0'){%>
                                                <span class="logRegError">Username already exists</span>
                                                <%}%>                                            
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-lg-6">
                                                <label class="control-label" for="email">Email Address</label>
                                                <input type="email" class="form-control" id="email" name="email" placeholder="example@mail.com" required="required">
                                                <%if(arrInputError[3] == '0'){%>
                                                <span class="logRegError">Email Addresses do not match</span>
                                                <%}%>
                                                <%if(arrInputError[1] == '0'){%>
                                                <span class="logRegError">Email Address already registered</span>
                                                <%}%>
                                            </div>
                                            <div class="col-lg-6">
                                                <label class="control-label" for="emailConfirm">Confirm Email</label>
                                                <input type="email" class="form-control" id="emailConfirm" name="emailConfirm" required="required">
                                            </div> 
                                        </div>

                                        <div class="row">
                                            <div class="col-lg-6">
                                                <label class="control-label" for="password">Password</label>
                                                <input type="password" class="form-control" id="password" name="password" required="required">
                                                <%if(arrInputError[2] == '0'){%>
                                                <span class="logRegError">Passwords do not match</span>
                                                <%}%>                                            
                                            </div>                                   
                                            <div class="col-lg-6">
                                                <label class="control-label" for="passConfirm">Confirm Password</label>
                                                <input type="password" class="form-control" id="passConfirm" name="passConfirm" required="required">
                                            </div>
                                        </div>    

                                        </br><input type="submit" class="btn btn default" value="Register">

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
