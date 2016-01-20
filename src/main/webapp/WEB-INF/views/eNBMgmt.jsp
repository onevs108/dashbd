<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>eNB Mgmt</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    
    <!-- FooTable -->
    <link href="css/plugins/footable/footable.core.css" rel="stylesheet">
    
    <link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

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
	   
    <script src="/dashbd/resources/app-js/apps/svc_enb_mgmt.js"></script>
    
    <script src="js/common.js"></script>
    
	<script type="text/javascript">
		$(document).ready(function() {
			getMenuList('ENB_MGMT');
		});
	</script>
    
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
				<h2><strong>eNB Mgmt</strong></h2>
				<ol class="breadcrumb">
				    <li>
					   <a href="/dashbd/resources/main.do">Home</a>
				    </li>
				    <li class="active">
					   <strong>eNB Mgmt</strong>
				    </li>
				</ol>
			</div>
		</div><!-- end row wrapper border-bottom white-bg page-heading -->
            
	<div class="wrapper wrapper-content">
        <div class="row">
        
            <!-- eEPG for ESPN upload-->
            <div class="col-lg-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h3>eNB Upload</h3>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal excelUpload" enctype="multipart/form-data" method="post" action="">
                            <div class="form-group"><label class="col-sm-3 control-label">Operator</label>
                                <div class="col-sm-9">
                                	<c:choose>
										<c:when test="${USER.grade == 0}">
											<select name="operator" id="operator" class="form-control">
												<c:forEach items="${OperatorList}" var="operator">
													<option value="${operator.id}">${operator.name}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select name="operator" id="operator" class="form-control" disabled="disabled">
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
<!-- 									<select name="operator" id="operator" class="form-control" > -->
<!-- 										<option value=''></option> -->
<%-- 	                                    <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx"> --%>
<%-- 											<option value="${operatorList.id }">${operatorList.name }</option> --%>
<%-- 										</c:forEach> --%>
<!-- 									</select> -->
						  		</div>
                            </div>
                            <div class="form-group"><label class="col-sm-3 control-label">BMSC</label>
                                <div class="col-sm-9">
											<select name="bmsc" id="bmsc" class="form-control">
											</select>
						  </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-3">
                                <label for="excelFile" class="btn btn-sm btn-default pull-right">
                                    <input type="file" name="excelFile" id="excelFile" class="hide" onchange="javascript:displayFile();"><strong>Select File</strong>
                                </label>
                                </div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="selectedExcelFile" value= "" readonly></div>
                        	</div>
                        </form>
                        <div class="hr-line-dashed"></div>
                        <div class="row">
				    		<div class="col-sm-9 pull-right">
						<button class="btn btn-block btn-sm btn-primary" id="uploadExcel"><strong>Upload</strong></button>
						</div>
				    </div>
                    </div><!-- end ibox-content -->
                </div>
            </div>
            <!-- end eEPG for ESPN upload -->
            
            <!-- eEPG for ESPN down-->
            <div class="col-lg-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h3>eNB Download</h3>
                    </div>
                    <div class="ibox-content">
                        <form method="post" class="form-horizontal" name="downloadForm">
                            <div class="form-group"><label class="col-sm-3 control-label">Operator</label>
                                <div class="col-sm-9">
                                	<c:choose>
										<c:when test="${USER.grade == 0}">
											<select name="operator_down" id="operator_down" class="form-control">
												<c:forEach items="${OperatorList}" var="operator">
													<option value="${operator.id}">${operator.name}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select name="operator_down" id="operator_down" class="form-control" disabled="disabled">
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
<!-- 										<select name="operator_down" id="operator_down" class="form-control" > -->
<!-- 											<option value=''></option> -->
<%-- 	                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx"> --%>
<%-- 											<option value="${operatorList.id }">${operatorList.name }</option> --%>
<%-- 											</c:forEach> --%>
<!-- 										</select> -->
						  </div>
                            </div>
                            <div class="form-group"><label class="col-sm-3 control-label">BMSC</label>
                                <div class="col-sm-9">
											<select name="bmsc_down" id="bmsc_down" class="form-control">
											</select>
						  </div>
                            </div>
                        
                        <div class="hr-line-dashed"></div>
                        <span style="font-weight:bold"><i class="fa fa-wifi"></i> Service Area</span>
                        <span class="pull-right" style="padding-bottom:22px">all : <strong class="text-danger" id="svcTotalCount">0</strong></span>
                        <div class="ibox-content" id="service_area">
                            <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="5">
                                <thead>
                                    <tr>
                                        <th></th>
										<th>SA_ID</th>
                                        <th>Description</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
							 			<td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3">
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
						<div class="row ">
							<div class="col-md-12">
							<button class="btn btn-block btn-sm btn-default" type="submit" id="downloadSA" onclick="javascript:downloadENBsByServiceAreaId( document.downloadForm );"><strong>Download eNB for Selected SA</strong></button>
							</div>
							<div class="col-md-12">
							<button class="btn btn-block btn-sm btn-default" type="submit" id="downloadBmsc" onclick="javascript:downloadENBs( document.downloadForm );"><strong>Download eNB for BMSC</strong></button>
							</div>
						</div>
						</form>
                    	</div><!-- end ibox-content -->
                    </div><!-- end ibox-content -->
                </div>
            </div>
            <!-- end eEPG for ESPN down -->
            
        </div>	
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->

</div><!-- end wrapper -->
    
</body>
</html>
