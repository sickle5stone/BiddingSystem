<%-- 
    Document   : addBid
    Created on : Oct 4, 2016, 4:04:08 PM
    Author     : Haseena
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Add Bid</h1>
        <form method="POST" action="addBidServlet">
            CourseCode: <input type="text" name="courseCode" value="C001" /><br/>
            SectionCode: <input type="text" name="sectionCode" value="S1" /><br/>
            Bid Amount: <input type="text" name="amount" value="20" /><br/>
            <input type="submit" value="Submit" />
        </form>
        <%
            ArrayList<String> errors = (ArrayList<String>) request.getAttribute("bidFailMessages");
         
 
           if (errors!=null && errors.size() != 0) {
                //If there is no error, print success message
                for(String error: errors){
                    out.print(error+"error");
                }
           }else{
               String success = (String) request.getAttribute("bidSuccessMessage");
               out.print(success);
           }
        %>
    </body>
</html>
