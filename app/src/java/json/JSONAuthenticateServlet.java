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
import static controller.LoginController.processLogin;
import dao.AdminDAO;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author reganseah & cheryl
 */
@WebServlet(name = "JSONAuthenticateServlet", urlPatterns = {"/json/authenticate"})
public class JSONAuthenticateServlet extends HttpServlet {
    private static final String SHARED_SECRET = "TwoOStubbornCows";
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
            out.println("<title>Servlet JSONAuthenticateServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONAuthenticateServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = "admin";
        
        JsonObject toReturn = new JsonObject();
        JsonArray errors = new JsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        

        // username parameter is missing
        if (username == null) {
            errors.add(new JsonPrimitive("missing username"));
            
        } else if (username.trim().isEmpty()) {
            // username parameter is empty
            errors.add(new JsonPrimitive("blank username"));
        }

        // password parameter is missing
        if (password == null) {
            errors.add(new JsonPrimitive("missing password"));
        } else if (password.trim().isEmpty()) {
            // password parameter is empty
            errors.add(new JsonPrimitive("blank password"));
        }
        
        if(errors.size()!=0){
            toReturn.addProperty("status", "error");
            toReturn.add("message",errors);
            out.print(gson.toJson(toReturn));
            return;
        }
        boolean status = processLogin(username, password, userType);
        if(!status){
            errors.add(new JsonPrimitive("invalid username/password"));
            toReturn.addProperty("status", "error");
            toReturn.add("message",errors);
            out.print(gson.toJson(toReturn));
            return;
        }

        toReturn.addProperty("status", "success");
        String token = JWTUtility.sign(SHARED_SECRET, username);
        toReturn.addProperty("token",token);
        
        out.print(gson.toJson(toReturn));
        
        
        
        
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
