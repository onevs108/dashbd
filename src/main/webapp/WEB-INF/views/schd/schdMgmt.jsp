<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Schedule Mgmt</title>
	
	<link href="/dashbd/resources/newPublish/css/bootstrap.min.css" rel="stylesheet">
	<link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet">
	
	<link href="../resourcesRenew/css/animate.css" rel="stylesheet">
	<link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet">
	<link href="../resourcesRenew/css/custom.css" rel="stylesheet">
	<link href="../resourcesRenew/css/timetable/timetablejs.css" rel="stylesheet" >
	<link href="../resourcesRenew/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="../resourcesRenew/css/plugins/footable/footable.core.css" rel="stylesheet">

    <!-- Mainly scripts -->
    <script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
    <script src="../resourcesRenew/js/bootstrap.min.js"></script>
    <script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="../resourcesRenew/js/plugins/sweetalert/sweetalert.min.js"></script>

    <!-- FooTable -->
    <script src="../resources/js/plugins/footable/footable.all.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="../resources/js/inspinia.js"></script>
	<script src="../resources/js/plugins/pace/pace.min.js"></script>
	
	<script src="../resources/js/common.js"></script>
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
	<script src="/dashbd/resources/js/plugins/jsTree/jstree.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/blockUI/blockUI.js"></script>
	<!-- Page-Level Scripts -->
	<script>
		var userGrade = ${userGrade};
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			
			getMenuList('SCHEDULE_MGMT');
			$("#selectCircle").val("");
// 			$($("input[name='radio']")[2]).click();
		});
		
		function sendMood() {
			$.ajax({
		        url : "receviceMoodRequest.do",
		        type: "post"
		    });
		}
	</script>
    	
    <script src="../resourcesRenew/js/plugins/fullcalendar/moment.min.js"></script>
	<script src="../resourcesRenew/js/timetable/timetable.min.js"></script>
	
	<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
	<script src="../resources/js/markerwithlabel.js"></script>
	<script src="../resources/app-js/apps/svc_schd.js"></script>
	<script src="../resources/app-js/apps/svc_main_forSchd.js"></script>
        
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
	
	 .timeline {
     position: absolute;    
     border: 2px dotted blue;
     width: 1px;
     margin: 0;
     padding: 0;
     z-index: 9;
     height: auto;
 }
	
	</style>
	
</head>

<body>
<div id="wrapper">
	<jsp:include page="../common/leftTab.jsp" />
	<div id="page-wrapper" class="gray-bg">
        <c:import url="/resources/header.do"></c:import>
		<div class="wrapper wrapper-content">
		<input type="hidden" id="searchDate" name="searchDate" value="${searchDate}">
			<!-- Schedule Mgmt -->
			<div class="row" id="viewProgram">
				<div class="col-md-12 ">
					<div class="ibox float-e-margins ibox-title">
						<div class="ibox-title">
							<div>
								<h5 style="padding-right: 20px;">Schedule</h5>
								<button type="button" class="btn btn-primary btn-xs" id="sendMood" style="float: right;" onclick="sendMood()">Send MoodData Test</button>
								<button type="button" class="btn btn-primary btn-xs" id="btnScheduleDetail" style="float: right;">Create Schedule</button>
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
		                <div class="form-group">
		                	<div class="ibox-content">
	                            <div class="row">
	                            	<div class="col-lg-12">
	                            	<div class="eepg_timeline">
		                                <div class="timetable"></div>
		                            </div>
	                            	</div>
	                            </div>
	                        </div>
	                    </div>   
						<!-- end ibox-content -->
					</div>
				</div>
			</div>
			<!-- end Contents Being Serviced -->
		</div><!-- end wrapper wrapper-content -->
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

</body>
</html>
