<%--
  Created by IntelliJ IDEA.
  User: vitaliy
  Date: 15.01.2019
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<article class="tile is-child notification is-dark">
    <h1 class="title">Manage datasets</h1>

    <div class="field is-horizontal">
        <div class="field-label is-normal">
            <label class="label has-text-light has-text-left">Upload own Data</label>
        </div>
        <div class="field-body">
            <p class="control">
            <div class="field">
                <div class="file is-link has-name">
                    <label class="file-label">
                        <form method="post" action="upload" enctype="multipart/form-data">
                            <input class="file-input" type="file" id="fileUpload" name="fileUpload" multiple="false"
                                   onchange="this.form.submit()">
                        </form>
                        <span class="file-cta">
                            <span class="file-icon">
                              <i class="fas fa-upload"></i>
                            </span>
                            <span class="file-label">
                                Select a File for upload…
                            </span>
                        </span>
                        <span class="file-name is-normal" style="background-color: #fff; color: #363636;">
                            MainDataFile.csv
                        </span>
                    </label>
                </div>
            </div>

            </p>
        </div>
    </div>

    <div class="field has-addons is-horizontal">
        <div class="field-label is-normal">
            <label class="label has-text-light has-text-left">Or download from external source</label>
        </div>
        <div class="field-body">
            <p class="control">
            <div class="control has-icons-left has-icons-right" style="min-width: 450px;">
                <input class="input is-link" type="text" placeholder="URL to datasource"
                       value="http://tmu.serveo.net/datasource.zip" id="externalURL">
                <span class="icon is-small is-left">
                        <i class="fas fa-external-link-square-alt"></i>
                </span>
            </div>
            </p>
            <p class="control">
                <a class="button is-link" id="downloadExternal">
                    Download
                </a>
            </p>
        </div>
    </div>

</article>
