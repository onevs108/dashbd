define([
	'backbone',
	'libs/utility'
], function(
	Backbone,
	Utility
) {
	return Backbone.View.extend({
		tagName: 'li',
		initialize: function() {
			this.listenTo(this.model, 'chosen', this.entryChosen);
			this.listenTo(this.model, 'unchosen', this.entryUnchosen);

			this.render();
		},
		events: {
			'click': 'entryClicked'
		},
		template: _.template('<a href="#" onclick="return false;"><%= bmsc_name %></a>'),
		render: function() {
			this.$el.html(this.template(this.model.attributes));

			// chosen?
			if(this.model.am_i_chosen()) {
				this.$el.find('a').addClass('active');
			} else {
				this.$el.find('a').removeClass('active');
			}

			return this;
		},

		entryClicked: function() {
			Utility.Context.get('router').navigate('operator/' + Backbone.history.getParam('operator', 1) + '/bmsc/' + this.model.id, {trigger: true});
		},
		entryChosen: function() {
			this.$el.find('a').addClass('active');
		},
		entryUnchosen: function() {
			this.$el.find('a').removeClass('active');
		}
	});
});
