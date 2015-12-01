define([
	'backbone',
	'underscore',
	'libs/status_code',
	'libs/utility',
	'asynclib'
], function(
	Backbone,
	_,
	Status_Code,
	Utility,
	Async
) {
	var logger = Utility.create_logger({
			scope: 'Router_User_Form',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.operator = options.operator;
			this.user = options.user;
		},
		routes: {
			'mode/create': 'triggerUserFetchedWithCreateMode',
			'mode/:mode/id/:id': 'fetchUserByModeAndID'
		},
		triggerUserFetchedWithCreateMode: function() {
			this.operator.fetch({
				reset: true,
				success: function() {
					this.user.trigger('userfetched', 'create');
				}.bind(this),
				statusCode: Status_Code
			});
		},
		fetchUserByModeAndID: function(mode, id) {
			Async.parallel([
				function(callback) {
					this.operator.fetch({
						reset: true,
						success: function() {
							callback(null, 'operatorfetched');
						},
						statusCode: Status_Code
					});
				}.bind(this),
				function(callback) {
					this.user.fetch({
						data: {
							id: id
						},
						success: function() {
							callback(null, 'userfetched');
						},
						statusCode: Status_Code
					});
				}.bind(this)
			], function(error) {
				this.user.trigger('userfetched', mode);
			}.bind(this));
		}
	});
});
