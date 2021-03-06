var content_id = "";
var contentsType = "";
var gTitle = "";
var gDuration = "";
var g_name = "";
var g_delRetrun;
var currentTime = getTimeStamp();
var glovalSaid = "";
var g_ServiceGroupId = '';
var g_ServiceAreaId = '';
var g_type = '';

$(document).ready(function() {
	$('#scheduleSearch').click(function(){
		ctrl.initialize();
	});
	
	$("#go-search").click(function() {
		loadContentList();
	});
	
	$("#selectCircle").on("change", function(e) {
    	var url = "";
    	var type = $("input[name='radio']:checked").val();
    	if(type == "group"){
    		url = "getGroupListFromCircleId.do";
    	}else{
    		url = "/dashbd/hotspot/getCityListFromCircleId.do";
    	}
    	
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var circleId = array[0];
    	var circleName = array[1];
    	if(circleId == ""){
    		g_ServiceAreaId = "";
    		$("#selectCity").html("");
        	$("#selectHotspot").html("");
    		return;
    	}
    	$.ajax({
    		url : url,
    		type: "post",
    		data : { "circleId" : circleId },
    		success : function(data) {
    			var json = JSON.parse(data).result;
    			var html = "<option value=''>Select City</option>";
    			if(type == "group"){
    				html = "<option value=''>Select Group</option>";
    				for (var i = 0; i < json.length; i++) {
                		html += "<option value='"+json[i].group_id+"^"+json[i].group_name+"^"+json[i].circle_id+"'>"+json[i].group_name+"</option>";
    				}
    			}else{
    				for (var i = 0; i < json.length; i++) {
                		html += "<option value='"+json[i].city_id+"^"+json[i].city_name+"^"+json[i].latitude+"^"+json[i].longitude+"'>"+json[i].city_name+"</option>";
    				}
    			}
    			glovalSaid = circleId;
    			g_ServiceAreaId = circleId;
            	$("#selectCity").html(html);
            	$("#selectHotspot").html("");
            	ctrl.initialize();
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "Network Error"
    			});
    		}
    	});
    });
    
    $("#selectCity").on("change", function(e){
    	var url = "";
    	var type = $("input[name='radio']:checked").val();
    	if(type == "group"){
    		url = "getGroupSaidList.do";
    	}else{
    		url = "/dashbd/hotspot/getHotSpotListFromCityId.do";
    	}
    	
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var cityId = array[0];
    	var cityName = array[1];
    	if(cityId == ""){
    		g_ServiceAreaId = "";
    		g_ServiceGroupId = "";
    		$("#selectCircle").val("");
    		$("#selectHotspot").html("");
    		return;
    	}
    	$.ajax({
    		url : url,
    		type: "post",
    		data : { "cityId" : cityId },
    		success : function(data) {
    			var json = JSON.parse(data).result;
    			if(type == "group"){
//    				var said = ""
//    				for (var i = 0; i < json.length; i++) {
//    					if(i == json.length-1){
//    						said += json[i].sub_said;
//    					}else{
//    						said += json[i].sub_said + ","
//    					}
//					}
    				if(json.length == 0){
    					alert("Group's element is not exist !");
    					$("#selectCity").val("");
    					return;
    				}
    				glovalSaid = cityId;
    				g_ServiceGroupId = cityId;
    				ctrl.initialize();
    	    	} 
    			else 
    	    	{
        			var html = "<option value=''>Select Hotspot</option>";
        			for (var i = 0; i < json.length; i++) {
        				html += "<option value='"+json[i].hotspot_id+"^"+json[i].hotspot_name+"'>"+json[i].hotspot_name+"</option>";
        			}
        			glovalSaid = cityId;
        			g_ServiceAreaId = cityId;
        			$("#selectHotspot").html(html);
        			ctrl.initialize();
    	    	}
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "Network Error"
    			});
    		}
    	});
    });
    
    $("#selectHotspot").on("change", function(e){
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var hotspotId = array[0];
    	var hotspotName = array[1];
    	if(hotspotId == ""){
    		g_ServiceAreaId = "";
    		g_ServiceGroupId = "";
    		$("#selectCircle").val("");
    		$("#selectCity").val("");
    		$("#selectHotspot").html("");
    		return;
    	}
    	glovalSaid = hotspotId;
		g_ServiceAreaId = hotspotId;
		ctrl.initialize();
    });
});

function radioClick(type) {
	g_type = type;
	if(userGrade == 9999) {
		$("#emergency").hide();
		$("#national").hide();
	}g_ServiceGroupId
	if(type == "group")
	{
		$("#selectHotspot").hide();
		$("#selectHotspotLabel").hide();
		$("#selectCityLabel").html("Group");
		$("#selectArea").parent().css({"margin-left":"1px", "margin-bottom":"15px"});
		$("#selectArea").show();
		if(glovalSaid != ""){
			$('#scheduleSearch').click();
		}
	}
	else if(type == "area")
	{
		$("#selectHotspot").show();
		$("#selectHotspotLabel").show();
		$("#selectCityLabel").html("City");
		$("#selectArea").parent().css({"margin-left":"1px", "margin-bottom":"15px"});
		$("#selectArea").show();
		g_ServiceGroupId = '';
	}
	else
	{
		$("#selectArea").hide();
		$('#scheduleSearch').click();
	}
	
	$("#selectCircle").val("");
	$("#selectCity").val("");
	$("#selectCity").html("");
	$("#selectHotspot").html("");
}

$(window).load(function() {
    if(userGrade == 9999) {
		$($("input[name='radio']")[2]).parent().click();
		$("#selectHotspot").show();
		$("#selectHotspotLabel").show();
		$("#selectCityLabel").html("City");
		$("#selectArea").show();
		$($("#selectCircle option")[1]).prop("selected", true);
		$("#selectCircle").change();
		g_ServiceAreaId = $("#selectCircle").val().split("^")[0];
	} else {
		$($("input[name='radio']")[1]).parent().click()
	}
    $("#tab2").click();
});

var ctrl = {
	initialize : function() {
		var type = g_type;
		$("#type").val(type);
		if(type == "national" || type == "emergency"){
			g_ServiceAreaId = setAllCircleSaid();
		}
		if (g_ServiceAreaId == '' || g_ServiceAreaId == undefined){
			if(type != "national" && type != "emergency"){
				alert('Please, choose ServiceArea.');
				return;
			}
		}
		if(g_ServiceGroupId != '') {
			g_ServiceAreaId = g_ServiceGroupId;
			type = "group";
		}
		var param = {
				  serviceAreaId : g_ServiceAreaId
				, type 			: type
				, serviceType	: $("#serviceType").val()
				, serviceClass	: $("#serviceClass").val()
				, searchType	: $("#searchType").val()
				, searchKeyword	: $("#searchKeyword").val()
			};
			
		$.ajax({
			type : "POST",
			url : "getSchedule.do",
			data : param,
			dataType : "json",
			async: false,
			success : function( data ) {
				setTimeTable(data);
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	}
};
	
function searchContents() {
	if(event.keyCode == 13){
		loadContentList(1, $("#contentsType").val());
	}
}

function loadContentList(page, contentsType){
	var param = {
			title : $("#form-title").val(),
//			category : $("#form-category").val(),
			type : contentsType,
			page : page
		};
	$.ajax({
		type : "POST",
		url : "getContents.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			$("#contentsType").val(contentsType);
			getContents(data.contents, page);
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function setDragEventFunction(){
	$('#external-events .fc-event').each(function() {
		// store data so the calendar knows to render an event upon drop
		$(this).data('event', {
			title: $.trim($(this).text()), // use the element's text as the event title
			stick: true // maintain when user navigates (see docs on the renderEvent method)
		});

		// make the event draggable using jQuery UI
		$(this).draggable({
			zIndex: 999,
			revert: true,      // will cause the event to go back to its
			revertDuration: 0  //  original position after the drag
		});

	});
}
	
function getContents(data, page){

	var $list = $("#external-events");
	
	// list 초기화
	$list.empty();
	$("#paging").empty();
	if (data.length < 1){
		alert("No data.");
	}
		
	var perPage = 5;
	var listPageCount = 2;
	
	for ( var i=0; i<data.length; i++) {
		var $div = $("<div/>");
		var $div1 = $("<div/>");
		var $div2 = $("<div/>");
		
		var id = data[i].cid;
		var title = data[i].title;
		var category = data[i].category;
		var duration = data[i].duration;
		var contentsType = data[i].type;
		var path = data[i].path;
		var hourExtra = 0;
		if(duration >= 86400){
			hourExtra = parseInt(duration / 86400) * 24;
			duration = (new Date(parseInt(duration) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0];
			duration = (parseInt(duration.substring(0,2)) + hourExtra) + duration.substring(2);
		}else{
			duration = (new Date(parseInt(duration) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0];
		}
		
		$div1.attr("class","feed-element");
		$div1.attr("style","padding-bottom: 0px")
		$div1.append("<a href='#' class='pull-left'><img alt='image' class='img-circle' src='"+ path + "'></a>");
		$div2.attr("class","media-body");
		$div2.append("<strong>[" + category +"]</strong> " + title + "<br/><small class='pull-right'><span class='text-danger'> Running Time </span>" + duration + "(sec)</small>" );
		
		$div1.append($div2);
		$div.attr("class","fc-event");
		$div.attr("data-id", id);
		$div.attr("data-title", title);
		$div.attr("data-duration", duration);
		$div.attr("data-type", contentsType);
		
		$div.append($div1);
		$list.append( $div );
	}
	// Pagination
    var totalCount = data[0].totalCount;
	
    if(totalCount > perPage) {
    	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
    	//alert(totalPageCount);
    	
    	var pageination = '';
        pageination += '<div class="text-center">';
        pageination += '<ul class="pagination">';
        if( page == 1 )
        {
        	pageination += '<li class="disabled"><a href="javascript:loadContentList(' + (page-1) + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
        }
        else {
        	pageination += '<li><a href="javascript:loadContentList(' + (page-1) + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
        }
        
        if(totalPageCount > listPageCount) {
        	for(var i = page, j = 0; i <= totalPageCount && j < listPageCount ; i++, j++) {
            	if( i == page ) {
            		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
            	}
            	else {
            		pageination += '<li><a href="javascript:loadContentList(' + i + ');">' + i + '</a></li>';
            	}
            }
        }
        else {
        	for(var i = 1; i <= totalPageCount && i <= listPageCount ; i++) {
            	if( i == page ) {
            		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
            	}
            	else {
            		pageination += '<li><a href="javascript:loadContentList(' + i + ');">' + i + '</a></li>';
            	}
            }
        }
        
        
        if( page == totalPageCount ) {
        	pageination += '<li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
        }
        else {
        	pageination += '<li><a href="javascript:loadContentList(' + (page+1) + ');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
        }
		pageination += '</ul>';
		pageination += '</div>';
		
		$("#paging").append(pageination);
    }
	/*
	$(".pagging").empty();
	$("#external-events").quickPager( {
		naviSize: 3,
		currentPage: 1,
		holder: ".pagging"
	});
	*/
	setDragEventFunction();
	$(".search-list").show();
}
	
	
function setTimeTable(data){
	var tmpServiceAreaId = g_ServiceAreaId;
	var tmpbmscId = $("#bmscId").val();
	var searchDate = $("#searchDate").val();
	var title = $("#form-title").val();
	var category =  $("#form-category").val();
	var contents = data.contents;
	var events = [];
	var schedule;
	var now = moment();
	
	var clrBackPassSchd 	= '#F06464'	,clrTxtPassSchd 	= '#ECF0F1'	,clrBrdPassSchd 	= '#318E8F';
	var clrBackPassSchdBMSC	= '#8c8c8c'	,clrTxtPassSchdBMSC	= '#ECF0F1'	,clrBrdPassSchdBMSC	= '#318E8F';
	
	var clrBackCurrSchd 	= '#F06464'	,clrTxtCurrSchd 	= '#ECF0F1'	,clrBrdCurrSchd 	= '#318E8F';
	var clrBackCurrSchdBMSC	= '#32AAFF'	,clrTxtCurrSchdBMSC	= '#ECF0F1'	,clrBrdCurrSchdBMSC	= '#318E8F';
	
	var clrBackNextSchd 	= '#F06464'	,clrTxtNextSchd 	= '#ECF0F1'	,clrBrdNextSchd 	= '#318E8F';
	var clrBackNextSchdBMSC	= '#32AAFF'	,clrTxtNextSchdBMSC	= '#ECF0F1'	,clrBrdNextSchdBMSC	= '#318E8F';
	
	for (var i = 0; i < contents.length; i++) {
		var id = contents[i].ID;
		var name = contents[i].NAME;
		var broadcast_info_id = contents[i].BCID;
		if (typeof broadcast_info_id == 'undefined'){
			broadcast_info_id = '';
		}
		var start_date = contents[i].start_date;
		var end_date = contents[i].end_date;
		var serviceType = contents[i].contentsType;
		var url = "schedule.do?id=" + id + "&BCID=" + broadcast_info_id + "&contentsType="+serviceType;
		if(userGrade == 9999 && (contents[i].national_yn == "Y" || contents[i].emergency_yn == "Y")){
			url = "#";
		}
		var now4compare = replaceAll4Time(now.format());
		var start4compare = replaceAll4Time(start_date);
		var end4compare = replaceAll4Time(end_date);
		var serviceId = contents[i].serviceId;
		if (broadcast_info_id == null || broadcast_info_id == ""){
			if (now4compare < start4compare ){
				//미래
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackNextSchd, textColor: clrTxtNextSchd, borderColor:clrBrdNextSchd, serviceId:serviceId};
			}else if (now4compare > end4compare ){
				//과거
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackPassSchd, textColor: clrTxtPassSchd, borderColor:clrBrdPassSchd, serviceId:serviceId};
			}else{
				//현재
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackCurrSchd, textColor: clrTxtCurrSchd, borderColor:clrBrdCurrSchd, serviceId:serviceId};
			}
		}else{
			if (now4compare < start4compare ){
				//미래
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackNextSchdBMSC, textColor: clrTxtNextSchdBMSC, borderColor:clrBrdNextSchdBMSC, serviceId:serviceId};
			}else if (now4compare > end4compare ){
				//과거
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackPassSchdBMSC, textColor: clrTxtPassSchdBMSC, borderColor:clrBrdPassSchdBMSC, serviceId:serviceId};
			}else{
				//현재
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackCurrSchdBMSC, textColor: clrTxtCurrSchdBMSC, borderColor:clrBrdCurrSchdBMSC, serviceId:serviceId};
			}
		}
		events.push( schedule );
	}
	$("#calendar").fullCalendar('destroy');
	$('#calendar').fullCalendar({ 
		schedulerLicenseKey: 'GPL-My-Project-Is-Open-Source',
		defaultView: 'agendaWeek', 
		editable: true, 			// enable draggable events
		droppable: true, 			// this allows things to be dropped onto the calendar
		slotDuration: '00:15:00',
		header: {
			left: 'prev,next,trash'
			, center: 'title'
			, right: 'month, agendaWeek, agendaDay'
		},
		defaultDate: searchDate,
		titleFormat: {
			   month: 'YYYY MMMM',
			   week:  "YYYY MMMM",
			   day: 'YYYY-MM-DD dddd'
			},
		timeFormat: 'HH:mm',
		selectable: false,
		selectHelper: true,
		select: function(start, end) {
			
		},
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: events,
		drop: function(event, dayDelta, minuteDelta, allDay, revertFunc) {
			content_id = $(this).attr("data-id");
			gTitle = $(this).attr("data-title");
			gDuration = $(this).attr("data-duration");
			contentsType = $(this).attr("data-type");
			$(this).remove();
		},
		eventReceive: function(event) { // called when a proper external event is dropped
			g_name = event.title;
			var startTime = event.start.format();
			var hours = gDuration.substring(0,2);
			var minutes = gDuration.substring(4,5);
			var seconds = gDuration.substring(7,8);
			
			var endTime = moment(startTime).add(hours, 'hours');
			endTime = moment(endTime).add(minutes, 'minutes');
			endTime = moment(endTime).add(seconds, 'seconds');
			
			endTime = endTime.format('YYYY-MM-DD[T]HH:mm:ss');
			if(getTimeDiff(startTime, currentTime)){
				alert("The start time has already passed")
				location.href = "schdMgmtDetail.do?bmscId=" + tmpbmscId + "&serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category + "&type=" + $("#type").val();
			}else{
				addSchedule(content_id, gTitle, startTime, endTime, contentsType);
			}
		},
		eventDrop: function(event) {
			var ret = confirm("It's going to update. are you sure??");
			if (ret){
				if(getTimeDiff(event.start.format(), currentTime)){
					alert("The start time has already passed")
					location.href = "schdMgmtDetail.do?bmscId=" + tmpbmscId + "&serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category + "&type=" + $("#type").val();
				}else{
					modifySchedule(event.url, event.start.format(), event.end.format());
				}
			}	
			else {
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category + "&type=" + $("#type").val();
			}
		}
		,eventResize: function(event, delta, revertFunc){
			var ret = confirm("It's going to update. are you sure??");
			
			if (ret)
				modifySchedule(event.url, event.start.format(), event.end.format());
			else
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category + "&type=" + $("#type").val();
		}
		, eventDragStop: function( event, jsEvent, ui, view ) { 
			if(event.url == "#"){
				alert("You don't have permission");
				return;
			}
			var trashEl = jQuery('#calendarTrash');
		    var ofs = trashEl.offset();

		    var x1 = ofs.left;
		    var x2 = ofs.left + trashEl.outerWidth(true);
		    var y1 = ofs.top;
		    var y2 = ofs.top + trashEl.outerHeight(true);

		    if (jsEvent.pageX >= x1 && jsEvent.pageX<= x2 && jsEvent.pageY>= y1 && jsEvent.pageY <= y2) {
		    	var BCID = event.url.substring(event.url.indexOf("&BCID=") + 6, event.url.lastIndexOf("&"));
		    	if(BCID.length > 0) {
		    		deleteSchedule(event.url);
		    	}else{
		    		if(!confirm("It will be deleted. do you want this??")){
		    			g_delRetrun = 0;
		    			return;
		    		}
		    		deleteAction(event.url, "Delete")
					$('#calendar').fullCalendar('removeEvents', event._id);
		    	}
		    }
	    }
		, eventAfterRenderfunction: function(event) { // called when an event (already on the calendar) is moved
			console.log('eventAfterRender..', event);
		}
		, viewRender: function(view, element){
			var now4compare = replaceAll4Day(moment().format());
			var viewDay4compare = replaceAll4Day(view.start.format());
			if (now4compare == viewDay4compare){
				setTimeline(view, "day");
			}else{
				setTimeline(view, "week");
			}
		}
	});
	
	var fbg = $('#calendar').find('.fc-button-group');
	fbg.append('<div id="calendarTrash" style="float: right; padding-top: 2px; padding-right: 5px; padding-left: 5px;"><span class="ui-icon ui-icon-trash"><img src="../resourcesRenew/img/trash.png"/></span></div>');
	setEventTitle(contents);
}

function setEventTitle(contents) {
	for (var i = 0; i < $(".fc-time-grid-event").length; i++) {
		for (var j = 0; j < contents.length; j++) {
			if(contents[j].ID == $(".fc-time-grid-event")[i].href.split("&")[0].split("=")[1]){
				if($($(".fc-time-grid-event")[i]).children().text().indexOf("(-)") > -1){
					$($(".fc-time-grid-event")[i]).css("background-color", "#3CAEFF");
				}
				var serviceTime = contents[j].start_date.split("T")[1] +" ~ "+ contents[j].end_date.split("T")[1];
				var serviceName = contents[j].NAME;
				var serviceType = contents[j].service;
				var serviceId = contents[j].serviceId;
				var serviceMode = contents[j].serviceMode;
				if(serviceType == undefined) {
					$(".fc-time-grid-event")[i].title = "serviceTime : " + serviceTime + "\nserviceName : " + contents[j].NAME
				}else{
					$(".fc-time-grid-event")[i].title = "serviceTime : " + serviceTime + "\nserviceName : " + contents[j].NAME +"\nserviceType : "+contents[j].service +"\nserviceId : "+contents[j].serviceId;
				}
				if(serviceType == "streaming"){
					$(".fc-time-grid-event")[i].title += "\nserviceMode : " + serviceMode;
				}
				break;
			}
		}
	}
}

function setTimeline(calView, mode) {
	
   if(jQuery(".timeline").length == 0){
      jQuery(".fc-time-grid-container").prepend("<div style='width:100%;overflow: visible;'><hr class='timeline'/></div>") 
    }
	
    var timeline = jQuery(".timeline");  

    var now = moment();
    var day = parseInt(now.format("e"))
    var width = 10000;
    var height = 80;	//나눠진 분마다 높이 조절
    var left = 60;
    //var top = ( (now.hours()*3600)+(now.minutes()*60)+now.seconds() )/86400;;
    var position = now.hours() + now.minutes() / 60 ;
    
    var top = height * position;
    
    timeline
    .css('width',width+"px")
    .css('left',left+"px")
    .css('top',top+"px") 
    
    $(".fc-time-grid-container").animate({scrollTop : top - top/12}, 400);	//최초 스크롤 위치 조정
    if(mode == "day"){
    	$(".fc-widget-content").css("background-color", "white");				//달력 배경 흰색으로
    }
}

function modifySchedule(url, startTime, endTime) {
	
	var id = url.substring(url.indexOf("=")  + 1, url.indexOf("&BCID"));
	var BCID = url.substring(url.indexOf("&BCID=") + 6, url.lastIndexOf("&"));
	
	if (typeof BCID == 'undefined')
		BCID = "";
	
	var param = {
			scheduleId : id,
			BCID : BCID,
			bmscId : $("#bmscId").val(),
			startTime : startTime,
			endTime: endTime
	};
	
	$.ajax({
		type : "POST",
		url : "modifyScheduleTime.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			outMsgForAjax(data);
			var resultCode = data.resultInfo.resultCode;
		},
		error : function(request, status, error) {
			alert("error for update Time:request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function deleteSchedule(url){
//	if (!confirm("It will be deleted. do you want this??")){
//		g_delRetrun = 0;
//		return;
//	}
	
	$("#url").val(url);
	$("#deleteAbortModal").modal();
}
	
function deleteAction(url, deleteType) {
	var id = url.substring(url.indexOf("=")  + 1, url.indexOf("&BCID"));
	var BCID = url.substring(url.indexOf("&BCID=") + 6, url.lastIndexOf("&"));
	
	if (typeof BCID == 'undefined')
		BCID = "";
	
	var param = {
			id : id,
			BCID : BCID,
			type : $("#type").val(),
			bmscId : $("#bmscId").val(),
			deleteType : deleteType
	};
	$.ajax({
		type : "POST",
		url : "delSchedule.do",
		data : param,
		dataType : "json",
		async: false,
		success : function( data ) {
			g_delRetrun = outMsgForAjax(data);
			var resultCode = data.resultInfo.resultCode;
			if(resultCode == "8410") {
				alert("This schedule can be deleted after the end time of the schedule");
				return;
			}
			if(resultCode == "6011") {
				if(confirm("Do you want to delete this schedule in SeSM?")){
					deleteScheduleSeSM(param);
				}
			}
			if(resultCode != "1000" && data.resultCode != "200"){
				return;
			}
			location.reload();
			/*if ( g_delRetrun == 1){
				$('#calendar').fullCalendar('removeEvents', event._id);
			}else{
				alert(bRet);
			}*/
		},
		complete : function( data ) {
			$("#deleteAbortModal").modal('hide');
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
			g_delRetrun = 0;
		}
	});
}

function deleteScheduleSeSM(param) {
	$.ajax({
		type : "POST",
		url : "delScheduleSeSM.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			var resultCode = data.resultInfo.resultCode;
			if(resultCode == "1000"){
				location.href = "schdMgmtDetail.do?bmscId="+bmscId;
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function addSchedule(content_id, g_name, startTime, endTime, contentsType){
	var param = {
			serviceAreaId : g_ServiceAreaId,
			bmscId : $("#bmscId").val(),
			contentId : content_id,
			titleName : g_name,
			startTime : startTime,
			endTime: endTime,
			type: $("#type").val()
		};
		
	$.ajax({
		type : "POST",
		url : "addScheduleWithInitContent.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			alert('Please enter detailed parameters in next screen');
			var url = "schedule.do?id="+data.scheduleId+"&BCID="+"&contentsType="+contentsType;
			location.href = url; 
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}
function replaceAll4Time(input){
	var output;
	output = input.replace(/-/gi,"");
	output = output.replace(/T/gi,"");
	output = output.replace(/:/gi,"");
	output = output.substring(0,14);
	return output;
}

function replaceAll4Day(input){
	var output;
	output = input.replace(/-/gi,"");
	output = output.substring(0,8);
	return output;
}

//national 일 경우 전체 서클 넣어주기
function setAllCircleSaid() {
	var said = "";
	var optionLength = $("#selectCircle option").length;
	for (var i = 1; i < optionLength; i++) {
		if(i == optionLength - 1){
			said += $("#selectCircle option")[i].value.split("^")[0];
		}else{
			said += $("#selectCircle option")[i].value.split("^")[0]+",";
		}
	}
	return said;
}