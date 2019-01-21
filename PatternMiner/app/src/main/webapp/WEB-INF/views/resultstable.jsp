<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 09.11.2018
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<article class="tile is-child notification is-dark">


    <h1 class="title">Patterns-Table</h1>

    <c:if test="${filteredResults.size() == 0 }">
        <h2 class="subtitle">No results match the given filters or empty table.</h2>
    </c:if>
    <c:if test="${filteredResults.size() > 0 }">
        <h2 class="subtitle is-horizontal">
            <div class="input-group">
                <span class="input-group-label">Found ${filteredResults.size()} sequential patterns.</span>
                <input type="button" class="button is-link" id="save-results" data-obj="" value="Export Table as JSON">
            </div>
        </h2>

        <table class="table fixed_header is-fullwidth is-striped is-hoverable is-gapless sortable" id="resultsTable">
            <thead>
            <tr>
                <th><a class="button" style="border:none;">#</a></th>
                <th><a class="button" style="border:none; justify-content: left">Sequential Pattern</a></th>
                <c:forEach var="group" items="${groups}">
                    <th><a class="button" style="border:none;">${group}</a></th>
                </c:forEach>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="seq" items="${filteredResults}" varStatus="keyNr" begin="0" end="99">
                <c:set var="keyNr" value="${keyNr}" scope="request"/>
                <c:set var="seq" value="${seq}" scope="request"/>
                <c:import url="sequence.jsp"/>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

</article>