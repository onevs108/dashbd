
$(function() {
	// button click event in list
	$('#go-search').click(doSearch);
	$('#btn-add-user').click(doAdd);
});

function doSearch() {
	$('#table').bootstrapTable('destroy');
	getContentList(false, true);
}

function doAdd() {
	location.href = '/dashbd/view/addContent.do';
}

function doInfo(id) {
	location.href = '/dashbd/view/viewContent.do?id=' + id;
}

function doEdit(id) {
	location.href = '/dashbd/view/editContent.do?id=' + id;
}

function doDelete(id) {
	if (confirm('Do you really want to delete ?')) {
		$.ajax({
			url: '/dashbd/api/content.do',
			method: 'POST',
			dataType: 'json',
			data: {
				request_type : 'delete',
				id: id
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					$('#table').bootstrapTable('destroy');
					getContentList(true, false);
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

function getContentList(isBack, isSearch) {
	var pageNumber = 1;
	var searchOperatorId = '';
	var searchColumn = '';
	var searchKeyword = '';
	if (isBack) {
		pageNumber = $.cookie('pagaNumber');
		searchOperatorId = $.cookie('searchOperatorId');
		searchKeyword = $.cookie('searchKeyword');
		searchColumn = $.cookie('searchColumn');
		
		$('#search-operator-id').val(searchOperatorId);
		$('#search-column').val(searchColumn);
		$('#search-keyword').val(searchKeyword);
	}
	
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/content/list.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			location.href = '#';
			
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
			var tempSearchOperatorId = $('#search-operator-id').val();
			var tempSearchColumn = $('#search-column').val();
			var tempSearchKeyword = $('#search-keyword').val();
			
			$.cookie('searchOperatorId', tempSearchOperatorId);
			$.cookie('searchColumn', tempSearchColumn);
			$.cookie('searchKeyword', tempSearchKeyword);
			
			params['searchOperatorId'] = tempSearchOperatorId;
			params['searchColumn'] = tempSearchColumn;
			params['searchKeyword'] = tempSearchKeyword;
			
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
			field: 'title',
			title: 'Title',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'category',
			title: 'Category',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'duration',
			title: 'Running time',
			width: '12%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'fileFormat',
			title: 'File Format',
			width: '12%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'description',
			title: 'Description',
			width: '31%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'command',
			title: 'Command',
			width: '15%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var no = row.no;
				var html = '<button type="button" onclick="doInfo(\'' + row.id + '\')" class="btn btn-default btn-xs button-info">Info</button> '
						+ '<button type="button" onclick="doEdit(\'' + row.id + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doDelete(\'' + row.id + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}