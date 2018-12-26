<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <c:set var="headertitle" value="Results" scope="request"/>
    <title>${requestScope.headertitle}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <script src="https://unpkg.com/tippy.js@3/dist/tippy.all.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>


    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/balloon-css/0.5.0/balloon.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/static/css/tooltip.css"/>"/>
    <script defer src="<c:url value="/resources/static/js/resultscell.js"/>"></script>
    <script defer src="<c:url value="/resources/static/js/resultstable.js"/>"></script>

    <script defer src="<c:url value="/resources/static/js/cursor.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>

</head>
<body>
<jsp:include page="common/_header.jsp"/>
<jsp:include page="common/_menu.jsp"/>

<section class="hero">
    <div class="hero-body">
        <div class="tile">
            <div class="tile is-parent is-vertical">

                <c:if test="${not empty requestScope.MAPPER}">
                    <c:if test="${not empty requestScope.MAPPER.resultsTable}">
                        <c:set var="filteredResults" value="${requestScope.MAPPER.filteredResultsAsSortedMap}"
                               scope="request"/>
                        <c:set var="groups" value="${requestScope.MAPPER.allGroups}" scope="request"/>
                        <c:set var="highestRelSupValue" value="${requestScope.MAPPER.highestRelSupValue}"
                               scope="request"/>
                        <c:set var="highestAbsSupValue" value="${requestScope.MAPPER.highestAbsSupValue}"
                               scope="request"/>

                        <jsp:include page="resultsfilter.jsp"/>
                        <jsp:include page="icdgroups.jsp"/>
                        <jsp:include page="resultstable.jsp"/>

                    </c:if>
                </c:if>

            </div>
        </div>

    </div>
</section>


<jsp:include page="common/_footer.jsp"/>
</body>
</html>
