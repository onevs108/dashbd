
/*
var $operator = $('operator');

// cleaning operator
$operator.empty();
$operator.append('<option value="">Select one</option>');

$.each(function(model) {
	$operator.append('<option value="' + model.id + '">' + model.get('name') + '</option>');
});
*/

$( document ).ready(function() {
	$('#operator').change(function(){
		alert( $('#operator option:selected').val() );
	});
});