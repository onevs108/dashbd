<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>System Mgmt</title>
	
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
	<link href="css/plugins/iCheck/custom.css" rel="stylesheet">
	<link href="css/animate.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	<link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="../resources/css/plugins/footable/footable.core.css" rel="stylesheet">
	
	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/jquery.cookie.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-table.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- FooTable -->
    <script src="../resources/js/plugins/footable/footable.all.min.js"></script>
    
	<!-- Custom and plugin javascript -->
	<script src="js/inspinia.js"></script>
	<script src="js/plugins/pace/pace.min.js"></script>
	
	<!-- iCheck -->
	<script src="js/plugins/iCheck/icheck.min.js"></script>
	
	<link href="css/plugins/c3/c3.min.css" rel="stylesheet">
	<script src="js/plugins/d3/d3.min.js"></script>
	<script src="js/plugins/c3/c3.min.js"></script>
	<!-- Page-Level Scripts -->

	<link href="css/plugins/chartist/chartist.min.css" rel="stylesheet">
	<script src="js/plugins/chartist/chartist.min.js"></script>
	<script src="/dashbd/resources/app-js/apps/svc_system.js"></script>
	<script src="../resources/js/common.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			
			getMenuList('SYSTEM_STAT_MGMT');
		});
	</script>
	<style type="text/css">
	.labels {
		color: red;
		background-color: white;
		font-family: "Lucida Grande", "Arial", sans-serif;
		font-size: 12px;
		font-weight: bold;
		text-align: center;
		width: 40px;     
		border: 1px solid red;
		white-space: nowrap;
	}
	
	 .timeline {
	     position: absolute;    
	     border: 2px dotted blue;
	     width: 1px;
	     margin: 0;
	     padding: 0;
	     z-index: 9;
	     height: auto;
 	}
	
	</style>
</head>
<body>
<div id="wrapper">
	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<ul class="nav metismenu" id="side-menu">
				<li class="nav-header">
					<div class="dropdown profile-element">
						<a href="/d	ashbd/resources/main.do"><img src="/dashbd/resources/img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="/dashbd/resources/img/logo2.png">
					</div>
				</li>
			</ul>
		</div>
	</nav>
	<div id="page-wrapper" class="gray-bg">
		<!-- content header -->
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header" style="padding-bottom: 10px;">
					<h2 style="margin-left: 15px;"><strong>System Mgmt - Statistic</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> System Mgmt / Statistic</strong>
					</span>
				</div><!-- end navbar-header -->
				<ul class="nav navbar-top-links navbar-right">
					<li>
						<a><i class="fa fa-user"></i><span id="navbar-user-name"></span></a>
					</li>
					<li class="dropdown">
						<ul class="dropdown-menu dropdown-alerts">
							<li>
								<a href="mailbox.html">
									<div>
										<i class="fa fa-envelope fa-fw"></i> You have 16 messages
										<span class="pull-right text-muted small">4 minutes ago</span>
									</div>
								</a>
							</li>
							<li class="divider"></li>
							<li>
								<a href="profile.html">
									<div>
										<i class="fa fa-twitter fa-fw"></i> 3 New Followers
										<span class="pull-right text-muted small">12 minutes ago</span>
									</div>
								</a>
							</li>
							<li class="divider"></li>
							<li>
								<a href="grid_options.html">
									<div>
										<i class="fa fa-upload fa-fw"></i> Server Rebooted
										<span class="pull-right text-muted small">4 minutes ago</span>
									</div>
								</a>
							</li>
							<li class="divider"></li>
							<li>
								<div class="text-center link-block">
									<a href="notifications.html">
										<strong>See All Alerts</strong>
										<i class="fa fa-angle-right"></i>
									</a>
								</div>
							</li>
						</ul>
					</li>
					<li>
						<a href="/dashbd/out">
							<i class="fa fa-sign-out"></i> Log out
						</a>
					</li>
					<li>
						<img src="img/samsung_small.png">
					</li>
				</ul>
			</nav>
		</div>
		<div class="wrapper wrapper-content">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>User Permission Statistics</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
					            <div class="col-sm-6 b-r" class="list-group">
					                <c:choose>
										<c:when test="${USER.grade == 0}">
											<select name="status" id="operatorPermission" class="form-control">
												<option value="">--ALL--</option>
												<c:forEach items="${OperatorList}" var="operator">
													<option value="${operator.id}">${operator.name}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select name="status" id="operatorPermission" class="form-control" disabled="disabled">
											<c:forEach items="${OperatorList}" var="operator">
												<c:choose>
													<c:when test="${USER.operatorId == operator.id}">
														<option value="${operator.id}" selected="selected">${operator.name}</option>
													</c:when>
													<c:otherwise>
														<option value="${operator.id}">${operator.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
									<div class="hr-line-dashed"></div>
									<div class="table-responsive">
										<table class="table table-bordered" id="permissionTable"></table>
									</div>
					            </div>
					            <div class="col-sm-6" class="list-group">
			                        <div style="padding-top: 40px;">
			                            <div id="permissionChart" class="ct-perfect-fourth"></div>
			                        </div>
					            </div>
					         </div>
				        </div>
					</div>
				</div>
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>Content Statistics</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <form method="get" class="form-horizontal">
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
					            <div class="col-sm-4">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Operator</label>
										<div class="col-sm-9">
											<c:choose>
												<c:when test="${USER.grade == 0}">
													<select name="status" id="operatorContent" class="form-control">
														<option value="">--ALL--</option>
													<c:forEach items="${OperatorList}" var="operator">
														<option value="${operator.id}">${operator.name}</option>
													</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select name="status" id="operatorContent" class="form-control" disabled="disabled">
														<option value="">--ALL--</option>
														<c:forEach items="${OperatorList}" var="operator">
															<c:choose>
																<c:when test="${USER.operatorId == operator.id}">
																	<option value="${operator.id}" selected="selected">${operator.name}</option>
																</c:when>
																<c:otherwise>
																	<option value="${operator.id}">${operator.name}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label class="col-sm-5 control-label" for="status">BM-SC</label>
										<div class="col-sm-7">
											<select name="status" id="bmscContent" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>	
								</div>
								<div class="col-sm-5">
									<div class="form-group">
										<label class="col-sm-5 control-label" for="status">Service Area</label>
										<div class="col-sm-7">
											<select name="status" id="serviceContent" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>
								</div>
					         </div>
					    	<div class="row">
					            <div class="col-sm-4">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Year</label>
										<div class="col-sm-9">
											<select name="status" id="yearContent" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 2016; i < year+5; i++){
													 	$("#yearContent").append('<option value="'+i+'">'+i+'</option>');
													}
												</script>
											</select>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label class="col-sm-5 control-label" for="status">Month</label>
										<div class="col-sm-7">
											<select name="status" id="monthContent" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 1; i < 13; i++){
														if(i < 10){
														 	$("#monthContent").append('<option value="0'+i+'">'+i+'</option>');
														}else{
														 	$("#monthContent").append('<option value="'+i+'">'+i+'</option>');
														}
													}
												</script>
											</select>
										</div>
									</div>	
								</div>
								<div class="col-sm-2">
									<div class="form-group">
										<label class="col-sm-5 control-label" for="status">Day</label>
										<div class="col-sm-7">
											<select name="status" id="dayContent" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label class="col-sm-5 control-label" for="status">Service Type</label>
										<div class="col-sm-7">
											<select name="status" id="serviceTypeContent" class="form-control">
												<option value="">--ALL--</option>
												<option value="Streaming">Streaming</option>
												<option value="Filedownload">Filedownload</option>
											</select>
										</div>
									</div>
								</div>
					         </div>
					    	<div class="row">
					    		<div class="col-sm-12" style="min-height: 100px !important;">
						             <div style="padding-top: 10px;">
			                            <div id="contentChart" style="min-height: 100px !important;"></div>
			                        </div>
						        </div>
					        </div>
				        </div>
				        </form>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>#of eNBs</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <form method="get" class="form-horizontal">
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
					            <div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Operator</label>
										<div class="col-sm-9">
											<c:choose>
												<c:when test="${USER.grade == 0}">
													<select name="status" id="operatorEnbs" class="form-control">
														<option value="">--ALL--</option>
													<c:forEach items="${OperatorList}" var="operator">
														<option value="${operator.id}">${operator.name}</option>
													</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select name="status" id="operatorEnbs" class="form-control" disabled="disabled">
														<option value="">--Select--</option>
														<c:forEach items="${OperatorList}" var="operator">
															<c:choose>
																<c:when test="${USER.operatorId == operator.id}">
																	<option value="${operator.id}" selected="selected">${operator.name}</option>
																</c:when>
																<c:otherwise>
																	<option value="${operator.id}">${operator.name}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
							 </div>
							 <div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">BM-SC</label>
										<div class="col-sm-9">
											<select name="status" id="bmscEnbs" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>	
								</div>
					         </div>
					    	<div class="row">
								<div class="col-sm-12">
									<div id="eNbsCount" style="text-align: center">
								        <h2><strong>-</strong></h2>
								    </div>
								</div>
					        </div>
				        </div>
				        </form>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>#of Service Areas</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <form method="get" class="form-horizontal">
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
					            <div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Operator</label>
										<div class="col-sm-9">
											<c:choose>
												<c:when test="${USER.grade == 0}">
													<select name="status" id="operatorServiceArea" class="form-control">
														<option value="">--ALL--</option>
													<c:forEach items="${OperatorList}" var="operator">
														<option value="${operator.id}">${operator.name}</option>
													</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select name="status" id="operatorServiceArea" class="form-control" disabled="disabled">
														<option value="">--ALL--</option>
														<c:forEach items="${OperatorList}" var="operator">
															<c:choose>
																<c:when test="${USER.operatorId == operator.id}">
																	<option value="${operator.id}" selected="selected">${operator.name}</option>
																</c:when>
																<c:otherwise>
																	<option value="${operator.id}">${operator.name}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
							 </div>
							 <div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">BM-SC</label>
										<div class="col-sm-9">
											<select name="status" id="bmscServiceArea" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>	
								</div>
					         </div>
					    	<div class="row">
								<div class="col-sm-12">
									<div id="serviceAreaCount" style="text-align: center">
								        <h2><strong>-</strong></h2>
								    </div>
								</div>
					        </div>
				        </div>
				        </form>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>Interface Traffic (schedule Packet)</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <form method="get" class="form-horizontal">
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
					            <div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Operator</label>
										<div class="col-sm-3">
											<c:choose>
												<c:when test="${USER.grade == 0}">
													<select name="status" id="operatorInterTff" class="form-control">
														<option value="">--ALL--</option>
													<c:forEach items="${OperatorList}" var="operator">
														<option value="${operator.id}">${operator.name}</option>
													</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select name="status" id="operatorInterTff" class="form-control" disabled="disabled">
														<option value="">--Select--</option>
														<c:forEach items="${OperatorList}" var="operator">
															<c:choose>
																<c:when test="${USER.operatorId == operator.id}">
																	<option value="${operator.id}" selected="selected">${operator.name}</option>
																</c:when>
																<c:otherwise>
																	<option value="${operator.id}">${operator.name}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</c:otherwise>
											</c:choose>
										</div>
										<label class="col-sm-3 control-label" for="status">BM-SC</label>
										<div class="col-sm-3">
											<select name="status" id="bmscInterTff" class="form-control">
												<option value="">--ALL--</option>
											</select>
										</div>
									</div>
								</div>
							 </div>
					    	<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Year</label>
										<div class="col-sm-3">
											<select name="status" id="yearInterTff" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 2016; i < year+5; i++){
													 	$("#yearInterTff").append('<option value="'+i+'">'+i+'</option>');
													}
												</script>
											</select>
										</div>
										<label class="col-sm-3 control-label" for="status">Month</label>
										<div class="col-sm-3">
											<select name="status" id="monthInterTff" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 1; i < 13; i++){
														if(i < 10){
														 	$("#monthInterTff").append('<option value="0'+i+'">'+i+'</option>');
														}else{
														 	$("#monthInterTff").append('<option value="'+i+'">'+i+'</option>');
														}
													}
												</script>
											</select>
										</div>
									</div>	
								</div>
							</div>		
					    	<div class="row">
								<div class="col-sm-12">
									<div class="table-responsive">
										<table class="table table-bordered" id="interTffTable"></table>
									</div>
								</div>
					        </div>
				        </div>
				        </form>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>Incoming Traffic (Total Page Access Log)</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <form method="get" class="form-horizontal">
					    <div class="ibox-content" style="display: block;">
					    	<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="status">Year</label>
										<div class="col-sm-3">
											<select name="status" id="yearIncomTff" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 2016; i < year+5; i++){
													 	$("#yearIncomTff").append('<option value="'+i+'">'+i+'</option>');
													}
												</script>
											</select>
										</div>
										<label class="col-sm-3 control-label" for="status">Month</label>
										<div class="col-sm-3">
											<select name="status" id="monthIncomTff" class="form-control">
												<option value="">--ALL--</option>
				                                <script>
													var myDate = new Date();
													var year = myDate.getFullYear();
													for(var i = 1; i < 13; i++){
														if(i < 10){
														 	$("#monthIncomTff").append('<option value="0'+i+'">'+i+'</option>');
														}else{
														 	$("#monthIncomTff").append('<option value="'+i+'">'+i+'</option>');
														}
													}
												</script>
											</select>
										</div>
									</div>	
								</div>
							</div>
					    	<div class="row">
								<div class="col-sm-12">
									<div class="table-responsive">
										<table class="table table-bordered" id="incomTffTable"></table>
									</div>
								</div>
					        </div>
				        </div>
				        </form>
					</div>
				</div>
			</div>
		</div>	
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

</body>
</html>
	
