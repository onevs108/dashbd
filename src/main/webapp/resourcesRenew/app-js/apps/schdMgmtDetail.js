var content_id = "";
var gTitle = "";
var g_name = "";
$(document).ready(function() {
	
	ctrl.initialize();
	/*
	$('.fc-prev-button span').click(function(){
		alert('prev is clicked, do something');
	});
	*/
	loadContentList(1);
	
	$("#go-search").click(function() {
		loadContentList();
	});

});

	
var ctrl = {
	initialize : function() {
		
		var param = {
				serviceAreaId : $('#serviceAreaId').val()
				/*, searchDate : $('#searchDate').val()*/
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
		// console.log((new Date(parseInt(duration) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0]);
		
		duration = (new Date(parseInt(duration) * 1000)).toUTCString().match(/(\d\d:\d\d:\d\d)/)[0];
		
		$div1.attr("class","feed-element");
		$div1.attr("style","padding-bottom: 0px")
		$div1.append("<a href='#' class='pull-left'><img alt='image' class='img-circle' src='"+ path + "'></a>");
		
		$div2.attr("class","media-body");
		$div2.append("<strong>[" + category +"]</strong>" + title + "<small class='pull-right'>Running Time" + duration + " </small>" );
		
		$div1.append($div2);
		$div.attr("class","fc-event");
		$div.attr("data-id", id);
		$div.attr("data-title", title);
		
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
	
	
function setTimeTable(data ){
	
	var searchDate = $("#searchDate").val();
	var contents = data.contents;
	var events = [];
	var schedule;
	var now = moment();
	var clrBackPassSchd = '#dddddd'		,clrTxtPassSchd 	= '#787A7C'		,clrBrdPassSchd = '#bbbbbb';
	var clrBackPassSchdBMSC='#888888'	,clrTxtPassSchdBMSC	= '#787A7C'	,clrBrdPassSchdBMSC	= '#bbbbbb'; 
	
	var clrBackCurrSchd = '#23C6C8'		,clrTxtCurrSchd 	= '#ecf0f1'		,clrBrdCurrSchd = '#bbbbbb';
	var clrBackCurrSchdBMSC='#23C6ff'	,clrTxtCurrSchdBMSC	= '#ecf0f1'	,clrBrdCurrSchdBMSC	= '#bbbbbb';
	
	var clrBackNextSchd = '#FFEEB7'		,clrTxtNextSchd 	= '#787A7C'		,clrBrdNextSchd = '#bbbbbb';
	var clrBackNextSchdBMSC='#FFD340'	,clrTxtNextSchdBMSC	= '#787A7C'	,clrBrdNextSchdBMSC	= '#bbbbbb';
	
	for ( var i = 0; i < contents.length; i++) {
		var id = contents[i].ID;
		var name = contents[i].NAME;
		var broadcast_info_id = contents[i].BCID;
		
		var start_date = contents[i].start_date;
		var end_date = contents[i].end_date;
		var url = "schedule.do?id=" + id + "&BCID=" + broadcast_info_id;
		var now4compare = replaceAll4Time(now.format());
		var start4compare = replaceAll4Time(start_date);
		var end4compare = replaceAll4Time(end_date);
		console.log('current=', now4compare , ', start_date=', start4compare, ', end_date=', end4compare);
		if (broadcast_info_id == null || broadcast_info_id == ""){
			if (now4compare < start4compare ){
				//미래
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackNextSchd, textColor: clrTxtNextSchd, borderColor:clrBrdNextSchd};
			}else if (now4compare > end4compare ){
				//과거
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackPassSchd, textColor: clrTxtPassSchd, borderColor:clrBrdPassSchd};
			}else{
				//현재
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackCurrSchd, textColor: clrTxtCurrSchd, borderColor:clrBrdCurrSchd};
			}
				
		}else{
			
			if (now4compare < start4compare ){
				//미래
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackNextSchdBMSC, textColor: clrTxtNextSchdBMSC, borderColor:clrBrdNextSchdBMSC};
			}else if (now4compare > end4compare ){
				//과거
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackPassSchdBMSC, textColor: clrTxtPassSchdBMSC, borderColor:clrBrdPassSchdBMSC};
			}else{
				//현재
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:clrBackCurrSchdBMSC, textColor: clrTxtCurrSchdBMSC, borderColor:clrBrdCurrSchdBMSC};
			}
			
			//schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#23C6C8", textColor: "#ecf0f1", borderColor:"#1AB394"};
		}
		
		//schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#eeeeee"};
		events.push( schedule );
	}
  		
	
	$('#calendar').fullCalendar({
		schedulerLicenseKey: 'GPL-My-Project-Is-Open-Source',
		defaultView: 'agendaDay',
		editable: true, 			// enable draggable events
		droppable: true, 			// this allows things to be dropped onto the calendar
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
			
		selectable: true,
		selectHelper: true,
		select: function(start, end) {
			var title = prompt('Event Title:');
			var eventData;
			if (title) {
				eventData = {
					title: title,
					start: start,
					end: end
				};
				$('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
			}
			console.log('select');
			$('#calendar').fullCalendar('unselect');
		},
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: events,
		drop: function(event, dayDelta,minuteDelta,allDay,revertFunc) {
			content_id = $(this).attr("data-id");
			gTitle = $(this).attr("data-title");
			
			$(this).remove();
		},
		
		eventReceive: function(event) { // called when a proper external event is dropped
			// console.log('eventReceive', event, ',' , event.start.format(), ',' , content_id);
			g_name = event.title;
			var startTime = event.start.format();
			var endTime = moment(startTime).add(2, 'hours');
			endTime = endTime.format('YYYY-MM-DD[T]HH:mm:ss');
			//console.log('endTime', endTime);
			console.log(content_id ,',', g_name, ',', startTime, ',', endTime );
			addSchedule(content_id, gTitle, startTime, endTime);
			//location.reload();
			
			var b = $('#calendar').fullCalendar('getDate');
		 	var searchDate = b.format('YYYY-MM-DD');
			var tmpServiceAreaId = $("#serviceAreaId").val();
			
			var title = encodeURI($("#form-title").val());
			var category =  encodeURI($("#form-category").val());
			location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category;
		},
		eventDrop: function(event) { // called when an event (already on the calendar) is moved
			console.log('eventDrop2', event, ',',event.start.format(), ',' , event.end.format() , event.url);
			var ret = confirm("It's going to update. are you sure??");
			if (ret)
				modifySchedule(event.url, event.start.format(), event.end.format());
			else
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category;
		}
		,eventResize: function(event, delta, revertFunc){
			console.log('event eventResize', event, ',',event.start.format(), ',' , event.end.format() , event.url);
			console.log('delta', delta, 'revertFunc', revertFunc);
			var ret = confirm("It's going to update. are you sure??");
			
			if (ret)
				modifySchedule(event.url, event.start.format(), event.end.format());
			else
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+ searchDate + "&title=" + title + "&category=" + category;
		}
		
		/*
		, eventAfterRenderfunction: function(event) { // called when an event (already on the calendar) is moved
			console.log('eventAfterRender..', event);
		}
		
		, eventRender: function(event, element) {
			console.log('eventRender', event, element);
	    }
		*/
		, viewRender: function(view, element){
			console.log(view, element);
			var now4compare = replaceAll4Day(moment().format());
			var viewDay4compare = replaceAll4Day(view.start.format());
			// console.log(now4compare , viewDay4compare);
			if (now4compare == viewDay4compare)
				setTimeline(view);
			
//			setInterval(function () {
//		        setTimeline(view);
//			}, 5000);
			
		}
	});
	
    
}
function setTimeline(calView) {
    console.log('calView.name =',calView.name )
	
   if(jQuery(".timeline").length == 0){
      jQuery(".fc-time-grid-container").prepend("<div style='width:100%;overflow: visible;'><hr class='timeline'/></div>") 
    }
	
    var timeline = jQuery(".timeline");  

    var now = moment();
    var day = parseInt(now.format("e"))
    var width =  10000;
    var height =  40;
    var left = 50;
    //var top = ( (now.hours()*3600)+(now.minutes()*60)+now.seconds() )/86400;;
    var position = now.hours() + now.minutes() / 60 ;
    
    console.log('now.hours()=',now.hours(), ', width=',width,', height=',height,', left=',left,', position=',position);
    
    var top = height * position;
    console.log('top=',top);
    
    timeline
    .css('width',width+"px")
    .css('left',left+"px")
    .css('top',top+"px") 

}

function modifySchedule(url, startTime, endTime){
	
	var id = url.substring(url.indexOf("=")  + 1, url.indexOf("&BCID"));
	var BCID = url.substring(url.indexOf("&BCID=") + 6);
	
	if (BCID == 'undefined')
		BCID = "";
	
	console.log('modifySchedule:', id,',' ,BCID);
	
	var param = {
			scheduleId : id,
			BCID : BCID,
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
	
function addSchedule(content_id, g_name, startTime, endTime){
	var param = {
			serviceAreaId : $("#serviceAreaId").val(),
			contentId : content_id,
			titleName : g_name,
			startTime : startTime,
			endTime: endTime
		};
		
	$.ajax({
		type : "POST",
		url : "addScheduleWithInitContent.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			alert('Success to add schedule');
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