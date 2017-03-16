<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% request.setCharacterEncoding("utf-8"); %>

<html>
<head>
	<jsp:include page="../common/head.jsp" />
	<link href="css/plugins/datapicker/datepicker3.css" rel="stylesheet">
	
	<style>
		ul{
		   list-style:none;
		   }
	</style>
</head>
<body>
<div id="wrapper">
	<!-- sidebar -->
	<jsp:include page="../common/leftTab.jsp" />
	
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<c:import url="/resources/header.do"></c:import>
		
		<!-- content body -->
		<div class="wrapper wrapper-content">
			<div class="serviceArea">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox">
							<div class="ibox-title">
	                            <h5>System Mgmt</h5>
	                            <div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
								</div>
	                        </div>
							<div class="ibox-content">
		                        <div class="row">
									<div class="col-lg-12">
										<div class="tabs-container">
											<ul class="nav nav-tabs">
											    <li class="active"><a href="#tab-1" data-toggle="tab" onclick="tabChange('1')">Operator Log</a></li>
											    <li><a href="#tab-2" data-toggle="tab" onclick="tabChange('2')">Service Area</a></li>
											    <li><a href="#tab-3" data-toggle="tab" onclick="tabChange('3')">Schedule</a></li>
											    <li><a href="#tab-4" data-toggle="tab" onclick="tabChange('4')">DB Backup</a></li>
											</ul>
											<div class="tab-content">	
												<div class="panel-body">
													<form class="form-horizontal" action="javascript:void(0);">
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">From</label>
																	<div class="col-sm-4">
																		<div class="input-group date" id="data_1">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateFrom" value="">
										                                </div>
																	</div>
																	<label class="col-sm-2 control-label">To</label>
																	<div class="col-sm-4">
																		<div class="input-group date" id="data_2">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateTo" value="">
										                                </div>
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">Search</label>
																	<div class="col-sm-10">
																		<div class="input-group">
																			<input type="text" class="form-control" onkeydown="javascript:if(event.keyCode == 13) ">
																			<span class="input-group-btn">
																				<button type="button" onclick="" class="btn btn-primary">Search</button>
																			</span>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</form>		
													<div class="row col-xs-6" style="margin-top:20px; height:500px; overflow:auto">
														<div id="tab-1" class="tab-pane active">
															<ul style="padding: 0 0 0 0;">
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
																<li>[2017-03-10 : 03:34:24] User ID : wkim2580 – Login (IP address : 211.12.12.34)</li>
															</ul>
														</div>
														<div id="tab-2" class="tab-pane">
															
														</div>
														<div id="tab-3" class="tab-pane">
															
														</div>
														<div id="tab-4" class="tab-pane">
															
														</div>
													</div>						
												</div>
											</div>
										</div>
									</div>
		                        </div>
		                    </div><!-- end ibox-content -->
						</div><!-- end ibox float-e-margins -->
					</div>
				</div>
			</div>
		</div><!-- end wrapper wrapper-content -->
		<jsp:include page="../common/footer.jsp" />
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

<script src="/dashbd/resources/app-js/apps/log_mgmt.js"></script>
   	
<!-- Data picker -->
<script src="/dashbd/resources/newPublish/js/plugins/datapicker/bootstrap-datepicker.js"></script>
   
<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('LOG_MGMT');
		
		$('#data_1.input-group.date').datepicker({
            todayBtn: "linked",
            keyboardNavigation: false,
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });
		$('#data_2.input-group.date').datepicker({
            todayBtn: "linked",
            keyboardNavigation: false,
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });
	});
</script>
</body>
</html>
