require([
	'jquery',
	'libs/utility',

	'models/collection.operator',
	'models/model.operator',
	'models/collection.bmsc',

	'views/view.main.main',

	'apps/router.main',

	'async!https://maps.googleapis.com/maps/api/js?key=' + GOOGLE_MAP.API_KEY
], function(
	$,
	Utility,

	Collection_Operator,
	Model_Operator,
	Collection_BMSC,

	View_Main_Main,

	Router_Main
) {
	$(document).ready(function() {
		Utility.Context.set('map', new google.maps.Map($('div#map')[0], {
			center: GOOGLE_MAP.MAP_CENTER,
			zoom: GOOGLE_MAP.ZOOM_LEVEL
		}));

		var operator = new Collection_Operator();
		var bmsc = new Collection_Operator();
		var service_area = new Collection_BMSC();
		var main_main = new View_Main_Main({
				collection: operator,
				bmsc: bmsc,
				service_area: service_area
			});

		operator.fetch({reset: true});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Main({
				operator: operator,
				bmsc: bmsc,
				service_area: service_area
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
