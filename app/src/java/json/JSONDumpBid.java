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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AroisuNamae
 */
@WebServlet(name = "JSONDumpBid", urlPatterns = {"/bid-dump"})
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
        //processRequest(request, response);
        
        String jsonString = request.getParameter("r");
        String token = request.getParameter("token");
        JsonObject toReturn = new JsonObject();
        JsonArray errors = new JsonArray();
        
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        
        if (request.getParameter("r") != null){
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(jsonString).getAsJsonObject();
            
            //get course
            if (obj.get("course") == null){
                errors.add(new JsonPrimitive("missing course"));
            } else {
                if (obj.get("course").getAsString().trim().isEmpty()){
                    errors.add(new JsonPrimitive("blank course"));
                }
            }
            
            //get section
            if (obj.get("section") == null){
                errors.add(new JsonPrimitive("missing section"));
            } else {
                if (obj.get("section").getAsString().trim().isEmpty()){
                    errors.add(new JsonPrimitive("blank section"));
                }
            }
            
            if(errors.size()!=0){
                toReturn.addProperty("status", "error");
                toReturn.add("message",errors);
                out.print(gson.toJson(toReturn));
                return;
            }
            
            String course = obj.get("course").getAsString();
            String section = obj.get("course").getAsString();
            
            
        
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
