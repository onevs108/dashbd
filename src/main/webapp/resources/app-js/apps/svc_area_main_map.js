var map;
var modalMap;
var circles = [];
var cities = [];
var hotspots = [];
var modalCities = [];

var default_lat = 24;
var default_lng = 82.975;
var default_zoom = 5;
var activeInfoWindow;
var default_center_lat = 24;
var default_center_lng = 83.975;

var red = '#FF0000';
var blue = '#1c84c6';
var gray = '#c2c2c2';
var white = '#FFFFFF';
var black = '#000000';

//최근 클릭된 InfoWindow를 담는 변수
var tempInfoWindow;
//마지막으로 클릭된 상위 Object를 담는 변수
var upperObj;

//현재 줌레벨을 담고있는 변수 선언
var currentZoomLevel = 'circle';

$(document).ready(function()
{
	jsTreeSetting();
    //json 형태로 변환
    circlemap = JSON.parse(circlemap);
});

function tabChange(tabDiv) {
	if(tabDiv == '1') {
		$($("ul.nav.nav-tabs")[0]).addClass("active");
		$($("ul.nav.nav-tabs")[1]).removeClass("active");
		$("#map").hide();
		$("#treeNode").show();
		
		jsTreeSetting();
	} else if(tabDiv == '2') {
		$($("ul.nav.nav-tabs")[1]).addClass("active");
		$($("ul.nav.nav-tabs")[0]).removeClass("active");
		$("#map").show();
		$("#treeNode").hide();
		
		google.maps.event.trigger(map, "resize");
		map.setCenter( new google.maps.LatLng( default_center_lat, default_center_lng ) );
	}
}

function jsTreeSetting() {
	$.getScript( "/dashbd/resourcesRenew/js/plugins/jsTree/jstree.min.js" )
		.done(function( script, textStatus ) {
			$.ajax({
			    url : "/dashbd/api/getTreeNodeData.do",
			    type: "POST",
			    data : { 
			    	gruop_id : ''
			    },
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			    success : function(responseData) {
			        $("#ajax").remove();
			        var data = JSON.parse(responseData);
			        
			        $("#treeNode").jstree("destroy").empty();
			        treeInit(data);
			    },
		        error : function(xhr, status, error) {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
			});
		});
}

//jsTree Init Function
function treeInit(data) {
	var treeData = data.resultList;
	for(var i=0; i < treeData.length; i++) {
		var node = treeData[i];
		
		if(i == 0) {
			$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + '</li></ul>');
			continue;
		}
		
		var compareClass = '';
		if(node.node_div == 'circle') compareClass='root';
		else if(node.node_div == 'city') compareClass='circle';
		else if(node.node_div == 'hotspot') compareClass='city';
		
		for(var j=0; j < $('#treeNode li.' + compareClass).length; j++) {
			var compareNode = $('#treeNode li.' + compareClass)[j];
			
			if($(compareNode).attr("data-init") == node.pnode_id) { 
				var liStr = '<li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name;
				liStr += '<span style="margin-left:20px;"><input type="text" name="lat" ></span>';
				liStr += '<span style="margin-left:10px;"><input type="text" name="lng" ></span>';
				liStr += '<span style="margin-left:10px;"><button type="button" class="btn btn-success btn-xs button-edit" onclick="callSetLocationModalMap(this, \'serviceArea\')">Map</button></span>';
				liStr += '</li>';
				
				if($(compareNode).html().indexOf("ul") == -1) {
					$(compareNode).append('<ul>' + liStr + '</ul>');
				} else {
					$(compareNode).find("ul").append(liStr)
				}
				
				break;
			}
		}
	}
	
	$("#treeNode").jstree({
	    "conditionalselect" : function (node, event) {
	      return false;
	    },
	    "plugins" : [ "conditionalselect" ]
	  });
	
	//제일 처음 노드 오픈
	$("#treeNode").jstree("open_node", $("#j1_1"))
}

//메인 화면의 모달 로드
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});
	
//	var mouseDownPos, gribBoundingBox = null,
//	    mouseIsDown = 0;
//	var themap = map;
	
	google.maps.event.addListener(map, 'zoom_changed', function() {
		currentZoomLevel = checkZoomLevel(this.zoom);
		
		//circle이 보이는 줌 레벨보다 멀어질 경우 circleList 그림
		if(currentZoomLevel == 'circle') {
			if(circles.length == 0) {
				cityClear('cities');
				hotspotClear();
				drawServiceAreaByBmSc();
			}
		} else if(currentZoomLevel == 'city') {
			circleClear();
			hotspotClear();
			
//			if(cities.length == 0) {
//				drawServiceAreaByCity(tempCircleObj);
//			}
		} else if(currentZoomLevel == 'hotspot') {
			circleClear();
			cityClear('cities');
		}
	});
	
	//맵 클릭 이벤트 부여
	google.maps.event.addListener(map, 'click', function(event) {
	    var contentString = makeInfoWindow('add', event);
		
		//이전에 열린 infoWindow가 있을 경우 닫아줌
		if(tempInfoWindow != undefined)
			tempInfoWindow.close();
		
		var infowindow = new google.maps.InfoWindow({ content: contentString
						, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
		infowindow.open(map, this);
		tempInfoWindow = infowindow
	});
	
	// 처음 로딩 시 지도에 표시해주는 부분
	drawServiceAreaByBmSc();
}

function makeInfoWindow(div, object) {
	var contentString = '';
	contentString = '<div>';
	contentString = '<form name="serviceAreaForm">'; 
	contentString += '<input type="hidden" name="proccessDiv">';
	contentString += '<input type="hidden" name="currentZoomLevel">';
	if(currentZoomLevel != 'circle') {
		contentString += '<input type="hidden" name="upper_said">';
		contentString += '<input type="hidden" name="upper_name">';
	}
	contentString += '<table>';
	contentString += '<tbody>';
	contentString += '<tr>';
	var upperChar = currentZoomLevel.substring(0,1).toUpperCase();
	contentString += '<td>' + upperChar + currentZoomLevel.substring(1) + '</td>';
	var name = '';
	if(div == 'edit') name = object.name;
	contentString += '<td colspan="2"><input type="text" style="width:100%" name="name" value="' + name + '"></td>';
	contentString += '</tr>';
	contentString += '<tr>';
	contentString += '<td>SAID</td>';
	var said = '';
	if(div == 'edit') said = object.said;
	contentString += '<td colspan="2"><input type="text" style="width:100%" name="said" value="' + said + '" ' + (div == "edit"? "readonly" : "") + '></td>';
	contentString += '</tr>';
	contentString += '<tr>';
	contentString += '<td>Latitude</td>';
	var lat,lng;
	if(div == 'add') {
		lat =  object.latLng.lat();
		lng =  object.latLng.lng();
	}
	else if(div == 'edit') {
		lat = object.center.lat();
		lng = object.center.lng();
	}
	contentString += '<td><input type="text" name="lat" value="' + lat + '" readonly></td>';
	if(div == 'edit')
		contentString += '<td rowspan="2"><button type="button" class="btn btn-success btn-xs button-edit" style="height:100%" onclick="callSetLocationModalMap(this, \'serviceArea\', \'' + currentZoomLevel + '\', ' + lat + ', ' + lng + ')">Reset<br>Location</button></td>';
	contentString += '</tr>';
	contentString += '<tr>';
	contentString += '<td>Longitude</td>';
	contentString += '<td><input type="text" name="lng" value="' + lng + '" readonly></td>';
	contentString += '</tr>';
	if(currentZoomLevel != 'circle') {
		var bandwidth = '';
		if(div == 'edit') bandwidth = object.bandwidth;
		contentString += '<tr>';
		contentString += '<td>Bandwidth</td>';
		contentString += '<td><input type="text" name="bandwidth" value="' + bandwidth + '"></td>';
		contentString += '</tr>';
	}
	contentString += '</tbody>';
	contentString += '</table>';
	contentString += '</form>';
	contentString += '</div>';
	contentString += '<div style="text-align:center; margin-top:3px">';
	
	if(div == 'add') {
		contentString += '<button type="button" class="btn btn-success btn-xs button-edit" onclick="serviceAreaProccess(\'add\')">Add</button>';
	} else if(div == 'edit') {
		contentString += '<button type="button" class="btn btn-success btn-xs button-edit" onclick="serviceAreaProccess(\'edit\')">Edit</button>';
		contentString += '<button type="button" class="btn btn-sm btn-default proccess-btn" onclick="serviceAreaProccess(\'delete\')">Delete</button>';
	}
	
	contentString += '</div>';
	
	return contentString; 
}

//현재 줌레벨을 기준으로 어느 데이터를 보여줄지 판단하는 메소드
function checkZoomLevel(zoom) {
	var zoomLevel = '';
	
	//circle level
	if(zoom < 8) {
		zoomLevel = 'circle';
		map.setOptions({ maxZoom: 7 });
	} 
	//city level
	else if(zoom < 10) {
		zoomLevel = 'city';
		map.setOptions({ maxZoom: 9 });
	} 
	//hotspot level
	else {
		zoomLevel = 'hotspot';
		map.setOptions({ maxZoom: 20 });
	}
	
	return zoomLevel;
}

google.maps.event.addDomListener(window, 'load', initMap);

function circleClear() {
	for(var i=0; i < circles.length; i++) {
		//서클 클리어
		var tempCircle = circles[i];
		tempCircle.setMap(null);
	}
	
	circles = [];
}

//도시 삭제(메인 페이지와 모달 페이지를 구분하여 삭제)
function cityClear(targetCity) {
	if(targetCity == 'cities') {
		for(var i=0; i < cities.length; i++) {
			var tempCity = cities[i];
			tempCity.setMap(null);
		}
		
		cities = [];
	} else {
		for(var i=0; i < modalCities.length; i++) {
			var tempCity = modalCities[i];
			tempCity.setMap(null);
		}
		
		modalCities = [];
	}
}

//핫스팟 삭제
function hotspotClear() {
	for(var i=0; i < hotspots.length; i++) {
		//서클 클리어
		var tempHotspot = hotspots[i];
		tempHotspot.setMap(null);
	}
	
	hotspots = [];
}

//서클 리스트를 지도상에 그려주는 메소드
function drawServiceAreaByBmSc() {
	for (var circle in circlemap) {
		//위도 경도를 숫자값으로 변경하여 셋팅
		circlemap[circle].center.lat = Number(circlemap[circle].center.lat);
		circlemap[circle].center.lng = Number(circlemap[circle].center.lng);
		
	    // Add the circle for this circle to the map.
		var cityCircle = new google.maps.Circle({
	      strokeColor: red,
	      strokeOpacity: 0.8,
	      strokeWeight: 2,
	      fillColor: red,
	      fillOpacity: 0.35,
	      map: map,
	      center: circlemap[circle].center,
	      radius: Math.sqrt(circlemap[circle].population) * 100,
	      said : circlemap[circle].circle_id,
	      name : circlemap[circle].circle_name
	    });
	    
	    cityCircle.addListener('click', function() {
	    	//선택된 object를 전역변수에 담음(추후 추가시에 부모 노드로 사용)
	    	upperObj = this;
	    	drawServiceAreaByCity(this);
		});
	    
	    cityCircle.addListener('rightclick', function(event) {
	    	var contentString = makeInfoWindow('edit', this);
			
			//이전에 열린 infoWindow가 있을 경우 닫아줌
			if(tempInfoWindow != undefined)
				tempInfoWindow.close();
			
			var infowindow = new google.maps.InfoWindow({ content: contentString
							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
			infowindow.open(map, this);
			tempInfoWindow = infowindow
		});
	    
	    circles.push(cityCircle);
	}
}

//도시 리스트를 찍어주는 메소드
function drawServiceAreaByCity(circle) {
	$.ajax({
	    url : "/dashbd/api/getCitiesInCircle.do",
	    type: "POST",
//	    async:false,
	    data : { 
	    	circle_id : circle.said
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        //도시 기본 줌 사이즈로 셋팅
	        map.setZoom(9);
	        //서클의 위도 경도로 이동
	        map.setCenter(new google.maps.LatLng(circle.center.lat(), circle.center.lng()));
	        
	        for (var city in data) {
	        	var cityCircle = new google.maps.Circle({
	  	  	      strokeColor: data[city].color,
	  	  	      strokeOpacity: 0.8,
	  	  	      strokeWeight: 2,
	  	  	      fillColor: data[city].color,
	  	  	      fillOpacity: 0.35,
	  	  	      map: map,
	  	  	      center: data[city].center,
	  	  	      radius: Math.sqrt(data[city].population) * 10,
	  	  	      said:data[city].city_id,
	  	  	      name:data[city].city_name,
	  	  	      bandwidth:data[city].bandwidth
	  	  	    });
	        	
	        	//매인 맵에 city circle을 넣어줄 경우
//	        	if(targetCity == 'cities') {
	        		cities.push(cityCircle);
	        		
	        		cityCircle.addListener('click', function() {
	        			//선택된 object를 전역변수에 담음(추후 추가시에 부모 노드로 사용)
	        	    	upperObj = this;
	        			drawServiceAreaByHotspot(this);
	        		});
	        		
	        		cityCircle.addListener('rightclick', function(event) {
	        	    	var contentString = makeInfoWindow('edit', this);
	        			
	        			//이전에 열린 infoWindow가 있을 경우 닫아줌
	        			if(tempInfoWindow != undefined)
	        				tempInfoWindow.close();
	        			
	        			var infowindow = new google.maps.InfoWindow({ content: contentString
	        							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
	        			infowindow.open(map, this);
	        			tempInfoWindow = infowindow
	        		});
	        		
	        		//오른쪽을 클릭할 경우 infoWindow 뿌려줌 
//	        		cityCircle.addListener('rightclick', function() {
//	        			for(var i=0; i < $("#area_group tr").length; i++) {
//	        				var tempTr = $("#area_group tr")[i];
//	        				
//	        				if($(tempTr).attr("choiceYn") == 'Y') {
//	        					var contentString = '';
//	    	        			if(this.fillColor == 'gray' || this.fillColor == 'blue') {
//	    	        			   contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'add\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Add to ' + $(tempTr).find("td").text() + '</a>';
//	    	        			} else {
//	    	        				contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'delete\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Delete From ' + $(tempTr).find("td").text() + '</a>';
//	    	        			}
//	    	        			
//	    	        			//선택된 city obj를 전역변수에 담음(color변경을 위해)
//	    	        			tempCityObj = this;
//	    	        			
//	    	        			//이전에 열린 infoWindow가 있을 경우 닫아줌
//	    	        			if(tempInfoWindow != undefined)
//	    	        				tempInfoWindow.close();
//	    	        			
//	    	        			var infowindow = new google.maps.InfoWindow({ content: contentString
//    	        								, position: new google.maps.LatLng(this.center.lat(), this.center.lng()) });
//	    	        			infowindow.open(map, this);
//	    	        			tempInfoWindow = infowindow
//	        				}
//	        			}
//	        		});
//	        	}
	        	//팝업 맵에 city circle을 넣어줄 경우
//	        	else {
//	        		modalCities.push(cityCircle);
//	        		
//	        		//오른쪽 마우스 클릭 이벤트 적용
//	        		cityCircle.addListener('click', function() {
//	        			if(this.fillColor == 'gray') 	        				
//	        				this.setOptions({strokeColor:'red', fillColor:'red'})
//	        			else 
//	        				this.setOptions({strokeColor:'gray', fillColor:'gray'})
//	        		});
//	        	}
	        }
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
}

//핫스팟 리스트를 찍어주는 메소드
function drawServiceAreaByHotspot(city) {
	$.ajax({
	    url : "/dashbd/api/getHotspotsInCities.do",
	    type: "POST",
	    async:false,
	    data : { 
	    	city_id : city.city_id
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        //도시 기본 줌 사이즈로 셋팅
	        map.setZoom(14);
	        //서클의 위도 경도로 이동
	        map.setCenter(new google.maps.LatLng(city.center.lat(), city.center.lng()));
	        
	        for (var hotspot in data) {
	        	var hotspotMarker = new google.maps.Marker({
	  	  	      map: map,
	  	  	      position: data[hotspot].center,
	  	  	      title: data[hotspot].hotspot_name,
		  	  	  hotspot_id:data[hotspot].hotspot_id,
		  	  	  hotspot_name:data[hotspot].hotspot_name,
		  	  	  bandwidth:data[hotspot].bandwidth
	  	  	    });
	        	
	        	hotspotMarker.addListener('rightclick', function(event) {
	    	    	var contentString = makeInfoWindow('edit', this);
	    			
	    			//이전에 열린 infoWindow가 있을 경우 닫아줌
	    			if(tempInfoWindow != undefined)
	    				tempInfoWindow.close();
	    			
	    			var infowindow = new google.maps.InfoWindow({ content: contentString
	    							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
	    			infowindow.open(map, this);
	    			tempInfoWindow = infowindow
	    		});
	        	
	        	hotspots.push(hotspotMarker);
	        }
	    },
	    error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
}

//circle, city, hotspot을 추가, 수정, 삭제할 경우 사용하는 메소드
function serviceAreaProccess(div) {
	//필수값이 모두 입력되어 있을 경우에는 이 값을 변경하지 않아 ajax 수행
	var ajaxYn = true;
	
	//form에 셋팅
	$("form[name='serviceAreaForm'] input[name='proccessDiv']").val(div); 
	$("form[name='serviceAreaForm'] input[name='currentZoomLevel']").val(currentZoomLevel);
	
	if(currentZoomLevel != 'circle') {
		//city, hotspot 마냥 상위 노드가 필요할 경우에만 셋팅
		if(upperObj != undefined) {
			$("form[name='serviceAreaForm'] input[name='upper_said']").val(upperObj.said);
			$("form[name='serviceAreaForm'] input[name='upper_name']").val(upperObj.name);
		}
	}
	
	$.each($("form[name='serviceAreaForm'] input"), function(index, obj) {
		if($(obj).val() == '') {
			swal({
              title: "Fail !",
              text: "Insert Value"
	        });
			
			$(obj).focus();
			ajaxYn = false;
			return false;
		}
	});
	
	if(ajaxYn) {
		$.ajax({
		    url : "/dashbd/api/serviceAreaProccess.do",
		    type: "POST",
		    data : $("form[name='serviceAreaForm']").serialize(),
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		        
		        if(data.resultCode == 'S') {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        } 
		        else if(data.resultCode == 'E') {
		        	swal({
		                title: "Exist Code Value",
		                text: "Exist SAID Code Value"
		            });
		        }
		        else {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
		    },
		    error : function(xhr, status, error) {
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
		});
	}
}




































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

function doDeleteService(dType, serviceAreaId, enBCnt, bmscId, circle) {
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
						getServiceAreaByBmScCity("1", bmscId, circle, "");
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

function searchToServiceArea(bmscId, circle){
	getServiceAreaByBmScCity("1", bmscId, circle, $("#toSearchTxt").val());
}
function getServiceAreaByBmScCity(page, bmscId, circle, toSearchTxt)
{
    $("#checkCityName").val(circle);
	var selectedCity = encodeURIComponent(circle);
	$.ajax({
        url : "/dashbd/api/serviceAreaByBmScCity.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId, "circle" : selectedCity, "toSearchTxt" : encodeURIComponent(toSearchTxt) },
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success : function(responseData){
            $("#ajax").remove();
            datas = JSON.parse(responseData);
            var dataLen = datas.length;
            var options = "";
            var idx = 0;
            
            options += "<div class=\"ibox-title\"><h5>Service Area for " + circle + "</h5></div>";
			options += "<div class=\"ibox-content\">";
			options += "<div class=\"input-group\"><input type=\"text\" class=\"form-control\" id=\"toSearchTxt\" name=\"toSearchTxt\" value=\""+toSearchTxt+"\" placeholder=\"SA_ID or SA_NAME\" />";
			options += "<span class=\"input-group-btn\">";
			options += '<button type="button" class="btn btn-primary4" onclick="javascript:searchToServiceArea(\'' + bmscId + '\', \'' + circle + '\');" id="toSearchBtn">Search</button>';
			options += "</span>";
			options += "</div>";
			options += "</div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">SA_NAME<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">COMMAND</th></tr></thead>";
			options += "<tbody>";
            for(var i=0; i<dataLen; i++){
            	options += "<tr id=\"" + datas[i].serviceAreaId + "\" class=\"footable-even\" style=\"display: table-row;cursor:pointer;\" onclick=\"javascript:moveToEnb(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ", '" + circle + "');\"><td>";
             	var html = '<button type="button" onclick="doEditService(\'Y\',\'' + datas[i].serviceAreaId + '\', \'' + datas[i].serviceAreaName + '\', \'' + datas[i].description + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
				+ '<button type="button" onclick="doDeleteService(\'Y\', \'' + datas[i].serviceAreaId + '\', \'' + datas[i].totalCount + '\', \'' + bmscId + '\', \'' + circle + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
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
