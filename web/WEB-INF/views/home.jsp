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

<%@ page contentType="text/html;charset=UTF-8"%>
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
                cycleStartDate: '${cycleStartDate.toString()}',
                cycleEndDate: '${cycleEndDate.toString()}'
            },
            success:function(chargeList){
                for(var i = 0; i < chargeList.length; i++){
                    var description = JSON.stringify(chargeList[i].description).replace(/\"/g, "");
                    var billableUnitType = JSON.stringify(chargeList[i].billableUnitType).replace(/\"/g,"");

                    for(var j = 0; j < chargeList[i].chargeLineSet.length; j++){
                        table = table + '<tr>';
                        var chargeAmount = JSON.stringify(chargeList[i].chargeLineSet[j].chargeAmount);
                        var billableUnitsBilled = JSON.stringify(chargeList[i].chargeLineSet[j].billableUnitsBilled);
                        var dateCharged = JSON.stringify(chargeList[i].chargeLineSet[j].dateCharged.month).replace(/\"/g,"")
                            .toLowerCase().replace(/\b[a-z]/g, function(letter) {
                            return letter.toUpperCase();
                            })
                            + ' ' + JSON.stringify(chargeList[i].chargeLineSet[j].dateCharged.dayOfMonth) + ', '
                            + JSON.stringify(chargeList[i].chargeLineSet[j].dateCharged.year) ;

                        table = table + '<td><b>Charge Description:</b></td>' +
                            '<td>' + description + ' - ' + dateCharged + '</td>' +
                            '<td><b>Time Billed (' + billableUnitType + ')</b></td>' +
                            '<td>' + billableUnitsBilled + '</td>' +
                            '<td><b>Charge Amount:</b></td>' +
                            '<td>' + chargeAmount + '</td>' +
                            '</tr>';
                    }
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
            "order": [["1", "asc"]]
        });

        var paidBalanceMemberTable = $('#paidBalanceMemberTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]],
            "order": [["1", "asc"]]
        });

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
        });

        $('#cycleStartDate, #cycleEndDate').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#cycleStartDate, #cycleEndDate').keydown(function (e) {
            // Allow: backspace, delete, tab
            if ($.inArray(e.keyCode, [46, 8, 9]) !== -1 || (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
                // Allow: home, end, left, right, down, up
                (e.keyCode >= 35 && e.keyCode <= 40) || ((e.keyCode >= 48 && e.keyCode <= 57) || e.keyCode === 191)) {
                // let it happen, don't do anything
                return;
            }
            // Ensure that it is a number and stop the keypress
            if ((e.shiftKey || (e.keyCode > 1 || e.keyCode < 200))) {
                e.preventDefault();
            }
        });

        $('#fetchChargesButton').click(function(){
            var cycleStartDateString = $('#cycleStartDate').val();
            var cycleEndDateString = $('#cycleEndDate').val();

            var cycleStartDate = parseDMY(cycleStartDateString);
            var cycleEndDate = parseDMY(cycleEndDateString);

            if(isNaN(cycleStartDate.getTime()) || (isNaN(cycleEndDate.getTime()))){
                alert("Must enter valid date for start and end dates!");
                return;
            }

            var url = '${pageContext.request.contextPath}/admin/home/fetchPeriod?cycleStartDate=' + cycleStartDateString + '&cycleEndDate=' + cycleEndDateString;
            window.location.replace(url);
        });

        $('.nav-tabs a[href="#tab-members"]').tab('show');
    });

    function parseDMY(value) {
        var date = value.split("/");
        var d = parseInt(date[1], 10),
            m = parseInt(date[0], 10),
            y = parseInt(date[2], 10);
        return new Date(y, m - 1, d);
    }
</script>

<!DOCTYPE html>
<html lang="en">
    <body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                <h1 class="page-header">Home</h1>
                <h3>${cycleString}</h3>

                <div class="container" style="float:left; border: thin solid black; padding: 10px; width:auto; margin-bottom: 30px;">
                    <label for="selectMonthBox">Select Month:</label>
                    <form:select path="cycleStartDate" id="selectMonthBox">
                        <form:option selected="true" value="">Select Month</form:option>
                        <c:forEach items="${monthsList}" var="date">
                            <form:option value="?month=${date.monthValue}&year=${date.year}">${date.month} ${date.year}</form:option>
                        </c:forEach>
                    </form:select>
                    <br>

                    <h5>Or Select A Period:</h5>
                    <label for="cycleStartDate">Starting From:</label>
                    <input type="date" id="cycleStartDate" style="float:right;"/>
                    <br>
                    <br>
                    <label for="cycleEndDate">Ending At:</label>
                    <input type="date" id="cycleEndDate" style="float:right;"/>
                    <br>
                    <br>
                    <button type="button" class="btn btn-primary" id="fetchChargesButton" style="float:right">Fetch Charges</button>
                </div>

                <div class="space" style="margin-top: 250px"></div>

                <a href="<spring:url value="/admin/resources/excel/generate/periodSummary/?cycleStartDate=${cycleStartDate.toString()}&cycleEndDate=${cycleEndDate.toString()}"/>"><span class="btn btn-primary" style="float:left; margin-bottom: 20px">Download Period Summary</span></a>

                <div class="container" style="float: left">
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Outstanding Balances</a></li>
                        <li role="presentation"><a href="#tab-paidBalanceMembers" aria-controls="tab-paidBalanceMembers" role="tab" data-toggle="tab">Paid Balances</a></li>
                    </ul>
                </div>

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
                            <c:forEach items="${outstandingChargesPaymentDTO.outstandingBalanceMemberList}" var="member">
                                <tr class="parent">
                                    <td class="details-control"><span class="glyphicon glyphicon-plus-sign"></span></td>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${outstandingChargesPaymentDTO.balanceAmountHashMap.get(member.memberID)}</td>
                                    <td>${outstandingChargesPaymentDTO.paymentAmountHashMap.get(member.memberID)} / ${outstandingChargesPaymentDTO.chargesAmountHashMap.get(member.memberID)}</td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
                                    <td><a href="<spring:url value="/admin/resources/excel/generate/memberInvoice/${member.memberID}?cycleStartDate=${cycleStartDate.toString()}&cycleEndDate=${cycleEndDate.toString()}"/>"><span class="glyphicon glyphicon-info-sign"></span></a></td>
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
                            <c:forEach items="${outstandingChargesPaymentDTO.paidBalanceMemberList}" var="member">
                                <tr>
                                    <td class="details-control" border="1"><span class="glyphicon glyphicon-plus-sign"></span></td>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${outstandingChargesPaymentDTO.balanceAmountHashMap.get(member.memberID)}</td>
                                    <td>${outstandingChargesPaymentDTO.paymentAmountHashMap.get(member.memberID)} / ${outstandingChargesPaymentDTO.chargesAmountHashMap.get(member.memberID)}</td>
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
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>
