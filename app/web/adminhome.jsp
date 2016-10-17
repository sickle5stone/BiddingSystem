<%-- 
    Document   : adminhome
    Created on : Oct 5, 2016
    Author     : Huiyan and Regan 
--%>

<%@page import="controller.RoundController"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
<html>
    
    <%

        String userId = (String) session.getAttribute("admin"); 
        if (userId != null){
            out.print("Welcome! " + userId.toUpperCase());
        } else {
            response.sendRedirect("loginpage.jsp");
        }
     %> 
 <%
      ArrayList <String> err = (ArrayList<String>)request.getAttribute("errorList");
      if (err != null){
          for (String error : err){
              out.print(error + "<br>");
          }
      }
 %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Homepage</title>
    </head>
    <body>
        <h1>Admin Home</h1>

          <div class="col-md-2">
            <div class="panel panel-primary ">
            <div class="panel-heading">
              <h3 class="panel-title">Round Information</h3>
            </div>
            <div class="panel-body">
                Round : <%=RoundController.getRound()%> <br>
                Status : <%=RoundController.getStatus()%> <br>
            </div></div></div>
        
        <form action="adminServlet" method="post">    
           
            <button type="submit" class="btn btn-lg btn-info" name="uploadFile" value="uploadFile">Upload File</button>
            
            <br>
      
            <button type="submit" class="btn btn-lg btn-info" name="StartEndRound" value="startEndRound">Start/End Round</button>
            
        </form>           
        
        <form action="logout" method="post">
            <input type="submit" value="Logout" class="btn btn-lg btn-warning" />
        </form>
    </body>
</html>
