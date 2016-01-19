
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