<%-- 
    Document   : footer
    Created on : 28-Sep-2016, 17:26:35
    Author     : Big Cheesy B
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    
    <footer>
        <ul>
            <li class="footer"><a href="/Instagrim">Home</a></li>
                <%
                    LoggedIn footerLG= (LoggedIn) session.getAttribute("LoggedIn");
                    if (footerLG != null){                        
                %>
            <li>Log Out</li>
                <%
                    }
                %>
            <li>&COPY; Andy C</li>
        </ul>
    </footer>
            
</html>
