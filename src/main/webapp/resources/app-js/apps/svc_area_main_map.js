$(document).ready(function()
{
    $('#operator').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
        //alert( $('#bmsc option:selected').val() );
        drawServiceAreaByBmSc($('#bmsc option:selected').val());
    });
});

var perPage = 15;
var listPageCount = 10;

// Service Area 조회 by bmsc id
function getServiceAreaByBmSc(page, bmscId)
{
	$.ajax({
        url : "/dashbd/api/serviceAreaByBmSc.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<li><a href="/dashbd/resources/serviceAreaMgmt.do?serviceAreaId=' + data[i].serviceAreaId + '">' + data[i].serviceAreaId + '</a></li>';
            }

            $("#service_area").empty();
            $("#service_area").append(options);
            
            // Pagination
            var totalCount = data[0].totalCount;
            if(totalCount > perPage) {
            	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
            	//alert(totalPageCount);
            	
            	var pageination = '';
                pageination += '<div class="text-center">';
                pageination += '<ul class="pagination">';
                if( page == 1 )
                {
                	pageination += '<li class="disabled"><a href="javascript:getServiceAreaByBmSc(' + (page-1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + (page-1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                
                if(totalPageCount > listPageCount) {
                	for(var i = page, j = 0; i <= totalPageCount && j < listPageCount ; i++, j++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + i + ',' + bmscId + ');">' + i + '</a></li>';
                    	}
                    }
                }
                else {
                	for(var i = 1; i <= totalPageCount && i <= listPageCount ; i++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + i + ',' + bmscId + ');">' + i + '</a></li>';
                    	}
                    }
                }
                
                
                if( page == totalPageCount ) {
                	pageination += '<li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + (page+1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
    			pageination += '</ul>';
    			pageination += '</div>';
    			
    			$("#service_area").append(pageination);
            }
            
        }
    });
}

// BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId)
{
	$.ajax({
        url : "/dashbd/api/serviceAreaBmScByOperator.do",
        type: "get",
        data : { "page" : page, "operatorId" : operatorId },
        //data : { "page" : page, "operatorId" : 1 },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value=""></option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $("#service_area").empty();
            $("#bmsc").empty();
            $("#bmsc").append(options);
        }
    });
}

// operator 조회
function getServiceAreaOperator(page)
{
	$.ajax({
        url : "/dashbd/api/getServiceAreaOperator.do",
        type: "get",
        data : { "page" : page },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<li><a href="javascript:getServiceAreaBmSc(1, ' + data[i].id + ');">' + data[i].name + '</a></li>';
            }

            $("#operator").empty();
            $("#operator").append(options);
        }
    });
}

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

var area_positions = [];
area_positions["02"] = {lat: 37.549761, lng: 126.991185};
area_positions["031"] = {lat: 37.420170, lng: 127.510520};
area_positions["032"] = {lat: 37.453885, lng: 126.707021};
area_positions["033"] = {lat: 37.826329, lng: 128.150706};
area_positions["041"] = {lat: 36.716505, lng: 126.795210};
area_positions["042"] = {lat: 36.335924, lng: 127.392935};
area_positions["043"] = {lat: 37.003972, lng: 127.699962};
area_positions["051"] = {lat: 35.164310, lng: 129.045659};
area_positions["052"] = {lat: 35.545247, lng: 129.254700};
area_positions["053"] = {lat: 35.829107, lng: 128.565520};
area_positions["054"] = {lat: 36.299079, lng: 128.882347};
area_positions["055"] = {lat: 35.462819, lng: 128.209498};
area_positions["061"] = {lat: 34.875420, lng: 126.987919};
area_positions["062"] = {lat: 35.153629, lng: 126.834405};
area_positions["063"] = {lat: 35.723109, lng: 127.154498};
area_positions["064"] = {lat: 33.421225, lng: 126.795606};

var Seoul = {lat: 37.549761, lng: 126.991185};
var Gyeonggi = {lat: 37.420170, lng: 127.510520};
var Busan = {lat: 35.164310, lng: 129.045659};
var Daejeon = {lat: 36.335924, lng: 127.392935};
var Gwangju = {lat: 35.153629, lng: 126.834405};
var Incheon = {lat: 37.453885, lng: 126.707021};
var Ulsan = {lat: 35.545247, lng: 129.254700};
var Daegu = {lat: 35.829107, lng: 128.565520};
var Jeollanam = {lat: 34.875420, lng: 126.987919};
var Jeollabuk = {lat: 35.723109, lng: 127.154498};
var Gyeongsangnam = {lat: 35.462819, lng: 128.209498};
var Gyeongsangbuk = {lat: 36.299079, lng: 128.882347};
var Chungcheongnam = {lat: 36.716505, lng: 126.795210};
var Chungcheongbuk = {lat: 37.003972, lng: 127.699962};
var Gangwon = {lat: 37.826329, lng: 128.150706};
var Jejudo = {lat: 33.421225, lng: 126.795606};

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
            var city = '';
            for(var i=0; i<dataLen; i++) {
            	if(area_positions[data[i].city]) {
            		city = data[i].city;
            		//var marker = new MarkerWithLabel({
            		var marker = new google.maps.Marker({
						position: area_positions[data[i].city],
						draggable: false,
						raiseOnDrag: true,
						map: map,
						//labelContent: '' + data[i].count,
						//labelAnchor: new google.maps.Point(22, 0),
						//labelClass: "labels", // the CSS class for the label
						//labelStyle: {opacity: 0.75},
						title: city,
						label: '' + data[i].count
            		});
	            
            		//marker.addListener('click', function() {
            		//google.maps.event.addListener(marker, 'click', function() {
            		google.maps.event.addListener(marker, 'click', (function(marker, i) {
            			return function() {
            				getServiceAreaByBmScCity(1, bmscId, city);
            			}
            		})(marker, i));
	            
		            markers.push(marker);
            	}
            }
            


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