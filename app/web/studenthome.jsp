<%--      
    Document   : studenthome 
    Author     : Huiyan and Aloysius 
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.Section"%> 
<%@page import="controller.CourseSectionController"%>
<%@page import="controller.RoundController"%>
<%@page import="controller.SectionStudentController"%>
<%@page import="controller.StudentController"%>
<%@page import="entity.Student"%>
<%@page import="entity.Bid"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.BidController"%>
<%@include file="studentProtect.jsp" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->



<html>
    <head>
        <title>BIOS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
        <script src='js/jquery.min.js'></script>
         
    </head>
    <body class="pagebg text-center container-fluid">
         <!--<div class="navbar navbar-inverse navbar-fixed-top">-->
            <div  role="navigation" class=' post container  navbar navbar-default navbar-inverse blue'>   
            <div class="navbar-header">
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
           <a class="navbar-brand" href="studenthome.jsp">Merlion University</a>
        </div>
                   <div id="navbarCollapse" class="collapse navbar-collapse">
                      <form action="StudentServlet" method="post">
                    <ul class="nav navbar-nav">
                      <li ><button class='navbar-brand btn-link' name="SearchCourse" value="yes">Add Bid </button></li>
                      <button class='navbar-brand btn-link' name="ViewCourse" value="yes">View Course Completed</button> 
                    </ul>
                      </form>
                       <form action="logout" method="post">
                          <ul class="nav navbar-nav navbar-right"> 
                             <li> <button class='navbar-brand btn-link'><span class="glyphicon glyphicon-log-out "></span>  Log Out </button> 
                              </li>
                          </ul>
                       </form>
                 </div>
                </div>
           
                        
        <%
            String firstLogin = (String) session.getAttribute("firstLogin");
            if (userId != null) {
               
                String bidSuccessMessage = (String) session.getAttribute("bidSuccessMessage");
                ArrayList<String> bidFailMessages = (ArrayList<String>) session.getAttribute("bidFailMessages");
                String bidUpdateMessage = (String) session.getAttribute("bidUpdateMessage");

                if (bidFailMessages != null || bidSuccessMessage != null || firstLogin != null || bidUpdateMessage != null) {

        %>

        <script type="text/javascript">
            $(window).load(function () {
                $('#myModal').modal('show');
            });
        </script>

        <%     }

            BidController bd = new BidController();
            ArrayList<Bid> bids = bd.getBidsByStudent(userId);
            ArrayList<Bid> sectionList = SectionStudentController.getSectionsByStudentId(userId);
            Student stu = StudentController.retrieveStudent(userId);
            String e = "";
            //Format the zeros in decimal
            DecimalFormat dcf = new DecimalFormat("0.00");

        %>  
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog modal-sm">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><%                            if (bidFailMessages != null) {
                                out.println("Bid Failed!");
                                e = bidFailMessages.toString();
                            } else if (bidSuccessMessage != null) {

                                out.println("Bid Successful!");
                            } else if (firstLogin != null) {
                                out.println("Welcome " + stu.getName());
                            } else if (bidUpdateMessage != null){
                                out.println("Update Successful!");
                            }
                            %></h4>
                    </div>
                    <div class="modal-body">
                        <p>
                            <%
                                if (bidFailMessages != null) {
                                    for (String s : bidFailMessages) {
                                        out.println("<div class=\"alert alert-danger\">");
                                        out.println(s);
                                        out.println("</div>");
                                    }
                                } else if (bidSuccessMessage != null) {
                                    double balance = StudentController.getEdollarBalance(userId);
                                    out.println("<div class=\"alert alert-success\">");
                                    out.println("Bid has been added successfully!");

                                    out.println("</div>");

                                    out.println("<div class=\"alert alert-success\">");
                                    out.println("Remaining E$ Balance is : <b>" + dcf.format(balance) + "</b>");
                                    out.println("</div>");

                                } else if (firstLogin != null) {
                                    out.println("WELCOME !");
                                    out.println("Edollar Balance: " + dcf.format(stu.geteDollar()));
                                } else if (bidUpdateMessage != null) {
                                    double balance = StudentController.getEdollarBalance(userId);
                                    out.println("<div class=\"alert alert-success\">");
                                    out.println("Update Successful!");
                                    out.println("</div>");
                                    out.println("<div class=\"alert alert-success\">");
                                    out.println("Remaining E$ Balance is : <b>" + dcf.format(balance) + "</b>");
                                    out.println("</div>");
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

        <%            
            session.removeAttribute("bidSuccessMessage");
            session.removeAttribute("bidFailMessages");
            session.removeAttribute("firstLogin");
            session.removeAttribute("bidUpdateMessage");
        %>

        <div class='container text-center'>
            <div class='row-fluid'>
               <div class=" col-md-2">
                        <div class=" panel ">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong>User Information</strong></h3>
                        </div>
                            
                            <div class=" panel-body">
                                <%
                                    if (stu != null) {
                                        String sch = stu.getSchool();
                                        double eDollar = stu.geteDollar();
                                        out.print("<ul class=\"list-unstyled\">");
                                        out.print("<li>Username: <b>" + userId + "</b></li>");
                                        out.print("<li>School: <b>" + sch + "</b></li>");
                                        out.print("<li>EDollar: <b>" + dcf.format(eDollar) + "</b></li>");
                                        out.print("<li>Round: <b>" + RoundController.getRound() + "</b></li>");
                                        out.print("<li> Status: <b>" + RoundController.getStatus().toUpperCase() + "</b></li>");
                                        out.print("</ul>");
                                    }
                                %>
                            </div>
                            
                        </div>
                   
                </div>
                
                <div class="span10">
                    <div class='col-md-10'> 
                   
                        <% String tableName="";
                            
                            
                            if (RoundController.getStatus().equals("active")){
                                 tableName="Current Bids";
                                
                            }else{
                                 tableName="Bidding Results";
                            }
                               
                            
                            
                        
                        
                        
                        %>

                        <div class="panel panel-table panel-table">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong><%= tableName %></strong></h3>
                            </div>
                            <div class=" panel-body">
                                <table border='1' class='table table-bordered'>
                                    <%
                                        if (bids != null && bids.isEmpty()) {
                                    %>
                                    <tr> 
                                    <center>
                                        No Bids Found! Start Bidding <a href="\app\searchCourse.jsp"> here!</a>
                                    </center>
                                    </tr>
                                    <%
                                    } else if(bids!=null && (!bids.isEmpty())){
                                    %>

                                    <thead class="thead-inverse">
                                        <tr>
                                            <th>Course Code</th>
                                            <th>Section Id</th>
                                            <th>Bid Amount</th>
                                            <th>Minimum Bid</th>
                                            <th>Vacancies</th>
                                            <th>Status</th>
                                            <!--Only if round is active, adds in header so as to shop the drop part-->
                                        <%  if(RoundController.getStatus().equals("active")){   %>
                                             <th></th>
                                        
                                       <%
                                        }
                                       %>
                                           
                                          
                                        </tr>
                                    </thead>
                                    <%
                                        if (bids != null) {
                                            for (Bid b : bids) {
                                                String courseCode = b.getCourseCode();
                                                String sectionCode = b.getSectionCode();
                                                double bidAmount = b.getBidAmount();
                                                String status = b.getBidStatus();
                                                Section s = CourseSectionController.getSection(courseCode, sectionCode);
                                                double minBid = BidController.getMinimumBid(courseCode, sectionCode);
                                                int size = s.getClassSize();
                                                int vacancies = size - SectionStudentController.getNumEnrolledInSection(courseCode, sectionCode);
                                    %>
                                    <tr>
                                        <td><% out.println(courseCode); %></td>
                                        <td><% out.println(sectionCode); %></td>
                                        <td><% out.println(dcf.format(bidAmount)); %></td>
                                        <td><% out.println(dcf.format(minBid)); %></td>
                                        <td><% out.println(vacancies); %></td>
                                        <td><% out.println(status); %></td>
                                        <%  if(RoundController.getStatus().equals("active")){   %>
                                        <td>
                                            <!--   <form action="StudentServlet" method="post">  -->
                                            <form action="DeleteBidServlet" method="post">
                                                <button class='btn' name="dropBid" value="confirmDrop">Drop 
                                                    <input type="hidden" name="bidAmt" value="<%out.print(bidAmount);%>">
                                                    <input type="hidden" name="courseId" value="<%out.print(courseCode);%>">
                                                    <input type="hidden" name="sectionId" value="<%out.print(sectionCode);%>">
                                                </button>
                                            </form>
                                        </td>
                                        <%
                                        }
                                        %>
                                    </tr>
                                    <%
                                            }
                                        }
                                    }
                        
                                    %>
                                </table>
                            
                            </div>
                        </div>
                   
                    
                    
                        <div class="panel panel-table panel-table">
                            <div class="panel-headinggrey">
                                <h3 class=" fontStyle"><strong>Enrollment</strong></h3>
                            </div>
                            <div class="panel-body"> 
                                <table border='1' class='table table-bordered'>
                                    <%if (sectionList != null && sectionList.isEmpty()) {%>
                                    <tr> 
                                    <center>
                                        No Enrolled Classes Found!</a>
                                    </center>
                                    </tr>
                                    <%} else{%>
                                    <thead class=" thead-inverse">
                                        <tr>
                                            <th>Course Code</th>
                                            <th>Section Id</th>
                                            <th>Bid Amount</th>
                                            <th>Status</th>
                                            <%  if(RoundController.getStatus().equals("active")){   %>
                                            <th> </th>
                                            <% } %> 
                                        </tr>
                                    </thead>

                                    <%
                                        if (sectionList != null) {
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
                                        <td><%=SECcourseCode%></td>
                                        <td><%=SECsectionCode%> </td>
                                        <td><%=dcf.format(SECbidAmount)%> </td>
                                        <td>enrolled</td>
                                        <%  if(RoundController.getStatus().equals("active")){    %>
                                        <td>
                                            <form action="dropSectionServlet" method="post">
                                                <button class='btn' name="dropSection" value="confirmDrop"> Drop 
                                                    <input type="hidden" name="bidAmt" value="<%out.print(SECbidAmount);%>">
                                                    <input type="hidden" name="courseId" value="<%out.print(SECcourseCode);%>">
                                                    <input type="hidden" name="sectionId" value="<%out.print(SECsectionCode);%>">
                                                </button>
                                            </form>
                                        </td>
                                        <%  } %>
                                    <tr/>
                                    <%      }
                                        }
                                    }//end bid loop else{%> 
                                </table>

                            </div> <!-- end div body -->
                        </div> <!-- end div panel -->
                    </div>

                  

                </div>
                <%
                    }
                %> 

            </div>

        </div>
       
        <script src='js/bootstrap.js'></script>
    </body>


</html>
