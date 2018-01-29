(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('VilleDialogController', VilleDialogController);

    VilleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ville'];

    function VilleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ville) {
        var vm = this;

        vm.ville = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ville.id !== null) {
                Ville.update(vm.ville, onSaveSuccess, onSaveError);
            } else {
                Ville.save(vm.ville, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:villeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
