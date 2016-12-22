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
import controller.RoundController;
import dao.BidDAO;
import dao.CourseDAO;
import dao.SectionDAO;
import entity.Bid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method to handle JSON requests for dump bid
 * @author Aloysius and Cheryl
 */
@WebServlet(name = "JSONDumpBid", urlPatterns = {"/json/bid-dump"})
public class JSONDumpBid extends HttpServlet {

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
            out.println("<title>Servlet JSONDumpBid</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDumpBid at " + request.getContextPath() + "</h1>");
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
        
        String course = jsonObject.get("course").getAsString();
        String section = jsonObject.get("section").getAsString();
        
        JsonObject returnBid = new JsonObject();
        
        JsonArray errorArr = new JsonArray();
        if (CourseDAO.getCourseByCourseCode(course) != null){
             if (SectionDAO.getSpecificSection(course, section) == null){
                  returnBid.addProperty("status", "error");
                  errorArr.add(new JsonPrimitive("invalid section"));
             }
        }else {
             returnBid.addProperty("status", "error");
             errorArr.add(new JsonPrimitive("invalid course"));
        }
        
        if(errorArr.size()>0){
            returnBid.add("message", errorArr);
            returnBid = JsonCommonValidation.sortJsonArray(returnBid);
            out.print(gson.toJson(returnBid));
            return;
        }
        
        ArrayList<Bid> bidList = BidDAO.getAllBidBySectionCourseId(course, section);
        
        int row = 0;
        JsonArray returnArr = new JsonArray();
        JsonObject retObj = new JsonObject();
        
        for (Bid b : bidList){
            retObj = new JsonObject();
            row+=1;
            retObj.addProperty("row", row);
            retObj.addProperty("userid",b.getUserId());
            retObj.addProperty("amount",b.getBidAmount());
            String result = b.getBidStatus();
            if (RoundController.getStatus().equals("inactive")){
                if (result.equals("success")){
                    retObj.addProperty("result", "in");
                }else if (result.equals("fail")){
                    retObj.addProperty("result", "out");
                }
            }
            else if (RoundController.getStatus().equals("active")){
                if (result.equals("success")){
                    retObj.addProperty("result", "-");
                }else if (result.equals("fail")){
                    retObj.addProperty("result", "-");
                }else if (result.equals("pending")){
                    retObj.addProperty("result", "-");
                }
            }
            returnArr.add(retObj);
        }
        
        returnBid.addProperty("status", "success");
        returnBid.add("bids", returnArr);
        out.print(gson.toJson(returnBid));
        
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
