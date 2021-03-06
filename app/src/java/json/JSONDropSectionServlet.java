/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import controller.CourseSectionController;
import controller.RoundController;
import controller.SectionStudentController;
import controller.StudentController;
import dao.SectionDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method to handle JSON requests for drop section
 * @author Haseena and Regan
 */
@WebServlet(name = "JSONDropSectionServlet", urlPatterns = {"/json/drop-section"})
public class JSONDropSectionServlet extends HttpServlet {

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
            out.println("<title>Servlet JSONDropSectionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDropSectionServlet at " + request.getContextPath() + "</h1>");
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
        
        //Create a object to store objects
        JsonObject toReturn = new JsonObject();
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        //Create an arrays of errors.
        JsonArray errors = new JsonArray();

        String urlObject = (String) request.getParameter("r"); 
       
        ArrayList<String> fields = new ArrayList<>();
        fields.add("userid");
        fields.add("course");
        fields.add("section");
        String token= request.getParameter("token");
        
        JsonObject error = JsonCommonValidation.validate(token, fields, urlObject);
        if (error != null){
            error = JsonCommonValidation.sortJsonArray(error);
            out.print(gson.toJson(error));
            return;
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(urlObject);
        
        String userid = jsonObject.get("userid").getAsString();
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();
        
        gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
       
        boolean roundActive;
        String status = RoundController.getStatus();
        int roundNum = RoundController.getRound();

        //Checks if the round is active and is in round 2
        if (status.equals("inactive")) {
            errors.add(new JsonPrimitive("round not active"));
            toReturn.addProperty("status", "error");
            toReturn.add("message", errors);
            out.print(gson.toJson(toReturn));
            return;
        } else {
            roundActive = true;
        }
        //Check if the course does not exist in the system's record
        Course c = CourseSectionController.getCourseByCourseCode(course);
        Student s = StudentController.retrieveStudent(userid);
        
        if(c==null){
             errors.add(new JsonPrimitive("invalid course"));
            
        }else{
             Section sec=  SectionDAO.getSpecificSection(course,section);
             if(sec==null){
                    errors.add(new JsonPrimitive("invalid section"));
                 
             }
            
        }
        
        if(s==null){
            errors.add(new JsonPrimitive("invalid userid"));
            
        }
        
        if(errors.size()==0){
              ArrayList<Bid> bidList = SectionStudentController.getSectionsByStudentId(userid);
                boolean sectionFound = false;
                for (Bid bid : bidList) {
                    if (bid.getCourseCode().equals(course)) {
                        if (bid.getSectionCode().equals(section)) {
                            sectionFound = true;
                            SectionStudentController.deleteSectionFromStudent(userid, course, section);
                            break;
                        }
                    }
                }
                if (!sectionFound) {
                    errors.add(new JsonPrimitive("no such enrollment record"));
                }
        }

        if (errors.size() > 0) {
            toReturn.addProperty("status", "error");
            toReturn.add("message", errors);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
        } else {
            toReturn.addProperty("status", "success");
        }
        out.print(gson.toJson(toReturn));

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
        response.sendRedirect("/app/wrongmethod.jsp");
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
