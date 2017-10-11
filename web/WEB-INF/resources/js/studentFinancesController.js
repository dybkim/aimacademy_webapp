/**
 * Created by davidkim on 8/12/17.
 */

var studentFinancesControllerApp = angular.module ("studentFinancesControllerApp",[]).config(function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = '_csrf';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-Token';
});

studentFinancesControllerApp.controller("studentFinancesControl", function($scope, $http, $location){

    $scope.refreshChargesList = function(){
        $http.get('/admin/student/rest/studentFinances/' + $scope.memberID + '?month=' + $scope.month + '&year=' + $scope.year).success(function (data){
            $scope.memberChargesFinancesWrapper = data;
            $scope.monthsList = $scope.memberChargesFinancesWrapper.monthsList;
            $scope.monthSelected = $scope.memberChargesFinancesWrapper.monthSelectedIndex;
            $scope.selectedOptions = $scope.monthsList[$scope.monthSelected];
            $scope.chargeDescription = "";
            $scope.chargeAmount = 0;
            $scope.chargeDiscount = 0;
        });
    };

    $scope.initChargesWrapper = function(memberID, month, year){
        $scope.memberID = memberID;
        $scope.month = month;
        $scope.year = year;
        $scope.refreshChargesList();
    };

    $scope.addMiscCharge = function(memberID, chargeDescription, chargeAmount, chargeDiscount){
        $http({
            url:'/admin/student/rest/studentFinances/' + $scope.memberID + '/addMiscCharge',
            method: "PUT",
            params:{chargeDescription: chargeDescription,
                    chargeAmount: chargeAmount,
                    chargeDiscount: chargeDiscount,
                    month: $scope.month,
                    year: $scope.year}

        }).success(function (){
            $scope.refreshChargesList();
        }).error(function(data){
            alert('ERROR:  ' +  data.error);
        });

    };

    $scope.addDiscountToCharge = function(chargeID, discountAmount){
        $http.put('/admin/student/rest/studentFinances/discountCharge/' + chargeID + '?discount=' + discountAmount).success(function (){
            $scope.refreshChargesList();
        });
    };

    $scope.dropMiscCharge = function(charge){
        if(charge.courseID === 1){
            $http.put('/admin/student/rest/studentFinances/dropMiscCharge/' + charge.chargeID).then(function(){
                $scope.refreshChargesList();
            })
        }
        else{
            alert('To modify course charges, please change modify the attendance on the course page');
        }
    };

    $scope.addPayment = function(memberID, paymentAmount){

    };
});