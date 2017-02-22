$(document).ready(function()
{
	setDefaultQCI_Level_SegmentAvailableOffset();
	displayRatio();
	
	$("#serviceType").on("change", function() {
		
		if($(this).val() == "fileDownload"){
			$("div[name='bcType_fileDownload']").show();
			$("#bcType_fileDownload2").show();
			$("#bcType_streaming").hide();
			$("div[name='bcType_streaming2']").hide();
			$("div[name='contentStartStop']").show();
			$("#interval").hide();
			$("div[name='contentPlus']").show();
			if($("#addServiceArea").length == 0){
				$("#serviceAreaRow").append($("#serviceArea").render());
				addServiceAreaEvent(0);
			}
		}else if($(this).val() == "carouselMultiple"){
			$("#bcType_fileDownload").show();
			$("#bcType_streaming").hide();
			$("div[name='bcType_streaming2']").hide();
			$("div[name='contentStartStop']").hide();
			$("#bcType_fileDownload2").hide();
			$("#interval").show();
			$("div[name='contentPlus']").show();
			if($("#addServiceArea").length == 0){
				$("#serviceAreaRow").append($("#serviceArea").render());
				addServiceAreaEvent(0);
			}
		}else if($(this).val() == "carouselSingle"){
			$("#bcType_fileDownload").show();
			$("#bcType_streaming").hide();
			$("div[name='bcType_streaming2']").hide();
			$("div[name='contentStartStop']").hide();
			$("#bcType_fileDownload2").hide();
			$("#interval").hide();
			$("div[name='contentPlus']").hide();
			if($("#addServiceArea").length == 0){
				$("#serviceAreaRow").append($("#serviceArea").render());
				addServiceAreaEvent(0);
			}
		}else{
			$("div[name='bcType_fileDownload']").hide();
			$("#bcType_fileDownload").hide();
			$("#bcType_fileDownload2").hide();
			$("#bcType_streaming").show();
			$("div[name='bcType_streaming2']").show();
			$("div[name='contentStartStop']").hide();
			$("#interval").hide();
			$("#addServiceArea").remove();
			addServiceAreaEvent(0);
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
	
	var saidListDiv = $('#saidListDiv');
	
	$("#saidListDiv").on('click', '.cRemSaid', function() {
		$(this).parents('p').remove();
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
				bmscId : $("#bmscId").val(),
				serviceType : $( "input[name='serviceType']" ).val()
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
	
	$("#btnOK").click(function() {
		var saidList = "";
		if($("#serviceType").val() == "streaming") {
			var saidListLength = $("input[name='saidList']").length;
			if(saidListLength > 1){
				for (var i = 0; i < saidListLength; i++) {
					if(i < saidListLength-1){
						saidList += $($("input[name='saidList']")[i]).val() + ",";
					}else{
						saidList += $($("input[name='saidList']")[i]).val();
					}
				}
			}else{
				saidList = $("input[name='saidList']")[0].value;
			}
		}else{
			saidList = $("input[name='saidList']")[0].value;
		}
		
		$.ajax({
			type : "POST",
			url : "checkBandwidth.do",
			dataType : "json",
			data : {saidList: saidList, bandwidth: $("#GBR").val()},
			async : false,
			success : function( data ) {
				if(data.result == "SUCCESS")
				{
					$("#frmScheduleReg").submit();
				}
				else if(data.result == "bwExceed")
				{
					swal({
		                title: "Warn !",
		                text: "exceed bandwidth said=" + data.resultObj.said
		            });
				}
				else 
				{
					swal({
		                title: "Warn !",
		                text: "Not exist said= " + data.resultObj
		            });
				}
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	});
	
	addScheduleRemoveEvent();
	addContentRemoveEvent();
});


function addServiceAreaEvent(idx){
	$($("button[name='mapAdd']")[idx]).click(function(){
		$("#circleCiryPop").modal();
	})
	
	$($("button[name='saidAdd']")[idx]).click(function(e) {
		
		var said = $($("input[name='said']")[idx]).val();
		var saidDefault = $($("input[name='saidDefault']")[idx]).val();
		
		if (saidDefault == said){
			alert ('this said is default.other said input.');
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "checkExistSaid.do",
			dataType : "json",
			data : {said: said},
			async : false,
			success : function( data ) {
				if(data.result == "SUCCESS")
				{
					var saidListValue = "";
					if($($("input[name='saidList']")[idx]).val() == ""){
						saidListValue = said;
					}else{
						saidListValue = $($("input[name='saidList']")[idx]).val()+","+said;
					}
					$($("input[name='saidList']")[idx]).val(saidListValue);
					
					$($("input[name='said']")[idx]).val("");
				}else{
					swal({
		                title: "Warn !",
		                text: "SAID-"+said+" is not exist" 
		            });
				}
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	});
}

function addScheduleRemoveEvent(){
	$(".close-schedule").click(function(e){
		if($(".close-schedule").length == 1){
			return;
		}
		var idx = $(".close-schedule").index(e.target.parentNode);
		$($(".close-schedule")[idx]).parent().parent().remove();
	});
}

function addContentRemoveEvent(){
	$(".close-content").click(function(e){
		var ctIdx = $("div[name='content']").index($(e.target).parents("div[name='content']"));
		var idx = $($("div[name='content']")[ctIdx]).find(".close-content").index(e.target.parentNode);
		if($($("div[name='content']")[ctIdx]).find(".close-content").length == 1){
			return;
		}
		$($($("div[name='content']")[ctIdx]).find(".schedule-list")[idx]).remove();
	});
}

function valadationCheck(){
	if ($("#fecType").val()== 'Raptor' && $("#fecRatio").val() == '0'){
		alert('0 is not allowed for ratio.')
		return false;
	}
	
	if ($("#serviceType").val() == 'streaming' && $("#segmentAvailableOffset").val() == ""){
		alert("plz, type in segmentAvailableOffset value");
		return false;
	}
	
	var s_start = $("#schedule_start").val().replace(/[^0-9]/g,'');
	var s_stop = $("#schedule_stop").val().replace(/[^0-9]/g,'');
	var d_start = $("#deliveryInfo_start").val().replace(/[^0-9]/g,'');
	var d_end = $("#deliveryInfo_end").val().replace(/[^0-9]/g,'');
	
	//console.log(s_start, '+', s_stop, '+',d_start, '+',d_end, '+');
	
	if (d_start < s_start ){
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

	if ($("#segmentAvailableOffset").val() == '')
		$("#segmentAvailableOffset").val(10);
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