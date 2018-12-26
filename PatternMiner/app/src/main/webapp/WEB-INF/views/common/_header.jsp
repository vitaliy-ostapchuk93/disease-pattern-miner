<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <section class="hero is-dark is-bold">
        <div class="hero-body">
            <div class="container is-fluid">
                <div class="columns">
                    <div>
                        <img src="<c:url value="/resources/static/img/logo.png"/>" alt="Disease-Pattern-Miner" width="80">
                    </div>
                    <div>
                        <h1 class="title">
                            Disease-Pattern-Miner
                        </h1>
                        <h2 class="subtitle">
                            ${requestScope.headertitle}
                        </h2>
                    </div>
                </div>
            </div>
        </div>
    </section>
</header>