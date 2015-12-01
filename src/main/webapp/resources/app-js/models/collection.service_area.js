define([
	'backbone',
	'models/model.enb'
], function(
	Backbone,
	Model_Enb
) {
	return Backbone.Collection.extend({
		model: Model_Enb,
		url: '/dashbd/api/service_area.do'
	});
});
