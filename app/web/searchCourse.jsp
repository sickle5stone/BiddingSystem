<%-- 
    Document   : studenthome 
    Author     : Huiyan and Aloysius 
--%>

<%@page import="dao.CourseDAO"%> 
<%@page import="java.util.Date"%>
<%@page import="entity.Course"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.CourseSectionController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="studentProtect.jsp" %>

<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->



<html>
    <% 
        ArrayList<Course> coursesByTitle = new ArrayList<>();
        String submitCourse = request.getParameter("searchCourse");
        String inputCourseTitle = request.getParameter("CourseTitle");
        String inputCourseCode = request.getParameter("CourseCode");
        String inputSchool = request.getParameter("school");

        if (submitCourse != null) {
            if (inputCourseTitle == null) {
                inputCourseTitle = "";
            }

            if (inputCourseCode == null) {
                inputCourseCode = "";
            }

            if (inputSchool == null) {
                inputSchool = "";
            }
        }

        CourseSectionController cs = new CourseSectionController();
        coursesByTitle = cs.getCourses(inputCourseTitle, inputCourseCode, inputSchool);

        String viewDetails = request.getParameter("viewSections");


    %>
    <head>
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
                      <a class="navbar-brand" href="studenthome.jsp">Merlion University</a>
                    </div>
                   <!-- <div class="collapse navbar-collapse"> -->
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
        </div>
       <div class='container'>
            <div class='row-fluid'>
                <div class="span12">
                        <div class='col-md-12'> 
                                <div class="panel panel-group">
                                <div class="panel panel-headinggrey">
                                    <h3 class=" fontStyle"><strong>Bid Search</strong></h3>
                                </div>
                                <div class="panel panel-body">
                                    <form class="form-horizontal form-group" action="searchCourse.jsp" method="POST">
                                        <div class="form-group">
                                            <div class="col-sm-4" >
                                            <label>Course Title:</label>
                                            <input type="text" class="form-control" name="CourseTitle">
                                          </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-4" >
                                            <label>Course Code:</label>
                                            <input type="text" class="form-control" name="CourseCode">
                                            </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-4" >
                                                 <label>School:</label>
                                                <input type="text" class="form-control" name="School">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-4" >
                                            <br>
                                            <button class="btn btn-success" name="searchCourse" type="submit"> <span class="form-group-addon"><i class="glyphicon glyphicon-search"></i></span> Search</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                </div>
                                    <%  if (coursesByTitle.size() > 0) {
                                    %>
                            
                        </div>

                        <div class='col-md-12'>   
                            <div class="panel panel-table panel-table">
                                <div class="panel panel-headinggrey">
                                    <h3 class=" fontStyle"><strong>Search Result</strong></h3>
                                </div>
                                <div class="panel panel-body"> 
                                    <table border='1' class='table table-bordered'>
                                        <thead class=" thead-inverse">
                                            <tr>
                                                <th>Course Code</th>
                                                <th>School offered</th>
                                                <th>Course Title </th>
                                                <th>Course Description</th>
                                                <th>Exam Date </th>
                                                <th>Exam Start Time </th>
                                                <th> Exam End Time</th>
                                                <th>Sections</th>
                                            </tr>
                                        </thead>

                                        <%
                                            for (Course c : coursesByTitle) {
                                                String courseCode = c.getCourseCode();
                                                String schoolTitle = c.getSchoolTitle();
                                                String courseTitle = c.getCourseTitle();
                                                String courseDesc = c.getDescription();
                                                Date examDate = c.getExamDate();
                                                Date examStart = c.getExamStart();
                                                Date examEnd = c.getExamEnd();
                                        %>

                                        <tr>
                                            <td> <%=courseCode%> </td>
                                            <td> <%=schoolTitle%> </td>
                                            <td> <%=courseTitle%> </td>
                                            <td> <%=courseDesc%> </td>
                                            <td> <%=examDate%> </td>
                                            <td> <%=examStart%> </td>
                                            <td> <%=examEnd%> </td>
                                            <td>
                                                <form action="searchSection.jsp" method="post">
                                                    <button class="btn panel-warning"name="viewSections" value="<%=courseCode%>"> View</br> Sections </button>
                                                </form>
                                            </td>
                                        </tr>
                                        <% }
                                        %> 
                                    </table>
                                <% }
                                %> 
                            </div> <!-- end div panel -->
                        </div>

                    </div>
                </div>
            </div>
       </div>


    </body>


</html>
