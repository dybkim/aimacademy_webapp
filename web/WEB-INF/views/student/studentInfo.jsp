<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<script>
    $(document).ready(function(){

        $('a[data-toggle="tabpanel"]').on( 'shown.bs.tab', function (e) {
            $.fn.dataTable.tables(true).columns.adjust();
        } );

        var coursesTable = $('#coursesTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]]
        });

        var inactiveCoursesTable = $('#inactiveCoursesTable').DataTable({
            "lengthMenu":[[50,-1], [50,'All']]
        });

        var financesTable = $('#financesTable').DataTable({
            "lengthMenu":[[50,-1], [50,'All']],
            "order": [[0, "desc"]],
            "columnDefs": [
                {
                    "targets": [0],
                    "visible": false,
                    "searchable": false
                }
            ]
        });

        $('.nav-tabs a[href="#tab-finances"]').tab('show');

    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Member Profile</h1>
            <h2>${member.memberFirstName} ${member.memberLastName}</h2>

            <a href="<spring:url value="/admin/student/studentList/editStudent/${member.memberID}"/>" class="btn btn-primary">Edit Profile</a>

            <br>
            <br>

            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation"><a href="#tab-courses" aria-controls="tab-courses" role="tab" data-toggle="tab">Active Courses</a></li>
                <li role="presentation"><a href="#tab-inactiveCourses" aria-controls="tab-inactiveCourses" role="tab" data-toggle="tab">Previous Courses</a></li>
                <li role="presentation"><a href="#tab-finances" aria-controls="tab-finances" role="tab" data-toggle="tab">Finances</a></li>
            </ul>

            <div class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active" id="tab-courses">
                    <table id="coursesTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Course Enrolled</th>
                            <th>Course Type</th>
                            <th>Attendance</th>
                        </tr>
                        </thead>

                        <tbody>
                            <c:forEach items="${activeCourseList}" var="course">
                            <tr>
                            <td>${course.courseID}</td>
                            <td><a href="<spring:url value="/admin/courseList/courseInfo/${course.courseID}"/>">${course.courseName}</a></td>
                            <td>${course.courseType}</td>
                            <td>${courseAttendanceCountHashMap.get(course.courseID)}/${courseSessionListHashMap.get(course.courseID).size()}</td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane fade in active" id="tab-inactiveCourses">
                    <table id="inactiveCoursesTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Course Enrolled</th>
                            <th>Course Type</th>
                            <th>Attendance</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${inactiveCourseList}" var="course">
                            <tr>
                                <td>${course.courseID}</td>
                                <td><a href="<spring:url value="/admin/courseList/courseInfo/${course.courseID}"/>">${course.courseName}</a></td>
                                <td>${course.courseType}</td>
                                <td>${courseAttendanceCountHashMap.get(course.courseID)}/${courseSessionListHashMap.get(course.courseID).size()}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane fade in active" id="tab-finances">
                    <table id="financesTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th ></th>
                            <th>Month/Year</th>
                            <th>Charges Fulfilled / ChargesTotal</th>
                            <th>Amount Paid</th>
                            <th>Total Charges</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${memberCourseFinancesWrapperList}" var="memberCourseFinancesWrapper" varStatus="i" begin="0">
                            <tr>
                                <td><fmt:parseDate value="${memberCourseFinancesWrapper.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                    <fmt:formatDate value="${parsedDate}" var="formattedHiddenDate" type="date" pattern="yyyy/MM/dd" timeZone="GMT" />${formattedHiddenDate}</td>

                                <c:choose>
                                    <c:when test="${memberCourseFinancesWrapper.chargeList.size() != 0}">
                                        <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${memberCourseFinancesWrapper.date.monthValue}&year=${memberCourseFinancesWrapper.date.year}"/>">${memberCourseFinancesWrapper.date.month.toString()} ${memberCourseFinancesWrapper.date.year}</a></td>
                                        <td>${memberCourseFinancesWrapper.chargePaymentHashMap.size()}/${memberCourseFinancesWrapper.chargeList.size()}</td>
                                        <td>${memberCourseFinancesWrapper.totalPaymentAmount}</td>
                                        <td>${memberCourseFinancesWrapper.totalChargeAmount}</td>
                                    </c:when>

                                    <c:otherwise>
                                        <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${memberCourseFinancesWrapper.date.monthValue}&year=${memberCourseFinancesWrapper.date.year}"/>">${memberCourseFinancesWrapper.date.month.toString()} ${memberCourseFinancesWrapper.date.year}</a></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>
