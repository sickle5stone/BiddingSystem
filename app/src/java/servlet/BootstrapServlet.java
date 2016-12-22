/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controller.BootstrapController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.UploadUtility;

/**
 * Servlet class to handle all requests pertaining to bootstrap
 * @author Cheryl and Huiyan
 */
@WebServlet(name = "BootstrapServlet", urlPatterns = {"/BootstrapServlet"})
public class BootstrapServlet extends HttpServlet {

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
       // System.out.println("Request processed");
       PrintWriter out = response.getWriter();
       HttpSession session = request.getSession();
       
       // check if admin is login in else redirect to login page
        boolean csvFilesExists = UploadUtility.readZipFile(request);
        if (csvFilesExists){
            BootstrapController.readAllCsvFiles();
            request.setAttribute("successList", BootstrapController.SUCCESSFULRECORDS );
            request.setAttribute("errorList", BootstrapController.ERRORLIST);
          //  forward request object containing error arrayList to adminHome page 
            RequestDispatcher view = request.getRequestDispatcher("adminhome.jsp");
            view.forward(request, response); 
        }else{
          // forward request object containing error message to fileupload page if csvFile==null
            request.setAttribute("error", "File uploaded is invalid.");
            RequestDispatcher view = request.getRequestDispatcher("fileupload.jsp");
            view.forward(request, response); 
           
           
          
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
