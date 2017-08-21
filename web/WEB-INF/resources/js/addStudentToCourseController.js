/**
 * Created by davidkim on 8/20/17.
 */

var addStudentToCourseControllerApp = angular.module("addStudentToCourseControllerApp",[]);

addStudentToCourseControllerApp.directive('onKeyDown',function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (element.val().length >= 3) {
                scope.$apply(function () {
                    scope.$eval(attrs.onKeyDown);
                });
                event.preventDefault();
            }
        });
    };
});

addStudentToCourseControllerApp.controller("addStudentToCourseController", function($scope, $http){

    $scope.refreshCourseRegistrationWrapper = function(courseRegistrationWrapper){
        $scope.course = courseRegistrationWrapper.course;
        $scope.courseRegistrationWrapperObjectList = courseRegistrationWrapper.courseRegistrationWrapperObjectList;
    };

    $scope.addToRegistrationList = function(memberID, courseRegistrationWrapperObjectList){
        var json = JSON.stringify(courseRegistrationWrapperObjectList);
        $http.put("/admin/courseList/rest/" + $scope.course.courseID + "/addStudentToCourse/" + memberID, json).success(function(courseRegistrationWrapper){
            $scope.refreshCourseRegistrationWrapper(courseRegistrationWrapper);
        });
        $scope.selectedMember = null;
    };

    $scope.removeStudentFromCourse = function(memberID, courseRegistrationWrapperObjectList){
        var json = JSON.stringify(courseRegistrationWrapperObjectList);
        $http.delete("/admin/courseList/rest/" + $scope.course.courseID + "/removeStudentFromCourse" + memberID, json).success(function(courseRegistrationWrapper){
            $scope.refreshCourseRegistrationWrapper(courseRegistrationWrapper);
        });
    };

    $scope.submitRegistrationList = function(courseRegistrationWrapper){
        var json = JSON.stringify(courseRegistrationWrapper);
        $http.post("/admin/courseList/rest/" + $scope.course.courseID + "/submit", json).success(function(){
            window.location.replace("/admin/courseList/courseInfo/" + $scope.course.courseID);
        });
    };

    $scope.fetchMember = function(memberString){
        $http.get("/admin/courseList/rest/" + $scope.courseID + "/fetchSearchString?=" + memberString).success(function(memberList){
            $scope.memberList = memberList;
        })
    };

});
