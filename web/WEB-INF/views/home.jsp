<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: davidkim
  Date: 8/15/17
  Time: 2:43 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="template/navbar.jsp"%>
<%@ include file="template/sidebar.jsp"%>

<script>

    function format(memberID){
        var table = '<table cellpadding="5" cellspacing="0" border="1" style="padding-left:50px;">';
        $.ajax({
            url: "/admin/student/rest/studentFinances/getChargeList/" + memberID,
            type: "GET",
            async: false,
            dataType: "json",
            data: {
                month: ${cycleStartDate.monthValue},
                year: ${cycleStartDate.year}
            },
            success:function(chargeList){
                for(var i = 0; i < chargeList.length; i++){
                    table = table + '<tr>';
                    var description = JSON.stringify(chargeList[i].description).replace(/\"/g, "");
                    var chargeAmount = JSON.stringify(chargeList[i].chargeAmount);
                    var hoursBilled = JSON.stringify(chargeList[i].hoursBilled);

                    table = table + '<td><b>Charge Description:</b></td>' +
                            '<td>' + description + '</td>' +
                            '<td><b>Hours Billed</b></td>' +
                            '<td>' + hoursBilled + '</td>' +
                            '<td><b>Charge Amount:</b></td>' +
                            '<td>' + chargeAmount + '</td>' +
                            '</tr>';
                }

                table = table + '</table>';
            },
            error:function(response){
                var jsonResponse = JSON.parse(response.responseText);
                var errorMessage = JSON.stringify(jsonResponse["errorMessage"]);
                alert("Error: " + errorMessage);
                table = null;
            }
        });
        return table;
    }

    $(document).ready(function() {

        $('a[data-toggle="tabpanel"]').on('shown.bs.tab', function () {
            $.fn.dataTable.tables(true).columns.adjust();
        });

        var memberTable = $('#memberTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]],
            "order": [["1", "desc"]]
        });

        var paidBalanceMemberTable = $('#paidBalanceMemberTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]],
            "order": [["1", "desc"]]
        });

        $('.nav-tabs a[href="#tab-members"]').tab('show');

        $('#selectMonthBox').change(function(){
            window.location.replace('/admin/home' + $(this).val());
        });

        $('#memberTable tbody').on('click', 'td.details-control', function(){
            var tr = $(this).closest('tr');
            var row = memberTable.row(tr);
            var id = $(this).closest('tr').find("td").eq(1).text();

            if(row.child.isShown()){
                row.child.hide();
                tr.removeClass('shown');
            }

            else{
                row.child(format(id)).show();
                tr.addClass('shown');
            }
        });

        $('#paidBalanceMemberTable tbody').on('click', 'td.details-control', function(){
            var tr = $(this).closest('tr');
            var row = paidBalanceMemberTable.row(tr);
            var id = $(this).closest('tr').find("td").eq(1).text();

            if(row.child.isShown()){
                row.child.hide();
                tr.removeClass('shown');
            }

            else{
                row.child(format(id)).show();
                tr.addClass('shown');
            }
        })
    });

</script>

<html>
    <body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                <h1 class="page-header">Home</h1>
                <h3>${cycleStartDate.month} ${cycleStartDate.year}</h3>

                <form:select path="cycleStartDate" id="selectMonthBox">
                    <form:option selected="true" value="">Select Month</form:option>
                    <c:forEach items="${monthsList}" var="date">
                        <form:option value="?month=${date.monthValue}&year=${date.year}">${date.month} ${date.year}</form:option>
                    </c:forEach>
                </form:select>

                <br>
                <br>

                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Outstanding Balances</a></li>
                    <li role="presentation"><a href="#tab-paidBalanceMembers" aria-controls="tab-paidBalanceMembers" role="tab" data-toggle="tab">Paid Balances</a></li>
                </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                        <table id="memberTable" class="table table-striped">
                            <thead>
                            <tr>
                                <th></th>
                                <th>ID</th>
                                <th>Member Name</th>
                                <th>Outstanding Balance</th>
                                <th>Amount Paid</th>
                                <th>Finance Info</th>
                                <th>Download Invoice</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${outstandingBalanceMemberList}" var="member">
                                <tr class="parent">
                                    <td class="details-control"><span class="glyphicon glyphicon-plus-sign"></span></td>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${balanceAmountHashMap.get(member.memberID)}</td>
                                    <td>${paymentAmountHashMap.get(member.memberID)} / ${chargesAmountHashMap.get(member.memberID)}</td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
                                    <td><a href="<spring:url value="/admin/resources/excel/generateInvoice/student/${member.memberID}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div role="tabpanel" class="tab-pane fade in active" id="tab-paidBalanceMembers">
                        <table id="paidBalanceMemberTable" class="table table-striped">
                            <thead>
                            <tr>
                                <th></th>
                                <th>ID</th>
                                <th>Member Name</th>
                                <th>Outstanding Balance</th>
                                <th>Amount Paid</th>
                                <th>Finance Info</th>
                                <th>Download Invoice</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${paidBalanceMemberList}" var="member">
                                <tr>
                                    <td class="details-control" border="1"><span class="glyphicon glyphicon-plus-sign"></span></td>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${balanceAmountHashMap.get(member.memberID)}</td>
                                    <td>${paymentAmountHashMap.get(member.memberID)} / ${chargesAmountHashMap.get(member.memberID)}</td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
                                    <td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
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
