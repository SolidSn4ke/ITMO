<%@ page contentType="text/html; Windows-1251" pageEncoding="Windows-1251" %>
<html>
    <head>
        <title>Результаты</title>
    </head>
    <body>
        <p>	
	<%
	ServletContext context = getServletContext();
	String res = (String) context.getAttribute("res");
	String x = (String) context.getAttribute("x");
	String y = (String) context.getAttribute("y");
	String r = (String) context.getAttribute("r");
	%>

	<table id="results">
                    <caption>Результаты</caption>
                    <thead>
                        <tr>
                            <th scope="col">Попадание</th>
                            <th scope="col">x</th>
                            <th scope="col">y</th>
                            <th scope="col">R</th>
			</tr>
                    </thead>
                    <tbody>
		<tr>
      		<th scope="row"><%= res %></th>
      		<td><%= x %></td>
      		<td><%= y %></td>
		<td><%= r %></td>
    		</tr>
                    </tbody>
                </table>
	<form action="http://localhost:8080/webapp-1.0-SNAPSHOT" method="GET">
	<input type="submit" value="Назад">
	</form>
	</p>
    </body>
</html>