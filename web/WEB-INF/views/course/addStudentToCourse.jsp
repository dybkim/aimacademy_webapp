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

<!-- Input form formatting -->
<script src="<spring:url value="/resources/js/addStudentToCourseController.js"/>"></script>

<!DOCTYPE html>
<html lang="en">
<body>

<div class="container-fluid" ng-app="addStudentToCourseControllerApp">
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <div class="content" ng-controller="addStudentToCourseController" ng-init="init('${course.courseID}')">
            <h1 class="page-header">${course.courseName}: Add students</h1>

            <form name="inputMember" ng-submit="addToRegistrationList()">
                <label for="memberDropdown">Search: </label>
                <input id="memberDropdown" ng-model="selectedMember" uib-typeahead="member as member.name for member in memberList | filter: {name:$viewValue}" typeahead-min-length="1" autocomplete="off" placeholder="Please use autofill">
            </form>

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
                        <tr ng-repeat="member in registrationList">
                            <td>{{member.id}}</td>
                            <td>{{member.name}}</td>
                            <td><a class="label label-danger" ng-click="removeStudentFromCourseRegistration(member)"><span class="glyphicon glyphicon-remove"></span></a></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <input type="submit" value="Submit" class="btn btn=defaul" ng-click="submitRegistrationList()">

            <input action="action" onclick="history.go(-1);" value="Cancel" class="btn btn-default">
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
</body>
</html>


