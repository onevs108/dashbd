
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
			center: {lat: lat, lng: lng},
			zoom: zoom
		});
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
			var num_check=/^[0-9]*$/;
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