define([
	'backbone',
	'underscore',
	'libs/utility',
	'toastr',

	'views/view.user.user.table',
	'views/view.pagination'
], function(
	Backbone,
	_,
	Utility,
	toastr,

	View_User_User_Table,
	View_Pagination
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.operator = options.operator;
			this.pagination_user = options.pagination_user;

			this.listenTo(this.collection, 'searchoptionchanged', this.searchOptionChanged);
			this.listenTo(this.collection, 'destroy', this.modelDestroyed);
			this.listenTo(this.pagination_user, 'change', this.renderPagination);

			this.render();
		},
		events: {
			'click #go-search': 'goSearch',
			'click #btn-add-user': 'clickAddUserButton'
		},
		render: function() {
			new View_User_User_Table({
				collection: this.collection,
				operator: this.operator
			});

			return this;
		},
		renderPagination: function() {
			new View_Pagination({
				model: this.pagination_user
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
		clickAddUserButton: function() {
			window.location.href = 'user_mgmt_form.html#mode/create';
		},
		modelDestroyed: function() {
			var router = Utility.Context.get('router');

			router.navigate('page/1', {replace: true});
			router.fetchUsersByPage(1);
		}
	});
});
