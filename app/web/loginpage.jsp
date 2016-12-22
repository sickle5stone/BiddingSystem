<%-- 
    Document   : loginpage
    Author     : Cheryl, Aloysius, Haseena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>BIOS Data</title>

    <!-- Bootstrap core CSS -->
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/custom.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="./css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="../../assets/js/ie-emulation-modes-warning.js"></script>

     
  </head>
  <body class="container text-center col-sm-3">
    <div style="width: 700px" class="col-md-offset-12 " role="alert" style="text-align: center;">
            </div>
<%
    String error = (String) request.getAttribute("loginError");
     if (error != null){
%>
        
                    <div class="top-content"> 
    <div style="width: 700px" class="red panel container col-md-offset-12 " role="alert" style="text-align: center;">
        <strong>
      
        <%
                out.print(error);%>
         
        </strong> 
    </div>
    <%}        %>    
    <div class="wrapper  col-md-offset-6">
    <div class="container text-center panel-body">    
        <div id="loginbox"  class="container-fluid text-center transparent col-md-4 col-md-offset-3 ">                    
            <div class="transparent container-fluid text-center panel-info" >
                       
                    <div class="panel-body " >
                       <form class="container-fluid form-horizontal form-group-lg" action="LoginServlet" method="POST">
                                    
                            <div style="margin-bottom: 15px" class="input-group">
                                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                       <input type="username" name="userId" class="form-control" placeholder="Username" required autofocus>
                                    </div>
                                
                            <div style="margin-bottom: 15px" class="input-group">
                                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                        <input type="password"  name="password" class="form-control" placeholder="Password" required autofocus>
                                    </div>

                            <div style="margin-bottom: 15px" class="input-group">    
                                  <span class="input-group-addon"><i class="glyphicon glyphicon-education"></i></span>
                           <select class="form-control" name="userType"> 
                    <option value="student">Student</option>
                    <option value="admin">Staff</option>
            </select>       
                        </div>   
                                <div style="margin-top:10px" class="form-group">
                                    <!-- Button -->

                                    <div class="col-sm-12 controls">
                                    
                                     <button class="btn btn-lg btn-success btn-block" type="submit"> <span class="form-group-addon"><i class="glyphicon glyphicon-log-in"></i></span> Log in</button>

                                    </div>
                                </div>
                            </form>     
                        </div>                     
                    </div>  
                </div>
            </div>
        </div>
                    </div>
     
</body>
</html>
