var initBmScId = 1;
var modalMap;

$(document).ready(function()
{
	$("#circleName").on("change", function(){
		checkCircle = false;
	});
	$("#circleId").on("change", function(){
		checkSAID = false;
	});
	$("#cityId").on("change", function(){
		checkSAID = false;
	});
	//getServiceAreaBmSc(1, $('#operator option:selected').val());
	$('#createServiceAreaLayer').on('hidden.bs.modal', function (e) {
		$('#createServiceAreaLayer').find('input').val('');
	})
    $('#operator').change(function(){
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
    	$("#viewEnbIDAdd").hide();
//    	$("#viewEnbIDList").hide();
//        drawServiceAreaByBmSc($('#bmsc option:selected').val());
        $('#toAddENBsBmscId').val($('#bmsc option:selected').val());
    });
    
    $('#btn-add-service-area').click(function(){
    	
    	if( isEmpty( $('#operator option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select operator first."
            });
    	} else if( isEmpty( $('#bmsc option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select BM-SC first."
            });
    	} else {
    		$("#createServiceAreaLayer").modal();
    	}
    });
    
    $('#btn-not-mapped-service-area').click(function() {
    	if( isEmpty( $('#operator option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select operator first."
            });
    	} else if( isEmpty( $('#bmsc option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select BM-SC first."
            });
    	} else {
    		getSeviceAreaNotMapped( $('#bmsc option:selected').val() );
    	}
    });
    
    $('#createSvcAreaBtn').click(function(){

    	if( isEmpty($('#serviceAreaId').val()) ) {
    		swal({
                title: "Sorry !",
                text: "Please input Service Area ID first."
            });
    	} else if( isEmpty($('#serviceAreaName').val()) ) {
    		swal({
                title: "Sorry !",
                text: "Please input Service Area Name first."
            });
    	} else {
    		$('#createServiceAreaLayer').modal('hide');
    	
    		createServiceArea( $('#operator option:selected').val(), $('#bmsc option:selected').val());
    	}
    });

    
    $('#addHotSpotBtn').click(function(){
    	if(!checkSAID){
    		swal({
                title: "Warning !",
                text: "Please Check SAID!"
            });
    		return;
    	}
    	$('#addHotSpotModal').modal('hide');
    	$.ajax({
            url : "/dashbd/hotspot/insertHotSpot.do",
            type: "post",
            data : { 
            	"hotSpotId" : $("#hotSpotId").val(),
            	"hotSpotName" : $("#hotSpotName").val(),
            	"cityId" : $("#cityId").val(),
    			"cityName" : $("#cityName").val(),
            	"latitude" : $("#latitude").val(),
            	"longitude" : $("#longitude").val(),
            	"bandwidth" : $("#bandwidth").val(),
            	"description" : $("#description").val()
            },
            success : function(data){
            	if(data == "SUCCESS"){
            		swal({
                        title: "Success !",
                        text: "HotSpot is created!"
                    });
            		hotSpotReload($("#cityId").val(), $("#latitude").val(), $("#longitude").val());
            	} else {
            		swal({
                        title: "Error !",
                        text: "Insert Fail."
                    });
            	}
            },
            error : function(xhr, status, error){
            	swal({
                    title: "Fail !",
                    text: "서버 통신 오류 입니다."
                });
            }
        });
    });
    
    $('#editCircleBtn').click(function(){
    	$('#addCircleModal').modal('hide');
    	$.ajax({
    		url : "/dashbd/api/insertCircle.do",
    		type: "post",
    		data : { 
    			"circleId" : $("#circleId").val(),
    			"circleName" : $("#circleName").val(),
    			"latitude" : $("#latitude").val(),
    			"longitude" : $("#longitude").val()
    		},
    		success : function(data){
    			if(data == "SUCCESS"){
    				swal({
    					title: "Success !",
    					text: "Circle is updated!"
    				});
    				setTimeout(() => {
    					location.reload();
    				}, 1000);
    			} else {
    				swal({
    					title: "Error !",
    					text: "Update Fail."
    				});
    			}
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "서버 통신 오류 입니다."
    			});
    		}
    	});
    });
    
    $('#addCityBtn').click(function(){
    	if(!checkSAID){
    		swal({
                title: "Warning !",
                text: "Please Check SAID!"
            });
    		return;
    	}
    	$('#addCityModal').modal('hide');
    	$.ajax({
    		url : "/dashbd/api/insertCity.do",
    		type: "post",
    		data : { 
    			"cityId" : $("#cityId").val(),
    			"cityName" : $("#cityName").val(),
    			"circleId" : $("#circleId").val(),
    			"circleName" : $("#circleName").val(),
    			"latitude" : $("#cityLatitude").val(),
    			"longitude" : $("#cityLongitude").val(),
    			"bandwidth" : $("#cityBandwidth").val(),
    			"description" : $("#cityDescription").val()
    		},
    		success : function(data){
    			if(data == "SUCCESS"){
    				swal({
    					title: "Success !",
    					text: "City is created!"
    				});
    				setTimeout(() => {
    					location.reload();
    				}, 2000);
    			} else {
    				swal({
    					title: "Error !",
    					text: "Update Fail."
    				});
    			}
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "서버 통신 오류 입니다."
    			});
    		}
    	});
    });
    
    $('#editCityBtn').click(function(){
    	$('#addCityModal').modal('hide');
    	$.ajax({
    		url : "/dashbd/api/insertCity.do",
    		type: "post",
    		data : { 
    			"cityId" : $("#cityId").val(),
    			"cityName" : $("#cityName").val(),
    			"circleId" : $("#circleId").val(),
    			"circleName" : $("#circleName").val(),
    			"latitude" : $("#cityLatitude").val(),
    			"longitude" : $("#cityLongitude").val(),
    			"bandwidth" : $("#cityBandwidth").val(),
    			"description" : $("#cityDescription").val()
    		},
    		success : function(data){
    			if(data == "SUCCESS"){
    				swal({
    					title: "Success !",
    					text: "City is updated!"
    				});
    				setTimeout(() => {
    					location.reload();
    				}, 2000);
    			} else {
    				swal({
    					title: "Error !",
    					text: "Update Fail."
    				});
    			}
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "서버 통신 오류 입니다."
    			});
    		}
    	});
    });
    
    $("#selectCircle").on("change", function(e){
    	if(e.target[0].value == ""){
    		e.target[0].remove();
    	}
    	clearHotSpot();
    	clearCity();
    	$("#selectCity").show();
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var circleId = array[0];
    	var circleName = array[1];
    	var latitude = array[2];
    	var longitude = array[3];
    	$.ajax({
    		url : "/dashbd/hotspot/getCityListFromCircleId.do",
    		type: "post",
    		data : { "circleId" : circleId },
    		success : function(data) {
    			var json = JSON.parse(data).result;
    			var html = "<option value=''>Select City</option>";
    			for (var i = 0; i < json.length; i++) {
            		html += "<option value='"+json[i].city_id+"^"+json[i].city_name+"^"+json[i].latitude+"^"+json[i].longitude+"'>"+json[i].city_name+"</option>";
				}
            	
            	for (var i = 0; i < json.length; i++) {
            		thisCityMap[json[i].city_id] = {
            			center: {lat: parseFloat(json[i].latitude), lng: parseFloat(json[i].longitude)},		
            			title: json[i].city_name,
            			bandwidth: json[i].bandwidth,
            			description: json[i].description,
            		    population: 10000																		
            		}
    			}
            	
            	drawCity(thisCityMap, "#FF0000", circleId, circleName);	//빨강
            	
            	$("#selectCity").html(html);
    			map.setZoom(8);
    			map.setCenter(new google.maps.LatLng(latitude, longitude));
            	
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "서버 통신 오류 입니다."
    			});
    		}
    	});
    });
    
    $("#selectCity").on("change", selectCity);
    
    $("#addHostSpot").on("click", addHostSpot);
    
    $("#reset").on("click", function(e){
    	callSetLocationModalMap(this, 'hotspotAdd', 'hotspot', $("#cityLatitude").val(), $("#cityLongitude").val());
    });
});

function selectCity(e){
	if(e.target[0].value == ""){
		e.target[0].remove();
	}
	clearCity();
	clearHotSpot();
	$("#selectCity").show();
	$("#addTable").show();
	var array = e.target[e.target.selectedIndex].value.split("^");
	var cityId = array[0];
	var cityName = array[1];
	var latitude = array[2];
	var longitude = array[3];
	$("#cityId").val(cityId);
	$("#cityName").val(cityName);
	$("#cityLatitude").val(latitude);
	$("#cityLongitude").val(longitude);
	google.maps.event.addListener(map, 'rightclick', cityRightClickEmpty);
	$.ajax({
		url : "/dashbd/hotspot/getHotSpotListFromCityId.do",
		type: "post",
		data : { "cityId" : cityId },
		success : function(data) {
			var json = JSON.parse(data).result;
			
			for (var i = 0; i < json.length; i++) {
				hotSpotMap[json[i].hotspot_id] = {
						center: {lat: parseFloat(json[i].latitude), lng: parseFloat(json[i].longitude)},		
						title: json[i].hotspot_name,
						bandwidth: json[i].bandwidth,
						description: json[i].description,
				}
			}
			
			drawHotSpot(hotSpotMap, "#FF0000", cityId);	//빨강
			
			map.setZoom(16);
			map.setCenter(new google.maps.LatLng(latitude, longitude));
			
			getHotSpotList(cityId);
			
		},
		error : function(xhr, status, error){
			swal({
				title: "Fail !",
				text: "서버 통신 오류 입니다."
			});
		}
	});
}

function addHostSpot(){
	$("#addHotSpotBtn").show();
	$("#editHotSpotBtn").hide();
	$("div .modal-body input").val("");
	$("div .modal-body textarea").val("");
	$("#addHotSpotModal").modal();
}

function drawHotSpot(hotSpotMap, color, cityId) {
	var infowindow;
	for (var hotSpot in hotSpotMap) {
		hotSpotMarker = new google.maps.Marker({
			position: hotSpotMap[hotSpot].center, 
			map: map, 
			icon : '/dashbd/resources/img/icon/ico_number_1_3.png',
			selected : false,
			id : hotSpot,
			title : hotSpotMap[hotSpot].title,
			longitude : hotSpotMap[hotSpot].center.lng,
			latitude : hotSpotMap[hotSpot].center.lat,
			bandwidth : hotSpotMap[hotSpot].bandwidth,
			description : hotSpotMap[hotSpot].description
		});
	    
		hotSpotMarker.addListener('mouseover', function(e){
			var contentString = "<b>"+this.title+"</b><br>SAID : "+this.id+"<br>Longitude : "+this.longitude+"<br>Latitude : "+this.latitude;
    		infowindow = new google.maps.InfoWindow({
    			content: contentString
    		});
    		infowindow.open(map, this);
		});
		
		hotSpotMarker.addListener('mouseout', function(e){
			infowindow.close();
		});

		hotSpotMarker.addListener('rightclick', function(e){
			infowindow.close();
			var contentString = "<b>"+this.title+"</b><br><button class='btn btn-success btn-xs' onclick='editHotSpot(\""+this.id+"\",\""+this.title+"\",\""+this.position.lat()+"\",\""+this.position.lng()+"\",\""+this.bandwidth+"\",\""+this.description+"\")'>Edit</button>" +
								"<button class='btn btn-success btn-xs' onclick=deleteHotSpot("+this.id+")>Detele</button>";
    		var infowindow2 = new google.maps.InfoWindow({
    			content: contentString
    		});
    		infowindow2.open(map, this);
	    });
		
	    hotSpotMarkers.push(hotSpotMarker);
	}
}

var perPage = 15;
var listPageCount = 10;

// BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId)
{
    $("#service_area").empty();
    $("#service_area").append(default_service_area);
    $("#enb_table").empty();
	$("#enb_table").append(default_enb_table);
	$("#selectedSvcArea").empty();
    $("#selectedENBs").empty();
	
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
            for( var i = 0; i < dataLen; i++ ) {
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }
            
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

var mode = "";
var map;
var markers = [];
var enb_markers = [];
var enbInfoWindows = {};
var menuInfoWindows = {};
var default_lat = 22.5;
var default_lng = 82.975;
var default_zoom = 5;
var activeInfoWindow;
var default_center_lat = 22.5;
var default_center_lng = 83.975;

var shiftPressed = false;
var ctlPressed = false;

var toAddEnbs = [];
var toDeleteEnbs = [];

var default_service_area = "<div class=\"ibox-title\"><h5>Service Area  </h5></div>";
default_service_area += "<div class=\"ibox-content\">";
default_service_area += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
/*default_service_area += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">SA_NAME</th><th class=\"footable-sortable\">COMMAND</th></tr></thead>";
default_service_area += "<tbody>";
default_service_area += "<tr>";
default_service_area += "<td></td>";
default_service_area += "<td></td>";
default_service_area += "<td></td>";
default_service_area += "</tr>";
default_service_area += "</tbody>";
default_service_area += "<tfoot>";
default_service_area += "<tr>";
default_service_area += "<td colspan=\"3\">";
default_service_area += "</td>";
default_service_area += "</tr>";
default_service_area += "</tfoot>";*/
default_service_area += "</table>";
default_service_area += "</div>";

var default_enb_table = "<table class=\"footable table table-stripped\" data-page-size=\"10\">";
default_enb_table += "<thead>";
default_enb_table += "<tr style=\"border-top-style:solid;border-top-width:1px;border-top-color:#c0c0c0;\">";
default_enb_table += "<th class=\"col-sm-1\">eNB ID</th>";
default_enb_table += "<th class=\"col-sm-3\" style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">eNB Name</th>";
default_enb_table += "<th class=\"col-sm-1\">eNB ID</th>";
default_enb_table += "<th class=\"col-sm-3\" style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">eNB Name</th>";
default_enb_table += "<th class=\"col-sm-1\">eNB ID</th>";
default_enb_table += "<th class=\"col-sm-3\">eNB Name</th>";
default_enb_table += "</tr>";
default_enb_table += "</thead>";
default_enb_table += "<tbody>";
default_enb_table += "<tr>";
default_enb_table += "<td></td>";
default_enb_table += "<td></td>";
default_enb_table += "<td></td>";
default_enb_table += "<td></td>";
default_enb_table += "<td></td>";
default_enb_table += "<td></td>";
default_enb_table += "</tr>";
default_enb_table += "</tbody>";
default_enb_table += "<tfoot>";
default_enb_table += "<tr>";
default_enb_table += "<td colspan=\"6\">";
default_enb_table += "</td>";
default_enb_table += "</tr>";
default_enb_table += "</tfoot>";
default_enb_table += "</table>";

var selectedENBsCount = 0;
var circleMap = new Array();
var thisCityMap = new Array();
var hotSpotMap = new Array();

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});

	$(window).keydown(function (e) {
	    if(e.which === 16) { // shift
	        shiftPressed = true;
	    }
	    
	    if(e.which === 17) {
			ctlPressed = true;
		}
	    
	}).keyup(function (e) {
	    if(e.which === 16) { // shift
	        shiftPressed = false;
	    }
	    
	    if(e.which === 17) { // ctl
			ctlPressed = false;
		}
	    
	});

	var mouseDownPos, gribBoundingBox = null,
	    mouseIsDown = 0;
	var themap = map;
	
	google.maps.event.addListener(themap, 'mousemove', function (e) {
	    if (mouseIsDown && (shiftPressed|| gribBoundingBox != null) ) {
	        if (gribBoundingBox !== null) // box exists
	        {         
	            var newbounds = new google.maps.LatLngBounds(mouseDownPos,null);
	            newbounds.extend(e.latLng);    
	            gribBoundingBox.setBounds(newbounds); // If this statement is enabled, I lose mouseUp events

	        } else // create bounding box
	        {
	            gribBoundingBox = new google.maps.Rectangle({
	                map: themap,
	                bounds: null,
	                fillOpacity: 0.15,
	                strokeWeight: 0.9,
	                clickable: false
	            });
	        }
	    }
	});

	google.maps.event.addListener(themap, 'mousedown', function (e) {
	    if (shiftPressed) {
	        mouseIsDown = 1;
	        mouseDownPos = e.latLng;
	        themap.setOptions({
	            draggable: false
	        });
	    }
	});
	
	drawServiceAreaByBmSc();
    $('#toAddENBsBmscId').val($('#bmsc option:selected').val());
}

function makeCircleMap() {
	for (var i = 0; i < circleJson.length; i++) {
		circleMap[circleJson[i].circle_id] = {
			center: {lat: parseFloat(circleJson[i].latitude), lng: parseFloat(circleJson[i].longitude)},		//위도, 경도
		    population: 1500000,																				//원 크기
		    title: circleJson[i].circle_name
		}
	}
}

var checkSAID = false;
function existSAID(type) {
	var said = "";
	if($("#circleId").val() == "" || $("#cityId").val() == "" || $("#hotSpotId").val() == "") {
		swal({
            title: "Warning !",
            text: "Please Input SAID"
        });
		return;
	}
	if(type == "circle") {
		said = $("#circleId").val();
	} else if (type == "city") {
		said = $("#cityId").val();
	} else if (type == "hotspot") {
		said = $("#hotSpotId").val();
	}
	$.ajax({
        url : "/dashbd/api/checkSaId.do",
        type: "post",
        data : { "checkSaId" : said},
        success : function(data) {
        	if(data == "EXIST"){
        		swal({
                    title: "Warning !",
                    text: "사용할 수 없는 이름입니다."
                });
        	} else {
        		swal({
                    title: "Success !",
                    text: "사용 가능합니다."
                });
        		checkSAID = true;
        	}
        },
        error : function(xhr, status, error){
        	swal({
                title: "Fail !",
                text: "서버 통신 오류 입니다."
            });
        }
    });
}

var checkCircle = false;
function existCircle() {
	if($("#circleName").val() == "") {
		swal({
            title: "Warning !",
            text: "Please Input Circle Name"
        });
		return;
	}
	$.ajax({
        url : "/dashbd/api/checkCircleName.do",
        type: "post",
        data : { "circleName" : $("#circleName").val()},
        success : function(data){
        	if(data == "EXIST"){
        		swal({
                    title: "Warning !",
                    text: "사용할 수 없는 이름입니다."
                });
        	} else {
        		swal({
                    title: "Success !",
                    text: "사용 가능합니다."
                });
        		checkCircle = true;
        	}
        },
        error : function(xhr, status, error){
        	swal({
                title: "Fail !",
                text: "서버 통신 오류 입니다."
            });
        }
    });
}

google.maps.event.addDomListener(window, 'load', initMap);

function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}

var cityCircles = [];
var townCircles = [];
var hotSpotMarkers = [];

function drawServiceAreaByBmSc() {
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	clearDatas();
	clearCircle();
//	map.setZoom( 8 );
	
//	$("#service_area").empty();
//	$("#service_area").append(default_service_area);
	$("#enb_table").empty();
	$("#enb_table").append(default_enb_table);
	$("#selectedSvcArea").empty();
    $("#selectedENBs").empty();
	
//    map.setZoom(7);
    // 원그리기
    for (var circle in circleMap) {
	    // Add the circle for this city to the map.
	    var cityCircle = new google.maps.Circle({
			strokeColor: '#FF0000',
			strokeOpacity: 0.8,
			strokeWeight: 2,
			fillColor: '#FF0000',
			fillOpacity: 0.35,
			map: map,
			center: circleMap[circle].center,
			radius: Math.sqrt(circleMap[circle].population) * 100,
			id: circle,
			title: circleMap[circle].title,
			position: circleMap[circle].center
	    });
	    
	    cityCircle.addListener('rightclick', function(e) {
			var contentString = "<b>"+this.title+"</b><br><button class='btn btn-success btn-xs' onclick='editCircle("+this.id+")'>Edit</button>" +
					"<button class='btn btn-success btn-xs' onclick=deleteCircle('"+encodeURI(this.id)+"')>Detele</button>";
			var infowindow = new google.maps.InfoWindow({
			    content: contentString
			});
    		infowindow.open(map, this);
	    	
//        	getServiceGroupList();
//        	getServiceAreaByBmScCity(1, bmscId, this.title, "");
		});
	    
	    cityCircle.addListener('click', function() {
	    	moveCityList(this.id);
    	});
	    
	    cityCircles.push(cityCircle);
	}

    map.setCenter(new google.maps.LatLng(default_lat, default_lng));
//    map.setZoom(default_zoom);
    
    
}

function cityRightClickEmpty(e) {
	var contentString = "Do you want to add this place as hotspot of "+$("#cityName").val()+" city?<br>" +
			"<div style='text-align: center;'>" +
			"<button class='btn btn-success btn-xs' onclick=addHotSpotInCityFromBlank("+e.latLng.lat()+",'"+e.latLng.lng()+"')>Continue</button></div>";
	var infowindow = new google.maps.InfoWindow({
		content: contentString,
		position: {lat: e.latLng.lat(), lng: e.latLng.lng()}
	});
	infowindow.open(map, this);
}

function getCircle(e) {
	if(this.zoom == default_zoom) {
		clearCity();
		initMap();
		return false;
	}else{
		
	}
}

function moveCityList(circleId) {
	
	clearCircle();
	map.setZoom(8);
	map.setCenter(circleMap[circleId].center);
	
	google.maps.event.clearListeners(map, 'mousedown', addCircle);	//클릭 시 Add circle 이벤트 제거
	google.maps.event.addListener(map, 'rightclick', cityRightClickEmpty);
	google.maps.event.addListener(map, 'zoom_changed', getCircle);
	
	$.ajax({
        url : "/dashbd/api/getCityFromCircleName.do",
        type: "post",
        data : { "circleName" : circleMap[circleId].title },
        dateType : 'json',
        success : function(data) {
        	var thisCity = JSON.parse(data).thisCity;
        	
        	var html = "";
        	//클릭한 circle에 해당된 town들
        	for (var i = 0; i < thisCity.length; i++) {
            	html += "<tr><td onclick='moveTownView("+thisCity[i].city_id+")'>"+thisCity[i].city_name+"</td></tr>";
            	thisCityMap[thisCity[i].city_id] = {
        			center: {lat: parseFloat(thisCity[i].latitude), lng: parseFloat(thisCity[i].longitude)},	//위도, 경도
        			title: thisCity[i].city_name,
        			bandwidth: thisCity[i].bandwidth,
        			description: thisCity[i].description,
        		    population: 10000																			//원 크기
        		}
			}
        	
        	drawCity(thisCityMap, "#FF0000", circleId, circleMap[circleId].title);	//빨강
        	
        	$("#cityList").html(html);
        	$("#circleId").val(circleId);
        	$("#circleName").val(circleMap[circleId].title);
        	$("#circleTitle").html(circleMap[circleId].title);
        	getCityList(circleId);
        },
        error : function(xhr, status, error){
        	swal({
                title: "Error !",
                text: "오류가 발생했습니다."
            });
        }
    });
}

//빈 공간 핫스팟 추가
function addHotSpotInCityFromBlank(lat, lng) {
	$("#addHotSpotBtn").show();
	$("#editHotSpotBtn").hide();
	$("div .modal-body input").val("");
	$("div .modal-body textarea").val("");
	$("#longitude").val(lng);
	$("#latitude").val(lat);
	$("#addHotSpotModal").modal();
}

function drawCity(cityMap, color, circleId, circleTitle) {
	for (var city in cityMap) {
	    var townCircle = new google.maps.Circle({
			strokeColor: color,
			strokeOpacity: 0.8,
			strokeWeight: 2,
			fillColor: color,
			fillOpacity: 0.35,
			map: map,
			center: cityMap[city].center,
			radius: Math.sqrt(cityMap[city].population) * 100,
			id: city,
			title: cityMap[city].title,
			bandwidth: cityMap[city].bandwidth,
			description: cityMap[city].description,
			position: cityMap[city].center
	    });
	    
	    townCircle.addListener('click', function(e) {
	    	hotSpotReload(this.id, this.center.lat(), this.center.lng());
	    });
	    
	    townCircles.push(townCircle);
	}
}

function deleteHotSpot(hotspotId) {
	swal({
	  title: "Are you sure?",
	  text: "Hot Spot will delete!",
	  type: "warning",
	  showCancelButton: true,
	  confirmButtonColor: "#DD6B55",
	  confirmButtonText: "Yes, delete it!",
	  closeOnConfirm: false
	},
	function(){
		$.ajax({
	        url : "/dashbd/hotspot/deleteHotSpot.do",
	        type: "post",
	        data : { "hotspotId" : hotspotId },
	        success : function(data){
	        	swal({
	                title: "Success !",
	                text: "삭제 되었습니다."
	            });
	        	hotSpotReload($("#cityId").val(), $("#latitude").val(), $("#longitude").val());
	        },
	        error : function(xhr, status, error){
	        	swal({
	                title: "Error !",
	                text: "오류가 발생했습니다."
	            });
	        }
	    });
	});
}

function moveTownView(id) {
	map.setZoom(10);
}

function clearCity() {
	thisCityMap = new Array();
	for (var i = 0; i < townCircles.length; i++) {
		townCircles[i].labelVisible = false;
		townCircles[i].setMap(null);
	}
	
	townCircles = [];
}

function clearHotSpot() {
	hotSpotMap = new Array();
	for (var i = 0; i < hotSpotMarkers.length; i++) {
		hotSpotMarkers[i].labelVisible = false;
		hotSpotMarkers[i].setMap(null);
	}
	
	hotSpotMarkers = [];
}

function clearCircle() {
	
	for (var i = 0; i < cityCircles.length; i++) {
		cityCircles[i].labelVisible = false;
		cityCircles[i].setMap(null);
	}
	
	cityCircles = [];
}

function clearMarkers() {

	for (var i = 0; i < markers.length; i++) {
		markers[i].labelVisible = false;
	    markers[i].setMap(null);
	}
	
	markers = [];
}

function clearEnbMarkers() {
	for (var i = 0; i < enb_markers.length; i++) {
	    enb_markers[i].setMap(null);
	}
	
	enb_markers = [];
}

function clearDatas() {
	toAddEnbs = [];
	toDeleteEnbs = [];
}

function moveToSelectedEnb(lat, lng) {

	map.setCenter(new google.maps.LatLng(lat, lng));
}

function isEmpty(value) {
    return (value === undefined || value == null || value.length <= 0) ? true : false;
}

function getHotSpotList(cityId) {
	$('#table').bootstrapTable('destroy');
	var pageNumber = 1;
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/hotspot/getHotSpotList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
			params['cityId'] = cityId;
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'hotspot_name',
			title: 'Hot Spot Name',
			width: '30%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<a onclick="focusCity(\''+index+'\', \''+row.latitude+'\', \''+row.longitude+'\', '+row.hospot_id+')">'+value+'</a>';
				return html;
			}
		}, {
			field: 'hotspot_id',
			title: 'SAID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true
		}, {
			field: 'longitude',
			title: 'Longitude',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input style="text-align: center;" value="'+value+'">';
				return html;
			}
		}, {
			field: 'latitude',
			title: 'Latitude',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: true,
			formatter: function(value, row, index) {
				var html = '<input style="text-align: center;" value="'+value+'">';
				return html;
			}
		}, {
			field: 'command',
			title: 'Command',
			width: '20%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var html = '<button type="button" onclick="editHotSpotDirect(\'' + index + '\',\'' + cityId + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
				+ '<button type="button" onclick="deleteHotSpot(\'' + row.hotspot_id + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}

function editHotSpot(hotSpotId, hotSpotName, lat, lng, bandwidth, description) {
	var latLng = hotSpotMap[hotSpotId].center;
	$("#editHotSpotBtn").show();
	$("#addHotSpotBtn").hide();
	$("#hotSpotId").val(hotSpotId);
	$("#hotSpotName").val(hotSpotName);
	$("#longitude").val(lng);
	$("#latitude").val(lat);
	$("#bandwidth").val(bandwidth);
	$("#description").val(description);
	$("#addHotSpotModal").modal();
}

function editHotSpotDirect(index, cityId) {
	var selector = $($("tr[data-index]")[index]);
	var hotSpotName = $(selector.children()[0]).children().html();
	var said = $(selector.children()[1]).html();
	var longitude = $(selector.children()[2]).children().val();
	var latitude = $(selector.children()[3]).children().val();
	$.ajax({
        url : "/dashbd/hotspot/insertHotSpot.do",
        type: "post",
        data : { "hotSpotId" : said, "hotSpotName" : hotSpotName, "longitude" : longitude, "latitude" : latitude },
        success : function(data){
        	swal({
                title: "Success !",
                text: "수정 되었습니다."
            });
        	hotSpotReload(cityId, latitude, longitude);
        },
        error : function(xhr, status, error){
        	swal({
                title: "Error !",
                text: "오류가 발생했습니다."
            });
        }
    });
}

function hotSpotReload(cityId, latitude, longitude) {
	clearCity();
	clearHotSpot();
	$("#addTable").show();
	$.ajax({
		url : "/dashbd/hotspot/getHotSpotListFromCityId.do",
		type: "post",
		data : { "cityId" : cityId },
		success : function(data) {
			var json = JSON.parse(data).result;
			
			for (var i = 0; i < json.length; i++) {
				hotSpotMap[json[i].hotspot_id] = {
						center: {lat: parseFloat(json[i].latitude), lng: parseFloat(json[i].longitude)},		
						title: json[i].hotspot_name,
						bandwidth: json[i].bandwidth,
						description: json[i].description,
				}
			}
			
			drawHotSpot(hotSpotMap, "#FF0000", cityId);	//빨강
			
			map.setZoom(16);
			map.setCenter(new google.maps.LatLng(latitude, longitude));
			
			getHotSpotList(cityId);
			
		},
		error : function(xhr, status, error){
			swal({
				title: "Fail !",
				text: "서버 통신 오류 입니다."
			});
		}
	});
}

function focusCity(index, lat, lng, cityId) {
	$($("tr[data-index]")).css("background-color", "#FFFFFF");
	$($("tr[data-index]")[index]).css("background-color", "#D3D3D3");
	map.setCenter(new google.maps.LatLng(lat, lng));
}



