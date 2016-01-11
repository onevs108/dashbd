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
    
    $('#bmsc_down').change(function(){
    	getServiceAreaByBmScId($('#bmsc_down option:selected').val());
    });
    
    $('#excelFile').change(function(){
        $('#selectedExcelFile').val( $('#excelFile').val().substring( $('#excelFile').val().lastIndexOf("\\") + 1 ) );
    });
    
    $('#uploadExcel').click( function(event) {
    	if( isEmpty( $('#operator option:selected').val() ) ) {
    		//swal({
                //title: "Sorry !",
                //text: "Please Select Operator first."
            //});
    		alert( "Please Select Operator first." );
    		return;
    	} else if( isEmpty( $('#bmsc option:selected').val() ) ) {
    		//swal({
                //title: "Sorry !",
                //text: "Please Select BMSC first."
            //});
    		alert( "Please Select BMSC first." );
    		return;
    	} else if( isEmpty( $('#excelFile').val() ) ) {
    		//swal({
                //title: "Sorry !",
                //text: "Please Select Upload File first."
            //});
    		alert( "Please Select Upload File first." );
    		return;
    	} else {
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
                	$("#ajax").remove();
                    var data = JSON.parse(responseData);
                    alert( data.count + "건 등록 완료");
                    
                	//swal({
                        //title: "Sorry !",
                        //text: "Please Select BMSC first."
                    //});
                }
            });
    	}
    	
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

function getServiceAreaByBmScId(bmscId) {
	
	$.ajax({
        url : "/dashbd/api/getSeviceAreaByBmScId.do",
        type: "get",
        data : { "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var datas = JSON.parse(responseData);
            var dataLen = datas.length;
            var options = "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"5\">";
            options += "<thead>";
            options += "<tr>";
			options += "<th></th>";
			options += "<th>SA_ID</th>";
			options += "<th>Description</th>";
			options += "</tr>";
			options += "</thead>";
			options += "<tbody>";
			
            for( var i = 0; i < dataLen; i++ ) {
            	options += "<tr>";
				options += "<td><input type=\"checkbox\" name=\"serviceAreaIds\" id=\"serviceAreaIds\" value=\"" + datas[i].serviceAreaId + "\"></td>";
				options += "<td>";
				options += datas[i].serviceAreaId;
				options += "</td>";
				options += "<td>";
				options += datas[i].serviceAreaName;
				options += "</td>";
				options += "</tr>";
            }

            options += "</tbody>";
            
            if( dataLen > 10 ) {
            	options += "<tfoot><tr><td colspan=\"3\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
			options += "</table>";
			options += "<div class=\"row \">";
			options += "<div class=\"col-md-12\">";
			options += "<button class=\"btn btn-block btn-sm btn-default\" type=\"submit\" id=\"downloadSA\" onclick=\"javascript:downloadENBsByServiceAreaId( document.downloadForm );\"><strong>Download eNB for Selected SA</strong></button>";
			options += "</div>";
			options += "<div class=\"col-md-12\">";
			options += "<button class=\"btn btn-block btn-sm btn-default\" type=\"submit\" id=\"downloadBmsc\" onclick=\"javascript:downloadENBs( document.downloadForm );\"><strong>Download eNB for BMSC</strong></button>";
			options += "</div>";
			options += "</div>";
            
			$("#svcTotalCount").empty();
            $("#svcTotalCount").append(dataLen);
			
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
        }
    });
}

function downloadENBs( form ) {

	if( isEmpty( $('#operator_down option:selected').val() ) ) {
		//swal({
            //title: "Sorry !",
            //text: "Please Select Operator first."
        //});
		alert( "Please Select Operator first." );
		return;
	} else if( isEmpty( $('#bmsc_down option:selected').val() ) ) {
		//swal({
            //title: "Sorry !",
            //text: "Please Select BMSC first."
        //});
		alert( "Please Select BMSC first." );
		return;
	} else {
		form.action = "/dashbd/api/downloadENBs.do";
		form.submit();
		
		/*
		$.ajax({
	        url : "/dashbd/api/downloadENBs.do",
	        type: "post",
	        data : { "operatorId" : $('#operator_down option:selected').val(), "bmscId" : $('#bmsc_down option:selected').val() },
	        dateType : 'json',
	        success : function(responseData){
	        	
	        },
	        error : function(xhr, status, error){

	        }
	    });
	    */
	}
}

function downloadENBsByServiceAreaId(form) {
	
	if( isEmpty( $('#operator_down option:selected').val() ) ) {
		//swal({
            //title: "Sorry !",
            //text: "Please Select Operator first."
        //});
		alert( "Please Select Operator first." );
		return;
	} else if( isEmpty( $('#bmsc_down option:selected').val() ) ) {
		//swal({
            //title: "Sorry !",
            //text: "Please Select BMSC first."
        //});
		alert( "Please Select BMSC first." );
		return;
	/*
	} else if( isEmpty( $('#bmsc_down option:selected').val() ) ) {
		//swal({
            //title: "Sorry !",
            //text: "Please Select BMSC first."
        //});
		alert( "Please Select BMSC first." );
		return;
	*/
	} else {
		form.action = "/dashbd/api/downloadENBsByServiceAreaId.do";
		form.submit();
		/*
		var checkboxValues = [];
	    $("input[name='serviceAreaIds']:checked").each(function(i) {
	        checkboxValues.push($(this).val());
	    });
	    
	    //$( '#downloadForm' ).attr( 'action', '/dashbd/api/downloadENBsByServiceAreaId.do' ).submit();
	    
		$.ajax({
	        url : "/dashbd/api/downloadENBsByServiceAreaId.do",
	        type: "post",
	        data : { "operatorId" : $('#operator_down option:selected').val(), "bmscId" : $('#bmsc_down option:selected').val(), "serviceAreaIds" : checkboxValues },
	        //dateType : 'json',
	        async: true,
            traditional: true,
            cache: false,
            contentType: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
	        success : function(responseData){
	        	
	        },
	        error : function(xhr, status, error){

	        }
	    });
	    */
	}
	
}

function isEmpty(value) {
    return (value === undefined || value == null || value.length <= 0) ? true : false;
}
