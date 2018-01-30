(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('SidebarController', SidebarController);
    
    SidebarController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Agent', 'Ville', 'Job', 'Jobcategorie'];

    function SidebarController ($scope, Principal, LoginService, $state, Agent, Ville, Job, Jobcategorie) {
    	 var vm = this;
    	
    	vm.isAuthenticated = Principal.isAuthenticated;
    	
  

    }
})();