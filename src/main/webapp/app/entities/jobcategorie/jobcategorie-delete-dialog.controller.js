(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('JobcategorieDeleteController',JobcategorieDeleteController);

    JobcategorieDeleteController.$inject = ['$uibModalInstance', 'entity', 'Jobcategorie'];

    function JobcategorieDeleteController($uibModalInstance, entity, Jobcategorie) {
        var vm = this;

        vm.jobcategorie = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Jobcategorie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
