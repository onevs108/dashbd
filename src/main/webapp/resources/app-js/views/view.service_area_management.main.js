define([
	'backbone',
	'underscore',
	'libs/utility',
	'libs/status_code',

	'models/collection.service_area',

	'views/view.service_area_management.bmsc_entry'
], function(
	Backbone,
	_,
	Utility,
	Status_Code,

	Collection_Service_Area,

	View_Service_Area_Management_BMSC_Entry
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.bmsc = options.bmsc;
			this.service_area = options.service_area;

			this.listenTo(this.collection, 'reset', this.renderOperatorSelector); // operator
			this.listenTo(this.bmsc, 'reset', this.renderBMSCSelector);

			this.listenTo(this.collection, 'selected', this.operatorSelected); // operator

			this.listenTo(this.service_area, 'reset', this.serviceAreaFetched);
		},
		events: {
			'change select#operator': 'operatorChanged',
			'click #btn-add-service-area': 'clickAddServiceAreaButton'
		},

		renderOperatorSelector: function() {
			var $operator = this.$el.find('select#operator');

			// cleaning operator
			$operator.empty();
			$operator.append('<option value="">Select one</option>');

			this.collection.each(function(model) {
				$operator.append('<option value="' + model.id + '">' + model.get('name') + '</option>');
			});
		},
		renderBMSCSelector: function() {
			var $bmsc = this.$el.find('ul#bmsc');

			// cleaning bmsc selector
			$bmsc.empty();

			this.bmsc.each(function(model) {
				$bmsc.append((new View_Service_Area_Management_BMSC_Entry({
					model: model
				})).el);
			});
		},
		clickAddServiceAreaButton: function() {
			window.location.href = 'service_area_form.html';
		},
		operatorChanged: function() {
			Utility.Context.get('router').navigate('operator/' + this.$el.find('select#operator').val(), {trigger: true});
		},
		operatorSelected: function(model_id) {
			this.$el.find('select#operator').val(model_id);
		},
		serviceAreaFetched: function() {
			var $service_area = this.$el.find('ul#service_area');

			// cleaning service area
			$service_area.find('li:gt(0)').empty();

			this.service_area.each(function(model) {
				$service_area.append('<li><a href="service_area.html#service_area/' + model.id + '">' + model.get('service_area_name') + '(' + model.id + ')</a></li>');
			});
		}
	});
});
