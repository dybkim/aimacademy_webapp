<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<body>
<%@include file="../template/navbar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Edit Course: ${course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse" method="post" modelAttribute="course">
                <div class="form-group"><form:errors path="courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title</label>
                    <form:input path="courseName" id="courseTitle" class="form-Control" value="${course.courseName}"/>
                </div>

                <div class="form-group"><form:errors path="courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type</label>
                    <form:input path="courseType" id="courseType" class="form-Control" value="${course.courseType}"/>
                </div>

                <div class="form-group"><span style="color: #FF0000">${startDateErrorMsg}</span>
                    <label for="startDate">Start Date (MM/DD/YYYY)</label>
                    <form:input path="courseStartDate" id="startDate" class="date" value="${course.courseStartDate}"/>
                </div>

                <div class="form-group"><span style="color: #FF0000">${endDateErrorMsg}</span>
                    <label for="endDate">End Date (MM/DD/YYYY)</label>
                    <form:input path="courseEndDate" id="endDate" class="date" value="${course.courseEndDate}"/>
                </div>

                <form:hidden path="isActive" value="${course.isActive}"/>

                <form:hidden path="numEnrolled" value="${course.numEnrolled}"/>

                <br><br>

                <input type="submit" value="submit" class="btn btn=default">

                <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">

            </form:form>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="<spring:url value="/resources/js/jquery-3.1.1.min.js"/>"<\/script>')</script>
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


