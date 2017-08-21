<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        var monthlySummaryListTable = $('#monthlySummaryListTable').DataTable({
            "lengthMenu": [[25,50,-1], [25,50, "All"]],
            "order": [[8, "desc"]],
            "columnDefs": [
                {
                    "targets": [8],
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
                        <th>Expected Revenue</th>
                        <th>Actual Revenue</th>
                        <th>Costs</th>
                        <th>Net Profit</th>
                        <th>Breakdown</th>
                        <th>YearMonth Hidden</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${monthlyFinancesSummaryList}" var="monthlyFinancesSummary">
                        <tr>
                            <td>${monthlyFinancesSummary.cycleStartDate.month.toString()} ${monthlyFinancesSummary.cycleStartDate.year}</td>
                            <td>${seasonDescriptionHashMap.get(monthlyFinancesSummary.monthlyFinancesSummaryID)}</td>
                            <td>${monthlyFinancesSummary.numMembers}</td>
                            <th>${monthlyFinancesSummary.totalChargeAmount}</th>
                            <th>${monthlyFinancesSummary.totalPaymentAmount}</th>
                            <td>TBA</td>
                            <td>TBA</td>
                            <td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>
                            <td><fmt:parseDate value="${monthlyFinancesSummary.cycleStartDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="formattedYear" type="date" pattern="yyyy" timeZone="GMT" />
                                <fmt:formatDate value="${parsedDate}" var="formattedMonth" type="date" pattern="MM" timeZone="GMT" />
                                    ${formattedYear}${formattedMonth}</td>
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
