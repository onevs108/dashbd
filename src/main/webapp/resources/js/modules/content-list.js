
$(function() {
	// button click event in list
	$('#go-search').click(doContentSearch);
	$('#btn-add-user').click(doContentAdd);
});

function doContentSearch() {
	$('#table').bootstrapTable('destroy');
	getContentList(false, true);
}

function doContentAdd() {
	location.href = '/dashbd/view/addContent.do';
}

function doContentInfo(id) {
	location.href = '/dashbd/view/viewContent.do?id=' + id;
}

function doContentEdit(id) {
	location.href = '/dashbd/view/editContent.do?id=' + id;
}

function doContentDelete(id) {
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
				
				if (data.code) { // 성공
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
		searchType = $.cookie('searchType');
		searchOperatorId = $.cookie('searchOperatorId');
		searchKeyword = $.cookie('searchKeyword');
		searchColumn = $.cookie('searchColumn');
		
		$('#search-type').val(searchType);
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
			var tempSearchType = $('#search-type').val();
			var tempSearchOperatorId = $('#search-operator-id').val();
			var tempSearchColumn = $('#search-column').val();
			var tempSearchKeyword = $('#search-keyword').val();
			
			$.cookie('searchType', tempSearchType);
			$.cookie('searchOperatorId', tempSearchOperatorId);
			$.cookie('searchColumn', tempSearchColumn);
			$.cookie('searchKeyword', tempSearchKeyword);
			
			params['searchType'] = tempSearchType;
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
				var html = '<button type="button" onclick="doContentInfo(\'' + row.id + '\')" class="btn btn-default btn-xs button-info">Info</button> '
						+ '<button type="button" onclick="doContentEdit(\'' + row.id + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doContentDelete(\'' + row.id + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}