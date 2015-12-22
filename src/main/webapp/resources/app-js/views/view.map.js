define([
	'backbone',
	'underscore',
	'asynclib',
	'markerclusterer',
	'libs/utility',

	'models/model.enb',
	'models/collection.service_area',

	'views/view.map.service_area',

	'async!https://maps.googleapis.com/maps/api/js?key=' + GOOGLE_MAP.API_KEY
], function(
	Backbone,
	_,
	Async,
	MarkerClusterer,
	Utility,

	Model_Enb,
	Collection_Service_Area,

	View_Map_Service_Area
) {
	var logger = Utility.create_logger({
		scope: 'View_Map',
		use_log: LOGGER.USE_LOG
	});

	return Backbone.View.extend({
		initialize: function(options) {
			this.use_drag_selection = _.result(options, 'use_drag_selection', true);
			this.use_click_selection = _.result(options, 'use_click_selection', true);
			this.use_marker_clusterer = _.result(options, 'use_marker_clusterer', false);
			this.use_reload_on_map_change = _.result(options, 'use_reload_on_map_change', true);
			this.service_areas = options.service_areas || [];
			this.map_listeners = [];
			this.info_window = null;
			this.marker_bounds = new google.maps.LatLngBounds();

			// collection이 주어진 경우는
			// service_area object에 추가한다.
			// (다른 function들의 호환성을 유지하기 위해서)
			if(this.collection) {
				this.service_areas.push(this.collection);
			}

			this.clusterer = new MarkerClusterer(Utility.Context.get('map'), [], {
				maxZoom: this.use_marker_clusterer ? GOOGLE_MAP.CLUSTERER_MAX_ZOOM_LEVEL : 1,
				gridSize: 50
			});

			if(this.use_drag_selection) {
				logger.info('initialize', 'use drag selection.');
				this.setDragSelectionEvent();
			}

			if(this.use_click_selection) {
				logger.info('initialize', 'use click selection.');
				this.setCtrlKeyPressedEvent();
			}

			if(this.use_marker_clusterer) {
				logger.info('initialize', 'use marker clusterer.');
			}

			// 모든 service_area collection들에 allmarkersaddedtocluster 이벤트를 리스닝해서
			// 모든 service_area의 enb들이 marker를 cluster에 추가하면
			// cluster의 fitMapToMarkers 메소드 호출.

			// 두번째로는 각 enb에 click event 발생 후
			// info window를 표시함
			var all_marker_added_service_area_count = 0;
			_.each(this.service_areas, function(service_area) {
				this.listenTo(service_area, 'allmarkersaddedtocluster', function() {
					all_marker_added_service_area_count++;

					if(this.service_areas.length === all_marker_added_service_area_count) {
						this.clusterer.fitMapToMarkers();
					}
				});

				this.listenTo(service_area, 'clicked', this.showOperationDialogWindow);
			}, this);

			// map의 change 이벤트에 따른 기록 로그
			var map = Utility.Context.get('map');

			map.addListener('zoom_changed', function() {
				logger.debug('initialize', 'current zoom level: ' + map.getZoom());
			});

			if(this.use_reload_on_map_change) {
				var reload_tid,
					reload_threshold = 800;

				var reload_enbs = function() {
						if(reload_tid) {
							clearTimeout(reload_tid);
						}

						reload_tid = setTimeout(function() {
							var new_coords = map.getBounds().toUrlValue(6);
							logger.debug('initialize', 'current map bounds: ' + new_coords);
							Backbone.history.changeParam('coords', new_coords, {trigger: true});
						}, reload_threshold);
					};

				//map.addListener('zoom_changed', reload_enbs);
				//map.addListener('center_changed', reload_enbs);
			}

			this.render();
		},
		render: function() {
			_.each(this.service_areas, function(service_area) {
				new View_Map_Service_Area({
					collection: service_area,
					clusterer: this.clusterer,
					use_click_selection: this.use_click_selection
				});
			}, this);
		},

		unloadAllMarkers: function() {
			// clear all clusters
			this.clusterer.clearMarkers();

			// unload event listener
			// map event listeners
			_.each(this.map_listeners, function(listener) {
				listener.remove();
			});

			// window key event listeners
			$(window)
				.off('keyup', '**')
				.off('keydown', '**');

			// collection suicide
			_.each(this.service_area, function(service_area) {
				service_area.trigger('unloadcollectionmarkers');
			});
		},
		_onClickMoreInfo: function(model) {
			if(this.info_window !== null) {
				this.info_window.close();
			}

			this.info_window = new google.maps.InfoWindow({
				content: _.template($('#tpl-more-info').html())(model.attributes)
			});
			this.info_window.open(Utility.Context.get('map'), model.get('marker').base_marker);
		},
		_onClickAddToServiceArea: function() {
			var service_area_id = parseInt(this.service_areas[0].service_area_id);
			var selected_models = _.chain(this.service_areas)
				.map(function(service_area) {
					return service_area.get_selected_models();
				})
				.flatten()
				.filter(function(model) {
					return service_area_id !== model.get('id');
				})
				.value();

			Async.each(selected_models, function(model, callback) {
				model.add_to_service_area(service_area_id, callback);
			}, function() {
				Utility.Context.get('router').navigate('service_area/' + service_area_id + '/level/success/message/' + encodeURIComponent('enbs added to service area.'));
				window.location.reload();
			});
		},
		_onClickDeleteFromServiceArea: function() {
			var service_area_id = parseInt(this.service_areas[0].service_area_id);
			var selected_models = _.chain(this.service_areas)
				.map(function(service_area) {
					return service_area.get_selected_models();
				})
				.flatten()
				.filter(function(model) {
					return service_area_id === model.get('id');
				})
				.value();

			Async.each(selected_models, function(model, callback) {
				model.delete_from_service_area(service_area_id, callback);
			}, function() {
				Utility.Context.get('router').navigate('service_area/' + service_area_id + '/level/success/message/' + encodeURIComponent('enbs deleted from service area.'));
				window.location.reload();
			});
		},
		_getInfoWindowContent: function(model) {
			var $content = $('<ul>' +
			'<li><a href="#" onclick="return false;" class="more_info">More info</span></a></li>' +
			'<li><a href="#" onclick="return false;" class="add_to_service_area">Add To Service Area</a></li>' +
			'<li><a href="#" onclick="return false;" class="delete_from_service_area">Delete From Service Area</a></li>' +
			'</ul>');

			$content.on('click', function(e) {
				e.preventDefault();

				var $target = $(e.target);

				if($target.hasClass('more_info')) {
					this._onClickMoreInfo(model);
				} else if($target.hasClass('add_to_service_area')) {
					this._onClickAddToServiceArea();
				} else if($target.hasClass('delete_from_service_area')) {
					this._onClickDeleteFromServiceArea();
				}
			}.bind(this));

			return $content[0];
		},
		showOperationDialogWindow: function(model) {
			if(this.info_window !== null) {
				this.info_window.close();
			}

			var map = Utility.Context.get('map');

			// map의 zoom level이 clusterer의 max zoom level 이하면
			// 그 수준까지 zoom을 한다.
			if(map.getZoom() <= GOOGLE_MAP.CLUSTERER_MAX_ZOOM_LEVEL) {
				map.setZoom(GOOGLE_MAP.CLUSTERER_MAX_ZOOM_LEVEL + 1);
			}

			// 선택된 enb의 position으로 map을 center 고정한다.
			map.setCenter(model.get('marker').base_marker.getPosition());

			this.info_window = new google.maps.InfoWindow({
				content: this._getInfoWindowContent(model)
			});
			this.info_window.open(Utility.Context.get('map'), model.get('marker').base_marker);
		},
		setCtrlKeyPressedEvent: function() {
			var Context = Utility.Context;

			Context.set('is_ctrl_pressed', false);

			$(window)
				.on('keydown', function(e) {
					if(e.which === 17) {
						Context.set('is_ctrl_pressed', true);
					}
				})
				.on('keyup', function(e) {
					if(e.which === 17) {
						Context.set('is_ctrl_pressed', false);
					}
				});
		},
		setDragSelectionEvent: function() {
			var self = this;
			var map = Utility.Context.get('map');
			var input_status = {
					is_shift_pressed: false,
					is_mouse_down: false
				};
			var south_west;
			var rectangle_selection;

			// shift press event
			$(window)
				.on('keydown', function(e) {
					if(e.which === 16) {
						input_status.is_shift_pressed = true;
					}
				})
				.on('keyup', function(e) {
					if(e.which === 16) {
						input_status.is_shift_pressed = false;
					}
				});

			this.map_listeners.push(map.addListener('mousedown', function(e) {
				if(input_status.is_shift_pressed) {
					input_status.is_mouse_down = true;
					south_west = e.latLng;

					map.setOptions({
						draggable: false
					});
				}
			}));
			this.map_listeners.push(map.addListener('mousemove', function(e) {
				if(input_status.is_mouse_down && (input_status.is_shift_pressed || rectangle_selection)) {
					if(rectangle_selection) {
						var new_bounds = new google.maps.LatLngBounds(south_west, null);
						new_bounds.extend(e.latLng);
						rectangle_selection.setBounds(new_bounds);
					} else {
						rectangle_selection = new google.maps.Rectangle({
							map: map,
							bounds: null,
							fillColor: '#2F70CC',
							fillOpacity: 0.15,
							strokeColor: '#2F70CC',
							strokeWeight: 0.2,
							clickable: false
						});
					}
				}
			}));
			this.map_listeners.push(map.addListener('mouseup', function(e) {
				if(input_status.is_mouse_down && (input_status.is_shift_pressed || rectangle_selection)) {
					var selection_bounds = _.clone(rectangle_selection.getBounds());

					input_status.is_mouse_down = false;

					rectangle_selection.setMap(null);
					rectangle_selection = undefined;

					_.chain(self.service_areas)
						.map(function(service_area) {
							return service_area.models;
						})
						.flatten()
						.each(function(model) {
							if(selection_bounds.contains(model.get('marker').base_marker.getPosition())) {
								if(model.is_selected) {
									model.unselect();
								} else {
									model.select();
								}
							}
						});
				}

				map.setOptions({
					draggable: true
				});
			}));
		}
	});
});
