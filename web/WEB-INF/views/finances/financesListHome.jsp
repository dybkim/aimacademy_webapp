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
            "order":[[0,"desc"],[1,"desc"]]
        });
    });
</script>

<!DOCTYPE html>
<html lang="en">
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1 class="page-header">Finances</h1>

            <br>

            <br>

            <br>

            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Month</th>
                        <th>Outstanding Charges</th>
                        <th>Revenue</th>
                        <th>Costs</th>
                        <th>More Info</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${monthlyFinancesSummaryList}" var="monthlyFinancesSummary">
                        <tr>
                            <jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
                            <c:set value="${monthlyFinancesSummary.summaryMonth}" var="month" />

                            <td>${month} ${monthlyFinancesSummary.summaryYear}</td>
                            <th>${monthlyFinancesSummary.numChargesFulfilled} / ${monthlyFinancesSummary.numChargesTotal}</th>
                            <td><a href="">TBA</a></td>
                            <td><a href="">TBA</a></td>
                            <td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>
