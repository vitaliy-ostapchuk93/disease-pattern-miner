<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <c:set var="headertitle" value="Dashboard" scope="request"/>
    <title>${requestScope.headertitle}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

</head>
<body>
    <jsp:include page="common/_header.jsp"></jsp:include>
    <jsp:include page="common/_menu.jsp"></jsp:include>

    <section class="hero">
        <div class="hero-body">
            <div class="container">
                <section class="section">
                    <div class="columns">
                        <div class="column">
                            <h1 class="title is-1"><b>Disease Pattern Miner</b> is a free, open-source mining-framework
                                for
                                discovering sequential patterns in medical health records.</h1>
                            <br>
                            <nav class="coulums">
                                <a class="button is-dark is-large"
                                   href="https://github.com/vitaliy-ostapchuk93/disease-pattern-miner">
                                    <span class="icon">
                                        <i class="fab fa-github"></i>
                                    </span>
                                    <span>
                                        View on Git
                                    </span>
                                </a>
                                <a class="button is-light is-large" href="#">
                                    <span>
                                        <span>View</span>
                                        <strong>docs</strong>
                                    </span>
                                </a>
                                <a class="button is-light is-large" href="#">
                                    <span>
                                        <span>Read</span>
                                        <strong>Publication</strong>
                                    </span>
                                </a>
                            </nav>
                        </div>
                        <div class="column">
                            <img src="<c:url value="/resources/static/img/logo.png"/>" type="image/x-icon">
                        </div>
                    </div>
                </section>

                <section class="section">
                    <h1 class="title is-3">Explore <b>Sequential-Disease-Patterns</b> found by different mining
                        algorithms.</h1>

                    <h2 class="subtitle">This pattern mining framework, can be used to run different mining algorithms
                        on
                        collections of electronic patient records and visualize the results in a meaningful
                        and interpretable way.</h2>

                    <div class="column">
                        <div class="content is-medium">
                            <ul>
                                <li>
                                    <p>Data from Taiwan’s National Health Insurance Research Database which contains <b>782
                                        million outpatient visits</b> and <b>15 394 unique phenotypes</b> that were
                                        observed in the
                                        entire Taiwanese population of <b>over 22 million individuals</b>.</p>
                                </li>
                                <li>
                                    <p>Results from <b>15 different sequence-mining algorithms</b> from different
                                        libraries collected in a single table.</p>
                                    <div class="section">
                                        <img src="<c:url value="/resources/static/img/pattern-table.PNG"/>"
                                             type="image/x-icon">
                                    </div>
                                </li>
                                <li>
                                    <p>Filtering options for the found patterns to find meaningful results.</p>
                                </li>
                                <li>
                                    <p>Interactive methods to explore <b>relations between diseases</b> in different
                                        gender-age-groups.</p>
                                    <div class="section">
                                        <img src="<c:url value="/resources/static/img/sankey_2%204%206_5M.png"/>"
                                             type="image/x-icon">
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </section>

    <jsp:include page="common/_footer.jsp"></jsp:include>
</body>
</html>
