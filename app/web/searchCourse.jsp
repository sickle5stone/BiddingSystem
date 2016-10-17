<%--  
    Document   : searchCourse
    Created on : 7 Oct, 2016, 9:03:18 PM
    Author     : ChenHuiYan
--%>

<%@page import="dao.CourseDAO"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Course"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controller.CourseSectionController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    
    <%   
        ArrayList<Course> coursesByTitle = new ArrayList<>();
        String submitCourse= request.getParameter("searchCourse"); 
        String inputCourseTitle= request.getParameter("CourseTitle");
        String inputCourseCode= request.getParameter("CourseCode");
        String inputSchool = request.getParameter("school");
         
        if(submitCourse!=null){
            if(inputCourseTitle==null){
                inputCourseTitle="";
            }

            if(inputCourseCode==null){
                inputCourseCode="";
            }

            if(inputSchool==null){
                inputSchool="";
            }
        }
     
        CourseSectionController cs= new CourseSectionController(); 
        coursesByTitle=cs.getCourses(inputCourseTitle, inputCourseCode, inputSchool);     
         
        String viewDetails = request.getParameter("viewSections");
     
        
    %>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='css/bootstrap.css' rel='stylesheet'>
        <title>JSP Page</title>
    </head>
    <body>
        
        <div class='container navbar navbar-default'>
            <div class='navbar-brand'>
                Search Course
            </div>
            <ul class='nav navbar-nav navbar-right'> 
          
                <li><a href='studenthome.jsp'>Home Page</a></li>  
            </ul>
        </div>
        
        
       <div class='container'>
            
           <div class='row'>

                <div class='col-md-6'>   
        
                <form action="searchCourse.jsp" method="post">
                    <table>
                        <tr>
                            <td>
                                Course title:
                            </td>
                            <td>
                                <input type="text" name="CourseTitle"/>
                            </td>
                        </tr>
                      
                        <tr>
                            <td>
                                 Course code:
                            </td>
                            
                            <td>
                                <input type="text" name="CourseCode"/>
                            </td>
                        </tr>

                        
                        <tr>
                            <td>
                                School:
                            </td>
                            <td>
                                <input type="text" name="school"/>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <input type="submit" name="searchCourse" value="Send" />
                            </td>
                        </tr>
                    </table>

                </form>
            
            <%  
                if(coursesByTitle.size()>0){ 
            %>
            <div class='col-md-12 '>
            <table border="0" class='lead table table-striped'>
                <tr>
                    <th>
                        Course Code
                        
                    </th>
                    
                    <th>
                        School offered
                    </th>
                    
                    <th>
                        Course Title
                    </th>
                    
                    <th>
                        Course Description
                    </th>
                    
                    
                    <th>
                        Exam Date 
                    </th>
                    
                    <th>
                        Exam Start Time 
                        
                    </th>
                    
                    <th>
                        Exam End Time
                        
                    </th>
                    
                    <th>
                        Sections
                        
                    </th>
                
                
            </tr>
             <%    
                
                for(Course c: coursesByTitle){
                    String courseCode=c.getCourseCode();
                    String schoolTitle=c.getSchoolTitle();
                    String courseTitle=c.getCourseTitle();    
                    String courseDesc= c.getDescription();
                    Date examDate= c.getExamDate();
                    Date examStart= c.getExamStart();
                    Date examEnd =c.getExamEnd();
            %>
                    <tr>
                        <td> <%=courseCode %> </td>
                        <td> <%=schoolTitle %> </td>
                        <td> <%=courseTitle %> </td>
                        <td> <%=courseDesc %> </td>
                        <td> <%=examDate %> </td>
                        <td> <%=examStart %> </td>
                        <td> <%=examEnd %> </td>
                        <td> 
                         
                            <form action="searchSection.jsp" method="post">

                                <button name="viewSections" value="<%=courseCode %>"> View Sections </button>

                            </form>
                        </td>
                    
                        
                    </tr>
            <%         
                }
                
                
            %>
                
                
                
                
            </table>
            </div>
                    
            <%  
                }
            
            
            %>
            
                </div>
            </div>
       </div>
            
         
    </body>
</html>
