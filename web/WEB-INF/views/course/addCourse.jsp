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

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../template/navbar.jsp"%>
<%@ include file="../template/sidebar.jsp"%>

<link href="<c:url value="/WEB-INF/resources/css/suggestion.css" />" rel="stylesheet">

<script>
    $(document).ready(function(){
        $('#courseStartDate, #courseEndDate').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#courseStartDate, #courseEndDate').keydown(function (e) {
            // Allow: backspace, delete, tab
            if ($.inArray(e.keyCode, [46, 8, 9]) !== -1 || (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
                // Allow: home, end, left, right, down, up
                (e.keyCode >= 35 && e.keyCode <= 40) || ((e.keyCode >= 48 && e.keyCode <= 57) || e.keyCode === 191)) {
                // let it happen, don't do anything
                return;
            }
            // Ensure that it is a number and stop the keypress
            if ((e.shiftKey || (e.keyCode > 1 || e.keyCode < 200))) {
                e.preventDefault();
            }
        });
    });
</script>

<!DOCTYPE html>
<html lang="en">
<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Add Course</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/addCourse" method="POST" modelAttribute="course">
                <div class="form-group"><form:errors path="courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title</label>
                    <form:input path="courseName" id="courseTitle" class="form-Control" cssStyle="width: 400px"/>
                </div>

                <div class="form-group"><form:errors path="courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type: </label>
                    <form:select path="courseType" id="courseType" class="form-control" cssStyle="width: 200px">
                        <form:option value="Supplement"/>
                        <form:option value="Finals Prep"/>
                        <form:option value="Test Prep"/>
                        <form:option value="Office Hour"/>
                        <form:option value="Other"/>
                    </form:select>
                </div>

                <c:choose>
                    <c:when test="${startDateErrorMessage != null}">
                        <span style="color: #FF0000">${startDateErrorMessage}</span>
                    </c:when>

                    <c:otherwise>
                        <br>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="courseStartDate">Start Date (MM/DD/YYYY): </label>
                    <form:input path="courseStartDate" id="courseStartDate" class="date"/>
                </div>

                <c:choose>
                    <c:when test="${endDateErrorMessage != null}">
                        <span style="color: #FF0000">${endDateErrorMessage}</span>
                    </c:when>

                    <c:otherwise>
                        <br>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="courseEndDate">End Date (MM/DD/YYYY): </label>
                    <form:input path="courseEndDate" id="courseEndDate" class="date"/>
                </div>

                <div class="form-group">
                    <label for="billingType">Billing Type: </label>
                    <form:select path="billableUnitType" id="billingType" class="form-Control">
                        <form:option value="hour">Per Hour</form:option>
                        <form:option value="session">Per Session</form:option>
                    </form:select>
                </div>

                <div class="form-group">
                    <label for="memberCoursePrice">Price per hour/session (for Members): </label>
                    <form:input path="memberPricePerBillableUnit" id="memberCoursePrice" class="form-control" cssStyle="width: 100px"/>
                </div>

                <div class="form-group">
                    <label for="nonMemberCoursePrice">Price per hour/session (for Non-Members): </label>
                    <form:input path="nonMemberPricePerBillableUnit" id="nonMemberCoursePrice" class="form-control" cssStyle="width: 100px"/>
                </div>

                <c:choose>
                    <c:when test="${billableUnitTypeError != null}">
                        <span style="color: #FF0000">${billableUnitTypeError}</span>
                    </c:when>

                    <c:otherwise>
                        <br>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="classDuration">Class Duration (hours): </label>
                    <form:input path="classDuration" id="classDuration" class="form-control" cssStyle="width: 100px"/>
                </div>

                <form:hidden path="isActive" value="true"/>

                <form:hidden path="numEnrolled" value="0"/>

                <br><br>

                <input type="submit" value="submit" class="btn btn=default">

                <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">

            </form:form>
        </div>
    </div>
</div>
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


