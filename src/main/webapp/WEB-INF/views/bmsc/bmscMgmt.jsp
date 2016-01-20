<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BM-SC Management</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/jquery.cookie.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-table.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="js/common.js"></script>
	<script src="js/modules/bmsc.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('BMSC_MGMT');
			getBmscList();
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
        
        <div class="row wrapper border-bottom white-bg page-heading">
			<div class="col-lg-12">
				<h2><strong>BM-SC Mgmt</strong></h2>
				<ol class="breadcrumb">
				    <li>
					   <a href="/dashbd/resources/main.do">Home</a>
				    </li>
				    <li class="active">
					   <strong>BM-SC Mgmt</strong>
				    </li>
				</ol>
			</div>
		</div><!-- end row wrapper border-bottom white-bg page-heading -->
		
		<!-- content body -->
        <div class="wrapper wrapper-content">
			
            <!-- Operator Mgmt -->
            <div class="row">
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
	                    		<div class="col-lg-8">
	                                <form method="get" class="form-horizontal">
										<div class="form-group"><label class="col-sm-2 control-label">Operator</label>
											<div class="col-sm-6">
												<c:choose>
													<c:when test="${USER.grade == 0}">
														<select name="status" id="form-operator-id" class="form-control">
															<c:forEach items="${operatorList}" var="operator">
																<option value="${operator.id}">${operator.name}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<select name="status" id="form-operator-id" class="form-control" disabled="disabled">
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
										</div>
									</form>
	                            </div>
	                            <div class="col-lg-4">
	                                <button type="button" class="btn btn-primary pull-right" id="modal-open-btn">
										Create New BMSC
	                                </button>
	                                <div class="modal inmodal" id="form-modal" tabindex="-1" role="dialog" aria-hidden="true">
	                                    <div class="modal-dialog">
	                                        <div class="modal-content animated fadeIn">
	                                            <div class="modal-header">
	                                                <button type="button" class="close" id="modal-cancel-icon-btn"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	                                                <i class="fa fa-folder-open-o modal-icon"></i>
	                                                <h4 class="modal-title" id="modal-title">Create New BMSC</h4>
	                                            </div>
	                                            <div class="modal-body">
	                                                <form method="get" class="form-horizontal">
	                                                    <div class="form-group"><label class="col-sm-3 control-label">BMSC Name</label>
	                                                        <div class="col-sm-9"><input type="text" class="form-control" id="form-bmsc-name"></div>
	                                                    </div>
	                                                    <div class="form-group"><label class="col-sm-3 control-label">IP Address</label>
	                                                        <div class="col-sm-9"><input type="text" class="form-control" id="form-bmsc-ipaddress"></div>
	                                                    </div>
	                                                    <div class="form-group"><label class="col-sm-3 control-label">Circle</label>
	                                                        <div class="col-sm-9"><input type="text" class="form-control" id="form-bmsc-circle" style="height:200px"></div>
	                                                    </div>
	                                                </form>
	                                            </div>
	                                            <div class="modal-footer">
	                                                <button type="button" class="btn btn-white" id="modal-cancel-btn">Cancel</button>
	                                                <button type="button" class="btn btn-primary" id="modal-add-btn">Save</button>
	                                            </div>
	                                        </div>
	                                    </div>
	                                </div>
	                            </div>
	                        </div>
	                    	<div class="hr-line-dashed"></div>
	                    
							<div class="table-responsive">
                            	<table class="table table-bordered" id="table"></table>
                            </div>
	                    </div><!-- end ibox-content -->
	                </div>
	            </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

</body>
</html>
