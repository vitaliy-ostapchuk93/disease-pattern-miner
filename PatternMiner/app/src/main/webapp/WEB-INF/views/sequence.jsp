<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 13.11.2018
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<form name="patternForm" action="${pageContext.request.contextPath}/explorer" method="post" style="margin: 0px;">
    <input type="hidden" name="patternKey" value="${seq.key}"/>

    <tr>
        <td><a class="button is-fullwidth is-gapless" style="border:none;">${keyNr.index+1}</a></td>

        <td>
            <a class="button has-background-link" style="border:none;"
               data-tooltip="${requestScope.MAPPER.getPatternFormatted(seq.key)}">
                <div class="buttons has-addons">
                    <c:forEach var='item' items='${requestScope.MAPPER.getPatternArray(seq.key)}'>
                        <c:if test="${item eq '-1'}">
                            <span class="button"
                                  style="background-color: ${requestScope.DiagnosesGroupsHelper.getColorByGroup(item)}; border-right: 5px solid #3273dc; border-left: 5px solid #3273dc;">${item}</span>
                        </c:if>
                        <c:if test="${item ne '-1'}">
                            <span class="button"
                                  style="background-color: ${requestScope.DiagnosesGroupsHelper.getColorByGroup(item)}">${item}</span>
                        </c:if>

                    </c:forEach>
                </div>
            </a>

            <c:if test="${requestScope.MAPPER.tTestGenderDifference(seq.key) > 0 and requestScope.MAPPER.tTestGenderDifference(seq.key) <= 0.05}">
                <span class="button is-pulled-right" style="border-right: 5px; border-left: 5px;"
                      data-tooltip="t-Test is indicating difference in gender-groups."><i
                        class="fas fa-user-circle"></i></span>
            </c:if>
            <c:if test="${requestScope.MAPPER.inverseSearchFinished(seq.key)}">
                <span class="button is-pulled-right" style="border-right: 5px; border-left: 5px;"
                      data-tooltip="Inverse-Search completed."><i class="fas fa-search"></i></span>
            </c:if>
        </td>


        <c:forEach var="group" items="${groups}">
            <c:set var="entry" value="${requestScope.MAPPER.getResultsEntry(seq.key, group)}" scope="request"/>

            <c:if test="${empty entry}">
                <td></td>
            </c:if>

            <c:if test="${not empty entry}">
                <c:set var="itemStyle"
                       value="padding: .3em .1em; text-align: center; background-color: hsl(0, 100%, ${100-(30*entry.relSupportValue/highestRelSupValue)}%)"/>
                <input type="hidden" name="patternGroupKey" value="${group}"/>

                <td style="${itemStyle}" class="cell" id="${seq.key}-${group}">
                    <div class="tooltipbox">
                        <input type="submit" class="button is-fullwidth" style="border:none; ${itemStyle}"
                               value="<fmt:formatNumber type="number" minFractionDigits="5" maxFractionDigits="5" value="${entry.relSupportValue}"/>">
                        <div class="top">
                            <div class="tooltip">
                                Not loaded yet!
                            </div>
                        </div>
                    </div>
                </td>
            </c:if>
        </c:forEach>

    </tr>

</form>
