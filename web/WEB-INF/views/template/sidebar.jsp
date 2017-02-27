<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <li><a href="<spring:url value="/admin/studentList"/>">Students</a></li>
        <li><a href="">Staff</a></li>
        <li><a href="<spring:url value="/admin/courseList"/>">Academics</a></li>
        <li><a href="">Finances</a></li>
        <li><a href="">Forms</a></li>
    </ul>
</div>
