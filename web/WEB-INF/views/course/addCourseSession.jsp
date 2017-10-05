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
<%@include file="../template/sidebar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Add Course Session</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/courseInfo/${courseSessionAttendanceListWrapper.courseSession.courseID}/addCourseSession" method="POST" modelAttribute="courseSessionAttendanceListWrapper">

                <%--<form:hidden path="coursesession.courseSessionID" value="${courseSessionAttendanceListWrapper.coursesession.courseSessionID}"/>--%>

                <form:hidden path="courseSession.courseID" value="${courseSessionAttendanceListWrapper.courseSession.courseID}"/>

                <div class="form-group">
                    <span style="color: #FF0000">${courseSessionDateErrorMessage}</span>
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
                        <c:forEach items="${memberList}" var="member" varStatus="i" begin="0">
                            <tr>
                                <form:hidden path="attendanceMap[${member.memberID}].attendanceID" value="${courseSessionAttendanceListWrapper.attendanceMap.get(member.memberID).attendanceID}"/>
                                <form:hidden path="attendanceMap[${member.memberID}].memberID" value="${member.memberID}"/>
                                <form:hidden path="attendanceMap[${member.memberID}].courseSessionID" value="${courseSessionAttendanceListWrapper.courseSession.courseSessionID}"/>
                                <td>${member.memberID}</td>
                                <td>${member.memberFirstName} ${member.memberLastName}</td>
                                <td><form:checkbox path="attendanceMap[${member.memberID}].wasPresent"/></td>
                            </tr>
                        </c:forEach>
                    </div>
                    </tbody>
                </table>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/courseInfo/${courseSessionAttendanceListWrapper.courseSession.courseID}/cancelAddCourseSession/${courseSessionAttendanceListWrapper.courseSession.courseSessionID}"/>" class="btn btn-default">Cancel</a>

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


