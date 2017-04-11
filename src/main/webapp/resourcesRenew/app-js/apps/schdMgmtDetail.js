var content_id = "";
var gTitle = "";
var gDuration = "";
var g_name = "";
var g_delRetrun;
var currentTime = getTimeStamp();
var glovalSaid = "";
var g_ServiceGroupId = '';
var g_ServiceAreaId = '';

function getTimeStamp() {
	var d = new Date();
	var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2) + ' ' +
	
	    leadingZeros(d.getHours(), 2) + ':' +
	    leadingZeros(d.getMinutes(), 2) + ':' +
	    leadingZeros(d.getSeconds(), 2);
	return s;
}

function leadingZeros(n, digits) {
	var zero = '';
	n = n.toString();
	
	if (n.length < digits) {
	  for (i = 0; i < digits - n.length; i++)
	    zero += '0';
	}
	return zero + n;
}

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
    
    $("input[name='radio']").click(function() {
    	var radioType = $("input[name='radio']:checked").val();
    	if(userGrade == 9999) {
    		$("#emergency").hide();
			$("#national").hide();
		}
    	if(radioType == "group")
    	{
    		$("#selectHotspot").hide();
    		$("#selectHotspotLabel").hide();
    		$("#selectCityLabel").html("Group");
    		$("#selectArea").show();
    		if(glovalSaid != ""){
    			$('#scheduleSearch').click();
    		}
    	}
    	else if(radioType == "area")
    	{
    		$("#selectHotspot").show();
    		$("#selectHotspotLabel").show();
    		$("#selectCityLabel").html("City");
    		$("#selectArea").show();
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
    });
	
});

$(window).load(function() {
    if(userGrade == 9999) {
		$($("input[name='radio']")[2]).click();
		$("#selectHotspot").show();
		$("#selectHotspotLabel").show();
		$("#selectCityLabel").html("City");
		$("#selectArea").show();
		$($("#selectCircle option")[1]).prop("selected", true);
		$("#selectCircle").change();
		g_ServiceAreaId = $("#selectCircle").val().split("^")[0];
	} else {
		$($("input[name='radio']")[1]).click();
	}
//    ctrl.initialize();
	loadContentList(1);
})
	
var ctrl = {
	initialize : function() {
		var type = $("input[name='radio']:checked").val();
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
	
function loadContentList(page){
	var param = {
			title : $("#form-title").val(),
			category : $("#form-category").val(),
			page : page
		};
	$.ajax({
		type : "POST",
		url : "getContents.do",
		data : param,
		dataType : "json",
		success : function( data ) {
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
		var path = data[i].path;
		duration = (new Date(parseInt(duration) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0];
		
		$div1.attr("class","feed-element");
		$div1.attr("style","padding-bottom: 0px")
		$div1.append("<a href='#' class='pull-left'><img alt='image' class='img-circle' src='"+ path + "'></a>");
		$div2.attr("class","media-body");
		$div2.append("<strong>[" + category +"]</strong> " + title + "<br/><small class='pull-right'><span class='text-danger'> Running Time </span>" + duration + "</small>" );
		
		$div1.append($div2);
		$div.attr("class","fc-event");
		$div.attr("data-id", id);
		$div.attr("data-title", title);
		$div.attr("data-duration", duration);
		
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
	var clrBackPassSchd = '#dddddd'		,clrTxtPassSchd 	= '#787A7C'		,clrBrdPassSchd = '#bbbbbb';
	var clrBackPassSchdBMSC='#888888'	,clrTxtPassSchdBMSC	= '#787A7C'	,clrBrdPassSchdBMSC	= '#bbbbbb';
	
	var clrBackCurrSchd = '#23C6C8'		,clrTxtCurrSchd 	= '#ecf0f1'		,clrBrdCurrSchd = '#318E8F';
	var clrBackCurrSchdBMSC='#22893E'	,clrTxtCurrSchdBMSC	= '#ecf0f1'	,clrBrdCurrSchdBMSC	= '#024B16';
	
	var clrBackNextSchd = '#FFEEB7'		,clrTxtNextSchd 	= '#787A7C'		,clrBrdNextSchd = '#B5B317';
	var clrBackNextSchdBMSC='#FFD340'	,clrTxtNextSchdBMSC	= '#787A7C'	,clrBrdNextSchdBMSC	= '#B5B317';
	
	for ( var i = 0; i < contents.length; i++) {
		var id = contents[i].ID;
		var name = contents[i].NAME;
		var broadcast_info_id = contents[i].BCID;
		if (typeof broadcast_info_id == 'undefined')
			broadcast_info_id = '';
		var start_date = contents[i].start_date;
		var end_date = contents[i].end_date;
		var url = "schedule.do?id=" + id + "&BCID=" + broadcast_info_id;
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
			
			//schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#23C6C8", textColor: "#ecf0f1", borderColor:"#1AB394"};
		}
		
		//schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#eeeeee"};
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
		selectable: false,
		selectHelper: true,
		select: function(start, end) {
			//console.log('select');
			//var ret = confirm('Do you want to add a new schedule?');
			//if (ret) {
			//	location.href='/dashbd/view/schedule.do';
			//}
		},
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: events,
		drop: function(event, dayDelta, minuteDelta,allDay,revertFunc) {
			content_id = $(this).attr("data-id");
			gTitle = $(this).attr("data-title");
			gDuration = $(this).attr("data-duration");
			
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
				addSchedule(content_id, gTitle, startTime, endTime);
			}
			
//			var b = $('#calendar').fullCalendar('getDate');
//		 	var searchDate = b.format('YYYY-MM-DD');
//			
//			var title = encodeURI($("#form-title").val());
//			var category =  encodeURI($("#form-category").val());
//			location.href = "schdMgmtDetail.do?bmscId=" + tmpbmscId + "&serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category + "&type=" + $("#type").val();
		},
		eventDrop: function(event) { // called when an event (already on the calendar) is moved
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
			
			var trashEl = jQuery('#calendarTrash');
		    var ofs = trashEl.offset();

		    var x1 = ofs.left;
		    var x2 = ofs.left + trashEl.outerWidth(true);
		    var y1 = ofs.top;
		    var y2 = ofs.top + trashEl.outerHeight(true);

		    if (jsEvent.pageX >= x1 && jsEvent.pageX<= x2 &&
		        jsEvent.pageY>= y1 && jsEvent.pageY <= y2) {
		    	
		        //alert('SIII');
		    	deleteSchedule(event.url);
		    	//alert(Boolean(ret) + ',' + ret);
		    	if ( g_delRetrun == 1)
		    		$('#calendar').fullCalendar('removeEvents', event._id);
		    	else
		    		alert(bRet);
		    	
		    }
	    }
		, eventAfterRenderfunction: function(event) { // called when an event (already on the calendar) is moved
			console.log('eventAfterRender..', event);
		}
		/*
		
		, eventRender: function(event, element) {
			console.log('eventRender', event, element);
	    }
		*/
		, viewRender: function(view, element){
			var now4compare = replaceAll4Day(moment().format());
			var viewDay4compare = replaceAll4Day(view.start.format());
			// console.log(now4compare , viewDay4compare);
			if (now4compare == viewDay4compare){
				setTimeline(view, "day");
			}else{
				setTimeline(view, "week");
			}
//			setInterval(function () {
//		        setTimeline(view);
//			}, 5000);
		}
	});
	
	 var fbg = $('#calendar').find('.fc-button-group');
	 //fbg.append('<div id="calendarTrash" style="float: right; padding-top: 5px; padding-right: 5px; padding-left: 5px;"><span class="ui-icon ui-icon-trash"></span></div>');
	 fbg.append('<div id="calendarTrash" style="float: right; padding-top: 2px; padding-right: 5px; padding-left: 5px;"><span class="ui-icon ui-icon-trash"><img src="../resourcesRenew/img/trash.png"/></span></div>');
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
	var BCID = url.substring(url.indexOf("&BCID=") + 6);
	
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
			console.log('Success to modify schedule');
		},
		error : function(request, status, error) {
			alert("error for update Time:request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function deleteSchedule(url){
	if (!confirm("It will be deleted. do you want this??")){
		g_delRetrun = 0;
		return;
	}
	
	var id = url.substring(url.indexOf("=")  + 1, url.indexOf("&BCID"));
	var BCID = url.substring(url.indexOf("&BCID=") + 6);
	
	if (typeof BCID == 'undefined')
		BCID = "";
	
	var param = {
			id : id,
			BCID : BCID,
			type : $("#type").val(),
			bmscId : $("#bmscId").val()
	};
	$.ajax({
		type : "POST",
		url : "delSchedule.do",
		data : param,
		dataType : "json",
		async: false,
		success : function( data ) {
			g_delRetrun = outMsgForAjax(data);
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
			g_delRetrun = 0;
		}
	});
}
	
function addSchedule(content_id, g_name, startTime, endTime){
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
			var url = "schedule.do?id="+data.scheduleId+"&BCID=";
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