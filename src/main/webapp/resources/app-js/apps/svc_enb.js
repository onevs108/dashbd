$(document).ready(function()
{
	getServiceAreaBmSc(1, $('#operator option:selected').val(), true);
    $('#operator').change(function() {
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val(), true);
    });
    
    $('#bmsc').change(function() {
    	getEnbList($('#operator option:selected').val(), $('#bmsc option:selected').val());
    });

    $('#enb-excel-mgmt-btn').click(moveToExcelMgmt);
});

//BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId, isUpload)
{	
	$.ajax({
        url : "/dashbd/api/serviceAreaBmScByOperator.do",
        type: "get",
        data : { "page" : page, "operatorId" : operatorId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value=""></option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }
            
            if( isUpload ) {
            	$("#bmsc").empty();
                $("#bmsc").append(options);
                
                $("#form-bmsc-id").empty();
                $("#form-bmsc-id").append(options);
            }
        }
    });
}

function moveToExcelMgmt() {
	location.href = "/dashbd/resources/eNBsExcelMgmt.do";
}


function isEmpty(value) {
    return (value === undefined || value == null || value.length <= 0) ? true : false;
}


$(function() {
	// button click event in list
	$('#modal-open-btn').click(openModal);
	$('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
	
	$('#search-operator-id').change(changeOperator);
});

function changeOperator() {
	$('#table').bootstrapTable('destroy');
	getBmscList();
}

function openModal() {
	$('#form-operator-id').val($('#operator').val());
	$('#form-bmsc-id').val($('#bmsc').val());
	$('#modal-title').html('Create New eNB');
	$('#form-modal').modal('show');
}

var bmscId = null;
function doAdd() {
	var enbId = $('#form-enb-id').val();
	var enbName = $('#form-enb-name').val();
	var enbIpaddress = $('#form-enb-ipaddress').val();
	
	if (enbId == null || enbId.length == 0) {
		alert('Please input the ID');
		return false;
	}
	
	if (enbName == null || enbName.length == 0) {
		alert('Please input the Name');
		return false;
	}
	
	var data = {
		id: bmscId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
		operatorId: $('#form-operator-id').val(),
		id: enbId,
		name: enbName,
		longitude: $('#form-enb-longitude').val(),
		latitude: $('#form-enb-latitude').val(),
		plmn: $('#form-enb-plmn').val(),
		circle: $('#form-enb-circle').val(),
		circleName: $('#form-enb-circle-name').val(),
		clusterId: $('#form-enb-cluster-id').val(),
		ipaddress: $('#form-enb-ipaddress').val(),
		earfcn: $('#form-enb-earfcn').val(),
		mbsfn: $('#form-enb-mbsfn').val(),
		mbmsServiceAreaId: $('#form-enb-mbms-service-area-id').val(),
		city: $('#form-enb-city').val(),
		bandwidth: $('#form-enb-bandwidth').val(),
		operator: $('#form-operator-id').val(),
		bmsc: $('#form-bmsc-id').val()
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
	$('#modal-title').html('Edit BMSC');
	$('#form-bmsc-name').val(name);
	$('#form-bmsc-ipaddress').val(ipaddress);
	$('#form-bmsc-circle').val(circle);
	$('#form-modal').modal('show');
}

function doDelete(bmscId, name) {
	if (confirm('Do you really want to delete the BMSC "' + name + '"?')) {
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
		url: '/dashbd/api/createENB.do',
		method: 'POST',
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		dateType : 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
				closeModal();
				alert('Success!! Please reload the page!');
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown + textStatus);
			return false;
		}
	});
}

function getEnbList(operatorId, bmscId) {
	// 테이블 생성
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/getEnbsList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['operatorId'] = operatorId;
			params['bmscId'] = bmscId;
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
			field: 'bmscId',
			title: 'BMSC ID',
			visible: false
		}, {
			field: 'id',
			title: 'eNB Id',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'name',
			title: 'eNB Name',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'longitude',
			title: 'Longitude',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'latitude',
			title: 'Latitude',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'plmn',
			title: 'plmn',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'city',
			title: 'City',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'bandwidth',
			title: 'Bandwidth',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'command',
			title: 'Command',
			width: '15%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				//var html = '<button type="button" onclick="openEditModal(\'' + row.id + '\', \'' + row.name + '\', \'' + row.ipaddress + '\', \'' + row.circle + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
						//+ '<button type="button" onclick="doDelete(\'' + row.id + '\', \'' + row.name + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				var html = ''
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
