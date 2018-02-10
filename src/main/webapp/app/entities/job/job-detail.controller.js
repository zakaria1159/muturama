(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Job', 'Jobcategorie', 'Utilisateur'];

    function JobDetailController($scope, $rootScope, $stateParams, previousState, entity, Job, Jobcategorie, Utilisateur) {
        var vm = this;

        vm.job = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('muturamaApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
