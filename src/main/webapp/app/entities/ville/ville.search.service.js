(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('VilleSearch', VilleSearch);

    VilleSearch.$inject = ['$resource'];

    function VilleSearch($resource) {
        var resourceUrl =  'api/_search/villes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
