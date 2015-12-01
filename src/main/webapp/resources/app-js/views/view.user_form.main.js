define([
	'backbone',
	'underscore',
	'libs/utility',
	'libs/status_code',
	'toastr'
], function(
	Backbone,
	_,
	Utility,
	Status_Code,
	toastr
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.operator = options.operator;

			this.listenTo(this.model, 'userfetched', this.showUserForm);
		},
		events: {
			'click #btn-cancel': function() {
				window.location.reload();
			},
			'click #btn-save': 'saveUser',
			'submit form': function(e) {
				e.preventDefault();
				return false;
			}
		},

		showUserForm: function(mode) {
			if(mode === 'info') {
				this._showUserInfoForm();
			} else if(mode === 'edit') {
				this._showUserEditForm();
			} else if(mode === 'create') {
				this._showUserCreateForm();
			}
		},
		saveUser: function() {
			var data = {
					operator_id: this.$el.find('#form-operator-id').val(),
					user_id: this.$el.find('#form-user-id').val(),
					password: this.$el.find('#form-password').val(),
					permission: this.$el.find('#form-permission').val(),
					first_name: this.$el.find('#form-first-name').val(),
					last_name: this.$el.find('#form-last-name').val(),
					department: this.$el.find('#form-department').val()
				};

			// TODO validate

			this.model.set(data);
			this.model.save(null, {
				success: function() {
					window.location.href = 'user_mgmt.html#level/success/message/' + encodeURIComponent('user created.');
				},
				statusCode: Status_Code
			});
		},
		_showUserEditForm: function() {
			// operator
			this.operator.each(function(operator) {
				this.$el.find('#form-operator-id').append('<option value="' + operator.id + '">' + operator.get('name') + '</option>');
			}, this);

			// fetched value
			_.each(this.model.attributes, function(value, key) {
				this.$el.find('#' + this.model.convert_attribute_name_to(key)).val(value);
			}, this);

			// "User Add" to name으로
			this.$el.find('div.ibox-title > h5').text('Edit ' + this.model.get('first_name') + ' ' + this.model.get('last_name'));
		},
		_showUserCreateForm: function() {
			// operator
			this.operator.each(function(operator) {
				this.$el.find('#form-operator-id').append('<option value="' + operator.id + '">' + operator.get('name') + '</option>');
			}, this);

			// create form에서는 다음의 항목을 숨긴다.
			// 1. registered date, modified date
			this.$el.find('#form-registered-date').parents('div.form-group').hide();
			this.$el.find('#form-modified-date').parents('div.form-group').hide();
		},
		_showUserInfoForm: function() {
			// operator
			this.operator.each(function(operator) {
				this.$el.find('#form-operator-id').append('<option value="' + operator.id + '">' + operator.get('name') + '</option>');
			}, this);

			// fetched value
			_.each(this.model.attributes, function(value, key) {
				this.$el.find('#' + this.model.convert_attribute_name_to(key)).val(value);
			}, this);

			// "User Add" to name으로
			this.$el.find('div.ibox-title > h5').text(this.model.get('first_name') + ' ' + this.model.get('last_name'));

			// info form에서는 다음의 항목을 숨긴다.
			// 1. password, confirm-password
			// 2. cancel, save 버튼
			this.$el.find('input[type=password]').parents('div.form-group').hide();
			this.$el.find('div.action-btn-group').hide();
		}
	});
});
