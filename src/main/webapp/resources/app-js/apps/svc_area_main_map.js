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
var enb_markers = [];
var enbInfoWindows = {};
var menuInfoWindows = {};
var default_lat = 36.869872;
var default_lng = 127.838728;
var default_zoom = 7;
var activeInfoWindow;

var shiftPressed = false;
var ctlPressed = false;

var toAddEnbs = [];
var toDeleteEnbs = [];

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
				if(enb_markers[i].isSameServieArea === null && typeof variable === "object") {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_gray.png") 
				} else if(enb_markers[i].isSameServieArea) {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_red.png")
				} else {
					enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_blue.png")
				}
				
				enb_markers[i].selected = false;
            }
	    	
	    	toAddEnbs = [];
	    	toDeleteEnbs = [];
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
	                	if(enb_markers[i].isSameServieArea === null && typeof variable === "object") {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
	                		toAddEnbs.push(enb_markers[i].infoWindowIndex);
	                	} else if(enb_markers[i].isSameServieArea) {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_red_on.png");
	                		toDeleteEnbs.push(enb_markers[i].infoWindowIndex);
	                	} else {
	                		enb_markers[i].setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
	                		toAddEnbs.push(enb_markers[i].infoWindowIndex);
	                	}
	                	
	                	enb_markers[i].selected = true;
	                	
	                	
	                } else {
	                    //markers[i].marker.setIcon("/dashbd/resources/img/icon/enb_gray.png")
	                }
	            }

	            gribBoundingBox.setMap(null); // remove the rectangle
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



function moveToEnb(bmscId, serviceAreaId)
{

	clearMarkers();
	clearEnbMarkers();
	clearDatas();

	
    
	$.ajax({
		url : "/dashbd/api/getServiceAreaEnbAp.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId },
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(responseData){
			$("#ajax").remove();
			var enb_datas = JSON.parse(responseData);
			var dataLen = enb_datas.length;
			var options = "";
			
			for(var i = 0; i < enb_datas.length; i++) {
				var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
				var marker;
								
				if( enb_datas[i].serviceAreaId == serviceAreaId ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_red.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : true,
						zIndex: google.maps.Marker.MAX_ZINDEX + 1
					});
				} else if( enb_datas[i].serviceAreaId == '' ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_gray.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : null
					});
				} else {
					marker = new google.maps.Marker({
						'position': latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_blue.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : false
					});
				}
				
				//alert(enb_datas[i].enbApName);
				var enbContent = '<ul>' 
				+ '<li>SA_ID : ' + enb_datas[i].serviceAreaId + '</li>'
				+ '<li>eNB ID : ' + enb_datas[i].enbApId + '</li>'
				+ '<li>eNB Name : '	+ enb_datas[i].enbApName + '</li>'
				+ '<li>PLMN : ' + enb_datas[i].plmn + '</li>'
				+ '<li>MBSFN : ' + enb_datas[i].mbsfn + '</li>'
				+ '</ul>';
				
				var menuContent = '<ul>' +
				//'<li><a href="#" class="more_info">More info</span></a></li>' +
				'<li><a href="javascript:addToServiceArea(' + serviceAreaId + ');" class="add_to_service_area">Add To Service Area</a></li>' +
				'<li><a href="javascript:deleteFromServiceArea(' + serviceAreaId + ');" class="delete_from_service_area">Delete From Service Area</a></li>' +
				'</ul>';
				
				var enbInfoWindow = new google.maps.InfoWindow({
					content: enbContent
				});
				
				var menuInfoWindow = new google.maps.InfoWindow({
					content: menuContent
				});
				
				enbInfoWindows[enb_datas[i].enbApId] = enbInfoWindow;
				menuInfoWindows[enb_datas[i].enbApId] = menuInfoWindow;
				
				//this.enbInfoWindow.setContent(enbContent);
				
				// add listener on InfoWindow for mouseover event
				google.maps.event.addListener(marker, 'mouseover', function() {
					
					// Close active window if exists - [one might expect this to be default behaviour no?]				
					if(activeInfoWindow != null) activeInfoWindow.close();

					// Close info Window on mouseclick if already opened
					//menuInfoWindows[this.infoWindowIndex].close();
					
					// Open new InfoWindow for mouseover event
					enbInfoWindows[this.infoWindowIndex].open(map, this);
					
					// Store new open InfoWindow in global variable
					activeInfoWindow = enbInfoWindows[this.infoWindowIndex];
				}); 							
				
				// on mouseout (moved mouse off marker) make infoWindow disappear
				google.maps.event.addListener(marker, 'mouseout', function() {
					enbInfoWindows[this.infoWindowIndex].close();	
				});
				
				/*
				google.maps.event.addListener(marker, 'click', function() {
					closeInfoWindow();
					this.infowindow.open(map, this);
				});
				*/
				
				// --------------------------------
				// ON MARKER CLICK - (Mouse click)
				// --------------------------------
				
				// add content to InfoWindow for click event 
				//this.menuInfoWindow.setContent(menuContent);
				
				// add listener on InfoWindow for click event
				google.maps.event.addListener(marker, 'click', function() {
					if(ctlPressed) {
						if(this.selected) {
							this.selected = false;
							if(this.isSameServieArea === null && typeof variable === "object") {
								this.setIcon("/dashbd/resources/img/icon/enb_gray.png");
								toAddEnbs.remove(this.infoWindowIndex);
		                	} else if(this.isSameServieArea) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red.png");
		                		toDeleteEnbs.remove(this.infoWindowIndex);
		                	} else {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue.png");
		                		toAddEnbs.remove(this.infoWindowIndex);
		                	}
						}
						else {
							this.selected = true;
							if(this.isSameServieArea === null && typeof variable === "object") {
								this.setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
								toAddEnbs.push(this.infoWindowIndex);
		                	} else if(this.isSameServieArea) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red_on.png");
		                		toDeleteEnbs.push(this.infoWindowIndex);
		                	} else {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
		                		toAddEnbs.push(this.infoWindowIndex);
		                	}
						}
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
			
			map.setCenter(new google.maps.LatLng(enb_datas[0].latitude, enb_datas[0].longitude));
			map.setZoom(12);
		}
	});
	
	var bounds = map.getBounds();

    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();
    
    $.ajax({
		url : "/dashbd/api/getServiceAreaEnbApOther.do",
		type: "get",
		data : { "serviceAreaId" : serviceAreaId, "swLat" : sw.lat(), "swLng" : sw.lng(), "neLat" : ne.lat(), "neLng" : ne.lng() },
		success : function(responseData){
			$("#ajax").remove();
			var enb_datas = JSON.parse(responseData);
			
			for(var i = 0; i < enb_datas.length; i++) {
				var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
				var marker;
								
				if( enb_datas[i].serviceAreaId == serviceAreaId ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_red.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : true,
						zIndex: google.maps.Marker.MAX_ZINDEX + 1
					});
				} else if( enb_datas[i].serviceAreaId == '' ) {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_gray.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : null
					});
				} else {
					marker = new google.maps.Marker({
						position: latLng, 
						map: map, 
						icon : '/dashbd/resources/img/icon/enb_blue.png',
						infoWindowIndex : enb_datas[i].enbApId,
						selected : false,
						isSameServieArea : false
					});
				}
				
				//alert(enb_datas[i].enbApName);
				var enbContent = '<ul>' 
				+ '<li>SA_ID : ' + enb_datas[i].serviceAreaId + '</li>'
				+ '<li>eNB ID : ' + enb_datas[i].enbApId + '</li>'
				+ '<li>eNB Name : '	+ enb_datas[i].enbApName + '</li>'
				+ '<li>PLMN : ' + enb_datas[i].plmn + '</li>'
				+ '<li>MBSFN : ' + enb_datas[i].mbsfn + '</li>'
				+ '</ul>';
				
				var menuContent = '<ul>' +
				//'<li><a href="#" onclick="return false;" class="more_info">More info</span></a></li>' +
				'<li><a href="javascript:addToServiceArea(' + serviceAreaId + ');" class="add_to_service_area">Add To Service Area</a></li>' +
				'<li><a href="javascript:deleteFromServiceArea(' + serviceAreaId + ');" class="delete_from_service_area">Delete From Service Area</a></li>' +
				'</ul>';
				
				var enbInfoWindow = new google.maps.InfoWindow({
					content: enbContent
				});
				
				var menuInfoWindow = new google.maps.InfoWindow({
					content: menuContent
				});
				
				enbInfoWindows[enb_datas[i].enbApId] = enbInfoWindow;
				menuInfoWindows[enb_datas[i].enbApId] = menuInfoWindow;
				
				//this.enbInfoWindow.setContent(enbContent);
				
				// add listener on InfoWindow for mouseover event
				google.maps.event.addListener(marker, 'mouseover', function() {
					
					// Close active window if exists - [one might expect this to be default behaviour no?]				
					if(activeInfoWindow != null) activeInfoWindow.close();

					// Close info Window on mouseclick if already opened
					//menuInfoWindows[this.infoWindowIndex].close();
					
					// Open new InfoWindow for mouseover event
					enbInfoWindows[this.infoWindowIndex].open(map, this);
					
					// Store new open InfoWindow in global variable
					activeInfoWindow = enbInfoWindows[this.infoWindowIndex];
				}); 							
				
				// on mouseout (moved mouse off marker) make infoWindow disappear
				google.maps.event.addListener(marker, 'mouseout', function() {
					enbInfoWindows[this.infoWindowIndex].close();	
				});
				
				/*
				google.maps.event.addListener(marker, 'click', function() {
					closeInfoWindow();
					this.infowindow.open(map, this);
				});
				*/
				
				// --------------------------------
				// ON MARKER CLICK - (Mouse click)
				// --------------------------------
				
				// add content to InfoWindow for click event 
				//this.menuInfoWindow.setContent(menuContent);
				
				// add listener on InfoWindow for click event
				google.maps.event.addListener(marker, 'click', function() {
					if(ctlPressed) {
						if(this.selected) {
							this.selected = false;
							if(this.isSameServieArea === null && typeof variable === "object") {
								this.setIcon("/dashbd/resources/img/icon/enb_gray.png");
								toAddEnbs.remove(this.infoWindowIndex);
		                	} else if(this.isSameServieArea) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red.png");
		                		toDeleteEnbs.remove(this.infoWindowIndex);
		                	} else {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue.png");
		                		toAddEnbs.remove(this.infoWindowIndex);
		                	}
						}
						else {
							this.selected = true;
							if(this.isSameServieArea === null && typeof variable === "object") {
								this.setIcon("/dashbd/resources/img/icon/enb_gray_on.png");
								toAddEnbs.push(this.infoWindowIndex);
		                	} else if(this.isSameServieArea) {
		                		this.setIcon("/dashbd/resources/img/icon/enb_red_on.png");
		                		toDeleteEnbs.push(this.infoWindowIndex);
		                	} else {
		                		this.setIcon("/dashbd/resources/img/icon/enb_blue_on.png");
		                		toAddEnbs.push(this.infoWindowIndex);
		                	}
						}
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



function drawServiceAreaByBmSc(bmscId) {
	clearMarkers();
	clearEnbMarkers();
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
						labelStyle: {opacity: 0.7},
						title: data[i].city,
						icon: {url: "/dashbd/resources/img/icon/enb_red_on.png"}
            		});

            		/*
            		var marker = new google.maps.Marker({
						position: area_positions[data[i].city],
						draggable: false,
						raiseOnDrag: true,
						map: map,
						icon: /dashbd/resources/img/icon/enb_red.png,
						title: data[i].city,
						label: '' + data[i].count
            		});
            		*/

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

function addToServiceArea(serviceAreaId) {
	
	alert('toAddEnbs length=[' + toAddEnbs.length + ']');
	$.ajax({
        url : "/dashbd/api/addToServiceArea.do",
        type: "post",
        data : { "serviceAreaId" : serviceAreaId, "enbIds" : toAddEnbs },
        dateType : 'json',
        success : function(responseData){
        	clearDatas();
        },
        error : function(xhr, status, error){
        	alert("error");
        }
    });
}

function deleteFromServiceArea(serviceAreaId) {
	alert('toDeleteEnbs length=[' + toDeleteEnbs.length + ']');
	$.ajax({
        url : "/dashbd/api/deleteFromServiceArea.do",
        type: "post",
        data : { "serviceAreaId" : serviceAreaId, "enbIds" : toDeleteEnbs },
        dateType : 'json',
        success : function(responseData){
            //$("#ajax").remove();
            //var data = JSON.parse(responseData);
        	alert("success");
        	clearDatas();
        },
        error : function(xhr, status, error){
        	alert("error");
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

function stopBounce() {

	for (var i = 0; i < enb_markers.length; i++) {
		if(enb_markers[i].getAnimation() !== null) {
			enb_markers[i].setAnimation(null);
		}
	}
}