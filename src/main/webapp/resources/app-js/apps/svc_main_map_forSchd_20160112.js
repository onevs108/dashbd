var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;
var g_ServiceAreaId = '';
var searchDate

$(function() {

	searchDate = $("#searchDate").val();
	//callTimetable(g_ServiceAreaId, searchDate);
	
	$("#btnScheduleDetail").click(function() {
		if (g_ServiceAreaId == ''){
			alert('Please, choose ServiceArea.')
			return;
		}
		location.href = "schdMgmtDetail.do?serviceAreaId=" + g_ServiceAreaId + "&searchDate="+searchDate;
	});
});

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom,
		// Style for Google Maps
		//styles: [{"featureType":"water","stylers":[{"saturation":43},{"lightness":-11},{"hue":"#0088ff"}]},{"featureType":"road","elementType":"geometry.fill","stylers":[{"hue":"#ff0000"},{"saturation":-100},{"lightness":99}]},{"featureType":"road","elementType":"geometry.stroke","stylers":[{"color":"#808080"},{"lightness":54}]},{"featureType":"landscape.man_made","elementType":"geometry.fill","stylers":[{"color":"#ece2d9"}]},{"featureType":"poi.park","elementType":"geometry.fill","stylers":[{"color":"#ccdca1"}]},{"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#767676"}]},{"featureType":"road","elementType":"labels.text.stroke","stylers":[{"color":"#ffffff"}]},{"featureType":"poi","stylers":[{"visibility":"off"}]},{"featureType":"landscape.natural","elementType":"geometry.fill","stylers":[{"visibility":"on"},{"color":"#b8cb93"}]},{"featureType":"poi.park","stylers":[{"visibility":"on"}]},{"featureType":"poi.sports_complex","stylers":[{"visibility":"on"}]},{"featureType":"poi.medical","stylers":[{"visibility":"on"}]},{"featureType":"poi.business","stylers":[{"visibility":"simplified"}]}]
	});
}

google.maps.event.addDomListener(window, 'load', initMap);

function moveToEnb(bmscId, serviceAreaId, serviceAreaName)
{

	clearMarkers();
	
	var enb_datas;

	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbAp.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId },
		success : function(responseData){
			$("#ajax").remove();
			enb_datas = JSON.parse(responseData);
			var dataLen = enb_datas.length;
			var options = "";
			
			for (var i = 0; i < enb_datas.length; i++) {
				var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
				var marker;
				if( enb_datas[i].serviceAreaId == serviceAreaId ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_red.png'
					});
				} else if( enb_datas[i].serviceAreaId == '' ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_gray.png'
					});
				} else {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_blue.png'
					});
				}
				markers.push(marker);
			}
			
			map.setCenter(new google.maps.LatLng(enb_datas[0].latitude, enb_datas[0].longitude));
			map.setZoom(12);
			
		}
	});
	
	
	
	var summary_datas = "";
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByServiceArea.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId },
		success : function(responseData){
			$("#ajax").remove();
			summary_datas = JSON.parse(responseData);
			var dataLen = summary_datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < summary_datas.length; i++) {
				content += "<div class=\"file-box\">";
				content += "<div class=\"file\">";
				content += "<span class=\"corner\"></span>";
				content += "<div class=\"image\">";
				content += "<img alt=\"image\" class=\"img-responsive\" src=\"img/p1.jpg\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + summary_datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> Streaming</h5>";
				content += "<small>[Sports]</small> ";
				content += summary_datas[i].scheduleName;
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}

			if(summary_datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No Service is available<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}
			
			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append('(Service Area ' + serviceAreaId + ')');
            
			$("#schedule_summary").empty();
            $("#schedule_summary").append(content);
		}
	});
	
	var bandwidth_datas = "";
	$.ajax({
		url : "/dashbd/api/bandwidthByServiceArea.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId },
		success : function(responseData){
			$("#ajax").remove();
			bandwidth_data = JSON.parse(responseData);
			var content = "<h2>" + bandwidth_data.GBRSum + "% is being used</h2>";
			content += "<div class=\"progress progress-big\">";
			content += "<div style=\"width:" + bandwidth_data.GBRSum + "%;\" class=\"progress-bar\"></div>";
			content += "</div>";
            
			$("#bandwidth").empty();
            $("#bandwidth").append(content);
		}
	});
	
	g_ServiceAreaId = serviceAreaId;
	callTimetable('', g_ServiceAreaId);
}

function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}

function getServiceAreaByBmScCity(page, bmscId, city)
{
	$.ajax({
        url : "/dashbd/api/serviceAreaByBmScCity.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId, "city" : city },
        success : function(responseData){
            $("#ajax").remove();
            datas = JSON.parse(responseData);
            var dataLen = datas.length;
            var options = "";
            var idx = 0;
            
			options += "<div class=\"ibox-title\"><h5>Service Area for " + city + "</h5></div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
			options += "<tbody>";

            for(var i = 0; i < dataLen; i++ ) {
            	//options += '<li><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');">' + datas[i].serviceAreaId + '</a></li>';
            	//options += '<tbody><tr><td>' + datas[i].serviceAreaId + '</td><td>' + datas[i].serviceAreaName + '</td></tr></tbody>';
            	//options += '<ul class="service_area_box list-inline"><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></a></ul>';
            	//options += '<a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ', ' + datas[i].serviceAreaName + ');"><ul class="service_area_box list-inline"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></ul></a>';
            	
            	if(i%2 == 0) {
            		options += "<tr class=\"footable-even\" style=\"display: table-row;\"><td>";
            	} else {
            		options += "<tr class=\"footable-odd\" style=\"display: table-row;\"><td>";
            	}
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"javascript:moveToEnb(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ", " + datas[i].serviceAreaName + ");\">";
            	options += datas[i].serviceAreaId;
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td></tr>";
				
            }

            options += "</tbody>";
            
            if(dataLen > 10) {
            	//alert(dataLen);
            	options += "<tfoot><tr><td colspan=\"2\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
        
            //alert(options);
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
            $('.footable2').footable();
            
            /*
            // Pagination
            var totalCount = datas[0].totalCount;
            if(totalCount > perPage) {
            	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
            	//alert(totalPageCount);
            	
            	var pageination = '';
                pageination += '<div class="text-center">';
                pageination += '<ul class="pagination">';
                if( page == 1 )
                {
                	pageination += '<li class="disabled"><a href="javascript:getServiceAreaByBmScCity(' + (page-1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + (page-1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                
                if(totalPageCount > listPageCount) {
                	for(var i = page, j = 0; i <= totalPageCount && j < listPageCount ; i++, j++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + i + ',' + bmscId + ', \'' + city + '\');">' + i + '</a></li>';
                    	}
                    }
                }
                else {
                	for(var i = 1; i <= totalPageCount && i <= listPageCount ; i++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + i + ',' + bmscId + ', \'' + city + '\');">' + i + '</a></li>';
                    	}
                    }
                }
                
                
                if( page == totalPageCount ) {
                	pageination += '<li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + (page+1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
    			pageination += '</ul>';
    			pageination += '</div>';
    			
    			//$("#service_area").append(pageination);
            }
            */
        }
    });
}


function drawServiceAreaByBmSc(bmscId, bmscName) {
	clearMarkers();
	$("#service_area").empty();
	$("#service_area").append(default_service_area);
	
	$.ajax({
        url : "/dashbd/api/getServiceAreaCountByBmSc.do",
        type: "get",
        data : { "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;

            for(var i = 0; i < dataLen; i++) {
            	if(area_positions[data[i].city]) {
            		var marker = new MarkerWithLabel({
						position: area_positions[data[i].city],
						draggable: false,
						raiseOnDrag: true,
						map: map,
						labelContent: '' + data[i].count,
						labelAnchor: new google.maps.Point(22, 0),
						labelClass: "labels", // the CSS class for the label
						labelStyle: {opacity: 0.75},
						title: data[i].city,
						icon: '/dashbd/resources/img/icon/enb_red_on.png'
            		});

            		marker.addListener('click', function() {
            				getServiceAreaByBmScCity(1, bmscId, this.title);
            		});

		            markers.push(marker);
            	}
            }
            
            map.setCenter(new google.maps.LatLng(default_lat, default_lng));
            map.setZoom(default_zoom);
        }
    });
	
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByBmsc.do",
		type: "get",
		data : { "bmscId" : bmscId },
		success : function(responseData){
			$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += "<div class=\"file-box\">";
				content += "<div class=\"file\">";
				content += "<span class=\"corner\"></span>";
				content += "<div class=\"image\">";
				content += "<img alt=\"image\" class=\"img-responsive\" src=\"img/p1.jpg\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> Streaming</h5>";
				content += "<small>[Sports]</small> ";
				content += datas[i].scheduleName;
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}
			
			if(datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No Service is available<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}

			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append('(' + bmscName + ')');
            
			$("#schedule_summary").empty();
            $("#schedule_summary").append(content);
		}
	});
}

function clearMarkers() {

	for (var i = 0; i < markers.length; i++) {
		markers[i].labelVisible = false;
	    markers[i].setMap(null);
	}
	
	markers = [];
}

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
		
		var position = 'position' + contents[i].depthPosition;
		console.log('idx=', i ,', position =' , position);
		timetable.addEvent(contents[i].NAME, position, 
									new Date(start_year,start_month, start_day,start_hour,start_mins ),
				 					new Date(end_year,end_month, end_day,end_hour,end_mins ),
				 					'');
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


