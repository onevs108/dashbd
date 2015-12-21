var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 37.489052, lng: 127.066331},
    zoom: 8
  });
  
  
}

var enb_datas;

$.ajax({
        url : "/dashbd/api/getServiceAreaEnbAp.do",
        type: "get",
        data : { "serviceAreaId" : 2013 },
        success : function(responseData){
            $("#ajax").remove();
            enb_datas = JSON.parse(responseData);
            var dataLen = enb_datas.length;
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<li><a href="javascript:moveToServiceAreaMgmt(' + enb_datas[i].enbApId + ');">' + enb_datas[i].enbApName + '</a></li>';
            }

            $("#enb_ap_in_service_area").empty();
            $("#enb_ap_in_service_area").append(options);
            
            var markers = [];
            
            for (var i = 0; i < enb_datas.length; i++) {
              var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
              var marker = new google.maps.Marker({'position': latLng});
              markers.push(marker);
            }
            
            var mcOptions = {gridSize: 5000, averageCenter:false};
            var markerCluster = new MarkerClusterer(map, markers, mcOptions);
            alert(markerCluster.getGridSize());
        }
    });



