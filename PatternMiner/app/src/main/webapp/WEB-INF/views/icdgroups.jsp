<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 04.12.2018
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<article class="tile is-child notification is-dark">
    <h1 class="title">ICD Group-Codes</h1>

    <button class="button" id="toggleICD"
            style="position: absolute; right: .5rem; top: .5rem; border-radius: 290486px;">
        <i class="fa fas fa-chevron-circle-down"></i>
    </button>

    <div class="field is-grouped is-grouped-multiline targetICD">

        <div class="control">
            <div class="tags has-addons is-medium">
                <span class="tag is-medium"
                      style="background-color: ${requestScope.DiagnosesGroupsHelper.getColorByGroup(-1)}">-1</span>
                <span class="tag is-medium">TIME_GAP ( <= 2 weeks)</span>
            </div>
        </div>

        <c:forEach var="icdgroup" items="${requestScope.DiagnosesGroupsHelper.getDiagnosesGroups()}">
            <c:if test="${icdgroup != 'TIME_GAP'}">
                <div class="control">
                    <div class="tags has-addons is-medium">
                        <span class="tag is-medium"
                              style="background-color: ${requestScope.DiagnosesGroupsHelper.getColorByGroup(icdgroup.ordinal())}">${icdgroup.ordinal()}</span>
                        <span class="tag is-medium">${icdgroup} ( ${requestScope.DiagnosesGroupsHelper.lookupMin(icdgroup)} - ${requestScope.DiagnosesGroupsHelper.lookupMax(icdgroup)})</span>
                    </div>
                </div>
            </c:if>
        </c:forEach>

    </div>

</article>