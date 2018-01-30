(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .factory('JobcategorieSearch', JobcategorieSearch);

    JobcategorieSearch.$inject = ['$resource'];

    function JobcategorieSearch($resource) {
        var resourceUrl =  'api/_search/jobcategories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
