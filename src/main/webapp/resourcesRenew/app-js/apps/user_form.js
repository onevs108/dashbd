require([
	'libs/utility',
	'backbone',

	'models/collection.operator',
	'models/model.user',

	'views/view.user_form.main',

	'apps/router.user_form'
], function(
	Utility,
	Backbone,

	Collection_Operator,
	Model_User,

	View_User_Form_Main,

	Router_User_Form
) {
	$(document).ready(function() {
		var operator = new Collection_Operator();
		var user = new Model_User();
		var user_form = new View_User_Form_Main({
				operator: operator,
				model: user
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_User_Form({
				operator: operator,
				user: user
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
