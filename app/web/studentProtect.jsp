<%-- 
    Document   : studentProtect
    Author     : Cheryl and Aloysius
--%>

<%
            String userId = (String) session.getAttribute("student"); 
            if (userId == null) {
                response.sendRedirect("loginpage.jsp");   //check whether the user has loged in 
                return;
            }
    
%>