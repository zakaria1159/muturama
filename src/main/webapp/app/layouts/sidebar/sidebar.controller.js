(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('SidebarController', SidebarController);
    
    SidebarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'Job'];

    function SidebarController ($state, Auth, Principal, ProfileService, LoginService, Job) {
    	  var vm = this;

    	  
    	    vm.jobs = Job.query();
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
