<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%= new java.util.Date() %>" />

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
											    <li class="active" name="tab1"><a href="javascript:void(0);" data-toggle="tab" onclick="tabChange('1')">Operator Log</a></li>
											    <li name="tab2"><a href="javascript:void(0);" data-toggle="tab" onclick="tabChange('2')">Service Area</a></li>
											    <li name="tab3"><a href="javascript:void(0);" data-toggle="tab" onclick="tabChange('3')">Schedule</a></li>
											    <li name="tab4"><a href="javascript:void(0);" data-toggle="tab" onclick="tabChange('4')">DB Backup</a></li>
											</ul>
											<div class="tab-content">	
												<div class="panel-body">
													<form class="form-horizontal" id="logForm" action="javascript:void(0);">
														<input type="hidden" id="tabDiv" name="tabDiv" value="1">
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">From</label>
																	<div class="col-sm-4">
																		<div class="input-group date" id="data_1">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateFrom" name="searchDateFrom" value='<fmt:formatDate pattern="MM/dd/yyyy" value="${now}" />'>
										                                </div>
																	</div>
																	<label class="col-sm-2 control-label">To</label>
																	<div class="col-sm-4">
																		<div class="input-group date" id="data_2">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateTo" name="searchDateTo" value='<fmt:formatDate pattern="MM/dd/yyyy" value="${now}" />'>
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
																			<input type="text" id="searchKeyword" name="searchKeyword" class="form-control" onkeydown="javascript:if(event.keyCode == 13) selectLogData();">
																			<span class="input-group-btn">
																				<button type="button" onclick="selectLogData()" class="btn btn-primary">Search</button>
																			</span>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</form>		
													<div class="row col-xs-6" style="height:500px; overflow:auto">
														<div id="tab-body" class="tab-pane active">
															<ul style="padding: 0 0 0 0;">
																<c:forEach var="obj" items="${resultList}" varStatus="status">
																	<li>${obj.reqMsg}</li>
																</c:forEach>
															</ul>
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
	
</script>
</body>
</html>
