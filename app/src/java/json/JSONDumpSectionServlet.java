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
import dao.CourseDAO;
import dao.SectionDAO;
import dao.SectionStudentDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method to handle JSON requests for Dump Section
 * @author Cheryl and Aloysius
 */
@WebServlet(name = "JSONDumpSectionServlet", urlPatterns = {"/json/section-dump"})
public class JSONDumpSectionServlet extends HttpServlet {

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
            out.println("<title>Servlet JSONDumpSectionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDumpSectionServlet at " + request.getContextPath() + "</h1>");
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
        
        String url = request.getParameter("r");
        String token = request.getParameter("token");
        ArrayList<String> fields = new ArrayList<>();
        fields.add("course");
        fields.add("section");
        JsonObject error = JsonCommonValidation.validate(token, fields, url);
        if (error != null){
            error = JsonCommonValidation.sortJsonArray(error);
            out.print(gson.toJson(error));
            return;
        }
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(url);
        
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();
        
        Course crs = CourseDAO.getCourseByCourseCode(course);
        if (crs == null ){
            toReturn.addProperty("status", "error");
            errors.add(new JsonPrimitive("invalid course"));
            toReturn.add("message", errors);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            out.print(gson.toJson(toReturn));
            return;
        }
        Section sect = SectionDAO.getSpecificSection(course, section);
        if (sect == null){
            toReturn.addProperty("status", "error");
            errors.add(new JsonPrimitive("invalid section"));
            toReturn.add("message", errors);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            out.print(gson.toJson(toReturn));
            return;
        }
        JsonArray sectStu = new JsonArray();
        ArrayList<Bid> stuList = SectionStudentDAO.getStudentEnrolledBySection(course, section);
        for (Bid b : stuList){
            JsonObject indivStu = new JsonObject();
            String userId = b.getUserId();
            double amt = b.getBidAmount();
            indivStu.addProperty("userid", userId);
            indivStu.addProperty("amount", amt); 
            sectStu.add(indivStu);
        }
        toReturn.addProperty("status", "success");
        toReturn.add("students", sectStu);
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
