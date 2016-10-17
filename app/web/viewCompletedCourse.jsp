<%-- 
    Author     : Aloysius Lim 
--%>

<%@page import="controller.RoundController"%>
<%@page import="entity.Student"%>
<%@page import="entity.Section"%>
<%@page import="controller.CourseSectionController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.StudentController"%>
<%@page import="entity.Course"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body class="body">

        <link href='css/bootstrap.css' rel='stylesheet'>
        <link href='css/custom.css' rel='stylesheet'>
  

            <%
                String userId = (String) session.getAttribute("student");
                if (userId == null) {
                    response.sendRedirect("loginpage.jsp");   //check whether the user has loged in 
                } else {

                    ArrayList<String> completedCourseList = StudentController.getCourseCompleted(userId);


            %>

            <div class="navbar">
                <div class="container form-inline">
                    <a href='/app/studenthome.jsp' class='navbar-brand'>
                        <img src="./images/test2.png" alt="Image" id="logo" class="img-responsive pull-left" />
                    </a>
                </div>
            </div>

            <div class="col-md-2">
                <div class="panel panel-primary ">
                    <div class="panel-heading">
                        <h3 class="panel-title">User Information</h3>
                    </div>
                    <div class="panel-body">
                        <%                        Student stu = StudentController.retrieveStudent(userId);
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
                    <form action="studenthome.jsp" method="post">
                        <button class='btn btn-lg btn-primary'>Back</button> 
                    </form>
                    
            </div>

                    
                    
            <div class="span10">
                <div class="col-md-10">
                    <div class="panel panel-primary panel-table">
                        <div class="panel-heading">
                            <h3 class="panel-title">Course Completed</h3>
                        </div>
                        <div class="panel-body"> 

                            <table border='1' class='table table-bordered '>
                                <tr>
                                    <th>Course Code</th>
                                    <th>Section Id</th>
                                </tr>

                                <%
                                    if (completedCourseList != null || !completedCourseList.isEmpty()) {
                                        for (String courseId : completedCourseList) {
                                            ArrayList<Section> sectionList = CourseSectionController.getSectionsByCourse(courseId);
                                            for (Section section : sectionList) {

                                %>

                                <tr>
                                    <td> <%out.println(section.getCourseCode());%> </td>
                                    <td> <%out.println(section.getSection());%> </td>
                                <tr/>

                                <%}
                                }
                            }%>
                            </table>
                        </div> <!-- end div body -->
                    </div> <!-- end div panel -->


                    <% }
                    %>
                </div>
            </div>
        

        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>
        <script src='js/bootstrap.js'></script>

    </body>
</html>
