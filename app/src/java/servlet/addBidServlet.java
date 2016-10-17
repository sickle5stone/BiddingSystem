/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controller.BidController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Haseena and Regan
 */
@WebServlet(name = "addBidServlet", urlPatterns = {"/addBidServlet"})
public class addBidServlet extends HttpServlet {

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
            out.println("<title>Servlet addBidServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addBidServlet at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        //HttpSession session = request.getSession();
        //String userId = (String) session.getAttribute("studentUser");
         HttpSession session = request.getSession();
         String userID=(String) session.getAttribute("student");
       //  String userID="amy.ng.2016";
         
         //Get the inputs from addBid.jsp
         String courseCode=request.getParameter("courseCode");
         String sectionCode=request.getParameter("sectionCode");
         String amt=request.getParameter("amount");
         amt=amt.replaceAll(" ","");
         double amount=Double.parseDouble(amt); 
         

         
      //   ArrayList<String> errorList=bc.addBid(userID, amount, sectionCode, courseCode);
         
         //test code
        ArrayList<String> errorList = BidController.addBid(userID, amount,sectionCode,courseCode);
        for(String error: errorList){
            System.out.println(error);
            
        }
      
       // errorList.add("error 1");
     //   errorList.add("error 2");
        
        //String [] errorArr = new String[errorList.size()];
        //errorArr = errorList.toArray(errorArr);
         
         //There is no error, can proceed to bid
         if(errorList.isEmpty()){
              //Returns the success message to the bid page
                session.setAttribute("bidSuccessMessage","Bid Successful!");
          /*  RequestDispatcher view = request.getRequestDispatcher("studenthome.jsp");
              view.forward(request, response);  */
                response.sendRedirect("studenthome.jsp");
              
          
         }else{
                      //Returns the error message to the bid page
                session.setAttribute("bidFailMessages",errorList);
                    //request.setAttribute("bidFailMessages",errorList);
                    //RequestDispatcher view = request.getRequestDispatcher("searchSection.jsp");
                    //view.forward(request, response);
                response.sendRedirect("studenthome.jsp");
           
          // RequestDispatcher view = request.getRequestDispatcher("studenthome.jsp");
           
          
         }
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
