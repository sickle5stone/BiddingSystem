<%-- 
    Document   : fileupload
    Created on : Sep 30, 2016, 5:12:28 PM
    Author     : Cheryl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%

        String userId = (String) session.getAttribute("admin"); 
        if (userId != null){
            out.print("Welcome! " + userId.toUpperCase());
        } else {
            response.sendRedirect("loginpage.jsp");
        }
     %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="BootstrapServlet" method="post" enctype="multipart/form-data" >
            Upload Zip File: <input type="file" name="pew" accept=".zip" /> <br>
            <br />
            <input type="submit" name="Submit" value="Upload" />
        </form>
    </body>
</html>

<%
   String error= (String) request.getAttribute("error");  
   if(error!=null){
       out.println(error); 
   }
   


%>
