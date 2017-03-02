<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<link href="/dashbd/resources/newPublish/css/plugins/iCheck/custom.css" rel="stylesheet">
	<jsp:include page="../common/head.jsp" />
</head>
<body>

<div id="wrapper">
	<!-- sidebar -->
	<jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
<%--     	<jsp:include page="../common/header.jsp" />	 --%>
		<c:import url="/resources/header.do"></c:import>
		<!-- content body -->
        <div class="wrapper wrapper-content">
        	<div class="operatorB">
        		<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-title">
									<h5>Operator Group Management</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
								</div>
								<!-- // ibox-title -->
								
								<c:if test="${userGrade == 13}">
									<div class="row">
										<div class="col-lg-12">
											<div class="ibox">
												<div class="ibox-content">
													<div class="row p-xs">
														<div class="col-sm-9 m-b-xs"><h4>National Group</h4></div>
														<div class="col-sm-3 text-right">
															<a class="btn btn-primary btn-outline" href="#" onclick="callGruopModal('National', 'add')">
									                            <i class="fa fa-plus-square"> </i> Add Group
									                        </a>
														</div>
													</div>
													<div class="tb_tpl table-responsive">
														<table id="table" class="table table-striped">
															<colgroup>
																<col>
																<col>
																<col>
															</colgroup>
															<thead>
																<tr>
																	<th>Group Name</th>
																	<th>Description</th>
																	<th></th>
																</tr>
															</thead>
															<tbody>
															</tbody>
														</table>
													</div>
													<!-- // tb_tpl -->
													
													<!-- MemberList Modal Area -->
													<div id="memberListArea"></div>
												</div>
												<!-- // ibox-content -->
											</div>
											<!-- // ibox -->
										</div>
										<!-- // col -->
									</div>
									<!-- // row -->
								</c:if>
								
								<!-- Group Modal Area -->
								<div id="groupArea"></div>
								<div class="row">
	
									<div class="col-lg-12">
										<div class="ibox">
											<div class="ibox-content">
												<div class="row p-xs">
													<div class="col-sm-9 m-b-xs"><h4>Regional Group</h4></div>
													<div class="col-sm-3 text-right">
														<a class="btn btn-primary btn-outline" href="#" onclick="callGruopModal('Regional','add')">
								                            <i class="fa fa-plus-square"> </i> Add Group
								                        </a>
													</div>
												</div>
												<div class="row">
													<form class="form-horizontal">
														<div class="col-lg-6">
															<div class="form-group">
																<label class="col-sm-6 control-label">Select Area</label>
																<div class="col-sm-6">
																	<select class="input-sm form-control input-s-sm" id="circleSelect" style="padding: 0px 0px 0px 0px;" <c:if test="${USER.grade ==  9999}">readonly</c:if>> 
								                                    	<option value="" <c:if test="${USER.grade == 9999}">disabled</c:if>>Circle</option>
								                                		<c:forEach var="circle" items="${circleList}">
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
													</form>
												</div>
												<div class="tb_tpl table-responsive">
													<table id="table2" class="table table-striped">
														<colgroup>
															<col>
															<col>
															<col>
														</colgroup>
														<thead>
															<tr>
																<th>Area Group Name</th>
																<th>Description</th>
																<th>ID</th>
																<th></th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td colspan="3">No matching records found</td>
															</tr>
														</tbody>
													</table>
												</div>
												<!-- // tb_tpl -->
											</div>
										</div>
									</div>
								</div><!-- // row -->
							</div>
							<!-- e : ibox -->
						</div>
						<!-- // col -->
					</div>
					<!-- // row -->
				</div>
				<!-- // operatorB -->
			</div>
			<!-- e : wrapper -->
			<jsp:include page="../common/footer.jsp" />	
		</div>
		<!-- e : page-wrapper -->
	</div>
	<!-- e : wrapper -->

<script src="js/jquery.cookie.js"></script>
<script src="js/modules/operator.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('OPERATOR_MGMT');
		
		if($("#globalGrade").val() == 13) {
			getOperatorList();	
		} else {
			if($("#circleSelect").val() != '')
				getOperatorList2($("#circleSelect").val());
		}
		
// 		$("input[name='permission']").click(checkSuperAdmin);
// 		$("input[name='permission2']").click(checkSuperAdmin);
		
	});
	
// 	function checkSuperAdmin() {
// 		var selector = "permission"
// 		if(this.name == "permission2") {
// 			selector += "2";
// 		}
// 		if(this.value == "13") {
// 			if($(this).is(":checked")) {
// 				$("input[name='"+selector+"']").attr("disabled", "disabled");
// 				$("input[name='"+selector+"']").prop("checked", false);
// 				$(this).prop("checked", true);
// 	 			this.removeAttribute("disabled");
// 			}else{
// 				$("input[name='"+selector+"']").removeAttr("disabled", "disabled");	
// 			}
// 		}
// 	}
	
</script>

</body>
</html>
