(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .controller('FooterController', FooterController);



    HeaderController.$inject = ['Job'];

    function FooterController (Job) {
    	var vm =this;
    	
    	 vm.jobs = Job.query();


    }
})();