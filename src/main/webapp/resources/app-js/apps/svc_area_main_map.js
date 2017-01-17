var initBmScId = 1;

$(document).ready(function()
{
	//getServiceAreaBmSc(1, $('#operator option:selected').val());
	$('#createServiceGroupLayer').on('hidden.bs.modal', function (e) {
		$('#createServiceGroupLayer').find('input').val('');
	})
//    $('#operator').change(function(){
//        getServiceAreaBmSc(1, $('#operator option:selected').val());
//    });
    
    $('#btn-add-service-area').click(function(){
    	
    	if( isEmpty( $('#operator option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select operator first."
            });
    	} else if( isEmpty(  ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select BM-SC first."
            });
    	} else {
    		$("#createServiceGroupLayer").modal();
    	}
    });
    
    $('#btn-not-mapped-service-area').click(function() {
    	if( isEmpty( $('#operator option:selected').val() ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select operator first."
            });
    	} else if( isEmpty(  ) ) {
    		swal({
                title: "Sorry !",
                text: "Please select BM-SC first."
            });
    	} else {
    		getSeviceAreaNotMapped(  );
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
    		$('#createServiceGroupLayer').modal('hide');
    	
    		createServiceArea( $('#operator option:selected').val());
    	}
    });

    
    $('#editSvcAreaBtn').click(function(){

    	if( isEmpty($('#editServiceAreaId').val()) ) {
    		swal({
                title: "Sorry !",
                text: "Please input Service Area ID first."
            });
    	} else if( isEmpty($('#editServiceAreaName').val()) ) {
    		swal({
                title: "Sorry !",
                text: "Please input Service Area Name first."
            });
    	} else {
    		$('#editServiceAreaLayer').modal('hide');
    	
    		editServiceArea( $('#operator option:selected').val());
    	}
    });
    
    //json 형태로 변환
    circlemap = JSON.parse(circlemap);
});

var perPage = 15;
var listPageCount = 10;

var enb_markers = [];
var enbInfoWindows = {};
var menuInfoWindows = {};
var shiftPressed = false;
var ctlPressed = false;
var toAddEnbs = [];
var toDeleteEnbs = [];
var selectedENBsCount = 0;

var map;
var modalMap;
var circles = [];
var cities = [];
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
//최근 클릭된 city Object를 담는 변수
var tempCityObj;

//메인 화면의 모달 로드
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});
	
	modalMap = new google.maps.Map(document.getElementById('modalMap'), {
		center: {lat: 22.352687, lng: 79.311189},
		zoom: default_zoom+1
	});

	var mouseDownPos, gribBoundingBox = null,
	    mouseIsDown = 0;
	var themap = map;
	
	google.maps.event.addListener(themap, 'zoom_changed', function() {
		//circle이 보이는 줌 레벨보다 멀어질 경우 circleList 그림
		if(this.zoom < default_zoom + 1) {
			if(circles.length == 0) {
				cityClear('cities');
				drawServiceAreaByBmSc();
			}
		} else {
			circleClear();
			
			if(cities.length == 0) {
				//서비스 영업 그룹이 한건이라도 선택됐어야 city를 그려줌
				serviceAreaGroupChoice();
			}
		}
	});
	
	// 처음 로딩 시 지도에 표시해주는 부분
	drawServiceAreaByBmSc();
}

//service area group표에서 선택된 로우가 있는지를 판별하여 city List를 가지고 오는 메소드
function serviceAreaGroupChoice() {
	cityClear('cities');
	
	for(var i=0; i < $("#area_group tr").length; i++) {
		var tempTr = $("#area_group tr")[i];
		
		if($(tempTr).attr("choiceYn") == 'Y') {
			drawServiceAreaByCity(map, $(tempTr).attr("data-init"), 'cities');
			break;
		}
	}
}

google.maps.event.addDomListener(window, 'load', initMap);

//도시 circle 삭제(메인 페이지와 모달 페이지를 구분하여 삭제)
function cityClear(targetCity) {
	if(targetCity == 'cities') {
		for(var i=0; i < cities.length; i++) {
			//서클 클리어
			var tempCity = cities[i];
			tempCity.setMap(null);
		}
		
		cities = [];
	} else {
		for(var i=0; i < modalCities.length; i++) {
			//서클 클리어
			var tempCity = modalCities[i];
			tempCity.setMap(null);
		}
		
		modalCities = [];
	}
}

function circleClear() {
	for(var i=0; i < circles.length; i++) {
		//서클 클리어
		var tempCircle = circles[i];
		tempCircle.setMap(null);
	}
	
	circles = [];
}

//도시 리스트를 찍어주는 메소드
function drawServiceAreaByCity(targetMap, group_id, targetCity) {
	$.ajax({
	    url : "/dashbd/api/getCitiesInServiceAreaGroup.do",
	    type: "POST",
	    async:false,
	    data : { 
	    	group_id : group_id
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        for (var city in data) {
	        	var cityCircle = new google.maps.Circle({
	  	  	      strokeColor: data[city].color,
	  	  	      strokeOpacity: 0.8,
	  	  	      strokeWeight: 2,
	  	  	      fillColor: data[city].color,
	  	  	      fillOpacity: 0.35,
	  	  	      map: targetMap,
	  	  	      center: data[city].center,
	  	  	      radius: Math.sqrt(data[city].population) * 10,
	  	  	      city_id:data[city].city_id,
	  	  	      city_name:data[city].city_name
	  	  	    });
	        	
	        	//매인 맵에 city circle을 넣어줄 경우
	        	if(targetCity == 'cities') {
	        		cities.push(cityCircle);
	        		
	        		//오른쪽을 클릭할 경우 infoWindow 뿌려줌 
	        		cityCircle.addListener('rightclick', function() {
	        			for(var i=0; i < $("#area_group tr").length; i++) {
	        				var tempTr = $("#area_group tr")[i];
	        				
	        				if($(tempTr).attr("choiceYn") == 'Y') {
	        					var contentString = '';
	    	        			if(this.fillColor == 'gray' || this.fillColor == 'blue') {
	    	        			   contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'add\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Add to ' + $(tempTr).find("td").text() + '</a>';
	    	        			} else {
	    	        				contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'delete\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Delete From ' + $(tempTr).find("td").text() + '</a>';
	    	        			}
	    	        			
	    	        			//선택된 city obj를 전역변수에 담음(color변경을 위해)
	    	        			tempCityObj = this;
	    	        			
	    	        			//이전에 열린 infoWindow가 있을 경우 닫아줌
	    	        			if(tempInfoWindow != undefined)
	    	        				tempInfoWindow.close();
	    	        			
	    	        			var infowindow = new google.maps.InfoWindow({ content: contentString
    	        								, position: new google.maps.LatLng(this.center.lat(), this.center.lng()) });
	    	        			infowindow.open(map, this);
	    	        			tempInfoWindow = infowindow
	        				}
	        			}
	        		});
	        	}
	        	//팝업 맵에 city circle을 넣어줄 경우
	        	else {
	        		modalCities.push(cityCircle);
	        		
	        		//오른쪽 마우스 클릭 이벤트 적용
	        		cityCircle.addListener('click', function() {
	        			if(this.fillColor == 'gray') 	        				
	        				this.setOptions({strokeColor:'red', fillColor:'red'})
	        			else 
	        				this.setOptions({strokeColor:'gray', fillColor:'gray'})
	        		});
	        	}
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
	      circleId : circle 
	    });
	    
	    cityCircle.addListener('click', function() {
	    	drawServiceAreaGroupList(this);
		});
	    
	    circles.push(cityCircle);
	}
}

//circle 선택시 service area group 리스트 조회 메소드 
function drawServiceAreaGroupList(circle) {
	var circleId = circle.circleId;
	
	$.ajax({
	    url : "/dashbd/api/getServiceAreaGroupList.do",
	    type: "POST",
	    data : { 
	    	circle_id : circleId
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        $("#area_group").empty();
	        
	        if( data.length != 0 ) {
	        	for(var i=0; i < data.length; i++) {
	        		$("#area_group").append('<tr onclick="selectServiceAreaGroup(this)" data-init="' 
		        			+ data[i].group_id + '" data-lat="' + circle.getCenter().lat() + '" data-lng="' + circle.getCenter().lng() + '" title="' 
		        			+ data[i].group_description + '"><td>' + data[i].group_name + '</td></tr>');
		        }
	        } else{
	        	$("#area_group").append('<tr><td>No Data</td></tr>');
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

//서비스 영억 그룹 테이블 선택시 발동하는 함수
function selectServiceAreaGroup(obj) {
	$(obj).css("background", blue);
	$(obj).css("color", white);
	$(obj).attr("choiceYn", 'Y');
	
	$(obj).siblings().css("background", white);
	$(obj).siblings().css("color", black);
	$(obj).siblings().attr("choiceYn", 'N');
	
	map.setCenter(new google.maps.LatLng( $(obj).attr("data-lat"), $(obj).attr("data-lng") ));
	map.setZoom(default_zoom + 2);
	
	//선택된 gruop에 따른 city 리스트 조회
	serviceAreaGroupChoice();
}

//service Group modal 호출
function openCreateServiceModal() {
	//팝업 내용 완전 초기화
	$("#serviceGroupName").val('');
	$("#serviceGroupName").prop("readonly", false);
	$("#serviceAreaDescription").val('');
	
	//기존에 추가한 도시가 있다면 모두 삭제
	$("#serviceGroupCityTab").empty();
	
	cityClear('modalCities');
	$("#createServiceGroupLayer").modal('show');
}

//서비스 그룹 이름의 중복 여부 판단
function checkServiceGroup() {
	if($("#serviceGroupName").val() != '') {
		$.ajax({
		    url : "/dashbd/api/checkServiceAreaGroupName.do",
		    type: "POST",
		    data : { 
		    	group_name : $("#serviceGroupName").val()
		    },
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		        
		        if(data.resultCode == 'S') {
		        	$("#serviceGroupName").prop("readonly", true);
		        } else {
		        	swal({
		                title: "Fail !",
		                text: "Already Exist Your Group Name"
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
	} else {
		swal({
            title: "Fail !",
            text: "Check Your Group Name"
        });
	}
}

//service group에 도시를 추가하기 위한 팝업 호출
function callSelectCityPop() {
	$("#selectCitiesModal").modal('show');
	
	$('#selectCitiesModal').on('shown.bs.modal', function () {
		google.maps.event.trigger(modalMap, "resize");
		
		//서비스 그룹 생성 팝업을 띄운상태에서 그 팝업을 닫지 않고 또다시 띄울경우 이전 상태 그대로 둠
		if(modalCities.length == 0)
			drawServiceAreaByCity(modalMap, '', 'modalCities');
	});
}

//도시 선택 팝업 에서 도시 선택 후 continue 버튼 클릭시 수행되는 메소드
function addCitiesInServiceGroup() {
	$("#serviceGroupCityTab").empty();
	
	for(var i=0; i < modalCities.length; i++) {
		var tempCity = modalCities[i];
		
		if(tempCity.fillColor == 'red') {
			$("#serviceGroupCityTab").append('<tr data-init="' + tempCity.city_id + '"><td>' + tempCity.city_name + '</td></tr>');
		}
	}
	
	$("#selectCitiesModal").modal('hide');
}

//서비스 그룹 생성 메소드
function createServiceGroup() {
	if($("#serviceGroupName").val().trim() != '' && $("#serviceGroupName").prop("readonly") == true) {
		if($("#serviceGroupCityTab tr").length > 0) {
			var cityListStr = '';
			for(var i=0; i < $("#serviceGroupCityTab tr").length; i++) {
				var tempCity = $("#serviceGroupCityTab tr")[i];
				cityListStr += ',' + $(tempCity).attr("data-init"); 
			}
			cityListStr = cityListStr.substring(1);
			
			$.ajax({
			    url : "/dashbd/api/insertServiceAreaGroup.do",
			    type: "POST",
			    data : { 
			    	group_name : $("#serviceGroupName").val(),
			    	group_description : $("#serviceAreaDescription").val(),
			    	cityListStr : cityListStr
			    },
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			    success : function(responseData) {
			        $("#ajax").remove();
			        var data = JSON.parse(responseData);
			        
			        if(data.resultCode == 'S') {
			        	swal({
			                title: "Success !",
			                text: "Success"
			            });
			        	
			        	location.reload();
			        } else {
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
		} else {
			swal({
	            title: "Fail !",
	            text: "Check Your City Group"
	        });
		}
	} else {
		swal({
            title: "Fail !",
            text: "Check Your Group Name"
        });
	}
}

//메인 맵에서 도시를 서비스 그룹으로 넣거나 뺴는 메소드
function addAndDeleteCityInServiceGroup(div, group_id, city_id) {
	$.ajax({
	    url : "/dashbd/api/addDeleteCitiInServiceAreaGroup.do",
	    type: "POST",
	    data : { 
	    	div : div,
	    	group_id : group_id,
	    	city_id : city_id
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        //데이터 등록이나 삭제에 성공했을 경우 수행
	        if(data.resultCode == 'S') {
	        	if(div == 'add') {
	        		tempCityObj.setOptions({strokeColor:'red', fillColor:'red'})
	        	} else if(div == 'delete') {
	        		tempCityObj.setOptions({strokeColor:'gray', fillColor:'gray'})
	        	}
	        } 
	        //삭제 했을 경우 다른 서비스 그룹에 있다면 파란색으로 셋팅
	        else if(data.resultCode == 'E') {
	        	tempCityObj.setOptions({strokeColor:'blue', fillColor:'blue'})
	        }
	        else {
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
	        
	        //열려있던 infoWindow 닫기
	        tempInfoWindow.close();
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
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
