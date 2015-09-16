'use strict';

angular.module('ucllApp')
    .controller('TodoController', function ($scope, Todo) {
        $scope.todos = [];
        $scope.loadAll = function() {
            Todo.query(function(result) {
               $scope.todos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Todo.get({id: id}, function(result) {
                $scope.todo = result;
                $('#deleteTodoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Todo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTodoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.todo = {title: null, description: null, status: null, id: null};
        };
    });
