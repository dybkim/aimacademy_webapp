<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">${member.memberFirstName} ${member.memberLastName}</h1>

            <a href="<spring:url value="/admin/studentList/editStudent/${member.memberID}"/>" class="btn btn-primary">Edit Profile</a>

            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Course Enrolled</th>
                        <th>Session</th>
                        <th>Course Type</th>
                        <th>Payment Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%--<c:forEach items="${memberList}" var="member">--%>
                        <%--<tr>--%>
                            <%--<td>${member.memberID}</td>--%>
                            <%--<td>${member.memberFirstName} ${member.memberLastName}</td>--%>
                            <%--<td><c:choose>--%>
                                <%--<c:when test="${member.memberIsActive == true}">Active</c:when>--%>
                                <%--<c:otherwise>Inactive</c:otherwise>--%>
                            <%--</c:choose></td>--%>
                            <%--<td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>--%>
                            <%--<td><a href="/admin/studentList/editStudent/${member.memberID}"><span class="glyphicon glyphicon-pencil"></span></a></td>--%>
                        <%--</tr>--%>
                    <%--</c:forEach>--%>
                    </tbody>
                </table>
            </div>
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
