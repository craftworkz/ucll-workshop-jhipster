'use strict';

angular.module('ucllApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


