define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		idAttribute: 'service_area_id',
		url: '/dashbd/api/service_area.do'
	});
});
