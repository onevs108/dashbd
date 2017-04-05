<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row border-bottom">
	<input type="hidden" id="globalGrade" value="${USER.grade}">
	<nav class="navbar navbar-static-top" role="navigation"
		style="margin-bottom: 0;">
		<a href="#" class="btn btn-w-m btn-link" style="padding-top: 18px;"><i class="fa fa-rss"></i> <u onclick="javascript:showServiceAreaBandwidth()">Service Area Bandwidth</u></a>
		<div class="navbar-header">
			<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"> <i class="fa fa-bars"></i></a> <span class="navbar-form-custom">SeSM (<c:choose><c:when test="${USER.gradeName != null}">${USER.gradeName}</c:when><c:otherwise>Administrator Group</c:otherwise></c:choose>)</span>
		</div>
		<ul class="nav navbar-top-links navbar-right">
			<li>
				<div class="profile-element">
					<span class="clear">
						<span class="block m-t-xs" style="padding: 14px 10px 14px 15px;">
							<c:choose>
								<c:when test="${USER.grade == 13}">
									<strong class="font-bold">${USER.lastName} ${USER.firstName} (${USER.department})</strong>
								</c:when>
								<c:otherwise>
									<strong class="font-bold" style="cursor: pointer;" onclick="doEdit('${USER.userId}', true)">${USER.lastName} ${USER.firstName} (${USER.department})</strong>
								</c:otherwise>
							</c:choose>
						</span>
					</span>
					
				</div>
			</li>
			<li>
				<h2 class="text-center"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}" /></h2>
				<div id="headerClock" class="light">
					<div class="digits"></div>
				</div>
			</li>
			<li><a href="/dashbd/out"><i class="fa fa-sign-out"></i>
					Log out</a></li>
		</ul>
	</nav>
</div>

<!-- s : POPUP - Add Operator -->
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
							<div class="col-lg-6">
<%-- 								<c:if test="${USER.grade == 13}"> --%>
									<button type="button" id="init-password-btn" onclick="javascript:initPassword();" class="btn btn-primary">Reset Password</button>
<%-- 								</c:if> --%>
							</div>
						</div>
						<div class="row">
							<div id="modal_circle_area" class="col-lg-6" <c:if test="${USER.grade !=  9999}">style="display:none;"</c:if>>
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Area</label>
									<div class="col-sm-6">
										<select class="form-control" id="modal-circle-id" onchange="getTownListFromCircle(this)" <c:if test="${USER.grade ==  9999}">readonly</c:if>>
											<option value="" <c:if test="${USER.grade == 9999}">disabled</c:if>>Area</option>
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
								<label class="col-sm-6 control-label">Area Group</label>
								<div class="col-sm-6">
                                   	<select class="input-sm form-control input-s-sm" id="form-circle">
                                   		<option value="">Area Group</option>
                                   		<c:forEach var="cgroup" items="${townList}">
                                   			<option value="${cgroup.id}">${cgroup.town_name}</option>
                                   		</c:forEach>
									</select>
                                </div>
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<div class="input-group" style="padding: 0 0 0 0;">
									<input type="text" placeholder="User ID" class="form-control" id="form-user-id" onblur="javascript:doCheckId(false);"> 
									<span class="input-group-btn">
										<button type="button" id="form-check-btn" onclick="javascript:doCheckId(true);" class="btn btn-primary">Check</button>
									</span>
								</div>
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
					<button type="button" id="addBtn" onclick="javascript:doInsert('add');" class="btn btn-primary">Add</button>
					<button type="button" id="editBtn" onclick="javascript:doInsert('edit');" class="btn btn-primary">Edit</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
				</div>
			</form>
		</div>
	</div>
	<jsp:include page="serviceAreaBandwidth.jsp" />
</div>
<!-- e : POPUP - Add Operator -->

<script type="text/javascript">
	var commonYn;
	var checkUserId = false;
	
	function doSearch() {
		$('#table').bootstrapTable('destroy');
		getUserList(false, true);
	}
	
	function doInfo(userId) {
		userFormAccess('info', userId);
	}
	
	function doEdit(userId, commonModalYn) {
		commonYn = commonModalYn;
		userFormAccess('edit', userId);
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
				circleName: $(circleObj).val()
			},
			success: function(data, textStatus, jqXHR) {
				if (data.result) {
					var html = "<option value=''>Area Group</option>";
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
				return false;
			}
		});	
	}
	
	//버튼 클릭에 따른 유저 팝업 컨트롤 메소드
	function userFormAccess(accessDiv, userId) {
		if(accessDiv == 'add') {
			//유저 등록 폼을 다시 띄울 경우 체크 여부 초기화
			checkUserId = false;
			
			$(".modal-title").text("Add Operator");
	
			if($("#globalGrade").val()  == 13) {
				$("#modal-grade-id").val('');
				$("#modal_circle_area").hide();
				$("#modal_circle_group").hide();
				$("#modal-circle-id").val('');
				$("#form-circle").empty();
				$("#form-circle").append('<option value="">Area Group</option>');
			}
			$("#addBtn").show();
			$("#editBtn").hide();
			$("#myModal input, #myModal textarea").val('');
			$("#myModal input, #myModal select, #myModal textarea, #myModal #form-check-btn").prop("disabled", false);
			
			$("#init-password-btn").hide();
		} else if(accessDiv == 'edit') {
			$(".modal-title").text("Edit Operator");
			$("#editBtn").show();
			$("#addBtn").hide();
			getUserInfo(userId);
			
			$("#myModal #form-user-id").prop("readonly", true);
			$("#myModal #form-check-btn").prop("disabled", true);
			$("#myModal input, #myModal select, #myModal textarea").prop("disabled", false);
			
			$("#init-password-btn").show();
		} else if(accessDiv == 'info') {
			$(".modal-title").text("Operator" + " \"" + userId + "\" Info");
			$("#editBtn").hide();
			$("#addBtn").hide();
			getUserInfo(userId);
			$("#myModal input, #myModal select, #myModal textarea, #myModal #form-check-btn").prop("disabled", true);
			
			$("#init-password-btn").hide();
		}
	}
	
	function doCheckId(proccessYn) {
		if(!proccessYn && $("#form-user-id").attr("data-init") != $("#form-user-id").val()) {
			checkUserId = false;
		} else if(!proccessYn && $("#form-user-id").attr("data-init") == $("#form-user-id").val()) {
			checkUserId = true;
		} else {
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
						$("#form-user-id").attr("data-init", $("#form-user-id").val());
						checkUserId = true;
						swal("Success !","Avaliable!", "success");
					}
					else { // 실패
						checkUserId = false;
						$("#form-user-id").attr("data-init", "");
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
		
		//Circle Gruop이 아닐 경우 숨김처리 및 초기화
		if($("#modal-grade-id").val() != 9999) {
			$("#modal-circle-id").val('');
			$("#form-circle").val('');
			$("#modal_circle_area").hide();
			$("#modal_circle_group").hide();
		}
		
		$("#myModal").modal('show');
	}
	
	function doInsert(accessDiv) {
		setTimeout(function() {
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
							swal("Fail !","Please select your Area", "warning");
							return false;
						}
						
						if(operatorId == '') {
							swal("Fail !","Please select your Area Group", "warning");
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
						operatorName : $("#modal-grade-id option:selected").text(),
						memo: memo
					},
					success: function(data, textStatus, jqXHR) {
						if (data.result) { // 성공
							swal({title:"Success !", text:"Success", type:"success"}, function() {
								if(!commonYn) {
									$('#table').bootstrapTable('destroy');
									getUserList(true, false);	
									$("#myModal").modal('hide');
								}
							});
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
		}, 200);
	}
	
	function initPassword() {
		swal({
		  title: "Are you sure?",
		  text: "Do you really want to init the " + $("#form-user-id").val() + "'s password?",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Yes",
		  closeOnConfirm: false
		},
		function(){
			$.ajax({
				url: '/dashbd/api/user/initPassword.do',
				method: 'POST',
				dataType: 'json',
				data: {
					userId: $("#form-user-id").val()
				},
				success: function(data, textStatus, jqXHR) {
					if (data.resultCode == 'S') { // 성공
						swal({title:"Success !", text:"Success", type:"success"}, function() {
							$("#myModal").modal("hide");
						});
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
	
	function showServiceAreaBandwidth() {
		$.ajax({
		    url : "/dashbd/api/getTreeNodeData.do",
		    type: "POST",
		    beforeSend:function() {
		    	$.blockUI();
		    },
		    data : { 
		    	gruop_id : '',
		    	searchType : $("#areaBandwidthModal #searchType").val(),
		    	searchInput : $("#areaBandwidthModal #search-input").val(),
		    	circle_id : $("#circleModal #circleId").val()
		    },
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		         
		        if(data.resultList.length != 0) {
			        $("#serviceAreaTree").jstree("destroy").empty();
			        serviceAreaTreeInit(data);
		        } else {
		        	swal({title:"Not Found !", text:"Please enter the keyword", type:"warning"}, function() {
		        		$("#areaBandwidthModal #search-input").val('');
		        		$("#areaBandwidthModal #searchType").val('');
		    		})
		        }
		        
		        $.unblockUI();
		    },
	        error : function(xhr, status, error) {
	        	$.unblockUI();
	        	
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
		});
	}
	
	//jsTree Init Function
	function serviceAreaTreeInit(data) {
		var treeData = data.resultList;
		for(var i=0; i < treeData.length; i++) {
			var node = treeData[i];
			
			//root를 그려줌(Circles)
			if(i == 0) {
				$('#serviceAreaTree').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + '</li></ul>');
				continue;
			}
			
			//현재 붙여넣어 줄 노드의 구분값을 판단하여 그에 따른 상위 노드만 모아서 부모 노드를 찾음
			var divClass = '';
			if(node.node_div == 'circle') divClass='root';
			else if(node.node_div == 'city') divClass='circle';
			else if(node.node_div == 'hotspot') divClass='city';
			
			for(var j=0; j < $('#serviceAreaTree li.' + divClass).length; j++) {
				var compareNode = $('#serviceAreaTree li.' + divClass)[j];
				
				if($(compareNode).attr("data-init") == node.pnode_id) { 
					var liStr = '<li class="' + node.node_div + '" title="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' 
								+ node.latitude + '" data-lng="' + node.longitude + '" data-band="' + node.bandwidth + '">' + node.name + '</li>';
					
					if($(compareNode).html().indexOf("ul") == -1) {
						$(compareNode).append('<ul>' + liStr + '</ul>');
					} else {
						$($(compareNode).find("ul")[0]).append(liStr);
					}
					
					break;
				}
			}
		}
		
		$("#serviceAreaTree")
			.bind('before_open.jstree', function(evt, data) {
				$(".jstree-icon.jstree-themeicon").remove();
			})  
			.bind('select_node.jstree', function(evt, data) {
				var said = '';
				var tempSaid = $("#" + data.selected[0]).attr("data-init");
				if($("#" + data.selected[0]).hasClass("circle")) {
					said = tempSaid.substring(tempSaid.indexOf("A")+1);
				} else if($("#" + data.selected[0]).hasClass("city")) {
					said = tempSaid.substring(tempSaid.indexOf("B")+1);
				} else if($("#" + data.selected[0]).hasClass("hotspot")) {
					said = tempSaid.substring(tempSaid.indexOf("C")+1);
				} else {
					$("#areaBandwidthModal #bandwidth").val("");
					$("#areaBandwidthModal #usedBandwidth").val("");
					$("#areaBandwidthModal #remainedBandwidth").val("");
					return;
				}
				
				$.ajax({
				    url : "/dashbd/view/checkBandwidth.do",
				    type: "POST",
				    data : { 
				    	saidList : said,
				    	bandwidth : '0'
				    },
				    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				    success : function(responseData) {
				        $("#areaBandwidthModal #bandwidth").val(data.node.data.band);
				        $("#areaBandwidthModal #usedBandwidth").val(responseData.usedBandwidth);
				        $("#areaBandwidthModal #remainedBandwidth").val(responseData.enableBandwidth);
				    },
			        error : function(xhr, status, error) {
			        	swal({
			                title: "Fail !",
			                text: "Error"
			            });
			        }
				});
			})
			.bind('ready.jstree', function(e, data) {
				$(".jstree-icon.jstree-themeicon").remove();
				$("#areaBandwidthModal #bandwidth").val("");
				$("#areaBandwidthModal #usedBandwidth").val("");
				$("#areaBandwidthModal #remainedBandwidth").val("");
				$("#areaBandwidthModal").modal("show");
		    }).jstree({
		    	"core" : {
	    	       "check_callback" : true
	    	     },
	    	    "plugins" : [ "contextmenu" ]
			  });
		
		//제일 처음 노드 오픈
		$("#serviceAreaTree").jstree("open_node", $("#serviceAreaTree .root"));
	}

	$(document).on("keydown", "#areaBandwidthModal #search-input", function(event) {
		//Enter입력시에만 조회
		if(event.keyCode == 13) {
			searchServiceAreaTreeNode();
	    }
	})

	function searchServiceAreaTreeNode() {
		var searchString = $("#areaBandwidthModal #search-input").val();
		
		if(searchString == '') 
			$("#areaBandwidthModal #searchType").val('');
		
		showServiceAreaBandwidth();
	}
</script>