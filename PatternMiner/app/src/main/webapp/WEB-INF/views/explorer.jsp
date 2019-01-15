<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 30.11.2018
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <c:set var="headertitle" value="Explorer" scope="request"/>
    <title>${requestScope.headertitle}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.5/jspdf.min.js"></script>


    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/static/css/sankey.css"/>"/>
    <script defer src="<c:url value="/resources/static/js/cursor.js"/>"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/balloon-css/0.5.0/balloon.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/static/css/tooltip.css"/>"/>
    <script src="https://unpkg.com/tippy.js@3/dist/tippy.all.min.js"></script>

    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/lodash.js/0.10.0/lodash.min.js"></script>

</head>

<body>
<jsp:include page="common/_header.jsp"/>
<jsp:include page="common/_menu.jsp"/>

<section class="hero">
    <div class="hero-body">
        <div class="tile">
            <article class="tile is-child notification is-dark">
                <h1 class="title">Pattern-Explorer</h1>

                <c:if test="${not empty requestScope.patternKey}">
                    <input type="hidden" id="patternKey" value="${requestScope.patternKey}"/>
                    <input type="hidden" id="patternGender" value="${requestScope.patternGender}"/>

                    <div class="tile is-parent">
                        <article class="tile is-child notification is-one-quarter">
                            <div class="field">
                                <label class="label">Pattern</label>
                                <div class="control">
                                    <a class="button has-background-link" style="border:none;"
                                       data-tooltip="${requestScope.MAPPER.getPatternFormatted(requestScope.patternKey)}">
                                        <div class="buttons has-addons">
                                            <c:forEach var='pattern'
                                                       items='${requestScope.MAPPER.getPatternList(requestScope.patternKey)}'>
                                    <span class="button"
                                          style="background-color: ${requestScope.MAPPER.getPatternColor(pattern)}">${pattern}</span>
                                            </c:forEach>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </article>

                        <article class="tile is-child notification is-one-quarter">
                            <div class="field">
                                <label class="label">Gender</label>
                                <div class="control">
                                    <div class="select">
                                        <select name="gender" id="gender">
                                            <option
                                                    <c:if test="${requestScope.patternGender eq 'MALE'}">selected</c:if>
                                                    value="MALE">MALE
                                            </option>
                                            <option
                                                    <c:if test="${requestScope.patternGender eq 'FEMALE'}">selected</c:if>
                                                    value="FEMALE">FEMALE
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </article>

                        <article class="tile is-child notification is-fullwidth">
                            <div class="field is-fullwidth">
                                <label class="label" id="agelabel">Age</label>
                                <div class="control is-fullwidth">
                                    <input class="is-info is-fullwidth" type="range" name="ageValue" id="ageValue"
                                           min="0" max="90" step="1" value="${requestScope.ageValue}" list="tickmarks"
                                           style="width: 50%;"/>
                                    <datalist id="tickmarks">
                                        <option value="0" label="0-9">
                                        <option value="10">
                                        <option value="20">
                                        <option value="30" label="30-39">
                                        <option value="40">
                                        <option value="50">
                                        <option value="60" label="60-69">
                                        <option value="70">
                                        <option value="80">
                                        <option value="90" label="90+">
                                    </datalist>
                                </div>
                            </div>
                        </article>

                        <article class="tile is-child notification is-one-quarter">
                            <div class="field">
                                <label class="label">Export</label>
                                <a id="downloadPNG" download="_.png">
                                    <button class="button is-link" id="save-png" disabled>Save as IMG</button>
                                </a>
                                <a id="downloadJSON" download="_.json">
                                    <button class="button is-link" id="save-json" disabled>Save as JSON</button>
                                </a>
                            </div>
                        </article>
                    </div>

                    <div class="tile is-vertical has-background-white has-text-black-bis">
                        <br>
                        <div id="chart" class="is-fullwidth"></div>

                        <script src="<c:url value="/resources/static/js/d3.v3.min.js"/>"></script>
                        <script src="https://cdn.rawgit.com/Neilos/bihisankey/master/bihisankey.js"></script>
                        <script src="<c:url value="/resources/static/js/sankey.js"/>"></script>

                        <div class="column">
                            <h2 class="subtitle has-text-danger">Due to interpolation and different size of
                                age-gender-groups the
                                support values shown in the links can slightly differ from the found values.</h2>
                        </div>
                    </div>

                </c:if>

                <c:if test="${empty requestScope.patternKey}">
                    <h2>Select some pattern from <a href="<c:url value="/results"/>"><span class="icon is-small"><i
                            class="fas fa-receipt" aria-hidden="true"></i></span><span>Results</span></a> which you wish
                        to explore.</h2>
                </c:if>
            </article>
        </div>
    </div>
</section>


<jsp:include page="common/_footer.jsp"/>
</body>
</html>