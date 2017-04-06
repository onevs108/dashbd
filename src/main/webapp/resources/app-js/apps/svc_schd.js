//inbo add START
var g_ServiceAreaId = '';
var g_ServiceGroupId = '';
var g_bmscId = '793';
var searchDate= $("#searchDate").val();

$(function() {

	searchDate = $("#searchDate").val();
	//callTimetable(g_ServiceAreaId, searchDate);

	$("#btnScheduleDetail").click(function() {
		var type = $("input[name='radio']:checked").val();
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
		location.href = "schdMgmtDetail.do?bmscId=" + g_bmscId + "&serviceAreaId=" + g_ServiceAreaId + "&searchDate="+searchDate + "&type="+type;
	});
});
//inbo add END

// national 일 경우 전체 서클 넣어주기
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

function callTimetable(bmscId, serviceAreaId_val, type){
	if(type == "national" || type == "emergency"){
		serviceAreaId_val = setAllCircleSaid();
	}
	if(serviceAreaId_val == ""){
		alert('Please, choose ServiceArea.');
		return;
	}
	var param = {
			  bmscId 		: bmscId
			, serviceAreaId : serviceAreaId_val
			, searchDate	: searchDate
			, serviceType	: $("#serviceType").val()
			, serviceClass	: $("#serviceClass").val()
			, type			: type
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
				//alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
}


function setTimeTable(data){
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
		if(serviceId == undefined){
			serviceId = "-";
		}
		beforPosition = position;
		
		var link = 'schedule.do?id='+contents[i].ID+'&BCID='+contents[i].BCID+'&type='+$("input[name='radio']:checked").val();
		if(userGrade == 9999 && (contents[i].national_yn == "Y" || contents[i].emergency_yn == "Y")){
			link = "#";
		}
		timetable.addEvent(contents[i].NAME, 'position' + position, 
									new Date(start_year,start_month, start_day,start_hour,start_mins ),
				 					new Date(end_year,end_month, end_day,end_hour,end_mins ), link, serviceId);
	}
	
	var renderer = new Timetable.Renderer(timetable);
	renderer.draw('.timetable');
	for (var i = 0; i < $(".time-entry").length; i++) {
		for (var j = 0; j < contents.length; j++) {
			if(contents[j].ID == $(".time-entry")[i].href.split("&")[0].split("=")[1]){
				if($($(".time-entry")[i]).children().text().indexOf("(-)") > -1){
					$($(".time-entry")[i]).css("background-color", "#3CAEFF");
				}
				$(".time-entry")[i].title = "contentName : " + $(".time-entry")[i].title +"\nserviceType : "+contents[j].service +"\nsaid : "+contents[j].service_area_id;
				continue;
			}
		}
	}

	setTimeline(maxPosition, viewStartHour);
}

function setTimeline(maxRow, viewStartHour) {
   	
   if(jQuery(".timeline").length == 0){
      jQuery(".room-timeline").prepend("<div style='width:100%; overflow: visible;'><hr class='timeline'/></div>") 
    }
	
    var timeline = jQuery(".timeline");

    var now = moment();
    var day = parseInt(now.format("e"))
    var width =   24;
    var height =  46;
    
    //var top = ( (now.hours()*3600)+(now.minutes()*60)+now.seconds() )/86400;
    var position = ((now.hours() - viewStartHour) + (now.minutes() / 60)) * 4 ;
    
    console.log('now.hours()=',now.hours(), ', height=',height,', left=',left,', position=',position);
    
    var rowsHeight= height * maxRow;
    var left = width * position;
    console.log('top=',top);
    
    timeline
    .css('left',left+"px")
    .css('height',rowsHeight+"px")
    //.css('top',top+"px") 
    $('section').css("width", "100%");
}
