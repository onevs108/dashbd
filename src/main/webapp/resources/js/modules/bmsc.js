
$(function() {
	// button click event in list
	$('#modal-open-btn').click(openModal);
	$('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
	$('#check-name-btn').click(doCheckName);
	$('#search-operator-id').change(changeOperator);
});

function changeOperator() {
	$('#table').bootstrapTable('destroy');
	getBmscList();
}

function openModal() {
	$('#form-operator-id').val($('#search-operator-id').val());
	$('#modal-title').html('Create New BM-SC');
	$('#form-modal').modal('show');
}

function doCheckName() {
	var bmscName = $('#form-bmsc-name').val();
	if (bmscName == null || bmscName.length == 0) {
		alert('Please input Bmsc Name');
		return false;
	}
	$.ajax({
		url: '/dashbd/api/bmsc/check.do',
		method: 'POST',
		dataType: 'json',
		data: {
			bmscName: bmscName,
			operatorId: $("#form-operator-id").val()
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				checkBmscName = true;
				$('#form-bmsc-name-input-area').attr('class', 'input-group has-success');
				alert('Avaliable!');
			}
			else { // 실패
				checkBmscName = false;
				$('#form-bmsc-name-input-area').attr('class', 'input-group has-error');
				alert('Already exist!');
				$('#form-bmsc-name').focus();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			checkUserId = false;
			return false;
		}
	});
}
var bmscId = null;
function doAdd() {
	var bmscName = $('#form-bmsc-name').val();
	var bmscCircle = $('#form-bmsc-circle').val();
	var bmscIpaddress = $('#form-bmsc-ipaddress').val();
	
	if (bmscName == null || bmscName.length == 0) {
		alert('Please input the Name');
		return false;
	}
	
	if (bmscIpaddress == null || bmscIpaddress.length == 0) {
		alert('Please input the IP Address');
		return false;
	}
	
	if (bmscCircle == null || bmscCircle.length == 0) {
		alert('Please input the Circle');
		return false;
	}
	
	var data = {
		id: bmscId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
		operatorId: $('#form-operator-id').val(),
		name: bmscName,
		ipaddress: bmscIpaddress,
		circle: bmscCircle
	}
	callInsert(data);
}

function doModalCancel() {
	bmscId = null;
	closeModal();
}

function openEditModal(id, name, ipaddress, circle) {
	bmscId = id;
	
	$('#form-operator-id').val($('#search-operator-id').val());
	$('#modal-title').html('Edit BM-SC');
	$('#form-bmsc-name').val(name);
	$('#form-bmsc-ipaddress').val(ipaddress);
	$('#form-bmsc-circle').val(circle);
	$('#form-modal').modal('show');
}

function doDelete(bmscId, name) {
	if (confirm('Do you really want to delete the BM-SC "' + name + '"?')) {
		$.ajax({
			url: '/dashbd/api/bmsc/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				bmscId: bmscId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					$('#table').bootstrapTable('destroy');
					getBmscList();
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
		url: '/dashbd/api/bmsc/insert.do',
		method: 'POST',
		dataType: 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				$('#table').bootstrapTable('destroy');
				getBmscList();
				closeModal();
				bmscId = null; // 초기화
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

function getBmscList() {
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/bmsc/list.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
//			location.href = '#';
			params['operatorId'] = $('#search-operator-id').val();
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
		columns: [{
			field: 'id',
			title: 'BMSC ID',
			visible: false
		}, {
			field: 'name',
			title: 'BM-SC Name',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'ipaddress',
			title: 'IP Address',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'circle',
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
				var html = '<button type="button" onclick="openEditModal(\'' + row.id + '\', \'' + row.name + '\', \'' + row.ipaddress + '\', \'' + row.circle + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						+ '<button type="button" onclick="doDelete(\'' + row.id + '\', \'' + row.name + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}

function closeModal() {
	$('#form-bmsc-name').val('');
	$('#form-bmsc-ipaddress').val('');
	$('#form-bmsc-circle').val('');
	$('#form-modal').modal('hide');
}