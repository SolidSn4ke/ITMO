<%@ page contentType="text/html; Windows-1251" pageEncoding="Windows-1251" import="java.util.ArrayList"%>
<html>

<head>
    <link rel="stylesheet" href="index.css">
    <script defer src="index.js"></script>
</head>

<body onload="
<% ServletContext context = getServletContext();
		ArrayList<String> tableValues = null;
		if(context.getAttribute("tableValues") == null){
			tableValues = new ArrayList<>();
		} else {
			tableValues = (ArrayList<String>) context.getAttribute("tableValues");
		}
		String res = (String) context.getAttribute("res");
        if(res != null){
            tableValues.add(res);
            tableValues.add((String) context.getAttribute("x"));
            tableValues.add((String) context.getAttribute("y"));
            tableValues.add((String) context.getAttribute("r"));
        }
		context.setAttribute("tableValues", tableValues);
        context.setAttribute("res", null);
		if(tableValues.size() > 0){
            for(int i=0; i < tableValues.size(); i+=4){
                    res = tableValues.get(i);
                	String x = tableValues.get(i+1);
		            String y = tableValues.get(i+2);
		            String r = tableValues.get(i+3);
%> 
                    updateTable('<%= res %>', '<%= x %>', '<%= y %>', '<%= r %>') 
<%          }
        } 
%>
">
    <div id="container">

        <header>
            <h1>Кузьмин Артемий Андреевич P3214 408941</h1>
        </header>

        <div id="wrapper" class="row">
            <div id="left_input" class="column">
                <p>
                    x - координата точки по оси ox<br>
                    <input type="button" name="x" value="-2" class="x_button">
                    <input type="button" value="-1.5" name="x" class="x_button">
                    <input type="button" value="-1" name="x" class="x_button">
                    <input type="button" value="-0.5" name="x" class="x_button">
                    <input type="button" value="0" name="x" class="x_button">
                    <input type="button" name="x" value="0.5" class="x_button">
                    <input type="button" value="1" name="x" class="x_button">
                    <input type="button" value="1.5" name="x" class="x_button">
                    <input type="button" value="2" name="x" class="x_button">
                </p>
                <form action="http://localhost:8080/webapp-1.0-SNAPSHOT/controller-servlet" method="post" onsubmit="dataTransfer()">
                <p>
                    <label class="error" id="x_error"></label>
                    <input type="hidden" name="x" id="x" value="">
                </p>
                <p>
                    y - координата точки по оси oy (от -3 до 5)<br>
                    <input type="text" id="y" name="y" placeholder="[-3..5]">
                    <label for="y" class="error" id="y_error"></label>
                </p>
                <p>
                    R - величина R на графике (от 1 до 3)<br>
                    <select name="R" id="R" onchange="    
                    if(RInput.value >= 1 && RInput.value <= 3 ){
                        let dots = document.getElementsByClassName('tmpDot');
                        while(dots[0]) {
                            dots[0].parentNode.removeChild(dots[0]);
                        }
<%
                        for(int i = 0; i < tableValues.size(); i+=4){
                            %>
                            addDot(<%= tableValues[i+1] %>, <%= tableValues[i+2] %>, RInput.value)
<%
                        }
%>
                    }">
                        <option value="">Выберите значение</option>
                        <option value="1">1</option>
                        <option value="1.5">1.5</option>
                        <option value="2">2</option>
                        <option value="2.5">2.5</option>
                        <option value="3">3</option>
                    </select>
                    <label for="R" class="error" id="R_error"></label>
                </p>
                <p>
                    <input type="submit" value="Подтвердить" id="confirm">
                </p>
            </form>
            </div>

            <div id="center_graphic" class="column">
                <svg width="300" height="300" id="graph" >
                    <rect x="150" y="90" width="120" height="60" fill="lightblue" />
                    <polygon points="150,150 150,90 30,150" fill="lightblue" />
                    <path d="M90 150
                    A 60 60, 0, 0, 0, 150 210
                    L 150 150 Z"
                    fill="lightblue" stroke="none" />
                    <line x1="150" y1="0" x2="150" y2="300" stroke="black" />
                    <line x1="300" y1="150" x2="290" y2="155" stroke="black" />
                    <line x1="300" y1="150" x2="290" y2="145" stroke="black" />
                    <line x1="0" y1="150" x2="300" y2="150" stroke="black" />
                    <line x1="150" y1="0" x2="145" y2="10" stroke="black" />
                    <line x1="150" y1="0" x2="155" y2="10" stroke="black" />
                    <line x1="210" y1="145" x2="210" y2="155" stroke="black" />
                    <line x1="270" y1="145" x2="270" y2="155" stroke="black" />
                    <line x1="90" y1="145" x2="90" y2="155" stroke="black" />
                    <line x1="30" y1="145" x2="30" y2="155" stroke="black" />
                    <line x1="145" y1="270" x2="155" y2="270" stroke="black" />
                    <line x1="145" y1="210" x2="155" y2="210" stroke="black" />
                    <line x1="145" y1="90" x2="155" y2="90" stroke="black" />
                    <line x1="145" y1="30" x2="155" y2="30" stroke="black" />
                    <text x="280" y="145" fill="black">x</text>
                    <text x="155" y="10" fill="black">y</text>
                    <circle cx="150" cy="150" r="0" fill="red" />
                  </svg>
            </div>

            <div id="right_table" class="column">
                <table id="results">
                    <caption>Результаты</caption>
                    <thead>
                        <tr>
                            <th scope="col">№</th>
                            <th scope="col">Попадание</th>
                            <th scope="col">x</th>
                            <th scope="col">y</th>
                            <th scope="col">R</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <label for="results" class="error" id="ser_error"></label>
                <form action="http://localhost:8080/webapp-1.0-SNAPSHOT/controller-servlet" method="post">
                    <input type="hidden" name="clear" value="true">
                    <input type="submit" value="очистить">
                </form>
            </div>
        </div>
    </div>
</body>
</html>