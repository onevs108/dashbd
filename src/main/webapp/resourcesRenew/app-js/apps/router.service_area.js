define([
	'backbone',
	'underscore',
	'libs/status_code',
	'libs/utility',
	'asynclib',
	'toastr'
], function(
	Backbone,
	_,
	Status_Code,
	Utility,
	Async,
	toastr
) {
	var logger = Utility.create_logger({
			scope: 'Router_Service_Area',
			use_log: LOGGER.USE_LOG
		});

	return Backbone.Router.extend({
		initialize: function(options) {
			this.service_area = options.service_area;
		},
		routes: {
			'service_area/:service_area/level/:level/message/:message': 'showMessageFromPreviousRequest',
			'service_area/:service_area_id(/coords/:coords)': 'fetchServiceAreaByID',
			'service_area/:service_area_id/plmn/:plmn(/mbsfn/:mbsfn)': 'fetchServiceAreaByIDAndPLMNAndMBSFN',
			'service_area/:service_area_id/mbsfn/:mbsfn': 'fetchServiceAreaByIDAndMBSFN'
		},
		showMessageFromPreviousRequest: function(service_area_id, level, message) {
			toastr[level](message);

			// 메세지 출력 후, 초기 요청시 자동으로 #service_area/?로 redirect
			this.navigate('service_area/' + service_area_id, {trigger: true});
		},
		_getEnbsBelongsToServiceArea: function(service_area_id, callback) {
			this.service_area.fetch({
				requestType: 'read_enb_ap',
				data: {
					id: service_area_id
				},
				success: function(collection) {
					callback(null, collection);
				},
				error: callback,
				statusCode: Status_Code
			});
		},
		_getOuterBoundsFromEnbCoords: function(collection, callback) {
			var padding = 0;
			var enbs = collection.map(function(model) {
					return {
						latitude: model.get('latitude'),
						longitude: model.get('longitude')
					};
				});

			var latitudes = _.chain(enbs)
				.sortBy('latitude')
				.map(function(enb) {
					return enb.latitude;
				})
				.value();

			var longitudes = _.chain(enbs)
				.sortBy('longitude')
				.map(function(enb) {
					return enb.longitude;
				})
				.value();

			logger.debug('latitudes', latitudes);
			logger.debug('longitudes', longitudes);

			callback(null, {
				lt_latitude: _.max(latitudes) + padding,
				lt_longitude: _.min(longitudes) - padding,
				rb_latitude: _.min(latitudes) - padding,
				rb_longitude: _.max(longitudes) + padding
			});
		},
		fetchServiceAreaByID: function(service_area_id) {
			Async.waterfall([
				// 이후 callback function들에 service_area_id를 넘겨주기 위한 callback
				function(callback) {
					callback(null, service_area_id);
				},
				this._getEnbsBelongsToServiceArea.bind(this),
				this._getOuterBoundsFromEnbCoords.bind(this)
			], function(error, bounds) {
				this.service_area.service_area_id = service_area_id;
				this.service_area.fetch({
					reset: true,
					requestType: 'read_range_enb_ap',
					data: bounds,
					statusCode: Status_Code
				});
			}.bind(this));
		},
		fetchServiceAreaByIDAndPLMNAndMBSFN: function(service_area_id, plmn, mbsfn) {
			this.service_area.trigger('findoptionselected', plmn, mbsfn);

			Async.waterfall([
				// 이후 callback function들에 service_area_id를 넘겨주기 위한 callback
				function(callback) {
					callback(null, service_area_id);
				},
				this._getEnbsBelongsToServiceArea.bind(this),
				this._getOuterBoundsFromEnbCoords.bind(this)
			], function(error, bounds) {
				if(error !== null) {
					return false;
				}

				this.service_area.service_area_id = service_area_id;
				this.service_area.fetch({
					reset: true,
					requestType: 'read_range_enb_ap',
					data: _.extend(bounds, {plmn: plmn}, (mbsfn ? {mbsfn: mbsfn} : {})),
					statusCode: Status_Code
				});
			}.bind(this));
		},
		fetchServiceAreaByIDAndMBSFN: function(service_area_id, mbsfn) {
			this.service_area.trigger('findoptionselected', plmn, mbsfn);

			Async.waterfall([
				// 이후 callback function들에 service_area_id를 넘겨주기 위한 callback
				function(callback) {
					callback(null, service_area_id);
				},
				this._getEnbsBelongsToServiceArea.bind(this),
				this._getOuterBoundsFromEnbCoords.bind(this)
			], function(error, bounds) {
				if(error !== null) {
					return false;
				}

				this.service_area.fetch({
					reset: true,
					requestType: 'read_range_enb_ap',
					data: _.extend(bounds, {mbsfn: mbsfn}, (plmn ? {plmn: plmn} : {})),
					statusCode: Status_Code
				});
			}.bind(this));
		}
	});
});
