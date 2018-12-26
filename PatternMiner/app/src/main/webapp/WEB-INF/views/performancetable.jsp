<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 07.12.2018
  Time: 01:26
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table is-fullwidth is-striped is-hoverable is-gapless">
    <thead>
    <tr>
        <th><a class="button is-fullwidth" style="border:none;">#</a></th>
        <th><a class="button is-fullwidth" style="border:none;">Algorithm Type</a></th>
        <th><a class="button is-fullwidth" style="border:none;">MinSup</a></th>
        <th><a class="button is-fullwidth" style="border:none;">Input File</a></th>
        <th><a class="button is-fullwidth" style="border:none;">Output File</a></th>
        <th><a class="button is-fullwidth" style="border:none;">Start</a></th>
        <th><a class="button is-fullwidth" style="border:none;">End</a></th>
        <th><a class="button is-fullwidth" style="border:none;">Time in s.</a></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="entryLine" items="${STATS}" varStatus="loop" begin="0">
        <tr>
            <td>${loop.index+1}</td>
            <c:forEach var="entry" items="${fn:split(entryLine, ';')}">
                <td>${entry}</td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>