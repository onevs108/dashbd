define([
	'backbone',
	'underscore',
	'libs/status_code',
	'libs/utility'
], function(
	Backbone,
	_,
	Status_Code,
	Utility
) {
	var logger = Utility.create_logger({
			scope: 'Router_Content_Form',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.content = options.content;
		},
		routes: {
			'mode/create': 'triggerContentFetchedWithCreateMode',
			'mode/:mode/id/:id': 'fetchContentByModeAndID'
		},
		triggerContentFetchedWithCreateMode: function() {
			this.content.trigger('contentfetched', 'create');
		},
		fetchContentByModeAndID: function(mode, id) {
			this.content.fetch({
				data: {
					id: id
				},
				success: function() {
					this.content.trigger('contentfetched', mode);
				}.bind(this),
				statusCode: Status_Code
			});
		}
	});
});
