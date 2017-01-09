
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
