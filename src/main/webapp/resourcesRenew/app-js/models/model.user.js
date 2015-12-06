define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		url: '/dashbd/api/user.do',
		attribute_name_map: {
			'id': 'id',
			'user_id': 'form-user-id',
			'operator_id': 'form-operator-id',
			'first_name': 'form-first-name',
			'last_name': 'form-last-name',
			'department': 'form-department',
			'permission': 'form-permission',
			'created_at': 'form-registered-date',
			'updated_at': 'form-modified-date'
		},
		convert_attribute_name_to: function(attribute_name) {
			return this.attribute_name_map[attribute_name];
		}
	});
});
