<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="./style/view.css" media="all">
<script type="text/javascript" src="./js/view.js"></script>
<link href = "http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css
         " rel = "stylesheet">
      <script src = "http://code.jquery.com/jquery-1.10.2.js"></script>
      <script src = "http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		
      <script>
         $(function() {
            $( "#tabs-1" ).tabs();
         });
      </script>
		
      <style>
         #tabs-1{font-size: 14px;}
         .ui-widget-header {
            background:#b9cd6d;
            border: 1px solid #b9cd6d;
            color: #FFFFFF;
            font-weight: bold;
         }
      </style>




<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Electronics TAsks</title>
</head>
<body>
	<img id="top" src="./images/top.png" alt="">
	<div id="form_container">
	
		<h1><a>Electronics Task Form</a></h1>
		
		<div class="form_description">
			<h2>Electronics Tasks</h2>
			<p></p>
		</div>						
			<ul >
			<li class="buttons">
			    
		</li>
			</ul>
	 <div id = "tabs-1">
         <ul>
            <li><a href = "#tabs-2">Electronics Data Update</a></li>
            <li><a href = "#tabs-3">MSP Product Update</a></li>
            <li><a href = "#tabs-4">Product Spec Update</a></li>
         </ul>
			
         <div id = "tabs-2">
         <form id="form_1089583" class="appnitro"  method="post" action="elecTaskScheduler">
            <p>
             <input id="choice" name="choice" class="element radio" type="radio" value="edu" />
	         <label class="choice" for="electronics">Electronics Data Update</label>
	         <input id="saveForm" class="button_text" type="submit" name="submit" value="Start Task" />
	         
	       </p>
          </form>	
         </div>
			
         <div id = "tabs-3">
          
            <form id="form_1089583" class="appnitro"  method="post" action="elecTaskScheduler">
            <p>
           	<input id="choice" name="choice" class="element radio" type="radio" value="mpu" />
			<label class="choice" for="others">MSP Product Update</label>
	         <input id="saveForm" class="button_text" type="submit" name="submit" value="Start Task" />
	         <select name="cars" multiple width="200px" class="button_text">
				  <option value="volvo">Volvo</option>
				  <option value="saab">Saab</option>
				  <option value="opel">Opel</option>
				  <option value="audi">Audi</option>
				</select>
	         
	       </p>
          </form>
          
         </div>
			
         <div id = "tabs-4">
          
          <form id="form_1089583" class="appnitro"  method="post" action="elecTaskScheduler">
            <p>
             <input id="choice" name="choice" class="element radio" type="radio" value="spec" />
			<label class="choice" for="others">Product Spec Update</label>
	         <input id="saveForm" class="button_text" type="submit" name="submit" value="Start Task" />
	       </p>
          </form>
                
         </div>
			
      </div>
		
		
			
		
		
	</div>
	<img id="bottom" src="./images/bottom.png" alt="">	
</body>
</html>