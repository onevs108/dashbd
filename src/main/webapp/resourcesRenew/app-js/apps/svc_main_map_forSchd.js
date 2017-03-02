var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;


//function initMap() {
//	map = new google.maps.Map(document.getElementById('map'), {
//		center: {lat: default_lat, lng: default_lng},
//		zoom: default_zoom,
//		// Style for Google Maps
//		//styles: [{"featureType":"water","stylers":[{"saturation":43},{"lightness":-11},{"hue":"#0088ff"}]},{"featureType":"road","elementType":"geometry.fill","stylers":[{"hue":"#ff0000"},{"saturation":-100},{"lightness":99}]},{"featureType":"road","elementType":"geometry.stroke","stylers":[{"color":"#808080"},{"lightness":54}]},{"featureType":"landscape.man_made","elementType":"geometry.fill","stylers":[{"color":"#ece2d9"}]},{"featureType":"poi.park","elementType":"geometry.fill","stylers":[{"color":"#ccdca1"}]},{"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#767676"}]},{"featureType":"road","elementType":"labels.text.stroke","stylers":[{"color":"#ffffff"}]},{"featureType":"poi","stylers":[{"visibility":"off"}]},{"featureType":"landscape.natural","elementType":"geometry.fill","stylers":[{"visibility":"on"},{"color":"#b8cb93"}]},{"featureType":"poi.park","stylers":[{"visibility":"on"}]},{"featureType":"poi.sports_complex","stylers":[{"visibility":"on"}]},{"featureType":"poi.medical","stylers":[{"visibility":"on"}]},{"featureType":"poi.business","stylers":[{"visibility":"simplified"}]}]
//	});
//	
//	drawServiceAreaByBmSc($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
//}

//google.maps.event.addDomListener(window, 'load', initMap);

function moveToEnb(bmscId, serviceAreaId)
{
	clearMarkers();
	var enb_datas;
	$("#"+serviceAreaId).siblings().removeClass("tr-highlighted");
	$("#"+serviceAreaId).toggleClass("tr-highlighted");
	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbAp.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId },
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
			$("#viewProgram").show();
		}
	});
	
	var summary_datas = "";
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByServiceArea.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId },
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
				content += "<img alt=\"image\" class=\"img-responsive\" src=\"" + summary_datas[i].thumbnail + "\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + summary_datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> " + summary_datas[i].serviceType + "</h5>";
				content += "<small>[" + summary_datas[i].category + "]</small> ";
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
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId },
		success : function(responseData){
			$("#ajax").remove();
			bandwidth_data = JSON.parse(responseData);
			var content = "<h2>" + bandwidth_data.GBRSum + " % is being used</h2>";
			content += "<div class=\"progress progress-big\">";
			content += "<div style=\"width:" + bandwidth_data.GBRSum + "%;\" class=\"progress-bar\"></div>";
			content += "</div>";
            
			$("#bandwidth").empty();
            $("#bandwidth").append(content);
		}
	});
	//inbo add START
	g_ServiceAreaId = serviceAreaId;
	g_bmscId = bmscId;
	callTimetable('', g_ServiceAreaId);
	//inbo add END
}

function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}

function searchToServiceArea(bmscId, city){
	getServiceAreaByBmScCity("1", bmscId, city, $("#toSearchTxt").val());
}

function getServiceAreaByBmScCity(page, bmscId, city, toSearchTxt)
{
    $("#checkCityName").val(city);
	var selectedCity = encodeURIComponent(city);
	
	$.ajax({
        url : "/dashbd/api/serviceAreaByBmScCity.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId, "city" : selectedCity, "toSearchTxt" : encodeURIComponent(toSearchTxt) },
        success : function(responseData){
        	$("#ajax").remove();
            datas = JSON.parse(responseData);
            var dataLen = datas.length;
            var options = "";
            var idx = 0;
            
			options += "<div class=\"ibox-title\"><h5>Service Area for " + city + "</h5></div>";
			options += "<div class=\"ibox-content\">";
			options += "<div class=\"input-group\"><input type=\"text\" class=\"form-control\" id=\"toSearchTxt\" name=\"toSearchTxt\" value=\""+toSearchTxt+"\" placeholder=\"SA_ID or SA_NAME\" />";
			options += "<span class=\"input-group-btn\">";
			options += '<button type="button" class="btn btn-primary" onclick="javascript:searchToServiceArea(\'' + bmscId + '\', \'' + city + '\');" id="toSearchBtn">Search</button>';
			options += "</span>";
			options += "</div>";
			options += "</div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
			options += "<tbody>";

            for(var i = 0; i < dataLen; i++ ) {
            	options += "<tr id=\"" + datas[i].serviceAreaId + "\" class=\"footable-even\" style=\"display: table-row;cursor:pointer;\" onclick=\"javascript:moveToEnb(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ");\"><td>";
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"#\">";
            	options += datas[i].serviceAreaId;
            	options += " (" + datas[i].totalCount + ")";
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
            $("#service_area").find('tr').removeClass("footable-odd");
            //$('.footable2').footable();
            
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
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	map.setZoom( 8 );
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
            				getServiceAreaByBmScCity(1, bmscId, this.title, "");
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
				content += "<img alt=\"image\" class=\"img-responsive\" src=\""+ datas[i].thumbnail + "\">";
				content += '</div>';
				content += "<div class=\"progress progress-mini\">";
				content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> " + datas[i].serviceType + "</h5>";
				content += "<small>[" + datas[i].category + "]</small> ";
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




