<%--    
    Document   : searchSection
    Created on : 8 Oct, 2016, 1:31:09 PM
    Author     : ChenHuiYan
--%>

<%@page import="controller.BidController"%>
<%@page import="controller.SectionStudentController"%>
<%@page import="controller.RoundController"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Section"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.CourseSectionController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
        String courseCode = request.getParameter("viewSections");
        CourseSectionController cs = new CourseSectionController();
        ArrayList<Section> sections = cs.getSectionsByCourse(courseCode);
        /*  ArrayList<String> bidFailMessages= (ArrayList<String>) session.getAttribute("bidFailMessages");
      if(bidFailMessages!=null){
          for(String error: bidFailMessages){
              out.println(error);
              out.println(sections.size());
          }
      }  */
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='css/bootstrap.css' rel='stylesheet'>
    </head>
    <body>
        <div class='container navbar navbar-default'>


            <ul class='nav navbar-nav navbar-right'> 

                <li><a href='studenthome.jsp'>Home Page</a></li>  
            </ul>
        </div>

        <div class='container'>

            <div class='row'>

                <div class='col-md-6'>   
                    <%
                        if (sections.size() > 0) {
                    %>
                    <table border="0" class='lead table table-striped'>
                        <thead class="">
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
                            <td> <%=minPrice%> </td>


                            <% if (!(RoundController.getRound() == 0 || RoundController.getStatus().equals("inactive"))) {%>
                            <td>
                                <form action="addBidServlet" method="post">
                                    Bid Amount:
                                    <input type="number" step="0.01" name="amount" required />
                                    <button name="viewSections" value="confirmBid"> Bid </button>
                                    <input type="hidden" name="courseCode" value="<%=courseCode%>">
                                    <input type="hidden" name="sectionCode" value="<%=sectionId%>">
                                </form>
                            </td>
                            <%}%>


                        </tr>
                        <%
                            }
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
                    
                    

                </div>
            </div>
        </div>



    </body>
</html>
