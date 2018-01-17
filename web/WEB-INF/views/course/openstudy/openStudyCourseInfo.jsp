<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/9/18
  Time: 12:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../template/navbar.jsp"%>
<%@include file="../../template/sidebar.jsp" %>

<script>
    function formatCourseSession(courseSessionID){
        var table = '<table cellpadding="5" cellspacing="0" border="1" style="padding-left:50px;">';
        $.ajax({
            url: "/admin/courseList/rest/getCourseSession/" + courseSessionID,
            type: "GET",
            async: false,
            dataType: "json",
            success:function(memberList){
                if(memberList.length !== 0){
                    for(var i = 0; i < memberList.length; i++){
                        table = table + '<tr>';
                        var memberID = JSON.stringify(memberList[i].memberID);
                        var memberName = JSON.stringify(memberList[i].memberFirstName).replace(/\"/g, "") + " " + JSON.stringify(memberList[i].memberLastName).replace(/\"/g, "");

                        table = table +
                            '<td>' + memberID + '</td>' +
                            '<td><b>' + memberName + '</b></td>' +
                            '</tr>';
                    }
                }
                else
                    table = table + '<tr><td><b>No Members Found</b></td></tr>';

                table = table + '</table>';
            },
            error:function(response){
                var jsonResponse = JSON.parse(response.responseText);
                var errorMessage = JSON.stringify(jsonResponse["errorMessage"]);
                alert("Error: " + errorMessage);
                table = null;
            }
        });
        return table;
    }

    $(document).ready(function(){
        $('#selectMonthBox').change(function(){
            window.location.replace('/admin/courseList/courseInfo/${course.courseID}' + $(this).val());
        });

        $('#addCourseSessionButton').click(function(){
            $.ajax({
                url: "${pageContext.request.contextPath}/admin/courseList/rest/${course.courseID}/validateAddCourseSession",
                type: "GET",
                async: false,
                data: {
                    month: '${cycleStartDate.monthValue}',
                    year: '${cycleStartDate.year}'
                },
                dataType: "json",
                success:function(){
                    window.location.replace('/admin/courseList/courseInfo/${course.courseID}/addCourseSession?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}');
                },
                error:function(response){
                    var jsonResponse = JSON.parse(response.responseText);
                    var errorMessage = JSON.stringify(jsonResponse["errorMessage"]);
                    alert("Error: " + errorMessage);
                }
            });
        });

        $('a[data-toggle="tabpanel"]').on( 'shown.bs.tab', function (e) {
            $.fn.dataTable.tables(true).columns.adjust();
        } );

        var memberListTable = $('#memberListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]]
        });

        var courseSessionTable = $('#courseSessionListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[5, "asc"]],
            "columnDefs": [
                {
                    "targets": [5,6],
                    "visible": false,
                    "searchable": false
                }
            ]
        });

        <!--Places an index column for the courseSessionTable-->
        courseSessionTable.on('order.dt search.dt', function(){
            courseSessionTable.column(1, {search:'applied', order:'applied'}).nodes().each(function (cell, i){
                cell.innerHTML = i + 1;
            });
        }).draw();

        $('#courseSessionListTable tbody').on('click', 'td.details-control', function(){
            var tr = $(this).closest('tr');
            var row = courseSessionTable.row(tr);
            //Have to use .data() since the desired column is hidden
            var id = row.data()[6];
            if(row.child.isShown()){
                row.child.hide();
                tr.removeClass('shown');
            }

            else{
                var table = formatCourseSession(id);
                if(table !== null){
                    row.child(table).show();
                    tr.addClass('shown');
                }
            }
        });

        $('.nav-tabs a[href="#tab-sessions"]').tab('show');
    });
</script>

<!DOCTYPE html>
<html lang="en">
<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">${course.courseName}: ${cycleString}</h1>

            <label for="selectMonthBox">Select Month:</label>
            <form:select path="cycleStartDate" id="selectMonthBox">
                <form:option selected="true" value="">Select Month</form:option>
                <c:forEach items="${monthsList}" var="date">
                    <form:option value="?month=${date.monthValue}&year=${date.year}">${date.month} ${date.year}</form:option>
                </c:forEach>
            </form:select>

            <br>

            <br>

            <button type="button" class="btn btn-primary"  id="addCourseSessionButton">Add Course Session</button>

            <br>

            <br>

            <a href="<spring:url value="/admin/courseList/editCourse/${course.courseID}"/>" class="btn btn-primary">Edit Course Information</a>

            <br>

            <br>

            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Active Roster</a></li>
                <li role="presentation"><a href="#tab-sessions" aria-controls="tab-sessions" role="tab" data-toggle="tab">Attendance</a></li>
            </ul>

            <div class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                    <table id="memberListTable" class="table table-striped dt-responsive">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Attendance</th>
                            <th>Finances</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${memberList}" var="member">
                            <tr>
                                <td>${member.memberID}</td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                <td>${memberAttendanceCountMap.get(member.memberID)}/${courseSessionList.size()}</td>
                                <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane fade in active" id="tab-sessions">
                    <table id="courseSessionListTable" class="table table-striped dt-responsive">
                        <thead>
                        <tr>
                            <th>Expand</th>
                            <th>Session #</th>
                            <th>Date</th>
                            <th>Attendance</th>
                            <th>Edit Session</th>
                            <th>Hidden Date</th>
                            <th>Hidden ID</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${courseSessionList}" var="courseSession">
                            <tr>
                                <td class="details-control" border="1"><span class="glyphicon glyphicon-plus-sign"></span></td>
                                <td></td>
                                <td><fmt:parseDate value="${courseSession.courseSessionDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                    <fmt:formatDate value="${parsedDate}" var="formattedDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />
                                        ${formattedDate}</td>
                                <td>${courseSession.numMembersAttended} / ${memberList.size()} Students</td>
                                <td><a href="<spring:url value="/admin/courseList/courseInfo/${course.courseID}/editCourseSession/${courseSession.courseSessionID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                <td><fmt:formatDate value="${parsedDate}" var="formattedHiddenDate" type="date" pattern="yyyy/MM/dd" timeZone="GMT" />${formattedHiddenDate}</td>
                                <td>${courseSession.courseSessionID}</td>
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
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>


