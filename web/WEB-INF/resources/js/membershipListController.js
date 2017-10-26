/**
 * Created by davidkim on 10/25/17.
 */

var membershipListControllerApp = angular.module ("membershipListControllerApp",[]).config(function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = '_csrf';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-Token';
});

membershipListControllerApp.controller("membershipListControl", function($scope, $http){

    $scope.refreshMemberList = function(){
        $http.get('/admin/studentList/rest/getMembershipRates').success(function (data){
            $scope.membershipRateHashMap = data;
        });
    };

    $scope.updateMembershipRate = function(memberID, membershipRate){
        $http.put('/admin/studentList/rest/updateMembershipRate/' + memberID + '?rate=' + membershipRate).success(function (){
            $scope.refreshChargesList();
        });
    };
});