<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>main</title>
	
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
	
	<link href="/dashbd/resources/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/css/style.css" rel="stylesheet">
    <link href="/dashbd/resources/css/custom_main.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="css/plugins/footable/footable.core.css" rel="stylesheet">
	
	<!-- Sweet Alert -->
    <link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    

    <!-- Mainly scripts -->
    <script src="js/jquery-2.1.1.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- FooTable -->
    <script src="js/plugins/footable/footable.all.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="js/inspinia.js"></script>
	<script src="js/plugins/pace/pace.min.js"></script>
	<script src="js/common.js"></script>
	<script src="js/dash.all.min.js"></script>
	
	<link href="css/plugins/c3/c3.min.css" rel="stylesheet">
	<script src="js/plugins/d3/d3.min.js"></script>
	<script src="js/plugins/c3/c3.min.js"></script>
	<!-- Page-Level Scripts -->
	
    <link href="css/plugins/chartist/chartist.min.css" rel="stylesheet">
    <script src="js/plugins/chartist/chartist.min.js"></script>
	<script>
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			
			getMenuList('MAIN');

		});
	</script>
    	
	<script src="app-js/config.js"></script>
	<script src="app-js/apps/svc_main.js"></script>
	<!-- 
	<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
	<script src="js/markerwithlabel.js"></script>
	 -->
    
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
	
	 video {
       width: 90%;
       height: 120px;
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
					<h2 style="margin-left: 15px;"><strong>eMBMS Provisioning Server </strong></h2>
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
<!-- 						<div class="ibox-title"> -->
<!-- 							<h3>Service Area Mgmt</h3> -->
<!-- 							<div class="ibox-tools"> -->
<!-- 							</div> -->
<!-- 						</div>end ibox-title -->
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label" for="status">Operator</label>
										<c:choose>
											<c:when test="${USER.grade == 0}">
												<select name="operator" id="operator" class="form-control">
													<c:forEach items="${OperatorList}" var="operator">
														<option value="${operator.id}">${operator.name}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<select name="status" id="operator" class="form-control" disabled="disabled">
													<c:forEach items="${OperatorList}" var="operator">
														<c:choose>
															<c:when test="${USER.operatorId == operator.id}">
																<option value="${operator.id}" selected="selected">${operator.name}</option>
															</c:when>
															<c:otherwise>
																<option value="${operator.id}">${operator.name}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</c:otherwise>
										</c:choose>
<!-- 										<select name="operator" id="operator" class="form-control" > -->
<!-- 											<option value=''></option> -->
<%-- 	                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx"> --%>
<%-- 											<option value="${operatorList.id }">${operatorList.name }</option> --%>
<%-- 											</c:forEach> --%>
<!-- 										</select> -->
									</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label class="control-label" for="status">BM-SC</label>
											<select name="bmsc" id="bmsc" class="form-control">
											<c:forEach var='bmscList' items="${BmscList}" varStatus="idx">
												<option value="${bmscList.id }">${bmscList.name }</option>
											</c:forEach>
											</select>
										</div>
									</div>
								</div>
	
								<div class="row">
								<!-- <div class="col-sm-8"><div class="google-map" id="map" style="height:535px;"></div></div>-->
								 
								 <div class="col-sm-8">
									<div class="ibox float-e-margins">
										<div class="ibox-title">
											<h3>Active Contents <span id="schedule_waiting_service_area_id"></span></h3>
											    	   <div>
											    	   <!-- 
									       <video data-dashjs-player autoplay src="http://vm2.dashif.org/livesim/testpic_2s/Manifest.mpd" controls></video>
											    	    -->
									   </div>
											<div class="ibox-tools">
												<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
												<a class="close-link"><i class="fa fa-times"></i></a-->
											</div>
										</div><!-- end ibox-title -->
										
										<div class="ibox-content">
				                            <div class="row">
				                            	<div class="col-lg-12" id="schedule_summary">
				                            		<div class="nothumbnail">
				                                    	<p>
				                                        	<i class="fa fa-search"></i> No Content is Service Currently<br/>
				                                        </p>
				                                        <small></small>
				                                    </div>
				                            	</div>
				                            </div>
				                        </div><!-- end ibox-content -->
									</div>
								</div>
								<div class="col-sm-4">
								 	<div class="ibox-title">
										<h3>Service Areas for BM-SC<span id="schedule_summary_service_area_id"></span></h3>
										<div class="ibox-tools">
											<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
											<a class="close-link"><i class="fa fa-times"></i></a-->
										</div>
									</div><!-- end ibox-title -->
									<div class="ibox float-e-margins" id="service_area">
										<div class="ibox-content">
											<div class="input-group">
												<input type="text" class="form-control" id="toSearchTxt" name="toSearchTxt" value="" placeholder="SA_ID or SA_NAME">
												<span class="input-group-btn">
													<button type="button" class="btn btn-primary" onclick="javascript:searchToServiceArea('75', '서울');" id="toSearchBtn">Search</button>
												</span>
											</div>
										</div>
										<div class="ibox-content">
											<table class="footable table table-stripped toggle-arrow-tiny" data-page-size="10">
												<thead>
													<tr>
														<th>SA_ID</th>
														<th>SA_NAME</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
												<tfoot>
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
        
				<!-- Contents Being Serviced -->
				<div class="row">
					<div class="col-md-8">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h3>Waiting Contents<span id="schedule_waiting_service_area_id"></span></h3>
								<div class="ibox-tools">
									<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
									<a class="close-link"><i class="fa fa-times"></i></a-->
								</div>
							</div><!-- end ibox-title -->
							
							<div class="ibox-content">
	                            <div class="row">
	                            	<div class="col-lg-12" id="schedule_waiting_summary">
	                            		<div class="nothumbnail">
	                                    	<p>
	                                        	<i class="fa fa-search"></i> No Content is Service Currently<br/>
	                                        </p>
	                                        <small></small>
	                                    </div>
	                            	</div>
	                            </div>
	                        </div><!-- end ibox-content -->
						</div>
					</div>
					<!-- Bandwidth -->
					<div class="col-md-4" id="divBandWidth" style="display:none;">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h3>Bandwidth</h3>
								<div class="ibox-tools">
									<!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
									<a class="close-link"><i class="fa fa-times"></i></a-->
								</div>
							</div><!-- end ibox-title -->
							<div class="ibox-content">
								<div>
	                                <div id="bandwidth"></div>
	                            </div>
							</div><!-- end ibox-content -->
						</div>
					</div>
					<!-- end Bandwidth -->
				</div> 
				<div class="row">
					<div class="col-md-12">
						 <div class="ibox float-e-margins">
	                        <div class="ibox-title">
	                            <h3>eMBMS Session Monitoring</h3>
	                            <div class="ibox-tools">
	                                <!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
	                                <a class="close-link"><i class="fa fa-times"></i></a-->
	                            </div>
	                        </div><!-- end ibox-title -->
	                   		<div class="ibox-content">
	                            <div class="row">
							<div class="col-md-12">	
								<table class="table2" >
								<tbody id="embmsList">
									<!-- 
									<tr >
									
										<td>
											<button class="btn btn-sm btn-default" type="button"><i class="fa fa-desktop"></i></button>
											<h4 class="text-center">BM-SC</h4>
										</td>
										<td>
											<button type="button" id="addServer" title="Create new cluster" class="btn btn-primary btn-sm">
											<i class="fa fa-plus"></i> <span class="bold"></span></button>
										</td>
										<td>
											<button class="btn btn-sm btn-default" type="button"><i class="fa fa-close"></i></button>
											<h4 class="text-center">GW</h4>
										</td>
										<td>
											<button class="btn btn-sm btn-default" type="button"><i class="fa fa-close"></i></button>
											<h4 class="text-center">MME</h4>
										</td>
										<td>
											<button class="btn btn-sm btn-default" type="button"><i class="fa fa-close"></i></button>
											<h4 class="text-center">MCE</h4>
										</td>
										<td>
											<button class="btn btn-sm btn-default" type="button"><i class="fa fa-close"></i></button>
											<h4 class="text-center">eNB</h4>
										</td>
									</tr>
									 -->
								<tbody>
								</table>
								</div>
	                            </div>
	                        </div><!-- end ibox-content -->
                        </div>  <!-- end ibox -->
					</div>
				</div>
				<!-- end Contents Being Serviced -->
		</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->
<div class="modal inmodal" id="form-modal" tabindex="-1" role="dialog" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content animated fadeIn">
           <div class="modal-header">
               <button type="button" class="close" id="modal-cancel-icon-btn"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
               <i class="fa fa-folder-open-o modal-icon"></i>
               <h4 class="modal-title" id="modal-title">Add Server</h4>
           </div>
           <div class="modal-body">
               <form method="get" class="form-horizontal">
				   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Server Name</label>
                       <div class="col-sm-9">
							<select name="frm_md_serer_name" id="frm_md_serer_name" class="form-control">
								<option value="">--SELECT--</option>
								<option value="MBMS-GW">MBMS-GW</option>
								<option value="MCE">MCE</option>
								<option value="MME">MME</option>
								<option value="eNB">eNB</option>
							</select>
                       </div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> IP Address</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_IPAddress"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Protocol</label>
                       <div class="col-sm-9">
							<select name="frm_md_protocol" id="frm_md_protocol" class="form-control">
								<option value="">--SELECT--</option>
								<option value="SSH">SSH</option>
								<option value="TELNET">TELNET</option>
							</select>
					   </div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Login Id</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_loginId"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Password</label>
                       <div class="col-sm-9"><input type="password" class="form-control" id="frm_md_password"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> eMBMS session Command</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_command" style="height:200px"></div>
                   </div>
               </form>
           </div>
           <div class="modal-footer">
               <button type="button" class="btn btn-white" id="modal-cancel-btn">Cancel</button>
               <button type="button" class="btn btn-primary" id="modal-add-btn">OK</button>
           </div>
       </div>
   </div>
</div>

<div class="modal inmodal" id="form-edit-modal" tabindex="-1" role="dialog" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content animated fadeIn">
           <div class="modal-header">
               <button type="button" class="close" id="modal-edit-cancel-icon-btn"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
               <i class="fa fa-folder-open-o modal-icon"></i>
               <h4 class="modal-title" id="modal-edit-title">Edit Server</h4>
           </div>
           <div class="modal-body">
               <form method="get" class="form-horizontal">
               	   <input type="hidden" class="form-control" id="embmsId">
				   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Server Name</label>
                       <div class="col-sm-9">
							<select name="frm_md_edit_serer_name" id="frm_md_edit_serer_name" class="form-control">
								<option value="">--SELECT--</option>
								<option value="MBMS-GW">MBMS-GW</option>
								<option value="MCE">MCE</option>
								<option value="MME">MME</option>
								<option value="eNB">eNB</option>
							</select>
                       </div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> IP Address</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_edit_IPAddress"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Protocol</label>
                       <div class="col-sm-9">
							<select name="frm_md_edit_protocol" id="frm_md_edit_protocol" class="form-control">
								<option value="">--SELECT--</option>
								<option value="SSH">SSH</option>
								<option value="TELNET">TELNET</option>
							</select>
					   </div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Login Id</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_edit_loginId"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Password</label>
                       <div class="col-sm-9"><input type="password" class="form-control" id="frm_md_edit_password"></div>
                   </div>
                   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> eMBMS session Command</label>
                       <div class="col-sm-9"><input type="text" class="form-control" id="frm_md_edit_command" style="height:200px"></div>
                   </div>
               </form>
           </div>
           <div class="modal-footer">
               <button type="button" class="btn btn-white" id="modal-edit-cancel-btn">Cancel</button>
               <button type="button" class="btn btn-primary" id="modal-edit-btn">OK</button>
           </div>
       </div>
   </div>
</div>
</body>
</html>
