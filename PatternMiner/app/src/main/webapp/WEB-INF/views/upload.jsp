<%--
  Created by IntelliJ IDEA.
  User: vital
  Date: 23.10.2018
  Time: 13:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<p class="title">Select CSV fileOfResult(s)</p>

<form action ="FileUploadServlet"  method="post" enctype="multipart/form-data">
        <div class="field">
            <div class="fileOfResult is-centered is-boxed is-success has-name">
                <label class="fileOfResult-label">
                    <input class="fileOfResult-input" type="fileOfResult" accept="*.text/csv" name="resume"
                           onchange="form.submit()" multiple>
                    <span class="fileOfResult-cta">
                                        <span class="fileOfResult-icon">
                                          <i class="fas fa-upload"></i>
                                        </span>
                                        <span class="fileOfResult-label">
                                          Upload fileOfResult
                                        </span>
                                </span>
                        </label>
                </div>
        </div>
</form>

<h2>${requestScope.message}</h2>
