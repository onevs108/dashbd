$(function() {
	// button click event in list
	$('#go-search').click(doSearch);
});

function getUserList(isBack, isSearch) {
	var pageNumber = 1;
	var searchOperatorId = '';
	var searchColumn = '';
	var searchKeyword = '';
	if (isBack) {
		pageNumber = $.cookie('pagaNumber');
		searchOperatorId = $.cookie('searchOperatorId');
		searchKeyword = $.cookie('searchKeyword');
		searchColumn = $.cookie('searchColumn');
		searchCircleId = $.cookie('searchCircleId');
		
		$('#search-operator-id').val(searchOperatorId);
		$('#search-column').val(searchColumn);
		$('#search-keyword').val(searchKeyword); 
		$('#search-circle-id').val(searchCircleId);
		
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
			var tempSearchOperatorId = $('#search-operator-id').val();
			var tempSearchColumn = $('#search-column').val();
			var tempSearchKeyword = $('#search-keyword').val();
			var tempSearchCircleId = $('#search-circle-id').val();
			
			$.cookie('searchOperatorId', tempSearchOperatorId);
			$.cookie('searchColumn', tempSearchColumn);
			$.cookie('searchKeyword', tempSearchKeyword); 
			$.cookie('searchCircleId', tempSearchCircleId);
			
			params['searchOperatorId'] = tempSearchOperatorId;
			params['searchColumn'] = tempSearchColumn;
			params['searchKeyword'] = tempSearchKeyword;
			params['searchCircleId'] = tempSearchCircleId;
			
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
			field: 'gradeName',
			title: 'Group',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				var returnVal = '';
				if(value == null) returnVal = 'N/A';
				else returnVal = value;
				
				return returnVal;
			}
		}, {
			field: 'operatorName',
			title: 'Circle',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				var returnVal = '';
				if(value == '') returnVal = 'N/A';
				else returnVal = value;
				
				return returnVal;
			}
		}, {
			field: 'userId',
			title: 'ID',
			width: '13%',
			align: 'center',
			valign: 'middle', 
			sortable: true
		}, {
			field: 'lastName',
			title: 'Last Name',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'firstName',
			title: 'First Name',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'department',
			title: 'Department',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'command',
			title: 'Command',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var no = row.no;
				var html = '<button type="button" onclick="doInfo(\'' + row.userId + '\')" class="btn btn-default btn-xs button-info">Info</button> '
						+ '<button type="button" onclick="doEdit(\'' + row.userId + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doDelete(\'' + row.userId + '\', \'' + row.operatorId + '\', \'' + row.firstName + '\', \'' + row.lastName + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}

function doDelete(userId, operatorId, firstName, lastName) {
	swal({
	  title: "Are you sure?",
	  text: 'Do you really want to delete the user "' + firstName + ' ' + lastName + '"?',
	  type: "warning",
	  showCancelButton: true,
	  confirmButtonColor: "#DD6B55",
	  confirmButtonText: "Yes",
	  closeOnConfirm: false
	},
	function(){
		$.ajax({
			url: '/dashbd/api/user/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				userId: userId,
				operatorId: operatorId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					swal("Success !", "Success !", "success");
					$('#table').bootstrapTable('destroy');
					getUserList(true, false);
				}
				else { // 실패
					swal("Fail !", "Failed!! Please you report to admin!", "warning");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !", errorThrown + textStatus, "warning");
				return false;
			}
		});
	});
}
