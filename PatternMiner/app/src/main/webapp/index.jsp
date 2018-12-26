<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Disease Pattern Miner</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <link rel="shortcut icon" href="<c:url value="/favicon.ico"/>">
    <link rel="icon" href="<c:url value="/favicon.ico"/>" type="image/x-icon">

  </head>
  <body>
    <jsp:forward page="WEB-INF/views/dashboard.jsp"/>
  </body>
</html>