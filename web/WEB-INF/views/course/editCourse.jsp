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
                        url: "/admin/courseList/rest/deleteCourse/${courseRegistrationDTO.course.courseID}",
                        type: "DELETE",
                        dataType: "json",
                        success: function() {
                            window.location.replace("/admin/courseList");
                        }
                    });
                }
        });

        $('#startDate, #endDate').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#startDate, endDate').keydown(function (e) {
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
            <h1 class="page-header">Edit Course: ${courseRegistrationDTO.course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse/${courseRegistrationDTO.course.courseID}" method="POST" modelAttribute="courseRegistrationDTO">
                <form:hidden path="course.courseID" value="${courseRegistrationDTO.course.courseID}"/>
                <form:hidden path="course.totalNumSessions" value="${courseRegistrationDTO.course.totalNumSessions}"/>
                <form:hidden path="course.billableUnitDuration" value="${courseRegistrationDTO.course.billableUnitDuration}"/>

                <div class="form-group"><form:errors path="course.courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title: </label>
                    <form:input path="course.courseName" id="courseTitle" class="form-control" value="${courseRegistrationDTO.course.courseName}" cssStyle="width: 400px"/>
                </div>

                <div class="form-group"><form:errors path="course.courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type: </label>
                    <form:select path="course.courseType" id="courseType" class="form-control" value="${courseRegistrationDTO.course.courseType}" cssStyle="width: 200px">
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
                    <fmt:parseDate value="${courseRegistrationDTO.course.courseStartDate}" pattern="yyyy-MM-dd" var="parsedStartDate" type="date" />
                    <fmt:formatDate value="${parsedStartDate}" var="formattedStartDate" type="date" pattern="MM/dd/yyyy"/>
                    <form:input path="course.courseStartDate" id="courseStartDate" class="date" value="${formattedStartDate}"/>
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
                    <fmt:parseDate value="${courseRegistrationDTO.course.courseEndDate}" pattern="yyyy-MM-dd" var="parsedEndDate" type="date" />
                    <fmt:formatDate value="${parsedEndDate}" var="formattedEndDate" type="date" pattern="MM/dd/yyyy"/>
                    <form:input path="course.courseEndDate" id="courseEndDate" class="date" value="${formattedEndDate}"/>
                </div>

                <div class="form-group">
                    <label for="billingType">Billing Type: </label>
                    <form:select path="course.billableUnitType" id="billingType" class="form-Control" value="${courseRegistrationDTO.course.billableUnitType}">
                        <form:option value="hour">Per Hour</form:option>
                        <form:option value="session">Per Session</form:option>
                    </form:select>
                </div>

                <div class="form-group">
                    <label for="memberCoursePrice">Price per hour/session (for Members): </label>
                    <form:input path="course.memberPricePerBillableUnit" id="memberCoursePrice" class="form-control" cssStyle="width: 100px" value="${courseRegistrationDTO.course.memberPricePerBillableUnit}"/>
                </div>

                <div class="form-group">
                    <label for="nonMemberCoursePrice">Price per hour/session (for Non-Members): </label>
                    <form:input path="course.nonMemberPricePerBillableUnit" id="nonMemberCoursePrice" class="form-control" placeholder="Default: Member Rate If Empty" cssStyle="width: 100px" value="${courseRegistrationDTO.course.nonMemberPricePerBillableUnit}"/>
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
                    <form:input path="course.classDuration" id="classDuration" class="form-control" cssStyle="width: 100px" value="${courseRegistrationDTO.course.classDuration}"/>
                </div>

                <div class="form-group">
                    <label for="isActiveCheckBox">Is Active:</label>
                    <form:checkbox path="course.isActive" value="${courseRegistrationDTO.course.isActive}" id="isActiveCheckBox"/>
                </div>

                <a href="<spring:url value="/admin/courseList/editCourse/${courseRegistrationDTO.course.courseID}/addStudentToCourse"/>" class="btn btn-primary">Add Member</a>

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
                        <c:forEach items="${courseRegistrationDTO.courseRegistrationDTOListItems}" var="courseRegistrationDTOListItem" varStatus="i" begin="0">
                            <tr>
                                <td><form:hidden path="courseRegistrationDTOListItems[${i.index}].member.memberID" value="${courseRegistrationDTOListItem.member.memberID}" id="$P"/>${courseRegistrationDTOListItem.member.memberID}</td>
                                <td><form:hidden path="courseRegistrationDTOListItems[${i.index}].member.memberFirstName" value="${courseRegistrationDTOListItem.member.memberFirstName}"/> <form:hidden path="courseRegistrationDTOListItems[${i.index}].member.memberLastName" value="${courseRegistrationDTOListItem.member.memberLastName}"/>${courseRegistrationDTOListItem.member.memberFirstName} ${courseRegistrationDTOListItem.member.memberLastName}</td>
                                <td><form:checkbox path="courseRegistrationDTOListItems[${i.index}].isDropped"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <form:hidden path="course.numEnrolled" value="${courseRegistrationDTO.course.numEnrolled}"/>

                <br><br>

                <input type="submit" value="submit" class="btn btn=default">

                <a href="<spring:url value="/admin/courseList/courseInfo/${courseRegistrationDTO.course.courseID}"/>" class="btn btn-default">Back To Course Page</a>

                <a href="" id="deleteCourseButton" class="btn btn-danger">Delete Course</a>

            </form:form>
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


