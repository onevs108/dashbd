define([
	'backbone',
	'libs/md5',

	'views/view.map.enb'
], function(
	Backbone,
	md5,

	View_Map_Enb
) {
	return Backbone.View.extend({
		initialize: function(options) {
			this.use_click_selection = options.use_click_selection;
			this.clusterer = options.clusterer;
			this.added_marker_count = 0;

			this.listenTo(this.collection, 'markeraddedtocluster', this.markerAddedToCluster);
			this.listenTo(this.collection, 'unloadcollectionmarkers', this.unloadMarkersBelongsToThisCollection);

			this.render();
		},
		render: function() {
			this.collection.each(function(model) {
				new View_Map_Enb(_.extend(
					{
						model: model,
						clusterer: this.clusterer,
						use_click_selection: this.use_click_selection
					},
					this.getServiceAreaBaseColorAndZIndex(model.get('id'))
				));
			}, this);
		},
		getServiceAreaBaseColorAndZIndex: function(enb_service_area_id) {
			// collection에 service_area_id가 존재할 경우
			// 1. service_area_id와 enb_service_area_id가 같다면 Red
			// 2. service_area_id와 enb_service_area_id가 다르다면 Blue
			// 3. enb_service_area_id가 null인 경우 Gray
			// collection에 service_area_id가 존재하지 않을 경우
			// 1. enb_service_area_id가 null인 경우 Gray
			// 2. 나머지는 undefined
			var service_area_id = this.collection.service_area_id || false;

			if(!service_area_id) {
				if(enb_service_area_id === null) {
					return {base_color: '#CCC'};
				} else {
					return {};
				}
			} else {
				if(enb_service_area_id === null) {
					return {base_color: '#969696'};
				} else if(parseInt(service_area_id) === enb_service_area_id) {
					return {base_color: '#D4132A', zIndex: 9999};
				} else {
					return {base_color: '#1673DE'};
				}
			}
		},
		markerAddedToCluster: function() {
			this.added_marker_count++;

			if(this.collection.length === this.added_marker_count) {
				this.collection.trigger('allmarkersaddedtocluster');
			}
		},
		unloadMarkersBelongsToThisCollection: function() {
			this.collection.each(function(model) {
				model.trigger('unloadmodelmarker');
			});
		}
	});
});
