define([
	'backbone',
	'models/model.bmsc',
	'models/model.service_area'
], function(
	Backbone,
	Model_BMSC,
	Model_Service_Area
) {
	return Backbone.Collection.extend({
		model: function(attrs, options) {
			switch(options.requestType) {
				case 'read':
					return new Model_BMSC(_.extend(attrs, {is: 'model_bmsc'}), options);
				case 'read_service_area':
					return new Model_Service_Area(_.extend(attrs, {is: 'model_service_area'}), options);
			}
		},
		modelId: function(attrs) {
			return attrs[attrs.is === 'model_bmsc' ? Model_BMSC.prototype.idAttribute || 'id' : Model_Service_Area.prototype.idAttribute || 'id'];
		},
		url: '/dashbd/api/bmsc.do'
	});
});
