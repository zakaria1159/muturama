(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('SidebarController', SidebarController);
    
    SidebarController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Agent', 'Ville', 'Job', 'Jobcategorie'];

    function SidebarController ($scope, Principal, LoginService, $state, Agent, Ville, Job, Jobcategorie) {
    	  var vm = this;

    	  
    	    vm.jobs = Job.query();
    	    vm.account = null;
            vm.isAuthenticated = null;
            vm.login = LoginService.open;
            vm.register = register;
            $scope.$on('authenticationSuccess', function() {
                getAccount();
            });

            getAccount();

            function getAccount() {
                Principal.identity().then(function(account) {
                    vm.account = account;
                    vm.isAuthenticated = Principal.isAuthenticated;
                });
            }
            function register () {
                $state.go('register');
            }
    	    
       

    }
})();
