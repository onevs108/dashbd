
$(function() {
	// button click event in list
	$('#check-id-btn').click(doCheckId);
	$('#save-btn').click(doInsert);
	$('#cancel-btn').click(goList);
	$('#list-btn').click(goList);
	
	// id 입력 or 변경 checkUserId 초기화
	$('#form-user-id').keypress(function() {
		checkUserId = false;
		$('#form-user-id-input-area').attr('class', 'input-group has-warning');
	});
});

var checkUserId = false;
function initForm(flag, userId) {
	this.flag = flag;
	if (flag == 'add') {
		checkUserId = false;
		$('#form-registered-date-area').hide();
		$('#form-modified-date-area').hide();
		$('#form-permission-area').hide();
		
		$('#save-btn').show();
		$('#cancel-btn').show();
		$('#list-btn').hide();
	}
	else if (flag == 'info') {
		getUserInfo(userId);
		$('#form-operator-id').attr("disabled", "disabled");
		$('#form-user-id').attr("disabled", "disabled");
		$('#check-id-btn').attr("disabled", "disabled");
		$('#form-password-area').hide();
		$('#form-confirm-password-area').hide();
		$('#form-first-name').attr("disabled", "disabled");
		$('#form-last-name').attr("disabled", "disabled");
		$('#form-department').attr("disabled", "disabled");
		$('#form-registered-date').attr("disabled", "disabled");
		$('#form-modified-date').attr("disabled", "disabled");
		$('#form-grade').attr("disabled", "disabled");
		
		$('#save-btn').hide();
		$('#cancel-btn').hide();
		$('#list-btn').show();
	}
	else if (flag == 'edit') {
		getUserInfo(userId);
		checkUserId = true; // 수정 시 ID는 변경할 수 없으므로 true로 설정
		$('#form-user-id').attr("disabled", "disabled");
		$('#check-id-btn').attr("disabled", "disabled");
		$('#form-registered-date-area').hide();
		$('#form-modified-date-area').hide();
		$('#form-permission-area').hide();
		
		$('#save-btn').show();
		$('#cancel-btn').show();
		$('#list-btn').hide();
	}
}

function doCheckId() {
	var userId = $('#form-user-id').val();
	if (userId == null || userId.length == 0) {
		alert('Please input your ID');
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
				$('#form-user-id-input-area').attr('class', 'input-group has-success');
				alert('Avaliable!');
			}
			else { // 실패
				checkUserId = false;
				$('#form-user-id-input-area').attr('class', 'input-group has-error');
				alert('Already exist!');
				$('#form-user-id').focus();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
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
			alert(errorThrown);
			return false;
		}
	});
}

function setElements(user) {
	$('#form-operator-id').val(user.operatorId);
	$('#form-user-id').val(user.userId);
	$('#form-password').val(user.password);
	$('#form-confirm-password').val(user.password);
	$('#form-first-name').val(user.firstName);
	$('#form-last-name').val(user.lastName);
	$('#form-department').val(user.department);
	$('#form-registered-date').val(user.createdAt);
	$('#form-modified-date').val(user.updatedAt);
	$('#form-grade').val(user.grade);
	
	for (var inx = 0; inx < user.permissions.length; inx++) {
		switch (user.permissions[inx].id) {
		case PERMISSION_ID_USER:
			$('#checkbox-permission-user').attr("checked", true);
			break;
		case PERMISSION_ID_PERMISSION:
			$('#checkbox-permission-permission').attr("checked", true);
			break;
		case PERMISSION_ID_CONTENTS:
			$('#checkbox-permission-contents').attr("checked", true);
			break;
		case PERMISSION_ID_OPERATOR:
			$('#checkbox-permission-operator').attr("checked", true);
			break;
		case PERMISSION_ID_BMSC:
			$('#checkbox-permission-bmsc').attr("checked", true);
			break;
		case PERMISSION_ID_SERVICE_AREA:
			$('#checkbox-permission-service-area').attr("checked", true);
			break;
		case PERMISSION_ID_ENB:
			$('#checkbox-permission-enb').attr("checked", true);
			break;
		case PERMISSION_ID_SCHEDULE:
			$('#checkbox-permission-schedule').attr("checked", true);
			break;
		default:
			break;
		}
	}
}

function doInsert() {
	var operatorId = $('#form-operator-id').val();
	var userId = $('#form-user-id').val();
	var password = $('#form-password').val();
	var confirmPassword = $('#form-confirm-password').val();
	var firstName = $('#form-first-name').val();
	var lastName = $('#form-last-name').val();
	var department = $('#form-department').val();
	var grade = $('#form-grade').val();
	
	if (userId == null || userId.length == 0) {
		alert('Please input the ID');
		$('#form-user-id').focus();
		return false;
	}
	
	if (!checkUserId) {
		alert('Please check the ID');
		$('#check-id-btn').focus();
		return false;
	}
	
	if (password == null || password.length == 0) {
		alert('Please input the password');
		$('#form-password').focus();
		return false;
	}
	else {
		if (password != confirmPassword) {
			alert('Please check the password');
			$('#form-confirm-password').focus();
			return false;
		}
	}
	
	if (firstName == null || firstName.length == 0) {
		alert('Please input the FirstName');
		$('#form-first-name').focus();
		return false;
	}
	
	if (lastName == null || lastName.length == 0) {
		alert('Please input the LastName');
		$('#form-last-name').focus();
		return false;
	}
	
	if (department == null || department.length == 0) {
		alert('Please input the Department');
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
			grade: grade
		},
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				goList();
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

function goList() {
	location.href = '/dashbd/resources/user.do?isBack=true';
}

