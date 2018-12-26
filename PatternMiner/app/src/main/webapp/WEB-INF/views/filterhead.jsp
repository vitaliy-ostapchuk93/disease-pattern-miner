<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 10.11.2018
  Time: 13:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>
    [${requestScope.filter.id}]
</p>

<div class="select">
    <form action="${pageContext.request.contextPath}/filterItem" method="post">
        <input type="hidden" name="filterID" value="${requestScope.filter.id}"/>
        <select name="resultFilterType" onchange='this.form.submit()'>
            <option selected value="${requestScope.filter.filterType}">${requestScope.filter.filterType}</option>
            <c:forEach var="type" items="${requestScope.filter.filterTypes}">
                <c:if test="${type != requestScope.filter.filterType}">
                    <option value="${type}">${type}</option>
                </c:if>
            </c:forEach>
        </select>
    </form>
</div>
<form action="${pageContext.request.contextPath}/filterItem" method="post">
    <input type="hidden" name="filterID" value="${requestScope.filter.id}"/>
    <button class="delete" type="submit" name="deleteFilter"></button>
</form>