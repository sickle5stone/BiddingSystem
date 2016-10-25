<%-- 
    Document   : updateBid

    Author     : Aloysius & Huiyan
--%>

<%@page import="controller.SectionStudentController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.StudentController"%>
<%@page import="entity.Student"%>
<%@page import="controller.RoundController"%>
<%@page import="entity.Bid"%>
<%@page import="controller.BidController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>BIOS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>


    </head>
    <body class="body">

        <%
            String userId = (String) session.getAttribute("student");
            if (userId == null) {
                response.sendRedirect("loginpage.jsp");   //check whether the user has loged in 
            } else {

                
                String errorMsg = (String) request.getAttribute("error");
                if (errorMsg != null){
                    out.println(errorMsg);
                }
                
               
            

            BidController bd = new BidController();
            ArrayList<Bid> bids = bd.getBidsByStudent(userId);
            ArrayList<Bid> sectionList = SectionStudentController.getSectionsByStudentId(userId);


        %> 








        <div class="navbar">
            <div class="container form-inline">
                <a href='/app/studenthome.jsp' class='navbar-brand'>
                    <img src="./images/logo4.png" alt="Image" id="logo" class="img-responsive pull-left" />
                </a>
            </div>

            <ul class='navbar-right'> 
                <form action="logout" method="post">
                    <input type="submit" value="Logout" class="btn btn-lg btn-warning" />
                </form>  
            </ul>
        </div>


        <div class='container-fluid'>

            <div class='row-fluid'>

                <div class="span2">

                    <div class="col-md-2">
                        <div class="panel panel-primary ">
                            <div class="panel-heading">
                                <h3 class="panel-title">User Information</h3>
                            </div>
                            <div class="panel-body">
                                <%                                    Student stu = StudentController.retrieveStudent(userId);
                                    if (stu != null) {
                                        String sch = stu.getSchool();
                                        double eDollar = stu.geteDollar();
                                        out.print("<br>Username: " + userId);
                                        out.print("<br>School: " + sch);
                                        out.print("<br>EDollar: " + eDollar);
                                        out.print("<br>Round: " + RoundController.getRound());
                                    }
                                %>
                            </div>
                        </div>

                        <i class='glyphicon glyphicon-heart'></i>
                        <form action="StudentServlet" name="viewCourse" method="post">
                            <button class='btn btn-lg btn-primary' name="ViewCourse" value="yes">View Completed <br> Courses </button> 
                        </form>

                        <i class='glyphicon glyphicon-equalizer'></i>
                        <form action="StudentServlet" method="post">
                            <button class='btn btn-lg btn-primary'  name="SearchCourse" value="yes">Search Course  </button> 
                        </form>

                        <i class='glyphicon glyphicon-equalizer'></i>
                        <form action="StudentServlet" method="post">
                            <button class='btn btn-lg btn-primary'  name="updateBids" value="confirmUpdate">Update Bids </button> 
                        </form>    
                    </div>
                    <div class='col-md-2'>  

                    </div>  
                </div>


                <div class="span10">
                    <div class='col-md-10'/> 
                    

                    <!-- <h1>Current Bids</h1> 
                    <p class='lead'> -->
                    <div class='col-md-10'> 
                        <div class="panel panel-primary panel-table">
                            <div class="panel-heading">
                                <h3 class="panel-title">Current Bids</h3>
                            </div>
                            <div class="panel-body">
                                <table border='1' class='table table-bordered'>
                                    <thead class="thead-inverse">
                                        <tr>
                                            <th>Course Code</th>
                                            <th>Section Id</th>
                                            <th>Bid Amount</th>
                                            <th>Minimum Bid</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <%
                                        if (bids != null || !bids.isEmpty()) {
                                            for (Bid b : bids) {
                                                String courseCode = b.getCourseCode();
                                                String sectionCode = b.getSectionCode();
                                                double bidAmount = b.getBidAmount();
                                                String status = b.getBidStatus();
                                                double minBid = BidController.getMinimumBid(courseCode, sectionCode);
                                    %>
                                    <tr>
                                    <form action="updateBid" method="POST">
                                        <td><% out.println(courseCode); %></td>
                                        <td><% out.println(sectionCode); %></td>
                                        <td><input type="number" step="0.01" name="newAmt" value="<%=bidAmount%>"</td>
                                        <td><% out.println(minBid); %></td>
                                        <td><% out.println(status); %></td>
                                        <td>
                                            <input type="submit" value="Update" class="btn">
                                            <input type="hidden" name="courseId" value="<%out.print(courseCode);%>">
                                            <input type="hidden" name="sectionId" value="<%out.print(sectionCode);%>">
                                            <input type="hidden" name="originalBid" value="<%out.print(bidAmount);%>">
                                        </td>
                                    </tr>
                                    </form>
                                        
                                       
                                    <%
                                            }
                                        }
                                    %>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
                <%
                    }%> 




            </div>

        </div>



        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>
        <script src='js/bootstrap.js'></script>
    </body>


</html>
