<%-- 
    Author     : Aloysius Lim and Huiyan
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.Bid"%>
<%@page import="controller.SectionStudentController"%>
<%@page import="controller.RoundController"%>
<%@page import="entity.Student"%>
<%@page import="entity.Section"%>
<%@page import="controller.CourseSectionController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.StudentController"%>
<%@page import="entity.Course"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'>

        </script>

    </head>
    <body class="pagebg text-center container-fluid">
        <%            
            ArrayList<String> completedCourseList = StudentController.getCourseCompleted(userId);
            //Format the zeros in decimal
            DecimalFormat dcf = new DecimalFormat("0.00");
        %>
        <!--<div class="navbar navbar-inverse navbar-fixed-top">-->
        <div class=' post container navbar navbar-default navbar-inverse blue'>
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="studenthome.jsp">Merlion University</a>
                </div>
                <!--<div class="collapse navbar-collapse"> -->
                    <form action="StudentServlet" method="post">
                        <ul class="nav navbar-nav">
                            <li> <button class='navbar-brand btn-link' name="SearchCourse" value="yes">Add Bid </button></li>
                            <li><button class='navbar-brand btn-link' name="ViewCourse" value="yes">View Course Completed</button></li>

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

        <div class='container text-center'>
            <div class='row-fluid'>
                <div class="span2">
                    <div class=" col-md-2">
                        <div class=" panel ">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong>User Information</strong></h3>
                            </div>

                            <div class="panel-body">
                                <%                                    Student stu = StudentController.retrieveStudent(userId);
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
                </div>

                <div class="span10">
                    <div class='col-md-10'> 
                        <div class="panel panel-table panel-table">
                            <div class="panel panel-headinggrey">
                                <h3 class=" fontStyle"><strong>Course Completed</strong></h3>
                            </div>
                            <div class=" panel-body">
                                <table border='1' class='table table-bordered'>
                                    
                                    
                                    <%if (completedCourseList != null && completedCourseList.isEmpty()) {%>
                                    <tr> 
                                    <center>
                                        No Completed Course Found!</a>
                                    </center>
                                    </tr>
                                    <%} else{%>
                                    <thead class="thead-inverse">
                                        <tr>
                                            <th>
                                                Course Code
                                            </th>

                                            <th>
                                                Title
                                            </th>

                                            <th>
                                                Description
                                            </th>

                                            <th>
                                                School
                                            </th>
                                        </tr>
                                    </thead>

                                    <%
                                        if (completedCourseList != null || !completedCourseList.isEmpty()) {
                                            for (String courseId : completedCourseList) {
                                                Course c = CourseSectionController.getCourseByCourseCode(courseId);
                                                String courseTitle = c.getCourseTitle();
                                                String desc = c.getDescription();
                                                String school = c.getSchoolTitle();

                                    %>
                                    <tr>    
                                        <td> <%=courseId%> </td>
                                        <td> <%=courseTitle%> </td>
                                        <td> <%=desc%> </td>
                                        <td> <%=school%> </td>
                                    </tr>

                                    <%

                                            }
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
