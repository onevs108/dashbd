
$(function() {
	// button click event in list
	$('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
});

var operatorId = null;
function doAdd() {
	var operatorName = $('#form-operator-name').val();
	var operatorDescription = $('#form-operator-description').val();
	
	if (operatorName == null || operatorName.length == 0) {
		alert('Please input the Operator Name');
		return false;
	}
	
	if (operatorDescription == null || operatorDescription.length == 0) {
		alert('Please input the Description');
		return false;
	}
	
	var data = {
		id: operatorId,
		name: operatorName,
		description: operatorDescription
	}
	callInsert(data);
}

function doModalCancel() {
	closeModal();
}

function doEdit(id, name, description) {
	operatorId = id;
	$('#form-operator-name').val(name);
	$('#form-operator-description').val(description);
	$('#form-modal').modal('show');
}

function doDelete(operatorId, name) {
	if (confirm('Do you really want to delete the Operator "' + name + '"?')) {
		$.ajax({
			url: '/dashbd/api/operator/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				operatorId: operatorId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					$('#table').bootstrapTable('destroy');
					getOperatorList();
				}
				else { // 실패
					alert('Failed!! Please you report to admin!');
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert(errorThrown + textStatus);
				return false;
			}
		});
	}
}

function callInsert(data) {
	$.ajax({
		url: '/dashbd/api/operator/insert.do',
		method: 'POST',
		dataType: 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				$('#table').bootstrapTable('destroy');
				getOperatorList();
				closeModal();
				operatorId = null; // 초기화
			}
			else { // 실패
				alert('Failed!! Please you report to admin!');
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown + textStatus);
			return false;
		}
	});
}

function getOperatorList() {
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/operator/list.do',
		contentType: 'application/json',
		dataType: 'json',
//		queryParams: function(params) {
//			location.href = '#';
//			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
//			return params;
//		},
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
		columns: [{
			field: 'id',
			title: 'Operator ID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'name',
			title: 'Operator Name',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'description',
			title: 'Description',
			width: '50%',
			align: 'left',
			valign: 'middle',
			sortable: false
		}, {
			field: 'command',
			title: 'Command',
			width: '20%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var no = row.no;
				var html = '<button type="button" onclick="doEdit(\'' + row.id + '\', \'' + row.name + '\', \'' + row.description + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doDelete(\'' + row.id + '\', \'' + row.name + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}

function closeModal() {
	$('#form-operator-name').val('');
	$('#form-operator-description').val('');
	$('#form-modal').modal('hide');
}