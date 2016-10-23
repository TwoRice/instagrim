<%--
    Specific Profile page for the profile of the user currently logged in. Contains
    additional features like upload profile picture etc.

    Document   : UsersProfile
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
         
        <article>

            <div class="row" style="padding-top:20px;padding-bottom:20px;">
                <div class="col-sm-2">
                    
                    <div class="thumbnail" style="height:200px;width:200px;">
                        <%--Link to upload a new profile picture--%>
                        <a href="/Instagrim/Upload/ProfilePicture">
                            <%
                                //Gets the username and profile picture from the servlet
                                String username = currUser.getUsername();
                                Pic profilePic = new Pic();
                                profilePic = (Pic) request.getAttribute("profilePic");
                                //Displays the default profile picture if profile picture comes back null
                                if(profilePic == null){
                            %>
                            <img src="/Instagrim/defaultProfile.gif" alt=" Default Profile Picture">
                            <%}else{%>
                            <img src="/Instagrim/Thumb/<%=profilePic.getSUUID()%>" alt="Profile Picture">
                            <%}%>
                        </a>    
                    </div>
                        
                </div>
                    
                <div class ="col-sm-2">
                    <div class="profileName"><%=username%>'s Profile</div>
                </div>
                                    
                <div class="col-sm-8"></div>
                
            </div>
                
            <div class="col-xs-1"></div>
            <!--Div in centre of page which contains the images-->
            <div class="col-xs-10">
            <%
                //Gets the linked list of user's pictures from the servlet's request
                java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
                if (lsPics == null) {
            %>
            <p>No Images found</p>
            <%
                //Iterates through the linked list of pictures displays them on the web page
                } else {
                    //Integer used to count numbers of pictures iterated through
                    int i = 0;
                    Iterator<Pic> iterator;
                    iterator = lsPics.iterator();
                    while (iterator.hasNext()) {                      
                        Pic p = (Pic) iterator.next();
                        //If i is even wrap the image div in a div for large devices so that there is one
                        //placed around every 2 image divs
                        if(i%2 == 0){
            %>            
            <!--Div for large devices which contains 2 small device divs, so that on a large display 
            a maximum of 4 divs are displayed in a row and on < large display a maximum of 2 divs are
            displayed-->
            <div class="col-lg-6">
                <!-- Div which will stack on small displays-->
                <div class="col-sm-6">
                    <!-- Div to contain the thumbnail and link to image-->
                    <div class="thumbnail">
                        <!--Link to image-->
                        <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
                        <!--Thumbnail for image-->
                            <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>
                    </div>
                    <div class="commentLink">
                        <a href="/Instagrim/Comments/<%=p.getSUUID()%>">Comments</a>
                    </div>    
                </div>        
            <%
                        //Adds seconds image div to the div for large devices and then closes the large
                        //device div
                        }else{
            %>
                <div class="col-sm-6">
                    <div class="thumbnail">
                        <a href="/Instagrim/Image/<%=p.getSUUID()%>" >
                        <img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/>    
                    </div>
                    <div class="commentLink">
                        <a href="/Instagrim/Comments/<%=p.getSUUID()%>">Comments</a>
                    </div>     
                </div>
            <!--closing tag for the large device div-->
            </div>    
            <% 
                        }
                        i++;
                    }
                }
            %>  
            </div>
            <!--Adds dynamically sized whitespace to right side of page--> 
            <div class="col-xs-1"></div>

        </article>
                
    </body>
    
</html>
