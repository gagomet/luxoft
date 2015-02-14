<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="../resources/style/menuStyle.css">
    <link rel="stylesheet" href="../resources/style/resultTableStyle.css">
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <title>ErrorPage</title>
</head>
<body>
<H1>
    OOPS! Something happened...
</H1>
<c:choose>
    <c:when test="${requestScope.error == null}">
        <h1 class="message"></h1>
    </c:when>
    <c:otherwise>
        <c:set var="errorText" value="${requestScope.error}"/>
        <h1 class="message">${errorText}</h1>
    </c:otherwise>
</c:choose>
<div>
    <form align="center" action="${pageContext.request.contextPath}/pages/menu.html">
        <input class="backButton" type="submit" value="Back to main menu">
    </form>
</div>
</body>
</html>
