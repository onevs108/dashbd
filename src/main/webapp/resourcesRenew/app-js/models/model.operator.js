define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		url: '/dashbd/api/operator.do'
	});
});
