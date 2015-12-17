
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
		//alert( $('#operator option:selected').val() );
		$.ajax({
	        url : "/dashbd/api/serviceAreaBmScByOperator.do",
	        type: "get",
	        //data : { "operatorId" : $('#operator option:selected').val() },
	        data : { "operatorId" : 1 },
	        success : function(responseData){
	            $("#ajax").remove();
	            var data = JSON.parse(responseData);
	            var dataLen = data.length;
                var options = "";
                for(var i=0; i<dataLen; i++){
                	options += '<li><a href="' + data[i].id + '">' + data[i].name + '</a></li>';
                }

	            $("#bmsc").append(options);
	        }
	    });
	});
});