/**
 * Googlemap setting
 */
var GOOGLE_MAP = {
		API_KEY: 'AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo',
		MAP_CENTER: {
			lat: 37.5462,
			lng: 126.9879
		},
		ZOOM_LEVEL: 12,
		CLUSTERER_MAX_ZOOM_LEVEL: 15
	};

/**
 * Logger
 */
var LOGGER = {
		USE_LOG: true
	};

/**
 * Users
 */
var USER = {
		DEFAULT_PAGE_ROWS: 10
	};

/**
 * function.bind를 지원하지 않는 하위 웹브라우져를 위한 호환성 유지 코드
 */
if(!Function.prototype.bind) {
	Function.prototype.bind = function(THIS) {
		if(typeof this !== 'function') {
			// closest thing possible to the ECMAScript 5
			// internal IsCallable function
			throw new TypeError('Function.prototype.bind - what is trying to be bound is not callable');
		}

		var args = Array.prototype.slice.call(arguments, 1),
			f_to_bind = this,
			f_nop = function() {},
			f_bound = function() {
				return f_to_bind.apply(this instanceof f_nop && THIS ? this : THIS, args.concat(Array.prototype.slice.call(arguments)));
			};

		f_nop.prototype = this.prototype;
		f_bound.prototype = new f_nop();

		return f_bound;
	};
}

requirejs.config({
	urlArgs: '_bust=' + (new Date()).getTime(),
	baseUrl: '/dashbd/resources/app-js',
	paths: {
		async: 'bower_components/requirejs-plugins/src/async', // backbone async 플러그인
		asynclib: 'bower_components/async/dist/async.min', // async 라이브러리
		raw_backbone: 'bower_components/backbone/backbone-min',
		backbone: 'backbone-loader',
		jquery: 'bower_components/jquery/dist/jquery.min',
		underscore: 'bower_components/underscore/underscore-min',
		markerclusterer: 'bower_components/js-marker-clusterer/src/markerclusterer',
		raw_toastr: 'bower_components/toastr/toastr.min',
		toastr: 'toastr-loader',
		simple_pagination: 'bower_components/simplePagination.js/jquery.simplePagination'
	},
	shim: {
		jquery: {
			exports: '$'
		},
		underscore: {
			exports: '_'
		},
		markerclusterer: {
			exports: 'MarkerClusterer'
		}
	}
});
