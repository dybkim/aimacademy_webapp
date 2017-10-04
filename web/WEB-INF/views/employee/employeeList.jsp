

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 9/28/17
  Time: 12:30 PM
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

        var employeeListTable = $('#employeeListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[0, "desc"]],
        });

        var inactiveEmployeeListTable = $('#inactiveEmployeeListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[0, "desc"]],
        });

        $('.nav-tabs a[href="#tab-employees"]').tab('show');
    });
</script>
<html>
<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Employees</h1>

            <a href="<spring:url value="/admin/employee/addEmployee"/>" class="btn btn-primary">Add Employee</a>

            <br>
            <br>

            <ul class="nav nav-tabs" role="tablist">
            <li role="presentation"><a href="#tab-employees" aria-controls="tab-courses" role="tab" data-toggle="tab">Active Employees</a></li>
            <li role="presentation"><a href="#tab-inactiveEmployees" aria-controls="tab-inactiveCourses" role="tab" data-toggle="tab">Inactive Employees</a></li>
            </ul>

            <div class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active" id="tab-employees">
                    <table class="table table-striped" id="employeeListTable">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Role</th>
                            <th>Start Date</th>
                            <th>Payroll</th>
                            <th>Edit Info</th>
                        </tr>
                        </thead>
                    <tbody>
                    <c:forEach items="${employeeList}" var="employee" varStatus="i" begin="0">
                        <tr>
                            <td>${i.count}</td>
                            <td>${employee.employeeFirstName} ${employee.employeeLastName}</td>
                            <td>${employee.jobDescription}</td>
                            <td><fmt:parseDate value="${employee.dateEmployed}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="formattedHiddenDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />${formattedHiddenDate}</td>
                            <td>TBA</td>
                            <td><a href="<spring:url value="/admin/employee/${employee}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane fade in active" id="tab-inactiveEmployees">
                    <table class="table table-striped" id="inactiveEmployeeListTable">
                    <thead>
                    <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Start Date</th>
                    <th>Payroll</th>
                    <th>Edit Info</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${inactiveEmployeeList}" var="employee" varStatus="i" begin="0">
                        <tr>
                            <td>${i.count}</td>
                            <td>${employee.employeeFirstName} ${employee.employeeLastName}</td>
                            <td>${employee.jobDescription}</td>
                            <td><fmt:parseDate value="${employee.dateEmployed}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="formattedHiddenDate" type="date" pattern="MM/dd/yyyy" timeZone="GMT" />${formattedHiddenDate}</td>
                            <td>TBA</td>
                            <td><a href="<spring:url value="/admin/employee"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
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
