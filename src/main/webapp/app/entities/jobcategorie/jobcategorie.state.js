(function() {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('jobcategorie', {
            parent: 'entity',
            url: '/jobcategorie',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.jobcategorie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jobcategorie/jobcategories.html',
                    controller: 'JobcategorieController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobcategorie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('jobcategorie-detail', {
            parent: 'jobcategorie',
            url: '/jobcategorie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'muturamaApp.jobcategorie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jobcategorie/jobcategorie-detail.html',
                    controller: 'JobcategorieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobcategorie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Jobcategorie', function($stateParams, Jobcategorie) {
                    return Jobcategorie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'jobcategorie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('jobcategorie-detail.edit', {
            parent: 'jobcategorie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jobcategorie/jobcategorie-dialog.html',
                    controller: 'JobcategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Jobcategorie', function(Jobcategorie) {
                            return Jobcategorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jobcategorie.new', {
            parent: 'jobcategorie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jobcategorie/jobcategorie-dialog.html',
                    controller: 'JobcategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('jobcategorie', null, { reload: 'jobcategorie' });
                }, function() {
                    $state.go('jobcategorie');
                });
            }]
        })
        .state('jobcategorie.edit', {
            parent: 'jobcategorie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jobcategorie/jobcategorie-dialog.html',
                    controller: 'JobcategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Jobcategorie', function(Jobcategorie) {
                            return Jobcategorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jobcategorie', null, { reload: 'jobcategorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jobcategorie.delete', {
            parent: 'jobcategorie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jobcategorie/jobcategorie-delete-dialog.html',
                    controller: 'JobcategorieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Jobcategorie', function(Jobcategorie) {
                            return Jobcategorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jobcategorie', null, { reload: 'jobcategorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
