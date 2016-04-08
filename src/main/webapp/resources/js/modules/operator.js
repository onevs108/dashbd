var operatorId = null;
var checkOperatorName = false;

$(function() {
	// button click event in list
	$('#modal-open-btn').click(openModal);
	$('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
	
	$('#check-name-btn').click(doCheckName);
	// name 입력 or 변경 checkOperatorName 초기화
	$('#form-operator-name').keypress(function() {
		checkOperatorName = false;
		$('#form-operator-name-input-area').attr('class', 'input-group has-warning');
	});
});

function openModal() {
	checkOperatorName = false;
	$('#modal-title').html('Create New Operator');
	$('#form-modal').modal('show');
}

function doAdd() {
	$('#modal-title').html('Create New Operator');
	
	var operatorName = $('#form-operator-name').val();
	var operatorDescription = $('#form-operator-description').val();
	
	if (operatorName == null || operatorName.length == 0) {
		alert('Please input the Operator Name');
		return false;
	}
	
	if (!checkOperatorName) {
		alert('Please check the Operator Name');
		$('#check-name-btn').focus();
		return false;
	}
	
	if (operatorDescription == null || operatorDescription.length == 0) {
		alert('Please input the Description');
		return false;
	}
	
	var data = {
		id: operatorId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
		name: operatorName,
		description: operatorDescription
	}
	callInsert(data);
}

function doModalCancel() {
	operatorId = null;
	operatorCheckName = false;
	closeModal();
}

function doEdit(id, name, description) {
	$('#modal-title').html('Edit Operator');
	checkOperatorName = true; // 수정 창을 처음 열었을땐 체크 된 상태이다.
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

function doCheckName() {
	var operatorName = $('#form-operator-name').val();
	if (operatorName == null || operatorName.length == 0) {
		alert('Please input Operator Name');
		return false;
	}
	
	$.ajax({
		url: '/dashbd/api/operator/check.do',
		method: 'POST',
		dataType: 'json',
		data: {
			operatorName: operatorName
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				checkOperatorName = true;
				$('#form-operator-name-input-area').attr('class', 'input-group has-success');
				alert('Avaliable!');
			}
			else { // 실패
				checkOperatorName = false;
				$('#form-operator-name-input-area').attr('class', 'input-group has-error');
				alert('Already exist!');
				$('#form-operator-name').focus();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			checkUserId = false;
			return false;
		}
	});
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
	var pageNumber = 1;
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/operator/list.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			location.href = '#';
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
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
			title: 'Operator ID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: false
		}, {
			field: 'name',
			title: 'Operator Name',
			width: '30%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'description',
			title: 'Description',
			width: '50%',
			align: 'left',
			valign: 'middle',
			sortable: true
		}, {
			field: 'command',
			title: 'Command',
			width: '20%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
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