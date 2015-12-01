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
		events: {
			'click #btn-cancel': function() {
				window.location.reload();
			},
			'click #btn-save': 'saveServiceArea',
			'submit form': function(e) {
				e.preventDefault();
				return false;
			}
		},

		saveServiceArea: function() {
			var data = {
					name: this.$el.find('#form-name').val(),
					bandwidth: this.$el.find('#form-bandwidth').val(),
					city: this.$el.find('#form-city').val()
				};

			// TODO validate

			this.model.set(data);
			this.model.save(null, {
				success: function(model, response) {
					window.location.href = 'service_area.html#service_area/' + response.result.id + '/level/success/message/' + encodeURIComponent('service area created.');
				},
				statusCode: Status_Code
			});
		}
	});
});
