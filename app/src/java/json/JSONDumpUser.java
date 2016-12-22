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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.StudentController;
import entity.Student;

/**
 * Method to handle JSON requests for Dump User 
 * @author ChenHuiYan and Regan 
 */
@WebServlet(name = "JSONDumpUser", urlPatterns = {"/json/user-dump"})
public class JSONDumpUser extends HttpServlet {

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
            out.println("<title>Servlet JSONDumpUser</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONDumpUser at " + request.getContextPath() + "</h1>");
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
        fields.add("userid");
        
        JsonObject error = JsonCommonValidation.validate(token, fields, urlObject);
        if (error != null){            
           error = JsonCommonValidation.sortJsonArray(error);
            out.print(gson.toJson(error));   

            return;
        }
        
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(urlObject);
        String userId = jsonObject.get("userid").getAsString();
        
        
     
        
        JsonObject toReturn = new JsonObject();

        //Create an arrays of errors.
        JsonArray errors = new JsonArray();
       
        
        Student student=StudentController.retrieveStudent(userId);
        if(student==null){
              errors.add(new JsonPrimitive("invalid userid"));
              toReturn.addProperty("status", "error");
              toReturn.add("message", errors);
              toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            
            
        }else{
            String password=student.getPassword();
            String name=student.getName();
            String school=student.getSchool();
            double eDollar=student.geteDollar();
            toReturn.addProperty("status", "success");
            toReturn.addProperty("userid", userId);
            toReturn.addProperty("password",password);
            toReturn.addProperty("name", name);
            toReturn.addProperty("school", school);
            toReturn.addProperty("edollar", eDollar);
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
