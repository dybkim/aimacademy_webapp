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
<%@ page contentType="text/html;charset=UTF-8"%>

<%@include file="../template/navbar.jsp"%>
<%@include file="../template/sidebar.jsp" %>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link href="<spring:url value="/resources/css/studentFinancesInfoElements.css"/>" rel="stylesheet">

<script>
    $(document).ready(function(){
        $('#dateCharged').datepicker({
            dateFormat: "mm/dd/yy",
            maxDate: '0',
            minDate: new Date(2016, 1, 1)
        });

        $('#dateCharged').keydown(function (e) {
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
    });
</script>

<html>
    <body>
        <div class="container-fluid" ng-app="studentFinancesControllerApp">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div ng-controller="studentFinancesControl" ng-init="initChargesDTO('${member.memberID}','${selectedDate.monthValue}','${selectedDate.year}')">
                    <h1 class="page-header">Finance Summary: ${member.memberFirstName} ${member.memberLastName}</h1>

                    <h3>{{memberChargesFinancesDTO.cycleStartDate.month}} {{memberChargesFinancesDTO.cycleStartDate.year}}</h3>

                    <div class="form-group">
                        <label for="selectDate">Month</label>
                        <select id="selectDate" data-ng-model="monthSelected"
                                data-ng-options="localDate as (localDate.month + ' ' + localDate.year) for localDate in monthsList"
                                ng-change="initChargesDTO('${member.memberID}', monthSelected.monthValue, monthSelected.year)">
                        </select>
                    </div>

                    <div class="container-fluid" style="border: thin solid black; float:left; padding-bottom: 20px; margin-bottom: 50px;">
                        <h4>Add Other Charge</h4>

                        <form>
                            <label for="chargeDescription">Charge Description:</label>
                            <input id="chargeDescription" class="form-control" ng-model="chargeDescription"/>

                            <label for="chargeAmount">Amount:</label>
                            <input id="chargeAmount" class="form-control" ng-model="chargeAmount"/>

                            <label for="chargeDiscount">Discount:</label>
                            <input id="chargeDiscount" class=rrayLi"form-control" ng-model="chargeDiscount"/>

                            <label for="dateCharged">Date:</label>
                            <input id="dateCharged" class="form-control" ng-model="dateCharged"/>

                            <input type="submit" value="Add Charge" class="btn btn=default" ng-click="addMiscCharge('${member.memberID}', chargeDescription, chargeAmount, chargeDiscount, dateCharged)"/>

                            <br>
                        </form>
                    </div>

                    <div class="container-fluid" >
                        <table id="chargesTable" class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Charge Description</th>
                                    <th>Hours Billed</th>
                                    <th>Balance</th>
                                    <th>Discount</th>
                                    <th>Total</th>
                                    <th>Drop Charge</th>
                                </tr>
                            </thead>

                            <tbody>
                                <tr ng-repeat = "charge in memberChargesFinancesDTO.chargeHashMap">
                                    <td>{{charge.description}}</td>
                                    <td>{{memberChargesFinancesDTO.billableUnitsBilledHashMap[charge.chargeID]}} {{charge.billableUnitType}}(s)</td>
                                    <td>{{charge.chargeAmount}}</td>
                                    <td><form ng-submit="addDiscountToCharge(charge.chargeID, charge.discountAmount)"><input ng-model="charge.discountAmount" style="text-align:center; width:50px;"/></form></td>
                                    <td>{{charge.chargeAmount - charge.discountAmount}}</td>
                                    <td><a class="label label-danger" ng-click="dropMiscCharge(charge)"><span class="glyphicon glyphicon-remove"></span></a></td>
                                </tr>

                                <tr>
                                    <td><b>Total</b></td>
                                    <td><b>{{memberChargesFinancesDTO.hoursBilledTotal}} hour(s) / {{memberChargesFinancesDTO.sessionsBilledTotal}} session(s)</b></td>
                                    <td><b>{{memberChargesFinancesDTO.totalChargesAmount}}</b></td>
                                    <td><b>{{memberChargesFinancesDTO.totalDiscountAmount}}</b></td>
                                    <td><b>{{memberChargesFinancesDTO.totalChargesAmount - memberChargesFinancesDTO.totalDiscountAmount}}</b></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/studentFinancesController.js"/>"></script>
