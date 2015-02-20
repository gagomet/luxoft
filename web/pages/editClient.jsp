<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page errorPage="/pages/error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit client</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="../resources/style/resultTableStyle.css">
</head>
<body>
<c:set var="client" value="${requestScope.clientFromDb}" scope="page"/>
<form action="ADD ACTION HERE">
    <label>Name: </label>
    <input class="clientField" type="text" name="clientsName" value="${client.name}">
    <label>City: </label>
    <input class="clientField" type="text" name="clientsCity" value="${client.city}">
    <label>Gender: </label>
    <input type="radio" name="gender" ${client.gender=="MALE"?"checked":""}/>Male
    <input type="radio" name="gender" ${client.gender=="FEMALE"?"checked":""}/>Female
    <label>e-mail: </label>
    <input class="clientField" type="text" name="clientsEmail" value="${client.email}">
    <label>e-mail: </label>
    <input class="clientField" type="text" name="clientsBalance" value="${client.activeAccount.balance}">
    <input class="submitButton" type="submit" name="edit" value="Edit">
</form>
</body>
</html>
