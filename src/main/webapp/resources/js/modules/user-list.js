
$(function() {
	// button click event in list
	$('#go-search').click(doSearch);
	$('#btn-add-user').click(doAdd);
});

function doSearch() {
	$('#table').bootstrapTable('destroy');
	getUserList(false, true);
}

function doAdd() {
	location.href = '/dashbd/resources/userform.do?flag=add';
}

function doInfo(userId) {
	location.href = '/dashbd/resources/userform.do?flag=info&userId=' + userId;
}

function doEdit(userId) {
	location.href = '/dashbd/resources/userform.do?flag=edit&userId=' + userId;
}

function doDelete(userId, firstName, lastName) {
	if (confirm('Do you really want to delete the user "' + firstName + ' ' + lastName + '"?')) {
		$.ajax({
			url: '/dashbd/api/user/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				userId: userId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					$('#table').bootstrapTable('destroy');
					getUserList(true, false);
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

function getUserList(isBack, isSearch) {
	var pageNumber = 1;
	var searchKeyword = '';
	var searchColumn = '';
	if (isBack) {
		pageNumber = $.cookie('pagaNumber');
		searchKeyword = $.cookie('searchKeyword');
		searchColumn = $.cookie('searchColumn');
		
		$('#search-keyword').val(searchKeyword);
		$('#search-column').val(searchColumn);
	}
	
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/user/list.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			location.href = '#';
			
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
			var tempSearchKeyword = $('#search-keyword').val();
			var tempsearchColumn = $('#search-column').val();
			$.cookie('searchKeyword', tempSearchKeyword);
			$.cookie('searchColumn', tempsearchColumn);
			params['searchKeyword'] = tempSearchKeyword;
			params['searchColumn'] = tempsearchColumn;
			
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 10,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'id',
			title: 'No.',
			width: '5%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'lastName',
			title: 'Last Name',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'firstName',
			title: 'First Name',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'userId',
			title: 'ID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'department',
			title: 'Department',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'operatorId',
			title: 'Operator',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'command',
			title: 'Command',
			width: '25%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var no = row.no;
				var html = '<button type="button" onclick="doInfo(\'' + row.userId + '\')" class="btn btn-default btn-xs button-info">Info</button> '
						+ '<button type="button" onclick="doEdit(\'' + row.userId + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doDelete(\'' + row.userId + '\', \'' + row.firstName + '\', \'' + row.lastName + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}