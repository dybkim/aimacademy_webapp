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
<!DOCTYPE html>
<html lang="en">

<body>
<%@include file="../template/navbar.jsp"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Add new student</h1>

            <form:form action="${pageContext.request.contextPath}/admin/studentList/addStudent" method="post" modelAttribute="member">

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
                    <label for="zipCode">Zip Code</label>
                    <form:input path="memberZipCode" id="zipCode" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="state">State</label>
                    <form:input path="memberState" id="state" class="form-Control"/>
                </div>

                <div class="form-group">
                    <label for="entryDate">Date Joined (MM/DD/YYYY)</label>
                    <form:input path="memberEntryDate" id="entryDate" class="form-Control"/>
                </div>

                <form:hidden path="memberIsActive" value="true"/>

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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="<spring:url value="/resources/js/jquery-3.1.1.min.js"/>"<\/script>')</script>
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


