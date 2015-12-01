define([
	'backbone'
], function(
	Backbone
) {
	return Backbone.Model.extend({
		url: '/dashbd/api/content.do',
		initialize: function() {
			// running_time 구하기
			var duration = parseInt(this.get('duration'));

			if(duration && duration > 0) {
				this.set('running_time', (new Date(parseInt(this.get('duration')) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0]);
			}
		},
		attribute_name_map: {
			'id': 'form-id',
			'type': 'form-type',
			'title': 'form-title',
			'category': 'form-category',
			'director': 'form-director',
			'actors': 'form-actors',
			'content_provider': 'form-content-provider',
			'age_restriction': 'form-age_restriction',
			'description': 'form-description',
			'file_format': 'form-file-format',
			'duration': 'form-duration',
			'bitrate': 'form-bitrate',
			'url': 'form-url'
		},
		convert_attribute_name_to: function(attribute_name) {
			return this.attribute_name_map[attribute_name];
		}
	});
});
