<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <%@include file="head.jsp" %>
    
    <body>
        
        <%@include file="header.jsp" %>
                
        <article>
            
            <h3>File Upload</h3>
            
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile">
                <br/>
                Privacy: <select name="Privacy">
                    <option value ="Public">Public</option>
                    <option value="Private">Private</option>
                </select>
                
                <div class="helptip">
                    ?
                    <p>This field determines </br> whether or not your </br> Images are displayed </br> on the front page of </br> Instagrim</p>
                </div>

                <br/><br/>
                <input type="submit" value="Upload"> to upload the file!
            </form>

        </article>
        
    </body>
    
</html>
