define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		idAttribute: 'bmsc_id',
		url: '/dashbd/api/bmsc.do'
	});
});
