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
<!DOCTYPE html>
<html lang="en">

<body>
<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Add Course Session</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/viewEnrollment/${courseSessionAttendanceListWrapper.courseSession.courseID}/addCourseSession" method="post" modelAttribute="courseSessionAttendanceListWrapper">

                <form:hidden path="courseSession.courseSessionID" value="${courseSessionAttendanceListWrapper.courseSession.courseSessionID}"/>

                <form:hidden path="courseSession.courseID" value="${courseSessionAttendanceListWrapper.courseSession.courseID}"/>

                <div class="form-group">
                    <span style="color: #FF0000">${courseSessionDateErrorMsg}</span>
                    <br>
                    <label for="courseSessionDate">Class Session Date (MM/DD/YYYY)</label>
                    <form:input path="courseSession.courseSessionDate" id="courseSessionDate" class="date"/>
                </div>

                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Attendance</tH>
                        </tr>
                    </thead>
                    <tbody>
                    <div class="form-group">
                        <c:forEach items="${courseSessionAttendanceListWrapper.memberAttendanceMap}" var="memberAttendanceMapEntry">
                            <tr>
                                <form:hidden path="memberAttendanceMap[${memberAttendanceMapEntry.key}].memberID" value="${memberAttendanceMapEntry.key.memberID}"/>
                                <form:hidden path="memberAttendanceMap[${memberAttendanceMapEntry.key}].courseSessionID" value="${courseSessionAttendanceListWrapper.courseSession.courseSessionID}"/>
                                <td>${memberAttendanceMapEntry.key.memberID}</td>
                                <td>${memberAttendanceMapEntry.key.memberFirstName} ${memberAttendanceMapEntry.key.memberLastName}</td>
                                <td><form:checkbox path="memberAttendanceMap[${memberAttendanceMapEntry.key}].wasPresent"/></td>
                            </tr>
                        </c:forEach>
                    </div>
                    </tbody>
                </table>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/viewEnrollment/${courseSessionAttendanceListWrapper.courseSession.courseID}/cancelAddCourseSession/${courseSessionAttendanceListWrapper.courseSession.courseSessionID}"/>" class="btn btn-default">Cancel</a>

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


