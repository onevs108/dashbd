define([
	'backbone',

	'views/view.service_area.enb_list_entry'
], function(
	Backbone,

	View_Service_Area_Enb_List_Entry
) {
	return Backbone.View.extend({
		el: 'ul#enb_ap_in_service_area',
		initialize: function() {
			this.service_area_id = parseInt(this.collection.service_area_id);

			this.render();
		},
		render: function() {
			var service_area_id = this.service_area_id;

			// clean enb list
			this.$el.empty();

			_.chain(this.collection.models)
				.filter(function(model) {
					return service_area_id === model.get('id');
				})
				.each(function(model) {
					this.$el.append((new View_Service_Area_Enb_List_Entry({
						model: model
					})).el);
				}.bind(this));

			return this;
		},

		clearEnbList: function() {

		}
	});
});
