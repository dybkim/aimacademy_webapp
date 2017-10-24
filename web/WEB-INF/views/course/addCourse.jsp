<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:10 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="../template/navbar.jsp"%>
<%@ include file="../template/sidebar.jsp"%>

<link href="<c:url value="/WEB-INF/resources/css/suggestion.css" />" rel="stylesheet">

<script>
    $(document).ready(function() {

        $('#w-input-search').autocomplete({
            serviceUrl: '${pageContext.request.contextPath}/admin/courseList/addCourse/getMembers',
            paramName: "memberTagName",
            delimiter: ",",
            transformResult: function(response) {

                return {
                    //must convert json to javascript object before process
                    suggestions: $.map($.parseJSON(response), function(item) {

                        return { value: item.tagName, data: item.id };
                    })
                };
            }
        });
    });
</script>

<!DOCTYPE html>
<html lang="en">

<body>
<%@include file="../template/navbar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Add Course</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/addCourse" method="post" modelAttribute="courseRegistrationWrapper">
                <div class="form-group"><form:errors path="course.courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title</label>
                    <form:input path="course.courseName" id="courseTitle" class="form-Control" cssStyle="width: 400px"/>
                </div>

                <div class="form-group"><form:errors path="course.courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type: </label>
                    <form:select path="course.courseType" id="courseType" class="form-Control" cssStyle="width: 200px">
                        <form:option value="Supplement"/>
                        <form:option value="Finals Prep"/>
                        <form:option value="Test Prep"/>
                        <form:option value="Office Hour"/>
                        <form:option value="Other"/>
                    </form:select>
                </div>

                <div class="form-group"><span style="color: #FF0000">${startDateErrorMessage}</span>
                    <label for="startDate">Start Date (MM/DD/YYYY): </label>
                    <form:input path="course.courseStartDate" id="startDate" class="date"/>
                </div>

                <div class="form-group"><span style="color: #FF0000">${endDateErrorMessage}</span>
                    <label for="endDate">End Date (MM/DD/YYYY): </label>
                    <form:input path="course.courseEndDate" id="endDate" class="date"/>
                </div>

                <div class="form-group">
                    <label for="billingType">Billing Type: </label>
                    <form:select path="course.billableUnitType" id="billingType" class="form-Control">
                        <form:option value="hour">Per Hour</form:option>
                        <form:option value="session">Per Session</form:option>
                    </form:select>
                </div>

                <div class="form-group">
                    <label for="coursePrice">Price per hour/session: </label>
                    <form:input path="course.pricePerBillableUnit" id="coursePrice" class="form-control" cssStyle="width: 100px"/>
                </div>

                <div class="form-group">
                    <label for="classDuration">Session length (hours): </label>
                    <form:input path="course.classDuration" id="classDuration" class="form-control" cssStyle="width: 100px"/>
                </div>

                <form:hidden path="course.isActive" value="true"/>

                <form:hidden path="course.numEnrolled" value="0"/>

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


