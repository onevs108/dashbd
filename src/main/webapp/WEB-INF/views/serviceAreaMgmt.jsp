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
        
	<script src="js/common.js"></script>
    
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('SERVICE_AREA_MGMT');
		});
	</script>
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
            </ul>
        </div>
    </nav><!-- sidebar end -->

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">

        <!-- content header -->
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
