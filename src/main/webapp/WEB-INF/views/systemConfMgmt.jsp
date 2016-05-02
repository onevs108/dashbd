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
	<script src="/dashbd/resources/app-js/apps/svc_systemConf.js"></script>
	<script src="../resources/js/common.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			
			getMenuList('SYSTEM_CONF_MGMT');
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
					<h2 style="margin-left: 15px;"><strong>System Mgmt - System Config</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> System Mgmt / System Config</strong>
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
					        <h5>System Config</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <div class="ibox-content">
					    	<div class="row">
					            <div class="col-sm-12" style="width:100%">
					                <table class="table2" style="text-align: center;width:100%;">
										<tbody>
											<tr>												
												<td width="30%" id="appView1" style="text-align:right;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-right" style="height:20px;">Tomcat Status 
													<button class="btn btn-sm button-edit" type="button" onclick="editTomcat('1')">
														<i class="fa fa-edit"></i>
													</button>
													<input type="radio" id="tomRadio" name="tomRadio" value="1" checked="checked" /></h4>
													<h4 class="text-right" style="height:20px;">MySQL Status 
													<button class="btn btn-sm button-edit" type="button" onclick="editDbStatus('1')">
														<i class="fa fa-edit"></i>
													</button>
													<input type="radio" id="dbRadio" name="dbRadio" value="1" checked="checked" /></h4>
												</td>
												<td width="20%">
													<div id="viewApp1">
														Move the Activer Server to APP#2 <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer('2', '1')">▷▶</button>
													</div>
													<div id="viewApp2" style="display:none;">
														Move the Activer Server to APP#1 <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer('1', '2')">◀◁</button>
													</div>
												</td>
												<td width="30%" id="appView2" style="text-align:left;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-left" style="height:20px;">Tomcat Status 
													<button class="btn btn-sm button-edit" type="button" onclick="editTomcat('2')">
														<i class="fa fa-edit"></i>
													</button>
													<input type="radio" id="tomRadio" name="tomRadio" value="2" /></h4>
													<h4 class="text-left" style="height:20px;">MySQL Status 
													<button class="btn btn-sm button-edit" type="button" onclick="editDbStatus('1')">
														<i class="fa fa-edit"></i>
													</button>
													<input type="radio" id="dbRadio" name="dbRadio" value="2" /></h4>
												</td>
											</tr>
										</tbody>
									</table>
									<div class="hr-line-dashed"></div>
					            </div>
					         </div>
				        </div>
					</div>
				</div>
			</div>
		</div>	
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

</body>
</html>
	
