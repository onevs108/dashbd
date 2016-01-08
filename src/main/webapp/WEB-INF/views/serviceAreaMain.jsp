<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Create New Content</title>

    <link href="/dashbd/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/dashbd/resources/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/dashbd/resources/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/css/style.css" rel="stylesheet">
    
    <!-- FooTable -->
    <link href="/dashbd/resources/css/plugins/footable/footable.core.css" rel="stylesheet">
    
    <!-- Sweet Alert -->
    <link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

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
					<a href="/dashbd/resources/user_mgmt.html"><i class="fa fa-user"></i> <span class="nav-label">User Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/PermissionMgmt.html"><i class="fa fa-lock"></i> <span class="nav-label">Permission Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/contents_mgmt.html"><i class="fa fa-file"></i> <span class="nav-label">Contents Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/OperatorMgmt.html"><i class="fa fa-envelope"></i> <span class="nav-label">Operator Mgmt</span></a>
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
                    <a href="login.html">
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


	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-12">
			<h2>Service Area Mgmt</h2>
			<ol class="breadcrumb">
			    <li>
				   <a href="index.html">Home</a>
			    </li>
			    <li class="active">
				   <strong>Service Area Mgmt</strong>
			    </li>
			</ol>
		</div>
	</div><!-- end row wrapper border-bottom white-bg page-heading -->
            
            
	<div class="wrapper wrapper-content">
	
		<!-- User Mgmt -->
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
                        <div class="row">
                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label" for="status">Operator</label>
                                        <select name="status" id="operator" class="form-control">
                                    	<option value=""></option>
                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx">
										<option value="${operatorList.id }">${operatorList.name }</option>
										</c:forEach>
                                    </select>
                                    </div>
                                </div>
						  
                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label" for="status">BM-SC</label>
                                        <select name="status" id="bmsc" class="form-control">
                                    </select>
                                    </div>
                                </div>
                                <div class="col-sm-4">
						  	<div class="form-group">
                                        <label class="control-label" for="status"></label>
                                        <p style="margin-top:5px">
                                        <button type="button" class="btn btn-block btn-default btn-sm demo1" id="btn-add-service-area">Create New Service Area</button>
                                        </p>
                                    </div>
                                </div>
						</div>
                        
                        <div class="row">
                        	<div class="col-sm-8">
                            	<div class="google-map" id="map"></div>
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
                                                <tr>
                                                    <td></td>
                                                    <td></td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="2">
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div><!-- end ibox-content -->
                                </div><!-- end ibox float-e-margins -->
                            </div>
					<div class="col-sm-12">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-content"><br>
							 	<form method="get" class="form-horizontal">
								  <div class="form-group"><label class="pull-left" style="padding:7px 0 0 10px">eNB ID</label>
									 <div class="col-sm-7">
										<div class="input-group"><input type="text" class="form-control" id="toAddENBs" name="toAddENBs">
										<input type="hidden" class="form-control" id="toAddENBsBmscId" name="toAddENBsBmscId" value="">
										<input type="hidden" class="form-control" id="toAddENBsServiceAreaId" name="toAddENBsServiceAreaId" value="">
											<span class="input-group-btn">
												<button type="button" class="btn btn-primary" onclick="javascript:addToServiceAreaManually();" id="toAddENBsBtn">ADD to Service Area</button>
											</span>
										</div>
									 </div>
								  </div>
								</form>
                                    </div><!-- end ibox-content -->
                                </div><!-- end ibox float-e-margins -->
                            </div>
					   
					   <div class="col-sm-12" style="margin-top:-30px;">
                                <div class="ibox float-e-margins">
                                	<div class="ibox-content" id="enb_table"><br>
                                        <table class="footable table table-stripped" data-page-size="10">
                                            <thead>
                                                <tr>
                                                    <th class="col-sm-1">eNB ID</th>
                                                    <th class="col-sm-3">eNB Name</th>
										  			<th class="col-sm-1">eNB ID</th>
                                                    <th class="col-sm-3">eNB Name</th>
										  			<th class="col-sm-1">eNB ID</th>
                                                    <th class="col-sm-3">eNB Name</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td></td>
                                                    <td></td>
										  			<td></td>
                                                    <td></td>
										  			<td></td>
                                                    <td></td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="6">
                                                    </td>
                                                </tr>
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
		<!-- More info -->
		<!-- 
                <div class="col-lg-7">
                        <div class="ibox-content">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th class="text-center">item</th>
                                    <th class="text-center">Option</th>
                                    <th class="text-center">Remark</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="text-center text-danger">PLMN 정보</td>
                                    <td class="text-center text-danger">Mandatory</td>
                                    <td class="text-danger">MCC, MNC</td>
                                </tr>
                                <tr>
                                    <td class="text-center text-danger">Circle</td>
                                    <td class="text-center text-danger">Optioanl</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td class="text-center text-danger">Circle명</td>
                                    <td class="text-center text-danger">Optioanl</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center">cluster ID</td>
                                    <td class="text-center">Optioanl</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center">위도, 경도</td>
                                    <td class="text-center">Mandatory</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center">eNB ID</td>
                                    <td class="text-center">Mandatory</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center">eNB IP</td>
                                    <td class="text-center">Optioanl</td>
                                    <td>NE IP (eNB OAP IP), M1 IP (multicast ID)</td>
                                </tr>
						  <tr>
                                    <td class="text-center">EARFCN</td>
                                    <td class="text-center">Optioanl</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center text-danger">MBSFN</td>
                                    <td class="text-center text-danger">Mandatory</td>
                                    <td></td>
                                </tr>
						  <tr>
                                    <td class="text-center text-danger">MBMS Service AreA id</td>
                                    <td class="text-center text-danger">Mandatory</td>
                                    <td></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                </div>
         -->
		<!-- end More info -->
	   
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->

</div><!-- end wrapper -->
    
</body>
</html>
