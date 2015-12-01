define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.View.extend({
		tagName: 'li',
		initialize: function() {
			this.render();
		},
		events: {
			'click': 'entryClicked'
		},
		template: _.template('<a href="#" onclick="return false;"><%= enb_ap_name %></a>'),
		render: function() {
			this.$el.html(this.template(this.model.attributes));

			return this;
		},

		clearEnbList: function() {

		},
		entryClicked: function() {
			this.model.select();
			this.model.trigger('clicked', this.model);
		}
	});
});
