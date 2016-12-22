<%--    
    Document   : searchSection
    Author     : ChenHuiYan and Regan
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="controller.BidController"%>
<%@page import="controller.SectionStudentController"%>
<%@page import="controller.RoundController"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Section"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.CourseSectionController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="studentProtect.jsp" %>
<% 
    String courseCode = request.getParameter("viewSections");
    CourseSectionController cs = new CourseSectionController();
    ArrayList<Section> sections = cs.getSectionsByCourse(courseCode);
    //Format the zeros in decimal
    DecimalFormat dcf = new DecimalFormat("0.00");

%>
    <head>
        <title>BIOS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
        
    </head>
    <body class="pagebg  container-fluid"> 
    <div class=' post container navbar navbar-default navbar-inverse blue'>
           
                <div class="navbar-header">
                    <a class="navbar-brand" href="studenthome.jsp">Merlion University</a>
                </div>
                <!--<div class="collapse navbar-collapse"> -->
                    <form action="StudentServlet" method="post">
                        <ul class="nav navbar-nav">
                            <li >
                                <button class='navbar-brand btn-link' name="SearchCourse" value="yes">Add Bid </button></li>
                            <li><button class='navbar-brand btn-link' name="ViewCourse" value="yes">View Course Completed</button></li>

                        </ul>
                    </form>
                     <form action="logout" method="post">
                          <ul class="nav navbar-nav navbar-right"> 
                             <li> <button class='navbar-brand btn-link'><span class="glyphicon glyphicon-log-out "></span>  Log Out </button> 
                              </li>
                          </ul>
                       </form>
           <!-- </div>-->
        </div>
       <div class='container'>
            <div class='row-fluid'>
                <div class="span12">
                        <div class='col-md-12'>  
                            <%
                                    if (sections.size() > 0) {
                            %>
                            <div class="panel panel-table panel-table">
                         
                                <div class="panel panel-headinggrey">
                                    <h3 class="fontStyle"><strong>View Sections</strong></h3>
                                </div>
                                <div class="panel-body"> 
                                    <table border='1' class='table table-bordered'>
                                        <thead class=" thead-inverse">
                                            <tr>
                                                <th>Course Code</th>
                                                <th>Section Id</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Instructor</th>
                                                <th>Venue</th>
                                                <th>Size</th>
                                                <th>Vacancies</th>
                                                <th>Minimum Bid</th>
                                                    <% if (!(RoundController.getRound() == 0 || RoundController.getStatus().equals("inactive"))) {%>
                                                <th>Bid</th>
                                                    <%} %>

                                            </tr>
                                        </thead>

                                       <%
                                            for (Section s : sections) {

                                                String sectionId = s.getSection();
                                                //out.println(sectionId); 
                                                int day = s.getDayOfWeek();
                                                Date startTime = s.getStartTime();
                                                Date endTime = s.getEndTime();
                                                String instructor = s.getInstructor();
                                                String venue = s.getVenue();
                                                int size = s.getClassSize();
                                                int vacancies = size - SectionStudentController.getNumEnrolledInSection(courseCode, sectionId);
                                                double minPrice = BidController.getMinimumBid(courseCode, sectionId);

                                        %>

                                      <tr>
                                            <td> <%=courseCode%> </td>
                                            <td> <%=sectionId%> </td>
                                            <td> <%=day%> </td>
                                            <td> <%=startTime%> </td>
                                            <td> <%=endTime%> </td>
                                            <td> <%=instructor%> </td>
                                            <td> <%=venue%> </td>
                                            <td> <%=size%>  </td>
                                            <td> <%=vacancies%> </td>
                                            <td> <%=dcf.format(minPrice)%> </td>
                                     

                                            <% if (!(RoundController.getRound() == 0 || RoundController.getStatus().equals("inactive"))) {%>
                                            <td>
                                                <form action="addBidServlet" method="post">
                                                    Bid Amount:
                                                    <input type="number" step="0.01" name="amount" required />
                                                    <button class="btn-danger btn " name="viewSections" value="confirmBid">Bid</button>
                                                    <input type="hidden" name="courseCode" value="<%=courseCode%>">
                                                    <input type="hidden" name="sectionCode" value="<%=sectionId%>">
                                                </form>
                                            </td>
                                            <%}%>


                                        </tr>
                                        <% }
                                        %> 
                                    </table>
                                    <%
                                    } else {

                                        out.print("No sections under the course");
                                    %>
                                    <i class='glyphicon glyphicon-heart'></i>
                                    <form action="searchCourse.jsp" method="post">
                                        <button class='btn btn-lg btn-primary'>Back</button> 
                                    </form>
                                    <%
                                        }
                                    %>
                            </div> <!-- end div panel -->
                        </div>

                    </div>
                </div>
            </div>
       </div>


    </body>


</html>
