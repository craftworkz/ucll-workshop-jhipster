 'use strict';

angular.module('ucllApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-ucllApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-ucllApp-params')});
                }
                return response;
            },
        };
    });