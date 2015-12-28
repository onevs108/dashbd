<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Main</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="css/plugins/chartist/chartist.min.css" rel="stylesheet">

	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="js/plugins/chartist/chartist.min.js"></script>
	
	
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
		font-size: 10px;
		font-weight: bold;
		text-align: center;
		width: 40px;     
		border: 1px solid black;
		white-space: nowrap;
	}
	</style>
 
</head>
<body>
<div id="wrapper">

    <!-- sidebar -->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        Logo
                    </div>
                    <div class="logo-element">
                        logo
                    </div>
                </li>
                <li>
                    <a href="user_mgmt.html"><i class="fa fa-user"></i> <span class="nav-label">User Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-lock"></i> <span class="nav-label">Permission Mgmt</span></a>
                </li>
                <li>
                    <a href="contents_mgmt.html"><i class="fa fa-file-text-o"></i> <span class="nav-label">Contents Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-bullhorn"></i> <span class="nav-label">Operator Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-flag"></i> <span class="nav-label">BM-SC Mgmt</span></a>
                </li>
                <li>
                    <a href="service_area_mgmt.html"><i class="fa fa-globe"></i> <span class="nav-label">Service Area Mgmt</span></a>
                </li>
                <li>
                    <a href="/dashbd/view/schdMgmt.do"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
                    <!-- 
                    <a href="schedule_mgmt_eepg.html"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
                     -->
                </li>
            </ul>
        </div>
    </nav><!-- sidebar end -->

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">

        <!-- content header -->
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top white-bg" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-success " href="#"><i class="fa fa-bars"></i> </a>
                    <form role="search" class="navbar-form-custom" action="search_results.html">
                        <div class="form-group">
                            <input type="text" placeholder="Search" class="form-control" name="top-search" id="top-search">
                        </div>
                    </form>
                </div>
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
                </ul>
            </nav>
        </div><!-- content header end -->

        <!-- content body -->
        <div class="wrapper wrapper-content">

            <!-- User Mgmt -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Service Area Mgmt</h5>
                            <div class="ibox-tools">
                                <!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a-->
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="row">
                                <div class="col-sm-3 m-b-sm">
                                    <select class="input-sm form-control input-s-sm" id="operator">
                                        <option value="">Select Operator</option>
                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx">
										<option value="${operatorList.id }">${operatorList.name }</option>
										</c:forEach>
                                    </select>
                                </div>
                                <div class="col-sm-3">
                                    <select class="input-sm form-control input-s-sm" id="bmsc">
                                        <option value="">Select BM-SC</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                            <div class="col-sm-9">
                            <div class="google_map" id="map" style="height:550px;"></div>
                            </div>
                            <div class="col-sm-3">
                                    <h5>Select Service Area</h5>
                                    <ul class="service_area_box" id="service_area">
                                        
                                    </ul>
                            </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Information Of Service Area -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Information Of Service Area <span id="schedule_summary_service_area_id"></span></h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="close-link">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="row" id="schedule_summary">
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <h5>View</h5>
                                    <div class="contents-box">
                                        <div id="ct-chart3" class="ct-perfect-fourth"></div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <h5>Bandwidth is being used</h5>
                                    <div class="contents-box">
                                        <div id="ct-chart5" class="ct-perfect-fourth"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>BM-SC</h5>
                        </div>
                        <div class="ibox-content">
                            Graph Here
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>CMS</h5>
                        </div>
                        <div class="ibox-content">
                            Graph Here
                        </div>
                    </div>
                </div>
                <!--div class="col-md-6 col-lg-3">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Notify Server</h5>
                        </div>
                        <div class="ibox-content">
                            Graph Here
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Portal</h5>
                        </div>
                        <div class="ibox-content">
                            Graph Here
                        </div>
                    </div>
                </div-->
            </div>

        </div><!-- content body end -->
    </div><!-- content end -->

</div><!-- wrapper end -->

</body>
</html>