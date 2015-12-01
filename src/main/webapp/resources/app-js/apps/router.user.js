define([
	'backbone',
	'underscore',
	'libs/status_code',
	'libs/utility',
	'toastr'
], function(
	Backbone,
	_,
	Status_Code,
	Utility,
	toastr
) {
	var logger = Utility.create_logger({
			scope: 'Router_User',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.user = options.user;
			this.pagination_user = options.pagination_user;
		},
		routes: {
			// list
			'': 'navigateToDefaultPage',
			'level/:level/message/:message': 'showMessageFromPreviousRequest',
			'page/:page': 'fetchUsersByPage',
			'column/:column/value/:value(/page/:page)': 'fetchUsersByPageAndColumnValue'
		},
		showMessageFromPreviousRequest: function(level, message) {
			toastr[level](message);

			// 메세지 출력 후, 초기 요청시 자동으로 #page/1로 redirect
			this.navigate('page/1', {trigger: true});
		},
		navigateToDefaultPage: function() {
			logger.info('navigateToDefaultPage', 'go to page #1.');

			// 초기 요청시 자동으로 #page/1로 redirect
			this.navigate('page/1', {trigger: true});
		},
		fetchUsersByPage: function(page) {
			if(/^[0-9]+$/.test(page) === false) {
				this.navigate('page/1', {trigger: true});
				return false;
			}

			this.pagination_user.fetch({
				requestType: 'get_tot_count'
			});
			this.user.fetch({
				reset: true,
				data: {
					page: page,
					limit: USER.DEFAULT_PAGE_ROWS
				},
				statusCode: Status_Code
			});
		},
		fetchUsersByPageAndColumnValue: function(column, value, page) {
			if(page && /^[0-9]+$/.test(page) === false && _.indexOf(['all', 'name', 'id', 'department'], column) === -1) {
				this.navigate('page/1', {trigger: true});
				return false;
			}

			this.pagination_user.fetch({
				requestType: 'get_tot_count',
				data: (function(column, value) {
					var return_value = {};
					return_value[column] = value;
					return return_value;
				})(column, value)
			});
			this.user.trigger('searchoptionchanged', column, value);
			this.user.fetch({
				reset: true,
				data: _.extend({
					page: page || 1,
					limit: USER.DEFAULT_PAGE_ROWS
				}, (function(column, value) {
					var return_value = {};
					return_value[column] = value;
					return return_value;
				})(column, value)),
				statusCode: Status_Code
			});
		}
	});
});
