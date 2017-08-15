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

        var memberListTable = $('#memberListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]]
        });
        
        var inactiveMemberListTable = $('#inactiveMemberListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]]
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

        <!--Places an index column for the courseSessionTable-->
        courseSessionTable.on('order.dt search.dt', function(){
            courseSessionTable.column(0, {search:'applied', order:'applied'}).nodes().each(function (cell, i){
                cell.innerHTML = i + 1;
            });
        }).draw();

        $('.nav-tabs a[href="#tab-members"]').tab('show');
    });


    var checkCourseMembers = function(){
        $.ajax({
            url: "/admin/courseList/rest/${course.courseID}/validateAddCourseSession",
            type: "GET",
            async: false,
            dataType: "json",
            success:function(){
                window.location.replace('/admin/courseList/courseInfo/${course.courseID}/addCourseSession');
            },
            error:function(response){
                var jsonResponse = JSON.parse(response.responseText);
                var errorMessage = JSON.stringify(jsonResponse["errorMessage"]);
                alert("Error: " + errorMessage);
            }
        });
    };
</script>

<html>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                    <h1 class="page-header">${course.courseName} - Course Info</h1>

                    <button type="button" class="btn btn-primary" onclick="checkCourseMembers()" id="addCourseSessionButton">Add Course Session</button>

                    <br>

                    <br>

                    <a href="<spring:url value="/admin/courseList/editCourse/${course.courseID}"/>" class="btn btn-primary">Edit Course Information/Roster</a>

                    <br>

                    <br>

                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Active Roster</a></li>
                        <li role="presentation"><a href="#tab-inactiveMembers" aria-controls="tab-inactiveMembers" role="tab" data-toggle="tab">Inactive Roster</a></li>
                        <li role="presentation"><a href="#tab-sessions" aria-controls="tab-sessions" role="tab" data-toggle="tab">Sessions</a></li>
                    </ul>

                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                            <table id="memberListTable" class="table table-striped dt-responsive">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>Student ID#</th>
                                    <th>Student Name</th>
                                    <th>Attendance</th>
                                    <th>Finances</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${memberList}" var="member">
                                    <tr>
                                        <td></td>
                                        <td>${member.memberID}</td>
                                        <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                        <td>${memberAttendanceCountMap.get(member.memberID)}/${courseSessionList.size()}</td>
                                        <td><a href=""><span class="glyphicon glyphicon-usd"></span></a></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div role="tabpanel" class="tab-pane fade in active" id="tab-inactiveMembers">
                            <table id="inactiveMemberListTable" class="table table-striped dt-responsive">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>Student ID#</th>
                                    <th>Student Name</th>
                                    <th>Attendance</th>
                                    <th>Finances</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${inactiveMemberList}" var="member">
                                    <tr>
                                        <td></td>
                                        <td>${member.memberID}</td>
                                        <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                        <td>${memberAttendanceCountMap.get(member.memberID)}/${courseSessionList.size()}</td>
                                        <td><a href=""><span class="glyphicon glyphicon-usd"></span></a></td>
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
                                            <td><fmt:parseDate value="${courseSession.courseSessionDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                <fmt:formatDate value="${parsedDate}" var="formattedDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />
                                                    ${formattedDate}</td>
                                            <td>${courseSession.numMembersAttended} / ${courseSessionMemberCountMap.get(courseSession.courseSessionID)} Students</td>
                                            <td><a href="<spring:url value="/admin/courseList/courseInfo/${course.courseID}/editCourseSession/${courseSession.courseSessionID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                            <td><fmt:formatDate value="${parsedDate}" var="formattedHiddenDate" type="date" pattern="yyyy/MM/dd" timeZone="GMT" />${formattedHiddenDate}</td>
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

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>

