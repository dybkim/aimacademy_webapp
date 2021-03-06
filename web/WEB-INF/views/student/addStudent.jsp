<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<script>
    $(document).ready(function(){
        $('#entryDate').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#entryDate').keydown(function (e) {
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
            <h1 class="page-header">Add new student</h1>

            <form:form action="${pageContext.request.contextPath}/admin/student/studentList/addStudent" method="POST" modelAttribute="member">

                <div class="form-group">
                    <label for="firstName">First Name</label><form:errors path="memberFirstName" cssStyle="color: #FF0000"/>
                    <form:input path="memberFirstName" id="firstName" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="lastName">Last Name</label><form:errors path="memberLastName" cssStyle="color:#FF0000"/>
                    <form:input path="memberLastName" id="lastName" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Phone Number</label>
                    <form:input path="memberPhoneNumber" id="phoneNumber" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="emailAddress">Email Address</label>
                    <form:input path="memberEmailAddress" id="emailAddress" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="streetAddress">Address</label>
                    <form:input path="memberStreetAddress" id="streetAddress" class="form-Control"/>
                </div>


                <div class="form-group">
                    <label for="apartment">Apartment Number</label>
                    <form:input path="memberAddressApartment" id="apartment" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="city">City</label>
                    <form:input path="memberCity" id="city" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="state">State</label>
                    <form:input path="memberState" id="state" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="zipCode">Zip Code</label>
                    <form:input path="memberZipCode" id="zipCode" class="form-Control"/>
                </div>

                <div class="form-group">
                    <span style="color: #FF0000">${dateJoinedErrorMessage}</span>
                    <label for="entryDate">Date Joined (MM/DD/YYYY)</label>
                    <form:input path="memberEntryDate" id="entryDate" class="form-Control"/>
                </div>

                <div class="form-group">
                    <span style="color: #FF0000">${membershipRateErrorMessage}</span>
                    <label for="membershipRate">Membership Rate</label>
                    <form:input path="membershipRate" id="membershipRate" placeholder="Price is ${membershipRate} if empty" class="form-Control"/>
                </div>

                <br><br>

                <input type="submit" value="Submit" class="btn btn=default">

                <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">

            </form:form>
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


