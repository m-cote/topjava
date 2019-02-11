<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table>
    <tr><th>Description</th><th>Date/time</th><th>Calories</th></tr>
<c:forEach var="meal" items="${meals}">
    <tr class=${meal.excess ? "mealWithExcess" : "mealWithoutExcess"} >
    <td><p><c:out value="${meal.description}"></c:out></p></td>
    <td><p>
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
        <fmt:formatDate pattern="${dateFormat}" value="${parsedDateTime}"/>
        </p></td>
    <td><p><c:out value="${meal.calories}"></c:out></p></td>

    </tr>
</c:forEach>
</table>
</body>
</html>