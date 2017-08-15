<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 7:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="David Kim">
    <link rel="icon" href="../../favicon.ico">

    <title>AIM Academy Portal</title>

    <!-- Bootstrap core CSS -->
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="<spring:url value="/resources/css/ie10-viewport-bug-workaround.css"/>" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">
    <![endif]-->
    <html>

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
            <a class="navbar-brand" href="">AIM Academy</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
        </div>
    </div>
</nav>

<div class="container-wrapper">
    <div class="container">
        <div id="login-box">
            <h2>Login</h2>

            <c:if test="${not empty logout}">
                <div class="logout">${logout}</div>
            </c:if>

            <form name="loginForm" action="<spring:url value="/login"/>" method="post">
                <c:if test="${not empty error}">
                    <div class="error" style="color:#ff0000">${error}</div>
                </c:if>

                <div class="form-group">
                    <label for="username">Username: </label>
                    <input id="username" name="username" class="form-control"/>
                </div>

                <div class="form-group">
                    <label for="password">Password: </label>
                    <input type="password" id="password" name="password" class="form-control"/>
                </div>

                <input type="submit" value="Submit" class="btn btn-default"/>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
    </div>
</div>
</body>
</html>
