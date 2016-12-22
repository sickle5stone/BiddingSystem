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
import controller.SectionStudentController;
import controller.StudentController;
import dao.BidDAO;
import dao.CourseDAO;
import dao.SectionDAO;
import dao.SectionMinimumPriceDAO;
import dao.SectionStudentDAO;
import entity.Bid;
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
 * Servlet to handle JSON Bid Status request
 * @author ChenHuiYan and Aloysius
 */
@WebServlet(name = "JSONBidStatusServlet", urlPatterns = {"/json/bid-status"})
public class JSONBidStatusServlet extends HttpServlet {

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
            out.println("<title>Servlet JSONBidStatusServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONBidStatusServlet at " + request.getContextPath() + "</h1>");
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
        // grab all parameter values
        String urlObject=request.getParameter("r");
        String token=request.getParameter("token");
        // expected fields
        ArrayList<String> fields = new ArrayList<>();
        fields.add("course");
        fields.add("section");
        
        JsonObject error = JsonCommonValidation.validate(token, fields, urlObject);
        if (error != null){            
            error = JsonCommonValidation.sortJsonArray(error);
            out.print(gson.toJson(error));   
            return;
        }
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(urlObject);
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();
        
        JsonObject toReturn = new JsonObject();
        //Create an arrays of errors.
        JsonArray errors = new JsonArray();
        if (CourseDAO.getCourseByCourseCode(course) != null){
             if (SectionDAO.getSpecificSection(course, section) == null){
                  toReturn.addProperty("status", "error");
                  errors.add(new JsonPrimitive("invalid section"));
             }
        }else {
            toReturn.addProperty("status", "error");
            errors.add(new JsonPrimitive("invalid course"));
        }
        
        if(errors.size()==0){
            String roundStatus = RoundController.getStatus();
            int  roundNum= RoundController.getRound();
            double minimumBidPrice=0;
            double bidAmount =0;
            String userId="";
            Student stu;
            double balance=0;   
            String eachBidStatus ="";
            JsonArray studentList = new JsonArray();
           
            ArrayList<Bid> bids= BidController.getAllBidBySectionCourseId(course, section);
            int classSize=CourseSectionController.getSection(course, section).getClassSize();
            int vacancy= classSize - SectionStudentController.getNumEnrolledInSection(course, section);
            // vacancy for round1-inactive and round2
            if(roundNum==1 && roundStatus.equals("active")){
               // RoundController.startRound();
                vacancy=classSize;  // round 1 vacancy is class size
                if(bids==null || bids.isEmpty() ){
                    minimumBidPrice=10;
                }else if(bids.size()<vacancy){
                    minimumBidPrice=BidController.getLowestBidAmount(course,section);
                }else{   // get clearing price when the number of bids >= vacancy
                    minimumBidPrice=BidController.getClearingPrice(course,section,vacancy);
                }
              
            }
            if((roundNum==1||roundNum==2) && (roundStatus.equals("inactive"))){
                minimumBidPrice=BidDAO.getLowestSuccessfulBid(course, section);
            }
            
            if(roundNum==2 && roundStatus.equals("active")){
 
                if(vacancy ==0){
                    minimumBidPrice=10;
                    
                }else{
                
                    double clearingPrice =BidController.getClearingPrice(course, section, vacancy);     
                    
                    minimumBidPrice = SectionMinimumPriceDAO.getMinBidAmtByCourseSection(course, section);
                    //minimumBidPrice = BidController.getNextHigherBidAmount(course, section, clearingPrice);
                    
                   // if(minimumBidPrice==0){
                       // minimumBidPrice=10;
                        
                    //}  // if only 1 bid 
                }
            }
          
            if(roundNum==2 && roundStatus.equals("inactive")){
                 bids=SectionStudentDAO.getStudentEnrolledBySectionJson(course,section);
            }
            if(bids!=null){  
                for (Bid b : bids) {
                    JsonObject indivStudent = new JsonObject();
                    bidAmount = b.getBidAmount();
                    userId=b.getUserId();
                    stu =StudentController.retrieveStudent(userId);
                    balance=stu.geteDollar();   
                    eachBidStatus = b.getBidStatus();
                  
                    indivStudent.addProperty("userid",userId);
                    indivStudent.addProperty("amount", bidAmount);
                    indivStudent.addProperty("balance", balance);
                    indivStudent.addProperty("status", eachBidStatus);

                    studentList.add(indivStudent);


                   // int vacancies = size - SectionStudentController.getNumEnrolledInSection(courseCode, sectionCode);
                }
            }
            
            toReturn.addProperty("status", "success");
            toReturn.addProperty("vacancy", vacancy);
            toReturn.addProperty("min-bid-amount", minimumBidPrice);
            toReturn.add("students", studentList);
            
        }else{
            toReturn.addProperty("status", "error");
            toReturn.add("message", errors);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            
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
        response.sendRedirect("wrongmethod.jsp");
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
