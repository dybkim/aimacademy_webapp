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

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<!-- Input form formatting -->
<link href="<spring:url value="/resources/css/inputDropdown.css"/>" rel="stylesheet">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<script>

</script>
<html lang="en">

<body>

<div class="container-fluid" ng-app="addStudentToCourseControllerApp">
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <div ng-controller="addStudentToCourseController" ng-init="refreshCourseRegistrationWrapper('${courseRegistrationWrapper}')">
            <h1 class="page-header">${course.courseName}: Add students</h1>

            <input type="submit" my-on-key-down-call="fetchMember(memberString)" ng-click="addToRegistrationList()"

            <div class="container-fluid" >
                <table id="addMembersTable" class="table table-striped">
                    <thead>
                        <tr>
                            <th>Member ID</th>
                            <th>Member</th>
                            <th>Cancel</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr ng-repeat="courseRegistrationWrapperObject in courseRegistrationWrapperObjectList">
                            <td>{{courseRegistrationWrapperObject.member.memberID}}</td>
                            <td>{{courseRegistrationWrapperObject.member.memberFirstName}} {{courseRegistrationWrapperObject.member.memberLastName}}</td>
                            <td><a class="label label-danger" ng-click="removeStudentFromCourse(courseRegistrationWrapperObjectList, courseRegistrationWrapperObject.member.memberID)"><span class="glyphicon glyphicon-remove"></span></a></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <input type="submit" value="Submit" class="btn btn=default">

            <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">
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


