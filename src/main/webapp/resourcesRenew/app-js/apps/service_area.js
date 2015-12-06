require([
	'jquery',
	'libs/utility',
	'backbone',

	'models/collection.service_area',

	'views/view.service_area.main',

	'apps/router.service_area',

	'async!https://maps.googleapis.com/maps/api/js?key=' + GOOGLE_MAP.API_KEY
], function(
	$,
	Utility,
	Backbone,

	Collection_Service_Area,

	View_Service_Area_Main,

	Router_Service_Area
) {
	$(document).ready(function() {
		Utility.Context.set('map', new google.maps.Map($('div#map')[0], {
			center: GOOGLE_MAP.MAP_CENTER,
			zoom: GOOGLE_MAP.ZOOM_LEVEL
		}));

		var service_area = new Collection_Service_Area();
		var service_area_main = new View_Service_Area_Main({
				collection: service_area
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Service_Area({
				service_area: service_area
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
