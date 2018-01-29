(function () {
    'use strict';

    angular
        .module('muturamaApp')
        .config(stateConfig)
        .directive("appMap", function () {
            return {
                restrict: "E",
                replace: true,
                template: "<div></div>",
                scope: {
                    center: "=",        // Center point on the map (e.g. <code>{ latitude: 10, longitude: 10 }</code>).
                    markers: "=",       // Array of map markers (e.g. <code>[{ lat: 10, lon: 10, name: "hello" }]</code>).
                    width: "@",         // Map width in pixels.
                    height: "@",        // Map height in pixels.
                    zoom: "@",          // Zoom level (one is totally zoomed out, 25 is very much zoomed in).
                    mapTypeId: "@",     // Type of tile to show on the map (roadmap, satellite, hybrid, terrain).
                    panControl: "@",    // Whether to show a pan control on the map.
                    zoomControl: "@",   // Whether to show a zoom control on the map.
                    scaleControl: "@"   // Whether to show scale control on the map.
                },
                link: function (scope, element, attrs) {
                    var toResize, toCenter;
                    var map;
                    var currentMarkers;

                    // listen to changes in scope variables and update the control
                    var arr = ["width", "height", "markers", "mapTypeId", "panControl", "zoomControl", "scaleControl"];
                    for (var i = 0, cnt = arr.length; i < arr.length; i++) {
                        scope.$watch(arr[i], function () {
                            cnt--;
                            if (cnt <= 0) {
                                updateControl();
                            }
                        });
                    }

                    // update zoom and center without re-creating the map
                    scope.$watch("zoom", function () {
                        if (map && scope.zoom)
                            map.setZoom(scope.zoom * 2);
                    });
                    scope.$watch("center", function () {
                        if (map && scope.center)
                            map.setCenter(getLocation(scope.center))
                            ;
                    });

                    // update the control
                    function updateControl() {

                        // update size
                        if (scope.width) element.width(scope.width);
                        if (scope.height) element.height(scope.height);

                        // get map options
                        var options =
                            {
                                center: new google.maps.LatLng(33, -6),
                                zoom: 10,
                                mapTypeId: "roadmap"
                            };
                        if (scope.center) options.center = getLocation(scope.center);
                        if (scope.zoom) options.zoom = scope.zoom * 1;
                        if (scope.mapTypeId) options.mapTypeId = scope.mapTypeId;
                        if (scope.panControl) options.panControl = scope.panControl;
                        if (scope.zoomControl) options.zoomControl = scope.zoomControl;
                        if (scope.scaleControl) options.scaleControl = scope.scaleControl;

                        // create the map
                        map = new google.maps.Map(element[0], options);

                        // update markers
                        updateMarkers();

                        // listen to changes in the center property and update the scope
                        google.mapTypeIds.event.addListener(map, 'center_changed', function () {

                            // do not update while the user pans or zooms
                            if (toCenter) clearTimeout(toCenter);
                            toCenter = setTimeout(function () {
                                if (scope.center) {

                                    // check if the center has really changed
                                    if (map.center.lat() != scope.center.lat ||
                                        map.center.lng() != scope.center.lon) {

                                        // update the scope and apply the change
                                        scope.center = {lat: map.center.lat(), lon: map.center.lng()};
                                        if (!scope.$$phase) scope.$apply("center");
                                    }
                                }
                            }, 500);
                        });
                    }






                    // update map markers to match scope marker collection
                    function updateMarkers() {
                        if (map && scope.markers) {
                            // create new markers


                            currentMarkers = [];
                            var markers = scope.markers;
                            if (angular.isString(markers)) markers = scope.$eval(scope.markers);
                            var infoWindow = new google.maps.InfoWindow();

                            for (var i = 0; i < markers.length; i++) {
                                var m = markers[i];
                                var loc = new google.maps.LatLng(m.lat, m.lon);
                                var mm = new google.maps.Marker({position: loc, map: map, animation: google.maps.Animation.DROP, title: m.nom + " || " + m.address});

                                currentMarkers.push(mm);
                                google.maps.event.addListener(mm,'click',function() {
                                    map.setZoom(13);
                                    map.setCenter(mm.getPosition());
                                });

                                /*google.maps.event.addListener(mm,'click',function() {
                                    var infowindow = new google.maps.InfoWindow({
                                        content:'cin: ' + m.cin+ '<br>nom: ' + m.nom+ '<br>prenom: ' + m.prenom+ '<br>address: ' + m.address
                                    });
                                    infowindow.open(map,mm);
                                });*/

                                (function (mm, m) {
                                    google.maps.event.addListener(mm, "click", function (e) {
                                        //Wrap the content inside an HTML DIV in order to set height and width of InfoWindow.
                                        infoWindow.setContent("<div style = 'width:200px;min-height:40px'>"+ 'cin: ' + m.cin+ '<br>nom: ' + m.nom+ '<br>prenom: ' + m.prenom+ '<br>address: ' + m.address+"</div>");
                                        infoWindow.open(map, mm);
                                    });
                                })(mm, m);



                            }
                        }
                    }





                    // convert current location to Google maps location
                    function getLocation(loc) {
                        if (loc == null) return new google.maps.LatLng(33, -6);
                        if (angular.isString(loc)) loc = scope.$eval(loc);
                        return new google.maps.LatLng(loc.lat, loc.lon);

                    }
                }
            };
        });

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'footer@': {
                    templateUrl: 'app/layouts/footer/footer.html',
                    controller: 'FooterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();

                }]
            }
        });
    }
})();