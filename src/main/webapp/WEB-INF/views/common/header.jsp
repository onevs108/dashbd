<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="com.catenoid.dashbd.util.SessionCounterListener"%>

<div class="row border-bottom">
	<input type="hidden" id="globalGrade" value="${USER.grade}">
	<nav class="navbar navbar-static-top" role="navigation"
		style="margin-bottom: 0;">
		<div class="navbar-header">
			<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"> <i class="fa fa-bars"></i></a> <span class="navbar-form-custom">SeSM (<c:choose><c:when test="${USER.gradeName != null}">${USER.gradeName}</c:when><c:otherwise>Administrator Group</c:otherwise></c:choose>)</span>
		</div>

		<ul class="nav navbar-top-links navbar-right">
			<li>
				<div class="profile-element">
					<span class="clear">
						<span class="block m-t-xs" style="cursor: pointer;">
							<strong class="font-bold" onclick="doAllEdit('${USER.userId}', '${USER.circleName}')">${USER.lastName} ${USER.firstName} (${USER.department})</strong>
						</span>
					</span>
				</div>
			</li>
			<li><a href="/dashbd/out"><i class="fa fa-sign-out"></i>
					Log out</a></li>
		</ul>
	</nav>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
				<i class="fa fa-laptop modal-icon"></i>
				<h4 class="modal-title">Add Operator</h4>
			</div>
			<form role="form" id="form" class="form-horizontal">
				<div class="modal-body">
					<fieldset>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Group</label>
									<div class="col-sm-6">
										<c:choose>
											<c:when test="${USER.grade == 13 || USER.grade == 1 || USER.grade == 2 || USER.grade == 3}">
												<select name="status" id="modal-grade-id" onchange="changeGroup(this, 'modal_circle_area')" class="input-sm form-control input-s-sm">
													<option value="">Group</option>
													<c:forEach var="row" items="${gradeList}">
														<option value="${row.id}">${row.name}</option>
													</c:forEach>
											</c:when>
											<c:otherwise>
												<select name="status" id="modal-grade-id"  onchange="changeGroup(this, 'modal_circle_area')" class="input-sm form-control input-s-sm" readonly>
												<c:forEach items="${gradeList}" var="row">
													<c:choose>
														<c:when test="${USER.grade == row.id}">
															<option value="${row.id}" selected>${row.name}</option>
														</c:when>
														<c:otherwise>
															<option value="${row.id}" disabled>${row.name}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div id="modal_circle_area" class="col-lg-6" <c:if test="${USER.grade !=  9999}">style="display:none;"</c:if>>
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Circle</label>
									<div class="col-sm-6">
										<select class="form-control" id="modal-circle-id" onchange="getTownListFromCircle(this)" <c:if test="${USER.grade ==  9999}">readonly</c:if>>
											<option value="">Circle</option>
											<c:forEach items="${circleList}" var="circle">
												<c:choose>
													<c:when test="${USER.grade == 9999}">
														<c:choose>
															<c:when test="${circle.circle_name == USER.circleName}">
																<option value="${circle.circle_name}" selected>${circle.circle_name}</option>
															</c:when>
															<c:otherwise>
																<option value="${circle.circle_name}" disabled>${circle.circle_name}</option>		
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<option value="${circle.circle_name}">${circle.circle_name}</option>		
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div id="modal_circle_group" class="col-lg-6" <c:if test="${USER.grade !=  9999}">style="display:none;"</c:if>>
								<label class="col-sm-6 control-label">Circle Group</label>
								<div class="col-sm-6">
                                   	<select class="input-sm form-control input-s-sm" id="form-circle" readonly>
                                   		<option value="">Circle Group</option>
                                   		<c:forEach var="cgroup" items="${townList}">
                                   			<option value="${cgroup.id}" selected>${cgroup.town_name}</option>
                                   		</c:forEach>
									</select>
                                </div>
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="User ID"
									class="form-control" id="form-user-id" name="form-user-id">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Last Name"
									class="form-control" id="form-last-name" name="form-last-name">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="password" placeholder="Password"
									class="form-control" id="form-password" name="form-password">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="First Name"
									class="form-control" id="form-first-name" name="form-first-name">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="password" placeholder="Confirm Password"
									class="form-control" id="form-confirm-password" name="form-confirm-password">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Department"
									class="form-control" id="form-department" name="form-department">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-12 m-b">
								<textarea class="form-control" rows="6" placeholder="Memo" id="form-memo" name="form-memo"></textarea>
							</div>
						</div>
						<!-- // row -->
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" id="editBtn" onclick="doInsertAll('edit')" class="btn btn-primary">Edit</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">

function doAllEdit(userId, circleName) {
	getAllUserList('edit', userId, circleName);
}

//버튼 클릭에 따른 유저 팝업 컨트롤 메소드
function getAllUserList(accessDiv, userId, circleName) {
	$(".modal-title").text("Edit Operator");
	$("#editBtn").show();
	getUserInfoAll(userId, circleName);
	
	$("#myModal #form-user-id").prop("readonly", true);
	$("#myModal #form-check-btn").prop("disabled", true);
	$("#myModal select").prop("disabled", true);
}

function getUserInfoAll(userId, circleName) {
	$.ajax({
		url: '/dashbd/api/user/info.do',
		method: 'POST',
		dataType: 'json',
		data: {
			userId: userId
		},
		success: function(data, textStatus, jqXHR) {
			setElementsAll(data.user, circleName);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			swal("Fail !", errorThrown, "warning");
			return false;
		}
	});
}

function setElementsAll(user, circleName) {
	if(user.grade == '9999'){$("#circleArea").show();}
	for(var i=0; i < $('#modal-circle-id option').length; i++) {
		var tempOpt = $($('#modal-circle-id option')[i]);
		if(tempOpt.text() == user.circleName) {
			tempOpt.prop("selected", true);
		}
	}
	
	if(user.operatorId != '') {
		$('#modal_circle_area').show();
		$('#modal_circle_group').show();
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
	$('#modal-grade-id').val(user.grade);
	$('#form-memo').val(user.memo);
	
	//Circle Gruop이 아닐 경우 숨김처리 및 초기화
	if($("#modal-grade-id").val() != 9999) {
		$("#modal-circle-id").val('');
		$("#form-circle").val('');
		$("#modal_circle_area").hide();
		$("#modal_circle_group").hide();
	}
	
	$("#myModal").modal('show');
}

function doInsertAll(accessDiv) {
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
		} else {
			if(grade == 9999) {
				if($("#modal-circle-id").val() == '') {
					swal("Fail !","Please select your Circle", "warning");
					return false;
				}
				
				if(operatorId == '') {
					swal("Fail !","Please select your Circle Group", "warning");
					return false;
				}
			}
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

</script>