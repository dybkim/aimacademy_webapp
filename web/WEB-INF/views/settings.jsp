
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 11/6/17
  Time: 8:59 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8"%>

<%@ include file="template/navbar.jsp"%>
<%@ include file="template/sidebar.jsp"%>

<script>
    $(document).ready(function(){

    $("#confirmPassword").keyup(function(){

        if($("#newPassword").val() !== $("#confirmPassword").val()){
            $("#break").hide();
            $("#passwordError").show();
        }

        else{
            $("#break").show();
            $("#passwordError").hide();
        }
        });
    });

</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Settings</h1>

            <form:form action="${pageContext.request.contextPath}/admin/settings" method="POST" modelAttribute="newPasswordFormWrapper" style="width:500px">

                <span style="color: #FF0000">${currentPasswordErrorMessage}</span>
                <div class="form-group">
                    <label for="currentPassword">Current Password:</label>
                    <form:input path="currentPassword" id="currentPassword" type="password" class="form-control" style="width:300px; display:inline; float:right"/>
                </div>

                <br id="break">

                <div id="passwordError" style="display:none; color: #FF0000">Passwords must match!</div>
                <span style="color: #FF0000" id="confirmPasswordErrorMessage">${confirmPasswordErrorMessage}</span>

                <div class="form-group">
                    <label for="newPassword">New Password:</label>
                    <form:input path="newPassword" id="newPassword" type="password" class="form-control" style="width:300px; display:inline; float:right"/>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <form:input path="confirmPassword" type="password" id="confirmPassword" class="form-control" style="width:300px; display:inline; float: right;"/>
                </div>

                <br>

                <input type="submit" value="Save" class="btn btn-default"/>

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

