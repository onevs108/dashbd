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
	for ( var i = 0; i < contents.length; i++) {
		var id = contents[i].ID;
		var name = contents[i].NAME;
		var broadcast_info_id = contents[i].BCID;
		
		var start_date = contents[i].start_date;
		var end_date = contents[i].end_date;
		var url = "schedule.do?id=" + id + "&BCID=" + broadcast_info_id;

		if (broadcast_info_id == null || broadcast_info_id == "")
			schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#dddddd", textColor: "#787A7C", borderColor:"#bbbbbb"};
		else
			schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#23C6C8", textColor: "#ecf0f1", borderColor:"#1AB394"};
		
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
			
			setInterval(function () {
		        //$('.timeline').remove();
		        setTimeline();
		    }, 5000);
			
		}
	});
	
    
}

function setTimeline(view) {
	//console.log('view=', view);
	//I get the div where the line will be shown.
       var parentDivAux = jQuery(".fc-time-area.fc-widget-content").children().children().children().children();
       var parentDiv = jQuery();
       for (var i = 0; i < parentDivAux.length; i++) {
	//I need only (i think so) the fc-bg, cause fc-content is defined on multiple ocassions and line would be painted multiple times.
           if (parentDivAux[i].className === "fc-bg") {
               parentDiv.push(parentDivAux[i]);
           }
       }

       var timeline = parentDiv.children(".timeline").children();
       
       console.log('timeline.length=', timeline.length);
       if (timeline.length == 0) { //if timeline isn't there, add it
         timeline = jQuery("<hr>").addClass("timeline");

       //my calendar starts at 9 am and finishes at 9 pm, so I get the total amount of seconds from midnight to 9 am and from 9 am to 9 pm
          var secondsHourMin = 9 * 3600;
          var totalSeconds = 12 * 3600 ;

	//I get the width of the div where the line will be painted
          var widthAux = jQuery(".fc-time-area.fc-widget-content").children().children().children();
          var width = widthAux.width();

       //we obtain the current time/date to know where we should draw the vertical line
          var curTime = new Date();
          var curSeconds = (curTime.getHours() * 60 * 60) + (curTime.getMinutes() * 60) + curTime.getSeconds() - secondsHourMin;
          var percentOfDay = curSeconds / totalSeconds; //totalSeconds = 12 hours

	//I calculate the margin with a known width and the actual percentOfDay
          var margin = (percentOfDay * width);
          console.log('margin=', margin, ', width=', width,', percentOfDay=', percentOfDay);
	//margin is applied and finally I prepend the timeline.
          timeline.css({
              top: '300px', 
              left: margin + "px",
              height: "100%",
              width: 1 + "px"
          });

          parentDiv.prepend(timeline);
       }

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
function popupShow(id, name){
	content_id = id;
	g_name = name;
	console.log(id +','+ g_name + ',' + ($(window).width() - 550));
	$(".pbox").css("top", $(window).scrollTop() + 250 + "px");
	$(".pbox").css("left", ($(window).width() - 630) + "px");
	//$(".pbox").css("left", 300 $(window).scrollLeft() + 100 + "px");
	
	$("#popupTitle").html(name);
	$("#addSchedule").show();
}