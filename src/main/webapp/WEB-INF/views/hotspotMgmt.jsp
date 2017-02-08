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
    <link href="/dashbd/resources/css/plugins/select2/select2.min.css" rel="stylesheet">
    
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
	<script src="/dashbd/resources/js/jquery.cookie.js"></script>
	<script src="/dashbd/resources/js/bootstrap.min.js"></script>
	<script src="/dashbd/resources/js/bootstrap-table.js"></script>
	<script src="/dashbd/resources/js/plugins/select2/select2.full.min.js"></script>
	<script src="/dashbd/resources/app-js/apps/hotspot_mgmt_main_map.js"></script>
    
    <script src="js/common.js"></script>
    
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('HOTSPOT_MGMT');
			$(".js-example-basic").on("select2:select", function (e) { alert("하이"); return false;})
			$(".js-example-basic").select2();
		});
	</script>
    
	<style type="text/css">
	.google-map {
	    height: 400px;
	}
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
					<h2 style="margin-left: 15px;"><strong>Hot Spot Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Hot Spot Mgmt</strong>
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
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
                        <div class="row">
                        	<div class="col-sm-3" style="padding-bottom: 5px;">
                        		<select id="selectCircle" class="input-sm form-control input">
                                    <option value="">Select Circle</option>
                                    <c:forEach var="row" items="${circleList}">
                                    	<option value="${row.circle_id}^${row.circle_name}^${row.latitude}^${row.longitude}">${row.circle_name}</option>
                                    </c:forEach>
                                </select>
							</div>
                        	<div class="col-sm-3" style="padding-bottom: 5px;">
                        		<select id="selectCity" class="input-sm form-control input" style="display: none;">
                                </select>
							</div>
							<div class="col-sm-12" style="min-h">
								<div class="google-map" id="map"></div><br>
							</div>
                            <div class="col-sm-12">
                                <div class="ibox float-e-margins" id="service_area">
                                    <table class="footable table table-stripped toggle-arrow-tiny" style="margin:0;" id="table">
                                    
                                    </table>
                                    <table class="footable table table-stripped toggle-arrow-tiny" style="margin:0; display: none;" id="addTable">
                                    	<tr>
                                    		<td width="30%"></td>
                                    		<td width="10%"></td>
                                    		<td width="20%"></td>
                                    		<td width="20%"></td>
                                    		<td width="20%" style="text-align: right;">
                                    			<button id="addHostSpot" type="button" class="btn btn-success btn-xs button-edit">Add</button>
                                    		</td>
                                    	</tr>
                                    </table>
                                </div><!-- end ibox float-e-margins -->
                            </div>
                        </div>
                    </div><!-- end ibox-content -->
				</div><!-- end ibox float-e-margins -->
			</div>
		</div>
	   
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->

</div><!-- end wrapper -->
<input type="hidden" id="cityId">
<input type="hidden" id="cityName">
<input type="hidden" id="cityLatitude">
<input type="hidden" id="cityLongitude">
<div class="modal fade" id="addHotSpotModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h5 class="modal-title">Add Hot Spot</h5>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
				<input type="hidden" placeholder="" class="form-control" id="editType">
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Name of the Hotspot</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="hotSpotName"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Longitude</label>
				<div class="col-lg-6"><input type="text" placeholder="" class="form-control" id="longitude"></div>
				<div class="col-lg-2"><button type="button" class="btn btn-primary0 btn-sm btn-white" type="text" id="reset">Reset</button></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Latitude</label>
				<div class="col-lg-6"><input type="text" placeholder="" class="form-control" id="latitude"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Bandwidth</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="bandwidth"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> SAID</label>
				<div class="col-lg-6"><input type="text" placeholder="" class="form-control" id="hotSpotId"></div>
				<div class="col-lg-2"><button type="button" class="btn btn-primary0 btn-sm btn-blue" onclick="existSAID('hotspot')">Check</button></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Description</label>
				<div class="col-lg-8"><textarea type="text" placeholder="" class="form-control" id="description" style="height: 150px;"></textarea></div>
				</div>
				<br>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary btn-sm btn-white" data-dismiss="modal">Cancle</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="addHotSpotBtn">OK</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="editHotSpotBtn">OK</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<jsp:include page="common/setLocationModal.jsp" />
</body>
</html>
