<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Permission Management</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/jquery.cookie.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-table.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="js/common.js"></script>
	<script src="js/modules/permission.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('PERMISSION_MGMT');
			getUserList();
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
					<div class="dropdown profile-element">
						<a href="/dashbd/resources/main.do"><img src="img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="img/logo2.png">
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
					<h2 style="margin-left: 15px;"><strong>Permission Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Permission Mgmt</strong>
					</span>
				</div><!-- end navbar-header -->
		        
				<ul class="nav navbar-top-links navbar-right">
					<li>
						<a>
							<i class="fa fa-user"></i><span id="navbar-user-name"></span>
						</a>
					</li>
					<li class="dropdown">
						<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
							<i class="fa fa-bell"></i>
<!-- 							<span class="label label-primary">8</span> -->
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
						<a href="/dashbd/out">
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
        
<!--         <div class="row wrapper border-bottom white-bg page-heading"> -->
<!-- 			<div class="col-lg-12"> -->
<!-- 				<h2><strong>Permission Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>Permission Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->
		
			<div class="wrapper wrapper-content">
		        <div class="row">
		            <!-- Permission Mgmt -->
		            <div class="col-lg-12">
		                <div class="ibox float-e-margins">
		                    <div class="ibox-content">
		                    	<div class="row" style="padding-top:20px">
		                            <div class="col-lg-8">
		                                <form method="get" class="form-horizontal">
		                                	<label class="col-sm-2 control-label">Operator</label>
		                                    <div class="col-sm-6">
			                                	<c:choose>
													<c:when test="${USER.grade == 0}">
														<select name="status" id="search-operator-id" class="form-control">
															<c:forEach items="${operatorList}" var="operator">
																<option value="${operator.id}">${operator.name}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<select name="status" id="search-operator-id" class="form-control" disabled="disabled">
															<c:forEach items="${operatorList}" var="operator">
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
											</div>
<!-- 		                                    <div class="form-group"> -->
<!-- 		                                    	<label class="col-sm-2 control-label">Operator</label> -->
<!-- 		                                        <div class="col-sm-6"><input type="text" class="form-control" id="search-operator-id"></div> -->
<!-- 		                                    </div> -->
		                                </form>
		                            </div>
		                        </div>
		                    </div><!-- end ibox-content -->
		                </div>
		            </div><!-- end Permission Mgmt -->
		            
		            <!-- Permission Mgmt1 -->
		            <div class="col-lg-8">
		                <div class="ibox float-e-margins">
		                    <div class="ibox-content">
		                    	<div class="row" style="padding-top:30px">
		                            <div class="col-md-10 col-md-offset-1">
		                                <div class="input-group"><input type="text" placeholder="Search" class="input-sm form-control" id="search-keyword"> <span class="input-group-btn">
		                                    <button type="button" class="btn btn-sm btn-primary" id="go-search"> search</button> </span>
		                                </div>
		                            </div>
		                        </div>
		                        <div class="hr-line-dashed"></div>
		                        <h3 class="text-center" style="padding-bottom:10px;">Search Result</h3>
		                        <div class="row">
							    <div class="col-md-10 col-md-offset-1">
							    	<div class="table-responsive">
		                            	<table class="table table-striped" id="table"></table>
		                            </div>
							    </div>
						    </div>
		                    </div><!-- end ibox-content -->
		                </div>
		            </div><!-- end Permission Mgmt1 -->
		            
		            <!-- Permission Mgmt2 -->
		            <div class="col-lg-4">
		                <div class="ibox float-e-margins">
		                    <div class="ibox-title"><h3 id="permission-box-title">Permission Granted</h3></div>
							<div class="ibox-content">
			                    <div class="row">
									<div class="col-md-12">
										<fieldset>
											<c:forEach items="${permissionList}" var="permission">
												<c:if test="${permission.id == 1}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-user" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox1"> User Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 2}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-permission" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox2"> Permission Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 3}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-contents" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox3"> Contents Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 4 && USER.grade == 0}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-operator" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox4"> Operator Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 5}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-bmsc" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox5"> BMSC Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 6}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-service-area" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox6"> Service Area Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 7}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-enb" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox7"> eNB Management</label>
													</div>
												</c:if>
												<c:if test="${permission.id == 8}">
													<div class="checkbox checkbox-primary">
														<input id="checkbox-permission-schedule" type="checkbox" name="permissions" value="${permission.id}">
														<label for="checkbox8"> Schedule Management</label>
													</div>
												</c:if>
											</c:forEach>
										</fieldset>
									</div>
								</div>
								<div class="row">
									<div class="hr-line-dashed"></div>
									<div class="col-md-6">
<!-- 										<input class="btn btn-success" type="button" id="save-btn" value="Save"> -->
										<button class="btn btn-block btn-sm btn-primary" type="submit" id="save-btn">Save</button>
									</div>
									<div class="col-md-6">
<!-- 										<input class="btn btn-white" type="button" id="cancel-btn" value="Cancel"> -->
										<button class="btn btn-block btn-sm btn-default" type="submit" id="cancel-btn">Cancel</button>
									</div>
								</div>
							</div><!-- end ibox-content -->
						</div>
					</div><!-- end Permission Mgmt2 -->
				</div>	
			</div><!-- end wrapper wrapper-content -->
			
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

</body>
</html>
