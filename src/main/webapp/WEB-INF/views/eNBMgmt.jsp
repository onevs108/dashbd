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
    
    <!-- FooTable -->
    <link href="css/plugins/footable/footable.core.css" rel="stylesheet">
    
    <link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
    
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
				<li>
					<a href="/dashbd/resources/user_mgmt.html"><i class="fa fa-user"></i> <span class="nav-label">User Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/PermissionMgmt.html"><i class="fa fa-lock"></i> <span class="nav-label">Permission Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/contents_mgmt.html"><i class="fa fa-file"></i> <span class="nav-label">Contents Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/OperatorMgmt.html"><i class="fa fa-envelope"></i> <span class="nav-label">Operator Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/BMSCManagement.html"><i class="fa fa-flag"></i> <span class="nav-label">BM-SC Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/serviceArea.do"><i class="fa fa-globe"></i> <span class="nav-label">Service Area  Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/resources/eNBMgmt.do"><i class="fa fa-puzzle-piece"></i> <span class="nav-label">eNB Mgmt</span></a>
				</li>
				<li>
					<a href="/dashbd/view/schdMgmt.do"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
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
			<h2>eNB Management</h2>
			<ol class="breadcrumb">
			    <li>
				   <a href="/dashbd/resources/main.do">Home</a>
			    </li>
                <li class="active">
				   <strong>eNB Management</strong>
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
                        <form method="get" class="form-horizontal">
                            <div class="form-group"><label class="col-sm-3 control-label">Operator</label>
                                <div class="col-sm-9">
										<select name="operator" id="operator" class="form-control" >
											<option value=''></option>
	                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx">
											<option value="${operatorList.id }">${operatorList.name }</option>
											</c:forEach>
										</select>
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
                                <label for="inputImage" class="btn btn-sm btn-default pull-right">
                                    <input type="file" accept="image/*" name="file" id="inputImage" class="hide"><strong>Select File</strong>
                                </label>
                                </div>
                                <div class="col-sm-9"><input type="text" class="form-control"></div>
                        	</div>
                        </form>
                        <div class="hr-line-dashed"></div>
                        <div class="row">
				    		<div class="col-sm-9 pull-right">
						<button class="btn btn-block btn-sm btn-primary" type="submit"><strong>Upload</strong></button>
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
                        <form method="get" class="form-horizontal">
                            <div class="form-group"><label class="col-sm-3 control-label">Operator</label>
                                <div class="col-sm-9">
										<select name="operator" id="operator_down" class="form-control" >
											<option value=''></option>
	                                        <c:forEach var='operatorList' items="${OperatorList}" varStatus="idx">
											<option value="${operatorList.id }">${operatorList.name }</option>
											</c:forEach>
										</select>
						  </div>
                            </div>
                            <div class="form-group"><label class="col-sm-3 control-label">BMSC</label>
                                <div class="col-sm-9">
											<select name="bmsc" id="bmsc_down" class="form-control">
											</select>
						  </div>
                            </div>
                        </form>
                        <div class="hr-line-dashed"></div>
                        <span style="font-weight:bold"><i class="fa fa-wifi"></i> Service Area</span>
                        <span class="pull-right" style="padding-bottom:22px">all : <strong class="text-danger">00</strong></span>
                        <div class="ibox-content">
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
							 	<td><input type="checkbox" id="checkbox1"></td>
                                        <td>100</td>
                                        <td>Service Area for Subway#2 Line</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox2"></td>
                                        <td>101</td>
                                        <td>Olympic Stadium</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox3"></td>
                                        <td>102</td>
                                        <td>ICN Airport</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox4"></td>
                                        <td>103</td>
                                        <td>Gimpo Airport</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox5"></td>
                                        <td>104</td>
                                        <td>ICN Golf Resort</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox6"></td>
                                        <td>100</td>
                                        <td>Service Area for Subway#2 Line</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox7"></td>
                                        <td>101</td>
                                        <td>Olympic Stadium</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox8"></td>
                                        <td>102</td>
                                        <td>ICN Airport</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox9"></td>
                                        <td>103</td>
                                        <td>Gimpo Airport</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox10"></td>
                                        <td>104</td>
                                        <td>ICN Golf Resort</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox11"></td>
                                        <td>103</td>
                                        <td>Gimpo Airport</td>
                                    </tr>
                                    <tr>
							 	<td><input type="checkbox" id="checkbox12"></td>
                                        <td>104</td>
                                        <td>ICN Golf Resort</td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3">
                                            <ul class="pagination pull-right"></ul>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
						<div class="row ">
							<div class="col-md-12">
							<button class="btn btn-block btn-sm btn-default" type="submit"><strong>Download eNB for Selected SA</strong></button>
							</div>
							<div class="col-md-12">
							<button class="btn btn-block btn-sm btn-default" type="submit"><strong>Download eNB for BMSC</strong></button>
							</div>
						</div>
                    	</div><!-- end ibox-content -->
                    </div><!-- end ibox-content -->
                </div>
            </div>
            <!-- end eEPG for ESPN down -->
            
        </div>	
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->

</div><!-- end wrapper -->

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
    
    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function() {

            $('.footable').footable();
            $('.footable2').footable();

        });

    </script>
    
</body>
</html>
