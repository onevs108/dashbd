$(document).ready(function()
{
	setDefaultQCI_Level_SegmentAvailableOffset();
	displayRatio();
	
	$("#serviceType").on("change", function() {
		//alert($(this).val());
		if ( $(this).val() == "fileDownload" ) {
			//filedownload
			$("#bcType_fileDownload").show();
			$("#bcType_streaming").hide();
			$("#bcType_streaming2").hide();
		}else{
			$("#bcType_fileDownload").hide();
			$("#bcType_streaming").show();
			$("#bcType_streaming2").show();
		}
	});
	
	$("#fecType").on("change", function() {
		displayRatio();
	});
		
	$("#btnCancel").click(function() {
		var tmpServiceAreaId = $("#serviceAreaId").val();
		var searchDate = $("#searchDate").val();
		location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate;
	});
		
	$("#btnDelete").click(function() {
		if (!confirm("It will be deleted. do you want this??"))
			return;
		var tmpServiceAreaId = $("#serviceAreaId").val();
		var searchDate = $("#searchDate").val();
		
		var param = {
				id : $("#id").val(),
				BCID : $("#BCID").val()
			};
		$.ajax({
			type : "POST",
			url : "delSchedule.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				outMsgForAjax(data);
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate;
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
			location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate;
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
});
	
function valadationCheck(){
	if ($("#fecType").val()== 'Raptor' && $("#fecRatio").val() == '0'){
		alert('0 of Ratio value is NOT correct.')
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
		$("#fecRatio").val("0");
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
