(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Utilisateur', 'User'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Utilisateur, User) {
        var vm = this;

        vm.utilisateur = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('muturamaApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
