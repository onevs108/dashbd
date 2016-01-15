$(document).ready(function()
{
    $('#operator').change(function(){
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
        drawServiceAreaByBmSc($('#bmsc option:selected').val());
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
            for(var i=0; i<dataLen; i++){
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

var map;
var markers = [];
var enb_markers = [];
var enbInfoWindows = {};
var menuInfoWindows = {};
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;
var activeInfoWindow;
var default_center_lat = 37.551968;
var default_center_lng = 126.991696;

var shiftPressed = false;
var ctlPressed = false;

var toAddEnbs = [];
var toDeleteEnbs = [];

var default_service_area = "<div class=\"ibox-title\"><h5>Service Area  </h5></div>";
default_service_area += "<div class=\"ibox-content\">";
default_service_area += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
default_service_area += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
default_service_area += "<tbody>";
default_service_area += "<tr>";
default_service_area += "<td></td>";
default_service_area += "<td></td>";
default_service_area += "</tr>";
default_service_area += "</tbody>";
default_service_area += "<tfoot>";
default_service_area += "<tr>";
default_service_area += "<td colspan=\"2\">";
default_service_area += "</td>";
default_service_area += "</tr>";
default_service_area += "</tfoot>";
default_service_area += "</table>";
default_service_area += "</div>";

var default_enb_table = "<table class=\"footable table table-stripped\" data-page-size=\"10\">";
default_enb_table += "<thead>";
default_enb_table += "<tr>";
default_enb_table += "<th class=\"col-sm-1\">eNB ID</th>";
default_enb_table += "<th class=\"col-sm-3\">eNB Name</th>";
default_enb_table += "<th class=\"col-sm-1\">eNB ID</th>";
default_enb_table += "<th class=\"col-sm-3\">eNB Name</th>";
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

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});

	// mouse drag start
	// Start drag rectangle to select markers !!!!!!!!!!!!!!!!

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
}

google.maps.event.addDomListener(window, 'load', initMap);


function createServiceArea( operatorId, bmscId ) {
	$.ajax({
	    url : "/dashbd/api/createServiceArea.do",
	    type: "get",
	    data : { "bmscId": bmscId, "serviceAreaId" : $('#serviceAreaId').val(), "serviceAreaName" : $('#serviceAreaName').val(), "serviceAreaDescription" : $('#serviceAreaDescription').val() },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);

	        if( data.count == 1 ) {
	        	swal({
	                title: "Success !",
	                text: "Service Area 등록이 완료되엇습니다."
	            });
	        	
	        	getSeviceAreaNotMapped( bmscId );
	        	
	        	map.setZoom( 12 );
	            moveToEnbNotMappedSA(bmscId, $('#serviceAreaId').val(), default_center_lat, default_center_lng);
	            
	    	} else if( data.count == 0 ) {
	    		swal({
	                title: "Info !",
	                text: "이미 등록된 Service Area ID 입니다."
	            });
	        } else if( data.count < 0 ) {
	        	swal({
	                title: "Fail !",
	                text: "Service Area 등록을 실패하였습니다."
	            });
	        }
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Service Area 등록을 실패하였습니다."
            });
        }
	});
}

function getSeviceAreaNotMapped( bmscId ) {
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
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
			options += "<tbody>";
			
            for( var i = 0; i < dataLen; i++ ) {
            	if( i % 2 == 0 ) {
            		options += "<tr class=\"footable-even\" style=\"display: table-row;\"><td>";
            	} else {
            		options += "<tr class=\"footable-odd\" style=\"display: table-row;\"><td>";
            	}
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"javascript:map.setZoom( 12 );moveToEnbNotMappedSA(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ", " + default_center_lat + ", " + default_center_lng + ");\">";
            	options += datas[i].serviceAreaId;
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td></tr>";
            }

            options += "</tbody>";
            
            options += "<tfoot><tr><td colspan=\"2\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            
            options += "</table></div>";
            
            $("#service_area").empty();
            $("#service_area").append( options );
            
            $('.footable').footable();
        },
        error : function( xhr, status, error ) {
        }
    });
}

function moveToEnb(bmscId, serviceAreaId)
{
	google.maps.event.clearListeners( map, 'idle' );
	clearMarkers();
	clearEnbMarkers();
	clearDatas();
	
	$("#enb_table").empty();
	$("#enb_table").append(default_enb_table);
	
	$('#toAddENBsServiceAreaId').val(serviceAreaId);

	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbAp.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId },
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(responseData){
			$("#ajax").remove();
			var enb_datas = JSON.parse(responseData);
			var dataLen = enb_datas.length;
			var options = "<table class=\"footable table table-stripped\" data-page-size=\"10\">";
			options += "<thead>";
			options += "<tr>";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\">eNB Name</th>";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\">eNB Name</th>";
			options += "<th class=\"col-sm-1\">eNB ID</th>";
			options += "<th class=\"col-sm-3\">eNB Name</th>";
			options += "</tr>";
			options += "</thead>";
			options += "<tbody>";
			
			for(var i = 0; i < enb_datas.length; i++) {
				
				if( i == 0 ) {
					options += "<tr>";
				}
				else if( i % 3 == 0 ) {
					options += "</tr><tr>";
				}
				options += "<td>";
				options += "<a href=\"javascript:moveToSelectedEnb(" + enb_datas[i].latitude + ", " + enb_datas[i].longitude + ");\">";
				options += enb_datas[i].enbApId;
				options += "</a>";
				options += "</td>";
				options += "<td>";
				options += enb_datas[i].enbApName;
				options += "</td>";
			}
			
			if( dataLen % 3 == 0 ) {
				options += "</tr>";
			} else if( dataLen % 3 == 1 ) {
				options += "<td></td></tr>";
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

			$("#enb_table").empty();
            $("#enb_table").append(options);
            
            $('.footable').footable();
            
            $("#selectedSvcArea").empty();
            $("#selectedSvcArea").append("Service Area : " + serviceAreaId);
            
            $("#selectedENBs").empty();
            selectedENBsCount = enb_datas.length;
            $("#selectedENBs").append( "Selected eNBs : " + 0 );
            
            map.setZoom( 12 );
            moveToEnbWithBounds(bmscId, serviceAreaId, enb_datas[0].latitude, enb_datas[0].longitude);
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
				+ '<li>SA_ID : ' + enb_datas[i].serviceAreaId + '</li>'
				+ '<li>eNB ID : ' + enb_datas[i].enbApId + '</li>'
				+ '<li>eNB Name : '	+ enb_datas[i].enbApName + '</li>'
				+ '<li>PLMN : ' + enb_datas[i].plmn + '</li>'
				+ '<li>MBSFN : ' + enb_datas[i].mbsfn + '</li>'
				+ '</ul>';
				
				var menuContent = '<ul>' +
				'<li><a href="javascript:addToServiceArea(' + bmscId + ', ' + serviceAreaId + ', ' + false + ');" class="add_to_service_area">Add To Service Area</a></li>' +
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
	
	$('#toAddENBsServiceAreaId').val(serviceAreaId);
	
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
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
			options += "<tbody>";
			
            for(var i=0; i<dataLen; i++){
             	if( i % 2 == 0 ) {
            		options += "<tr class=\"footable-even\" style=\"display: table-row;\"><td>";
            	} else {
            		options += "<tr class=\"footable-odd\" style=\"display: table-row;\"><td>";
            	}
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"javascript:moveToEnb(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ");\">";
            	options += datas[i].serviceAreaId;
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td></tr>";
            }

            options += "</tbody>";
            
            if(dataLen > 30) {
            	options += "<tfoot><tr><td colspan=\"2\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
            
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
            
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



function drawServiceAreaByBmSc(bmscId) {
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
	
    map.setZoom(7);
    
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
						labelStyle: {opacity: 0.7},
						title: data[i].city,
						icon: {url: "/dashbd/resources/img/icon/enb_red_on.png"}
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
            	moveToEnb(bmscId, serviceAreaId);
            	map.setCenter(centerLatLng);
        	}
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
	        	
	        	clearDatas();
	        	$('#toAddENBs').val( '' );
	        	
	        	swal({
	                title: "Success !",
	                text: "등록이 완료 되었습니다."
	            });
	        	
	        	var centerLatLng = map.getCenter();
	        	moveToEnb($('#toAddENBsBmscId').val(), $('#toAddENBsServiceAreaId').val());
	        	map.setCenter(centerLatLng);
	        },
	        error : function(xhr, status, error){
	        	swal({
	                title: "Fail !",
	                text: "등록이 실패 되었습니다."
	            });
	        	moveToEnb($('#toAddENBsBmscId').val(), $('#toAddENBsServiceAreaId').val());
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
        	moveToEnb(bmscId, serviceAreaId);
        	map.setCenter(centerLatLng);
        },
        error : function(xhr, status, error){
        	swal({
                title: "Fail !",
                text: "삭제를 실패하였습니다."
            });
        	moveToEnb(bmscId, serviceAreaId);
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