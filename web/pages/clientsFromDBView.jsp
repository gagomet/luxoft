<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
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
                <td><form onsubmit="ADD ACTION HERE">
                    <input type="submit" name="editClient" value="Edit client">
                </form></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
