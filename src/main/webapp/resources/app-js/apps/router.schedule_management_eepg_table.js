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
			scope: 'Router_Schedule_Management_EEPG_Table',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.content = options.content;
		},
		routes: {
			'category/:category/content_provider/:content_provider/title/:title': 'fetchContentsByCategoryAndContentProviderAndTitle',
			'category/:category/content_provider/:content_provider': 'fetchContentsByCategoryAndContentProvider',
			'category/:category/title/:title': 'fetchContentsByCategoryAndTitle',
			'content_provider/:content_provider/title/:title': 'fetchContentsByContentProviderAndTitle',
			'category/:category': 'fetchContentsByCategory',
			'content_provider/:content_provider': 'fetchContentsByContentProvider',
			'title/:title': 'fetchContentsByTitle'
		},
		fetchContentsByCategoryAndContentProviderAndTitle: function(category, content_provider, title) {
			this.content.trigger('searchoptionchanged', category, content_provider, title);
			this.content.fetch({
				reset: true,
				data: {
					category: category,
					content_provider: content_provider,
					title: title,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByCategoryAndContentProvider: function(category, content_provider) {
			this.content.trigger('searchoptionchanged', category, content_provider, '');
			this.content.fetch({
				reset: true,
				data: {
					category: category,
					content_provider: content_provider,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByCategoryAndTitle: function(category, title) {
			this.content.trigger('searchoptionchanged', category, '', title);
			this.content.fetch({
				reset: true,
				data: {
					category: category,
					title: title,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByContentProviderAndTitle: function(content_provider, title) {
			this.content.trigger('searchoptionchanged', '', content_provider, title);
			this.content.fetch({
				reset: true,
				data: {
					content_provider: content_provider,
					title: title,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByCategory: function(category) {
			this.content.trigger('searchoptionchanged', category, '', '');
			this.content.fetch({
				reset: true,
				data: {
					category: category,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByContentProvider: function(content_provider) {
			this.content.trigger('searchoptionchanged', '', content_provider, '');
			this.content.fetch({
				reset: true,
				data: {
					content_provider: content_provider,
					limit: 0
				},
				statusCode: Status_Code
			});
		},
		fetchContentsByTitle: function(title) {
			this.content.trigger('searchoptionchanged', '', '', title);
			this.content.fetch({
				reset: true,
				data: {
					title: title,
					limit: 0
				},
				statusCode: Status_Code
			});
		}
	});
});
