<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<s:if test="hasActionErrors()">
   <div class="errors">
      <s:actionerror/>
   </div>
   </s:if>
  <s:form action="login">
 
      <s:textfield name="user.username" label="Username" />
      <s:password  name="user.password" label="Password" />
     
       
      <s:submit/>
       
</s:form> 
 
</body>
</html>