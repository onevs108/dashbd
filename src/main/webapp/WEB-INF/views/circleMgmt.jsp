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
	<script src="/dashbd/resources/app-js/apps/circle_mgmt_main_map.js"></script>
    
    <script src="js/common.js"></script>
    
	<script type="text/javascript">
		var circleJson = ${circleList};
		var circleJsonLength = circleJson.length;
		$(document).ready(function() {
			getMenuList('ROLE_CIRCLE_MGMT');
		});
	</script>
    
	<style type="text/css">
	.google-map {
	    height: 700px;
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
					<h2 style="margin-left: 15px;"><strong>Circle Mgmt</strong></h2>
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
							<div class="col-sm-8" style="min-h">
								<div class="ibox-title">
									<h5><i class="fa fa-wifi"></i> <span id="selectedSvcArea"></span></h5>
								</div>
								<div class="google-map" id="map"></div><br>
								<h3 style="position:absolute;bottom:35px;left:25px;padding:5px 10px;border-radius:15px; background-color:rgba(0,0,0,0.5);box-shadow:0 0 10px #ccc;color:#fff;">
									<i class="fa fa-circle text-danger"></i> <span id="selectedENBs"></span>
								</h3><br>
							</div>
                            <div class="col-sm-4">
                                <div class="ibox float-e-margins" id="service_area">
                                    <div class="ibox-title">
                                        <h5><span id="circleTitle"></span>&nbsp;City List</h5>
                                    </div>
                                     <div class="ibox-content" style="padding-top:0;padding-bottom:0;">
                                     <table class="footable table table-stripped toggle-arrow-tiny" style="margin:0;">
                                            <thead>
                                                <tr>
                                                    <th>City Name</th>
                                                </tr>
                                            </thead>
                                    </table>
                                    </div>
                                    <div class="ibox-content" style="height:660px;padding-top:0;border-top:0;overflow-y:scroll;">
                                        <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="10">
                                            <tbody id="cityList">
                                                
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="3">
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div><!-- end ibox-content -->
                                </div><!-- end ibox float-e-margins -->
                            </div>
							<div class="col-sm-12" id="viewEnbIDAdd" style="display:none;">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-content"><br>
							 	<form method="get" class="form-horizontal">
								  <div class="form-group"><label class="pull-left" style="padding:7px 0 0 10px">eNB ID</label>
									 <div class="col-sm-7">
										<div class="input-group"><input type="text" class="form-control" id="toAddENBs" name="toAddENBs">
										<input type="hidden" class="form-control" id="toAddENBsBmscId" name="toAddENBsBmscId" value="">
										<input type="hidden" class="form-control" id="toAddENBsServiceAreaId" name="toAddENBsServiceAreaId" value="">
											<span class="input-group-btn">
												<button type="button" class="btn btn-primary4" onclick="javascript:addToServiceAreaManually();" id="toAddENBsBtn">Add</button>
											</span>
										</div>
									 </div>
								  </div>
								</form>
                                    </div><!-- end ibox-content -->
                                </div><!-- end ibox float-e-margins -->
                            </div>
					  		 <div class="col-sm-12" style="margin-top:-30px; display: none;" id="viewEnbIDList">
                                <div class="ibox ">
                                	<input type="hidden" id="checkCityName" name="checkCityName">
                                	<div class="ibox-content" id="enb_table">
                                        <table class="footable table table-stripped" data-page-size="10">
                                            <thead>
                                                <tr style="border-top-style:solid;border-top-width:1px;border-top-color:#c0c0c0;">
                                                    <th class="col-sm-1">townName</th>
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

<div class="modal fade" id="createServiceAreaLayer">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h5 class="modal-title">Create New Service Area</h5>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Service Area ID</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="serviceAreaId"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Service Area Name</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="serviceAreaName"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label">Description</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-controlr" id="serviceAreaDescription"></div>
				</div>
				<br>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary btn-sm btn-white" data-dismiss="modal">Cancle</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="createSvcAreaBtn">OK</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="addCircleModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h5 class="modal-title">Create New Circle</h5>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
				<input type="hidden" placeholder="" class="form-control" id="editType">
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Circle Name</label>
				<div class="col-lg-6"><input type="hidden" placeholder="" class="form-control" id="circleId"></div>
				<div class="col-lg-6"><input type="text" placeholder="" class="form-control" id="circleName"></div>
				<div class="col-lg-2"><button type="button" class="btn btn-primary0 btn-sm btn-blue" onclick="existCircle()">Check</button></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Longitude</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="longitude"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Latitude</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="latitude"></div>
				</div>
				<br>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary btn-sm btn-white" data-dismiss="modal">Cancle</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="addCircleBtn">OK</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="editCircleBtn">OK</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="addCityModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h5 class="modal-title">Create New Circle</h5>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
				<input type="hidden" placeholder="" class="form-control" id="editType">
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i>Name of the City</label>
				<div class="col-lg-6"><input type="hidden" placeholder="" class="form-control" id="cityId"></div>
				<div class="col-lg-6"><input type="text" placeholder="" class="form-control" id="cityName"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Longitude</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="cityLongitude"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Latitude</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="cityLatitude"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Bandwidth</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control" id="cityLatitude"></div>
				</div>
				<div class="form-group"><label class="col-lg-4 control-label"><i class="fa fa-check text-importance"></i> Bandwidth</label>
				<div class="col-lg-8"><input type="text" placeholder="" class="form-control0" id="cityLatitude"></div>
				</div>
				<br>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary btn-sm btn-white" data-dismiss="modal">Cancle</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="addCircleBtn">OK</button>
				<button type="button" class="btn btn-primary0 btn-sm btn-white" id="editCircleBtn">OK</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>
</html>
