<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        $('a[data-toggle="tabpanel"]').on( 'shown.bs.tab', function () {
            $.fn.dataTable.tables(true).columns.adjust();
        } );

        var membershipTable = $('#membershipTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]]
        });

        var inactiveTable = $('#inactiveTable').DataTable({
            "lengthMenu":[[50,-1], [50,'All']]
        });

        $('.nav-tabs a[href="#tab-members"]').tab('show');

        $('#selectMonthBox').change(function(){
            var json = $.parseJSON($(this).val());
            var month = json[0];
            var year = json[1];
            window.location.replace('/admin/student/studentList?month=' + month + '&year=' + year);
        });

    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Student List: Membership</h1>

            <h3>${cycleStartDate.month} ${cycleStartDate.year}</h3>

            <form:select path="cycleStartDate" id="selectMonthBox">
                <form:option selected="true" value="">Select Month</form:option>
                <c:forEach items="${monthsList}" var="date">
                    <form:option value="[${date.monthValue},${date.year}]">${date.month} ${date.year}</form:option>
                </c:forEach>
            </form:select>

            <br>
            <br>

            <a href="<spring:url value="/admin/student/studentList/addStudent"/>" class="btn btn-primary">Add New Member</a>

            <br>

            <br>

            <form:form action="${pageContext.request.contextPath}/admin/student/studentList?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}" method="post" modelAttribute="memberListWrapper">

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
                            <th>Finances</th>
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
                                    <td><form:checkbox path="membershipHashMap[${member.memberID}]" value="${memberListWrapper.membershipHashMap.get(member.memberID)}" id="isActiveCheckbox"/></td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
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
                            <th>Finances</th>
                            <th>Edit Profile</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${memberListWrapper.inactiveMemberList}" var="member" varStatus="i" begin="0">
                            <tr>
                                <form:hidden path="inactiveMemberList[${i.index}].memberID" value="${member.memberID}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberFirstName" value="${member.memberFirstName}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberLastName" value="${member.memberLastName}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberPhoneNumber" value="${member.memberPhoneNumber}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberEmailAddress" value="${member.memberEmailAddress}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberStreetAddress" value="${member.memberStreetAddress}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberAddressApartment" value="${member.memberAddressApartment}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberCity" value="${member.memberCity}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberZipCode" value="${member.memberZipCode}"/>
                                <form:hidden path="inactiveMemberList[${i.index}].memberState" value="${member.memberState}"/>
                                <fmt:parseDate value="${member.memberEntryDate}" pattern="yyyy-MM-dd" var="parsedInactiveDate" type="date" />
                                <fmt:formatDate value="${parsedInactiveDate}" var="formattedInactiveDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />
                                <form:hidden path="inactiveMemberList[${i.index}].memberEntryDate" id="entryDate" class="form-Control" value="${formattedInactiveDate}"/>
                                <td><a href="<spring:url value ="/admin/studentList/editStudent/${member.memberID}"/>">${member.memberID}</a></td>
                                <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                <td><form:checkbox path="membershipHashMap[${member.memberID}]" value="${memberListWrapper.membershipHashMap.get(member.memberID)}" id="isActiveCheckbox"/></td>
                                <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
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
