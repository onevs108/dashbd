<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% request.setCharacterEncoding("utf-8"); %>

<html>

<head>
	<jsp:include page="common/head.jsp" />
	
	<!-- JSTree -->
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
	<link href="/dashbd/resources/newPublish/css/plugins/chosen/chosen.css" rel="stylesheet"> 
	<link href="/dashbd/resources/newPublish/css/plugins/iCheck/custom.css" rel="stylesheet">
</head>
<body>
<div id="wrapper">
    <!-- sidebar -->
	<jsp:include page="common/leftTab.jsp" />
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<jsp:include page="common/header.jsp" />
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
													<select class="input-sm form-control input-s-sm" id="search-circle" onchange="changeCircle()">
					                            		<option value="">Select Circle</option>
					                            		<c:forEach var="obj" items="${circleList}" varStatus="status">
					                            			<option value="${obj.circle_id}">${obj.circle_name}</option>
					                            		</c:forEach>
					                                </select>
												</div>
												<div class="row">
													<h3 style="height: 17px;"><span id="selectedCircle"></span></h3>
													<div class="slimScrollDiv" style="position: relative; overflow: auto; width: auto; height: 263px;">
														<div class="scroll_content" style="width: auto;">
															<ul class="list-group" id="group_area"></ul>
														</div>
<!-- 														<div class="slimScrollBar" style="background: rgb(0, 0, 0); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 185.938px;"></div> -->
<!-- 														<div class="slimScrollRail" style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(51, 51, 51); opacity: 0.2; z-index: 90; right: 1px;"></div> -->
													</div>
													<div class="hr-line-dashed"></div>
													<div class="input-group">
														<input type="text" class="form-control" style="display:none;">
														<span class="input-group-btn">
															<button type="button" class="btn btn-primary" onclick="addServiceAreaGroup(this)" style="display:none;">Add</button>
														</span>
													</div>
												</div>
											</div>
										</div>
										<div class="col-lg-6">
											<div class="tb_tpl table-responsive vertical-bsl typeA" id="service_area">
												<div id="treeNode" style="height: 340px; overflow: auto;"></div>
											</div>
											<div class="row">
												<div class="hr-line-dashed"></div>
					                        	<div class="col-md-12">
													<button class="btn btn-w-m btn-primary" type="button" id="save-btn" style="display:none;">Save</button>
					                        	</div>
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

<!-- Mainly scripts -->
<!-- <script src="/dashbd/resources/js/jquery-2.1.1.js"></script> -->
<!-- <script src="/dashbd/resources/js/bootstrap.min.js"></script> -->
<!-- <script src="/dashbd/resources/js/plugins/metisMenu/jquery.metisMenu.js"></script> -->
<!-- <script src="/dashbd/resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script> -->

<!-- FooTable -->
<!-- <script src="/dashbd/resources/js/plugins/footable/footable.all.min.js"></script> -->

<!-- Custom and plugin javascript -->
<!-- <script src="/dashbd/resources/js/inspinia.js"></script> -->
<!-- <script src="/dashbd/resources/js/plugins/pace/pace.min.js"></script> -->

<!-- Sweet alert -->
<!--    <script src="/dashbd/resources/js/plugins/sweetalert/sweetalert.min.js"></script> -->
   
<!-- <script src="/dashbd/resources/app-js/config.js"></script> -->

<!-- <script src="/dashbd/resources/js/markerwithlabel.js"></script> -->
<script src="/dashbd/resources/app-js/apps/svc_area_group_map.js"></script>
   
<!-- <script src="js/common.js"></script> -->
   
<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('SERVICE_AREA_GROUP_MGMT');
	});
</script>

</body>
</html>
