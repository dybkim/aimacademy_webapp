<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
                { "width": "20%" },
            ]
        });

        var dropListTable = $('#dropListContainer').DataTable({
            "lengthMenu": [10,-1],
            "searching": false,
            "paging": false,
            "columns": [
                { "width": "20%" },
                { "width": "30%" },
                { "width": "20%" },
            ]
        });

        memberListTable.on('click', '#removeMemberButton', function (){
            var tr = $(this).closest("tr");
            tr.find('button').attr("id","addMemberButton");
            var row = memberListTable.row(tr);

            row.remove().draw();

            dropListTable.row.add($(tr)).draw();

        });


        dropListTable.on('click','#addMemberButton', function(){
            var tr = $(this).closest("tr");
            tr.find('button').attr("id","removeMemberButton");
            var row = dropListTable.row(tr);

            row.remove().draw();

            memberListTable.row.add($(tr)).draw();
        });
    })

</script>

<html lang="en">

<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Edit Course: ${courseRegistrationWrapper.course.courseName}</h1>

            <form:form action="${pageContext.request.contextPath}/admin/courseList/editCourse/${courseRegistrationWrapper.course.courseID}}" method="POST" modelAttribute="courseRegistrationWrapper">
                <div class="form-group"><form:errors path="course.courseName" cssStyle="color: #FF0000"/>
                    <label for="courseTitle">Course Title</label>
                    <form:input path="course.courseName" id="courseTitle" class="form-Control" value="${courseRegistrationWrapper.course.courseName}"/>
                </div>

                <div class="form-group"><form:errors path="course.courseType" cssStyle="color: #FF0000"/>
                    <label for="courseType">Course Type</label>
                    <form:select path="course.courseType" id="courseType" class="form-Control" value="${courseRegistrationWrapper.course.courseType}">
                        <form:option value="Supplement"/>
                        <form:option value="Finals Prep"/>
                        <form:option value="Test Prep"/>
                        <form:option value="Office Hour"/>
                        <form:option value="Other"/>
                    </form:select>
                </div>

                <div class="form-group"><span style="color: #FF0000">${startDateErrorMessage}</span>
                    <label for="startDate">Start Date (MM/DD/YYYY)</label>
                    <form:input path="course.courseStartDate" id="startDate" class="date" value="${courseRegistrationWrapper.course.courseStartDate}"/>
                </div>

                <div class="form-group"><span style="color: #FF0000">${endDateErrorMessage}</span>
                    <label for="endDate">End Date (MM/DD/YYYY)</label>
                    <form:input path="course.courseEndDate" id="endDate" class="date" value="${courseRegistrationWrapper.course.courseEndDate}"/>
                </div>

                <h><b>Members Enrolled</b></h>
                <div class="table-responsive">
                    <table class="table table-striped dt-responsive" id="memberListContainer">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Drop Enrollment</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${courseRegistrationWrapper.memberList}" var="member" varStatus="i" begin="0">
                            <tr>
                                <td><form:hidden path="memberList[${i.index}].memberID" value="${member.memberID}" id="$P"/></td>
                                <td><form:hidden path="memberList[${i.index}].memberFirstName" value="${member.memberFirstName}"/> <form:hidden path="memberList[${i.index}].memberLastName" value="${member.memberLastName}"/></td>
                                <td><button type="button" class="glyphicon glyphicon-remove" id="removeMemberButton"></button></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <h><b>Drop Enrollment Table</b></h>

                <div class="table-responsive">
                    <table class="table table-striped dt-responsive" id="dropListContainer">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Cancel Drop</th>
                        </tr>
                        </thead>


                        <tbody>
                            <c:forEach items="${dropMemberList}" var="member">
                                <tr>
                                    <td><form:hidden path="member.memberID" value="${member.memberID}"/>${member.memberID}</td>
                                    <td><form:hidden path="member.memberFirstName" value="${member.memberFirstName}"/><form:hidden path="member.memberLastName" value="${member.memberLastName}"/>${member.memberFirstName} ${member.memberLastName}</td>
                                    <td><button type="button" class="glyphicon glyphicon-remove" id="addMemberButton"></button></td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </div>

                <form:hidden path="course.isActive" value="${courseRegistrationWrapper.course.isActive}"/>

                <form:hidden path="course.numEnrolled" value="${courseRegistrationWrapper.course.numEnrolled}"/>

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
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


