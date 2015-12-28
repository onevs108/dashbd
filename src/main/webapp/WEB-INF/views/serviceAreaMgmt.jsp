<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Service Area Mgmt</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="app-js/bower_components/js-marker-clusterer/src/markerclusterer.js"></script>
	
	<script src="app-js/config.js"></script>
	<script src="app-js/apps/svc_area_management.js"></script>
	<script src="app-js/apps/svc_area_mgmt_map.js"></script>
	
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo&callback=initMap"
        async defer></script>
</head>

<body>
<div id="wrapper">

    <!-- sidebar -->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="profile-element">
                        Logo
                    </div>
                    <div class="logo-element">
                        Logo
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
                <li class="landing_link">
                    <a href="service_area_mgmt.html"><i class="fa fa-globe"></i> <span class="nav-label">Service Area Mgmt</span></a>
                </li>
                <li>
                    <a href="schedule_mgmt_eepg.html"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
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

            <!-- Contents -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Service Area Management</h5>
                            <div class="ibox-tools">
                                <!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a-->
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="row">
                                <div class="col-sm-3 m-b-sm">
                                    <select class="input-sm form-control input-s-sm" id="plmn">
                                        <option value="">Select one</option>
                                    </select>
                                </div>
                                <div class="col-sm-3">
                                    <select class="input-sm form-control input-s-sm" id="mbsfn">
                                        <option value="">Select one</option>
                                    </select>
                                </div>
                            </div>
                            <!-- google map -->
                            <div class="google_map" id="map"></div>

                            <form method="get" class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">eNB ID</label>
                                    <div class="col-sm-10"><div class="input-group"><input type="text" id="enb_aps" name="enb_aps" class="form-control"> <span class="input-group-btn"> <button type="button" class="btn btn-success" id="btn-enb-aps">Add
                                        </button> </span></div></div>
                                </div>
                            </form>
                            <div class="row">
                                <div class="col-sm-12">
                                    <ul class="service_area_box" id="enb_ap_in_service_area">
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div><!-- content body end -->
    </div><!-- content end -->

</div><!-- wrapper end -->

<script type="text/template" id="tpl-more-info">
	<table class="info-window-table">
		<thead>
			<tr>
				<th>Item</th>
				<th>Option</th>
				<th>Remark</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>PLMN</td>
				<td><%//= plmn %></td>
				<td></td>
			</tr>
			<tr>
				<td>Latitude, Longitude</td>
				<td><%//= latitude %>, <%//= longitude %></td>
				<td></td>
			</tr>
			<tr>
				<td>eNB ID</td>
				<td><%//= enb_ap_id %></td>
				<td></td>
			</tr>
			<tr>
				<td>MBSFN</td>
				<td><%//= mbsfn %></td>
				<td></td>
			</tr>
			<tr>
				<td>MBMS Service Area ID</td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
</script>

</body>
</html>