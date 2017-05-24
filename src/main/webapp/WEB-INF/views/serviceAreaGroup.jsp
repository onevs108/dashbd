<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% request.setCharacterEncoding("utf-8"); %>

<html>

<head>
	<jsp:include page="common/head.jsp" />
	
	<!-- JSTree -->
	<link href="/dashbd/resources/newPublish/css/plugins/chosen/chosen.css" rel="stylesheet"> 
	<link href="/dashbd/resources/newPublish/css/plugins/iCheck/custom.css" rel="stylesheet">
	
	<style type="text/css">
		.jstree-node {
			font-size:14px
		}
	</style>
</head>
<body>
<div id="wrapper">
    <!-- sidebar -->
	<jsp:include page="common/leftTab.jsp" />
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<c:import url="/resources/header.do"></c:import>
		<!-- content body -->
		<div class="wrapper wrapper-content">
			<div class="serviceGrpMgmt">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox">
							<div class="ibox-title">
	                            <h5>Service Group Management</h5>
	                            <div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
								</div>
	                        </div>
	                        <div class="ibox-content" style="min-height:455px;">
								<div class="row">
									<form class="form-horizontal">
										<div class="col-lg-6" style="height: 415px;">
											<div class="col-lg-12">
												<div class="form-group">
													<h3>Select the Area to view the list of Service Area Group</h3>
													<select class="input-sm form-control input-s-sm" id="search-circle" onchange="changeCircle()" <c:if test="${USER.grade ==  9999}">readonly</c:if>>
					                            		<option value="" <c:if test="${USER.grade ==  9999}">disabled</c:if>>Select Area</option>
					                            		<c:forEach var="circle" items="${circleList}" varStatus="status">
					                            			<c:choose>
																<c:when test="${USER.grade == 9999}">
																	<c:choose>
																		<c:when test="${circle.circle_name == USER.circleName}">
																			<option value="${circle.circle_id}" selected>${circle.circle_name}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${circle.circle_id}" disabled>${circle.circle_name}</option>		
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<option value="${circle.circle_id}">${circle.circle_name}</option>		
																</c:otherwise>
															</c:choose>
					                            		</c:forEach>
					                                </select>
												</div>
												<div class="row">
													<div class="slimScrollDiv" style="position: relative; overflow: auto; width: auto; height: 295px;">
														<div class="scroll_content" style="width: auto;">
															<ul class="list-group" id="group_area"></ul>
														</div>
<!-- 														<div class="slimScrollBar" style="background: rgb(0, 0, 0); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 185.938px;"></div> -->
<!-- 														<div class="slimScrollRail" style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(51, 51, 51); opacity: 0.2; z-index: 90; right: 1px;"></div> -->
													</div>
													<div class="hr-line-dashed"></div>
													<div class="input-group" style="text-align: center;">
<!-- 														<input type="text" class="form-control" style="display:none;"> -->
														<span class="input-group-btn">
															<button type="button" class="btn btn-primary" onclick="openModal()" style="display:none;">Create a New Service Area Group</button>
														</span>
<!-- 														<span class="input-group-btn"> -->
<!-- 															<button type="button" class="btn btn-primary" onclick="addServiceAreaGroup(this)" style="display:none;">Add</button> -->
<!-- 														</span> -->
													</div>
<!-- 													<div class="input-group infoArea" style="margin-top: 5px;display:none"> -->
<!-- 													  Please enter the Service Area Group name and click “Add” button to add the Service Area Group -->
<!-- 													</div> -->
												</div>
											</div>
										</div>
										<div class="col-lg-6">
											<h3 style="height: 17px;"><span id="selectedCircle"></span></h3>
											<div class="tb_tpl table-responsive vertical-bsl typeA" id="service_area">
												<div id="treeNode" style="height: 340px; overflow: auto;"></div>
											</div>
											<div class="row">
												<div class="hr-line-dashed"></div>
					                        	<div class="col-md-12" style="margin-bottom:5px; text-align: center;">
													<button class="btn btn-w-m btn-primary" type="button" id="save-btn" style="display:none;">Save the Service Area Group <span id="groupName"/></button>
					                        	</div>
<!-- 					                        	<div class="input-group infoArea" style="display:none"> -->
<!-- 												  Please select city or hotspot to be included to the Service Area Group and click “Save” button -->
<!-- 												</div> -->
					                        </div>
										</div>
									</form>
		                        </div>
		                    </div><!-- end ibox-content -->
						</div>
					</div>
				</div>
			</div>
		</div><!-- end wrapper wrapper-content -->
		<jsp:include page="common/footer.jsp" />
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->
<div class="modal" id="myModal3" aria-hidden="true" style="display: none; z-index: 1060;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<h3 class="modal-title2">Create New Service Area Group for <span id="groupNameModal"></span> Area</h3>
			</div>
			<div class="modal-body">
				<form class="form-inline" style="text-align: center;">
	            	<div class="form-group">
						<label class="control-label">Service Area Group Name</label>
						<input type="text" placeholder="" class="form-control" id="newGroupName" name="newGroupName" style="margin-left: 20px;">
					</div>
				</form>
				<div class="hr-line-dashed"></div>
				<div class="tb_tpl table-responsive vertical-bsl typeA" id="service_area">
					<div id="treeNodeModal" style="height: 340px; overflow: auto;"></div>
				</div>
            </div>
            <div class="modal-footer" style="text-align: center;">
				<button type="button" onclick="javascript:addServiceAreaGroup(this);" class="btn btn-primary">Create</button>
				<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<script src="/dashbd/resources/app-js/apps/svc_area_group_map.js"></script>
   
<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('SERVICE_AREA_GROUP_MGMT');
		
		if($("#globalGrade").val() == 9999) {
			changeCircle();
		}
	});
</script>

</body>
</html>
