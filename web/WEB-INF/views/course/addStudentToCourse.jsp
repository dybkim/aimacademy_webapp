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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<body>
<%@include file="../template/navbar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">${course.courseName}: Add student</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse/${course.courseID}/addStudentToCourse" method="post" modelAttribute="memberCourseRegistration">

                <form:hidden path="courseID" value="${memberCourseRegistration.courseID}"/>
                <form:hidden path="isEnrolled" value="true"/>

                <div class="form-group">
                    <label for="addMember">Member</label>
                    <form:select path="memberID" id="addMember" items="${unenrolledMembersMap}" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="referralMember">Referred By:</label>
                    <form:select path="referMemberID" id="referralMember" items="${allMembersMap}" class="form-Control"/>
                </div>

                <div class="form-group"><span style="color: #FF0000">${dateEnrolledErrorMessage}</span>
                    <label for="dateEnrolled">Date Enrolled (MM/DD/YYYY)</label>
                    <form:input path="dateRegistered" id="dateEnrolled" class="form-Control"/>
                </div>

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


