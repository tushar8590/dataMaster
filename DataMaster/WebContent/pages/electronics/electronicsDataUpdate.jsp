<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Electronics Data Update</title>
</head>
<script src="http://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
	<script>
	$(document).ready(function() {
      //  $('#userName').blur(function(event) {
               
                $.get('GetUserServlet', {
                }, function(responseText) {
                        $('#ajaxGetUserServletResponse').text(responseText);
             //   });
        });
});
	</script>
<body>
   <s:property value="message" />
   <div id="ajaxGetUserServletResponse">
   </div>
</body>
</html>