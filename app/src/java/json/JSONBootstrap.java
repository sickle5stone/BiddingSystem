/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import entity.BootstrapError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import controller.BootstrapController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.UploadUtility;

/**
 * Method to handle JSON requests for bootstrap
 * @author Regan and Aloysius
 */
@WebServlet(name = "JSONBootstrap", urlPatterns = {"/json/bootstrap"})
public class JSONBootstrap extends HttpServlet {

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
            out.println("<title>Servlet JSONBootstrap</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JSONBootstrap at " + request.getContextPath() + "</h1>");
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
        response.sendRedirect("/app/wrongmethod.jsp");
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
   //   MultipartRequest fr = new MultipartRequest(request, System.getProperty("java.io.tmpdir"));
    //  String token = fr.getParameter("token");
        boolean csvFilesExists = UploadUtility.readZipFile(request);
        PrintWriter out = response.getWriter();
        String token= UploadUtility.getToken();
        JsonObject toReturn = new JsonObject();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String error = JsonCommonValidation.checkToken(token);
        
        JsonArray errorList = new JsonArray();
        if (!error.isEmpty()){
            errorList.add(new JsonPrimitive(error));
        }
        if (!csvFilesExists){
            errorList.add(new JsonPrimitive("invalid csv file"));            
        }
        
        if (!error.isEmpty()){
            toReturn.addProperty("status", "error");
            toReturn.add("message", errorList);
            toReturn = JsonCommonValidation.sortJsonArray(toReturn);
            out.print(gson.toJson(toReturn));
            return;
        }
        
            JsonArray successRecords = new JsonArray();
        
        if (csvFilesExists) {
            BootstrapController.readAllCsvFiles();
            ArrayList<BootstrapError> returnError = BootstrapController.ERRORLIST;
            Collections.sort(returnError);
            JsonObject bidObj = new JsonObject();
            bidObj.add("bid.csv", new JsonPrimitive(BootstrapController.COUNTBIDSPROCESSED));
            successRecords.add(bidObj);

            JsonObject courseObj = new JsonObject();
            courseObj.add("course.csv", new JsonPrimitive(BootstrapController.COURSELIST.size()));
            successRecords.add(courseObj);

            JsonObject ccObj = new JsonObject();
            ccObj.add("course_completed.csv", new JsonPrimitive(BootstrapController.getNumElements(BootstrapController.COURSECOMPLETE)));
            successRecords.add(ccObj);

            JsonObject prereqObj = new JsonObject();
            prereqObj.add("prerequisite.csv", new JsonPrimitive(BootstrapController.getNumElements(BootstrapController.PREREQLIST)));
            successRecords.add(prereqObj);

            JsonObject sectObj = new JsonObject();
            sectObj.add("section.csv", new JsonPrimitive(BootstrapController.getNumElements(BootstrapController.COURSESECTION)));
            successRecords.add(sectObj);

            JsonObject studObj = new JsonObject();
            studObj.add("student.csv", new JsonPrimitive(BootstrapController.STUDENTLIST.size()));
            successRecords.add(studObj);

            if (returnError.isEmpty()) {
                toReturn.addProperty("status", "success");
                toReturn.add("num-record-loaded", successRecords);
                out.print(gson.toJson(toReturn));

            } else {
                toReturn.addProperty("status", "error");
                toReturn.add("num-record-loaded", successRecords);

                JsonArray jsonErrors = new JsonArray();
                for (BootstrapError b : returnError) {
                    JsonObject errorsObj = new JsonObject();
                    JsonArray eArr = new JsonArray();
                    errorsObj.add("file", new JsonPrimitive(b.getFile()));
                    errorsObj.add("line", new JsonPrimitive(b.getLine()));
                    ArrayList<String> errors = b.getErrors();
                    for (String e : errors) {
                        eArr.add(new JsonPrimitive(e));
                    }
                    errorsObj.add("message", eArr);
                    jsonErrors.add(errorsObj);
                }
                toReturn.add("error", jsonErrors);
                out.print(gson.toJson(toReturn));
                return;

            }
        }else{
           toReturn.addProperty("status", "error");
           JsonArray errorArr = new JsonArray();
           errorArr.add(new JsonPrimitive("invalid csv file"));
           toReturn.add("message", errorArr);
           out.print(gson.toJson(toReturn));
        }
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
