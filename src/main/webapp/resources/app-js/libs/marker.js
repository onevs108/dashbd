define([
	'underscore',
	'asynclib',

	'async!https://maps.googleapis.com/maps/api/js?key=' + GOOGLE_MAP.API_KEY
], function(
	_,
	Async
) {
	var selected_saturation = 50; // marker 선택시 외곽선을 base_color보다 50포인트 증가함
	var Marker = function(option) {
			this.option = _.extend({
				lat: 0,
				lng: 0,
				title: null,
				map: null,
				base_color: '#CCCCCC',
				selected_stroke_color: '#CCCCCC',
				zIndex: {
					base: 0,
					station: 9999
				}
			}, option || {});

			this.base_marker = null;
			this.station_marker = null;

			this.marker_listeners = [];

			// marker 선택시 외곽선의 색상을 미리 지정함.
			this.option.selected_stroke_color = '#' + _.chain(this.option.base_color.replace('#', '').match(/(.{1,2})/g))
				.map(function(hex) {
					var new_color = Math.max(parseInt(hex, '16') - selected_saturation, 0).toString(16).toUpperCase();
					return Array(2 - new_color.length + 1).join('0') + new_color;
				})
				.value()
				.join('');
		};

	Marker.prototype.get_base_symbol = function() {
		return {
			path: google.maps.SymbolPath.CIRCLE,
			fillColor: this.option.base_color,
			fillOpacity: 1,
			scale: 12,
			strokeColor: this.option.base_color,
			strokeWeight: 4
		};
	};

	// 선택된 모양의 symbol object를 반환
	Marker.prototype.get_selected_base_symbol = function() {
		return {
			path: google.maps.SymbolPath.CIRCLE,
			fillColor: this.option.base_color,
			fillOpacity: 1,
			scale: 12,
			strokeColor: this.option.selected_stroke_color,
			strokeWeight: 4
		};
	};

	Marker.prototype.get_station_symbol = function() {
		return {
			path: 'M -197.9,355.7l-4.2-5.6c-13.9,9.4-23,24.8-23,42.3c0,17.5,9.1,32.9,23,42.3l4.2-5.6c-12-8.1-19.9-21.5-19.9-36.6C-217.8,377.2-209.9,363.8-197.9,355.7M-191.1,364.8l-3.1-4.2c-10.4,7-17.2,18.6-17.2,31.7c0,13.1,6.8,24.7,17.2,31.7l3.1-4.2c-9-6.1-14.9-16.1-14.9-27.5C-206,380.9-200.1,370.9-191.1,364.8 M-154.2,392.3c0-8.7-7.4-15.8-16.6-15.8c-9.2,0-16.6,7.1-16.6,15.8c0,7.8,5.9,14.2,13.6,15.6v79.3c0,1.6,1.4,2.9,3,2.9c1.7,0,3-1.3,3-2.9v-79.3C-160,406.5-154.2,400-154.2,392.3M-147.3,360.6l-3.1,4.2c9,6.1,14.9,16.1,14.9,27.5c0,11.4-5.9,21.4-14.9,27.5l3.1,4.2c10.4-7,17.2-18.6,17.2-31.7C-130,379.2-136.9,367.6-147.3,360.6 M-116.5,392.3c0-17.5-9.1-32.9-23-42.3l-4.2,5.6c12,8.1,19.9,21.5,19.9,36.6c0,15.1-7.9,28.5-19.9,36.6l4.2,5.6C-125.6,425.2-116.5,409.8-116.5,392.3 z',
			fillColor: 'White',
			fillOpacity: 1,
			strokeColor: 'White',
			strokeWeight: 1,
			scale: 0.2,
			anchor: new google.maps.Point(-170, 410)
		};
	};

	Marker.prototype.draw_base_marker = function() {
		this.base_marker = new google.maps.Marker({
			position: _.pick(this.option, 'lat', 'lng'),
			map: this.option.map,
			title: this.option.title,
			icon: this.get_base_symbol(),
			zIndex: this.option.zIndex.base
		});
	};

	Marker.prototype.draw_station_marker = function() {
		this.station_marker = new google.maps.Marker({
			position: _.pick(this.option, 'lat', 'lng'),
			map: this.option.map,
			title: this.option.title,
			icon: this.get_station_symbol(),
			zIndex: this.option.zIndex.station
		});
	};

	Marker.prototype.mark_as_selected = function() {
		this.base_marker.setIcon(this.get_selected_base_symbol());
	};

	Marker.prototype.mark_as_unselected = function() {
		this.base_marker.setIcon(this.get_base_symbol());
	};

	Marker.prototype.unload_marker = function(callback) {
		_.each(this.marker_listeners, function(listener) {
			listener.remove();
		});

		if(this.base_marker !== null) {
			this.base_marker.setMap(null);
			this.base_marker = null;
		}

		if(this.station_marker !== null) {
			this.station_marker.setMap(null);
			this.station_marker = null;
		}

		Async.nextTick(callback);
	};

	Marker.create = function(option) {
		var marker = new Marker(option);

		marker.draw_base_marker();
		//marker.draw_station_marker();

		return marker;
	};

	return Marker;
});
