'use strict';

angular.module('ucllApp')
    .controller('TodoDetailController', function ($scope, $rootScope, $stateParams, entity, Todo, User) {
        $scope.todo = entity;
        $scope.load = function (id) {
            Todo.get({id: id}, function(result) {
                $scope.todo = result;
            });
        };
        $rootScope.$on('ucllApp:todoUpdate', function(event, result) {
            $scope.todo = result;
        });
    });
