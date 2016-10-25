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
import com.google.gson.JsonPrimitive;
import controller.RoundController;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ChenHuiYan and Regan 
 */
@WebServlet(name = "JSONStartRoundServlet", urlPatterns = {"/json/start"})
public class JSONStartRoundServlet extends HttpServlet {

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
            out.println("<title>Servlet JSONStartRoundServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONStartRoundServlet at " + request.getContextPath() + "</h1>");
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

        if (tok == null) {
            out.print(gson.toJson(error));
        }

        try {
            // verify that user is valid (throws exception if user is not valid)
            String username = JWTUtility.verify(tok, "TwoOStubbornCows");
            JsonArray errors = new JsonArray();
            
            String status = RoundController.getStatus();
            int  roundNum= RoundController.getRound(); 

            if (roundNum==2 && status.equals("inactive")) {
                errors.add(new JsonPrimitive("round 2 ended"));

            }else if (status.equals("inactive")){
                RoundController.startRound();
                
            }
            
            if (errors.size() > 0) {
                toReturn.addProperty("status", "error");
                toReturn.add("message", errors);
            } else {
                if(RoundController.getStatus().equals("inactive")){ 
                    errors.add(new JsonPrimitive("Failed to start round"));

                    
                }else{
                    toReturn.addProperty("status", "success");
                    
                    toReturn.addProperty("round", roundNum);
                    
                }
            }
            out.print(gson.toJson(toReturn));

        } catch (JWTException e) {
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
        processRequest(request, response);
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
