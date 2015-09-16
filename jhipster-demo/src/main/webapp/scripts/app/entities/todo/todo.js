'use strict';

angular.module('ucllApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('todo', {
                parent: 'entity',
                url: '/todos',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ucllApp.todo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/todo/todos.html',
                        controller: 'TodoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('todo');
                        $translatePartialLoader.addPart('todoStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('todo.detail', {
                parent: 'entity',
                url: '/todo/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ucllApp.todo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/todo/todo-detail.html',
                        controller: 'TodoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('todo');
                        $translatePartialLoader.addPart('todoStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Todo', function($stateParams, Todo) {
                        return Todo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('todo.new', {
                parent: 'todo',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/todo/todo-dialog.html',
                        controller: 'TodoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, description: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('todo', null, { reload: true });
                    }, function() {
                        $state.go('todo');
                    })
                }]
            })
            .state('todo.edit', {
                parent: 'todo',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/todo/todo-dialog.html',
                        controller: 'TodoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Todo', function(Todo) {
                                return Todo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('todo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
