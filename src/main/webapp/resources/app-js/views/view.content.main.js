define([
	'backbone',
	'underscore',
	'libs/utility',
	'toastr',

	'views/view.content.content.table',
	'views/view.pagination'
], function(
	Backbone,
	_,
	Utility,
	toastr,

	View_Content_Content_Table,
	View_Pagination
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.pagination_content = options.pagination_content;

			this.listenTo(this.collection, 'searchoptionchanged', this.searchOptionChanged);
			this.listenTo(this.collection, 'destroy', this.modelDestroyed);
			this.listenTo(this.pagination_content, 'change', this.renderPagination);

			this.render();
		},
		events: {
			'click #go-search': 'goSearch',
			'click #btn-add-content': 'clickAddContentButton'
		},
		render: function() {
			new View_Content_Content_Table({
				collection: this.collection
			});

			return this;
		},
		renderPagination: function() {
			new View_Pagination({
				model: this.pagination_content
			});
		},

		goSearch: function() {
			var column = this.$el.find('select#search-column').val(),
				value = this.$el.find('input#search-keyword').val();

			var router = Utility.Context.get('router');

			if(column && value) {
				router.navigate('column/' + column + '/value/' + value, {trigger: true});
			} else {
				router.navigate('page/1', {trigger: true});
			}
		},
		searchOptionChanged: function(column, value) {
			this.$el.find('select#search-column').val(column);
			this.$el.find('input#search-keyword').val(value);
		},
		clickAddContentButton: function() {
			window.location.href = 'contents_mgmt_form.html#mode/create';
		},
		modelDestroyed: function() {
			var router = Utility.Context.get('router');

			router.navigate('page/1', {replace: true});
			router.fetchContentsByPage(1);
		}
	});
});
