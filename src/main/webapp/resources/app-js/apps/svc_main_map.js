var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});
}

google.maps.event.addDomListener(window, 'load', initMap);

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
	
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByServiceArea.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId },
		success : function(responseData){
			$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += '<div class="col-xs-3">';
				content += '<div class="contents-box">';
				content += '<div class="file">';
				content += '<div class="image">';
				content += '<img alt="image" class="img-responsive" src="img/p1.jpg">';
				content += '</div>';
				content += '<div class="file-name">';
				content += datas[i].scheduleName;
				content += '</div>';
				content += '<div class="progress">';
				content += '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: ' + datas[i].progressRate + '%"><span class="sr-only">' + datas[i].progressRate + '% Complete</span></div>';
				content += '</div>';
				content += '</div>';
				content += '</div>';
				content += '</div>';
			}

			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append(serviceAreaId);
            
			$("#schedule_summary").empty();
            $("#schedule_summary").append(content);
		}
	});
	
	drawPieGraph();

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

            /*
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
             */
            
			var marker = new MarkerWithLabel({
				position: seoul,
				draggable: false,
				raiseOnDrag: true,
				map: map,
				labelContent: '' + seoulCount,
				labelAnchor: new google.maps.Point(22, 0),
				labelClass: "labels", // the CSS class for the label
				labelStyle: {opacity: 0.75}
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

function drawPieGraph()
{
	var data = {
		series: [1, 1, 1]
	};

	var sum = function(a, b) { return a + b };

	new Chartist.Pie('#ct-chart5', data, {
		labelInterpolationFnc: function(value) {
			return Math.round(value / data.series.reduce(sum) * 100) + '%';
		}
	});
}

