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
		tagName: 'tr',
		initialize: function() {
			// template을 initialize시에 compile함.
			this.template = _.template($('#tpl-content-entry').html());

			this.render();
		},
		events: {
			'click .button-info': 'clickInfoButton',
			'click .button-edit': 'clickEditButton',
			'click .button-delete': 'clickDeleteButton'
		},
		render: function() {
			this.$el.html(this.template(this.model.attributes));

			return this;
		},

		clickInfoButton: function() {
			window.location.href = 'contents_mgmt_form.html#mode/info/id/' + this.model.id;
		},
		clickEditButton: function() {
			window.location.href = 'contents_mgmt_form.html#mode/edit/id/' + this.model.id;
		},
		clickDeleteButton: function() {
			if(confirm('Do you really want to delete this content?')) {
				this.model.destroy({
					wait: true,
					requestType: 'delete',
					data: 'id=' + this.model.id,
					success: function() {
						toastr.success('record deleted.');
					},
					error: function() {
						toastr.error('failed to delete record.');
					}
				});
			}
		}
	});
});
