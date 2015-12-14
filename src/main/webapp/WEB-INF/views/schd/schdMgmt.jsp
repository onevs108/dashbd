<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <link href="../resourcesRenew/css/custom.css" rel="stylesheet">
    <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="../resourcesRenew/css/timetable/timetablejs.css" rel="stylesheet" >
    
    
	<!-- Mainly scripts -->
	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
	<script src="../resourcesRenew/js/bootstrap.min.js"></script>
	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	
	<!-- FooTable -->
	<script src="../resourcesRenew/js/plugins/footable/footable.all.min.js"></script>
	
	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/js/inspinia.js"></script>
	<script src="../resourcesRenew/js/plugins/pace/pace.min.js"></script>
	<script src="../resourcesRenew/js/timetable/timetable.min.js"></script>
	
	<!-- Page-Level Scripts -->
<script>
	$(function() {
		tmpServiceAreaId = '3048' 
		callTimetable(tmpServiceAreaId);
		
		$("#btnScheduleDetail").click(function() {
			location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId;
		});
	});
	
	function callTimetable(serviceAreaId_val){
		var param = {
				serviceAreaId : serviceAreaId_val
			};
			
			$.ajax({
				type : "POST",
				url : "getSchedule.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					setTimeTable(data);
			
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
	}
	
	function setTimeTable(data ){
		var contents = data.contents;
		var viewStartHour = data.viewStartHour;
		var timetable = new Timetable();
		//현재시점에서 2시전, 끝까지.
		timetable.setScope(viewStartHour,0);
        timetable.addLocations(['depth1', 'depth2']);
		
		for ( var i=0; i<contents.length; i++) {
			var name = contents[i].name;
			var start_year = contents[i].start_year;
			var start_month = contents[i].start_month;
			var start_day = contents[i].start_day;
			var start_hour = contents[i].start_hour;
			var start_mins = contents[i].start_mins;
			
			var end_year = contents[i].end_year;
			var end_month = contents[i].end_month;
			var end_day = contents[i].end_day;
			var end_hour = contents[i].end_hour;
			var end_mins = contents[i].end_mins;
			
			timetable.addEvent(contents[i].NAME, 'depth1', 
										new Date(start_year,start_month, start_day,start_hour,start_mins ),
					 					new Date(end_year,end_month, end_day,end_hour,end_mins ),
					 					'#');
			if ( i == 3 )
				break;
		}
		
		var renderer = new Timetable.Renderer(timetable);
           renderer.draw('.timetable');
           
            /*
            timetable.addEvent('Sightseeing', 'depth1', new Date(2015,7,17,10,45), new Date(2015,7,17,12,30), '#');
            timetable.addEvent('Zumba', 'depth2', new Date(2015,7,17,12), new Date(2015,7,17,13), '#');
            timetable.addEvent('Zumbu', 'depth2', new Date(2015,7,17,13,30), new Date(2015,7,17,15), '#');
            timetable.addEvent('Lasergaming', 'depth3', new Date(2015,7,17,17,45), new Date(2015,7,17,19,30), '#');
            timetable.addEvent('All-you-can-eat grill', 'depth4', new Date(2015,7,17,21), new Date(2015,7,18,1,30), '#');
            */
            
	}
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
                        Logo
                    </div>
                    <div class="logo-element">
                        logo
                    </div>
                </li>
                <li>
                    <a href="user_mgmt.html"><i class="fa fa-user"></i> <span class="nav-label">User Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-lock"></i> <span class="nav-label">Permission Mgmt</span></a>
                </li>
                <li>
                    <a href="contents_mgmt.html"><i class="fa fa-file-text-o"></i> <span class="nav-label">Contents Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-bullhorn"></i> <span class="nav-label">Operator Mgmt</span></a>
                </li>
                <li>
                    <a href="#" onclick="return false;"><i class="fa fa-flag"></i> <span class="nav-label">BM-SC Mgmt</span></a>
                </li>
                <li>
                    <a href="service_area_mgmt.html"><i class="fa fa-globe"></i> <span class="nav-label">Service Area Mgmt</span></a>
                </li>
                <li class="landing_link">
                    <a href="schdMgmt.do"><i class="fa fa-calendar"></i> <span class="nav-label">Schedule Mgmt</span></a>
                </li>
            </ul>
        </div>
    </nav><!-- sidebar end -->

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">

        <!-- content header -->
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top white-bg" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-success " href="#"><i class="fa fa-bars"></i> </a>
                    <form role="search" class="navbar-form-custom" action="search_results.html">
                        <div class="form-group">
                            <input type="text" placeholder="Search" class="form-control" name="top-search" id="top-search">
                        </div>
                    </form>
                </div>
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
                </ul>
            </nav>
        </div><!-- content header end -->

        <!-- content body -->
        <div class="wrapper wrapper-content">

            <!-- Contents -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Schedule Mgmt : eEPG</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="row">
                                <div class="col-sm-3 m-b-sm">
                                    <select class="input-sm form-control input-s-sm">
                                        <option value="0">Option 1</option>
                                        <option value="1">Option 2</option>
                                        <option value="2">Option 3</option>
                                        <option value="3">Option 4</option>
                                    </select>
                                </div>
                                <div class="col-sm-3">
                                    <select class="input-sm form-control input-s-sm">
                                        <option value="0">Option 1</option>
                                        <option value="1">Option 2</option>
                                        <option value="2">Option 3</option>
                                        <option value="3">Option 4</option>
                                    </select>
                                </div>
                            </div>
                            <div class="google_map">
                                Map Here
                            </div>
                            <div class="eepg_timeline">
                                <div class="timetable"></div>
                            </div>
                            <div class="">
	                            <button type="button" class="btn btn-success btn-sm" id="btnScheduleDetail">eEPG management</button>
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
