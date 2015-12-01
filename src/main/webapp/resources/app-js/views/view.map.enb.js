define([
	'backbone',
	'libs/marker',
	'libs/md5',
	'libs/utility'
], function(
	Backbone,
	Marker,
	md5,
	Utility
) {
	return Backbone.View.extend({
		initialize: function(options) {
			var model = this.model;
			var service_area_id = _.result(model.attributes, 'service_area_id', model.get('id'));
			var zIndex = options.zIndex || service_area_id;
			var base_color = options.base_color || '#' + md5(service_area_id).slice(0, 6);

			model.set('marker', Marker.create({
				lat: model.get('latitude'),
				lng: model.get('longitude'),
				title: model.get('enb_ap_name'),
				base_color: base_color,
				zIndex: {
					base: zIndex,
					station: zIndex
				}
			}));

			options.clusterer.addMarker(model.get('marker').base_marker);
			this.model.trigger('markeraddedtocluster');

			this.listenTo(this.model, 'selected', this.showSelectionBounds);
			this.listenTo(this.model, 'unselected', this.hideSelectionBounds);
			this.listenTo(this.model, 'unloadmodelmarker', this.unloadMarker);

			if(options.use_click_selection) {
				this.setClickSelectionEvent();
			}
		},

		unloadMarker: function() {
			this.model.get('marker').unload_marker(function() {
				this.model.set('marker', null);
			}.bind(this));
		},
		setClickSelectionEvent: function() {
			this.model.get('marker').marker_listeners.push(this.model.get('marker').base_marker.addListener('click', function() {
				var model = this.model;

				if(Utility.Context.get('is_ctrl_pressed')) {
					if(model.is_selected) {
						model.unselect();
					} else {
						model.select();
					}
				} else if(model.is_selected) { // 선택된 marker(model) 위에서만 click 이벤트가 발생하도록
					model.trigger('clicked', model);
				}
			}.bind(this)));
		},
		showSelectionBounds: function() {
			this.model.get('marker').mark_as_selected();
		},
		hideSelectionBounds: function() {
			this.model.get('marker').mark_as_unselected();
		}
	});
});
