<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BM-SC Management</title>

    <link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet">
    <link href="../resourcesRenew/css/style.css" rel="stylesheet">
    <link href="../resourcesRenew/css/animate.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="../resourcesRenew/css/custom.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/jquery.cookie.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-table.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="/dashbd/resources/app-js/apps/svc_systemDb.js"></script>
	<script src="js/common.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('SYSTEM_DB_MGMT');
			getDatabaseList();
		});
	</script>
	
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
        <!-- content header -->
        <div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header" style="padding-bottom: 10px;">
					<h2 style="margin-left: 15px;"><strong>System Mgmt - DB Backup & Restore</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong>System Mgmt / DB Backup & Restore</strong>
					</span>
				</div><!-- end navbar-header -->
		        
				<ul class="nav navbar-top-links navbar-right">
					<li>
						<a>
							<i class="fa fa-user"></i><span id="navbar-user-name"></span>
						</a>
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
		</div><!-- end border-bottom -->
		<!-- content body -->
        <div class="wrapper wrapper-content">
			
            <!-- Operator Mgmt -->
            <div class="row">
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
					            <div class="col-lg-4">
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
								</div>
	                            <div class="col-lg-4">
	                                <button type="button" class="btn btn-primary pull-right" onclick="openBackupModal();">
										Backup Database
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

</body>
</html>
