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
    $('a[data-toggle="tabpanel"]').on( 'shown.bs.tab', function (e) {
        $.fn.dataTable.tables(true).columns.adjust();
    } );

    var coursesTable = $('#coursesTable').DataTable({
        "lengthMenu": [[50,-1], [50, "All"]]
    });
</script>

<html>
    <body>
        <div class="container-fluid">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <h1 class="page-header">Finance Summary: ${member.memberFirstName} ${member.memberLastName}</h1>

                <br>

                <div class="form-group">
                    <label for="selectedDate">Month</label>
                    <form:select path="selectedDate" id="selectedDate" class="form-Control">
                        <form:options items="${monthHashMap}" selected="${monthHashMap.get(selectedDateIndex)}"/>
                    </form:select>
                </div>

                <form:form action="${pageContext.request.contextPath}/admin/student/studentFinances/" method="PUT" modelAttribute="chargeLine">
                    <div class="form-group">
                        <label for="chargeDescription">Charge Description:</label>
                        <form:input path="description" id="chargeDescription" class="form-Control"/>
                    </div>

                    <div class="form-group">
                        <label for="amount">Amount: </label><form:errors path="totalCharge" cssStyle="color: #FF0000"/>
                        <form:input path="totalCharge" id="amount" class="form-Control"/>
                    </div>

                    <input type="submit" value="Add" class="btn btn=default">
                </form:form>

                <form:form action="${pageContext.request.contextPath}/admin/student/studentFinances/" method="PUT" modelAttribute="chargeLineList">
                <table id="chargesTable" class="table table-striped">
                    <thead>
                    <tr>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${chargeLineList}" var="chargeLine">
                        <tr>
                            <td></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <input type="submit" value="Update Charges" class="btn btn=default">
                </form:form>

            </div>

        </div>


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>

    </body>
</html>