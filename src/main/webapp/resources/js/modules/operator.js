var operatorId = null;
var checkOperatorName = false;

$(function() {
	// button click event in list
//	$('#modal-open-btn').click(openModal);
//	$('#modal-open-btn2').click(openModal2);
//	$('#modal-add-btn').click(doAdd);
//	$('#modal-add-btn2').click(doAdd2);
//	$('#modal-cancel-btn').click(doModalCancel);
//	$('#modal-cancel-btn2').click(doModalCancel2);
//	$('#modal-cancel-icon-btn').click(doModalCancel);
//	$('#modal-cancel-icon-btn2').click(doModalCancel2);	
//	$('#check-name-btn').click(doCheckName);
//	$('#check-name-btn2').click(doCheckName2);
	// name 입력 or 변경 checkOperatorName 초기화
//	$('#form-operator-name').keypress(function() {
//		checkOperatorName = false;
//		$('#form-operator-name-input-area').attr('class', 'input-group has-warning');
//	});
//	$('#form-operator-name2').keypress(function() {
//		checkOperatorName = false;
//		$('#form-operator-name-input-area2').attr('class', 'input-group has-warning');
//	});
});

//function openModal() {
//	checkOperatorName = false;
//	$('#modal-title').html('Create New National');
//	$('#form-modal').modal('show');
//}

//function openModal2() {
//	checkOperatorName = false;
//	$('#modal-title2').html('Create New Regional');
//	$('#form-modal2').modal('show');
//}

//function doAdd() {
//	$('#modal-title').html('Create New National');
//	
//	var operatorName = $('#form-operator-name').val();
//	var operatorDescription = $('#form-operator-description').val();
//	var permission = "";
//	var checkbox = $("input[name='permission']");
//	for(var i=0; i < checkbox.length; i++){
//		if($(checkbox[i]).is(":checked")){
//			permission += checkbox[i].value + ",";
//		}
//	}
//	
//	if (operatorName == null || operatorName.length == 0) {
//		alert('Please input the Group Name');
//		return false;
//	}
//	
//	if (!checkOperatorName) {
//		alert('Please check the Group Name');
//		$('#check-name-btn').focus();
//		return false;
//	}
//	
//	if (operatorDescription == null || operatorDescription.length == 0) {
//		alert('Please input the Description');
//		return false;
//	}
//	
//	var data = {
//		id: operatorId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
//		name: operatorName,
//		description: operatorDescription,
//		permission: permission
//	}
//	callInsert(data);
//}

//function doAdd2() {
//	$('#modal-title2').html('Create New Regional');
//	
//	var circleName = $('#circleSelect').val();
//	var operatorName = $('#form-operator-name2').val();
//	var operatorDescription = $('#form-operator-description2').val();
//	
//	var permission = "";
//	var checkbox = $("input[name='permission2']");
//	for(var i=0; i < checkbox.length; i++){
//		if($(checkbox[i]).is(":checked")){
//			permission += checkbox[i].value + ",";
//		}
//	}
//	
//	if (operatorName == null || operatorName.length == 0) {
//		alert('Please input the Operator Name');
//		return false;
//	}
//	
//	if (!checkOperatorName) {
//		alert('Please check the Operator Name');
//		$('#check-name-btn2').focus();
//		return false;
//	}
//	
//	if (operatorDescription == null || operatorDescription.length == 0) {
//		alert('Please input the Description');
//		return false;
//	}
//	
//	var data = {
//			id: operatorId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
//			circleName: circleName,
//			name: operatorName,
//			description: operatorDescription,
//			permission: permission
//	}
//	callInsert2(data);
//}

//function doModalCancel() {
//	operatorId = null;
//	operatorCheckName = false;
//	closeModal();
//}

//function doModalCancel2() {
//	operatorId = null;
//	operatorCheckName = false;
//	closeModal2();
//}

$("#circleSelect").on("change", function(e){
	getOperatorList2(this.value);
});

function doEdit(groupName) {
	callGruopModal('National', 'edit', groupName);
//	$('#modal-title').html('Edit National Wise Group');
//	checkOperatorName = true; // 수정 창을 처음 열었을땐 체크 된 상태이다. 							
//	operatorId = id;
//	$('#form-operator-name').val(name);
//	$('#form-operator-description').val(description);
//	var checkbox = $("input[name='permission']");
//	var permissionArray = permission.split(",");
//	for(var i=0; i < checkbox.length; i++){
//		if(permissionArray.indexOf(checkbox[i].value) > -1){
//			$(checkbox[i]).prop("checked", true);
//		}
//	}
	$('#form-modal').modal('show');
}

function doEdit2(groupName) {
	callGruopModal('Regional', 'edit', groupName);
//	$('#modal-title2').html('Edit Regional Group');
//	checkOperatorName = true; // 수정 창을 처음 열었을땐 체크 된 상태이다. 							
//	operatorId = id;
//	$('#form-circle-name2').val(circleName);
//	$('#form-operator-name2').val(name);
//	$('#form-operator-description2').val(description);
//	var checkbox = $("input[name='permission2']");
//	var permissionArray = permission.split(",");
//	for(var i=0; i < checkbox.length; i++){
//		if(permissionArray.indexOf(checkbox[i].value) > -1){
//			$(checkbox[i]).prop("checked", true);
//		}
//	}
	$('#form-modal').modal('show');
}

function doDelete(gradeId, name) {
	swal({
		  title: "Are you sure?",
		  text: 'Do you really want to delete the Grade "' + name + '"?',
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Yes",
		  closeOnConfirm: false
		},
	function() {
		$.ajax({
			url: '/dashbd/api/grade/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				gradeId: gradeId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					swal({title:"Success !", text:"Success", type:"success"}, function() {
						getOperatorList();
					});
				}
				else { // 실패
					swal("Fail !","Please contact system administrator", "warning");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !","Please contact system administrator", "warning");
				return false;
			}
		});
	});
}

function doDelete2(operatorId, name) {
	swal({
		  title: "Are you sure?",
		  text: 'Do you really want to delete the Circle "' + name + '"?',
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Yes",
		  closeOnConfirm: false
		},
	function() {
		$.ajax({
			url: '/dashbd/api/circle/delete.do',
			method: 'POST',
			dataType: 'json',
			data: {
				circleId: operatorId
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					swal({title:"Success !", text:"Success", type:"success"}, function() {
						getOperatorList2($("#circleSelect").val());
					});
				}
				else { // 실패
					swal("Fail !","Please contact system administrator", "warning");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !","Please contact system administrator", "warning");
				return false;
			}
		});
	});
}

//function doCheckName() {
//	var operatorName = $('#form-operator-name').val();
//	if (operatorName == null || operatorName.length == 0) {
//		alert('Please input Operator Name');
//		return false;
//	}
//	
//	$.ajax({
//		url: '/dashbd/api/grade/check.do',
//		method: 'POST',
//		dataType: 'json',
//		data: {
//			operatorName: operatorName
//		},
//		success: function(data, textStatus, jqXHR) {
//			if (data.result) { // 성공
//				checkOperatorName = true;
//				$('#form-operator-name-input-area').attr('class', 'input-group has-success');
//				alert('Avaliable!');
//			}
//			else { // 실패
//				checkOperatorName = false;
//				$('#form-operator-name-input-area').attr('class', 'input-group has-error');
//				alert('Already exist!');
//				$('#form-operator-name').focus();
//			}
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//			alert(errorThrown);
//			checkUserId = false;
//			return false;
//		}
//	});
//}

//function doCheckName2() {
//	var operatorName = $('#form-operator-name2').val();
//	if (operatorName == null || operatorName.length == 0) {
//		alert('Please input Group Name');
//		return false;
//	}
//	
//	$.ajax({
//		url: '/dashbd/api/operator/check.do',
//		method: 'POST',
//		dataType: 'json',
//		data: {
//			operatorName: operatorName
//		},
//		success: function(data, textStatus, jqXHR) {
//			if (data.result) { // 성공
//				checkOperatorName = true;
//				$('#form-operator-name-input-area2').attr('class', 'input-group has-success');
//				alert('Avaliable!');
//			}
//			else { // 실패
//				checkOperatorName = false;
//				$('#form-operator-name-input-area2').attr('class', 'input-group has-error');
//				alert('Already exist!');
//				$('#form-operator-name2').focus();
//			}
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//			alert(errorThrown);
//			checkUserId = false;
//			return false;
//		}
//	});
//}

//function callInsert(data) {
//	$.ajax({
//		url: '/dashbd/api/grade/insert.do',
//		method: 'POST',
//		dataType: 'json',
//		data: data,
//		success: function(data, textStatus, jqXHR) {
//			if (data.result) { // 성공
//				getOperatorList();
//				closeModal();
//				operatorId = null; // 초기화
//			}
//			else { // 실패
//				alert('Failed!! Please you report to admin!');
//			}
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//			alert(errorThrown + textStatus);
//			return false;
//		}
//	});
//}

//function callInsert2(data) {
//	var circleName = data.circleName;
//	$.ajax({
//		url: '/dashbd/api/operator/insert.do',
//		method: 'POST',
//		dataType: 'json',
//		data: data,
//		success: function(data, textStatus, jqXHR) {
//			if (data.result) { // 성공
//				getOperatorList2(circleName);
//				closeModal2();
//				operatorId = null; // 초기화
//			}
//			else { // 실패
//				alert('Failed!! Please you report to admin!');
//			}
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//			alert(errorThrown + textStatus);
//			return false;
//		}
//	});
//}

function getOperatorList() {
	$('#table').bootstrapTable('destroy');
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
		pageSize: 5,
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
			title: 'Group Name',
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
			align: 'center',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				if(row.id > 3 && row.id != 9999){
					var html = '<button type="button" onclick="doEdit(\'' + row.name + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
					+ '<button type="button" onclick="doDelete(\'' + row.id + '\', \'' + row.name + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				} else {
					var html = '<button type="button" class="btn btn-primary btn-xs" onclick="callMemberListModal(\'' + row.id + '\')">View Member</button>';
				}
				return html;
			}
		}]
	});
}

function getOperatorList2(circleName) {
	$('#table2').bootstrapTable('destroy');
	var pageNumber = 1;
	var table = $('#table2').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/circle/selectCircle.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
			params['circleName'] = circleName;
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
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
			field: 'town_name',
			title: 'Area Group Name',
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
			align: 'center',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var html = '<button type="button" onclick="doEdit2(\'' + row.town_name + '\')" class="btn btn-success btn-xs button-edit">Edit</button> '
				+ '<button type="button" onclick="doDelete2(\'' + row.id + '\', \'' + row.town_name + '\')" class="btn btn-danger btn-xs btn-delete-action button-delete">Delete</button>';
				return html;
			}
		}]
	});
}

var tempGroupId;
function getOperatorList3(groupId, tabDivId) {
	$('#' + tabDivId).bootstrapTable('destroy');
	// 테이블 생성
	var pageNumber = 1;
	var table = $('#' + tabDivId).bootstrapTable({
		method: 'post',
		url: '/dashbd/api/grade/getMemberList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			location.href = '#';
			pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
			params.groupId = groupId; //그룹아이디 부여
			params.tabDivId = tabDivId; //테이블 구분 값 넘겨줌
			
			//search div
			params.searchGrade = $("#searchGrade").val();
			params.searchDiv = $("#searchDiv").val(); 
			params.searchInput = $("#searchInput").val();
			
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: pageNumber,
		pageSize: 5,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 3,
		clickToSelect: false,
		columns: [{
			field: 'circleName',
			title: 'Circle',
			width: '15%',
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
			field: 'id',
			title: 'Operator ID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			visible: false
		}, {
			field: 'townName',
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
			field: 'userId',
			title: 'ID',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			class:'userIdCol'
		}, {
			field: 'lastName',
			title: 'Last Name',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'firstName',
			title: 'First Name',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'department',
			title: 'Department',
			width: '10%',
			align: 'left',
			valign: 'middle',
			sortable: true
		}, {
			field: 'command',
			title: 'Command',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var html;
				if(tabDivId == "table3") {
					html = '<button type="button" class="btn btn-danger btn-xs" onclick="deleteMmeber(\'memberListModal\', this)">Remove from Group</button>';
				} else {
					html = '<div class="icheckbox_square-green" style="position: relative;" onclick="checkboxClick(this)">';
					html += '<ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div>'
				}
				
				return html;
			}
		}]
	});
	
	if(tabDivId == 'table3') {
		tempGroupId=groupId;
		$('#table3').on('load-success.bs.table', function (e, data){
			getOperatorList3(groupId, 'table4');
	    });
	} else if(tabDivId == 'table4') {
		$('#table4').on('load-success.bs.table', function (e, data){
			$("#memberListModal").modal("show");
	    });
	}
}

function searchOtherOperator(formId) {
	if(formId == 'memberListModal') {
		getOperatorList3(tempGroupId, 'table4');
	} else if(formId == 'addGroupModal') {
		var searchDiv = $("#addGroupModal select#searchDiv").val();
		var searchInput = $("#addGroupModal input#searchInput").val();
		
		$.ajax({
			url: '/dashbd/api/operator/getOtherUserList.do',
			method: 'POST',
			data: {
				groupId : $("#groupId").val(),
				searchDiv : searchDiv,
				searchInput : searchInput
			},
			success: function(data, textStatus, jqXHR) {
				$("#addGroupModal ul.addMemberArea").empty();
				
				var resultList = data.rows;
				
				var resultHtml;
				for(var i=0; i < resultList.length; i++) {
					var tempObj = resultList[i];
					resultHtml = '<li class="list-group-item">';
					resultHtml += '<div class="i-select"><label> <input type="checkbox" value="' + tempObj.id + '">';
					resultHtml += '<span> ' + tempObj.userId + ' (' + tempObj.lastName + ' ' + tempObj.firstName + ')' + '</span></label></div></li>';
				}
				
				$("#addGroupModal ul.addMemberArea").append(resultHtml);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !","Please contact system administrator", "warning");
				return false;
			}
		});
		
		
	}
}

//custom checkbox click event
function checkboxClick(obj) {
	if($(obj).hasClass("checked")) {
		$(obj).removeClass("checked");
	} else {
		$(obj).addClass("checked");
	}
	
}

//function closeModal() {
//	$('#form-operator-name').val('');
//	$('#form-operator-description').val('');
//	$("input[name='permission']").prop("checked", false);
//	$('#form-modal').modal('hide');
//}

//function closeModal2() {
//	$('#form-operator-name2').val('');
//	$('#form-operator-description2').val('');
//	$("input[name='permission2']").prop("checked", false);
//	$('#form-modal2').modal('hide');
//}



function callGruopModal(groupDiv, accessDiv, groupName) {
	var ajaxYn = true;
	if(groupDiv == 'Regional') {
		if($("#circleSelect").val() == '')
			ajaxYn = false;
	}
	
	if(ajaxYn) {
		$.ajax({
			url: '/dashbd/api/operator/callAddGruopModal.do',
			method: 'POST',
			data: {
				groupDiv : groupDiv,
				accessDiv : accessDiv,
				groupName : groupName
			},
			success: function(data, textStatus, jqXHR) {
				$("#groupArea").empty();
				$("#groupArea").html(data);
				
				if(accessDiv == 'add') {
					$(".modal-header .modal-title").text("Add " + groupDiv + " Group");
				} else if(accessDiv == 'edit') {
					$(".modal-header .modal-title").text("Edit " + groupDiv + " Group");
				}
				
				$("#addGroupModal").modal("show");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !","Please contact system administrator", "warning");
				return false;
			}
		});
	} else {
		swal("Fail !","Please select the circle", "warning");
	}
}

function callMemberListModal(groupId) {
	$.ajax({
		url: '/dashbd/api/operator/callMemberListModal.do',
		method: 'POST',
		data: {groupId : groupId},
		success: function(data, textStatus, jqXHR) {
			$("#memberListArea").empty();
			$("#memberListArea").html(data);
			
			getOperatorList3(groupId, 'table3');
		},
		error: function(jqXHR, textStatus, errorThrown) {
			swal("Fail !","Please contact system administrator", "warning");
			return false;
		}
	});
}

function addMmeber(formId, obj) {
	if(formId == 'addGroupModal') {
		var checkList = $("#" + formId + " ul.addMemberArea li");
		
		for(var i=0; i < checkList.length; i++) {
			var tempObj = $(checkList[i]);
			
			if(tempObj.find("input[type='checkbox']").prop("checked")) {
//				tempObj.find("input[type='checkbox']").prop("checked", false);
				$("#" + formId + " ul.initMemberArea").append(tempObj);
			}
		}
	} else {
		swal({
			  title: "Are you sure?",
			  text: "Do you want to proceed with this operation?",
			  type: "warning",
			  showCancelButton: true,
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "Yes",
			  closeOnConfirm: false
			},
		function() {
			var memberListStr='';
			
			var checkedList = $("#memberListModal .icheckbox_square-green.checked");
			for(var i=0; i < checkedList.length; i++) {
				var tempObj = $(checkedList[i]);
				
				memberListStr += ',' + $(tempObj.parents("tr")[0]).find("td.userIdCol").text();
			}
			
			if(memberListStr != '') {
				$.ajax({
					url: '/dashbd/api/operator/proccessNationalGroup.do',
					method: 'POST',
					data: {
						proccessDiv : 'add',
						memberListStr : memberListStr.substring(1),
						groupId : tempGroupId
					},
					success: function(data, textStatus, jqXHR) {
						if(data.resultCode == 'S') {
							swal({title:"Success !", text:"Success", type:"success"}, function() {
								getOperatorList3(tempGroupId, 'table3');
							})
						} else {
							swal("Fail !","Please contact system administrator", "warning");
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						swal("Fail !","Please contact system administrator", "warning");
						return false;
					}
				});
			} else {
				swal("Fail !","Please select members", "warning");
			}
		});
	}
}

function deleteMmeber(formId, obj) {
	if(formId == 'addGroupModal') {
		var checkList = $("#" + formId + " ul.initMemberArea li");
		
		for(var i=0; i < checkList.length; i++) {
			var tempObj = $(checkList[i]);
			
			if(tempObj.find("input[type='checkbox']").prop("checked")) {
//				tempObj.find("input[type='checkbox']").prop("checked", false);
				$("#" + formId + " ul.addMemberArea").append(tempObj);
			}
		}
	} else {
		var tempUserId = $($(obj).parents("tr")[0]).find("td.userIdCol").text();
		
		swal({
			  title: "Are you sure?",
			  text: "Do you want to proceed with this operation?",
			  type: "warning",
			  showCancelButton: true,
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "Yes",
			  closeOnConfirm: false
			},
		function() {
			$.ajax({
				url: '/dashbd/api/operator/proccessNationalGroup.do',
				method: 'POST',
				data: {
					proccessDiv : 'delete',
					memberListStr : tempUserId,
					groupId : tempGroupId
				},
				success: function(data, textStatus, jqXHR) {
					if(data.resultCode == 'S') {
						swal({title:"Success !", text:"Success", type:"success"}, function() {
							getOperatorList3(tempGroupId, 'table3');
						})
					} else {
						swal("Fail !","Please contact system administrator", "warning");
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					swal("Fail !","Please contact system administrator", "warning");
					return false;
				}
			});
		});
	}
}

/**
 * accessDiv : nation, region
 * proccessDiv : add, edit
 * formId
 * @param accessDiv
 * @param proccessDiv
 * @param formId
 * @returns
 */
function proccessGroup(accessDiv, proccessDiv, formId) {
	swal({
		  title: "Are you sure?",
		  text: "Do you want to proceed with this operation?",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Yes",
		  closeOnConfirm: false
		},
	function() {
		var groupId = $("#groupId").val();
		var menuListStr = '';
		var groupName = '';
		var gruopDescription = '';
		var memberListStr = '';
		
		if(formId != 'memberListModal') { 
			groupName = $("#addGroupModal [name='groupName']").val();
			gruopDescription = $("#addGroupModal [name='groupDescription']").val();
			
			var menuList = $(".menuArea li input[type='checkbox']");
			for(var i=0; i < menuList.length; i++) {
				var tempCheckObj = $(menuList[i]);
				
				if(tempCheckObj.prop("checked") == true) 
					menuListStr += ',' + tempCheckObj.val(); 
			}
			
			if(menuListStr != '')
				menuListStr = menuListStr.substring(1);
			
			var memberList = $("#addGroupModal ul.initMemberArea li input[type='checkbox']");
			for(var i=0; i < memberList.length; i++) {
				var tempMemberObj = $(memberList[i]);
//				if(tempMemberObj.prop("checked") == true)
				memberListStr += ',' + tempMemberObj.val();
			}
			
			if(memberListStr != '')
				memberListStr = memberListStr.substring(1);
		}
		
		if(groupName == '') {
			swal("Fail !","Please enter the Group Name", "warning");
			return;
		}
		
		if(menuListStr == '') {
			swal("Fail !","Please check the Menu", "warning");
			return;
		}
		
		$.ajax({
			url: '/dashbd/api/operator/proccessGroup.do',
			method: 'POST',
			data: {
				accessDiv : accessDiv,
				proccessDiv : proccessDiv,
				groupId : groupId,
				menuListStr : menuListStr,
				groupName : groupName,
				gruopDescription : gruopDescription,
				memberListStr : memberListStr,
				circleName : $("#circleSelect").val()
			},
			success: function(data, textStatus, jqXHR) {
				if(data.resultCode == 'S') {
					swal({title:"Success !", text:"Success", type:"success"}, function() {
						if(accessDiv == 'national') {
							getOperatorList();
							$("#addGroupModal").modal("hide");
							$("#memberListModal").modal("hide");
						} else if(accessDiv == 'regional') {
							getOperatorList2($("#circleSelect").val());
							$("#addGroupModal").modal("hide");
						}
					})
				} 
				else if(data.resultCode == 'E') {
					swal("Fail !","Data already exists.", "warning");
				}
				else {
					swal("Fail !","Please contact system administrator", "warning");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !","Please contact system administrator", "warning");
				return false;
			}
		});
	});
}