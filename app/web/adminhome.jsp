<%-- 
    Document   : adminhome
    Author     : Huiyan and Regan 
--%>

<%@page import="entity.BootstrapError"%>
<%@page import="controller.RoundController"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="adminProtect.jsp" %>
<!DOCTYPE html>


<html>
    <head>
        <title>BIOS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'>

        </script>

    </head>
    <body class="pagebg text-center container-fluid">
        <%             //String userId = (String) session.getAttribute("student");
            String firstLogin = (String) session.getAttribute("firstLogin");
            String startEndRound = (String) session.getAttribute("startEndRoundSuccess");
            if (firstLogin != null || startEndRound != null) {
        %>

        <script type="text/javascript">
            $(window).load(function () {
                $('#myModal').modal('show');
            });
        </script>

        <%  }
        %> 

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog modal-sm">

                <!— Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><%                            if (firstLogin != null) {
                                out.println("Welcome Admin! ");
                            }
                            %></h4>
                    </div>
                    <div class="modal-body">
                        <p>
                            <%
                                if (firstLogin != null) {
                                    out.println("Current Round: " + RoundController.getRound());
                                    out.println("Round status: " + RoundController.getStatus());
                                }
                                if (startEndRound != null) {
                                    String roundStatus = RoundController.getStatus();
                                    int roundNum = RoundController.getRound();
                                    if (roundStatus.equals("inactive")) {
                                        out.println("Round " + roundNum + " Stopped!");

                                    } else if (roundStatus.equals("active")) {

                                        out.println("Round " + roundNum + " Started!");

                                    }
                                }

                            %>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>


        <%  session.removeAttribute("firstLogin");
            session.removeAttribute("startEndRoundSuccess");
        %>

        <div class=' post container navbar navbar-default navbar-inverse blue'>
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="adminhome.jsp">Merlion University</a>
                </div>
                <!--<div class="collapse navbar-collapse"> -->
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
              <!--  </div>-->
            </div>
        </div>

        <div class='container text-center'>
            <div class='row-fluid'>
                <div class="span2">
                    <div class=" col-md-2">
                        <div class=" panel ">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong>User Information</strong></h3>
                            </div>

                            <div class="panel-body">
                                <%
                                    out.print("<ul class=\"list-unstyled\">");
                                    out.print("<li>Username: <b>" + "Admin" + "</b></li>");
                                    out.print("<li>Round: <b>" + RoundController.getRound() + "</b></li>");
                                    out.print("<li> Status: <b>" + RoundController.getStatus().toUpperCase() + "</b></li>");
                                    out.print("</ul>");

                                %>

                            </div>

                        </div>
                    </div>
                </div>

                <div class="span10">
                    <div class='col-md-10'> 
                        <div class="panel panel-table panel-table">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong>Bootstrap Details</strong></h3>
                            </div>
                            <div class="panel-body">
                                <table border='1' class='table table-bordered'>

                                    <thead class="thead-inverse">
                                        <tr>
                                            <th>Records Loaded</th>
                                        </tr>
                                    </thead>
                                    <%
                                        ArrayList<String> successRecords = (ArrayList<String>) request.getAttribute("successList");
                                        if (successRecords != null) {
                                            for (String str : successRecords) {
                                    %>
                                    <tr>
                                        <td>
                                            <%= str%>
                                        </td>

                                    </tr>
                                    <%
                                        }
                                        request.removeAttribute("successList");
                                    } else {

                                    %>
                                    <tr>
                                        <td>
                                            No new records loaded. 
                                        </td>

                                    </tr>

                                    <%                                            }


                                    %>
                                </table>

                                <%                              ArrayList<BootstrapError> err = (ArrayList<BootstrapError>) request.getAttribute("errorList");
                                    if (err != null) {
                                %>      <table border='1' class='table table-bordered'>
                                            <thead class="thead-inverse">
                                                <tr>
                                                    <th>File</th>
                                                    <th>Line</th>
                                                    <th>Errors</th>
                                                </tr>
                                            </thead>
                                            <%
                                                for (BootstrapError error : err) {
                                            %>
                                            <tr>
                                                <td><%=error.getFile()%>
                                                </td>
                                                <td><%=error.getLine()%>
                                                </td>
                                                <td><%=error.getErrors()%>
                                                </td>

                                            </tr>
                                            <%
                                                    }
                                                }
                                            %>
                                     </table>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <script src='js/bootstrap.js'></script>
    </body>


</html>
