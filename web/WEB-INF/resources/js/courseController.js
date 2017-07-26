/**
 * Created by davidkim on 2/2/17.
 */

var courseControllerApp = angular.module("courseControllerApp", []);

courseControllerApp.controller("courseController", function($scope, $http){

    $scope.refreshCourse = function(){
        $http.get('/admin/courseList/rest/').success(function (data){
            $scope.cart = data;
        });
    };

    $scope.addMemberToCourse = function(memberID){

    }
});
