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
			this.template = _.template($('#tpl-user-entry').html());

			// 목록 로드 후에 operator_id값이 변경되는 이벤트 발생시
			// render 함수를 재호출함. (operator가 나중에 로드되기 때문에)
			this.listenTo(this.model, 'change:operator_id', this.render);

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
			window.location.href = 'user_mgmt_form.html#mode/info/id/' + this.model.id;
		},
		clickEditButton: function() {
			window.location.href = 'user_mgmt_form.html#mode/edit/id/' + this.model.id;
		},
		clickDeleteButton: function() {
			if(confirm('Do you really want to delete the user "' + this.model.get('first_name') + ' ' + this.model.get('last_name') + '"?')) {
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
