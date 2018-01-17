<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../../template/navbar.jsp"%>
<%@include file="../../template/sidebar.jsp"%>

<!DOCTYPE html>
<html lang="en">
<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Edit Course: ${courseRegistrationDTO.course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse/${courseRegistrationDTO.course.courseID}" method="POST" modelAttribute="courseRegistrationDTO">
                <form:hidden path="course.courseID" value="${courseRegistrationDTO.course.courseID}"/>
                <form:hidden path="course.totalNumSessions" value="${courseRegistrationDTO.course.totalNumSessions}"/>
                <form:hidden path="course.billableUnitDuration" value="${courseRegistrationDTO.course.billableUnitDuration}"/>
                <form:hidden path="course.courseName" value="${courseRegistrationDTO.course.courseName}"/>
                <form:hidden path="course.courseType" value="${courseRegistrationDTO.course.courseType}"/>

                <fmt:parseDate value="${courseRegistrationDTO.course.courseStartDate}" pattern="yyyy-MM-dd" var="parsedStartDate" type="date" />
                <fmt:formatDate value="${parsedStartDate}" var="formattedStartDate" type="date" pattern="MM/dd/yyyy"/>
                <form:hidden path="course.courseStartDate" value="${formattedStartDate}"/>

                <fmt:parseDate value="${courseRegistrationDTO.course.courseEndDate}" pattern="yyyy-MM-dd" var="parsedEndDate" type="date" />
                <fmt:formatDate value="${parsedEndDate}" var="formattedEndDate" type="date" pattern="MM/dd/yyyy"/>
                <form:hidden path="course.courseEndDate" value="${formattedEndDate}"/>

                <form:hidden path="course.billableUnitType" value="${courseRegistrationDTO.course.billableUnitType}"/>
                <form:hidden path="course.classDuration" value="${courseRegistrationDTO.course.classDuration}"/>
                <form:hidden path="course.nonMemberPricePerBillableUnit" value="${courseRegistrationDTO.course.nonMemberPricePerBillableUnit}"/>
                <form:hidden path="course.isActive" value="${courseRegistrationDTO.course.isActive}"/>
                <form:hidden path="course.numEnrolled" value="${courseRegistrationDTO.course.numEnrolled}"/>

                <div class="form-group">
                    <label for="memberCoursePrice">Membership price per month: </label>
                    <form:input path="course.memberPricePerBillableUnit" id="memberCoursePrice" class="form-control" cssStyle="width: 100px" value="${courseRegistrationDTO.course.memberPricePerBillableUnit}"/>
                </div>

                <br>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/courseInfo/${courseRegistrationDTO.course.courseID}"/>" class="btn btn-default">Back To Course Page</a>

            </form:form>
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


