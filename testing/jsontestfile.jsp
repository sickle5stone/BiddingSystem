<%-- 
    Document   : test
    Created on : Oct 18, 2016, 7:45:20 PM
    Author     : reganseah
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="json/authenticate"  method="post" >
            <input type='text' name='username' value ='admin' /><br />
            <input type='text' name='password' value='' />
            <input type='submit'/>
        </form>
    </body>
</html>