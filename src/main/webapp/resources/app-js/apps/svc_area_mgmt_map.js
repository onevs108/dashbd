
var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 37.489052, lng: 127.066331},
    zoom: 8
  });
  
var data = [{lat: 37.489052, lng: 127.066331}, {lat: 37.557046, lng: 126.946351 }, {lat: 37.589867, lng: 127.058592}, {lat: 37.583838, lng: 126.909788}, {lat: 37.564268, lng: 126.902408}];
  var markers = [];
  /*
  for (var i = 0; i < 7; i++) {
    var latLng = new google.maps.LatLng(data[i][lat],
        data[i][lng]);
    var marker = new google.maps.Marker({'position': latLng});
    markers.push(marker);
  }*/
  
  var marker = new google.maps.Marker({'position': new google.maps.LatLng(37.489052, 127.066331)});
  markers.push(marker);
  
  marker = new google.maps.Marker({'position': new google.maps.LatLng(37.557046, 126.946351)});
  markers.push(marker);
  
  marker = new google.maps.Marker({'position': new google.maps.LatLng(37.589867, 127.058592)});
  markers.push(marker);
  
  var markerCluster = new MarkerClusterer(map, markers);
  
  alert("zoom level="+map.getZoom());
  
  alert($(EnbList).serviceAreaId);
}

