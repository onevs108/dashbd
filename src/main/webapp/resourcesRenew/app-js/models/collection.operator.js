define([
	'backbone',
	'models/model.operator',
	'models/model.bmsc'
], function(
	Backbone,
	Model_Operator,
	Model_BMSC
) {
	return Backbone.Collection.extend({
		model: function(attrs, options) {
			switch(options.requestType) {
				case 'read':
					return new Model_Operator(_.extend(attrs, {is: 'model_operator'}), options);
				case 'read_operator_bmsc':
					return new Model_BMSC(_.extend(attrs, {is: 'model_bmsc'}), options);

			}
		},
		modelId: function(attrs) {
			return attrs[attrs.is === 'model_operator' ? Model_Operator.prototype.idAttribute || 'id' : Model_BMSC.prototype.idAttribute || 'id'];
		},
		url: '/dashbd/api/operator.do'
	});
});
