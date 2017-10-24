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

        var memberListTable = $('#memberListContainer').DataTable({
            "lengthMenu": [10,-1],
            "searching": false,
            "paging": false,
            "columns": [
                { "width": "20%" },
                { "width": "30%" },
                { "width": "20%" }
            ]
        });

        $("#deleteCourseButton").click(function() {
            if(confirm("Deleting this course will delete all related attendance and financial records! Are you sure you want to delete?")){
                    $.ajax({
                        url: "/admin/courseList/rest/deleteCourse/${courseRegistrationWrapper.course.courseID}/",
                        type: "DELETE",
                        dataType: "json",
                        success: function() {
                            window.location.replace("/admin/courseList");
                        }
                    });
                }
        });
})

</script>

<html lang="en">

<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Edit Course: ${courseRegistrationWrapper.course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse/${courseRegistrationWrapper.course.courseID}" method="PUT" modelAttribute="courseRegistrationWrapper">
                <form:hidden path="course.courseID" value="${courseRegistrationWrapper.course.courseID}"/>
                <form:hidden path="course.totalNumSessions" value="${courseRegistrationWrapper.course.totalNumSessions}"/>
                <form:hidden path="course.pricePerBillableUnit" value="${courseRegistrationWrapper.course.pricePerBillableUnit}"/>
                <form:hidden path="course.billableUnitDuration" value="${courseRegistrationWrapper.course.billableUnitDuration}"/>

                <div class="form-group"><form:errors path="course.courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title: </label>
                    <form:input path="course.courseName" id="courseTitle" class="form-Control" value="${courseRegistrationWrapper.course.courseName}"/>
                </div>

                <div class="form-group"><form:errors path="course.courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type: </label>
                    <form:select path="course.courseType" id="courseType" class="form-Control" value="${courseRegistrationWrapper.course.courseType}">
                        <form:option value="Supplement"/>
                        <form:option value="Finals Prep"/>
                        <form:option value="Test Prep"/>
                        <form:option value="Office Hour"/>
                        <form:option value="Other"/>
                    </form:select>
                </div>

                <span style="color: #FF0000">${startDateErrorMessage}</span>
                <div class="form-group">
                    <label for="startDate">Start Date (MM/DD/YYYY): </label>
                    <fmt:parseDate value="${courseRegistrationWrapper.course.courseStartDate}" pattern="yyyy-MM-dd" var="parsedStartDate" type="date" />
                    <fmt:formatDate value="${parsedStartDate}" var="formattedStartDate" type="date" pattern="MM/dd/yyyy"/>
                    <form:input path="course.courseStartDate" id="startDate" class="date" value="${formattedStartDate}"/>
                </div>

                <span style="color: #FF0000">${endDateErrorMessage}</span>
                <div class="form-group">
                    <label for="endDate">End Date (MM/DD/YYYY): </label>
                    <fmt:parseDate value="${courseRegistrationWrapper.course.courseEndDate}" pattern="yyyy-MM-dd" var="parsedEndDate" type="date" />
                    <fmt:formatDate value="${parsedEndDate}" var="formattedEndDate" type="date" pattern="MM/dd/yyyy"/>
                    <form:input path="course.courseEndDate" id="endDate" class="date" value="${formattedEndDate}"/>
                </div>

                <div class="form-group">
                    <label for="billingType">Billing Type: </label>
                    <form:select path="course.billableUnitType" id="billingType" class="form-Control" value="${courseRegistrationWrapper.course.billableUnitType}">
                        <form:option value="hour">Per Hour</form:option>
                        <form:option value="session">Per Session</form:option>
                    </form:select>
                </div>

                <div class="form-group">
                    <label for="coursePrice">Price per hour/session: </label>
                    <form:input path="course.pricePerBillableUnit" id="coursePrice" class="form-control" cssStyle="width: 100px" value="${courseRegistrationWrapper.course.pricePerBillableUnit}"/>
                </div>

                <div class="form-group">
                    <label for="billableUnitDuration">Session length (hours): </label>
                    <form:input path="course.billableUnitDuration" id="billableUnitDuration" class="form-control" cssStyle="width: 100px" value="${courseRegistrationWrapper.course.billableUnitDuration}"/>
                </div>

                <div class="form-group">
                    <label for="isActiveCheckBox">Is Active:</label>
                    <form:checkbox path="course.isActive" value="${courseRegistrationWrapper.course.isActive}" id="isActiveCheckBox"/>
                </div>

                <a href="<spring:url value="/admin/courseList/editCourse/${courseRegistrationWrapper.course.courseID}/addStudentToCourse"/>" class="btn btn-primary">Add Member</a>

                <br>
                <br>

                <h><b>Drop Members</b></h>
                <div class="table-responsive">
                    <table class="table table-striped dt-responsive" id="memberListContainer">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Drop Enrollment</th>
                        </tr>
                        </thead>

                        <tbody id="memberList">
                        <c:forEach items="${courseRegistrationWrapper.courseRegistrationWrapperObjectList}" var="courseRegistrationWrapperObject" varStatus="i" begin="0">
                            <tr>
                                <td><form:hidden path="courseRegistrationWrapperObjectList[${i.index}].member.memberID" value="${courseRegistrationWrapperObject.member.memberID}" id="$P"/>${courseRegistrationWrapperObject.member.memberID}</td>
                                <td><form:hidden path="courseRegistrationWrapperObjectList[${i.index}].member.memberFirstName" value="${courseRegistrationWrapperObject.member.memberFirstName}"/> <form:hidden path="courseRegistrationWrapperObjectList[${i.index}].member.memberLastName" value="${courseRegistrationWrapperObject.member.memberLastName}"/>${courseRegistrationWrapperObject.member.memberFirstName} ${courseRegistrationWrapperObject.member.memberLastName}</td>
                                <td><form:checkbox path="courseRegistrationWrapperObjectList[${i.index}].isDropped"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <form:hidden path="course.numEnrolled" value="${courseRegistrationWrapper.course.numEnrolled}"/>

                <br><br>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/courseInfo/${courseRegistrationWrapper.course.courseID}"/>" class="btn btn-default">Back To Course Page</a>

                <a href="" id="deleteCourseButton" class="btn btn-danger">Delete Course</a>

            </form:form>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


