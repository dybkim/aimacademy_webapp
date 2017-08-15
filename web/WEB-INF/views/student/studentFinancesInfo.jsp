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

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script>

//    studentFinancesControllerApp.directive('chargeTable', function(){
//        return function(scope, element, attrs){
//
//            // apply DataTable options, use defaults if none specified by user
//            var options = {};
//            if (attrs.chargeTable.length > 0) {
//                options = scope.$eval(attrs.chargeTable);
//            } else {
//                options = {
//                    "bStateSave": true,
//                    "iCookieDuration": 2419200, /* 1 month */
//                    "bJQueryUI": true,
//                    "bPaginate": false,
//                    "bLengthChange": false,
//                    "bFilter": false,
//                    "bInfo": false,
//                    "bDestroy": true
//                };
//            }
//
//            // Tell the dataTables plugin what columns to use
//            // We can either derive them from the dom, or use setup from the controller
//            var explicitColumns = [];
//            element.find('th').each(function(index, elem) {
//                explicitColumns.push($(elem).text());
//            });
//            if (explicitColumns.length > 0) {
//                options["aoColumns"] = explicitColumns;
//            } else if (attrs.aoColumns) {
//                options["aoColumns"] = scope.$eval(attrs.aoColumns);
//            }
//
//            // aoColumnDefs is dataTables way of providing fine control over column config
//            if (attrs.aoColumnDefs) {
//                options["aoColumnDefs"] = scope.$eval(attrs.aoColumnDefs);
//            }
//
//            if (attrs.fnRowCallback) {
//                options["fnRowCallback"] = scope.$eval(attrs.fnRowCallback);
//            }
//
//            // apply the plugin
//            var dataTable = element.dataTable(options);
//
//            // watch for any changes to our data, rebuild the DataTable
//            scope.$watch(attrs.aaData, function(value) {
//                var val = value || null;
//                if (val) {
//                    dataTable.ajax.reload();
//                }
//            });
//        };
//    });



//        $scope.myCallback = function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
//            $('td:eq(2)', nRow).bind('click', function () {
//                $scope.$apply(function () {
//                    $scope.someClickHandler(aData);
//                });
//            });
//            return nRow;
//        };
//
//        $scope.someClickHandler = function (info) {
//            $scope.message = 'clicked: ' + info.price;
//        };
//
//        $scope.columnDefs = [
//            {"mDataProp": "category", "aTargets": [0]},
//            {"mDataProp": "name", "aTargets": [1]},
//            {"mDataProp": "price", "aTargets": [2]}
//        ];
//
//        $scope.overrideOptions = {
//            "bStateSave": true,
//            "iCookieDuration": 2419200, /* 1 month */
//            "bJQueryUI": true,
//            "bPaginate": true,
//            "bLengthChange": false,
//            "bFilter": true,
//            "bInfo": true,
//            "bDestroy": true
//        };


</script>

<html>
    <body>
        <div class="container-fluid" ng-app="studentFinancesControllerApp">
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div ng-controller="studentFinancesControl" ng-init="initChargesWrapper('${member.memberID}','${selectedDate.monthValue}','${selectedDate.year}')">
                    <h1 class="page-header">Finance Summary: ${member.memberFirstName} ${member.memberLastName}</h1>

                    <h3>{{memberChargesFinancesWrapper.cycleStartDate.month}} {{memberChargesFinancesWrapper.cycleStartDate.year}}</h3>

                    <div class="form-group">
                        <label for="selectDate">Month</label>
                        <select id="selectDate" data-ng-model="monthSelected"
                                data-ng-options="monthSelected as [monthSelected.month, monthSelected.year].join(' ') for monthSelected in monthsList"
                                ng-change="initChargesWrapper('${member.memberID}',monthSelected.monthValue,monthSelected.year)">
                        </select>
                    </div>

                    <div class="container-fluid" style="border: thin solid black">
                        <h4>Add Other Charge</h4>

                        <form>
                            <label for="chargeDescription">Charge Description:</label>
                            <input id="chargeDescription" class="form-control" ng-model="chargeDescription"/>

                            <label for="chargeAmount">Amount:</label>
                            <input id="chargeAmount" class="form-control" ng-model="chargeAmount"/>

                            <label for="chargeDiscount">Discount:</label>
                            <input id="chargeDiscount" class="form-control" ng-model="chargeDiscount"/>

                            <input type="submit" value="Add Charge" class="btn btn=default" ng-click="addMiscCharge('${member.memberID}', chargeDescription, chargeAmount, chargeDiscount)"/>
                        </form>
                    </div>

                    <div class="container-fluid" >
                        <table id="chargesTable" class="table table-striped">
                            <thead>
                            <tr>
                                <th>Description</th>
                                <th>Hours Billed</th>
                                <th>Price</th>
                                <th>Discount</th>
                                <th>Total</th>
                                <th>Drop Charge</th>
                            </tr>
                            </thead>

                            <tbody>
                                <tr ng-repeat = "charge in memberChargesFinancesWrapper.chargeHashMap">
                                    <td>{{charge.description}}</td>
                                    <td>{{memberChargesFinancesWrapper.hoursBilledHashMap[charge.chargeID]}} hours</td>
                                    <td>{{charge.chargeAmount}}</td>
                                    <td ng-change="addDiscountToCharge(charge,discountAmount)" ng-model="discountAmount">{{charge.discountAmount}}</td>
                                    <td>{{charge.chargeAmount - charge.discountAmount}}</td>
                                    <td><a href="" class="label label-danger" ng-click="dropMiscCharge(charge)"><span class="glyphicon glyphicon-remove"></span></a></td>
                                </tr>

                                <tr>
                                    <td><b>Total</b></td>
                                    <td><b>{{memberChargesFinancesWrapper.hoursBilledTotal}} hours</b></td>
                                    <td><b>{{memberChargesFinancesWrapper.totalChargesAmount}}</b></td>
                                    <td><b>{{memberChargesFinancesWrapper.totalDiscountAmount}}</b></td>
                                    <td><b>{{memberChargesFinancesWrapper.totalChargesAmount - memberChargesFinancesWrapper.totalDiscountAmount}}</b></td>
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
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/studentFinancesController.js"/>"></script>
