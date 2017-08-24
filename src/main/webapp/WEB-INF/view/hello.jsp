<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/7/29 0029
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Hello</title>
</head>
<body>

<table border=1 width=400>
    <tr align=center >
        <td>姓名</td>
        <td>性别</td>
        <td>电话</td>
        <td>省</td>
        <td>市</td>
        <td>来源</td>
        <td>注册时间</td>
    </tr>
    <c:forEach items="${list}" var="t" varStatus="s">
        <tr align=center>
            <td><c:out value="${t.userName}"/></td>
            <td><c:out value="${t.sex}"/></td>
            <td><c:out value="${t.phone}"/></td>
            <td><c:out value="${t.province}"/></td>
            <td><c:out value="${t.city}"/></td>
            <td><c:out value="${t.source}"/></td>
            <td><c:out value="${t.createTs}"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
