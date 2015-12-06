require([
	'jquery',
	'libs/utility',

	'models/collection.operator',
	'models/model.operator',
	'models/collection.bmsc',

	'views/view.service_area_management.main',

	'apps/router.service_area_management'
], function(
	$,
	Utility,

	Collection_Operator,
	Model_Operator,
	Collection_BMSC,

	View_Service_Area_Management_Main,

	Router_Service_Area_Management
) {
	$(document).ready(function() {
		var operator = new Collection_Operator();
		var bmsc = new Collection_Operator();
		var service_area = new Collection_BMSC();
		var main_main = new View_Service_Area_Management_Main({
				collection: operator,
				bmsc: bmsc,
				service_area: service_area
			});

		operator.fetch({reset: true});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Service_Area_Management({
				operator: operator,
				bmsc: bmsc,
				service_area: service_area
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
