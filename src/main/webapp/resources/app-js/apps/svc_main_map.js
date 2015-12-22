
var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 36.869872, lng: 127.838728},
    zoom: 7,
    scrollwheel:  false
  });
 
}


function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}

function getAerviceArea(lat, lng)
{
	$.ajax({
        url : "/dashbd/api/getServiceAreaEnbAp.do",
        type: "get",
        data : { "serviceAreaId" : getParameter('serviceAreaId') },
        success : function(responseData){
            $("#ajax").remove();
            enb_datas = JSON.parse(responseData);
            var dataLen = enb_datas.length;
            var options = "";
            var idx = 0;
            for(var i=0; i<dataLen; i++){
          	  if( enb_datas[i].serviceAreaId == getParameter('serviceAreaId') ) {
          		  //options += '<li><a href="/dashbd/resources/serviceAreaMgmt.do?serviceAreaId=' + enb_datas[i].enbApId + '">' + enb_datas[i].enbApName + '</a></li>';
          		  options += '<li><a href="javascript:moveToEnb(' + enb_datas[i].latitude + ', ' + enb_datas[i].longitude + ');">' + enb_datas[i].enbApName + '</a></li>';
          	  }
            }

            $("#enb_ap_in_service_area").empty();
            $("#enb_ap_in_service_area").append(options);
            
            var markers = [];
            
            for (var i = 0; i < enb_datas.length; i++) {
              var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
              var marker;
              if( enb_datas[i].serviceAreaId == getParameter('serviceAreaId') ) {
              	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
              } else if( enb_datas[i].serviceAreaId == '' ) {
              	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
              } else {
              	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
              }
              
              google.maps.event.addListener(marker, 'click', function() {
              	   if (!selecting) return;
              	   var id = this.id;
              	   var index = selectedMarkers.indexOf(id);
              	   if (index>-1) {
              	     this.setIcon('https://maps.gstatic.com/mapfiles/ms2/micons/red-dot.png');
              	     selectedMarkers.splice(index, 1);
              	   } else {
              	     selectedMarkers.push(id);             
              	     this.setIcon('https://maps.gstatic.com/mapfiles/ms2/micons/blue-dot.png');
              	   }
              	});
              
              //markers.push(marker);
            }
            
            
            //var mcOptions = {gridSize: 0, averageCenter:false};
            //var markerCluster = new MarkerClusterer(map, markers, mcOptions);
            //alert(markerCluster.getGridSize());
        }
    });
}


function drawServiceAreaByBmSc(bmscId) {
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
            for(var i=0; i<dataLen; i++){
          	  if( data[i].city == '02' ) {
          		  seoulCount = seoulCount + 1;
          	  }
          	  
          	  if( data[i].city == '031' ) {
          		gyounggiCount = gyounggiCount + 1;
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
            	  getAerviceArea(seoul.lat, seoul.lng);
            	  });

            marker = new google.maps.Marker({
            	    position: gyounggi,
            	    map: map,
            	    title: '경기도',
            	    label: '' + gyounggiCount
            });

            marker.addListener('click', function() {
            	  getAerviceArea(gyounggi.lat, gyounggi.lng);
            });
        }
    });
	


}