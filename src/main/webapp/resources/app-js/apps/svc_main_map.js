var perPage = 15;
var map;
var markers = [];
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;

function initMap() {
//	map = new google.maps.Map(document.getElementById('map'), {
//		center: {lat: default_lat, lng: default_lng},
//		zoom: default_zoom,
//	});
//	
//	google.maps.event.addListener( map, 'idle', function() {
//    } );
//	
//	drawServiceAreaByBmSc($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
}

//google.maps.event.addDomListener(window, 'load', initMap);




function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}




function drawServiceAreaByBmSc(bmscId, bmscName) {
	/*
	google.maps.event.clearListeners( map, 'idle' );
	
	clearMarkers();
	map.setZoom( 8 );
	*/
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
}

function clearMarkers() {

	for (var i = 0; i < markers.length; i++) {
		markers[i].labelVisible = false;
	    markers[i].setMap(null);
	}
	
	markers = [];
}


