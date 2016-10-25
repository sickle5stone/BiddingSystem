/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import controller.SectionStudentController;
import dao.BidDAO;
import dao.CourseCompleteDAO;
import dao.CourseDAO;
import dao.SectionDAO;
import dao.SectionStudentDAO;
import dao.StudentDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Cheryl
 */
@WebServlet(name = "JSONDumpTableServlet", urlPatterns = {"/dump"})
public class JSONDumpTableServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet JSONDumpTableServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDumpTableServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {   
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        
        JsonObject error = new JsonObject();
        error.addProperty("status", "error");
        String tok = request.getParameter("token");
        
        JsonObject toReturn = new JsonObject();
        toReturn.addProperty("status", "success");
        
        if (tok == null){
            out.print(gson.toJson(error));
        }
        try{
            // verify that user is valid (throws exception if user is not valid)
            String username = JWTUtility.verify(tok, "TwoOStubbornCows");
            
            // --- Course Table ----
            JsonArray courseList = new JsonArray();  // new json object for all courses
            
            ArrayList<Course> allCourse = CourseDAO.getAllCourses();
            for (Course c : allCourse){
                JsonObject indivCourse = new JsonObject();
                indivCourse.addProperty("course",c.getCourseCode());
                indivCourse.addProperty("school",c.getSchoolTitle());
                indivCourse.addProperty("title",c.getCourseTitle());
                indivCourse.addProperty("description",c.getDescription());
                indivCourse.addProperty("exam date",""+c.getExamDate()); // convert date into String format
                indivCourse.addProperty("exam start",""+c.getExamStart()); 
                indivCourse.addProperty("exam end",""+c.getExamEnd()); 
                
                
                courseList.add(indivCourse);
                                
            }
            toReturn.add("course", courseList);
            
            // --- Section Table ---
           
            JsonArray sectionList = new JsonArray();  // new json object for all section
            
            ArrayList<Section> allSection = SectionDAO.getAllSections(); 
            for (Section s: allSection){
                JsonObject indivSection = new JsonObject();
                
                int day = s.getDayOfWeek();
                String dayOfWeek = "";
                switch(day){
                    case 1: dayOfWeek = "Monday";
                        break;
                    case 2: dayOfWeek = "Tuesday";
                        break;
                    case 3: dayOfWeek = "Wednesday";
                        break;
                    case 4: dayOfWeek = "Thursday";
                        break;
                    case 5: dayOfWeek = "Friday";
                        break;
                    case 6: dayOfWeek = "Saturday";
                        break;
                    case 7: dayOfWeek = "Sunday";
                        break;
                    
                }
                
                indivSection.addProperty("course", s.getCourseCode());
                indivSection.addProperty("section", s.getSection());
                indivSection.addProperty("day", dayOfWeek );
                indivSection.addProperty("start", ""+s.getStartTime());
                indivSection.addProperty("end", ""+s.getEndTime());
                indivSection.addProperty("instructor", s.getInstructor());
                indivSection.addProperty("venue", s.getVenue());
                indivSection.addProperty("size", s.getVenue());
                
                sectionList.add(indivSection);
            }
            toReturn.add("section",sectionList);
         
            // --- Student Table --- 
            JsonArray studentList = new JsonArray(); // new json object for all section
            
            ArrayList<Student> allStudent = StudentDAO.retrieveAllStudents(); 
            
            for(Student s : allStudent ){
                JsonObject indivStudent = new JsonObject();
                indivStudent.addProperty("userid",s.getUserId());
                indivStudent.addProperty("password",s.getPassword());
                indivStudent.addProperty("name",s.getName());
                indivStudent.addProperty("school",s.getSchool());
                indivStudent.addProperty("edollar",s.geteDollar());
                
                studentList.add(indivStudent);
            }
            toReturn.add("student", studentList);
            
            // --- Prerequisite Table ---
            JsonArray preReqList = new JsonArray();
            HashMap<String, ArrayList<String>> courseToPrereq = CourseDAO.getCourseSectionHashMap();
            Set<String> courseKey =  courseToPrereq.keySet(); 
            String[] keyArray = courseKey.toArray(new String[0]); 
            Arrays.sort(keyArray);
            for (String ck : keyArray){
                ArrayList<String> prereqForCourse = courseToPrereq.get(ck);
                for (String str : prereqForCourse){
                    JsonObject indivCoursePrereq  = new JsonObject();
                    indivCoursePrereq.addProperty("course", ck);                    
                    indivCoursePrereq.addProperty("prerequisite", str);
                    preReqList.add(indivCoursePrereq);
                }
                
            }
            toReturn.add("prerequisite", preReqList); 
          
          // --- Bid Table ---
            JsonArray bidList = new JsonArray();
            ArrayList<Bid> bidFromDAO = BidDAO.getAllBids();
            for (Bid b : bidFromDAO){
                JsonObject indivBids = new JsonObject();
                indivBids.addProperty("userid",b.getUserId());
                indivBids.addProperty("course",b.getCourseCode());
                indivBids.addProperty("section",b.getSectionCode());
                indivBids.addProperty("amount",b.getBidAmount());
                bidList.add(indivBids);
            }
            toReturn.add("bid", bidList);
          
          // --- Course Complete Table ---
            JsonArray courseComList = new JsonArray();
            HashMap<String, ArrayList<String>> courseToStudent = CourseCompleteDAO.getAllCourseCompleted();
            Set<String> courseComKey =  courseToStudent.keySet(); 
            String[] courseComKeyArray = courseComKey.toArray(new String[0]); 
            Arrays.sort(courseComKeyArray);
            for (String ck : courseComKeyArray){
                ArrayList<String> stuForCourse = courseToStudent.get(ck);
                for (String str : stuForCourse){
                    JsonObject indivCourseCom  = new JsonObject();
                    indivCourseCom.addProperty("userid", str);
                    indivCourseCom.addProperty("course", ck);                    
                    courseComList.add(indivCourseCom);
                }
                
            }
            toReturn.add("completed-course", courseComList); 
          
          
            
          // --- Section Student Table ---  
          
            JsonArray secStudDetails = new JsonArray();
            ArrayList<Bid> sectionStudentList = SectionStudentDAO.getAllSectionStudent();
            for (Bid b : sectionStudentList){
                JsonObject indivSecStudDetails = new JsonObject();
                indivSecStudDetails.addProperty("userid",b.getUserId());
                indivSecStudDetails.addProperty("course",b.getCourseCode());
                indivSecStudDetails.addProperty("section",b.getSectionCode());
                indivSecStudDetails.addProperty("amount",b.getBidAmount());
                secStudDetails.add(indivSecStudDetails);
            }
            toReturn.add("section-student", secStudDetails);
            
          
            out.print(gson.toJson(toReturn));
        
        }catch (JWTException e ){
            out.print(gson.toJson(error));
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
