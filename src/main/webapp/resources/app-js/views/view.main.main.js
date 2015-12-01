define([
	'backbone',
	'underscore',
	'libs/utility',
	'asynclib',

	'models/collection.service_area',

	'views/view.map'
], function(
	Backbone,
	_,
	Utility,
	Async,

	Collection_Service_Area,

	View_Map
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.view_map = null;
			this.bmsc = options.bmsc;
			this.service_area = options.service_area;
			this.service_areas = [];

			this.listenTo(this.collection, 'reset', this.renderOperatorSelector); // operator
			this.listenTo(this.bmsc, 'reset', this.renderBMSCSelector);
			this.listenTo(this.collection, 'selected', this.operatorSelected); // operator
			this.listenTo(this.bmsc, 'selected', this.bmscSelected);
			this.listenTo(this.service_area, 'reset', this.serviceAreaFetched);
		},
		events: {
			'change select#operator': 'operatorChanged',
			'change select#bmsc': 'bmscChanged'
		},

		serviceAreaFetched: function() {
			if(this.view_map !== null) {
				this.view_map.unloadAllMarkers();
			}

			if(this.service_areas.length >= 0) {
				this.service_areas = [];
			}

			Async.each(this.service_area.models, function(service_area, callback) {
				(new Collection_Service_Area()).fetch({
					reset: true,
					requestType: 'read_enb_ap',
					data: {
						id: service_area.id
					},
					success: function(collection) {
						this.service_areas.push(collection);

						callback();
					}.bind(this)
				});
			}.bind(this), function(error) {
				this.view_map = new View_Map({
					use_drag_selection: false,
					use_click_selection: false,
					use_reload_on_map_change: false,
					service_areas: this.service_areas
				});
			}.bind(this));
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
			var $bmsc = this.$el.find('select#bmsc');

			// cleaning bmsc selector
			$bmsc.empty();
			$bmsc.append('<option value="">Select one</option>');

			this.bmsc.each(function(model) {
				$bmsc.append('<option value="' + model.id + '">' + model.get('bmsc_name') + '</option>');
			});
		},
		operatorChanged: function() {
			Utility.Context.get('router').navigate('operator/' + this.$el.find('select#operator').val(), {trigger: true});
		},
		bmscChanged: function() {
			Utility.Context.get('router').navigate('operator/' + this.$el.find('select#operator').val() + '/bmsc/' + this.$el.find('select#bmsc').val(), {trigger: true});
		},
		operatorSelected: function(model_id) {
			this.$el.find('select#operator').val(model_id);
		},
		bmscSelected: function(model_id) {
			this.$el.find('select#bmsc').val(model_id);
		}
	});
});
