<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Schedule Mgmt</title>
	
	<link href="../resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="../resources/font-awesome/css/font-awesome.css" rel="stylesheet">
	
	<link href="../resources/css/animate.css" rel="stylesheet">
	<link href="../resources/css/style.css" rel="stylesheet">
	<link href="../resources/css/custom.css" rel="stylesheet">
	<link href="../resourcesRenew/css/timetable/timetablejs.css" rel="stylesheet" >
	<link href="../resourcesRenew/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="../resources/css/plugins/footable/footable.core.css" rel="stylesheet">

    <!-- Mainly scripts -->
    <script src="../resources/js/jquery-2.1.1.js"></script>
    <script src="../resources/js/bootstrap.min.js"></script>
    <script src="../resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="../resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="../resourcesRenew/js/plugins/sweetalert/sweetalert.min.js"></script>

    <!-- FooTable -->
    <script src="../resources/js/plugins/footable/footable.all.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="../resources/js/inspinia.js"></script>
	<script src="../resources/js/plugins/pace/pace.min.js"></script>
	
	<script src="../resources/js/common.js"></script>
	
	<!-- Page-Level Scripts -->
	<script>
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			
			getMenuList('SCHEDULE_MGMT');
			
		});
	</script>
    	
    <script src="../resourcesRenew/js/plugins/fullcalendar/moment.min.js"></script>
	<script src="../resources/app-js/config.js"></script>
	<script src="../resourcesRenew/js/timetable/timetable.min.js"></script>
	
	
	
	<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
	<script src="../resources/js/markerwithlabel.js"></script>
	<script src="../resources/app-js/apps/svc_schd.js"></script>
	<script src="../resources/app-js/apps/svc_main_forSchd.js"></script>
	<script src="../resources/app-js/apps/svc_main_map_forSchd.js"></script>
	
        
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
						<a href="/dashbd/resources/main.do"><img src="/dashbd/resources/img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="/dashbd/resources/img/logo2.png">
					</div>
				</li>
			</ul>
		</div>
	</nav>

	<div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header" style="padding-bottom: 10px;">
					<h2 style="margin-left: 15px;"><strong>Schedule Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Schedule Mgmt</strong>
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
						<img src="../resources/img/samsung_small.png">
					</li>
				</ul>
			</nav>
		</div><!-- end border-bottom -->
        
<!--         <div class="row wrapper border-bottom white-bg page-heading"> -->
<!-- 			<div class="col-lg-12"> -->
<!-- 				<h2><strong>Schedule Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>Schedule Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->

		<div class="wrapper wrapper-content">
		<input type="hidden" id="searchDate" name="searchDate" value="${searchDate}">
			<!-- Schedule Mgmt -->
			<div class="row" id="viewProgram">
				<div class="col-md-12 ">
					<div class="ibox float-e-margins ibox-title">
						<div class="ibox-title">
							<div>
								<h5>Regional Schedule</h5>
								<button type="button" class="btn btn-primary btn-xs" id="btnScheduleDetail" style="float: right;">Create Regional Schedule</button>
								<input type="radio" class="btn btn-primary btn-xs" name="radio" value="area" style="margin-left: 20px;" checked/>Service Area&nbsp;
								<input type="radio" class="btn btn-primary btn-xs" name="radio" value="group"/>Service Area Group&nbsp;
							</div>
						</div>
						<div class="col-sm-3">
                       		<select id="selectCircle" class="input-sm form-control input">
                               <option value="">Select Circle</option>
                               <c:forEach var="row" items="${circleList}">
                               	<option value="${row.circle_id}^${row.circle_name}">${row.circle_name}</option>
                               </c:forEach>
                            </select>
						</div>
						<div class="col-sm-3">
							<select id="selectCity" class="input-sm form-control input">
                               
                            </select>
                        </div>
						<div class="col-sm-3">
							<select id="selectHotspot" class="input-sm form-control input">
                               
                            </select>
                        </div>
						<div class="ibox-content">
                            <div class="row">
                            	<div class="col-lg-12">
                            	<div class="eepg_timeline">
	                                <div class="timetable"></div>
	                            </div>
                            	</div>
                            </div>
                        </div><!-- end ibox-content -->
					</div>
				</div>
			</div>
			<!-- end Contents Being Serviced -->
		</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

</body>
</html>
