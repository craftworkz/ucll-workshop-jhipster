'use strict';

angular.module('ucllApp').controller('TodoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Todo', 'User',
        function($scope, $stateParams, $modalInstance, entity, Todo, User) {

        $scope.todo = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Todo.get({id : id}, function(result) {
                $scope.todo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ucllApp:todoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.todo.id != null) {
                Todo.update($scope.todo, onSaveFinished);
            } else {
                Todo.save($scope.todo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
