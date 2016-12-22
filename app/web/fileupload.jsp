<%-- 
    Document   : fileupload
    Created on : Sep 30, 2016, 5:12:28 PM
    Author     : Cheryl, Aloysius
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="adminProtect.jsp" %>
 <html>
     <head>
        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
        <title>BIOS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
        
    </head>
    <body class="pagebg  container-fluid"> 
        <!--<div class="navbar navbar-inverse navbar-fixed-top">-->
               <div class=' post container navbar navbar-default navbar-inverse blue'>
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="adminhome.jsp">Merlion University</a>
                </div>
              <!--  <div class="collapse navbar-collapse"> -->
                    <form action="adminServlet" method="post">
                        <ul class="nav navbar-nav">
                            <li >
                                <button class='navbar-brand btn-link' name="uploadFile" value="uploadFile">Upload File </button></li>
                            <li><button class='navbar-brand btn-link' name="StartEndRound" value="startEndRound">Start/End Round</button></li>
                        

                        </ul>
                    </form>
                     <form action="logout" method="post">
                          <ul class="nav navbar-nav navbar-right"> 
                             <li> <button class='navbar-brand btn-link'><span class="glyphicon glyphicon-log-out "></span>  Log Out </button> 
                              </li>
                          </ul>
                       </form>
                </div>
           <!-- </div>-->
        </div>

       <div class='container'>
            <div class='row-fluid'>
                <div class="span12">
                        <div class='col-md-12'> 
                           <div class="panel panel-group">
                                <div class="panel panel-headinggrey">
                                    <h3 class=" fontStyle"><strong><%out.print("Welcome " + userId.toUpperCase()+" !");%></strong></h3>
                                </div>
                                <div class="panel panel-body">
                                    <form class="form-horizontal form-group" action="BootstrapServlet" method="post" enctype="multipart/form-data" >
                                        <div class="form-group">
                                            <div class="col-sm-4" >
                                                 <label>Upload Zip File:</label>
                                                 <br>
                                                 <label class="custom-file">
                                                    <input type="file" name="bootstrap-file" accept=".zip" class="custom-file-input"/>
                                                 </label>
                                            </div>
                                        </div> 
                                        <!--input type='text' name='token' value='eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTQ3Nzg5NjQ0NCwiaWF0IjoxNDc3ODkyODQ0fQ.Mm2n-1ZZJUN2dI9iT0Am18KHxrXumHfvuphNEciqA-c' />
                                        -->
                                      
                                            
                                            <button class="btn btn-success" name="Submit" type="submit"> <span class="form-group-addon"><i class="glyphicon glyphicon-upload"></i></span>Upload</button>
                                            
                                      
                                    </form>
                                </div>
                                  
                            </div>
                        </div>
                </div>
            </div>
       </div>
    </body>
</html>
