<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <c:set var="headertitle" value="Algorithms" scope="request"/>
    <title>${requestScope.headertitle}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

</head>
<body>
    <jsp:include page="common/_header.jsp"></jsp:include>
    <jsp:include page="common/_menu.jsp"></jsp:include>

    <section class="hero">
        <div class="hero-body">
            <div class="tile is-parent">
                <article class="tile is-child notification is-dark">
                    <h1 class="title">Select algorithms & parameters</h1>

                    <div class="tile is-parent has-background-white">
                        <c:if test="${empty requestScope.algorithmsList}">
                            <h2 class="subtitle has-text-black	">No algorithms created or run yet.</h2>
                        </c:if>
                        <c:if test="${not empty requestScope.algorithmsList}">

                            <table class="table is-fullwidth is-striped is-hoverable">
                                <thead>
                                <tr>
                                    <th><abbr title="listID">#</abbr></th>
                                    <th><abbr title="type">Type</abbr></th>
                                    <th><abbr title="param">Parameter</abbr></th>
                                    <th><abbr title="pauseOrRun">Pause / Run / Cancel</abbr></th>
                                    <th><abbr title="status">Status</abbr></th>
                                    <th><abbr title="timeCreated">Created</abbr></th>
                                    <th><abbr title="timeStart">Start</abbr></th>
                                    <th><abbr title="timeEnd">End</abbr></th>
                                    <th><abbr title="timeTotal">Total Time</abbr></th>

                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="algorithm" items="${requestScope.algorithmsList}">
                                    <c:set var="algorithm" value="${algorithm}" scope="request"/>
                                    <c:import url="algorithmitem.jsp"/>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                    <br>

                    <form action="algorithms" method="post">
                        <div class="field">
                            <p class="control">
                                <input type="submit" class="button is-medium is-success" name="addAlgorithm"
                                       value="Add new Algorithm">
                                <input type="submit" class="button is-medium is-success" name="createAlgorithmTestSuite"
                                       value="Create Test-Suite">

                                <input type="submit" class="button is-medium is-info" name="runAll" value="Run all">

                                <input type="submit" class="button is-medium is-warning" name="pauseAll"
                                       value="Pause all">

                                <input type="submit" class="button is-medium is-danger" name="cancelAll"
                                       value="Cancel all">
                            </p>
                        </div>
                    </form>

                </article>
            </div>
        </div>
    </section>



    <jsp:include page="common/_footer.jsp"></jsp:include>
</body>
</html>
