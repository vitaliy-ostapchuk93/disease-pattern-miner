<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 09.11.2018
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<article class="tile is-child notification is-dark">
    <h1 class="title">Results Filter</h1>

    <button class="button" id="toggleFilter"
            style="position: absolute; right: .5rem; top: .5rem; border-radius: 290486px;">
        <i class="fa fas fa-chevron-circle-down"></i>
    </button>

    <article class="media">
        <div class="media-content targetFilter">

            <div class="columns is-multiline">
                <c:forEach var="filter" items="${requestScope.MAPPER.filterList}" varStatus="loop">
                    <c:set var="filter" value="${filter}" scope="request"/>
                    <div class="column is-one-fifth">
                        <article class="message is-link">
                            <div class="message-header">
                                <c:import url="filterhead.jsp"/>
                            </div>
                            <div class="message-body">
                                <c:import url="filterparameter.jsp"/>
                            </div>
                        </article>
                    </div>
                </c:forEach>

                <div class="column is-one-fifth">
                    <article class="message is-dark">
                        <form action="resultsfilter" method="post">
                            <div class="message-body has-background-dark" style="border-radius: 0px;">
                                <div class="level-left">
                                    <div class="level-item">
                                        <input type="submit" class="button is-success is-medium is-fullwidth"
                                               name="addFilter" value="Add new Filter">
                                    </div>
                                </div>
                            </div>
                        </form>
                    </article>
                </div>

            </div>
        </div>
    </article>
</article>