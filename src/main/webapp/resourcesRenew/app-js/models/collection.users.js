define([
	'backbone',
	'models/model.user'
], function(
	Backbone,
	Model_User
) {
	return Backbone.Collection.extend({
		model: Model_User,
		url: '/dashbd/api/user.do'
	});
});
