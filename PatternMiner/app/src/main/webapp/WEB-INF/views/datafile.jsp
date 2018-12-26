<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 15.11.2018
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<article class="tile is-child notification is-light" style="margin-bottom: 0px; padding: 0.8rem 2.5rem 0.8rem 1.5rem;">
    <article class="media">
        <div class="media-content">
            <div class="columns is-multiline">
                <figure class="media-left">
                        <span class="tag is-small">
                            <form action="datafile" method="post">
                                <input type="hidden" name="fileName" value="${requestScope.file.name}"/>
                                <br>
                                <br>
                                <br>
                                <input type="checkbox" name="fileSelected${loop.index}" id="selected${loop.index}"
                                       style="transform: scale(2);"
                                       onchange='this.form.submit()'
                                       <c:if test="${requestScope.file.selected == true}">checked</c:if>>
                            </form>
                        </span>
                </figure>
                <div class="media-content">
                    <div class="message is-dark" style="margin-bottom: 0px">
                        <div class="message-body" style="padding: 0.25em 1.5em; margin-top: 5px; margin-bottom: 5px">
                            <strong>${requestScope.file['class'].simpleName} -> ${requestScope.file.name}</strong>

                            <br>
                            ${requestScope.file.path}
                        </div>
                    </div>

                    <c:if test="${requestScope.file['class'].simpleName eq 'MainDataFile' and not empty requestScope.file.sortedChildFiles}">
                        <c:forEach var="childfile" items="${requestScope.file.sortedChildFiles}" varStatus="loop">
                            <c:set var="file" value="${childfile}" scope="request"/>
                            <c:set var="loop" value="${loop}" scope="request"/>
                            <c:import url="datafile.jsp"/>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </article>
</article>