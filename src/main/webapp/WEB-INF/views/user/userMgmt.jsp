<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Mgmt</title>

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
	<script src="js/modules/user-list.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('USER_MGMT');
			getUserList('${isBack}' == 'true' ? true : false, false);
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
					<h2 style="margin-left: 15px;"><strong>User Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> User Mgmt</strong>
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
		
<!-- 		<div class="row wrapper border-bottom white-bg page-heading"> -->
<!-- 			<div class="col-lg-12"> -->
<!-- 				<h2><strong>User Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>User Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->
		
		<!-- content body -->
        <div class="wrapper wrapper-content">

            <!-- User Mgmt -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
<!--                         <div class="ibox-title"> -->
<!--                             <h5>User Mgmt</h5> -->
<!--                             <div class="ibox-tools"> -->
<!--                                 <a class="collapse-link"><i class="fa fa-chevron-up"></i></a> -->
<!--                                 <a class="close-link"><i class="fa fa-times"></i></a> -->
<!--                             </div> -->
<!--                         </div> -->
                        <div class="ibox-content">
                            <div class="row" id="search-area">
                            	<div class="col-sm-3">
									<c:choose>
										<c:when test="${USER.grade == 0}">
											<select name="status" id="search-operator-id" class="input-sm form-control input-s-sm">
												<option value="">All</option>
												<c:forEach items="${operatorList}" var="operator">
													<option value="${operator.id}">${operator.name}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select name="status" id="search-operator-id" class="input-sm form-control input-s-sm" disabled="disabled">
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
                                <div class="col-sm-3">
                                    <select class="input-sm form-control input-s-sm" id="search-column">
                                        <option value="all">All</option>
                                        <option value="name">Name</option>
                                        <option value="userId">ID</option>
                                        <option value="department">Department</option>
<!--                                         <option value="operator">Operator</option> -->
                                    </select>
                                </div>
                                <div class="col-sm-3" id="search-keyword-area">
                                    <div class="input-group"><input type="text" placeholder="Search" class="input-sm form-control" id="search-keyword"> <span class="input-group-btn">
                                            <button type="button" class="btn btn-sm btn-primary" id="go-search"> Go!</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-sm-3 pull-right text-right">
                                    <button type="button" class="btn btn-primary btn-sm" id="btn-add-user">User Add</button>
                                </div>
                            </div>
                            <div class="table-responsive">
                            	<table class="table table-bordered" id="table"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

</body>
</html>
