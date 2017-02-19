
$(function() {
	// button click event in list
	$('#go-search').click(doSearch);
});

function doSearch() {
	$('#table').bootstrapTable('destroy');
	getUserList(false, true);
}

function doInfo(userId) {
	userFormAccess('info', userId);
}

function doEdit(userId) {
	userFormAccess('edit', userId);
}

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
		
		$('#search-operator-id').val(searchOperatorId);
		$('#search-column').val(searchColumn);
		$('#search-keyword').val(searchKeyword);
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
			field: 'gradeName',
			title: 'Group',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: true
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

//그룹 콤보박스 변경시 서클 콤보박스 컨트롤 메소드
function changeGroup(obj, targetId) {
	//서클그룹 선택시
	if(obj.value == '9999') {
		$("#" + targetId).show();
		$("#modal_circle_group").show();
	} else {
		$("#" + targetId).hide();
		$("#modal_circle_group").hide();
	}
}

function getTownListFromCircle(circleObj) {
	$.ajax({
		url: '/dashbd/api/user/getTownFromCircle.do',
		method: 'POST',
		dataType: 'json',
		data: {
			circleName: $(circleObj).find("option:selected").text()
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result) {
				var html = "<option value=''>Circle Group</option>";
				for (var i = 0; i < data.result.length; i++) {
					html += "<option value='"+data.result[i].id+"'>"+data.result[i].town_name+"</option>";
				}
				$("#form-circle").html(html);
			}
			else {
				alert("Fail! Please");
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			checkUserId = false;
			return false;
		}
	});	
}

//버튼 클릭에 따른 유저 팝업 컨트롤 메소드
function userFormAccess(accessDiv, userId) {
	if(accessDiv == 'add') {
		$(".modal-title").text("Add Operator");
		$("#modal-grade-id").val('');
		$("#modal_circle_area").hide();
		$("#modal_circle_group").hide();
		$("#addBtn").show();
		$("#editBtn").hide();
		$("#myModal input, #myModal select, #myModal textarea").val('');
		$("#myModal input, #myModal select, #myModal textarea, #myModal #form-check-btn").prop("disabled", false);
	} else if(accessDiv == 'edit') {
		$(".modal-title").text("Edit Operator");
		$("#editBtn").show();
		$("#addBtn").hide();
		getUserInfo(userId);
		$("#myModal #form-user-id").prop("readonly", true);
		$("#myModal #form-check-btn").prop("disalbed", true);
		$("#myModal input, #myModal select, #myModal textarea").prop("disabled", false);
	} else if(accessDiv == 'info') {
		$(".modal-title").text("Operator" + " \"" + userId + "\" Info");
		$("#editBtn").hide();
		$("#addBtn").hide();
		getUserInfo(userId);
		$("#myModal input, #myModal select, #myModal textarea, #myModal #form-check-btn").prop("disabled", true);
	}
}

function doCheckId() {
	var userId = $('#form-user-id').val();
	if (userId == null || userId.length == 0) {
		swal("Fail !","Please enter your ID", "warning");
		return false;
	}
	
	$.ajax({
		url: '/dashbd/api/user/check.do',
		method: 'POST',
		dataType: 'json',
		data: {
			userId: userId
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				checkUserId = true;
				swal("Success !","Avaliable!", "success");
			}
			else { // 실패
				checkUserId = false;
				swal("Fail !", "Already exist!", "warning");
				$('#form-user-id').focus();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			swal("Fail !", errorThrown, "warning");
			checkUserId = false;
			return false;
		}
	});
}

function getUserInfo(userId) {
	$.ajax({
		url: '/dashbd/api/user/info.do',
		method: 'POST',
		dataType: 'json',
		data: {
			userId: userId
		},
		success: function(data, textStatus, jqXHR) {
			setElements(data.user);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			swal("Fail !", errorThrown, "warning");
			return false;
		}
	});
}

function setElements(user) {
	if(user.grade == '9999'){$("#circleArea").show();}
	for(var i=0; i < $('#modal-circle-id option').length; i++) {
		var tempOpt = $($('#modal-circle-id option')[i]);
		if(tempOpt.text() == user.circleName) {
			tempOpt.prop("selected", true);
		}
	}
	getTownListFromCircle($('#modal-circle-id')[0]);
	setTimeout( function() {
		$('#form-circle').val(user.operatorId);
	}, 200);
	
	if(user.operatorId != '') {
		$('#modal_circle_area').show();
		$('#modal_circle_group').show();
//		$('#modal-circle-id').val(user.operatorId);
	} else {
		$('#modal_circle_area').hide();
		$('#modal_circle_group').hide();
	} 
		
	$('#form-user-id').val(user.userId);
	$('#form-password').val(user.password);
	$('#form-confirm-password').val(user.password);
	$('#form-first-name').val(user.firstName);
	$('#form-last-name').val(user.lastName);
	$('#form-department').val(user.department);
//	$('#form-registered-date').val(user.createdAt);
//	$('#form-modified-date').val(user.updatedAt);
	$('#modal-grade-id').val(user.grade);
	$('#form-memo').val(user.memo);
	
//	if(user.grade == 13){
//		$('#checkbox-permission-user').attr("checked", true);
//		$('#checkbox-permission-permission').attr("checked", true);
//		$('#checkbox-permission-contents').attr("checked", true);
//		$('#checkbox-permission-operator').attr("checked", true);
//		$('#checkbox-permission-bmsc').attr("checked", true);
//		$('#checkbox-permission-service-area').attr("checked", true);
//		$('#checkbox-permission-enb').attr("checked", true);
//		$('#checkbox-permission-schedule').attr("checked", true);
//	}else{
//			for (var inx = 0; inx < user.permissions.length; inx++) {
//				switch (user.permissions[inx].id) {
//				case PERMISSION_ID_USER:
//					$('#checkbox-permission-user').attr("checked", true);
//					break;
//				case PERMISSION_ID_PERMISSION:
//					$('#checkbox-permission-permission').attr("checked", true);
//					break;
//				case PERMISSION_ID_CONTENTS:
//					$('#checkbox-permission-contents').attr("checked", true);
//					break;
//				case PERMISSION_ID_OPERATOR:
//					$('#checkbox-permission-operator').attr("checked", true);
//					break;
//				case PERMISSION_ID_BMSC:
//					$('#checkbox-permission-bmsc').attr("checked", true);
//					break;
//				case PERMISSION_ID_SERVICE_AREA:
//					$('#checkbox-permission-service-area').attr("checked", true);
//					break;
//				case PERMISSION_ID_ENB:
//					$('#checkbox-permission-enb').attr("checked", true);
//					break;
//				case PERMISSION_ID_SCHEDULE:
//					$('#checkbox-permission-schedule').attr("checked", true);
//					break;
//				case PERMISSION_ID_SYSTEM:
//					$('#checkbox-permission-system').attr("checked", true);
//					break;
//				case PERMISSION_ID_STATICTIS:
//					$('#checkbox-permission-statictis').attr("checked", true);
//					break;
//				default:
//					break;
//				}
//			}
//	}
	
	$("#myModal").modal('show');
}

function doInsert(accessDiv) {
	swal({
		  title: "Are you sure?",
		  text: 'Do you really want to ' + accessDiv + ' the user?',
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Yes",
		  closeOnConfirm: false
		},
	function(){
		var operatorId = $('#form-circle').val();
		var userId = $('#form-user-id').val();
		var password = $('#form-password').val();
		var confirmPassword = $('#form-confirm-password').val();
		var firstName = $('#form-first-name').val();
		var lastName = $('#form-last-name').val();
		var department = $('#form-department').val();
		var grade = $('#modal-grade-id').val();
		var memo = $('#form-memo').val();
		
		if (grade == null || grade.length == 0) {
			swal("Fail !","Please select your Group", "warning");
			return false;
		}
		
		if (userId == null || userId.length == 0) {
			swal("Fail !","Please enter your ID", "warning");
			$('#form-user-id').focus();
			return false;
		}
		
		if (accessDiv == 'add' && !checkUserId) {
			swal("Fail !","Please check the ID", "warning");
			$('#check-id-btn').focus();
			return false;
		}
		
		if (password == null || password.length == 0) {
			swal("Fail !","Please enter the password", "warning");
			$('#form-password').focus();
			return false;
		}
		else {
			if (password != confirmPassword) {
				swal("Fail !","Please check the password", "warning");
				$('#form-confirm-password').focus();
				return false;
			}
		}
		
		if (firstName == null || firstName.length == 0) {
			swal("Fail !","Please enter the FirstName", "warning");
			$('#form-first-name').focus();
			return false;
		}
		
		if (lastName == null || lastName.length == 0) {
			swal("Fail !","Please enter the LastName", "warning");
			$('#form-last-name').focus();
			return false;
		}
		
		if (department == null || department.length == 0) {
			swal("Fail !","Please enter the Department", "warning");
			$('#form-department').focus();
			return false;
		}
		
		
		$.ajax({
			url: '/dashbd/api/user/insert.do',
			method: 'POST',
			dataType: 'json',
			data: {
				operatorId: operatorId,
				userId: userId,
				password: password,
				firstName: firstName,
				lastName: lastName,
				department: department,
				grade: grade,
				memo: memo
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) { // 성공
					swal("Success !", "Success !", "success");
					$('#table').bootstrapTable('destroy');
					getUserList(true, false);
					$("#myModal").modal('hide');
				}
				else { // 실패
					swal("Fail !", "Failed!! Please you report to admin!", "warning");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				swal("Fail !", errorThrown + textStatus, "warning");
				checkUserId = false;
				return false;
			}
		});
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
				checkUserId = false;
				return false;
			}
		});
	});
}