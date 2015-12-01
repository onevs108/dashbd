define([
	'backbone',
	'underscore',
	'libs/utility',
	'toastr',

	'views/view.schedule_management_eepg_table.content.table',
	'views/view.schedule_management_eepg_table.content.search'
], function(
	Backbone,
	_,
	Utility,
	toastr,

	View_Schedule_Management_EEPG_Table_Content_Table,
	View_Schedule_Management_EEPG_Table_Content_Search
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.listenTo(this.collection, 'searchoptionchanged', this.searchOptionChanged);

			this.render();
		},
		events: {
			'click #go-search': 'goSearch',
			'submit form': function(e) {
				e.preventDefault();
				return false;
			}
		},
		render: function() {
			new View_Schedule_Management_EEPG_Table_Content_Search({
				collection: this.collection
			});

			new View_Schedule_Management_EEPG_Table_Content_Table();

			return this;
		},

		goSearch: function() {
			var category = this.$el.find('input#form-category').val(),
				content_provider = this.$el.find('input#form-content-provider').val(),
				title = this.$el.find('input#form-title').val();

			var router = Utility.Context.get('router');

			var hash = [];

			if(category) {
				hash.push('category/' + category);
			}

			if(content_provider) {
				hash.push('content_provider/' + content_provider);
			}

			if(title) {
				hash.push('title/' + title);
			}

			Utility.Context.get('router').navigate(hash.join('/'), {trigger: true});
		},
		searchOptionChanged: function(category, content_provider, title) {
			this.$el.find('input#form-category').val(category);
			this.$el.find('input#form-content-provider').val(content_provider);
			this.$el.find('input#form-title').val(title);
		}
	});
});
