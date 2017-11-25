<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LogMonitor Search</title>
</head>
<body>
<div id="main_div">
    <a href="${pageContext.request.contextPath}/" style="float:right">
        <button class="btn btn-default">Go home</button>
    </a>
    <span style="clear:both"></span>
    <div align="center">
        <h1>LogMonitor</h1>
    </div>
        <form action="${pageContext.request.contextPath}/search" method="GET" class="form-inline">
            <input type="text" name="userid" placeholder="Search for user id" class="form-control">
            <button type="submit" value="Search" class="btn btn-default"><span
                    class="glyphicon glyphicon-search"></span> Search
            </button>
        </form>

    <c:if test="${not empty records}">
        <h3>Search results:</h3>
        <table id="main_table" class="table table-hover">
            <thead>
            <tr>
                <th class="tr_orderable" onclick="sortTable(0)">Timestamp <span class="glyphicon glyphicon-sort"></span>
                </th>
                <th class="tr_orderable" onclick="sortTable(1)">UserID <span class="glyphicon glyphicon-sort"></span>
                </th>
                <th>Site</th>
                <th>Time</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="record" items="${records}">
                <tr>
                    <td>${record.timeStamp}</td>
                    <td>${record.userName}</td>
                    <td>${record.site}</td>
                    <td>${record.duration}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty records}">
        <p class="bg-warning p_info">Not found any results...</p>
    </c:if>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/static/js/app.js"></script>
</body>
</html>
