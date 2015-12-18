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
    <link href="../resourcesRenew/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet" media='print' />
    <link href="../resourcesRenew/css/plugins/fullcalendar/scheduler.css" rel="stylesheet" media='print' />
    <link href="../resourcesRenew/css/plugins/timePicki/timepicki.css" rel="stylesheet" media='print' />
    <link href="../resourcesRenew/css/custom.css" rel="stylesheet">
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
	<!-- Page-Level Scripts -->
	<style>

	
/* popup */
.popupbox { position:absolute; left:10%; top:0;width:150px; z-index:99999; }
.l_popup_bg { position:fixed; left:0; top:0; width:100%; height:100%; z-index:999999; background:#000; opacity:0.7; filter:alpha(opacity=70); -ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=70)"; }
.popupbox .pbox { position:relative; text-align:left; z-index:9999999; width:300%; padding:0 0 15px 0; box-shadow:0 5px 10px #000; }
.popupbox .titbox { position:relative; z-index:9; height:60px; }
.popupbox .titbox h2.tit { padding:22px 0 0 0; text-align:center; color:#fff; font-size:20px; line-height:20px; font-weight:bold; }
.popupbox .pop_body { position:relative; z-index:9; }
.popupbox .pop_body .board_box { text-align:left; background-color:#fff; }
.popupbox .pop_body .board_box .tbox { padding:34px 40px; *zoom:1; } 
.popupbox .pop_body .board_box .tbox:after {content:""; display:block; visibility:hidden; clear:both; height:0;}
.popupbox .pop_body .board_box .tbox .imgbox { float:left; }
.popupbox .pop_body .board_box .tbox .imgbox img { border:1px solid #d4d4d4; }
.popupbox .pop_body .board_box .tbox .txtbox { float:left; padding:10px 0 0 32px; }
.popupbox .pop_body .board_box .tbox .txtbox .txt { padding:10px 0 20px 0; }
.popupbox .pop_body .board_box p.tit { color:#767676; font-size:16px; font-weight:bold; }
.popupbox .pop_body .board_box .txt { color:#767676; font-size:14px; line-height:30px }
.popupbox .pop_body .board_box .answer { text-align:center; padding:16px 0 45px 0; background:#f4f4f4 url(../img/sub/bg_answer.gif) center top no-repeat;}
.popupbox .pop_body .board_box .answer .txt { padding:10px 0 0 0; }
.popupbox .pop_body .pop_map { position:relative; padding:10px; background-color:#fff; }
.popupbox .pop_body .pop_map .location_p { position:absolute; left:308px; top:60px; width:248px; height:170px; background:url(../img/sub/bg_location_p.png) left top no-repeat;}
.popupbox .pop_body .pop_map .location_p ul { padding:34px 0 0 50px; }
.popupbox .pop_body .pop_map .location_p li { margin:0 0 8px 0; color:#5a5a5a; font-weight:bold; font-size:16px; }
.popupbox .pop_body .ipbox { padding:20px 0 20px 2px; background-color:#fff; *zoom:1; } 
.popupbox .pop_body .ipbox:after {content:""; display:block; visibility:hidden; clear:both; height:0;}
.popupbox .pop_body .ipbox .imgbox { float:left; }
.popupbox .pop_body .ipbox .imgbox li { float:left; margin:0 0 0 8px; }
.popupbox .pop_body .ipbox .imgbox li img { border:1px solid #f0f0f0; }
.popupbox .pop_body .ipbox .txt { float:left; /*width:495px;*/ padding:0 0 0 30px; text-align:left; }
.popupbox .pop_body .ipbox .txt li { margin:0 0 13px 0; color:#767676;}
.popupbox .pop_body .ipbox .txt textarea { width:100%; height:55px; padding:5px 0 5px 0; overflow:auto; line-height:22px; color:#767676; border:1px solid #d2d2d2; background-color:#fff; }
.popupbox .pop_body .btnbox { padding:15px 0 0 0; text-align:center;  border-top:1px solid #efefef;border-radius:0 0 7px 7px; background-color:#f5f5f5;  }
.popupbox .pop_body .btnbox a { display:inline-block; width:166px; height:58px; cursor:pointer; text-indent:-9999em; overflow:hidden; background:url(../resourcesRenew/img/btn/btn_confirm.png) left top no-repeat; }
.popupbox .pop_body .btnbox a.btn_cancel { display:inline-block; width:166px; height:58px; cursor:pointer; text-indent:-9999em; overflow:hidden; background:url(../resourcesRenew/img/btn/btn_cancel.png) left top no-repeat; }
.popupbox .pop_body .btnchk { padding:15px 0 0 0; text-align:left;  border-top:1px solid #efefef;border-radius:0 0 7px 7px; background-color:#f5f5f5;  }
.popupbox span.btn_close { position:absolute; right:25px; top:15px; z-index:9999; display:block; width:35px; height:35px; line-height:30px;cursor:pointer; text-indent:-9999em; overflow:hidden; background:url(../resourcesRenew/img/btn/btn_close_p.png) left top no-repeat; }
.popupbox span.lt { position:absolute; left:0; top:0; z-index:1; width:100%; height:60px; display:block; overflow:hidden; background:url(../resourcesRenew/img/common/bg_round1.png) left top no-repeat; }
.popupbox span.rt { position:absolute; right:0; top:0; z-index:1; width:100%; height:60px; display:block; overflow:hidden; background:url(../resourcesRenew/img/common/bg_round1.png) right top no-repeat; }
.popupbox span.lb { position:absolute; left:0; bottom:0; z-index:1; width:100%; height:92px; display:block; overflow:hidden; background:url(../resourcesRenew/img/common/bg_round2.png) left bottom no-repeat; }
.popupbox span.rb { position:absolute; right:0; bottom:0; z-index:1; width:100%; height:92px; display:block; overflow:hidden; background:url(../resourcesRenew/img/common/bg_round2.png) right bottom no-repeat; }


#external-events .fc-event {
	margin: 10px 0;
	cursor: pointer;
}

#external-events {
	float: right;
	width: 95%;
	padding: 0 10px;
	border: 1px solid #ccc;
	background: #eee;
	text-align: left;
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
	var content_id = "";
	var g_name = "";
	$(document).ready(function() {
		
		ctrl.initialize();
		/*
		$('.fc-prev-button span').click(function(){
			alert('prev is clicked, do something');
		});
		*/
		
		$("#go-search").click(function() {
			var param = {
					title : $("#form-title").val()
				};
			$.ajax({
				type : "POST",
				url : "getContents.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					getContents(data.contents);
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		});
	
	});

	
	var ctrl = {
		initialize : function() {
			
			var param = {
					serviceAreaId : $('#serviceAreaId').val()
					/*, searchDate : $('#searchDate').val()*/
				};
				
			$.ajax({
				type : "POST",
				url : "getSchedule.do",
				data : param,
				dataType : "json",
				async: false,
				success : function( data ) {
					setTimeTable(data);
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		}
	};
	
	function setDragEventFunction(){
		$('#external-events .fc-event').each(function() {
			// store data so the calendar knows to render an event upon drop
			$(this).data('event', {
				title: $.trim($(this).text()), // use the element's text as the event title
				stick: true // maintain when user navigates (see docs on the renderEvent method)
			});

			// make the event draggable using jQuery UI
			$(this).draggable({
				zIndex: 999,
				revert: true,      // will cause the event to go back to its
				revertDuration: 0  //  original position after the drag
			});

		});
	}
	
	function getContents(data){

		var $list = $("#external-events");
		
		// list 초기화
		$list.empty();
		if (data.length < 1){
			alert("No data.");
		}
			
		for ( var i=0; i<data.length; i++) {
			var $div = $("<div/>");
			var id = data[i].id;
			var title = data[i].title;
			$div.append(title);
			$div.attr("class","fc-event");
			$div.attr("data-id", id);
			$list.append( $div );
		}
		
		// pagging 초기화
		$(".pagging").empty();
		/*
		$("#content-table tbody").quickPager( {
			naviSize: 3,
			currentPage: 1,
			holder: ".pagging"
		});
		*/
		setDragEventFunction();
		$(".search-list").show();
	}
	
	
	function setTimeTable(data ){
		var searchDate = $("#searchDate").val();
		var contents = data.contents;
		var events = [];
		var schedule;
		for ( var i = 0; i < contents.length; i++) {
			var id = contents[i].ID;
			var name = contents[i].NAME;
			var broadcast_info_id = contents[i].BCID;
			
			var start_date = contents[i].start_date;
			var end_date = contents[i].end_date;
			var url = "schedule.do?id=" + id;
			
			if (broadcast_info_id == null || broadcast_info_id == "")
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#dddddd", textColor: "#787A7C", borderColor:"#bbbbbb"};
			else
				schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#23C6C8", textColor: "#ecf0f1", borderColor:"#1AB394"};
			
			//schedule = {start: start_date, end: end_date, title: name, url : url, backgroundColor:"#eeeeee"};
			events.push( schedule );
		}
   		
		$('#calendar').fullCalendar({
			schedulerLicenseKey: 'GPL-My-Project-Is-Open-Source',
			defaultView: 'agendaDay',
			editable: true, 			// enable draggable events
			droppable: true, 			// this allows things to be dropped onto the calendar
			header: {
				left: 'prev,next',
				center: 'title'
				,right: 'agendaDay'
			},
			defaultDate: searchDate,
			selectable: true,
			selectHelper: true,
			select: function(start, end) {
				var title = prompt('Event Title:');
				var eventData;
				if (title) {
					eventData = {
						title: title,
						start: start,
						end: end
					};
					$('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
				}
				console.log('select');
				$('#calendar').fullCalendar('unselect');
			},
			editable: true,
			eventLimit: true, // allow "more" link when too many events
			events: events,
			drop: function(event, dayDelta,minuteDelta,allDay,revertFunc) {
				content_id = $(this).attr("data-id");
				$(this).remove();
			},
			
			eventReceive: function(event) { // called when a proper external event is dropped
				// console.log('eventReceive', event, ',' , event.start.format(), ',' , content_id);
				g_name = event.title;
				var startTime = event.start.format();
				var endTime = moment(startTime).add(2, 'hours');
				endTime = endTime.format('YYYY-MM-DD[T]HH:mm:ss');
				//console.log('endTime', endTime);
				console.log(content_id ,',', g_name, ',', startTime, ',', endTime );
				addSchedule(content_id, g_name, startTime, endTime);
				//location.reload();
				
				var b = $('#calendar').fullCalendar('getDate');
			 	var searchDate = b.format('YYYY-MM-DD');
				var tmpServiceAreaId = $("#serviceAreaId").val();
				
				location.href = "schdMgmtDetail.do?serviceAreaId=" + tmpServiceAreaId + "&searchDate="+searchDate;
			},
			
			eventDrop: function(event) { // called when an event (already on the calendar) is moved
				console.log('eventDrop2', event, ',',event.start.format(), ',' , event.end.format() , event.url);
				
				modifySchedule(event.url, event.start.format(), event.end.format());
			}
			,eventResizeStop: function(event){		//Triggered when event resizing stops.
				console.log('event eventResizeStop', event, ',',event.start.format(), ',' , event.end.format() , event.url);
			}
			
			/*
			, eventAfterRenderfunction: function(event) { // called when an event (already on the calendar) is moved
				console.log('eventAfterRender..', event);
			}
			
			, eventRender: function(event, element) {
				console.log('eventRender', event, element);
		    }
			*/
		});
	}
	
	function modifySchedule(url, startTime, endTime){
		
		var id = url.substring(url.indexOf("=")+1);
		console.log('eventDrop2:', id);
		var param = {
				scheduleId : id,
				startTime : startTime,
				endTime: endTime
		};
		
		$.ajax({
			type : "POST",
			url : "modifyScheduleTime.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				console.log('Success to modify schedule');
			},
			error : function(request, status, error) {
				alert("error for update Time:request=" +request +",status=" + status + ",error=" + error);
			}
		});
		
	}
	function addSchedule(content_id, g_name, startTime, endTime){
		var param = {
				serviceAreaId : $("#serviceAreaId").val(),
				contentId : content_id,
				titleName : g_name,
				startTime : startTime,
				endTime: endTime
			};
			
		$.ajax({
			type : "POST",
			url : "addScheduleWithInitContent.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				alert('Success to add schedule');
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	
	}
	function popupShow(id, name){
		content_id = id;
		g_name = name;
		console.log(id +','+ g_name + ',' + ($(window).width() - 550));
		$(".pbox").css("top", $(window).scrollTop() + 250 + "px");
		$(".pbox").css("left", ($(window).width() - 630) + "px");
		//$(".pbox").css("left", 300 $(window).scrollLeft() + 100 + "px");
		
		$("#popupTitle").html(name);
		$("#addSchedule").show();
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
            <input type="hidden" id="serviceAreaId" name="serviceAreaId" value="${serviceAreaId}"/>
            <input type="hidden" id="searchDate" name="searchDate" value="${searchDate}"/>
            <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Schedule Mgmt : eEPG for ESPN</h5>
                            <div class="ibox-tools">
                                <!--a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a-->
                            </div>
                        </div>
                        
                        <div class="ibox-content">
                        <form method="get" class="form-horizontal">
                            <div class="row">
                                <div class="col-sm-8" id="epg-table">
									<div style = "width:90%;"id="calendar"></div>
                                </div>
                                <div class="col-sm-4">
                                    
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">Category</label>
                                            <!--div class="col-md-8">
                                                <select class="form-control input-sm">
                                                    <option value="">?</option>
                                                </select>
                                            </div-->
                                            <div class="col-md-8"><input type="text" id="form-category" class="form-control input-sm"></div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">Title</label>
                                            <div class="col-md-8"><input type="text" id="form-title" class="form-control input-sm"></div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-8 col-sm-offset-4">
                                            	<button class="btn btn-success btn-sm btn-block" type="button" id="go-search">Search</button>
                                           	</div>
                                        </div>
                                        <!-- 
<div id='external-events'>
			<h4>Draggable Events</h4>
			<div class='fc-event'>My Event 1</div>
			<div class='fc-event'>My Event 2</div>
			<div class='fc-event'>My Event 3</div>
			<div class='fc-event'>My Event 4</div>
			<div class='fc-event'>My Event 5</div>
			<p>
				<input type='checkbox' id='drop-remove' />
				<label for='drop-remove'>remove after drop</label>
			</p>
		</div>
                                         -->
		
                                        
                                        <div class="hr-line-dashed"></div>
                                        <div class="search-list" style="display:none">
                                        	
                                            <h5>Search Result</h5>
                                            <div id='external-events'>
                                            
                                            </div>
                                             <!-- 
                                            <table class="footable table table-bordered" data-page-size="10" id="content-table">
			                                    <tbody id="result_list">
			                                   
			                                    
			                                    </tbody>
			                                   
			                                    <tfoot>
			                                    <tr>
			                                        <td colspan="9">
			                                            <ul class="pagination pull-right">
			                                            </ul>
			                                        </td>
			                                    </tr>
			                                    </tfoot>
			                                     -->
			                                </table>
                                        </div>
                                    
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

<div class="popupbox" id="addSchedule" style="display:none">
	<div class="l_popup_bg"></div>
	<div class="pbox">
		<span class="lt"></span><span class="rt"></span><span class="lb"></span><span class="rb"></span>
		<div class="titbox">
			<h2 class="tit">
				schedule adding
			</h2>
		</div>
		<div class="pop_body">
			
			<div class="ibox-content">
              <div class="row">
				<form class="form-horizontal">
					<center><h3 id="popupTitle"></h3></center></br>
					<div class="form-group">
                        <label class="col-md-4 control-label">Start Time</label>
                        <div class="col-md-5"><input type="text" id="startTime" class="form-control input-sm"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">End Time</label>
                        <div class="col-md-5"><input type="text" id="timepicker" class="form-control input-sm"></div>
                    </div>
				</form>
				
			</div>
			</div>
			<div class="btnbox">
				<a href="javascript:;" name="ntCloseBtn">확인</a>
			</div>
		</div>
		<span class="btn_close">닫기</span>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		$('#timepicker').timepicki(); 
		
		$(".pbox span.btn_close").on("click", function() {
			$(".popupbox").hide();
		});
		
		$("a[name=ntCloseBtn]").on("click", function() {
			$(".popupbox").hide();
			//ajax add schedule
			
			alert('add schedule(content_id=' + content_id + ',g_name=' + g_name + ')');
			var param = {
				serviceAreaId : $("#serviceAreaId").val(),
				contentId : content_id,
				titleName : g_name,
				startTime : $("#startTime").val(),
				endTime: $("#endTime").val()
			};
			
			$.ajax({
				type : "POST",
				url : "addScheduleWithInitContent.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					alert('Success to add schedule');
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
			
			location.reload();
			//$('#schedule').empty();
			//ajax: load schedule
			//ctrl.initialize();
		});
	});
</script>
</html>
