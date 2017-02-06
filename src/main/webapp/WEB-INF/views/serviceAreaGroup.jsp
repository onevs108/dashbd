<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% request.setCharacterEncoding("utf-8"); %>

<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Create New Content</title>

    <link href="/dashbd/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/dashbd/resources/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/dashbd/resources/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/css/style.css" rel="stylesheet">
    <link href="/dashbd/resources/css/custom.css" rel="stylesheet">
    
    <!-- FooTable -->
    <link href="/dashbd/resources/css/plugins/footable/footable.core.css" rel="stylesheet">
    
    <!-- Sweet Alert -->
    <link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

    <!-- JSTree -->
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
<!--     <script src="/dashbd/resources/js/plugins/jsTree/jstree.min.js"></script> -->

    <!-- Mainly scripts -->
    <script src="/dashbd/resources/js/jquery-2.1.1.js"></script>
    <script src="/dashbd/resources/js/bootstrap.min.js"></script>
    <script src="/dashbd/resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/dashbd/resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- FooTable -->
    <script src="/dashbd/resources/js/plugins/footable/footable.all.min.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/dashbd/resources/js/inspinia.js"></script>
    <script src="/dashbd/resources/js/plugins/pace/pace.min.js"></script>

    <!-- Sweet alert -->
    <script src="/dashbd/resources/js/plugins/sweetalert/sweetalert.min.js"></script>
    
	<script src="/dashbd/resources/app-js/config.js"></script>
	
	<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
	<script src="/dashbd/resources/js/markerwithlabel.js"></script>
	<script src="/dashbd/resources/app-js/apps/svc_area_group_map.js"></script>
    
    <script src="js/common.js"></script>
    
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('SERVICE_AREA_GROUP_MGMT');
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
	.btn-primary {
	  background-color: #1ab394;
	  border-color: #1ab394;
	  color: #FFFFFF;
/* 	  width:10%; */
	}
	.btn-primary2 {
	  background-color: #1ab394;
	  border-color: #1ab394;
	  color: #FFFFFF;
	  width:50%;
	}
	.btn-primary4 {
	  background-color: #1ab394;
	  border-color: #1ab394;
	  color: #FFFFFF;
	  width:100%;
	}
	.btn-primary0 {
	  background-color: #1ab394;
	  border-color: #1ab394;
	  color: #FFFFFF;
	}
	.form-group4{
	  text-align: center; 
	  vertical-align: middle;
	  margin-top: 22px;
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
                </ul>
            </div>
        </nav>
	<div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				<div class="navbar-header" style="padding-bottom: 10px;">
					<h2 style="margin-left: 15px;"><strong>Service Area Group Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Service Area Group Mgmt</strong>
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
	<div class="wrapper wrapper-content">
		<!-- User Mgmt -->
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<div class="row" id="search-area">
                        	<div class="col-sm-4">
                                <select class="input-sm form-control input-s-sm" id="search-circle" onchange="changeCircle()">
                            		<option value="">Select Circle</option>
                            		<c:forEach var="obj" items="${circleList}" varStatus="status">
                            			<option value="${obj.circle_id}">${obj.circle_name}</option>
                            		</c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="row">
							<div class="col-sm-4">
								<div class="ibox-title">
									<h5><span id="selectedCircle"></span></h5>
									<div class="ibox-content" style="scroll:auto">
                                        <table class="table table-bordered table-hover" data-page-size="10">
                                        	<colgroup>
                                        		<col width="80%">
                                        		<col width="20%">
                                        	</colgroup>
                                            <tbody id="group_area"></tbody>
                                        </table>
                                    </div><!-- end ibox-content -->
								</div>
							</div>
                            <div class="col-sm-8">
                                <div class="ibox float-e-margins" id="service_area" style="height:750px; overflow:scroll;">
                                    <div class="ibox-title">&nbsp;</div>
                                    <div id="treeNode"></div>
                                </div><!-- end ibox float-e-margins -->
                            </div>
                        </div>
                        <div class="row">
                        	<div class="col-sm-4"></div>
                        	<div class="col-sm-8">
								<button class="btn btn-sm btn-primary proccess-btn" type="button" id="save-btn">Save</button>
<!-- 								<button class="btn btn-sm btn-default proccess-btn" type="button" id="cancel-btn">Cancel</button> -->
                        	</div>
                        </div>
                    </div><!-- end ibox-content -->
				</div><!-- end ibox float-e-margins -->
			</div>
		</div>
		<!-- end User Mgmt -->
	</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->
</body>
</html>
