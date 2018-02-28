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
<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp"%>

<script>
    $(document).ready(function(){
        $('#courseSessionDate').datepicker({
            dateFormat: 'mm/dd/yy',
            defaultDate: '${monthOffset}',
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#courseSessionDate').keydown(function (e) {
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
            <h1 class="page-header">Add Course Session: ${courseSessionDTO.course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/courseInfo/${courseSessionDTO.course.courseID}/addCourseSession" method="POST" modelAttribute="courseSessionDTO">
            <form:hidden path="course.courseID" value="${courseSessionDTO.course.courseID}"/>
                <div class="form-group">
                    <span style="color: #FF0000">${courseSessionDateErrorMessage}</span>
                    <br>
                    <label for="courseSessionDate">Class Session Date (MM/DD/YYYY):</label>
                    <form:input path="courseSessionDate" id="courseSessionDate" class="date"/>
                </div>

                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Attendance</th>
                        </tr>
                    </thead>
                    <tbody>
                    <div class="form-group">
                        <c:forEach items="${courseSessionDTO.attendanceList}" var="attendance" varStatus="i" begin="0">
                            <tr>
                                <form:hidden path="attendanceList[${i.index}].member.memberID" value="${attendance.member.memberID}"/>
                                <td>${attendance.member.memberID}</td>
                                <td>${attendance.member.memberFirstName} ${attendance.member.memberLastName}</td>
                                <td><form:checkbox path="attendanceList[${i.index}].wasPresent"/></td>
                            </tr>
                        </c:forEach>
                    </div>
                    </tbody>
                </table>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/courseInfo/${courseSessionDTO.course.courseID}"/>" class="btn btn-default">Back To Course Page</a>

            </form:form>
        </div>
    </div>
</div>
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


