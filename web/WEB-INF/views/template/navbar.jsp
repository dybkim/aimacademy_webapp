<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 7:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/WEB-INF/resources/images/favicon.ico"/>
<title>AIM Academy</title>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="David Kim">

    <title>AIM Academy Portal</title>

    <!-- Angular JS -->
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular.min.js"></script>

    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-animate.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/2.5.0/ui-bootstrap-tpls.js"></script>

    <!-- JQuery 3-->
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

    <!-- JQuery UI-->
    <script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.min.js"/>"></script>

    <!-- Bootstrap JS -->
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <!-- Data Table -->
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>

    <!--Data Table Bootstrap -->
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap.min.js"></script>

    <!-- Bootstrap core CSS -->
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">

    <!-- Table row column alignment -->
    <link href="<spring:url value="/resources/css/tablerow-alignment.css"/>" rel="stylesheet">

    <!-- CSS DataTables Link -->
    <link href="https://cdn.datatables.net/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet">

    <!-- CSS DataTables Bootstrap -->
    <link href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css" rel="stylesheet">

    <!-- JQueryUI CSS -->
    <link href="<spring:url value="/resources/css/jquery-ui.min.css"/>" rel="stylesheet">

    <![endif]-->
</head>
<body>


<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<spring:url value="/admin/home"/> ">AIM Academy</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <li><a>${pageContext.request.userPrincipal.name}</a></li>
                </c:if>
                <li><a href="<spring:url value="/admin/home"/>">Home</a></li>
                <%--<li><a href="#">Settings</a></li>--%>
                <%--<li><a href="#">Profile</a></li>--%>
                <%--<li><a href="#">Help</a></li>--%>
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <li><a href="<c:url value="/logout"/>">Logout</a></li>
                </c:if>
            </ul>
            <!--
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
            -->
        </div>
    </div>
</nav>
</body>
</html>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>