<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 8/18/17
  Time: 10:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<!DOCTYPE html>
<html lang="en">
<html>
<head>
<body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <h1 class="page-header">Course Finances</h1>
                    <h2>${course.courseDescription}</h2>


                </div>
            </div>
        </div>
    </head>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>