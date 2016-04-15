//inbo add START
var g_ServiceAreaId = '';
var g_bmscId = '';
var searchDate

$(function() {

	searchDate = $("#searchDate").val();
	//callTimetable(g_ServiceAreaId, searchDate);
	
	$("#btnScheduleDetail").click(function() {
		if (g_ServiceAreaId == ''){
			alert('Please, choose ServiceArea.')
			return;
		}
		location.href = "schdMgmtDetail.do?bmscId=" + g_bmscId + "&serviceAreaId=" + g_ServiceAreaId + "&searchDate="+searchDate;
	});
});
//inbo add END

function callTimetable(bmscId, serviceAreaId_val){
	var param = {
			bmscId : bmscId
			, serviceAreaId : serviceAreaId_val
			, searchDate	: searchDate
		};
		
		$.ajax({
			type : "POST",
			url : "getSchedule.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				setTimeTable(data);
		
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
}


function setTimeTable(data ){
	var contents = data.contents;
	var maxPosition = data.maxPosition;
	var viewStartHour = data.viewStartHour;
	var timetable = new Timetable();
	//현재시점에서 2시전, 끝까지.
	timetable.setScope(viewStartHour,0);
	var arrayPosition = [];
	
	for (var i = 0; i < maxPosition; i++){
		arrayPosition[i] = 'position' + i;
		console.log('idx=', i , ', ap=',arrayPosition[i]);
	}
	
	timetable.addLocations(arrayPosition);
	var start_hour = 0
	var start_mins = 0;
	var end_hour = 0;
	var end_mins = 0;
	var beforPosition = -1;
	for ( var i=0; i < contents.length; i++) {
		var name = contents[i].name;
		var start_year = contents[i].start_year;
		var start_month = contents[i].start_month;
		var start_day = contents[i].start_day;
		start_hour = contents[i].start_hour;
		start_mins = contents[i].start_mins;
		
		var end_year = contents[i].end_year;
		var end_month = contents[i].end_month;
		var end_day = contents[i].end_day;
		end_hour = contents[i].end_hour;
		end_mins = contents[i].end_mins;
		
		var position = contents[i].depthPosition;
		if (position == beforPosition && position != 0 )
			position--;
		var serviceId = contents[i].serviceId;
		beforPosition = position;
		console.log('idx=', i ,', position =' , position);
		timetable.addEvent(contents[i].NAME, 'position' + position, 
									new Date(start_year,start_month, start_day,start_hour,start_mins ),
				 					new Date(end_year,end_month, end_day,end_hour,end_mins ),
				 					'', contents[i].serviceId);
	}
	
	var renderer = new Timetable.Renderer(timetable);
	renderer.draw('.timetable');
	setTimeline(maxPosition, viewStartHour);
}


function setTimeline(maxRow, viewStartHour) {
   	
   if(jQuery(".timeline").length == 0){
      jQuery(".room-timeline").prepend("<div style='width:100%;overflow: visible;'><hr class='timeline'/></div>") 
    }
	
    var timeline = jQuery(".timeline");  

    var now = moment();
    var day = parseInt(now.format("e"))
    var width =   24;
    var height =  46;
    
    //var top = ( (now.hours()*3600)+(now.minutes()*60)+now.seconds() )/86400;;
    var position = ((now.hours() - viewStartHour) + (now.minutes() / 60)) * 4 ;
    
    console.log('now.hours()=',now.hours(), ', height=',height,', left=',left,', position=',position);
    
    var rowsHeight= height * maxRow ;
    var left = width * position;
    console.log('top=',top);
    
    timeline
    .css('left',left+"px")
    .css('height',rowsHeight+"px")
    //.css('top',top+"px") 
}
