define([
	'backbone',
	'underscore',
	'libs/status_code',
	'libs/utility',
	'asynclib'
], function(
	Backbone,
	_,
	Status_Code,
	Utility,
	Async
) {
	var logger = Utility.create_logger({
			scope: 'Router_Service_Area_Management',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			// 기본 collection
			this.operator = options.operator;
			this.bmsc = options.bmsc;
			this.service_area = options.service_area;
		},
		routes: {
			'operator/:operator_id': 'fetchBMSCByOperatorID',
			'operator/:operator_id/bmsc/:bmsc_id': 'fetchServiceAreaByBMSCID'
		},
		fetchBMSCByOperatorID: function(operator_id) {
			var operator = this.operator;

			if(!operator.get(operator_id)) {
				operator.once('reset', function() {
					operator.get(operator_id).select();
				});
			} else {
				operator.get(operator_id).select();
			}

			this.bmsc.fetch({
				reset: true,
				requestType: 'read_operator_bmsc',
				data: {
					operator_id: operator_id
				},
				statusCode: Status_Code
			});
		},
		fetchServiceAreaByBMSCID: function(operator_id, bmsc_id) {
			var operator = this.operator,
				bmsc = this.bmsc,
				service_area = this.service_area;

			if(!operator.get(operator_id)) {
				operator.once('reset', function() {
					operator.get(operator_id).select();
				});
			} else {
				operator.get(operator_id).select();
			}

			if(!bmsc.get(bmsc_id)) {
				bmsc.once('reset', function() {
					bmsc.get(bmsc_id).choose();
				});
			} else {
				bmsc.get(bmsc_id).choose();
			}

			bmsc.fetch({
				reset: true,
				requestType: 'read_operator_bmsc',
				data: {
					operator_id: operator_id
				},
				statusCode: Status_Code,
				success: function() {
					service_area.fetch({
						reset: true,
						requestType: 'read_service_area',
						data: {
							id: bmsc_id
						},
						statusCode: Status_Code
					});
				}
			});
		}
	});
});
