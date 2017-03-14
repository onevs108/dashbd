<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>System Mgmt</title>
	
	<link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet">
	<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
	<link href="../resourcesRenew/css/plugins/iCheck/custom.css" rel="stylesheet">
	<link href="../resourcesRenew/css/animate.css" rel="stylesheet">
	<link href="../resourcesRenew/css/style.css" rel="stylesheet">
	<link href="../resourcesRenew/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="../resourcesRenew//css/plugins/footable/footable.core.css" rel="stylesheet">
	
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
	<script src="/dashbd/resources/js/common.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			getMenuList('SYSTEM_CONF_MGMT');
			
			if('${sessionCntsessionHostNameCnt}' == 'nexdream'){
				
			}else{
				$("#viewApp1").show();
				$("#viewApp2").hide();
				$("#appView1").css("opacity", "1");
				$("#appView2").css("opacity", "0.6");
				
				$('#tomCheck1').prop("checked", true);
				$('#dbCheck1').prop("checked", true);
				$('#tomCheck1').removeAttr("disabled");
				$('#dbCheck1').removeAttr("disabled");
				
				$('#tomCheck2').attr("disabled", true);
				$('#dbCheck2').attr("disabled", true);
				$("#tomCheck2").prop("checked",false);
				$("#dbCheck2").prop("checked",false);
			}
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
 	.custom-table{
 	}
 	.custom-table .checkbox label::before {
	    content: "";
	    display: inline-block;
	    position: absolute;
	    width: 17px;
	    height: 17px;
	    left: 0;
	    margin-left: -20px;
	    border: 1px solid #cccccc;
	    border-radius: 50%;
	    background-color: #fff;
	    -webkit-transition: border 0.15s ease-in-out;
	    -o-transition: border 0.15s ease-in-out;
	    transition: border 0.15s ease-in-out;
	}
	.custom-table .checkbox label::after {
	    display: inline-block;
	    position: absolute;
	    content: " ";
	    width: 11px;
	    height: 11px;
	    left: 3px;
	    top: 3px;
	    margin-left: -20px;
	    border-radius: 50%;
	    background-color: #555555;
		-webkit-transform: scale(.6, .6);
		-ms-transform: scale(.6, .6);
		-o-transform: scale(.6, .6);
		transform: scale(.6, .6);
		-webkit-transition: -webkit-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		-moz-transition: -moz-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		-o-transition: -o-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		transition: transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
	}
	.custom-table input[type="checkbox"] + label::after {
	    background-color: #d9534f;

	    border-radius:0;
	}
	.custom-table input[type="checkbox"]:checked + label::after {
		background-color: #0066FF;
		border-radius:50%;
		-webkit-transform: scale(1, 1);
		-ms-transform: scale(1, 1);
		-o-transform: scale(1, 1);
		transform: scale(1, 1);
	}
	.checkbox input[type="checkbox"]:checked + label::after {
		content:"";
	}
	.checkbox input[type="checkbox"]:checked + label::after, .checkbox input[type="radio"]:checked + label::after {
	    font-family: "FontAwesome";
	    content: "";
	}
 	.checkbox input[type="checkbox"]:checked + label::after, .checkbox input[type="radio"]:checked + label::after{
 	}
    </style>
</head>
<body>
<div id="wrapper">
	<jsp:include page="common/leftTab.jsp" />
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
					                <table class="table2 custom-table" style="text-align: center;width:100%;">
					                	<thead>
					                		<tr height="70px;">
												<th width="20%"></th>
												<th width="15%" style="text-align: center;">Active Server</th>
												<th width="20%"></th>
												<th width="15%" style="text-align: center;">Standby Server</th>
												<th width="20%"></th>
											</tr>
					                	</thead>
										<tbody>
											<tr>
												<td width="20%"></td>
												<td width="10%" id="appView1" style="text-align:center;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-center" style="height:20px;">Tomcat Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="tomCheck1" name="tomCheck1" value="1" onclick="checkTomcat(1);" disabled/>
														<label></label>
													</div>
													</h4>
													<br/><br/>
													<img src="img/mysql2.png" width="50px">
													<h4 class="text-center" style="height:20px;">MySQL Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="dbCheck1" name="dbCheck1" value="1" onclick="checkDatabase(1);" disabled/>
														<label></label>
													</div>
													</h4>
												</td>
												<td width="20%">
													<div id="viewApp1">
														Turn Over <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer(2, 1)">▷▶</button>
													</div>
													<div id="viewApp2">
														Turn Over <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer(1, 2)">◀◁</button>
													</div>
												</td>
												<td width="10%" id="appView2" style="text-align:center;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-center" style="height:20px;">Tomcat Status
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="tomCheck2" name="tomCheck2" value="2" onclick="checkTomcat(2);" disabled/>
														<label></label>
													</div>
													</h4>
													<br/><br/>
													<img src="img/mysql2.png" width="50px">
													<h4 class="text-center" style="height:20px;">MySQL Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="dbCheck2" name="dbCheck2" value="2" onclick="checkDatabase(2);" disabled/>
														<label></label>
													</div>
													</h4>
												</td>
												<td width="20%"></td>
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
	
