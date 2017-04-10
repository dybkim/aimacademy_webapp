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

        var studentListTable = $('#studentListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
        });

        var courseSessionTable = $('#courseSessionListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[4, "asc"]],
            "columnDefs": [
                {
                    "targets": [4],
                    "visible": false,
                    "searchable": false
                }
            ]
        });

        courseSessionTable.on('order.dt search.dt', function(){
            courseSessionTable.column(0, {search:'applied', order:'applied'}).nodes().each(function (cell, i){
                cell.innerHTML = i + 1;
            });
        }).draw();

        $('.nav-tabs a[href="#tab-students"]').tab('show');
    });
</script>

<html>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                    <h1 class="page-header">${course.courseName} - Course Info</h1>

                    <br>

                    <a href="<spring:url value="/admin/courseList/viewEnrollment/${course.courseID}/addStudentToCourse"/>" class="btn btn-primary">Add Student</a>

                    <br>

                    <br>

                    <a href="<spring:url value="/admin/courseList/viewEnrollment/${course.courseID}/addCourseSession"/>" class="btn btn-primary">Add Course Session</a>

                    <br>

                    <br>

                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation"><a href="#tab-students" aria-controls="tab-students" role="tab" data-toggle="tab">Roster</a></li>
                        <li role="presentation"><a href="#tab-sessions" aria-controls="tab-sessions" role="tab" data-toggle="tab">Sessions</a></li>
                    </ul>

                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane fade in active" id="tab-students">
                            <table id="studentListTable" class="table table-striped dt-responsive">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>Student ID#</th>
                                    <th>Student Name</th>
                                    <th>Attendance</th>
                                    <th>Finances</th>
                                    <th>Drop student</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${studentList}" var="student">
                                    <tr>
                                        <td></td>
                                        <td>${student.memberID}</td>
                                        <td>${student.memberFirstName} ${student.memberLastName}</td>
                                        <td>TBA</td>
                                        <td><a href=""><span class="glyphicon glyphicon-usd"></span></a></td>
                                        <td><a href=""><span class="glyphicon glyphicon-remove-sign"></span></a></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div role="tabpanel" class="tab-pane fade in active" id="tab-sessions">
                            <table id="courseSessionListTable" class="table table-striped dt-responsive">
                                <thead>
                                    <tr>
                                        <th>Session #</th>
                                        <th>Date</th>
                                        <th>Attendance</th>
                                        <th>Edit Info</th>
                                        <th>Hidden Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${courseSessionList}" var="courseSession">
                                        <tr>
                                            <td></td>
                                            <td><fmt:formatDate value="${courseSession.courseSessionDate}" var="dateString" pattern="MM/dd/yyyy" timeZone="GMT"/>${dateString}</td>
                                            <td>${courseSession.numMembersAttended} / ${numEnrolled} Students</td>
                                            <td><a href="<spring:url value="/admin/courseList/viewEnrollment/${course.courseID}/editCourseSession/${courseSession.courseSessionID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                            <td><fmt:formatDate value="${courseSession.courseSessionDate}" var="dateStringHidden" pattern="yyyy/MM/dd" timeZone="GMT"/>${dateStringHidden}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <a href="<spring:url value="/admin/courseList"/>" class="btn btn-primary">Back</a>
                </div>
            </div>
        </div>
    </body>
</html>
