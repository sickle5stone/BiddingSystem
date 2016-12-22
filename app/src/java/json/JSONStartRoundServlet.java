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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static json.JsonCommonValidation.checkToken;

/**
 * Method to handle JSON requests for start round
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
         JsonArray errors = new JsonArray();
        JsonObject toReturn = new JsonObject();
        String token = request.getParameter("token");
        if(token==null){
            toReturn.addProperty("status", "error");
            errors.add(new JsonPrimitive("invalid request object"));
            toReturn.add("message", errors);
            out.print(gson.toJson(toReturn)); 
            return;
            
        }
        
      
           
        if (!checkToken(token).isEmpty()) {
                errors.add(new JsonPrimitive((checkToken(token))));
        }
        
        if (errors.size()>0){  
            toReturn.addProperty("status", "error");
            toReturn.add("message", errors);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            out.print(gson.toJson(toReturn));
            return;
        }
        
        toReturn.addProperty("status", "success");
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
                toReturn.addProperty("status", "error");
                toReturn.add("message", errors);
            }else{
                toReturn.addProperty("status", "success");
                roundNum= RoundController.getRound(); 
                toReturn.addProperty("round", roundNum);

            }
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
