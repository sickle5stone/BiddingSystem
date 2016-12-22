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
import controller.BidController;
import controller.CourseSectionController;
import controller.RoundController;
import controller.StudentController;
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
 * Method to handle JSON requests for delete bid
 * @author Haseena and Regan
 */
@WebServlet(name = "JSONDeleteBid", urlPatterns = {"/json/delete-bid"})
public class JSONDeleteBidServlet extends HttpServlet {
    //private static final String SHARED_SECRET = "TwoOStubbornCows";

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
            out.println("<title>Servlet JSONDeleteBid</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDeleteBid at " + request.getContextPath() + "</h1>");
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
        
        String urlObject=request.getParameter("r");
        String token=request.getParameter("token");
        
        ArrayList<String> fields = new ArrayList<>();
        fields.add("userid");
        fields.add("course");
        fields.add("section"); 
        
        JsonObject toReturn = new JsonObject();

        //Create an arrays of errors.
        JsonArray errors = new JsonArray();
        JsonObject error = JsonCommonValidation.validate(token, fields, urlObject);
        if (error != null){            
            //toReturn.add("message", errors);
            error = JsonCommonValidation.sortJsonArray(error);
            out.print(gson.toJson(error));
            return;
        }
        
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(urlObject);
        
        String userId = jsonObject.get("userid").getAsString();
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();
        



        // verify that user is valid (throws exception if user is not valid)
        // String username = JWTUtility.verify(tok, "TwoOStubbornCows");
        boolean roundActive=false;
        String status = RoundController.getStatus();
        int roundNum = RoundController.getRound();

        //Checks if the current round has ended
        if (status.equals("inactive") || (roundNum < 0 || roundNum > 3)) {
            errors.add(new JsonPrimitive("round ended"));
        } else {
            roundActive = true;
        }

        //Check if the course does not exist in the system's record
        Course c = CourseSectionController.getCourseByCourseCode(course);
        boolean courseValid = false;
        boolean useridValid = false;
        boolean sectionValid = false;
        Student s = StudentController.retrieveStudent(userId);
        if (s != null) {
            useridValid = true;
            //if student exist, check for section
        } else {
            errors.add(new JsonPrimitive("invalid userid"));

        }
        if (c != null) {
            //if course exist, check for userId
            courseValid = true;
            Section sect = CourseSectionController.getSection(course, section);
            if (sect != null) {
                sectionValid = true;
            } else {
                errors.add(new JsonPrimitive("invalid section"));
            }
           
        } else {
            errors.add(new JsonPrimitive("invalid course"));
        }

        if (roundActive && courseValid && useridValid && sectionValid) {
            //checks if bid exist
            if (!BidController.deleteBidFromStudent(userId, course, section)) {
                errors.add(new JsonPrimitive("no such bid"));

            } else {
                //refund edollars & delete bid
                BidController.deleteBidFromStudent(userId, course, section);
            }

          

        }
        
        if (errors.size() > 0) {
                toReturn.addProperty("status", "error");
                toReturn.add("message", errors);
                toReturn = JsonCommonValidation.sortJsonArray(toReturn);
        }else {
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
