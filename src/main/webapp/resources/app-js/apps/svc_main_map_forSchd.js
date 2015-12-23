var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;
var g_ServiceAreaId = '3048';
var searchDate
$(function() {

	searchDate = $("#searchDate").val();
	//callTimetable(g_ServiceAreaId, searchDate);
	
	$("#btnScheduleDetail").click(function() {
		location.href = "schdMgmtDetail.do?serviceAreaId=" + g_ServiceAreaId + "&searchDate="+searchDate;
	});
});

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: default_lat, lng: default_lng},
    zoom: default_zoom
  });
 
}

function moveToEnb(bmscId, serviceAreaId)
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
	                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
	                } else if( enb_datas[i].serviceAreaId == '' ) {
	                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
	                } else {
	                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
	                }
	                markers.push(marker);
	              }
	              
	              map.setCenter(new google.maps.LatLng(enb_datas[0].latitude, enb_datas[0].longitude));
	        	  map.setZoom(12);
	          }
	  });
	
	g_ServiceAreaId = serviceAreaId;
	callTimetable(g_ServiceAreaId, searchDate);

	  
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
            for(var i=0; i<dataLen; i++){
          		  options += '<li><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');">' + datas[i].serviceAreaId + '</a></li>';
            }

            $("#service_area").empty();
            $("#service_area").append(options);
            
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
    			
    			$("#service_area").append(pageination);
            }
        }
    });
}


function drawServiceAreaByBmSc(bmscId) {
	clearMarkers();
	$("#service_area").empty();
	
	$.ajax({
        url : "/dashbd/api/getServiceAreaCountByBmSc.do",
        type: "get",
        data : { "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = "";
            var seoulCount = 0;
            var gyounggiCount = 0;
            var seoulCode = "";
            var gyounggiCode = "";
            for(var i=0; i<dataLen; i++){
          	  if( data[i].city == '02' ) {
          		  seoulCount = data[i].count;
          		  seoulCode = data[i].city;
          	  }
          	  
          	  if( data[i].city == '031' ) {
          		  gyounggiCount = data[i].count;
          		  gyounggiCode = data[i].city;
        	  }
            }
            
            var seoul = {lat: 37.565018, lng: 126.989166};
            var gyounggi = {lat: 37.421893, lng: 127.508270};

            var marker = new google.maps.Marker({
              position: seoul,
              map: map,
              title: '서울',
              label: '' + seoulCount
            });

            marker.addListener('click', function() {
            	getServiceAreaByBmScCity(1, data[0].bmscId, seoulCode);
            });
            
            markers.push(marker);

            marker = new google.maps.Marker({
        	    position: gyounggi,
        	    map: map,
        	    title: '경기도',
        	    label: '' + gyounggiCount
            });

            marker.addListener('click', function() {
            	getServiceAreaByBmScCity(1, data[0].bmscId, gyounggiCode);
            });
            
            markers.push(marker);
            
            map.setCenter(new google.maps.LatLng(default_lat, default_lng));
            map.setZoom(default_zoom);
        }
    });
}

function clearMarkers() {

	for (var i = 0; i < markers.length; i++) {
	    markers[i].setMap(null);
	}
	
	markers = [];
}


function callTimetable(serviceAreaId_val, searchDate){
	var param = {
			serviceAreaId : serviceAreaId_val
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
	var viewStartHour = data.viewStartHour;
	var timetable = new Timetable();
	//현재시점에서 2시전, 끝까지.
	timetable.setScope(viewStartHour,0);
    timetable.addLocations(['depth1', 'depth2', 'depth3']);
	var beforeEndHM1 = 0;
	var beforeEndHM2 = 0;
	var currStartHM = 0;
	var start_hour = 0
	var start_mins = 0;
	var end_hour = 0;
	var end_mins = 0;
	var depth= 'init';
	for ( var i=0; i < contents.length; i++) {
		beforeEndHM2 = beforeEndHM1; 
		beforeEndHM1 = end_hour + '' + end_mins;
		
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
		
		//depth 계산
		currStartHM = start_hour + '' + start_mins;
		
		if (i == 0){
			depth= 'depth1';
		}else{
			if (currStartHM < beforeEndHM1){
				depth= 'depth2';
			
				if (beforeEndHM2 != 0){
					if (currStartHM < beforeEndHM2)
						depth= 'depth3';
				}
			}else{
				depth= 'depth1';
			}
		}
		console.log('idx=', i ,'currStartHM=', currStartHM ,',bef1=',beforeEndHM1 ,',bef2=',beforeEndHM2,',depth level =' , depth);
		timetable.addEvent(contents[i].NAME, depth, 
									new Date(start_year,start_month, start_day,start_hour,start_mins ),
				 					new Date(end_year,end_month, end_day,end_hour,end_mins ),
				 					'#');
	}
	
	var renderer = new Timetable.Renderer(timetable);
    
	renderer.draw('.timetable');
    
        /*
        timetable.addEvent('Sightseeing', 'depth1', new Date(2015,7,17,10,45), new Date(2015,7,17,12,30), '#');
        timetable.addEvent('Zumba', 'depth2', new Date(2015,7,17,12), new Date(2015,7,17,13), '#');
        timetable.addEvent('Zumbu', 'depth2', new Date(2015,7,17,13,30), new Date(2015,7,17,15), '#');
        timetable.addEvent('Lasergaming', 'depth3', new Date(2015,7,17,17,45), new Date(2015,7,17,19,30), '#');
        timetable.addEvent('All-you-can-eat grill', 'depth4', new Date(2015,7,17,21), new Date(2015,7,18,1,30), '#');
        */
}
