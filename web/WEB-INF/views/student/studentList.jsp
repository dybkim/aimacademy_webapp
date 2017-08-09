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

        var membershipTable = $('#membershipTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]]
        });

        var inactiveTable = $('#inactiveTable').DataTable({
            "lengthMenu":[[50,-1], [50,'All']]
        });

        $('.nav-tabs a[href="#tab-members"]').tab('show');

    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Student List</h1>

            <br>

            <a href="<spring:url value="/admin/student/studentList/addStudent"/>" class="btn btn-primary">Add Student</a>

            <br>

            <br>

            <form:form action="${pageContext.request.contextPath}/admin/student/studentList" method="post" modelAttribute="memberListWrapper">

                <input type="submit" value="Save Changes" class="btn btn=default">

                <br>

                <br>

            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Active</a></li>
                <li role="presentation"><a href="#tab-inactive" aria-controls="tab-inactive" role="tab" data-toggle="tab">Inactive</a></li>
            </ul>

            <div class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                    <table id="membershipTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Membership</th>
                            <th>Classes</th>
                            <th>Edit Profile</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${memberListWrapper.memberList}" var="member" varStatus="i" begin="0">
                            <tr>
                                <form:hidden path="memberList[${i.index}].memberID" value="${member.memberID}"/>
                                <form:hidden path="memberList[${i.index}].memberFirstName" value="${member.memberFirstName}"/>
                                <form:hidden path="memberList[${i.index}].memberLastName" value="${member.memberLastName}"/>
                                <form:hidden path="memberList[${i.index}].memberPhoneNumber" value="${member.memberPhoneNumber}"/>
                                <form:hidden path="memberList[${i.index}].memberEmailAddress" value="${member.memberEmailAddress}"/>
                                <form:hidden path="memberList[${i.index}].memberStreetAddress" value="${member.memberStreetAddress}"/>
                                <form:hidden path="memberList[${i.index}].memberAddressApartment" value="${member.memberAddressApartment}"/>
                                <form:hidden path="memberList[${i.index}].memberCity" value="${member.memberCity}"/>
                                <form:hidden path="memberList[${i.index}].memberZipCode" value="${member.memberZipCode}"/>
                                <form:hidden path="memberList[${i.index}].memberState" value="${member.memberState}"/>
                                <fmt:parseDate value="${member.memberEntryDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="formattedDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />
                                <form:hidden path="memberList[${i.index}].memberEntryDate" id="entryDate" class="form-Control" value="${formattedDate}"/>
                                <td><a href="<spring:url value ="/admin/student/studentList/editStudent/${member.memberID}"/>">${member.memberID}</a></td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                <td><form:checkbox path="memberList[${i.index}].memberIsActive" value="${member.memberIsActive}" id="isActiveCheckbox"/></td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                <td><a href="<spring:url value="/admin/student/studentList/editStudent/${member.memberID}"/>"><span class="glyphicon glyphicon-pencil"></span></a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane fade in active" id="tab-inactive">
                    <table id="inactiveTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>Student ID#</th>
                            <th>Student Name</th>
                            <th>Membership</th>
                            <th>History</th>
                            <th>Edit Profile</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${memberListWrapper.inactiveList}" var="member" varStatus="i" begin="0">
                            <tr>
                                <form:hidden path="inactiveList[${i.index}].memberID" value="${member.memberID}"/>
                                <form:hidden path="inactiveList[${i.index}].memberFirstName" value="${member.memberFirstName}"/>
                                <form:hidden path="inactiveList[${i.index}].memberLastName" value="${member.memberLastName}"/>
                                <form:hidden path="inactiveList[${i.index}].memberPhoneNumber" value="${member.memberPhoneNumber}"/>
                                <form:hidden path="inactiveList[${i.index}].memberEmailAddress" value="${member.memberEmailAddress}"/>
                                <form:hidden path="inactiveList[${i.index}].memberStreetAddress" value="${member.memberStreetAddress}"/>
                                <form:hidden path="inactiveList[${i.index}].memberAddressApartment" value="${member.memberAddressApartment}"/>
                                <form:hidden path="inactiveList[${i.index}].memberCity" value="${member.memberCity}"/>
                                <form:hidden path="inactiveList[${i.index}].memberZipCode" value="${member.memberZipCode}"/>
                                <form:hidden path="inactiveList[${i.index}].memberState" value="${member.memberState}"/>
                                <fmt:parseDate value="${member.memberEntryDate}" pattern="yyyy-MM-dd" var="parsedInactiveDate" type="date" />
                                <fmt:formatDate value="${parsedInactiveDate}" var="formattedInactiveDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />
                                <form:hidden path="inactiveList[${i.index}].memberEntryDate" id="entryDate" class="form-Control" value="${formattedInactiveDate}"/>
                                <td><a href="<spring:url value ="/admin/studentList/editStudent/${member.memberID}"/>">${member.memberID}</a></td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                <td><form:checkbox path="inactiveList[${i.index}].memberIsActive" value="${member.memberIsActive}" id="isActiveCheckbox"/></td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                <td><a href="<spring:url value="/admin/studentList/editStudent/${member.memberID}"/>"><span class="glyphicon glyphicon-pencil"></span></a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <br>
            </div>

            <input type="submit" value="Save Changes" class="btn btn=default">
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
