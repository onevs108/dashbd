define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		url: '/dashbd/api/content.do'
	});
});
