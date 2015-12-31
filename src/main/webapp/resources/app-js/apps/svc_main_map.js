var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;

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
				marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/bs_ico_r_mini.png'});
				} else if( enb_datas[i].serviceAreaId == '' ) {
				marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/bs_ico_g_mini.png'});
				} else {
				marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/bs_ico_b_mini.png'});
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
				content += "<div class=\"file-box\">";
				content += "<span class=\"corner\"></span>";
				content += "<div class=\"image\">";
				content += "<img alt=\"image\" class=\"img-responsive\" src=\"img/p1.jpg\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += datas[i].scheduleName;
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}

			if(datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No thumbnail<br/>";
				content += "</p>";
				content += "<small>현재 방송 중인 서비스가 없습니다.</small>";
				content += "</div>";
			}
			
			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append('(Service Area ' + serviceAreaId + ')');
            
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
            
			options += "<div class=\"ibox-title\"><h5>Service Area for " + city + "</h5></div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th>SA_ID</th><th>Description</th></tr></thead>";
			options += "<tbody>";

            for(var i = 0; i < dataLen; i++ ) {
            	//options += '<li><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');">' + datas[i].serviceAreaId + '</a></li>';
            	//options += '<tbody><tr><td>' + datas[i].serviceAreaId + '</td><td>' + datas[i].serviceAreaName + '</td></tr></tbody>';
            	//options += '<ul class="service_area_box list-inline"><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></a></ul>';
            	//options += '<a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ', ' + datas[i].serviceAreaName + ');"><ul class="service_area_box list-inline"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></ul></a>';
            	options += "<tr><td>";
            	options += datas[i].serviceAreaId;
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td></tr>";
            }

            options += "</tbody>";
            
            if(dataLen > 10) {
            	alert(dataLen);
            	options += "<tfoot><tr><td colspan=\"2\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
        
            alert(options);
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
            $('.footable2').footable();
            
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
        }
    });
}


function drawServiceAreaByBmSc(bmscId, bmscName) {
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
						label: '' + data[i].count
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
				content += "<span class=\"corner\"></span>";
				content += "<div class=\"image\">";
				content += "<img alt=\"image\" class=\"img-responsive\" src=\"img/p1.jpg\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += datas[i].scheduleName;
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}
			
			if(datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No thumbnail<br/>";
				content += "</p>";
				content += "<small>현재 방송 중인 서비스가 없습니다.</small>";
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

