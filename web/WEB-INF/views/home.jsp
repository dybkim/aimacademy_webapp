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

    });
</script>

<html>
    <body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

                <h1 class="page-header">Home</h1>

                <br>
                <br>

                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation"><a href="#tab-members" aria-controls="tab-members" role="tab" data-toggle="tab">Active Members</a></li>
                    <li role="presentation"><a href="#tab-inactiveMembers" aria-controls="tab-inactiveMembers" role="tab" data-toggle="tab">Inactive Members</a></li>
                </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="tab-members">
                        <table id="memberTable" class="table table-striped">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Member Name</th>
                                <th>Outstanding Balance</th>
                                <th>Finance Info</th>
                                <th>Invoice</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${memberList}" var="member">
                                <tr>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${outstandingChargesHashMap.get(member.memberID)}</td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.monthValue}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
                                    <td><a href=""><span class="glyphicon glyphicon-info-sign"></span></a></td>
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
                                <th>Finance Info</th>
                                <th>Invoice</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${inactiveMemberList}" var="member">
                                <tr>
                                    <td>${member.memberID}</td>
                                    <td><a href="<spring:url value="/admin/student/studentList/${member.memberID}"/>">${member.memberFirstName} ${member.memberLastName}</a></td>
                                    <td>${inactiveOutstandingChargesHashMap.get(member.memberID)}</td>
                                    <td><a href="<spring:url value="/admin/student/studentFinances/${member.memberID}?month=${cycleStartDate.month}&year=${cycleStartDate.year}"/>"><span class="glyphicon glyphicon-usd"></span></a></td>
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
