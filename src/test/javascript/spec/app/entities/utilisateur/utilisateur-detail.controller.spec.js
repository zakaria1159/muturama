'use strict';

describe('Controller Tests', function() {

    describe('Utilisateur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUtilisateur, MockUser, MockMessage, MockJob;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            MockUser = jasmine.createSpy('MockUser');
            MockMessage = jasmine.createSpy('MockMessage');
            MockJob = jasmine.createSpy('MockJob');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Utilisateur': MockUtilisateur,
                'User': MockUser,
                'Message': MockMessage,
                'Job': MockJob
            };
            createController = function() {
                $injector.get('$controller')("UtilisateurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'muturamaApp:utilisateurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
