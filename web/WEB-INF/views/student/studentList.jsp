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

        $('.table').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]],
        });
    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Student List</h1>

            <br>

            <a href="<spring:url value="/admin/studentList/addStudent"/>" class="btn btn-primary">Add Student</a>

            <br>

            <br>

            <form:form action="${pageContext.request.contextPath}/admin/studentList" method="post" modelAttribute="memberListWrapper">

                <input type="submit" value="Save Changes" class="btn btn=default">

                <br>

                <br>

                <div class="table-responsive">
                    <table class="table table-striped">
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
                                        <fmt:formatDate value="${member.memberEntryDate}" var="dateString" pattern="MM/dd/yyyy" timeZone="GMT"/>
                                        <form:hidden path="memberList[${i.index}].memberEntryDate" id="entryDate" class="form-Control" value="${dateString}"/>
                                        <td><a href="<spring:url value ="/admin/studentList/editStudent/${member.memberID}"/>">${member.memberID}</a></td>
                                        <td>${member.memberFirstName} ${member.memberLastName}</td>
                                        <td><form:checkbox path="memberList[${i.index}].memberIsActive" value="${member.memberIsActive}"/></td>
                                        <td><a href="<spring:url value="/admin/studentHistory/${member.memberID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                        <td><a href="<spring:url value="/admin/studentList/editStudent/${member.memberID}"/>"><span class="glyphicon glyphicon-pencil"></span></a></td>
                                    </tr>
                                </c:forEach>
                        </tbody>
                    </table>
                </div>

                <br>

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
                        </tbody>

                    </table>
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
