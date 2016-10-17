<%-- 
    Document   : message
    Created on : Oct 6, 2016, 12:46:09 PM
    Author     : Cheryl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Drop bid message</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
           out.println((String) request.getAttribute("msg"));
        %>
    </body>
</html>
