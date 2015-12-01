define([
	'backbone',
	'underscore',
	'libs/utility',

	'views/view.schedule_management_eepg_table.content.search.result'
], function(
	Backbone,
	_,
	Utility,

	View_Schedule_Management_EEPG_Table_Content_Search_Result
) {
	return Backbone.View.extend({
		el: 'ul#content-entries',
		initialize: function(options) {
			this.listenTo(this.collection, 'reset', this.render);
		},
		events: {

		},
		render: function() {
			// 기존 렌더링된 entry들을 삭제
			this.$el.empty();

			this.collection.each(function(model) {
				this.$el.append((new View_Schedule_Management_EEPG_Table_Content_Search_Result({
					model: model
				})).el);
			}, this);

			return this;
		}
	});
});
