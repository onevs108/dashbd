
$(function() {
	// button click event in list
	$('#go-search').click(doSearch);
	$('#save-btn').click(doSave);
	$('#cancel-btn').click(doCancel);
	$('#search-operator-id').change(doSearch);
});

function doSearch() {
	$('#table').bootstrapTable('destroy');
	getUserList();
}

function doSave() {
	if (curUser == null) {
		alert('Please select user');
		return false;
	}
	
	var checkedPermission = [];
	$('input:checkbox[name="permissions"]:checked').each(function(pi, po) {
		checkedPermission.push(po.value);
	});
	
	// 화면 처리
	initTableRow();
	initPermissionBox();
	
	var body = {
		userId: curUser.userId,
		permissions: checkedPermission
	};
	
	$.ajax({
		url: '/dashbd/api/permission/insert.do',
		method: 'POST',
		dataType: 'json',
		data: {
			body: JSON.stringify(body)
		},
		success: function(data, textStatus, jqXHR) {
			curUser = null; // null로 초기화
			if (data.result)
				alert('Success!');
			else
				alert('Error occurred!');
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			curUser = null; // null로 초기화
			return false;
		}
	});
}

function doCancel() {
	initTableRow();
	initPermissionBox();
	curUser = null;
}

var curUser = null;
function getUserList() {
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/permission/userlist.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
//			location.href = '#';
			
			params['searchUserId'] = $('#search-keyword').val();
			params['searchOperatorId'] = $('#search-operator-id').val();
			
			// 테이블 재 구성 시 permission box 초기화
			initPermissionBox();
			
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: 1,
		pageSize: 10,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		onClickRow: function(row, $element) {
			curUser = row;
			
			// 선택한 row의 background 색깔 변경
			$tds = $element.parent().find('td');
			for (var inx = 0; inx < $tds.length; inx++)
				$tds[inx].removeAttribute('style');
			
			$td = $('>td', $element);
			$td.attr('style', 'background:#1c84c6; color:#fff; font-weight: bold;');
			
			getUserPermissions();
		},
		columns: [{
			field: 'id',
			title: 'No.',
			sortable: false,
			visible: false
		}, {
			field: 'userId',
			title: 'ID',
			width: '25%',
			align: 'left',
			valign: 'middle',
			sortable: true
//			formatter: function(value, row, index) {
//				var html = '<i class="fa fa-circle fa-fw"></i> ' + row.userId;
//				return html;
//			}
		}, {
			field: 'firstName',
			title: 'First Name',
			width: '25%',
			align: 'left',
			valign: 'middle',
			sortable: true
		}, {
			field: 'lastName',
			title: 'Last Name',
			width: '25%',
			align: 'left',
			valign: 'middle',
			sortable: true
		}, {
			field: 'department',
			title: 'Department',
			width: '25%',
			align: 'left',
			valign: 'middle',
			sortable: true
		}]
	});
}

function getUserPermissions() {
	initPermissionBox();
	
	// permission box 타이틀 변경
	$('#permission-box-title').html('Permission Granted for ' + curUser.userId);
	
	$.ajax({
		url: '/dashbd/api/permission/list.do',
		method: 'POST',
		dataType: 'json',
		data: {
			userId: curUser.userId
		},
		success: function(data, textStatus, jqXHR) {
			for (var inx = 0; inx < data.permissionList.length; inx++) {
				switch (data.permissionList[inx].id) {
				case PERMISSION_ID_USER:
					$('#checkbox-permission-user').prop("checked", true);
					break;
				case PERMISSION_ID_PERMISSION:
					$('#checkbox-permission-permission').prop("checked", true);
					break;
				case PERMISSION_ID_CONTENTS:
					$('#checkbox-permission-contents').prop("checked", true);
					break;
				case PERMISSION_ID_OPERATOR:
					$('#checkbox-permission-operator').prop("checked", true);
					break;
				case PERMISSION_ID_BMSC:
					$('#checkbox-permission-bmsc').prop("checked", true);
					break;
				case PERMISSION_ID_SERVICE_AREA:
					$('#checkbox-permission-service-area').prop("checked", true);
					break;
				case PERMISSION_ID_ENB:
					$('#checkbox-permission-enb').prop("checked", true);
					break;
				case PERMISSION_ID_SCHEDULE:
					$('#checkbox-permission-schedule').prop("checked", true);
					break;
				default:
					break;
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			return false;
		}
	});
}

function initPermissionBox() {
	// permission box 타이틀 변경
	$('#permission-box-title').html('Permission Granted');
	
	$('#checkbox-permission-user').prop("checked", false);
	$('#checkbox-permission-permission').prop("checked", false);
	$('#checkbox-permission-contents').prop("checked", false);
	$('#checkbox-permission-operator').prop("checked", false);
	$('#checkbox-permission-bmsc').prop("checked", false);
	$('#checkbox-permission-service-area').prop("checked", false);
	$('#checkbox-permission-enb').prop("checked", false);
	$('#checkbox-permission-schedule').prop("checked", false);
}

function initTableRow() {
	$tds = $('#table').find('td');
	for (var inx = 0; inx < $tds.length; inx++)
		$tds[inx].removeAttribute('style');
}

