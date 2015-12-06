define([
	'backbone',
	'models/model.content'
], function(
	Backbone,
	Model_Content
) {
	return Backbone.Collection.extend({
		model: Model_Content,
		url: '/dashbd/api/content.do'
	});
});
