<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="./style/view.css" media="all">
<script type="text/javascript" src="./js/view.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Electronics TAsks</title>
</head>
<body>
	<img id="top" src="./images/top.png" alt="">
	<div id="form_container">
	
		<h1><a>Electronics Task Form</a></h1>
		<form id="form_1089583" class="appnitro"  method="post" action="elecTaskScheduler">
					<div class="form_description">
			<h2>Electronics Tasks</h2>
			<p></p>
		</div>						
			<ul >
			
					<li id="li_1" >
		<label class="description" for="element_1"> Select Task </label>
		<span>
		
	<input id="choice" name="choice" class="element radio" type="radio" value="edu" />
	<label class="choice" for="electronics">Electronics Data Update</label>
	<input id="choice" name="choice" class="element radio" type="radio" value="mpu" />
	<label class="choice" for="others">MSP Product Update</label>
	<input id="choice" name="choice" class="element radio" type="radio" value="spec" />
	<label class="choice" for="others">Product Spec Update</label>
		</span> 
		</li>
			
					<li class="buttons">
			    <input type="hidden" name="form_id" value="1089583" />
			    
				<input id="saveForm" class="button_text" type="submit" name="submit" value="Start Task" />
		</li>
			</ul>
		</form>	
	
	</div>
	<img id="bottom" src="./images/bottom.png" alt="">	
</body>
</html>