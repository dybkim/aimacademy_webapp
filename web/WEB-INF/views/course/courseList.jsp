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

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<script>
    $(document).ready(function(){

        $('.table').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
        });
    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Course List</h1>

            <br>

            <a href="<spring:url value="/admin/courseList/addCourse"/>" class="btn btn-primary">Add Course</a>

            <br>

            <br>

            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Course ID#</th>
                        <th>Course Name</th>
                        <th>Course Type</th>
                        <th>Students Enrolled</th>
                        <th>Finances</th>
                        <th>Edit Course Info</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${courseList}" var="course">
                        <tr>
                            <td>${course.courseID}</td>
                            <td><a href="<spring:url value="/admin/courseList/viewEnrollment/${course.courseID}"/>">${course.courseName}</a></td>
                            <td>${course.courseType}</td>
                            <td>${course.numEnrolled}</td>
                            <td><a href=""><span class="glyphicon glyphicon-usd"></span></a></td>
                            <td><a href="<spring:url value="/admin/courseList/editCourse/${course.courseID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
