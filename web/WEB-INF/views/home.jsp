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
    $(document).ready(function() {

        $('a[data-toggle="tabpanel"]').on('shown.bs.tab', function (e) {
            $.fn.dataTable.tables(true).columns.adjust();
        });

        var memberTable = $('#memberTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]]
        });

        var inactiveMemberTable = $('#inactiveMemberTable').DataTable({
            "lengthMenu": [[50,-1], [50, "All"]]
        });

        $('.nav-tabs a[href="#tab-members"]').tab('show');

        $('#selectMonthBox').change(function(){
            var date = $.parseJSON($(this).val());
            var month = date[0];
            var year = date[1];
            window.location.replace('/admin/home?month=' + month + '&year=' + year);
        });

    });



//    var changeMonth = function(date){
//        alert(date);
//        window.location.replace('/admin/home?month=' + date[0] + '&year=' + date[1]);
//    };
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
                        <form:option value="[${date.monthValue},${date.year}]">${date.month} ${date.year}</form:option>
                    </c:forEach>
                </form:select>

                <br>
                <br>

                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Outstanding Balances</a></li>
                    <li role="presentation"><a href="#tab-inactiveMembers" aria-controls="tab-inactiveMembers" role="tab" data-toggle="tab">No Balances</a></li>
                </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                        <table id="memberTable" class="table table-striped">
                            <thead>
                            <tr>
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
                                <tr>
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

                    <div role="tabpanel" class="tab-pane fade in active" id="tab-inactiveMembers">
                        <table id="inactiveMemberTable" class="table table-striped">
                            <thead>
                            <tr>
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
    </body>
</html>



<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

</body>
</html>
