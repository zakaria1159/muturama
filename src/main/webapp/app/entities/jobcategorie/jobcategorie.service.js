(function() {
    'use strict';
    angular
        .module('muturamaApp')
        .factory('Jobcategorie', Jobcategorie);

    Jobcategorie.$inject = ['$resource'];

    function Jobcategorie ($resource) {
        var resourceUrl =  'api/jobcategories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
