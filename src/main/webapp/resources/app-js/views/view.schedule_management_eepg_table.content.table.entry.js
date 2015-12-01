define([
	'backbone',
	'underscore',
	'libs/utility'
], function(
	Backbone,
	_,
	Utility
) {
	return Backbone.View.extend({
		tagName: 'div',
		is_fixed: false,
		initialize: function(options) {
			this.create(options.html);
		},
		events: {
		},

		create: function(html) {
console.log('created', html);
			this.$el.append(html);
			this.$el.addClass('scheduled-content-entry');
		},
		moveTo: function(x, y) {
console.log('move_to', x, y);
			this.$el.css({
				top: y,
				left: x
			});
		},
		fix: function() {
console.log('fixed');
			this.is_fixed = true;
		},
		destroy: function() {
console.log('destroyed');
		}
	});
});
