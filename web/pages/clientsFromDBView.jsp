<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../resources/script/redirectTool.js"></script>
    <link rel="stylesheet" href="../resources/style/resultTableStyle.css">
</head>
<body>
<div class="resultTable">
    <table align="center">
        <tr>
            <td>User City</td>
            <td>User Name</td>
            <td>Balance</td>
        </tr>
        <c:forEach var="client" items="${requestScope.clientsList}">
            <tr>
                <td>${client.city}</td>
                <td>${client.name}</td>
                <td>${client.activeAccount.balance}</td>
                <td>
                    <form action="../pages/editClient.jsp" method="post">
                        <input type="submit" name="editClient" value="Edit client">
                        <input type="hidden" name="id" value="${client.id}">
                        <input type="hidden" name="name" value="${client.name}">
                        <input type="hidden" name="city" value="${client.city}">
                        <input type="hidden" name="gender" value="${client.sex}">
                        <input type="hidden" name="email" value="${client.email}">
                        <input type="hidden" name="accountID" value="${client.activeAccount.id}">
                        <input type="hidden" name="balance" value="${client.activeAccount.balance}">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
