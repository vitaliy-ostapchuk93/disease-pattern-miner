<%--
  Created by IntelliJ IDEA.
  User: vitaliy
  Date: 15.01.2019
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<article class="tile is-child notification is-dark">
    <h1 class="title">(Un)Select the datasets</h1>


    <select id="optData" multiple="multiple">

        <c:forEach var="file" items="${requestScope.DATAFILES}" varStatus="loop">
            <optgroup label="${file.name}">
                <c:forEach var="childfile" items="${file.sortedChildFiles}" varStatus="loop">
                    <option value="${childfile.name}"
                            <c:if test="${childfile.selected == true}">selected</c:if> >${childfile.getGendederAgeGroup()}
                        - ${childfile.name} - ${childfile.getLengthInMB()} MB
                    </option>
                </c:forEach>
            </optgroup>
        </c:forEach>

    </select>

</article>
