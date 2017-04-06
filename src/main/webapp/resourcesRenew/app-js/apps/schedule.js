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
			$("#serviceModeArea").hide();
			$("#fileUpload_F").show();
			addSearchContentEvent(0);
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
			$("#serviceModeArea").hide();
			$("#fileUpload_F").show();
			addSearchContentEvent(0);
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
			$("#serviceModeArea").hide();
			$("#fileUpload_F").show();
			addSearchContentEvent(0);
		}else{
			$("div[name='bcType_fileDownload']").hide();
			$("#bcType_fileDownload").hide();
			$("#bcType_fileDownload2").hide();
			$("#bcType_streaming").show();
			$("div[name='bcType_streaming2']").show();
			$("div[name='contentStartStop']").hide();
			$("#interval").hide();
			$("#serviceModeArea").show();
			$("#addServiceArea").remove();
			$("#fileUpload_F").remove();
			addServiceAreaEvent(0);
		}
		$("input[name='saidList']").val($("#serviceAreaId").val());
		$("input[name='bcSaidList']").val($("#serviceAreaId").val());
		$(":file").filestyle({buttonBefore: true});
		$(".bootstrap-filestyle > input").css("background-color", "white");
	});
	
	$("#serviceMode").on("change", function() {
		if($(this).val() == "SC")
		{
			$("#moodArea").show();
			$("#bcServiceArea").hide();
			$("#consumptionReport").hide();
		}
		else if($(this).val() == "MooD")	//Mood 일 때
		{
			$("#moodArea").show();
			$("#bcServiceArea").hide();
			$("#consumptionReport").show();
		}
		else
		{
			$("#moodArea").hide();
			$("#bcServiceArea").hide();
			$("#consumptionReport").hide();
		}
	});
	
	$("#addBcPattern").on("click", function() {
		$("div[name='bcPattern']").last().after($("#bcPatternArea").render());
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
			$("#reportType").prop('disabled', false);
			$("#samplePercentage").prop('disabled', false);
			$("#randomTime").prop('disabled', false);
		}else{
			$("#offsetTime").prop('disabled', true);
			$("#reportType").prop('disabled', true);
			$("#samplePercentage").prop('disabled', true);
			$("#randomTime").prop('disabled', true);
//			$("#samplePercentage").val("");
		}
		changePercentage();
	});
	
	$("#reportClientId").on("change", function() {
		if(this.checked){
			$("#moodReportInterval").prop('disabled', false);
			$("#moodOffsetTime").prop('disabled', false);
			$("#moodRandomTimePeriod").prop('disabled', false);
			$("#moodSamplePercentage").prop('disabled', false);
		}else{
			$("#moodReportInterval").prop('disabled', true);
			$("#moodOffsetTime").prop('disabled', true);
			$("#moodRandomTimePeriod").prop('disabled', true);
			$("#moodSamplePercentage").prop('disabled', true);
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
		location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId+"&type="+$("#type").val();
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
				type : $("#type").val(),
				serviceType : $( "input[name='serviceType']" ).val()
			};
		$.ajax({
			type : "POST",
			url : "delSchedule.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				outMsgForAjax(data);
//				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId+"&type="+$("#type").val();
				location.href = "schdMgmt.do";
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
//			location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate+"&bmscId="+bmscId+"&type="+$("#type").val();
			location.href = "schdMgmt.do";
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
	
	$("#btnOK,#btnUPDATE").click(function() {
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
					$("#serviceType").removeAttr("disabled");
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
	
	$("button[name='bcSaidAdd']").click(function(e) {
		
		var bcSaid = $("input[name='bcSaid']").val();
		var bcSaidDefault = $("input[name='saidDefault']").val();

		if (bcSaid == ""){
			alert ('please enter the said.');
			return;
		}
		
		if (bcSaidDefault == bcSaid){
			alert ('this bcSaid is default.other bcSaid input.');
			return;
		}
		//  bcS
		$.ajax({
			type : "POST",
			url : "checkExistSaid.do",
			dataType : "json",
			data : {said: bcSaid},
			async : false,
			success : function( data ) {
				if(data.result == "SUCCESS")
				{
					var saidListValue = "";
					if($("input[name='bcSaidList']").val() == ""){
						saidListValue = bcSaid;
					}else{
						saidListValue = $("input[name='bcSaidList']").val()+","+bcSaid;
					}
					$("input[name='bcSaidList']").val(saidListValue);
					
					$("input[name='bcSaid']").val("");
				}else{
					swal({
		                title: "Warn !",
		                text: "SAID-"+bcSaid+" is not exist" 
		            });
				}
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	});
	
	$("button[name='bcMapAdd']").click(function(){
		$("#popupType").val("mood");
		$("#circleCiryPop").modal();
	})
	
	$("#newClass").click(openServiceClass);
	
	addScheduleRemoveEvent();
	addContentRemoveEvent();
	$("#serviceType").change();
	$("#serviceMode").change();
	
	$("#uploadFile").click(function(){
		var fileName = $(".bootstrap-filestyle > input").val();
		var startIdx = fileName.indexOf(".")+1;
		var fileType = fileName.substring(startIdx).toLowerCase();
		if(fileType != "txt"){
			alert("please upload .txt file");
			return;
		}
		var form = $("#uploadFileForm")[0];
        var formData = new FormData(form);
        $.ajax({
           url: 'saidUpload.do',
           processData: false,
           contentType: false,
           data: formData,
           type: 'POST',
           success: function(data){
               setSaidFromFile(data.result);
           }
       });
	})
	
});

function setSaidFromFile(saidList) {
	var saidArray = saidList.split(",");
	for (var i = 0; i < saidArray.length; i++) {
		addSaidCheckFromFile(saidArray[i]);
	}
}

function addSaidCheckFromFile(said) {
	if(saidDefault == said) {
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
				if($("input[name='saidList']").val() == ""){
					saidListValue = said;
				}else{
					saidListValue = $("input[name='saidList']").val()+","+said;
				}
				$("input[name='saidList']").val(saidListValue);
				
				$("input[name='said']").val("");
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
}

function addSearchContentEvent(idx) {
	$($("button[name='searchContent']")[idx]).click(function(){
		$("#idx").val(idx);
		$("#type").val("fileDownload");
		$("#contentList").modal();
	})
}

function openServiceClass() {
	$("#serviceClassList").modal();
}

function getContentList() {
	$('#table').bootstrapTable('destroy');
	var pageNumber = 1;
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: 'getContentTable.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
//			params['pageNumber'] = (params.offset / params.limit) + 1;
			params['title'] = $("#form-title").val(),
			params['category'] = $("#form-category").val()
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'cid',
			title: 'cid',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: false
		},{
			field: 'title',
			title: 'Title',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<a onclick="setContentInfo('+row.cid+', \''+row.url+'\', '+row.duration+')">'+value+'</a>';
				return html;
			}
		}, {
			field: 'category',
			title: 'Category',
			width: '30%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true
		}, {
			field: 'duration',
			title: 'duration(sec)',
			width: '30%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}]
	});
}

function getServiceClassList() {
	$('#classTable').bootstrapTable('destroy');
	var pageNumber = 1;
	var table = $('#classTable').bootstrapTable({
		method: 'post',
		url: 'getServiceClassTable.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
//			params['pageNumber'] = (params.offset / params.limit) + 1;
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'class_name',
			title: 'Class Name',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input type="text" id="className'+row.id+'" class="form-control" value="'+value+'">';
				return html;
			}
		},{
			field: 'description',
			title: 'Description',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input type="text" id="description'+row.id+'" class="form-control" value="'+value+'">';
				return html;
			}
		}, {
			field: 'id',
			title: 'command',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<button type="button" class="btn btn-primary btn-sm" onclick="actionClass(\'edit\', '+row.id+')">Edit</button>&nbsp;';
					html += '<button type="button" class="btn btn-danger btn-sm" onclick="actionClass(\'delete\', '+row.id+')">Delete</button>';
				return html;
			}
		}]
	});
}

function getServiceIdList() {
	$('#idTable').bootstrapTable('destroy');
	var pageNumber = 1;
	var table = $('#idTable').bootstrapTable({
		method: 'post',
		url: 'getServiceIdTable.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
//			params['pageNumber'] = (params.offset / params.limit) + 1;
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,		
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'id_name',
			title: 'Id Name',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input type="text" id="idName'+row.id+'" class="form-control" value="'+value+'">';
				return html;
			}
		}, {
			field: 'description',
			title: 'Description',
			width: '40%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input type="text" id="description'+row.id+'" class="form-control" value="'+value+'">';
				return html;
			}
		}, {
			field: 'id',
			title: 'command',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<button type="button" class="btn btn-primary btn-sm" onclick="actionId(\'edit\', '+row.id+')">Edit</button>&nbsp;';
				html += '<button type="button" class="btn btn-danger btn-sm" onclick="actionId(\'delete\', '+row.id+')">Delete</button>';
				return html;
			}
		}],
	});
}

function openServiceIdModal() {
	$("#serviceIdList").modal();
}

function actionClass(type, id) {
	var className = $("#className"+id).val();
	var description = $("#description"+id).val();
	$.ajax({
		type : "POST",
		url : "actionServiceClass.do",
		data : {id: id, className: className, description: description, type: type},
		dataType : "json",
		success : function( data ) {
			if(data.result == "SUCCESS") {
				getServiceClassList();
				setServiceClassView();
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function actionId(type, id) {
	var idName = $("#idName"+id).val();
	var description = $("#description"+id).val();
	$.ajax({
		type : "POST",
		url : "actionServiceId.do",
		data : {id: id, idName: idName, description: description, type: type},
		dataType : "json",
		success : function( data ) {
			if(data.result == "SUCCESS") {
				getServiceIdList();
				setServiceIdView();
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function closeClassModal() {
	$("#serviceClassList").modal('hide');
}

function closeIdModal() {
	$("#serviceIdList").modal('hide');
}

function addClass() {
	if(!confirm("Do you want to create Service Class?")){
		return;
	}
	$.ajax({
		type : "POST",
		url : "insertServiceClass.do",
		data : {className: $("#className").val(), description: $("#description").val()},
		dataType : "json",
		success : function( data ) {
			if(data.result == "EXIST") {
				alert("Class name is already exist!");
			}else{
				alert("Service Class is created!");
				getServiceClassList();
				setServiceClassView();
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function addId() {
	if(!confirm("Do you want to create Service Id?")){
		return;
	}
	$.ajax({
		type : "POST",
		url : "insertServiceId.do",
		data : {idName: $("#idName").val(), idDescription: $("#idDescription").val()},
		dataType : "json",
		success : function( data ) {
			if(data.result == "EXIST") {
				alert("ID name is already exist!");
			}else{
				alert("Service Id is created!");
				getServiceIdList();
				setServiceIdView();
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function setServiceIdView() {
	$.ajax({
		type : "POST",
		url : "selectServiceIdAll.do",
		dataType : "json",
		success : function( data ) {
			var html = "";
			for (var i = 0; i < data.rows.length; i++) {
				html += "<option value='"+data.rows[i].id_name+"'>"+data.rows[i].id_name+"</option>";
			}
			$("#selectServiceId").html(html);
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function setServiceClassView() {
	$.ajax({
		type : "POST",
		url : "selectServiceClassAll.do",
		dataType : "json",
		success : function( data ) {
			var html = "";
			for (var i = 0; i < data.rows.length; i++) {
				html += "<option value='"+data.rows[i].class_name+"'>"+data.rows[i].class_name+"</option>";
			}
			$("#serviceClass").html(html);
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function setContentInfo(cid, url, duration){
	var idx = $("#idx").val();
	var type = $("#type").val();
	if(type == "streaming") {
		$("#contentSetId").val(cid);
		$("input[name='mpdURI']").val(url);
		$("#contentList").modal("hide");
	}
	else{
		$($("input[name='contentId']")[idx]).val(cid);
		$($("input[name='fileURI']")[idx]).val(url);
		$($("input[name='duration']")[idx]).val(duration);
		var scheduleStart;
		if(idx == 0){
			scheduleStart = $("#schedule_start").val();
		}else{
			scheduleStart = $($("input[name='deliveryInfo_end']")[idx-1]).val();
		}
		
		$($("input[name='deliveryInfo_start']")[idx]).val(getTimeAddSecond(scheduleStart, 15));
		var contentStart = $($("input[name='deliveryInfo_start']")[idx]).val();
		$($("input[name='deliveryInfo_end']")[idx]).val(getTimeAddSecond(contentStart, duration));
		var contentEnd = $($("input[name='deliveryInfo_end']")[idx]).val();
		$("#schedule_stop").val(getTimeAddSecond(contentEnd, 15));
		
		if(idx < $("input[name='contentId']").length - 1){
			SetContentTime(Number(idx));
		} else {
			$("#contentList").modal("hide");
		}
	}
}

function SetContentTime(idx) {	
	var contentLength = $("input[name='contentId']").length;
	var contentEnd;
	var setScheduleFlag = true;
	for (var i = idx; i < contentLength; i++) {
		if($($("input[name='duration']")[i+1]).val() == ""){
			setScheduleFlag = false;
			break;
		}
		if($($("input[name='duration']")[i+1]).val() == undefined){
			break;
		}
		contentEnd = $($("input[name='deliveryInfo_end']")[i]).val();
		var contentStartNext = getTimeAddSecond(contentEnd, 15);
		$($("input[name='deliveryInfo_start']")[i+1]).val(contentStartNext);
		var duration = $($("input[name='duration']")[i+1]).val();
		contentEnd = getTimeAddSecond(contentStartNext, duration)
		$($("input[name='deliveryInfo_end']")[i+1]).val(contentEnd);
	}
	if(setScheduleFlag){
		$("#schedule_stop").val(getTimeAddSecond(contentEnd, 15));
	}
	$("#contentList").modal("hide");
}

function addServiceAreaEvent(idx){
	$($("button[name='mapAdd']")[idx]).click(function(){
		$("#popupType").val("normal");
		$("#circleCiryPop").modal();
	})
	
	$($("button[name='saidAdd']")[idx]).click(function(e) {
		
		var said = $($("input[name='said']")[idx]).val();
		var saidDefault = $($("input[name='saidDefault']")[idx]).val();

		if (said == ""){
			alert ('please enter the said.');
			return;
		}
		
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
	if($("#serviceId").val() == ""){
		alert("Please enter the Service Id");
		$("#serviceId").focus();
		return;
	}
	
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
	
	if($("#serviceType").val() != "streaming"){
		if (d_start < s_start ){
			alert("It can not be 'content start time' over than 'schedule start time' ");
			return false;
		}
		
		if (d_end > s_stop ){
			alert("It can not be 'content start stop time' over than 'schedule Stop time' ");
			return false;
		}
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
//		$("#samplePercentage").val("");
	}
}