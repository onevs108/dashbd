define([
	'underscore'
], function(
	_
) {
	var Utility = {};

	// Context
	Utility.Context = (function() {
		var context = {};

		return {
			get: function(key) {
				return context[key] || undefined;
			},
			set: function(key, value) {
				context[key] = value;

				return value;
			}
		};
	})();

	// Logger
	var Logger = function(options) {
			this.options = _.defaults(options || {}, {
				scope: '*',
				use_log: false
			});
		};

	Logger.prototype.log = function(log_level, title, message) {
		if(this.options.use_log === true && typeof window.console !== 'undefined') {
			console.log((new Date()).toLocaleString() + ' - ' + log_level + ': [' + this.options.scope + '/' + title + '] ' + (typeof message !== 'string' ? JSON.stringify(message) : message));
		}
	};

	Logger.prototype.info = function(title, message) {
		this.log('info', title, message);
	};

	Logger.prototype.debug = function(title, message) {
		this.log('debug', title, message);
	};

	Logger.prototype.error = function(title, message) {
		this.log('error', title, message);
	};

	Utility.create_logger = function(options) {
		return new Logger(options);
	};

	return Utility;
});
