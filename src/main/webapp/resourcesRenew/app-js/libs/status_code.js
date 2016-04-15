define([
	'toastr'
], function(
	toastr
) {
	return {
		400: function() {
			toastr.error('bad request.');
		},
		401: function() {
			toastr.error('not authorized or have no permission.');
		},
		404: function() {
			toastr.warning('record not found.@@@@@');
		}
	};
});
