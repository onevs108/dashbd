<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Mgmt</title>
    <link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet">
    <link href="../resourcesRenew/css/style.css" rel="stylesheet">
    <link href="../resourcesRenew/css/animate.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/footable/footable.core.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet" media='print' />
    <link href="../resourcesRenew/css/plugins/fullcalendar/scheduler.css" rel="stylesheet" media='print' />
    <link href="../resourcesRenew/css/plugins/timePicki/timepicki.css" rel="stylesheet" media='print' />
    
	<link href="../resources/css/custom.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/datapicker/datepicker3.css" rel="stylesheet" type="text/css" />
    <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet">

    
    <!-- Mainly scripts -->
	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
	<script src="../resourcesRenew/js/jquery-ui-1.10.4.min.js"></script>
	
	<script src="../resourcesRenew/js/bootstrap.min.js"></script>
	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="../resourcesRenew/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	
	<!-- FooTable -->
	<script src="../resourcesRenew/js/plugins/footable/footable.all.min.js"></script>
	
	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/js/inspinia.js"></script>
	<script src="../resourcesRenew/js/plugins/pace/pace.min.js"></script>
	<script src="../resourcesRenew/js/plugins/fullcalendar/moment.min.js"></script>
	<script src="../resourcesRenew/js/plugins/fullcalendar/fullcalendar.js"></script>
	<script src="../resourcesRenew/js/plugins/fullcalendar/scheduler.js"></script>
	<script src="../resourcesRenew/js/plugins/timePicki/timepicki.js"></script>
	<script src="../resourcesRenew/js/popup/jquery.leanModal.min.js"></script>
	
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	<script src="../resourcesRenew/app-js/apps/schdMgmtDetail.js"></script>
	
	<script src="../resources/js/common.js"></script>
	
	<!-- Page-Level Scripts -->
	<script>
		$(document).ready(function() {
			getMenuList('SCHEDULE_MGMT');
		});
	</script>
	
	<!-- Page-Level Scripts -->
	<style>
#external-events .fc-event {
	margin: 10px 0;
	cursor: pointer;
}



#external-events {
	width: 2;
	padding: 0 10px;
	/*
	float: right;
	border: 1px solid #ccc;
	background: #eee;
	*/
	text-align: left;
}

 .timeline {
     position: absolute;    
     border: 2px dotted red;
     width: 1px;
     margin: 0;
     padding: 0;
     z-index: 9;
     height: auto;
 }
	
/*
#wrap {
		width: 1100px;
		margin: 0 auto;
	}
		

		
	#external-events h4 {
		font-size: 16px;
		margin-top: 0;
		padding-top: 1em;
	}
		

		
	#external-events p {
		margin: 1.5em 0;
		font-size: 11px;
		color: #666;
	}
		
	#external-events p input {
		margin: 0;
		vertical-align: middle;
	}

	#calendar {
		float: right;
		width: 900px;
	}
	*/	
	</style>
	
<script>

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
						<a href="/dashbd/resources/main.do"><img src="/dashbd/resources/img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="/dashbd/resources/img/logo2.png">
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
					<h2 style="margin-left: 15px;"><strong>Schedule Mgmt</strong></h2>
					<span style="margin-left: 15px;">
						<a href="/dashbd/resources/main.do" style="color: #2f4050;">Home</a> / <strong> Schedule Mgmt</strong>
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
<!-- 				<h2><strong>Schedule Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>Schedule Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->
		
        <!-- content body -->
        <div class="wrapper wrapper-content">

            <!-- Contents -->
            <div class="row">
            <input type="hidden" id="serviceAreaId" name="serviceAreaId" value="${serviceAreaId}"/>
            <input type="hidden" id="searchDate" name="searchDate" value="${searchDate}"/>
            <input type="hidden" id="category" name="category" value="${category}"/>
            <input type="hidden" id="title" name="title" value="${title}"/>
            
            <div class="col-lg-12">
                    <div class="ibox float-e-margins">
<!--                         <div class="ibox-title"> -->
<!--                             <h5>Schedule Mgmt </h5> -->
<!--                             <div id="calendarTrash" style="float: right; padding-top: 5px; padding-right: 5px; padding-left: 5px;"><span class="ui-icon ui-icon-trash"><img src="../resourcesRenew/img/trash.png"/></span></div> -->
<!--                             <div class="ibox-tools"> -->
<!--                                 <a class="collapse-link"><i class="fa fa-chevron-up"></i></a> -->
<!--                                 <a class="close-link"><i class="fa fa-times"></i></a> -->
<!--                             </div> -->
<!--                         </div> -->
                        
                        <div class="ibox-content">
                        <form method="get" class="form-horizontal">
                            <div class="row">
                                <div class="col-sm-8" id="epg-table">
									<div style = "width:100%;"id="calendar"></div>
                                </div>
                                <div class="col-sm-4">
                                    
                                        <div class="form-group" style="margin-bottom: 5px;">
                                            <label class="col-md-4 control-label">Category</label>
                                            <!--div class="col-md-8">
                                                <select class="form-control input-sm">
                                                    <option value="">?</option>
                                                </select>
                                            </div-->
                                            <div class="col-md-8"><input type="text" id="form-category" class="form-control input-sm" value="${category}"></div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">Title</label>
                                            <div class="col-md-8"><input type="text" id="form-title" class="form-control input-sm" value="${title}"></div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-8 col-sm-offset-4">
                                            	<button class="btn btn-block btn-primary" type="button" id="go-search">Search</button>
                                           	</div>
                                        </div>
                                        
                                        <div class="hr-line-dashed"></div>
                                        <div class="search-list" style="display:none">
                                        	
                                            <h5>Search Result</h5>
                                            <div id='external-events'>
                                            </div>
                                        </div>
                                        <div id='paging'></div>
                                    
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>
                </div>
                
            </div>
			
        </div><!-- content body end -->
    </div><!-- content end -->

</div><!-- wrapper end -->
</body>
</html>
