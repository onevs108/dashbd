define([
	'backbone',
	'underscore',
	'libs/utility',
	'toastr',
	'asynclib',

	'models/model.service_area',

	'views/view.map',
	'views/view.service_area.enb_list'
], function(
	Backbone,
	_,
	Utility,
	toastr,
	Async,

	Model_Service_Area,

	View_Map,
	View_Service_Area_Enb_List
) {
	var logger = Utility.create_logger({
			scope: 'View_Service_Area_Main',
			use_log: LOGGER.USE_LOG
		});
	return Backbone.View.extend({
		el: 'body',
		initialize: function() {
			this.view_map = null;
			this.view_service_area_enb_list = null;

			this.listenTo(this.collection, 'reset', this.render);
			this.listenTo(this.collection, 'findoptionselected', this.findOptionSelected);

			// plmn과 mbsfn selector는 한번만 렌더링함.
			this.renderPLMNAndMBSFN();
		},
		events: {
			'change': 'getServiceAreaByBMSCId',
			'change select#plmn': 'findOptionChanged',
			'change select#mbsfn': 'findOptionChanged',
			'click button#btn-enb-aps': 'addEnbsToServiceArea',
			'submit form': function(e) {
				e.preventDefault();
				return false;
			}
		},
		render: function() {
			if(this.view_map !== null) {
				this.view_map.unloadAllMarkers();
			}

			this.view_map = new View_Map({
				collection: this.collection,
				use_drag_selection: true,
				use_click_selection: true,
				use_marker_clusterer: true
			});

			if(this.view_service_area_enb_list !== null) {
				this.view_service_area_enb_list.clearEnbList();
			}

			this.view_service_area_enb_list = new View_Service_Area_Enb_List({
				collection: this.collection
			});

			this.changeWindowTitle();

			return this;
		},

		changeWindowTitle: function() {
			Backbone.ajax({
				url: Model_Service_Area.prototype.url + '?request_type=read&id=' + this.collection.service_area_id,
				dataType: 'json',
				success: function(response) {
					this.$el.find('div.ibox-title > h5').text('Service Area ' + response.result.name + ' Management');
				}.bind(this)
			});
		},
		findOptionChanged: function(e) {
			var plmn = this.$el.find('select#plmn').val(),
				mbsfn = this.$el.find('select#mbsfn').val();

			var router = Utility.Context.get('router');

			if(typeof this.collection.service_area_id === 'undefined') {
				alert('select service area first.');
				$(e.target).val('');
				return false;
			}

			if(plmn && !mbsfn) {
				router.navigate('service_area/' + this.collection.service_area_id + '/plmn/' + plmn, {trigger: true});
			} else if(mbsfn && !plmn) {
				router.navigate('service_area/' + this.collection.service_area_id + '/mbsfn/' + mbsfn, {trigger: true});
			} else if(plmn && mbsfn) {
				router.navigate('service_area/' + this.collection.service_area_id + '/plmn/' + plmn + '/mbsfn/' + mbsfn, {trigger: true});
			} else {
				router.navigate('service_area/' + this.collection.service_area_id, {trigger: true});
			}
		},
		renderPLMNAndMBSFN: function() {
			var $plmn = this.$el.find('select#plmn'),
				$mbsfn = this.$el.find('select#mbsfn');

			_.each(['MCC', 'MNC'], function(plmn) {
				$plmn.append('<option value="' + plmn + '">' + plmn + '</option>');
			});

			_.each(_.range(256), function(mbsfn) {
				$mbsfn.append('<option value="' + mbsfn + '">' + mbsfn + '</option>');
			});
		},
		findOptionSelected: function(plmn, mbsfn) {
			this.$el.find('select#plmn').val(plmn ? plmn : '');
			this.$el.find('select#mbsfn').val(mbsfn ? mbsfn : '');
		},
		addEnbsToServiceArea: function() {
			var service_area_id = this.collection.service_area_id,
				enbs = this.$el.find('input#enb_aps').val();

			if(!enbs) {
				toastr.warning('input field is empty.');
				return false;
			}

			enbs = _.chain(enbs.replace(/ /g, '').split(','))
				.filter(function(enb) {
					return /^[0-9]+(-[0-9]+)?$/.test(enb);
				})
				.map(function(enb) {
					if(enb.indexOf('-') >= 0) {
						enb = enb.split('-');
						return _.range(parseInt(enb[0]), parseInt(enb[1]) + 1);
					} else {
						return parseInt(enb);
					}
				})
				.flatten()
				.uniq()
				.value();

			logger.debug('addEnbsToServiceArea', 'enbs to be added: ' + JSON.stringify(enbs));

			if(enbs.length > 0) {
				Async.each(enbs, function(enb, callback) {
					Backbone.ajax({
						url: Model_Service_Area.prototype.url + '?request_type=add_enb',
						method: 'POST',
						data: {
							id: service_area_id,
							enb_ap_id: enb
						},
						success: function() {
							callback();
						},
						error: function() {
							callback(enb);
						}
					});
				}, function(error) {
					if(error) {
						toastr.error('failed to add #' + error + ' enb');
					} else {
						Utility.Context.get('router').navigate('service_area/' + Backbone.history.getParam('service_area') + '/level/success/message/' + encodeURIComponent('enbs added. ' + JSON.stringify(enbs)), {trigger: true});
					}
				});
			} else {
				toastr.warning('no enbs to add.');
			}
		}
	});
});
