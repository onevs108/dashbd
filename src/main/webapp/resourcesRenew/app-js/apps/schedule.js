$(document).ready(function()
{
	setDefaultQCI_Level_SegmentAvailableOffset();
	displayRatio();
	
	$("#serviceType").on("change", function() {
		//alert($(this).val());
		if ( $(this).val() == "fileDownload" ) {
			//filedownload
			$("#bcType_fileDownload").show();
			$("#bcType_fileDownload2").show();
			$("#bcType_streaming").hide();
			$("#bcType_streaming2").hide();
		}else{
			$("#bcType_fileDownload").hide();
			$("#bcType_fileDownload2").hide();
			$("#bcType_streaming").show();
			$("#bcType_streaming2").show();
		}
	});
	
	$("#fecType").on("change", function() {
		displayRatio();
	});
	
	$("#FileRepair").on("change", function() {
		if(this.checked){
			$("#frOffsetTime").prop('disabled', false);
			$("#frRandomTime").prop('disabled', false);
			
		}else{
			$("#frOffsetTime").prop('disabled', true);
			$("#frRandomTime").prop('disabled', true);
		}
	});
	
	
	$("#receptionReport").on("change", function() {
		if(this.checked){
			$("#offsetTime").prop('disabled', false);
			$("#samplePercentage").prop('disabled', false);
			$("#reportType").prop('disabled', false);
			$("#randomTime").prop('disabled', false);
			changePercentage();
		}else{
			$("#offsetTime").prop('disabled', true);
			$("#samplePercentage").prop('disabled', true);
			$("#reportType").prop('disabled', true);
			$("#randomTime").prop('disabled', true);
			$("#samplePercentage").val("");
		}
	});
	
	
		
	$("#btnCancel").click(function() {
		var tmpServiceAreaId = $("#serviceAreaId").val();
		var searchDate = $("#searchDate").val();
		var bmscId= $("#bmscId").val();
		location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId;
	});
		
	$("#btnDelete").click(function() {
		if (!confirm("It will be deleted. do you want this??"))
			return;
		var tmpServiceAreaId = $("#serviceAreaId").val();
		var searchDate = $("#searchDate").val();
		var bmscId= $("#bmscId").val();
		
		var param = {
				id : $("#id").val(),
				BCID : $("#BCID").val(),
				bmscId : $("#bmscId").val()
			};
		$.ajax({
			type : "POST",
			url : "delSchedule.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				outMsgForAjax(data);
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId;
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	});
		
	$("#frmScheduleReg").ajaxForm({
		dataType : "json",
		beforeSubmit : function(data, frm, opt) {
			
			if (!valadationCheck())
				return false;
			if (!confirm('are you sure?')) {
				return false;
			}
			
		},
		success : function(result) {
			outMsgForAjax(result);
			var tmpServiceAreaId = $("#serviceAreaId").val();
			var searchDate = $("#searchDate").val();
			var bmscId= $("#bmscId").val();
			location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId;
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
});
	
function valadationCheck(){
	if ($("#fecType").val()== 'Raptor' && $("#fecRatio").val() == '0'){
		alert('0 is not allowed for ratio.')
		return false;
	}
	
	var s_start = $("#schedule_start").val().replace(/[^0-9]/g,'');
	var s_stop = $("#schedule_stop").val().replace(/[^0-9]/g,'');
	
	var d_start = $("#deliveryInfo_start").val().replace(/[^0-9]/g,'');
	var d_end = $("#deliveryInfo_end").val().replace(/[^0-9]/g,'');
	
	console.log(s_start, '+', s_stop, '+',d_start, '+',d_end, '+');
	
	
	
	if (d_start > s_start ){
		alert("It can not be 'content start time' over than 'schedule start time' ");
		return false;
	}
	
	if (d_end > s_stop ){
		alert("It can not be 'content start stop time' over than 'schedule Stop time' ");
		return false;
	}
	
	return true;
	
}
function validation( from ) {
	var $form = from;
	var retFlag = true;
	console.log('here0');
	$form.find("input").each(function(idx) {
		if (retFlag && $(this).attr("required")) {
			console.log('here1');
			if ($(this).val() == '') {
				console.log('here2');
				alert($(this).attr("alt") + '필수항목 입니다.');
				retFlag = false;
				
			}
		}
	});
	return retFlag;
}

function displayRatio(){
	if ($("#fecType").val()== 'NoFEC'){
		$("#fecRatio").prop('disabled', true);
		$("#fecRatio").val("");
	}else{
		$("#fecRatio").prop('disabled', false);
	}
}

function setDefaultQCI_Level_SegmentAvailableOffset(){
	if ($("#QCI").val() == '')
		$("#QCI").val(1);
	
	if ($("#level").val() == '')
		$("#level").val(1);

	if ($("#SegmentAvailableOffset").val() == '')
		$("#SegmentAvailableOffset").val(10);
}

function changePercentage(){
	if ($("#reportType").val()== 'RAck'){
		$("#samplePercentage").prop('disabled', true);
		$("#samplePercentage").val("100");
	}else{
		$("#samplePercentage").prop('disabled', false);
		$("#samplePercentage").val("");
	}
}