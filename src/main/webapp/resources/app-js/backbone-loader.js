define([
	'raw_backbone',
	'jquery',
	'underscore'
], function(
	Backbone,
	$,
	_
) {
	// Map from CRUD to HTTP for our default `Backbone.sync` implementation.
	var methodMap = {
		'create': 'POST',
		'update': 'PUT',
		'patch':  'PATCH',
		'delete': 'DELETE',
		'read':   'GET'
	};

	// http method emulation
	Backbone.emulateHTTP = true;
	Backbone.emulateJSON = true;

	// Backbone.sync 오버라이드
	_.extend(Backbone, {
		sync: function(method, model, options) {
			var type = methodMap[method];

			// Default options, unless specified.
			_.defaults(options || (options = {}), {
				emulateHTTP: Backbone.emulateHTTP,
				emulateJSON: Backbone.emulateJSON,
				requestType: method
			});

			// Default JSON-request options.
			var params = {type: type, dataType: 'json'};

			// Ensure that we have a URL.
			if(!options.url) {
				params.url = _.result(model, 'url') || urlError();

				// add reqeustType to url
				params.url += '?request_type=' + options.requestType;
			}

			// Ensure that we have the appropriate request data.
			if(options.data === null && model && (method === 'create' || method === 'update' || method === 'patch')) {
				params.contentType = 'application/json';
				params.data = JSON.stringify(options.attrs || model.toJSON(options));
			}

			// For older servers, emulate JSON by encoding the request into an HTML-form.
			if(options.emulateJSON) {
				params.contentType = 'application/x-www-form-urlencoded';
				params.data = model ? model.attributes : {};
			}

			// For older servers, emulate HTTP by mimicking the HTTP method with `_method`
			// And an `X-HTTP-Method-Override` header.
			if(options.emulateHTTP && (type === 'PUT' || type === 'DELETE' || type === 'PATCH')) {
				params.type = 'POST';
				if(options.emulateJSON) params.data._method = type;
				var beforeSend = options.beforeSend;
				options.beforeSend = function(xhr) {
					xhr.setRequestHeader('X-HTTP-Method-Override', type);
					if (beforeSend) return beforeSend.apply(this, arguments);
				};
			}

			// Don't process data on a non-GET request.
			if(params.type !== 'GET' && !options.emulateJSON) {
				params.processData = false;
			}

			// Pass along `textStatus` and `errorThrown` from jQuery.
			var error = options.error;
			options.error = function(xhr, textStatus, errorThrown) {
				options.textStatus = textStatus;
				options.errorThrown = errorThrown;
				if(error) error.call(options.context, xhr, textStatus, errorThrown);
			};

			// Make the request, allowing the user to override any Ajax options.
			var xhr = options.xhr = Backbone.ajax(_.extend(params, options));
			model.trigger('request', model, xhr, options);
			return xhr;
		}
	});

	// Backbone.Model 오버라이드
	_.extend(Backbone.Model.prototype, {
		parse: function(response) {
			return _.result(response, 'result', response);
		},

		is_selected: false,
		select: function() {
			this.is_selected = true;
			this.trigger('selected', this.id);
		},
		unselect: function() {
			this.is_selected = false;
			this.trigger('unselected', this.id);
		},

		choose: function() {
			if(this.collection) {
				this.collection._choose_me(this.id);
			}
		},
		am_i_chosen: function() {
			if(this.collection && this.collection.get_chosen() === this.id) {
				return true;
			} else {
				return false;
			}
		}
	});

	// Backbone.Collection 오버라이드
	_.extend(Backbone.Collection.prototype, {
		get_selected_models: function() {
			return _.filter(this.models, function(model) {
				return model.is_selected;
			});
		},

		chosen: null,
		_choose_me: function(model_id) {
			this.each(function(model) {
				if(model.id === model_id) {
					model.trigger('chosen', model.id);
				} else {
					model.trigger('unchosen', model.id);
				}
			});
			this.chosen = model_id;
		},
		get_chosen: function() {
			return this.chosen;
		},

		is_selected: false,
		select: function() {
			this.is_selected = true;
		},
		unselect: function() {
			this.is_selected = false;
		},

		select_all_models: function(options) {
			if(_.result(options || {}, 'silent', false)) {
				this.each(function(model) {
					model.is_selected = true;
				});
			} else {
				this.each(function(model) {
					model.select();
				});
			}
		},
		unselect_all_models: function(options) {
			if(_.result(options || {}, 'silent', false)) {
				this.each(function(model) {
					model.is_selected = false;
				});
			} else {
				this.each(function(model) {
					model.unselect();
				});
			}
		},
		parse: function(response) {
			return _.result(response, 'result', response);
		}
	});

	// Backbone.History 오버라이드
	_.extend(Backbone.History.prototype, {
		getParam: function(key, default_value) {
			var current_hash = this.getFragment();

			if(typeof default_value === 'undefined') {
				default_value = null;
			}

			if(current_hash.indexOf(key + '/') >= 0) {
				current_hash = current_hash.split('/');
				for(var i = 0, max = current_hash.length; i < max; i += 2) {
					if(current_hash[i] === key) {
						break;
					}
				}

				return current_hash[i + 1] || default_value;
			} else {
				return default_value;
			}
		},
		changeParam: function(key, value, options) {
			var current_hash = this.getFragment().split('/');

			if(_.indexOf(current_hash, key) >= 0) {
				for(var i = 0, max = current_hash.length; i < max; i += 2) {
					if(current_hash[i] === key) {
						current_hash[i + 1] = value;
					}
				}
			} else {
				current_hash.push(key, value);
			}

			current_hash = current_hash.join('/');
			
			if(current_hash !== this.getFragment()) {
				this.navigate(current_hash, options || {});
			}
		}
	});

	return Backbone;
});
