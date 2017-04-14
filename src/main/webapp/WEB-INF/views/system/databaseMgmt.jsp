<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<title>SeSM Database Management</title>	
	<jsp:include page="../common/head.jsp" />
	
<!--     <link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/style.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/animate.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/plugins/toastr/toastr.min.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/custom.css" rel="stylesheet"> -->
<!--     <link href="font-awesome/css/font-awesome.css" rel="stylesheet"> -->
	
	<style type="text/css">
		td {
			font-size: 13px;
		}
		.th-inner {
			font-size: 13px;
		}
	</style>

</head>
<body>
<div id="wrapper">
	<jsp:include page="../common/leftTab.jsp" />
    <div id="page-wrapper" class="gray-bg">
    	<c:import url="/resources/header.do"></c:import>
		<!-- content body -->
        <div class="wrapper wrapper-content">
			
            <!-- Operator Mgmt -->
            <div class="row"> 
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                	<div class="ibox-title">
							<h5>Database Backup</h5>
						</div>
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
	                    		<label class="col-lg-3" style="">Database Auto backup</label>
	                    		<div class="col-lg-4 swich">
	                                <div class="onoffswitch">
	                                    <input type="checkbox" class="onoffswitch-checkbox" id="autoBackup" name="autoBackup" onchange="autoBackupChange()">
	                                    <label class="onoffswitch-label" for="autoBackup">
	                                        <span class="onoffswitch-inner"></span>
	                                        <span class="onoffswitch-switch"></span>
	                                    </label>
	                                </div>
                                </div>
	                    	</div>
	                    	<div class="row" style="padding-top:20px">
	                    		<label class="col-lg-3" style="">Database Auto backup Schedule</label>
	                    		<span id="backupSchedule" style="display: none;">
		                    		<label class="col-lg-2" style="width: 12.5%">Every day at :</label>
		                    		<div class="col-lg-2" style="margin-top: -7px;">
		                                <input type="text" class="form-control" id="backupTime" name="backupTime" value="${backupTime}" placeholder="hh:mi:ss" data-ax5formatter="time">
	                                </div>
	                                <div class="col-lg-2" style="margin-top: -5px;">
		                                <button type="button" class="btn btn-primary btn-sm" onclick="setBackupTime();">
											Set Time
		                                </button>
	                                </div>
                                </span>
	                    	</div>
	                    	<div class="row" style="padding-top:20px">
					            <!-- <div class="col-lg-4">
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
								<div class="col-lg-4">
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
								</div> -->
								<div class="col-lg-4">
	                                <h3>Backup List</h3>
	                            </div>
	                            <div class="col-lg-4 col-md-offset-4">
	                                <button type="button" class="btn btn-primary pull-right" onclick="openBackupModal();">
										Database Backup Now
	                                </button>
	                            </div>
	                        </div>
	                    	<div class="hr-line-dashed"></div>
	                    
							<div class="table-responsive">
                            	<table class="table table-bordered" id="table"></table>
                            </div>
	                    </div><!-- end ibox-content -->
	                </div>
	            </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

<script src="js/jquery.cookie.js"></script>
<script src="/dashbd/resources/app-js/apps/svc_systemDb.js"></script>
<!-- <script src="js/common.js"></script> -->

<!-- ax5ui -->
<link href="../resourcesRenew/css/plugins/ax5ui/ax5formatter.css" rel="stylesheet">
<script src="../resourcesRenew/js/plugins/ax5ui/ax5core.js"></script>
<script src="../resourcesRenew/js/plugins/ax5ui/ax5formatter.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('SYSTEM_DB_MGMT');
		getDatabaseList();
		if("${autoYN}" == "Y"){
			$("#autoBackup").prop("checked", true);
			$("#autoBackup").change();	
		}
		$('[data-ax5formatter]').ax5formatter();
	});
</script>

</body>
</html>
