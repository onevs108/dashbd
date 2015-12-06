define([
	'backbone',
	'underscore',
	'libs/utility',

	'views/view.schedule_management_eepg_table.content.table.entry'
], function(
	Backbone,
	_,
	Utility,

	View_Schedule_Management_EEPG_Table_Content_Table_Entry
) {
	return Backbone.View.extend({
		el: 'div#epg-table',
		initialize: function() {
			this.scheduled_content_entry = null;
		},
		events: {
			'dragenter': 'dragEnter',
			'dragleave': 'dragLeave',
			'dragover': 'dragOver',
			'drop': 'drop'
		},

		dragEnter: function(e) {
			this.scheduled_content_entry_coords = {x: 0, y: 0};
			this.scheduled_content_entry = new View_Schedule_Management_EEPG_Table_Content_Table_Entry({
				html: e.originalEvent.dataTransfer.getData('text/html')
			});
			this.$el.append(this.scheduled_content_entry.el);
		},
		dragLeave: function(e) {
			if(this.scheduled_content_entry && !this.scheduled_content_entry.is_fixed) {
				this.scheduled_content_entry.destroy();
			}
		},
		dragOver: function(e) {
			e.preventDefault();

			var data_transfer = e.originalEvent.dataTransfer,
				new_x = e.originalEvent.x,
				new_y = e.originalEvent.y;

			data_transfer.dropEffect = 'move';

			if(this.scheduled_content_entry && this.scheduled_content_entry_coords.x !== new_x && this.scheduled_content_entry_coords.y !== new_y) {
				this.scheduled_content_entry_coords = {x: new_x, y: new_y};
				this.scheduled_content_entry.moveTo(new_x, new_y);
			}

			return false;
		},
		drop: function(e) {
			e.preventDefault();
			e.stopPropagation();

			e.originalEvent.dataTransfer.dropEffect = 'move';
		},

		render: function() {


			return this;
		}
	});
});
