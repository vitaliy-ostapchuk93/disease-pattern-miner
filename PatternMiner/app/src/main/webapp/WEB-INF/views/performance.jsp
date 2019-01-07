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

    <c:set var="headertitle" value="Performance" scope="request"/>
    <title>${requestScope.headertitle}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src='https://unpkg.com/simple-statistics@6.1.1/dist/simple-statistics.min.js'></script>

    <script defer src="<c:url value="/resources/static/js/performance.js"/>"></script>
    <script defer src="<c:url value="/resources/static/js/cursor.js"/>"></script>


</head>
<body>
<jsp:include page="common/_header.jsp"/>
<jsp:include page="common/_menu.jsp"/>

<section class="hero">
    <div class="hero-body">
        <div class="tile is-parent">
            <article class="tile is-child notification is-dark">
                <p class="title">Overview of algorithms performance</p>
                <input type='hidden' value='${requestScope.MainStatsJSON}' id='mainStatsJSON'>

                <div class="container is-fluid">
                    <div class="columns">
                        <div class="column" id="statsMinSup" style="height: 60%;"></div>
                        <div class="column" id="statsSeqCount" style="height: 60%;"></div>
                    </div>
                    <br>
                    <div class="colum" id="stats3D" style="height: 80%; padding: initial;"></div>
                    <br>
                </div>
            </article>
        </div>

    </div>
</section>


<jsp:include page="common/_footer.jsp"/>
</body>
</html>
