
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 10/9/17
  Time: 7:44 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../template/navbar.jsp"%>
<%@ include file="../template/sidebar.jsp"%>

<script>
    $(document).ready(function(){
        $('#startDate').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#startDate').keydown(function (e) {
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
            <h1 class="page-header">Add Employee</h1>

            <form:form action="${pageContext.request.contextPath}/admin/employee/addEmployee" method="POST" modelAttribute="employee">

                <form:hidden path="employeeID" value="${employee.employeeID}"/>

                <div class="form-group"><form:errors path="employeeFirstName" cssStyle="color: #FF0000"/>
                    <label for="employeeFirstName">First Name</label>
                    <form:input path="employeeFirstName" id="employeeFirstName" class="form-Control" value="${employee.employeeFirstName}" cssStyle="width: 400px"/>
                </div>

                <div class="form-group"><form:errors path="employeeLastName" cssStyle="color: #FF0000"/>
                    <label for="employeeLastName">Last Name</label>
                    <form:input path="employeeLastName" id="employeeLastName" class="form-Control" value="${employee.employeeLastName}" cssStyle="width: 400px"/>
                </div>

                <div class="form-group">
                    <label for="jobDescription">Job Description</label>
                    <form:select path="jobDescription" id="jobDescription" class="form-Control" value="${employee.jobDescription}" cssStyle="width: 200px">
                        <form:option value="Director"/>
                        <form:option value="Instructor"/>
                        <form:option value="Admin"/>
                        <form:option value="Other"/>
                    </form:select>
                </div>

                <div class="form-group">
                    <span style="color: #FF0000">${dateEmployedErrorMessage}</span>
                    <label for="startDate">Date Employed (MM/DD/YYYY)</label>
                    <fmt:parseDate value="${employee.dateEmployed}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="formattedDate" type="date" pattern="MM/dd/yyyy" />
                    <form:input path="dateEmployed" id="startDate" class="date" value="${formattedDate}"/>
                </div>

                <div class="form-group">
                    <label for="isActiveCheckBox">Is Active:</label>
                    <form:checkbox path="isActive" value="${employee.isActive}" id="isActiveCheckBox"/>
                </div>

                <br><br>

                <input type="submit" value="submit" class="btn btn=default">

                <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">

            </form:form>
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>



