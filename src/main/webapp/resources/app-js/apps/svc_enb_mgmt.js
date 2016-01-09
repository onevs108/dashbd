$(document).ready(function()
{
    $('#operator').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val(), true);
    });
    
    $('#operator_down').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator_down option:selected').val(), false);
    });
    
    $('#uploadExcel').click(function(event){
    	alert('asdfasdfas');
    	var formData = new FormData();
    	formData.append('operator', $('#operator option:selected').val());
    	formData.append('bmsc', $('#bmsc option:selected').val());
    	formData.append('excelFile', $('input[type=file]')[0].files[0]); 

        $.ajax({
            url : '/dashbd/api/uploadENBs.do',
            data : formData,
            type : 'POST',
            processData: false,
            contentType: false,
            success : function(responseData){
            	swal({
                    title: "Sorry !",
                    text: "Please Select BMSC first."
                });
            }
        });
   });
});

//BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId, isUpload)
{	
	$.ajax({
        url : "/dashbd/api/serviceAreaBmScByOperator.do",
        type: "get",
        data : { "page" : page, "operatorId" : operatorId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value=""></option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }
            
            if( isUpload ) {
            	$("#bmsc").empty();
                $("#bmsc").append(options);
            } else {
            	$("#bmsc_down").empty();
                $("#bmsc_down").append(options);
            }
            
        }
    });
}

