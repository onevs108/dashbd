
/**
 * Permission ID
 */
var PERMISSION_ID_USER = 1;
var PERMISSION_ID_PERMISSION = 2;
var PERMISSION_ID_CONTENTS = 3;
var PERMISSION_ID_OPERATOR = 4;
var PERMISSION_ID_BMSC = 5;
var PERMISSION_ID_SERVICE_AREA = 6;
var PERMISSION_ID_ENB = 7;
var PERMISSION_ID_SCHEDULE = 8;
var PERMISSION_ID_SYSTEM = 9;
var PERMISSION_ID_STATISTIC = 10;
var PERMISSION_ID_SYSTEM_CONFIG = 11;
var PERMISSION_ID_DB_CONFIG = 12;

/**
 * ajax 사용 시 session check
 */
//(function($) {
//	$.ajaxSetup({
//		beforeSend: function(xhr) {
//			xhr.setRequestHeader("AJAX", true);
//		},
//		error: function(xhr, status, err) {
//			if (xhr.status == 401 || xhr.status == 403) {
//				alert('Session expried!');
//				location.href = '/dashbd/out';
//			}
//			else {
//				alert('Error occurred! status: ' + status + ', err: ' + err);
//			}
//		}
//	});
//})(jQuery);

/**
 * 사용자 등급에 맞는 사이드 메뉴 받아와 화면구성
 */
function getMenuList(currentMenu) {
	$.ajax({
		url: '/dashbd/api/menu.do',
		method: 'POST',
		dataType: 'json',
		data: {
			currentMenu: currentMenu
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result == 0) {
				var menuHtml = data.menuHtml;
				if (menuHtml != null && menuHtml.length > 0)
				$('#side-menu').append(menuHtml);
				$('#navbar-user-name').html(data.userLastName + ' ' + data.userFirstName);
			}
			else if (data.result == 1) {
				alert('Expired session!');
				location.href = '/dashbd/out';
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			return false;
		}
	});
}

function goSystemMgmt(){
	$.post( "/dashbd/resources/systemMgmt.do", function( data ) {
		$("#page-wrapper").html( data );
	});
}
///dashbd/resources/systemMgmt.do
$(document).on('click', '.nav.metismenu li',function(e){
	if($(e.target).closest('li').find('ul').length > 0){
		if($(e.target).closest('li').hasClass('active')){
			$(e.target).closest('li').removeClass('active');
			$(e.target).closest('li').find('ul').removeClass('collapse in');
			$(e.target).closest('li').find('ul').addClass('collapsing');
			$(e.target).closest('li').find('ul').addClass('collapse');
		}else{
			$(e.target).closest('li').addClass('active');
			$(e.target).closest('li').find('ul').addClass('collapse in');
			$(e.target).closest('li').find('ul').removeClass('collapsing');
			$(e.target).closest('li').find('ul').removeClass('collapse');
		}
	}
});

function launchCenter(url, name, width, height, scroll) {
	var str = "height=" + height + ",innerHeight=" + height;
	str += ",width=" + width + ",innerWidth=" + width;
	str += ",status=no,scrollbars=" + scroll;

	if (window.screen)
	{
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;

		var xc = (aw - width) / 2;
		var yc = (ah - height) / 2;

		str += ",left=" + xc + ",screenX=" + xc;
		str += ",top=" + yc + ",screenY=" + yc;
	}

	return window.open(url, name, str);
}

//이전에 선택한 마커를 저장하는 임시 전역변수
var modalMarker;
var latTarget;
var lngTarget;

//위치 선정을 위한 모달 맵 호출
function callSetLocationModalMap(obj, accessDiv, zoomLevel, lat, lng) {
	if(accessDiv == 'serviceArea') {
		if($(obj).parents("li").length > 0) {
			latTarget = $($(obj).parents("li")[0]).find("input[name='lat']");
			lngTarget =	$($(obj).parents("li")[0]).find("input[name='lng']");
		} else if($(obj).parents("table").length > 0) {
			latTarget = $(obj).parents("table").find("tr td input[name='lat']");
			lngTarget =	$(obj).parents("table").find("tr td input[name='lng']");
		}
		
	} else if (accessDiv == 'hotspotAdd') {
		latTarget = $(obj).parents("form").find("input[id='latitude']");
		lngTarget =	$(obj).parents("form").find("input[id='longitude']");
	}
	
	//위도 경도 값이 없을 경우 기본값 셋팅
	if(lat == "undefined" || lng == "undefined") {
		lat = 22.059619;
		lng = 78.934389;
		zoomLevel = 'circle';
	}
	
	if(zoomLevel == undefined) {
		modalMap = new google.maps.Map(document.getElementById('modalMap'), {
			center: {lat: 22.059619, lng: 78.934389},
			zoom: 5
		});
		
		lat = 22.059619;
		lng = 78.934389;
	} else {
		var zoom = 0;
		if(zoomLevel == 'circle') zoom = 5;
		else if(zoomLevel == 'city') zoom = 9;
		else if(zoomLevel == 'hotspot') zoom = 15;
		
		modalMap = new google.maps.Map(document.getElementById('modalMap'), {
			center: {lat: Number(lat), lng: Number(lng)},
			zoom: zoom
		});
		
		var infowindow = new google.maps.InfoWindow({
		    content: '<button type="button" class="btn btn-primary4" onclick="settingLatLng()">Use Location</button>'
		});
		
		var marker = new google.maps.Marker({
		    position: {lat: Number(lat), lng: Number(lng)},
		    map: modalMap
		  });
		
		modalMarker = marker;
	}
	
	modalMap.addListener('click', function(event) {
		if(modalMarker != undefined) modalMarker.setMap(null);
		
		var infowindow = new google.maps.InfoWindow({
		    content: '<button type="button" class="btn btn-primary4" onclick="settingLatLng()">Use Location</button>'
		});
		
		var marker = new google.maps.Marker({
		    position: event.latLng,
		    map: modalMap
		  });
		
		marker.addListener('click', function() {
			infowindow.open(modalMap, marker);
		});
		
		google.maps.event.trigger(marker, "click");
		
		modalMarker = marker;
	});
	
	$("#setLocationModal").modal('show');
	$('#setLocationModal').on('shown.bs.modal', function () {
		google.maps.event.trigger(modalMap, "resize");
		modalMap.setCenter( new google.maps.LatLng( lat, lng) );
	});
}

//위치 지정 맵에서 위치 사용 버튼 클릭시 셋팅해주는 메소드
function settingLatLng() {
	latTarget.val(modalMarker.position.lat());
	lngTarget.val(modalMarker.position.lng());
	
	$("#setLocationModal").modal('hide');
}


//유효성 체크 메소드
function validationCheck(type, obj) {
	var tempVal = obj.value; 
	
	if(type == 'number') {
		if(tempVal != '') {
			var num_check=/^[\.0-9]*$/;
			if(!num_check.test(tempVal)) {
				obj.value = '';
				swal({
	                title: "Fail !",
	                text: "Please enter a number"
	            }, function() {
	            	$(obj).focus();
	            });
			}
		}
	}
}

function getTimeStamp() {
	  var d = new Date();
	  var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2) + ' ' +

	    leadingZeros(d.getHours(), 2) + ':' +
	    leadingZeros(d.getMinutes(), 2) + ':' +
	    leadingZeros(d.getSeconds(), 2);

	  return s;
	}

function leadingZeros(n, digits) {
  var zero = '';
  n = n.toString();

  if (n.length < digits) {
    for (i = 0; i < digits - n.length; i++)
      zero += '0';
  }
  return zero + n;
}

//시간 더하기 계산
//date : 2016-10-10 23:00
function getTimeDiff(date, time) {
	var	startTime = new Date(date.substr(0,4), date.substr(5,2)-1, date.substr(8,2), date.substr(11,2), date.substr(14,2));
	var	currentTime = new Date(time.substr(0,4), time.substr(5,2)-1, time.substr(8,2), time.substr(11,2), time.substr(14,2));
	
	if((currentTime - startTime) > -1) {
		return true;
	}
	return false;
}


//시간 초 더하기
//date : 2016-10-10 23:00
function getTimeAddSecond(date, second) {
	var	d = new Date(date.substr(0,4), date.substr(5,2)-1, date.substr(8,2), date.substr(11,2), date.substr(14,2), date.substr(17,2));
	d.setSeconds(d.getSeconds()+second);
	
	var s =
		leadingZeros(d.getFullYear(), 4) + '-' +
		leadingZeros(d.getMonth() + 1, 2) + '-' +
		leadingZeros(d.getDate(), 2) + ' ' + d.toTimeString().substr(0,8);
	return s;
}

//시간 초 더하기 + 가드타임
//date : 2016-10-10 23:00
function getTimeAddSecond15Guard(date, second) {
	var	d = new Date(date.substr(0,4), date.substr(5,2)-1, date.substr(8,2), date.substr(11,2), date.substr(14,2), date.substr(17,2));
	d.setSeconds(second+15);
	
	var s =
		leadingZeros(d.getFullYear(), 4) + '-' +
		leadingZeros(d.getMonth() + 1, 2) + '-' +
		leadingZeros(d.getDate(), 2) + ' ' + d.toTimeString().substr(0,8);
	return s;
}