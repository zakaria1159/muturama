(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('JobcategorieDetailController', JobcategorieDetailController);

    JobcategorieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Jobcategorie'];

    function JobcategorieDetailController($scope, $rootScope, $stateParams, previousState, entity, Jobcategorie) {
        var vm = this;

        vm.jobcategorie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:jobcategorieUpdate', function(event, result) {
            vm.jobcategorie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
