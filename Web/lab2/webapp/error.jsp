<%@ page contentType="text/html; Windows-1251" pageEncoding="Windows-1251" %>
<html>
    <head>
        <title>Сообщение об ошибке</title>
    </head>
    <body>
        <p>
            <%
            String errorMessage = (String) request.getSession().getAttribute("errorMessage");
           %>
           <%= errorMessage %>
	   <input type="button" onclick="window.history.back()" value="Назад">
        </p>
    </body>
</html>