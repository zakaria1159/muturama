(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('UtilisateurDialogController', UtilisateurDialogController);

    UtilisateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Utilisateur', 'User'];

    function UtilisateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Utilisateur, User) {
        var vm = this;

        vm.utilisateur = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.utilisateur.id !== null) {
                Utilisateur.update(vm.utilisateur, onSaveSuccess, onSaveError);
            } else {
                Utilisateur.save(vm.utilisateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:utilisateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datedenaissance = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
