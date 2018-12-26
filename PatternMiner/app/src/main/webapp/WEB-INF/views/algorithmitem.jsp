<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 13.11.2018
  Time: 13:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<tr>
    <td>${algorithm.listID}</td>

    <td>
        <form action="${pageContext.request.contextPath}/algorithmitem" method="post">
            <input type="hidden" name="algorithmID" value="${algorithm.listID}"/>
            <div class="select is-small">
                <select name="algorithmType" onchange='this.form.submit()'
                        <c:if test="${algorithm.algorithmStatus.name() != 'CREATED'}">disabled</c:if>>
                    <option selected value="${type}">${algorithm.algorithmType}</option>

                    <c:forEach var="type" items="${algorithm.algorithmTypes}">
                        <c:if test="${type != algorithm.algorithmType}">
                            <option value="${type}">${type}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
        </form>
    </td>

    <td>
        <div class="is-grouped is-grouped-multiline">
            <c:forEach var="parameter" items="${algorithm.algorithmParameters}" varStatus="paramID" begin="0">
                <form action="${pageContext.request.contextPath}/algorithmitem" method="post">

                    <c:set var="id" value="${fn:replace(parameter.key, ' ', '')}"/>
                    <input type="hidden" name="algorithmID" value="${algorithm.listID}"/>
                    <input type="hidden" name="algorithmParamKey" value="${parameter.key}"/>
                    <input type="hidden" name="algorithmParamID" value="${paramID.index}"/>


                    <div class="tags has-addons">
                        <span class="tag is-medium">
                            <label for="${id}">${parameter.key}</label>
                        </span>

                        <span class="tag is-medium">
                            <c:choose>
                                <c:when test="${parameter.value.getClass().simpleName == 'Float'}">
                                    <input name="param_N${paramID.index}" class="input is-hovered" id="${id}"
                                           type="number" value="${parameter.value}" min="0.0001" max="0.9999"
                                           onchange='this.form.submit()'
                                           <c:if test="${algorithm.algorithmStatus.name() != 'CREATED'}">disabled</c:if>>
                                </c:when>
                                <c:when test="${parameter.value.getClass().simpleName == 'Integer'}">
                                    <input name="param_N${paramID.index}" class="input is-hovered" id="${id}"
                                           type="number" value="${parameter.value}" min="1" max="100"
                                           onchange='this.form.submit()'
                                           <c:if test="${algorithm.algorithmStatus.name() != 'CREATED'}">disabled</c:if>>
                                </c:when>
                                <c:when test="${parameter.value.getClass().simpleName == 'Boolean'}">
                                    <input name="param_C${paramID.index}" class="checkbox" id="${id}" type="checkbox"
                                           <c:if test="${parameter.value == true}">checked</c:if>
                                           onchange='this.form.submit()'
                                           <c:if test="${algorithm.algorithmStatus.name() != 'CREATED'}">disabled</c:if>>
                                </c:when>
                            </c:choose>
                        </span>
                    </div>


                </form>
            </c:forEach>
        </div>
    </td>

    <td>
        <c:if test="${algorithm.algorithmStatus.name() != 'FINISHED' and algorithm.algorithmStatus.name() != 'CANCELED' and algorithm.algorithmStatus.name() != 'FAILED'}">
            <form action="${pageContext.request.contextPath}/algorithmitem" method="post">
                <input type="hidden" name="algorithmID" value="${algorithm.listID}"/>

                <div class="field has-addons">
                    <p class="control">
                        <c:choose>
                            <c:when test="${algorithm.algorithmStatus.name() == 'RUNNING'}">
                                <button type="submit" class="button is-warning is-fullwidth" name="pause">
                                <span class="icon is-small">
                                    <i class="fas fa-step-forward"></i>
                                </span>
                                    <span>Stop</span>
                                </button>
                            </c:when>
                            <c:when test="${algorithm.algorithmStatus.name() == 'PAUSED' or algorithm.algorithmStatus.name() == 'CREATED'}">
                                <button type="submit" class="button is-info is-fullwidth" name="run">
                                <span class="icon is-small">
                                    <i class="fas fa-step-forward"></i>
                                </span>
                                    <span>Run</span>
                                </button>
                            </c:when>
                        </c:choose>


                    </p>
                    <p class="control">
                        <button type="submit" class="button is-danger" name="cancel">
                        <span class="icon is-small">
                            <i class="fas fa-stop-circle"></i>
                        </span>
                            <span>Cancel</span>
                        </button>
                    </p>
                </div>
            </form>
        </c:if>
    </td>

    <td>${algorithm.algorithmStatus}</td>
    <td>${algorithm.timeCreated}</td>
    <td>${algorithm.timeStart}</td>
    <td>${algorithm.timeEnd}</td>
    <td>${algorithm.timeTotal}</td>


</tr>
