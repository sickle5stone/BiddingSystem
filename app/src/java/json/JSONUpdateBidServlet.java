/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import bootstrap_validation.ValidateBid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import controller.BidController; 
import controller.CourseSectionController;
import controller.RoundController;
import controller.SectionStudentController;
import controller.StudentController;
import dao.BidDAO;
import dao.CourseDAO;
import dao.RoundDAO; 
import dao.SectionMinimumPriceDAO;
import dao.SectionStudentDAO;
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
 * Method to handle JSON requests for Update Bid
 * @author Haseena and Cheryl
 */
@WebServlet(name = "JSONUpdateBidServlet", urlPatterns = {"/json/update-bid"})
public class JSONUpdateBidServlet extends HttpServlet {

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
            out.println("<title>Servlet JSONUpdateBidServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONUpdateBidServlet at " + request.getContextPath() + "</h1>");
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

        String tok = request.getParameter("token");

        JsonObject toReturn = new JsonObject();
        toReturn.addProperty("status", "success");
        ArrayList<String> fields = new ArrayList<>();
        fields.add("userid");
        fields.add("amount");
        fields.add("course");
        fields.add("section");

        String urlObject = request.getParameter("r");

        JsonObject commonErrors = JsonCommonValidation.validate(tok, fields, urlObject);
        if (commonErrors != null) {
            commonErrors = JsonCommonValidation.sortJsonArray(commonErrors);
            out.println(gson.toJson(commonErrors));
            return;
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(urlObject);

        String userid = jsonObject.get("userid").getAsString();
        String amount = jsonObject.get("amount").getAsString();
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();

        Student student = StudentController.retrieveStudent(userid);
        Course c = CourseDAO.getCourseByCourseCode(course);
        Section s = CourseSectionController.getSection(course, section);

//Error object
        JsonArray error = new JsonArray();

        boolean amountIsValid = ValidateBid.checkIfAmountIsValid(amount);
        if (!amountIsValid) {
            error.add(new JsonPrimitive("invalid amount"));
        }
        //check for valid course
        if (c == null) {
            error.add(new JsonPrimitive("invalid course"));
        } else if (s == null) {
            error.add(new JsonPrimitive("invalid section"));
        }
        if (student == null) {
            error.add(new JsonPrimitive("invalid userid"));
        }
        
        //Check if the input validation doesn't pass(error array is not empty)
        //print out errors and stop processing
        if (error.size() > 0) {
            toReturn.addProperty("status", "error");
            toReturn.add("message", error);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            out.print(gson.toJson(toReturn));
            return;
        }
        
        String status = RoundController.getStatus();
        if (status.equals("inactive")) {
            error.add(new JsonPrimitive("round ended"));
        }
        
        double minPrice = SectionMinimumPriceDAO.getMinBidAmtByCourseSection(course, section);
        double amt = Double.parseDouble(amount);

        if (amt < minPrice && RoundController.getRound()==2) {
            error.add(new JsonPrimitive("bid too low"));
        }

        Bid b = BidDAO.getBidForCourse( course, userid);
        double existingBidAmt = 0;
        if (b != null) {
            //bid is present
            String existSection=b.getSectionCode();
            if(!existSection.equals(section)){               
                if (BidController.checkIfClassTimeTableClashes(course, section, userid, b )){
                    error.add(new JsonPrimitive("class timetable clash"));
                }                
            }
            existingBidAmt = b.getBidAmount();
            boolean isSufficient = BidController.checkIfAmountBalanceIsSufficient(userid, amt - existingBidAmt);
            
            if (!isSufficient) {
                error.add(new JsonPrimitive("insufficient e$"));
            }
            // if student hasnt bidded for the course before, check for exam time table
        }else {
            if (BidController.checkIfClassTimeTableClashes(course, section, userid, null)){
                error.add(new JsonPrimitive("class timetable clash"));
            }

            boolean abc = BidController.checkIfCourseAlreadyCompleted(userid, course);

            if (BidController.checkIfCourseAlreadyCompleted(userid, course)) {
                error.add(new JsonPrimitive("course completed"));
            }
            boolean isEnrolled = SectionStudentDAO.isEnrolled(userid, course);

            if (isEnrolled) {
                error.add(new JsonPrimitive("course enrolled"));
            }
            // if student hasnt bidded for the course before, check for exam time table
            if (b == null) {
                if (BidController.checkIfExamsClashes(course, userid)){
                    error.add(new JsonPrimitive("exam timetable clash"));
                }
            }

            if(!BidController.checkIfPreRequisiteCompleted(userid, course)){
                error.add(new JsonPrimitive("incomplete prerequisite"));
            }
            // when it is a new bid
            boolean isSufficient = BidController.checkIfAmountBalanceIsSufficient(userid, amt - existingBidAmt);
            if (!isSufficient) {
                error.add(new JsonPrimitive("insufficient e$"));
            }
            if (RoundDAO.getRoundNum() == 1 && !BidController.checkIfRoundBidIsOwnSchool(course, userid)) {
                error.add(new JsonPrimitive("not own school course"));
            }
            int classSize = s.getClassSize();
            int enrolled = SectionStudentController.getNumEnrolledInSection(course, section);

            if (b == null && classSize - enrolled == 0) {
                error.add(new JsonPrimitive("no vacancy"));
            }

            if (BidController.checkIfSectionLimitReached(userid, course, section)) {
                error.add(new JsonPrimitive("section limit reached"));
            }


        }
        if(error.size()>0){
            toReturn.addProperty("status", "error");
            toReturn.add("message", error);
            out.print(gson.toJson(toReturn));
            return;
        }
        
        if(b == null){
            BidController.addBid(userid, amt, section, course);
        }else{
           String existSection=b.getSectionCode();
           BidController.jsonUpdateBid(userid, amt, section,existSection, course);
        }
        toReturn.addProperty("status", "success");
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
