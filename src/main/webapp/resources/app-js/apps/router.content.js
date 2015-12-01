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
			scope: 'Router_Content',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.content = options.content;
			this.pagination_content = options.pagination_content;
		},
		routes: {
			// list
			'': 'navigateToDefaultPage',
			'level/:level/message/:message': 'showMessageFromPreviousRequest',
			'page/:page': 'fetchContentsByPage',
			'column/:column/value/:value(/page/:page)': 'fetchContentsByPageAndColumnValue'
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
		fetchContentsByPage: function(page) {
			if(/^[0-9]+$/.test(page) === false) {
				this.navigate('page/1', {trigger: true});
				return false;
			}

			this.pagination_content.fetch({
				requestType: 'get_tot_count'
			});
			this.content.fetch({
				reset: true,
				data: {
					page: page,
					limit: USER.DEFAULT_PAGE_ROWS
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByPageAndColumnValue: function(column, value, page) {
			if(page && /^[0-9]+$/.test(page) === false && _.indexOf(['title', 'category', 'actors', 'content_provider'], column) === -1) {
				this.navigate('page/1', {trigger: true});
				return false;
			}

			this.pagination_content.fetch({
				requestType: 'get_tot_count',
				data: (function(column, value) {
					var return_value = {};
					return_value[column] = value;
					return return_value;
				})(column, value)
			});
			this.content.trigger('searchoptionchanged', column, value);
			this.content.fetch({
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
