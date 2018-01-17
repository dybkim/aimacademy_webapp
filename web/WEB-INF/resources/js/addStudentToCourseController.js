/**
 * Created by davidkim on 8/20/17.
 */

var addStudentToCourseControllerApp = angular.module("addStudentToCourseControllerApp",['ui.bootstrap']);

addStudentToCourseControllerApp.controller("addStudentToCourseController", function($scope, $http, $window){

    $scope.selectedMember = null;
    $scope.memberList = [];
    $scope.registrationList = [];

    $scope.init = function(courseID){
        $scope.courseID = courseID;
        $http.get("/admin/studentList/rest/getList").then(function(memberList){
            for(var i = 0; i < memberList.data.length; i++){
                var member = JSON.parse(memberList.data)[i];
                $scope.memberList.push(new Member(member.memberID, member.memberFirstName, member.memberLastName));
            }
        });
    };

    $scope.addToRegistrationList = function(){
        var hasMember = false;
        for(i = 0; i < $scope.registrationList.length; i++){
            if($scope.registrationList[i].id === $scope.selectedMember.id){
                hasMember = true;
                break;
            }
        }

        if(!hasMember)
            $scope.registrationList.push($scope.selectedMember);

        $scope.selectedMember = null;
    };

    $scope.removeStudentFromCourseRegistration = function(member){
        var id = member.id;
        $scope.registrationList = $scope.registrationList.filter(function(member){
            return member.id !== id;
        })
    };

    $scope.submitRegistrationList = function(){
        if($scope.registrationList.length === 0){
            alert("No members selected!");
            return;
        }

        var idList = [];
        for(var i = 0; i < $scope.registrationList.length; i++){
            idList.push($scope.registrationList[i].id);
        }

        idList = angular.toJson(idList);
        $http.post("/admin/courseList/rest/" + $scope.courseID + "/submitRegistrationList", idList).then(function(){
            $window.location.href = "/admin/courseList/courseInfo/" + $scope.courseID;
        });
    };

    var Member = function(memberID, memberFirstName, memberLastName){
        this.id = memberID;
        this.name = memberFirstName + ' ' + memberLastName;
    }
});