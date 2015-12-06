require([
	'libs/utility',
	'backbone',

	'models/collection.contents',

	'views/view.schedule_management_eepg_table.main',

	'apps/router.schedule_management_eepg_table'
], function(
	Utility,
	Backbone,

	Collection_Contents,

	View_Schedule_Management_EEPG_Table_Main,

	Router_Schedule_Management_EEPG_Table
) {
	$(document).ready(function() {
		var content = new Collection_Contents();
		var schedule_management_eepg_table = new View_Schedule_Management_EEPG_Table_Main({
				collection: content
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Schedule_Management_EEPG_Table({
				content: content
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
