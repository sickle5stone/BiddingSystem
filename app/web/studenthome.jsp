<%@page import="controller.RoundController"%>
<%@page import="controller.SectionStudentController"%>
<%@page import="controller.StudentController"%>
<%@page import="entity.Student"%>
<%@page import="entity.Bid"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.BidController"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%-- 
    Document   : studenthome 
    Author     : Huiyan and Aloysius 
--%>

<html>
    <head>
        <title>BOSS</title>
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

                String bidSuccessMessage = (String) session.getAttribute("bidSuccessMessage");
                ArrayList<String> bidFailMessages = (ArrayList<String>) session.getAttribute("bidFailMessages");

                if (bidFailMessages != null) {
                    out.println("Bid Fail!");
        %>
        <br>
        <%
            for (String error : bidFailMessages) {
                out.println(error);
        %>

        <br>   

        <%
                }

            } else if (bidSuccessMessage != null) {
                out.println(bidSuccessMessage);
            }
            session.removeAttribute("bidSuccessMessage");
            session.removeAttribute("bidFailMessages");

            BidController bd = new BidController();
            ArrayList<Bid> bids = bd.getBidsByStudent(userId);
            ArrayList<Bid> sectionList = SectionStudentController.getSectionsByStudentId(userId);


        %> 

        
        
        
            
            
             
        
        <div class="navbar">
            <div class="container form-inline">
                <a href='/app/studenthome.jsp' class='navbar-brand'>
                <img src="./images/test2.png" alt="Image" id="logo" class="img-responsive pull-left" />
                </a>
            </div>
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
                                <%
                                    Student stu = StudentController.retrieveStudent(userId);
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
                    <div class='col-md-10'> 
                        <% if (RoundController.getStatus().equals("active")) { %>

                        <!-- <h1>Current Bids</h1> 
                        <p class='lead'> -->
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
                                        <td><% out.println(courseCode); %></td>
                                        <td><% out.println(sectionCode); %></td>
                                        <td><% out.println(bidAmount); %></td>
                                        <td><% out.println(minBid); %></td>
                                        <td><% out.println(status); %></td>
                                        <td>
                                            <!--   <form action="StudentServlet" method="post">  -->
                                            <form action="DeleteBidServlet" method="post">
                                                <button class='btn' name="dropBid" value="confirmDrop"> Drop 
                                                    <input type="hidden" name="bidAmt" value="<%out.print(bidAmount);%>">
                                                    <input type="hidden" name="courseId" value="<%out.print(courseCode);%>">
                                                    <input type="hidden" name="sectionId" value="<%out.print(sectionCode);%>">
                                                </button>
                                            </form>
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




                    <div class='col-md-10 '>   

                        <div class="panel panel-primary panel-table">
                            <div class="panel-heading">
                                <h3 class="panel-title">Enrollment</h3>
                            </div>
                            <div class="panel-body"> 

                                <table border='1' class='table table-bordered '>
                                    <tr>
                                        <th>Course Code</th>
                                        <th>Section Id</th>
                                        <th>Bid Amount</th>
                                        <th>Status</th>
                                    </tr>

                                    <%
                                        if (sectionList != null || !sectionList.isEmpty()) {
                                            for (Bid section : sectionList) {

                                                String SECcourseCode = null;
                                                String SECsectionCode = null;
                                                double SECbidAmount = 0.0;
                                                String SECstatus = null;
                                                SECcourseCode = section.getCourseCode();
                                                SECsectionCode = section.getSectionCode();
                                                SECbidAmount = section.getBidAmount();
                                                SECstatus = section.getBidStatus();

                                    %>

                                    <tr>
                                        <td> <%out.println(SECcourseCode);%> </td>
                                        <td> <%out.println(SECsectionCode);%> </td>
                                        <td> <%out.println(SECbidAmount);%> </td>
                                        <td> <%out.println(SECstatus);%> </td>
                                        <td>
                                            <form action="dropSectionServlet" method="post">
                                                <button class='btn' name="dropSection" value="confirmDrop"> Drop 
                                                    <input type="hidden" name="bidAmt" value="<%out.print(SECbidAmount);%>">
                                                    <input type="hidden" name="courseId" value="<%out.print(SECcourseCode);%>">
                                                    <input type="hidden" name="sectionId" value="<%out.print(SECsectionCode);%>">
                                                </button>
                                            </form>
                                        </td>
                                    <tr/>
                                </table>
                            </div> <!-- end div body -->
                        </div> <!-- end div panel -->

                        <% }
                            }//end bid loop 

                        } else{%>  
                        <h1>Bidding Results</h1> 
                        <p class='lead'> 
                        <table border='1' class='table table-bordered'>
                            <tr>
                                <th>Course Code</th>
                                <th>Section Id</th>
                                <th>Bid Amount</th>
                                <th>Minimum Bid</th>
                                <th>Status</th>
                            </tr>
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
                                <td><% out.println(courseCode); %></td>
                                <td><% out.println(sectionCode); %></td>
                                <td><% out.println(bidAmount); %></td>
                                <td><% out.println(minBid); %></td>
                                <td><% out.println(status); %></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </table>
                    </div>

                </div>
                <%}
                            }%> 



                </table>

            </div>

        </div>



    </div>




    <div class='col-md-10'></div>



</div>

<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>
<script src='js/bootstrap.js'></script>
</body>


</html>
