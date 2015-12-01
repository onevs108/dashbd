define([
	'jquery',
	'backbone',
	'underscore',
	'libs/utility',
	'toastr'
], function(
	$,
	Backbone,
	_,
	Utility,
	toastr
) {
	return Backbone.View.extend({
		tagName: 'li',
		initialize: function() {
			// initialize시에 template을 compile함.
			this.template = _.template($('#tpl-content-entry').html());

			this.render();
		},
		events: {
			'dragstart': 'dragStart',
			'dragend': 'dragEnd'
		},

		dragStart: function(e) {
			var data_transfer = e.originalEvent.dataTransfer;

			data_transfer.effectAllowed = 'move';
			data_transfer.setData('text/html', this.$el.html());
		},
		dragEnd: function(e) {
		},

		render: function() {
			this.$el.html(this.template(this.model.attributes));
			this.$el.attr('draggable', true);

			return this;
		}
	});
});
