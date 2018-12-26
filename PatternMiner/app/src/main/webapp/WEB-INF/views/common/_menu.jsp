<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Main container -->

<section class="section">
    <div class="container is-fluid">
        <nav class="navbar" role="navigation" aria-label="dropdown navigation">

            <a href="<c:url value="/dashboard"/>"
               class="panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Dashboard'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fas fa-chalkboard-teacher" aria-hidden="true"></i>
                </span>
                Dashboard
            </a>

            <a href="<c:url value="/data"/>"
               class="navbar-item panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Data'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fas fa-book" aria-hidden="true"></i>
                </span>
                Data
            </a>

            <a href="<c:url value="/algorithms"/>"
               class="navbar-item panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Algorithms'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fas fa-code" aria-hidden="true"></i>
                </span>
                Algorithms
            </a>

            <a href="<c:url value="/results"/>"
               class="navbar-item panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Results'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fas fa-receipt" aria-hidden="true"></i>
                </span>
                Results
            </a>

            <a href="<c:url value="/explorer"/>"
               class="navbar-item panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Explorer'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fab fa-wpexplorer" aria-hidden="true"></i>
                </span>
                Explorer
            </a>


            <a href="<c:url value="/performance"/>"
               class="navbar-item panel-block is-size-3 <c:if test="${requestScope.headertitle == 'Performance'}">is-active</c:if>"
               style="border: none;">
                <span class="panel-icon is-medium" style="font-size: unset;">
                    <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
                </span>
                Performance
            </a>

        </nav>
    </div>
</section>