
<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 09.11.2018
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form action="${pageContext.request.contextPath}/filterItem" method="post">
    <input type="hidden" name="filterID" value="${requestScope.filter.id}"/>

    <c:choose>

        <c:when test="${requestScope.filter.filterType == 'REL_SUP'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="minRelSup">
                            Minimum rel. support: <fmt:formatNumber type="number" minFractionDigits="5"
                                                                    maxFractionDigits="5"
                                                                    value="${requestScope.filter.filterRelSup}"/>
                        </label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="minRelSup" id="minRelSup"
                               oninput='this.form.submit()' min="0" max="${highestRelSupValue}"
                               step="${highestRelSupValue/100}"
                               value="${requestScope.filter.filterRelSup}">
                    </span>
                    <br>
                    <p>
                        Chose a min. relative support for each cell.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'ABS_SUP'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="minAbsSup">
                            Minimum abs. support: ${requestScope.filter.filterAbsSup}
                        </label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="minAbsSup" id="minAbsSup"
                               oninput='this.form.submit()' min="0" max="${highestAbsSupValue}" step="1"
                               value="${requestScope.filter.filterAbsSup}">
                    </span>
                    <br>
                    <p>
                        Choose a min. absolute support for each cell.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'TOP_N_SUP_ALL'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="topNSupAllSlider">Top N Support (All): ${requestScope.filter.filterTopNAll}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="topNSupAllSlider" id="topNSupAllSlider"
                               oninput='this.form.submit()' min="1" max="100" step="1"
                               value="${requestScope.filter.filterTopNAll}">
                    </span>
                    <br>
                    <p>
                        Choose a number of rows with highest support values in table.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'TOP_N_SUP_GROUP'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="topNSupGroupSlider">Top N Support (Groups): ${requestScope.filter.filterTopNGroups}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="topNSupGroupSlider" id="topNSupGroupSlider"
                               oninput='this.form.submit()' min="1" max="20" step="1"
                               value="${requestScope.filter.filterTopNGroups}">
                    </span>
                    <br>
                    <p>
                        Choose a number of rows with highest support value for each age-gender-group.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'GROUPS_COUNT'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="minGroupsCountSlider">MinGroupsCount: ${requestScope.filter.filterGroupsCount}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="minGroupsCountSlider" id="minGroupsCountSlider"
                               oninput='this.form.submit()' min="1" max="${fn:length(requestScope.MAPPER.allGroups)}"
                               step="1" value="${requestScope.filter.filterGroupsCount}">
                    </span>
                    <br>
                    <p>
                        Choose a minimum number of columns (age-gender-groups) in each row.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'GENDER'}">
            <div class="field is-grouped is-grouped-multiline is-medium">
                <c:forEach var="gender" items="${requestScope.filter.filterGender}" varStatus="loop">
                    <div class="control">
                        <div class="tags has-addons">
                            <span class="tag is-link is-medium">
                                <label for="gender">${gender.key}</label>
                            </span>
                            <span class="tag is-link is-medium">
                                <input type="radio" name="gender" id="gender" value="${gender.key}"
                                       oninput='this.form.submit()'
                                       <c:if test="${gender.value == true}">checked</c:if>>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="control">
                <p>
                    <br>
                    Select a gender to filter.
                </p>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'AGE'}">
            <div class="field is-grouped is-grouped-multiline is-medium">
                <c:forEach var="age" items="${requestScope.filter.filterAgeGroups}" varStatus="loop">
                    <div class="control">
                        <div class="tags has-addons">
                            <span class="tag is-link is-medium">
                                <label for="age">${age.key}</label>
                            </span>
                            <span class="tag is-link is-medium">
                                <input type="checkbox" name="age" id="age" value="${age.key}"
                                       oninput='this.form.submit()' <c:if test="${age.value == true}">checked</c:if>>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="control">
                <p>
                    <br>
                    Select age-groups to filter.
                </p>
            </div>
        </c:when>


        <c:when test="${requestScope.filter.filterType == 'TOP_PERCENT_ALL'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="topPSupAllSlider">Top Percent Support (All): ${requestScope.filter.filterTopPAll}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="topPSupAllSlider" id="topPSupAllSlider"
                               oninput='this.form.submit()' min="1" max="25" step="0.1"
                               value="${requestScope.filter.filterTopPAll}">
                    </span>
                    <br>
                    <p>
                        Select a percentage of highest support values in table to be displayed.
                    </p>
                </div>
            </div>
        </c:when>

        <c:when test="${requestScope.filter.filterType == 'TOP_PERCENT_GROUP'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="topPSupGroupSlider">Top Percent Support (Groups): ${requestScope.filter.filterTopPGroups}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="topPSupGroupSlider" id="topPSupGroupSlider"
                               oninput='this.form.submit()' min="0.01" max="10" step="0.01"
                               value="${requestScope.filter.filterTopPGroups}">
                    </span>
                    <br>
                    <p>
                        Select a percentage of highest support values in each group to be displayed.
                    </p>
                </div>
            </div>
        </c:when>


        <c:when test="${requestScope.filter.filterType == 'PATTERN'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="pattern">Pattern:</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input class="input is-info" name="pattern" id="pattern" type="text"
                               value="${requestScope.filter.filterPattern}" onchange='this.form.submit()'>
                    </span>
                </div>
                <br>
                <p>
                    - Use ^X for start with Pattern X.<br>
                    - Use (.*) for any Pattern.<br>
                    - Use X$ for end with Pattern X.<br>
                </p>
            </div>
        </c:when>


        <c:when test="${requestScope.filter.filterType == 'SEQ_LENGTH'}">
            <div class="control ">
                <div class="tags has-addons has-background-white-bis">
                    <span class="tag is-link is-medium">
                        <label for="minGroupsCountSlider">MinSeqLength: ${requestScope.filter.filterSeqLength}</label>
                    </span>
                    <span class="tag is-link is-medium">
                        <input type="range" name="minSeqLengthSlider" id="minSeqLengthSlider"
                               oninput='this.form.submit()' min="3" max="${requestScope.MAPPER.highestPatternLength}"
                               step="1" value="${requestScope.filter.filterSeqLength}">
                    </span>
                    <br>
                    <p>
                        Select the min. length a pattern should have.
                    </p>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="control">
                UNDEFINED FILTER OPERATION
            </div>
        </c:otherwise>

    </c:choose>
</form>