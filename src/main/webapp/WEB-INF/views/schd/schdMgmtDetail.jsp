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
    <link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet">
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
	
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	<script src="../resourcesRenew/app-js/apps/schdMgmtDetail.js"></script>
	
	<script src="../resources/js/common.js"></script>
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
	<script src="/dashbd/resources/js/plugins/jsTree/jstree.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/blockUI/blockUI.js"></script>
	<!-- Page-Level Scripts -->
	<script>
		var userGrade = ${userGrade};
		$(document).ready(function() {
			getMenuList('SCHEDULE_MGMT');
			$("#selectCircle").val("");
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
	
	</style>
	
<script>

</script>
</head>
<body>
<div id="wrapper">
    <jsp:include page="../common/leftTab.jsp" />
    <div id="page-wrapper" class="gray-bg">
        <c:import url="/resources/header.do"></c:import>
        <div class="wrapper wrapper-content">
            <!-- Contents -->
            <div class="row">
            <input type="hidden" id="serviceAreaId" name="serviceAreaId" value="${serviceAreaId}"/>
            <input type="hidden" id="bmscId" name="bmscId" value="${bmscId}"/>
            <input type="hidden" id="searchDate" name="searchDate" value="${searchDate}"/>
            <input type="hidden" id="category" name="category" value="${category}"/>
            <input type="hidden" id="title" name="title" value="${title}"/>
            <input type="hidden" id="type" name="type" value="${type}"/>
            <input type="hidden" id="contentsType" name="ContentsType" value="file"/>
            
            <div class="col-lg-12">
                    <div class="ibox float-e-margins ibox-title">
                        <div class="ibox-title">
							<div>
								<h5 style="padding-right: 20px;">Schedule</h5>
<!-- 								<button type="button" class="btn btn-primary btn-xs" id="sendMood" style="float: right;" onclick="sendMood()">Send MoodData Test</button> -->
<!-- 								<button type="button" class="btn btn-primary btn-xs" id="btnScheduleDetail" style="float: right;">Create Schedule</button> -->
								<span id="emergency"><input type="radio" class="btn btn-primary btn-xs" name="radio" value="emergency"/>Emergency</span>
								<span id="national"><input type="radio" class="btn btn-primary btn-xs" name="radio" value="national" checked/>National</span>
								<input type="radio" class="btn btn-primary btn-xs" name="radio" value="area"/>Service Area&nbsp;
								<input type="radio" class="btn btn-primary btn-xs" name="radio" value="group"/>Service Area Group&nbsp;
							</div>
						</div>
						<div class="row" style="margin-left: 1px;">
							<div id="selectArea" class="form-group" style="display: none;">
								<label class="col-sm-2" style="margin-top: 5px;width: 12%;">Area</label>
								<div class="col-sm-3" style="width: 20%;">
		                       		<select id="selectCircle" class="input-sm form-control input">
		                               <option value="">Select Area</option>
		                               <c:forEach var="row" items="${circleList}">
		                               	<option value="${row.circle_id}^${row.circle_name}">${row.circle_name}</option>
		                               </c:forEach>
		                            </select>
								</div>
								<label id="selectCityLabel" class="col-sm-2" style="margin-top: 5px;width: 12%;">City</label>
								<div class="col-sm-3" style="width: 20%;">
									<select id="selectCity" class="input-sm form-control input">
		                               
		                            </select>
		                        </div>
		                        <label id="selectHotspotLabel" class="col-sm-2" style="margin-top: 5px;width: 12%;">Hot Spot</label>
								<div class="col-sm-3" style="width: 20%;">
									<select id="selectHotspot" class="input-sm form-control input">
		                               
		                            </select>
		                        </div>
	                        </div>
		                </div>
		                <hr style="margin-top: 10px;margin-bottom: 10px;">
		                <div class="row" style="margin-left: 1px;margin-bottom: 10px;">
							<div class="form-group">
								<label class="col-sm-2" style="margin-top: 5px;width: 12%;">Service Type</label>
								<div class="col-sm-3" style="width: 20%;">
									<select id="serviceType" class="input-sm form-control input">
		                               <option value="">All</option>
		                               <option value="fileDownload">File Download</option>
                                       <option value="streaming">Streaming</option>
                                       <option value="carouselMultiple">Carousel Multiple Files</option>
                                       <option value="carouselSingle">Carousel Single File</option>
		                            </select>
	                            </div>
	                            <label class="col-sm-2" style="margin-top: 5px;width: 12%;">Service Class</label>
								<div class="col-sm-3" style="width: 20%;">
									<select id="serviceClass" class="input-sm form-control input">
		                               <option value="">Select Class</option>
		                               <c:forEach var="row" items="${scList}">
		                               	<option value="${row.class_name}">${row.class_name}</option>
		                               </c:forEach>
		                            </select>
	                            </div>
								<div class="col-sm-2" style="width: 15%;">
									<button id="scheduleSearch" class="btn btn-primary btn-sm">Search</button>
	                            </div>
							</div>
						</div>
                        <div class="ibox-content">
                        <form method="get" class="form-horizontal">
                        	<input type="text" style="display: none;">	<!-- form submit 방지용 -->
                            <div class="row">
                                <div class="col-sm-8" id="epg-table">
									<div style="width:100%;" id="calendar"></div>
                                </div>
                                <div class="col-sm-4">
                                	<div class="tabs-container">
										<ul class="nav nav-tabs">
										    <li class="active"><a id="tab1" href="#tab-1" data-toggle="tab" onclick="loadContentList(1, 'file')">FileDownload</a></li>
										    <li><a id="tab2" href="#tab-2" data-toggle="tab" onclick="loadContentList(1, 'streaming')">Streaming</a></li>
										</ul>
									</div>
                                    <div class="form-group" style="margin: 10px 20px -10px -40px;">
                                        <label class="col-md-4 control-label">Title</label>
                                        <div class="col-md-8"><input type="text" id="form-title" class="form-control input-sm" value="${title}" onkeyup="searchContents()"></div>
                                    </div>
                                    <div class="hr-line-dashed"></div>
                                    <div class="search-list" style="display:none">
                                        <div id='external-events'></div>
                                    </div>
                                    <div id='paging'></div>
                                </div>
                            </div>
                        </form>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="deleteAbortModal.jsp"/>
        </div><!-- content body end -->
    </div><!-- content end -->

</div><!-- wrapper end -->
</body>
</html>
