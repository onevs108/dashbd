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
	<script src="/dashbd/resources/app-js/apps/svc_area_main_map.js"></script>
    
    <script src="/dashbd/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="js/common.js"></script>
    
	<script type="text/javascript">
		var circlemap = '${circlemap}';
		
		$(document).ready(function() {
			getMenuList('SERVICE_AREA_MGMT');
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
	  width:40%;
	}
	.btn-primary1 {
	  background-color: #1ab394;
	  border-color: #1ab394;
	  color: #FFFFFF;
	  width:20%;
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
					<h2 style="margin-left: 15px;"><strong>Service Area Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Service Area Mgmt</strong>
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
        
<!--         <div class="row wrapper border-bottom white-bg page-heading"> -->
<!-- 			<div class="col-lg-12"> -->
<!-- 				<h2><strong>Service Area Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>Service Area Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->
            
            
	<div class="wrapper wrapper-content">
		<!-- User Mgmt -->
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
                        <div class="row">
							<div class="col-sm-8">
								<div class="ibox-title">
									<ul class="nav nav-tabs">
									    <li class="active"><a href="#" data-toggle="tab" onclick="tabChange('1')">Tree View</a></li>
									    <li><a href="#" data-toggle="tab" onclick="tabChange('2')">Map View</a></li>
									</ul>
								</div>
								<div class="input-group" style="width:100%">
									<select id="searchType" class="input-sm form-control input search-group" style="width:20%; margin:0 0 0 0">
	                                    <option value="">선택</option>
	                                    <option value="circle">Circle</option>
	                                    <option value="city">City</option>
	                                    <option value="circleCity">Circle & City</option>
	                                </select>
									<input type="text" id="search-input" class="search-group">
									<button type="button" onclick="searchTreeNode()" class="btn btn-success btn-xs button-edit search-group">Search</button>
								</div>
								<div id="treeNode" style="height: 700px; overflow: scroll;"></div>
								<div class="google-map" id="map" style="height: 700px; display:none;"></div>
							</div>
<!--                             <div class="col-sm-4"> -->
<!--                                 <div class="ibox float-e-margins" id="service_area"> -->
<!--                                     <div class="ibox-title"> -->
<!--                                     	<button type="button" class="btn btn-primary2 btn-sm demo1" id="service-create-btn" onclick="javascript:openCreateServiceModal()">Create Service Group</button> -->
<!--                                     </div> -->
<!--                                     <div class="ibox-content" style="scroll:auto"> -->
<!--                                         <table class="table table-bordered table-hover" data-page-size="10"> -->
<!--                                         	<thead> -->
<!--                                         		<tr> -->
<!--                                         			<th>Service Area Group</th> -->
<!--                                         		</tr> -->
<!--                                         	</thead> -->
<!--                                             <tbody id="area_group"> -->
                                                
<!--                                             </tbody> -->
<!--                                         </table> -->
<!--                                     </div>end ibox-content -->
<!--                                 </div>end ibox float-e-margins -->
<!--                             </div> -->
                        </div>
                    </div><!-- end ibox-content -->
				</div><!-- end ibox float-e-margins -->
			</div>
		</div>
		<!-- end User Mgmt -->
	</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

<!-- 위치지정 모달 팝업 페이지 호출 -->
<jsp:include page="common/setLocationModal.jsp" />

</body>
</html>
