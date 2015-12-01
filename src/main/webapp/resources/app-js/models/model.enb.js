define([
	'backbone',
	'libs/utility',

	'models/model.service_area'
], function(
	Backbone,
	Utility,

	Model_Service_Area
) {
	var logger = Utility.create_logger({
			scope: 'Model_Enb',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Model.extend({
		idAttribute: 'enb_ap_id',
		url: '/dashbd/api/enb.do',
		add_to_service_area: function(service_area_id, callback) {
			if(service_area_id !== this.get('id')) {
				var url = Model_Service_Area.prototype.url + '?request_type=add_enb',
					data = {
						id: service_area_id,
						enb_ap_id: this.id
					};

				logger.debug('add_to_service_area', 'url: ' + url + ', params: ' + JSON.stringify(data));

				Backbone.ajax({
					method: 'POST',
					url: url,
					data: data,
					complete: function() {
						callback();
					}
				});
			}
		},
		delete_from_service_area: function(service_area_id, callback) {
			if(service_area_id === this.get('id')) {
				var url = Model_Service_Area.prototype.url + '?request_type=remove_enb',
					data = {
						id: service_area_id,
						enb_ap_id: this.id
					};

				logger.debug('delete_from_service_area', 'url: ' + url + ', params: ' + JSON.stringify(data));

				Backbone.ajax({
					method: 'POST',
					url: url,
					data: data,
					complete: function() {
						callback();
					}
				});
			}
		}
	});
});
