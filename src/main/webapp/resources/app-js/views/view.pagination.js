define([
	'backbone',
	'underscore',
	'libs/utility',
	'simple_pagination'
], function(
	Backbone,
	_,
	Utility
) {
	var logger = Utility.create_logger({
			scope: 'View_Pagination',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.View.extend({
		el: 'ul.pagination',
		initialize: function(options) {
			this.rows_in_page = options.rows_in_page || USER.DEFAULT_PAGE_ROWS;
			this.pages_in_pagination = options.pages_in_pagination || 5;

			logger.debug('initialize', 'rows_in_page: ' + this.rows_in_page + ', pages_in_pagination: ' + this.pages_in_pagination);

			this.render();
		},
		events: {

		},
		render: function() {
			var current_page = Backbone.history.getParam('page', 1);

			var column = Backbone.history.getParam('column', null),
				value = Backbone.history.getParam('value', null);

			$(this.el).pagination({
				items: this.model.get('count') * 1,
				itemsOnPage: this.rows_in_page,
				currentPage: current_page,
				hrefTextPrefix: '#' + (function(column, value) {
					return column && value ? 'column/' + column + '/value/' + value + '/' : '';
				})(column, value) + 'page/',
				prevText: '‹',
				nextText: '›'
			});

			logger.debug('render', 'items: ' + this.model.get('count') * 1 + ', currentPage: ' + current_page);

			return this;
		}
	});
});
