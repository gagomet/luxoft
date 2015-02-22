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
<form action="/edit" method="post">
    <table>
        <tr>
            <td><input class="clientField" type="hidden" name="clientsID" value="${param.id}"></td>
        </tr>
        <tr>
            <td><label>Name: </label>
            <input class="clientField" type="text" name="clientsName" value="${param.name}"><br>
        </tr>
        <tr>
            <label>City: </label>
            <input class="clientField" type="text" name="clientsCity" value="${param.city}"><br>
        </tr>
        <tr>
            <label>Gender: </label>
            <input type="radio" name="gender" ${param.gender=="MALE"?"checked":""}/>Male
            <input type="radio" name="gender" ${param.gender=="FEMALE"?"checked":""}/>Female<br>
        </tr>
        <tr>
            <label>e-mail: </label>
            <input class="clientField" type="text" name="clientsEmail" value="${param.email}"><br>
        </tr>
        <tr>
            <label>Active account ID: </label>
            <input class="clientField" type="text" name="clientsActiveAccountId" value="${param.accountID}"><br>
        </tr>
        <tr>
            <label>Balance: </label>
            <input class="clientField" type="text" name="clientsBalance" value="${param.balance}"><br>
        </tr>
        <tr>
            <input class="submitButton" id="green-button" type="submit" name="edit" value="Edit"><br>
        </tr>
    </table>
</form>
</body>
</html>
