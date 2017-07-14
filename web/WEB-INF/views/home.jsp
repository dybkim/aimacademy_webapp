<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 1/18/17
  Time: 5:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="template/navbar.jsp"%>
<%@include file="template/sidebar.jsp" %>

<script>
    $(document).ready(function(){

        var monthlySummaryListTable = $('#monthlySummaryListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[6, "desc"]],
            "columnDefs": [
                {
                    "targets": [6],
                    "visible": false,
                    "searchable": false
                }
            ]
        });
    });
</script>

<html>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Home</h1>

            <br>

            <a href="<spring:url value=""/>" class="btn btn-primary">Generate Summaries</a>

            <br>

            <br>

            <div class="table-responsive">
                <table id="monthlySummaryListTable" class="table table-striped">
                    <thead>
                    <tr>
                        <th>Month/Year</th>
                        <th>Season</th>
                        <th>Active Members</th>
                        <th>Charges Paid</th>
                        <th>Courses Held</th>
                        <th>Info</th>
                        <th>YearMonth Hidden</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${monthlyChargesSummaryList}" var="monthlyChargesSummary">
                        <tr>
                            <jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
                            <c:set value="${monthlyChargesSummary.cycleStartDate}" var="cycleStartDate" />

                            <td><fmt:formatDate type="date" value="cycleStartDate" pattern="M"/> <fmt:formatDate type="date" value="cycleStartDate" pattern="yyyy"/></td>
                            <td>${monthlyChargesSummary.seasonID}</td>
                            <td>${monthlyChargesSummary.numMembers}</td>
                            <th>${monthlyChargesSummary.numChargesFulfilled} / ${monthlyChargesSummary.numChargesTotal}</th>
                            <td>${monthlyChargesSummary.numCourses}</td>
                            <td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>
                            <td><fmt:formatDate type="date" value="cycleStartDate" pattern="yyyy"/><fmt:formatDate type="date" value="cycleStartDate" pattern="MM"/></td>
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
