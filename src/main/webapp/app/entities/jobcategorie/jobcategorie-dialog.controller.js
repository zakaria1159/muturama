(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('JobcategorieDialogController', JobcategorieDialogController);

    JobcategorieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Jobcategorie'];

    function JobcategorieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Jobcategorie) {
        var vm = this;

        vm.jobcategorie = entity;
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
            if (vm.jobcategorie.id !== null) {
                Jobcategorie.update(vm.jobcategorie, onSaveSuccess, onSaveError);
            } else {
                Jobcategorie.save(vm.jobcategorie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('muturamaApp:jobcategorieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
