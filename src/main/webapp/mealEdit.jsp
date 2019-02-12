<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a> / <a href="meals">Meals</a></h3>

<h2>Meal</h2>
<form action="meals" method="POST">
    <input type="hidden" name="id" value="${meal.id}" />
    <table>
        <tr>
           <td>Описание:</td>
            <td><input type="text" name="description" value="${meal.description}"/></td>
        </tr>
        <tr> <td>Дата:</td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}"/></td>
        </tr>
        <tr> <td>Калории:</td>
            <td><input type="number" name="calories" value="${meal.calories}"/></td>
        </tr>
        <tr>
            <td><input type="submit" name="update" value="Сохранить"/></td>
            <td><input type="submit" name="delete" value="Удалить"/></td>
        </tr>
    </table>
</form>
</body>
</html>