define([
	'backbone',
	'underscore',
	'libs/utility',

	'views/view.content.content.entry'
], function(
	Backbone,
	_,
	Utility,

	View_Content_Content_Entry
) {
	return Backbone.View.extend({
		el: 'table#content-table',
		initialize: function(options) {
			this.$tbody = this.$el.find('tbody');

			this.listenTo(this.collection, 'reset', this.render);
		},
		events: {

		},
		render: function() {
			// 기존 렌더링된 entry들을 삭제
			this.$tbody.empty();

			this.collection.each(function(model) {
				this.$el.append((new View_Content_Content_Entry({
					model: model
				})).el);
			}, this);

			return this;
		}
	});
});
