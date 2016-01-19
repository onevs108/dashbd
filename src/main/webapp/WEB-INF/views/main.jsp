<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>main</title>
	
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
	
	<link href="css/animate.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="css/plugins/footable/footable.core.css" rel="stylesheet">

    <!-- Mainly scripts -->
    <script src="js/jquery-2.1.1.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- FooTable -->
    <script src="js/plugins/footable/footable.all.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="js/inspinia.js"></script>
	<script src="js/plugins/pace/pace.min.js"></script>
	
	<script src="js/common.js"></script>
	
	<!-- Page-Level Scripts -->
	<script>
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
		});
	</script>
    	
	<script src="app-js/config.js"></script>
	<script src="app-js/apps/svc_main.js"></script>
	
	<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
	<script src="js/markerwithlabel.js"></script>
	<script src="app-js/apps/svc_main_map.js"></script>
        
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
	</style>
</head>

<body>
<div id="wrapper">
	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<ul class="nav metismenu" id="side-menu">
				<li class="nav-header">
					<div class="dropdown profile-element">
						<a href="/dashbd/resources/main.do"><img src="img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="img/logo2.png">
					</div>
				</li>
				<li>
					<a href="/dashbd/resources/user.do"><i class="fa fa-user"></i> <span class="nav-label">User Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/permission.do"><i class="fa fa-lock"></i> <span class="nav-label">Permission Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/contents_mgmt.html"><i class="fa fa-file"></i> <span class="nav-label">Contents Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/operator.do"><i class="fa fa-envelope"></i> <span class="nav-label">Operator Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/BMSCManagement.html"><i class="fa fa-flag"></i> <span class="nav-label">BM-SC Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/serviceArea.do"><i class="fa fa-globe"></i> <span class="nav-label">Service Area  Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/eNBMgmt.do"><i class="fa fa-puzzle-piece"></i> <span class="nav-label">eNB Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/view/schdMgmt.do"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
				</li>
			</ul>
		</div>
	</nav>

	<div id="page-wrapper" class="gray-bg">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header">
					<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
					<form role="search" class="navbar-form-custom" action="search_results.html">
						<div class="form-group">
							<input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
						</div>
					</form>
				</div><!-- end navbar-header -->
		        
				<ul class="nav navbar-top-links navbar-right">
					<li>
						<a>
							<i class="fa fa-user"></i>User Name
						</a>
					</li>
					<li class="dropdown">
						<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
							<i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
						</a>
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
						<a href="login.html">
							<img src="img/samsung_small.png">
						</a>
					</li>
				</ul>
			</nav>
		</div><!-- end border-bottom -->

		<div class="wrapper wrapper-content">
			<!-- User Mgmt -->
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div>
								<h2>Service Area Mgmt</h2>
							</div>
							<br>
							<div class="row">
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label" for="status">Operator</label>
										<select name="operator" id="operator" class="form-control" >
											<option value=''></option>
	                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx">
											<option value="${operatorList.id }">${operatorList.name }</option>
											</c:forEach>
										</select>
									</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label class="control-label" for="status">BM-SC</label>
											<select name="bmsc" id="bmsc" class="form-control">
											</select>
										</div>
									</div>
								</div>
	
								<div class="row">
									<div class="col-sm-8">
										<div class="google-map" id="map" style="height:535px;"></div>
									</div>
									<div class="col-sm-4">
										<div class="ibox float-e-margins" id="service_area">
											<div class="ibox-title">
												<h5>Service Area</h5>
											</div>
											<div class="ibox-content">
												<table class="footable table table-stripped toggle-arrow-tiny" data-page-size="10">
													<thead>
														<tr>
															<th>SA_ID</th>
															<th>Description</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
													<tfoot>
													</tfoot>
												</table>
											</div><!-- end ibox-content -->
										</div><!-- end ibox float-e-margins -->
									</div>
								</div>
							</div><!-- end ibox-content -->
						</div><!-- end ibox float-e-margins -->
					</div>
				</div>
				<!-- end User Mgmt -->
        
				<!-- Contents Being Serviced -->
				<div class="row">
					<div class="col-md-8">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h3>Contents Being Serviced <span id="schedule_summary_service_area_id"></span></h3>
								<div class="ibox-tools">
									<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
									<a class="close-link"><i class="fa fa-times"></i></a-->
								</div>
							</div><!-- end ibox-title -->
							
							<div class="ibox-content">
	                            <div class="row">
	                            	<div class="col-lg-12" id="schedule_summary">
	                            		<div class="nothumbnail">
	                                    	<p>
	                                        	<i class="fa fa-search"></i> No Service is available<br/>
	                                        </p>
	                                        <small></small>
	                                    </div>
	                            	</div>
	                            </div>
	                        </div><!-- end ibox-content -->
						</div>
					</div>
					<!-- Bandwidth -->
					<div class="col-md-4">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h3>Bandwidth</h3>
								<div class="ibox-tools">
									<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
									<a class="close-link"><i class="fa fa-times"></i></a-->
								</div>
							</div><!-- end ibox-title -->
							<div class="ibox-content">
								<div class="row">
									<div class="col-xs-12" id="bandwidth">
										<h2>0% is being used</h2>
										<div class="progress progress-big">
											<div style="width:0%;" class="progress-bar"></div>
										</div>
									</div>
								</div>
							</div><!-- end ibox-content -->
						</div>
					</div>
					<!-- end Bandwidth -->
					<!-- BM-SC Interface Status  -->
                <div class="col-md-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h3>BM-SC Interface Status</h3>
                            <div class="ibox-tools">
                                <!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a-->
                            </div>
                        </div><!-- end ibox-title -->
                        <div class="ibox-content">
                            <div class="row">
						<div class="col-md-12">	
							<table class="table2">
							<tbody>
								<tr>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#1</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-danger" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#2</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#3</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#4</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#5</h4>
									</td>
								</tr>
								<tr>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#6</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-danger" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#7</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#8</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#9</h4>
									</td>
									<td>
										<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
										<h4 class="text-center">#10</h4>
									</td>
								</tr>
							<tbody>
							</table>
						</div>
                            </div>
                        </div><!-- end ibox-content -->                        
                    </div>
                </div>
                <!-- end BM-SC Interface Status  -->
				</div> 
				<!-- end Contents Being Serviced -->
		</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

</body>
</html>
