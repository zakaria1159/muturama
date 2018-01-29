(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('Utilisateur', Utilisateur);

    Utilisateur.$inject = ['$resource', 'DateUtils'];

    function Utilisateur ($resource, DateUtils) {
        var resourceUrl =  'api/utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datedenaissance = DateUtils.convertLocalDateFromServer(data.datedenaissance);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datedenaissance = DateUtils.convertLocalDateToServer(copy.datedenaissance);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datedenaissance = DateUtils.convertLocalDateToServer(copy.datedenaissance);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
