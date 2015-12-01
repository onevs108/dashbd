require([
	'libs/utility',
	'backbone',

	'models/collection.users',
	'models/collection.operator',
	'models/model.pagination.user',

	'views/view.user.main',

	'apps/router.user'
], function(
	Utility,
	Backbone,

	Collection_User,
	Collection_Operator,
	Model_Pagination_User,

	View_User_Main,

	Router_User
) {
	$(document).ready(function() {
		var operator = new Collection_Operator();
		var user = new Collection_User();
		var pagination_user = new Model_Pagination_User();
		var user_main = new View_User_Main({
				collection: user,
				operator: operator,
				pagination_user: pagination_user
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_User({
				user: user,
				pagination_user: pagination_user
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
