var initBmScId = 1;
var modalMap;
var globalCircleId;
var globalCircleName;
var globalCircleLatitude;
var globalCircleLongitude;

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

    
    $('#addCircleBtn').click(function(){
    	if(!checkCircle){
    		swal({
                title: "Warning !",
                text: "Please Check Circle Name!"
            });
    		return;
    	}
    	if(!checkSAID){
    		swal({
                title: "Warning !",
                text: "Please Check SAID!"
            });
    		return;
    	}
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
                        text: "Circle is created!"
                    });
            		setTimeout(() => {
            			location.reload();
					}, 1000);
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
    
    $("#searchType").on("change", function(){
    	$.ajax({
            url : "/dashbd/api/circle/getCityListSearch.do",
            type: "post",
            data : { "searchType" : $("#searchType").val(), "searchKeyword" : "" },
            datatype : "json", 
            success : function(data){
            	var json = data.rows;
            	var html = "";
            	for (var i = 0; i < json.length; i++) {
            		html += "<option value='"+json[i].id+"'>"+json[i].name+"</option>";
				}
            	$("#searchKeyword").html(html);
            	$("#searchKeyword").focus();
            }
        });
    });
    
});

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
var noneCityMap = new Array();
var otherCityMap = new Array();

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});

	// mouse drag start
	// Start drag rectangle to select markers !!!!!!!!!!!!!!!!
//	google.maps.event.clearListeners(map, 'zoom_changed', getCircle);	//zoom change 이벤트 제거
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
	    
	    if(e.which === 27) { // esc
	    	for (var i = 0; i < enb_markers.length; i++) {
				if( enb_markers[i].isSameServieArea && enb_markers[i].isSameServieArea !== "N" ) {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_red.png");
				} else if( !enb_markers[i].isSameServieArea ) {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_blue.png");
				} else {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_gray.png");
				}  
				
				enb_markers[i].selected = false;
            }

	    	toAddEnbs = [];
	    	toDeleteEnbs = [];
	    	
	    	$("#selectedENBs").empty();
	    	$("#selectedENBs").append( "Selected eNBs : " + toAddEnbs.length );
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
	
	google.maps.event.addListener(themap, 'mousedown', addCircle);
	
	google.maps.event.addListener(themap, 'mouseup', function (e) {
	    if (mouseIsDown && (shiftPressed|| gribBoundingBox != null)) {
	        mouseIsDown = 0;
	        if (gribBoundingBox !== null) // box exists
	        {
	            var boundsSelectionArea = new google.maps.LatLngBounds(gribBoundingBox.getBounds().getSouthWest(), gribBoundingBox.getBounds().getNorthEast());

	            for (var i = 0; i < enb_markers.length; i++) { // looping through my Markers Collection	
	                if (gribBoundingBox.getBounds().contains(enb_markers[i].getPosition())) 
	                {
	                	if( enb_markers[i].isSameServieArea && enb_markers[i].isSameServieArea !== "N" ) {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_red_on.png");
	                		if( !enb_markers[i].selected ) {
	                			toDeleteEnbs.push(enb_markers[i].infoWindowIndex);
	                		}
	                	} else if( !enb_markers[i].isSameServieArea ) {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
	                		
	                		if( !enb_markers[i].selected ) {
		                		toAddEnbs.push(enb_markers[i].infoWindowIndex);
	                		}
	                	} else {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
	                		if( !enb_markers[i].selected ) {
		                		toAddEnbs.push(enb_markers[i].infoWindowIndex);
	                		}
	                	}  
	                	
	                	enb_markers[i].selected = true;
	                	
	                	
	                } else {
	                    //markers[i].marker.setIcon("/dashbd/resources/img/icon/enb_gray.png")
	                }
	            }

	            gribBoundingBox.setMap(null); // remove the rectangle
	            
        		$("#selectedENBs").empty();
				//$("#selectedENBs").append( "Selected eNBs : " + (selectedENBsCount + toAddEnbs.length) );
        		$("#selectedENBs").append( "Selected eNBs : " + toAddEnbs.length );
	        }
	        gribBoundingBox = null;
	    }

	    themap.setOptions({
	        draggable: true
	    });
	});
	// mouse drag end
	
	//alert($('#bmsc option:selected').val());
	
	// 처음 로딩 시 지도에 표시해주는 부분
//	makeCircleMap();
	drawServiceAreaByBmSc();
    $('#toAddENBsBmscId').val($('#bmsc option:selected').val());
}

//function makeCircleMap() {
//	for (var i = 0; i < circleJson.length; i++) {
//		circleMap[circleJson[i].circle_id] = {
//			center: {lat: parseFloat(circleJson[i].latitude), lng: parseFloat(circleJson[i].longitude)},		//위도, 경도
//		    population: 1500000,																				//원 크기
//		    title: circleJson[i].circle_name
//		}
//	}
//}

var checkSAID = false;
function existSAID(type) {
	var said = "";
	if($("#circleId").val() == "" || $("#cityId").val() == "") {
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
	}
	$.ajax({
        url : "/dashbd/api/checkSaId.do",
        type: "post",
        data : { "checkSaId" : said},
        success : function(data) {
        	if(data == "EXIST"){
        		swal({
                    title: "Warning !",
                    text: "사용할 수 없는 ID입니다."
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

function createServiceArea( operatorId, bmscId ) {
	$.ajax({
	    url : "/dashbd/api/createServiceArea.do",
	    type: "GET",
	    data : { "bmscId": bmscId, "serviceAreaId" : $('#serviceAreaId').val(), "serviceAreaName" : encodeURIComponent($('#serviceAreaName').val()), "serviceAreaDescription" : encodeURIComponent($('#serviceAreaDescription').val()) },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);

	        if( data.count == 1 ) {
	        	swal({
	                title: "Success !",
	                text: "Please register eNB infomation."
	            });
	        	
	        	getSeviceAreaNotMapped( bmscId );
	        	
	        	map.setZoom( 12 );
	            moveToEnbNotMappedSA(bmscId, $('#serviceAreaId').val(), default_center_lat, default_center_lng);
	            
	    	} else if( data.count == 0 ) {
	    		if(data.selCount == 0){
		    		swal({
		                title: "Info !",
		                text: "Already have a Service Area ID."
		            });
	    		}else{
		    		swal({
		                title: "Success !",
		                text: "Please register eNB infomation."
		            });
	    		}
	        } else if( data.count < 0 ) {
	        	swal({
	                title: "Fail !",
	                text: "Service Area Registration failed."
	            });
	        }
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Service Area Registration failed."
            });
        }
	});
}

function editServiceArea( operatorId, bmscId ) {
	$.ajax({
	    url : "/dashbd/api/editServiceArea.do",
	    type: "GET",
	    data : { "bmscId": bmscId, "serviceAreaId" : $('#editServiceAreaId').val(), "serviceAreaName" : encodeURIComponent($('#editServiceAreaName').val()), "serviceAreaDescription" : encodeURIComponent($('#editServiceAreaDescription').val()) },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);

	        if( data.count == 1 ) {
	        	swal({
	                title: "Success !",
	                text: "Service Area 정보가 수정되었습니다."
	            });
	        	
	        	if($("#editType").val() == "Y"){
	        		getServiceAreaByBmScCity("1", bmscId, $("#checkCityName").val(), "");
	        	}else{
	        		getSeviceAreaNotMapped( bmscId );
	        	}
	        } else{
	        	swal({
	                title: "Fail !",
	                text: "Service Area 수정을 실패하였습니다."
	            });
	        }
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Service Area 수정을 실패하였습니다."
            });
        }
	});
}
function getSeviceAreaNotMapped(bmscId) {
	
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	clearDatas();
	
	$("#service_area").empty();
	$("#service_area").append(default_service_area);
	$("#enb_table").empty();
	$("#enb_table").append(default_enb_table);
	$("#selectedSvcArea").empty();
    $("#selectedENBs").empty();
	
    map.setZoom(12);
    map.setCenter( new google.maps.LatLng( default_center_lat, default_center_lng ) );
    
	$.ajax({
        url : "/dashbd/api/getSeviceAreaNotMapped.do",
        type: "get",
        data : { "bmscId" : bmscId },
        dateType : 'json',
        success : function( responseData ) {
        	$("#ajax").remove();
            datas = JSON.parse( responseData );
            var dataLen = datas.length;
            var options = "";
            var idx = 0;
            
            options += "<div class=\"ibox-title\"><h5>Not Mapped Service Area</h5></div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">SA_NAME<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">COMMAND</th></tr></thead>";
			options += "<tbody>";
			
            for( var i = 0; i < dataLen; i++ ) {
             	options += "<tr id=\"" + datas[i].serviceAreaId + "\" class=\"footable-even\" style=\"display: table-row;cursor:pointer;\" onclick=\"javascript:map.setZoom( 12 );clearMarkers();clearEnbMarkers();clearDatas();moveToEnbNotMappedSA(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ", " + default_center_lat + ", " + default_center_lng + ");\"><td>";
             	var html = '<button type="button" onclick="doEditService(\'N\',\'' + datas[i].serviceAreaId + '\', \'' + datas[i].serviceAreaName + '\', \'' + datas[i].description + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
				+ '<button type="button" onclick="doDeleteService(\'N\',\'' + datas[i].serviceAreaId + '\', \'0\', \'' + bmscId + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"#\">";
            	options += datas[i].serviceAreaId;
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td><td>";
				options += html;
				options += "</td></tr>";
            }

            options += "</tbody>";
            if(dataLen > 10) {
            	//alert(dataLen);
            	options += "<tfoot><tr><td colspan=\"2\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
            
            $("#service_area").empty();
            $("#service_area").append( options );
            
            $('.footable').footable();
        	$("#service_area").find('tr').removeClass("footable-odd");
        },
        error : function( xhr, status, error ) {
        }
    });
}

function moveToEnb(bmscId, serviceAreaId, mapCity)
{
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	clearDatas();
	$("#viewEnbIDAdd").show();
	$("#viewEnbIDList").show();
	$("#enb_table").empty();
	$("#enb_table").append(default_enb_table);
	
	$("#"+serviceAreaId).siblings().removeClass("tr-highlighted");
	$("#"+serviceAreaId).toggleClass("tr-highlighted");
	$('#toAddENBsServiceAreaId').val(serviceAreaId);

	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbAp.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId },
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(responseData){
			$("#ajax").remove();
			var maplatitude = "";
			var maplongitude = "";
			var enb_datas = JSON.parse(responseData);
			var dataLen = enb_datas.length;
			var options = "<table class=\"footable table table-stripped\" data-page-size=\"10\">";
			options += "<thead>";
			options += "<tr style=\"border-top-style:solid;border-top-width:1px;border-top-color:#c0c0c0;\">";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\" style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">eNB Name</th>";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\" style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">eNB Name</th>";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\">eNB Name</th>";
			options += "</tr>";
			options += "</thead>";
			options += "<tbody>";
			for(var i = 0; i < enb_datas.length; i++) {
				if( i == 0 ) {
					options += "<tr style=\"border-bottom-style:solid;border-bottom-width:1px;border-bottom-color:#c0c0c0;\">";
					options += "<td>";
					options += "<a href=\"javascript:moveToSelectedEnb(" + enb_datas[i].latitude + ", " + enb_datas[i].longitude + ");\">";
					options += enb_datas[i].enbApId;
					options += "</a>";
					options += "</td>";
					options += "<td style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">";
					options += enb_datas[i].enbApName;
					options += "</td>";
				}
				else if( i != 0 && i % 3 != 2 ) {
					options += "<td>";
					options += "<a href=\"javascript:moveToSelectedEnb(" + enb_datas[i].latitude + ", " + enb_datas[i].longitude + ");\">";
					options += enb_datas[i].enbApId;
					options += "</a>";
					options += "</td>";
					options += "<td style=\"border-right-style:solid;border-right-width:1px;border-right-color:#c0c0c0;\">";
					options += enb_datas[i].enbApName;
					options += "</td>";
				}
				else if( i != 0 && i % 3 == 2 ) {
					options += "<td>";
					options += "<a href=\"javascript:moveToSelectedEnb(" + enb_datas[i].latitude + ", " + enb_datas[i].longitude + ");\">";
					options += enb_datas[i].enbApId;
					options += "</a>";
					options += "</td>";
					options += "<td>";
					options += enb_datas[i].enbApName;
					options += "</td>";
					options += "</tr><tr style=\"border-bottom-style:solid;border-bottom-width:1px;border-bottom-color:#c0c0c0;\">";
				}

				if(mapCity == enb_datas[i].mapCity){
					maplatitude = enb_datas[i].latitude;
					maplongitude = enb_datas[i].longitude;
				}else{
					maplatitude = enb_datas[0].latitude;
					maplongitude = enb_datas[0].longitude;
				}
			}
			if( dataLen % 3 == 0 ) {
				options += "</tr>";
			} else if( dataLen % 3 == 1 ) {
				options += "<td></td><td></td><td></td><td></td></tr>";
			} else if( dataLen % 3 == 2 ) {
				options += "<td></td><td></td></tr>";
			}
			options += "</tbody>";
			if( dataLen > 10 ) {
				options += "<tfoot><tr>";
				options += "<td colspan=\"6\">";
				options += "<ul class=\"pagination pull-right\"></ul>";
				options += "</td>";
				options += "</tr></tfoot>";
			}
			options += "</table>";

            
            $('.footable').footable();
			$("#enb_table").empty();
            $("#enb_table").append(options);
            
            $("#selectedSvcArea").empty();
            $("#selectedSvcArea").append("Service Area : " + serviceAreaId);
            
            $("#selectedENBs").empty();
            selectedENBsCount = enb_datas.length;
            $("#selectedENBs").append( "Selected eNBs : " + 0 );
            map.setZoom( 12 );
            moveToEnbWithBounds(bmscId, serviceAreaId, maplatitude, maplongitude);
		}
	});
}

function moveToEnbWithBounds( bmscId, serviceAreaId, lat, lng )
{
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	
	map.setCenter( new google.maps.LatLng( lat, lng ) );
	
	var bounds = map.getBounds();

    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();
    
	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbApWithBounds.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId, "swLat" : sw.lat(), "swLng" : sw.lng(), "neLat" : ne.lat(), "neLng" : ne.lng() },
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function( responseData ) {
			$("#ajax").remove();
			var enb_datas = JSON.parse( responseData );
			var dataLen = enb_datas.length;
			var options = "";
			
			for( var i = 0; i < enb_datas.length; i++ ) {
				var latLng = new google.maps.LatLng( enb_datas[i].latitude, enb_datas[i].longitude );
				var marker;
				var thisIcon = "";
				var thisSelected = false;
				var isContains = -1;
				
				if( enb_datas[i].serviceAreaId == serviceAreaId ) {
					isContains = toDeleteEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_red_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_red.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : true,
						zIndex: google.maps.Marker.MAX_ZINDEX + 1
					});
				} else if( enb_datas[i].serviceAreaId == '' || enb_datas[i].serviceAreaId == null ) {
					isContains = toAddEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_gray_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_gray.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : 'N'
					});
				} else {
					isContains = toAddEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_blue_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_blue.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : false
					});
				}
				
				var enbContent = '<ul>' 
				+ '<li>eNB ID : ' + enb_datas[i].enbApId + '</li>'
				+ '<li>eNB Name : '	+ enb_datas[i].enbApName + '</li>'
				+ '<li>PLMN : ' + enb_datas[i].plmn + '</li>'
				+ '<li>MBSFN : ' + enb_datas[i].mbsfn + '</li>'
				+ '</ul>';
				
				var menuContent = '<ul>' +
				'<li><a href="javascript:addToServiceArea(' + bmscId + ', ' + serviceAreaId + ', ' + false + ');" class="add_to_service_area">Add</a></li>' +
				'<li><a href="javascript:deleteFromServiceArea(' + bmscId + ', ' +  serviceAreaId + ');" class="delete_from_service_area">Delete</a></li>' +
				'</ul>';
				
				var enbInfoWindow = new google.maps.InfoWindow({
					content: enbContent
				});
				
				var menuInfoWindow = new google.maps.InfoWindow({
					content: menuContent
				});
				
				enbInfoWindows[enb_datas[i].enbApId] = enbInfoWindow;
				menuInfoWindows[enb_datas[i].enbApId] = menuInfoWindow;

				// add listener on InfoWindow for mouseover event
				google.maps.event.addListener(marker, 'mouseover', function() {
					
					// Close active window if exists - [one might expect this to be default behaviour no?]				
					if(activeInfoWindow != null) activeInfoWindow.close();
					
					// Open new InfoWindow for mouseover event
					enbInfoWindows[this.infoWindowIndex].open(map, this);
					
					// Store new open InfoWindow in global variable
					activeInfoWindow = enbInfoWindows[this.infoWindowIndex];
				}); 							
				
				// on mouseout (moved mouse off marker) make infoWindow disappear
				google.maps.event.addListener(marker, 'mouseout', function() {
					enbInfoWindows[this.infoWindowIndex].close();	
				});
	
				// --------------------------------
				// ON MARKER CLICK - (Mouse click)
				// --------------------------------
				
				// add listener on InfoWindow for click event
				google.maps.event.addListener(marker, 'click', function() {
					if(ctlPressed) {
						if(this.selected) {
							this.selected = false;
							 if( this.isSameServieArea && this.isSameServieArea !== "N") {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red.png");

		                		var idx = toDeleteEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toDeleteEnbs.splice(idx, 1);
								}
		                	} else if( !this.isSameServieArea ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue.png");

		                		var idx = toAddEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toAddEnbs.splice(idx, 1);
								}
		                	} else {
								this.setIcon("/dashbd/resources/img/icon/enb_gray.png");

								var idx = toAddEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toAddEnbs.splice(idx, 1);
								}
		                	}
						}
						else {
							this.selected = true;
							if( this.isSameServieArea && this.isSameServieArea !== "N" ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red_on.png");
		                		toDeleteEnbs.push(this.infoWindowIndex);
		                	} else if( !this.isSameServieArea ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
		                		toAddEnbs.push(this.infoWindowIndex);
		                	} else {
								this.setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
								toAddEnbs.push(this.infoWindowIndex);
		                	} 
						}
						
						$("#selectedENBs").empty();
						$("#selectedENBs").append( "Selected eNBs : " + toAddEnbs.length );
					}
					else {
						if(this.selected) {
							//Close active window if exists - [one might expect this to be default behaviour no?]				
							if(activeInfoWindow != null) activeInfoWindow.close();
		
							// Close "mouseover" infoWindow
							enbInfoWindows[this.infoWindowIndex].close();
							
							// Open InfoWindow - on click 
							menuInfoWindows[this.infoWindowIndex].open(map, this);
							
							// Store new open InfoWindow in global variable
							activeInfoWindow = menuInfoWindows[this.infoWindowIndex];
						}
					}
				});
				
				enb_markers.push(marker);
			}
		}
	});
	
	google.maps.event.addListener( map, 'idle', function() {
    	moveToEnbWithBounds( bmscId, serviceAreaId, map.getCenter().lat(), map.getCenter().lng() );
    } );
    

}

function moveToEnbNotMappedSA( bmscId, serviceAreaId, lat, lng )
{
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	//clearDatas();
	
	$('#toAddENBsServiceAreaId').val(serviceAreaId);

	$("#"+serviceAreaId).siblings().removeClass("tr-highlighted");
	$("#"+serviceAreaId).toggleClass("tr-highlighted");
	map.setCenter( new google.maps.LatLng( lat, lng ) );
	
	var bounds = map.getBounds();

    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();
    
	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbApNotMappedSA.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId, "swLat" : sw.lat(), "swLng" : sw.lng(), "neLat" : ne.lat(), "neLng" : ne.lng() },
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function( responseData ) {
			$("#ajax").remove();
			var enb_datas = JSON.parse( responseData );
			var dataLen = enb_datas.length;
			var options = "";
            
			for( var i = 0; i < enb_datas.length; i++ ) {
				var latLng = new google.maps.LatLng( enb_datas[i].latitude, enb_datas[i].longitude );
				var marker;
				var thisIcon = "";
				var thisSelected = false;
				var isContains = -1;
				
				if( enb_datas[i].serviceAreaId == serviceAreaId ) {
					isContains = toDeleteEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_red_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_red.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : true,
						zIndex: google.maps.Marker.MAX_ZINDEX + 1
					});
				} else if( enb_datas[i].serviceAreaId == '' || enb_datas[i].serviceAreaId == null ) {
					isContains = toAddEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_gray_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_gray.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : 'N'
					});
				} else {
					isContains = toAddEnbs.indexOf( enb_datas[i].enbApId );

					if( isContains != -1 ) {
						thisIcon = '/dashbd/resources/img/icon/enb_blue_on.png';
						thisSelected = true;
					} else {
						thisIcon = '/dashbd/resources/img/icon/enb_blue.png';
						thisSelected = false;
					}
					
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : thisIcon,
						infoWindowIndex : enb_datas[i].enbApId,
						selected : thisSelected,
						isSameServieArea : false
					});
				}
				
				var enbContent = '<ul>' 
				+ '<li>SA_ID : ' + enb_datas[i].serviceAreaId + '</li>'
				+ '<li>eNB ID : ' + enb_datas[i].enbApId + '</li>'
				+ '<li>eNB Name : '	+ enb_datas[i].enbApName + '</li>'
				+ '<li>PLMN : ' + enb_datas[i].plmn + '</li>'
				+ '<li>MBSFN : ' + enb_datas[i].mbsfn + '</li>'
				+ '</ul>';
				
				var menuContent = '<ul>' +
				'<li><a href="javascript:addToServiceArea(' + bmscId + ', ' + serviceAreaId + ', ' + true + ');" class="add_to_service_area">Add To Service Area</a></li>' +
				'<li><a href="javascript:deleteFromServiceArea(' + bmscId + ', ' +  serviceAreaId + ');" class="delete_from_service_area">Delete From Service Area</a></li>' +
				'</ul>';
				
				var enbInfoWindow = new google.maps.InfoWindow({
					content: enbContent
				});
				
				var menuInfoWindow = new google.maps.InfoWindow({
					content: menuContent
				});
				
				enbInfoWindows[enb_datas[i].enbApId] = enbInfoWindow;
				menuInfoWindows[enb_datas[i].enbApId] = menuInfoWindow;
				
				// add listener on InfoWindow for mouseover event
				google.maps.event.addListener(marker, 'mouseover', function() {
					
					// Close active window if exists - [one might expect this to be default behaviour no?]				
					if(activeInfoWindow != null) activeInfoWindow.close();
					
					// Open new InfoWindow for mouseover event
					enbInfoWindows[this.infoWindowIndex].open(map, this);
					
					// Store new open InfoWindow in global variable
					activeInfoWindow = enbInfoWindows[this.infoWindowIndex];
				}); 							
				
				// on mouseout (moved mouse off marker) make infoWindow disappear
				google.maps.event.addListener(marker, 'mouseout', function() {
					enbInfoWindows[this.infoWindowIndex].close();	
				});
				
				// --------------------------------
				// ON MARKER CLICK - (Mouse click)
				// --------------------------------
				
				// add listener on InfoWindow for click event
				google.maps.event.addListener(marker, 'click', function() {
					if(ctlPressed) {
						if(this.selected) {
							this.selected = false;
							 if( this.isSameServieArea && this.isSameServieArea !== "N") {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red.png");

		                		var idx = toDeleteEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toDeleteEnbs.splice(idx, 1);
								}
		                	} else if( !this.isSameServieArea ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue.png");

		                		var idx = toAddEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toAddEnbs.splice(idx, 1);
								}
		                	} else {
								this.setIcon("/dashbd/resources/img/icon/enb_gray.png");

								var idx = toAddEnbs.indexOf( this.infoWindowIndex );
								if(idx != -1) {
									toAddEnbs.splice(idx, 1);
								}
		                	}
						}
						else {
							this.selected = true;
							if( this.isSameServieArea && this.isSameServieArea !== "N" ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red_on.png");
		                		toDeleteEnbs.push(this.infoWindowIndex);
		                	} else if( !this.isSameServieArea ) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
		                		toAddEnbs.push(this.infoWindowIndex);
		                	} else {
								this.setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
								toAddEnbs.push(this.infoWindowIndex);
		                	} 
						}
						
						$("#selectedENBs").empty();
						$("#selectedENBs").append( "Selected eNBs : " + toAddEnbs.length );
					}
					else {
						if(this.selected) {
							//Close active window if exists - [one might expect this to be default behaviour no?]				
							if(activeInfoWindow != null) activeInfoWindow.close();
		
							// Close "mouseover" infoWindow
							enbInfoWindows[this.infoWindowIndex].close();
							
							// Open InfoWindow - on click 
							menuInfoWindows[this.infoWindowIndex].open(map, this);
							
							// Store new open InfoWindow in global variable
							activeInfoWindow = menuInfoWindows[this.infoWindowIndex];
						}
					}
				});
				
				enb_markers.push(marker);
			}

			$("#viewEnbIDAdd").show();
			$("#viewEnbIDList").show();
		    
		    $("#selectedSvcArea").empty();
		    $("#selectedSvcArea").append("Service Area : " + serviceAreaId);
		}
	});
	
	google.maps.event.addListener( map, 'idle', function() {
    	moveToEnbNotMappedSA( bmscId, serviceAreaId, map.getCenter().lat(), map.getCenter().lng() );
    } );
    

}

function getParameter(name) {
	var url = location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( url );
	
	return results == null ? null : results[1];
}

function doDeleteService(dType, serviceAreaId, enBCnt, bmscId, city) {
	var conValue = "";
	if(dType == 'Y'){
		conValue = 'Do you really want to delete this Service Area ID "' + serviceAreaId + '" And eNB Info?';
	}else{
		conValue = 'Do you really want to delete this Service Area ID "' + serviceAreaId + '"?';
	}
	if(confirm(conValue)) {
		$.ajax({
			url: '/dashbd/api/serviceAreaByDelete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				serviceAreaId: serviceAreaId,
				dType: dType
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result == "SUCCESS") { // 성공
					alert('Success!!');
					if(dType == 'Y'){
						getServiceAreaByBmScCity("1", bmscId, city, "");
					}else{
						getSeviceAreaNotMapped( bmscId );
					}
				}
				else { // 실패
					alert('Failed!! Please you report to admin!');
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert(errorThrown + textStatus);
				checkUserId = false;
				return false;
			}
		});
	}
}

function doEditService(dType, serviceAreaId, serviceAreaName, description){
	$("#editServiceAreaLayer").modal();
	$("#editType").val(dType);
	$("#editServiceAreaId").val(serviceAreaId);
	$("#editServiceAreaName").val(serviceAreaName);
	$("#editServiceAreaDescription").val(description);
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
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
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
			options += '<button type="button" class="btn btn-primary4" onclick="javascript:searchToServiceArea(\'' + bmscId + '\', \'' + city + '\');" id="toSearchBtn">Search</button>';
			options += "</span>";
			options += "</div>";
			options += "</div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">SA_NAME<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">COMMAND</th></tr></thead>";
			options += "<tbody>";
            for(var i=0; i<dataLen; i++){
            	options += "<tr id=\"" + datas[i].serviceAreaId + "\" class=\"footable-even\" style=\"display: table-row;cursor:pointer;\" onclick=\"javascript:moveToEnb(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ", '" + city + "');\"><td>";
             	var html = '<button type="button" onclick="doEditService(\'Y\',\'' + datas[i].serviceAreaId + '\', \'' + datas[i].serviceAreaName + '\', \'' + datas[i].description + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
				+ '<button type="button" onclick="doDeleteService(\'Y\', \'' + datas[i].serviceAreaId + '\', \'' + datas[i].totalCount + '\', \'' + bmscId + '\', \'' + city + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"#\">";
            	options += datas[i].serviceAreaId;
            	options += " (" + datas[i].totalCount + ")";
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td><td>";
				options += html;
				options += "</td></tr>";
            }

            options += "</tbody>";

            if(dataLen > 10) {
            	options += "<tfoot><tr><td colspan=\"3\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
            
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
        	$("#service_area").find('tr').removeClass("footable-odd");
            // Pagination
            /*
            var totalCount = datas[0].totalCount;
            if(totalCount > perPage) {
            	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
            	
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
            */
        }
    });
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


function addCircle(e) {
	$("#editCircleBtn").hide();
	$("#addCircleBtn").show();
	$("#addCircleModal").modal();
	$("#circleId").val("");
	$("#circleName").val("");
	$("#longitude").val(e.latLng.lng());
	$("#latitude").val(e.latLng.lat());
}

function editCircle(circleId) {
	var latLng = circleMap[circleId].center;
	$("#editCircleBtn").show();
	$("#addCircleBtn").hide();
	$("#addCircleModal").modal();
	$("#circleId").val(circleId);
	$("#circleName").val(circleMap[circleId].title);
	$("#longitude").val(latLng.lng);
	$("#latitude").val(latLng.lat);
}

function deleteCircle(circleId) {
	title = decodeURI(circleId);
	if(confirm("do you want to delete?")) {
		$.ajax({
	        url : "/dashbd/api/deleteCircle.do",
	        type: "post",
	        data : { "circleId" : circleId },
	        success : function(responseData){
	        	swal({
	                title: "Success !",
	                text: "삭제 되었습니다."
	            });
	        	setTimeout(() => {
	        		location.reload();
				}, 2000);
	        },
	        error : function(xhr, status, error){
	        	swal({
	                title: "Error !",
	                text: "오류가 발생했습니다."
	            });
	        }
	    });
	}
}

var infowindow;
function cityRightClickEmpty(e) {
	var contentString = "Do you want to add this place as city of "+circleTitle.innerHTML+" Circle?<br>" +
			"<div style='text-align: center;'>" +
			"<button id='continue' class='btn btn-success btn-xs' onclick=addCityInCircleFromBlank("+e.latLng.lat()+",'"+e.latLng.lng()+"')>Continue</button></div>";
	infowindow = new google.maps.InfoWindow({
		content: contentString,
		position: {lat: e.latLng.lat(), lng: e.latLng.lng()}
	});
	infowindow.open(map, this);
	
	$("#continue").on("click", function(){
		infowindow.close();
	});
}

function getCircle(e) {
	if(this.zoom == default_zoom) {
		initMap();
		$(".circle-map").show();
		$("#map").hide();
		return false;
	}else{
		
	}
}

function getCity(e) {
	if(this.zoom == 8) {
		initMap();
		$(".circle-map").show();
		$("#map").hide();
		return false;
	}else{
		
	}
}

function moveCityList(circleId, circleName, latitude, longitude) {
	
	globalCircleId = circleId;
	globalCircleName = circleName;
	globalCircleLatitude = latitude;
	globalCircleLongitude = longitude;
	
	clearCity();
	$(".circle-map").hide();
	$("#map").show();
	
	google.maps.event.trigger(map, "resize");
	
	map.setZoom(8);
	
	map.setCenter({lat: Number(latitude), lng: Number(longitude)});
	
	google.maps.event.clearListeners(map, 'mousedown', addCircle);	//클릭 시 Add circle 이벤트 제거
	google.maps.event.addListener(map, 'zoom_changed', getCircle);
	
	$.ajax({
        url : "/dashbd/api/getCityFromCircleName.do",
        type: "post",
        data : { "circleName" : circleName },
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
        	
        	drawCity(thisCityMap, "#FF0000", circleId, circleName);	//빨강
        	
        	$("#cityList").html(html);
        	$("#circleId").val(circleId);
        	$("#circleName").val(circleName);
        	$("#circleTitle").html(circleName);
        	
        },
        error : function(xhr, status, error){
        	swal({
                title: "Error !",
                text: "오류가 발생했습니다."
            });
        }
    });
}

//빈 공간 시티 추가
function addCityInCircleFromBlank(lat, lng) {
	$("#addCityBtn").show();
	$("#editCityBtn").hide();
	$("#cityId").val("");
	$("#cityName").val("");
	$("#cityLongitude").val(lng);
	$("#cityLatitude").val(lat);
	$("#addCityModal").modal();
}

//아무대도 없는 시티 해당 서클로 추가
function addCityInCircle(circleName, lat, lng) {
	$("#addCityBtn").show();
	$("#editCityBtn").hide();
	$("#addCityModal").modal();
}

// 시티 수정
function editCity(cityId, cityName, lat, lng, bandwidth, description) {
	$("#addCityBtn").hide();
	$("#editCityBtn").show();
	$("#cityId").val(cityId);
	$("#cityName").val(cityName);
	$("#cityLongitude").val(lng);
	$("#cityLatitude").val(lat);
	$("#cityBandwidth").val(bandwidth);
	$("#cityDescription").val(description);
	$("#addCityModal").modal();
}

//시티 이동 City A --> City B
function changeCityInCircle(cityId, circleId, circleName) {
	$.ajax({
        url : "/dashbd/api/moveCityOtherCircle.do",
        type: "post",
        data : { "cityId" : cityId, "circleId" : circleId, "circleName" : circleName, },
        success : function(data){
        	swal({
                title: "Success !",
                text: "이동 되었습니다."
            });
        	setTimeout(() => {
				location.reload();
			}, 1000);
        },
        error : function(xhr, status, error){
        	swal({
                title: "Error !",
                text: "오류가 발생했습니다."
            });
        }
    });
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
	    
	    townCircle.addListener('rightclick', function(e){
	    	
	    });
	    
	    townCircle.addListener('click', function(e) {
	    	$("#selectCity option:selected").text(this.title);
	    	hotSpotReload(this.id, this.center.lat(), this.center.lng(), this.title);
	    });
	    
	    townCircles.push(townCircle);
	}
}

function addSaidFromMap(said) {
	var idx = 0;
//	$("button[name='mapAdd']").index();
	var saidDefault = $($("input[name='saidDefault']")[idx]).val();
	
	if (saidDefault == said){
		alert ('this said is default.other said input.');
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "checkExistSaid.do",
		dataType : "json",
		data : {said: said},
		async : false,
		success : function( data ) {
			if(data.result == "SUCCESS")
			{
				var saidListValue = "";
				if($($("input[name='saidList']")[idx]).val() == ""){
					saidListValue = said;
				}else{
					saidListValue = $($("input[name='saidList']")[idx]).val()+","+said;
				}
				$($("input[name='saidList']")[idx]).val(saidListValue);
				
				$($("input[name='said']")[idx]).val("");
				$("#circleCiryPop").modal('hide');
			}else{
				document.oncontextmenu = "return false";
				swal({
	                title: "Warn !",
	                text: "SAID-"+said+" is not exist" 
	            });
			}
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

function clearHotSpot() {
	hotSpotMap = new Array();
	for (var i = 0; i < hotSpotMarkers.length; i++) {
		hotSpotMarkers[i].labelVisible = false;
		hotSpotMarkers[i].setMap(null);
	}
	
	hotSpotMarkers = [];
}

function hotSpotReload(cityId, latitude, longitude, cityName) {
	clearCity();
	clearHotSpot();
	$("#addTable").show();
	$("#cityId").val(cityId);
	if($("#cityName").val() == ""){
		$("#cityName").val(cityName);
	}
	$("#latitude").val(latitude);
	$("#longitude").val(longitude);
	$("#cityLatitude").val(latitude);
	$("#cityLongitude").val(longitude);
	
	google.maps.event.addListener(map, 'rightclick', cityRightClickEmpty);
	google.maps.event.addListener(map, 'zoom_changed', getCity);
	
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
			
			
		},
		error : function(xhr, status, error){
			swal({
				title: "Fail !",
				text: "서버 통신 오류 입니다."
			});
		}
	});
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

function moveTownView(id) {
	map.setZoom(10);
}

function addToServiceArea( bmscId, serviceAreaId, isNotMapped ) {
	$.ajax({
        url : "/dashbd/api/addToServiceArea.do",
        type: "post",
        data : { "serviceAreaId" : serviceAreaId, "enbIds" : toAddEnbs },
        dateType : 'json',
        success : function(responseData){
        	
        	clearDatas();
        	
        	swal({
                title: "Success !",
                text: "등록이 완료 되었습니다."
            });
        	
        	if( isNotMapped ) {
        		getSeviceAreaNotMapped( bmscId );
        	} else {
            	var centerLatLng = map.getCenter();
            	moveToEnb(bmscId, serviceAreaId, "");
            	map.setCenter(centerLatLng);
        	}
        	getServiceAreaByBmScCity("1", $("#bmsc").val(), $("#checkCityName").val(), "");
        },
        error : function(xhr, status, error){
        	swal({
                title: "Error !",
                text: "오류가 발생했습니다."
            });
        }
    });
}

function addToServiceAreaManually() {
	if( isEmpty($('#toAddENBsBmscId').val()) ) {
		swal({
            title: "Sorry !",
            text: "Please Select BMSC first."
        });
	} else if( isEmpty($('#toAddENBsServiceAreaId').val()) ) {
		swal({
            title: "Sorry !",
            text: "Please Select ServiceArea first."
        });
	} else if( isEmpty($('#toAddENBs').val()) ) {
		swal({
            title: "Sorry !",
            text: "Please input eNB ID first."
        });
	} else {

		$.ajax({
	        url : "/dashbd/api/addToServiceAreaManually.do",
	        type: "post",
	        data : { "serviceAreaId" : $('#toAddENBsServiceAreaId').val(), "enbIds" : $('#toAddENBs').val() },
	        dateType : 'json',
	        success : function(responseData){

	            datas = JSON.parse( responseData );

	        	$('#toAddENBs').val( '' );
	        	var title = "";
	        	var successMsg = "";
	        	var successCnt = datas[0].enbCnt - datas[0].NEnbCnt ;
	        	if(successCnt == 0){
	        		title = "Fail";
	        		successMsg = "존재하지 않는 eNB ID입니다.";
	        	}else{
	        		title = "Success !";
		        	successMsg = "총 " + datas[0].enbCnt + "건 중 " + successCnt + " 건 등록이 완료 되었습니다.";
	        	}

	        	swal({
	                title: title,
	                text: successMsg
	            });
	        	
	        	var centerLatLng = map.getCenter();
	        	moveToEnb($('#toAddENBsBmscId').val(), $('#toAddENBsServiceAreaId').val(), "");
	        	map.setCenter(centerLatLng);
	        	getServiceAreaByBmScCity("1", $("#bmsc").val(), $("#checkCityName").val(), "");
	        },
	        error : function(xhr, status, error){
	        	swal({
	                title: "Fail !",
	                text: "등록이 실패 되었습니다."
	            });
	        	moveToEnb($('#toAddENBsBmscId').val(), $('#toAddENBsServiceAreaId').val(), "");
	        }
	    });
	}
}

function deleteFromServiceArea(bmscId, serviceAreaId) {
	$.ajax({
        url : "/dashbd/api/deleteFromServiceArea.do",
        type: "post",
        data : { "serviceAreaId" : serviceAreaId, "enbIds" : toDeleteEnbs },
        dateType : 'json',
        success : function(responseData){
        	clearDatas();
        	
        	swal({
                title: "Success !",
                text: "삭제가 완료 되었습니다."
            });
        	
        	var centerLatLng = map.getCenter();
        	moveToEnb(bmscId, serviceAreaId, "");
        	map.setCenter(centerLatLng);
        	getServiceAreaByBmScCity("1", $("#bmsc").val(), $("#checkCityName").val(), "");
        },
        error : function(xhr, status, error){
        	swal({
                title: "Fail !",
                text: "삭제를 실패하였습니다."
            });
        	moveToEnb(bmscId, serviceAreaId, "");
        }
    });
}

function clearCity() {
	thisCityMap = new Array();
	noneCityMap = new Array();
	otherCityMap = new Array();
	for (var i = 0; i < townCircles.length; i++) {
		townCircles[i].labelVisible = false;
		townCircles[i].setMap(null);
	}
	
	townCircles = [];
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

function focusCity(index, lat, lng, cityId) {
	$($("tr[data-index]")).css("background-color", "#FFFFFF");
	$($("tr[data-index]")[index]).css("background-color", "#D3D3D3");
	map.setCenter(new google.maps.LatLng(lat, lng));
}



