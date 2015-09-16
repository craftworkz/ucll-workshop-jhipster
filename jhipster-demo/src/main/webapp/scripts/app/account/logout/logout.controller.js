'use strict';

angular.module('ucllApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
