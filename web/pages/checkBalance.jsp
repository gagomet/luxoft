<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page errorPage="/pages/error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Balance</title>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="../resources/style/resultTableStyle.css">
</head>
<body>

<c:choose>
    <c:when test="${requestScope.success == null}">
        <h2 class="message">Balance</h2>
    </c:when>
    <c:otherwise>
        <c:set var="message" value="${requestScope.success}"/>
        <c:set var="amount" value="${requestScope.amount}"/>
        <h2 class="message">${message} to amount ${amount}</h2>
    </c:otherwise>
</c:choose>
<div class="resultTable">
    <table align="center">
        <tr>
            <td>User Name</td>
            <td>Account ID</td>
            <td>Balance</td>
        </tr>

        <c:set var="client" value="${requestScope.clientFromDb}" />
            <tr>
                <td>${client.name}</td>
                <td>${client.activeAccount.id}</td>
                <td>${client.activeAccount.balance}</td>
            </tr>
    </table>

    <div>
        <form align="center" action="${pageContext.request.contextPath}/pages/menu.html">
            <input class="backButton" type="submit" value="Back to main menu">
        </form>
    </div>
</div>
</body>
</html>