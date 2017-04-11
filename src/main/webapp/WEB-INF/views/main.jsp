<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%= new java.util.Date() %>" />

<!DOCTYPE html>
<html>
<head>
	<link href="/dashbd/resources/newPublish/css/plugins/iCheck/custom.css" rel="stylesheet">
	<link href="css/plugins/datapicker/datepicker3.css" rel="stylesheet">
	<link href="../resources/css/sampleVideo.css" rel="stylesheet" />
	<script src="../resources/js/dash.all.debug.js"></script>
	<script src="../resources/js/sampleVideo.js"></script>
	
	<style type="text/css">
		.main .main-sch .tb_tpl1 #table > tbody > tr > td > table thead th {
		    padding: 3px 8px;
		    background-color: #1ab394;
		    text-align : left;
		}
		.main .main-sch .tb_tpl1 #table > tbody > tr > td> table {
		    border-top: solid 2px #1ab394;
		    border-bottom: solid 1px #1ab394; 
		}
	</style>
	
	<jsp:include page="common/head.jsp" />
	
	<style type="text/css">
		.table {
			font-size: 11px;
		}
	</style>
</head>
<body>
	<div id="wrapper" class="main">
		<!-- sidebar -->
		<jsp:include page="common/leftTab.jsp" />
		
		<div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 1014px;">
<%-- 			<jsp:include page="common/header.jsp" />	 --%>
			<c:import url="/resources/header.do"></c:import>
			<div class="row border-bottom white-bg dashboard-header">
				<div class="session">
					<h2>SeSM Main</h2>
<!-- 					<div class="pull-right"> -->
<%-- 						<span>Num Sessions: ${total_session}</span> --%>
<!-- 						<span> -->
<!-- 							<span class="onDisp on1"></span>활성화일때 클래스 on1, on2 -->
<!-- 							<span class="onDisp"></span> -->
<!-- 						</span> -->
<!-- 					</div> -->
				</div>
			</div>
			<!-- s : wrapper -->
			<div class="wrapper wrapper-content">
				<div class="main-sch">
					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-title">
									<h5>SeSM Main</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
								</div>
								<!-- // ibox-title -->
								
								<div class="ibox-content">
									<div class="row">
										<form class="form-horizontal" action="javascript:void(0);">
											<input type="hidden" id="choiceTreeStr" value="all">
											<div class="col-lg-7">
												<div class="form-group">
													<label class="col-sm-3 control-label">Service Type</label>
													<div class="col-sm-9">
														<div class="input-group">
															<select id="searchServiceType" class="input-sm form-control input-s-sm inline">
																<option value="">All</option>
																<option value="streaming">Streaming</option>
																<option value="fileDownload">File download</option>
																<option value="multiple">Carousel – Multiple Files</option>
																<option value="single">Carousel – Single File</option>
															</select>
															<span class="input-group-btn">
																<a href="#" class="btn btn-w-m btn-link"><i class="fa fa-link"></i> <u onclick="choiceServiceArea()">Select Service Area</u></a>
															</span>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label">Schedule</label>
													<div class="col-sm-9">
														<div class="row">
															<div class="col-lg-12">
																<div class="m-b">
																	<label class="checkbox-inline i-checks" onclick="radioCheck('')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="" name="searchSchedule" style="position: absolute; opacity: 0;" checked><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> All</label>
																	<label class="checkbox-inline i-checks" onclick="radioCheck('onair')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="onair" name="searchSchedule" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> On-Air</label>
																	<label class="checkbox-inline i-checks" onclick="radioCheck('today')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="today" name="searchSchedule" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> Today</label>
																	<label class="checkbox-inline i-checks" onclick="radioCheck('national')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="national" name="searchSchedule" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> National</label>
																</div>
															</div>
														</div>
														
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">From</label>
																	<div class="col-sm-10">
																		<div class="input-group date" id="data_1">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateFrom" value='<fmt:formatDate pattern="MM/dd/yyyy" value="${beforeDate}" />'>
										                                </div>
																	</div>
																</div>
															</div>
														</div>
														
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">To</label>
																	<div class="col-sm-10">
																		<div class="input-group date" id="data_2">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" id="searchDateTo" value='<fmt:formatDate pattern="MM/dd/yyyy" value="${now}" />'>
										                                </div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label">Search</label>
													<div class="col-sm-9">
														<div class="col-xs-3">
															<select id="searchType" name="searchType" class="form-control">
							                                    <option value="">Select</option>
							                                    <option value="serviceId">ServiceID</option>
							                                    <option value="serviceName">ServiceName</option>
							                                    <option value="uri">URI</option>
							                                    <option value="serviceClass">Service Class</option>
							                                </select>
														</div>
														<div class="col-xs-9">
															<div class="input-group">
																<input type="text" placeholder="Keyword" id="searchKeyword" class="form-control" onkeydown="javascript:if(event.keyCode == 13) searchRegionalSchedule(false);">
																<span class="input-group-btn">
																	<button type="button" class="btn btn-primary" onclick="searchRegionalSchedule(false)">Search</button>
																</span>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="col-lg-5 profile-content today">
												<h2 class="text-center"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}" /></h2>
												<div id="clock" class="light">
													<div class="digits"></div>
												</div>
												<div class="row total">
													<div class="col-sm-4">
														<div class="panel panel-primary text-center">
															<div class="panel-heading">Total Users</div>
															<div class="panel-body">
																<span class="text-info">${total_user}</span>
															</div>
														</div>
													</div>
													<div class="col-sm-4">
														<div class="panel panel-primary text-center">
															<div class="panel-heading">CountUC</div>
															<div class="panel-body">
																<span class="text-info">${count_uc}</span>
															</div>
														</div>
													</div>
													<div class="col-sm-4">
														<div class="panel panel-primary text-center">
															<div class="panel-heading">CountBC</div>
															<div class="panel-body">
																<span class="text-info">${count_bc}</span>
															</div>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
								</div> <!-- // ibox-content -->
							</div> <!-- ibox -->
						</div> <!-- // col -->
					</div> <!-- // row -->
					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-title">
									<h5>Schedule</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<div class="table-responsive tb_tpl1">
										<table id="table" class="table table-striped">
											<colgroup>
												<col>
												<col style="width: 9.5%;">
												<col style="width: 12%;">
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
											</colgroup>
											<thead>
												<tr class="headTr">
													<th></th>
													<th>Area</th>
													<th>Service ID</th>
													<th>Service Name</th>
													<th>Service type</th>
<!-- 													<th>Schedule Type</th> -->
													<th>Start time</th>
													<th>Stop time</th>
													<th>GBR</th>
													<th>FEC (%)</th>
													<th>Delivery Type</th>
													<th>#of Viewers</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>	
				</div> <!-- // main-sch -->
			</div> <!-- e : wrapper -->
			<jsp:include page="common/footer.jsp" />	
		</div>
	</div>
	
	<jsp:include page="serviceModal.jsp" />
	<jsp:include page="common/circleModal.jsp" />
	
	<!-- Custom and plugin javascript -->
	<script src="/dashbd/resources/newPublish/js/plugins/video/responsible-video.js"></script>
	<!-- iCheck -->
    <script src="/dashbd/resources/newPublish/js/plugins/iCheck/icheck.min.js"></script>
    <!-- Data picker -->
	<script src="/dashbd/resources/newPublish/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	
	<script src="js/jquery.cookie.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/digitalclock/script.js"></script>
	<script src="/dashbd/resources/js/moment.js"></script>
	
	<script>
		$.blockUI();
	
		$(document).ready(function() {
			jsTreeSetting();
			
			$('#data_1.input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });
			$('#data_2.input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });
			
			$('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
			
			getMenuList('MAIN');
			searchRegionalSchedule(true);
			
			$('#table').on('load-success.bs.table', function (e, obj) {
				arrangementTrData(obj.rows);
			});
		});
		
		function radioCheck(value) {
			if(value != '' && value != 'national') {
				$($("#searchDateFrom").parents(".row")[0]).hide();
				$($("#searchDateTo").parents(".row")[0]).hide();
			} else {
				$($("#searchDateFrom").parents(".row")[0]).show();
				$($("#searchDateTo").parents(".row")[0]).show();
			}
		}
		
		function arrangementTrData(rows) {
			var trList = $("table tr").not(".headTr");
			for(var i=0; i < trList.length; i++) {
				if(i == trList.length -2) break;
				
				var tempObj = rows[i];
				var tempNextObj = rows[i+1];
				
				if(tempObj.circleName != 'emergency' && tempObj.circleName != 'national' ) {
					if(tempObj.circleName == tempNextObj.circleName
							&& $(trList[i]).find("i").hasClass("fa-plus-square")) {
							$(trList[i]).find("i.fa.fa-plus-square").remove();
						}	
				}
			}
			
			$.unblockUI();
		}
		
		function searchRegionalSchedule(initYn) {
			$('#table').bootstrapTable('destroy');
			// 테이블 생성
			var pageNumber = 1;
			var table = $('#table').bootstrapTable({
				method: 'post',
				url: '/dashbd/api/searchRegionalSchedule.do',
				contentType: 'application/json',
				dataType: 'json',
				queryParams: function(params) {
					location.href = '#';
					pageNumber = $.cookie('pagaNumber', (params.offset / params.limit) + 1);
					params.searchServiceType = $("#searchServiceType").val();
					params.searchSchedule = $("form input[type='radio'][name='searchSchedule']:checked").val();
					params.searchDateFrom = $("#searchDateFrom").val();
					params.searchDateTo = $("#searchDateTo").val();
					params.searchType = $("#searchType").val();
					params.searchKeyword = $("#searchKeyword").val();
					params.choiceTreeStr = $("#choiceTreeStr").val();
					params.circle_id = $("#circleId").val();
// 					if(initYn) {
// 						params.searchDateFrom = '';
// 						params.searchDateTo =  '';
// 					}
					return params;
				},
				cache: false,
				pagination: true,
				sidePagination: 'server',
				pageNumber: pageNumber,
				pageSize: 20,
				search: false,
				showHeader: true,
				showColumns: false,
				showRefresh: false,
				minimumCountColumns: 3,
				clickToSelect: false,
				columns: [{
					field: 'scheduleId',
					title: '',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, {
					field: 'circleId',
					title: '',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, { 
					field: 'psaid',
					title: '',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, { 
					field: 'layerDiv',
					title: '',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, {
					field: 'onAirYn',
					title: 'onAirYn',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, {
					field: 'emergencyYn',
					title: 'emergencyYn',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, {
					field: 'serviceMode',
					title: 'serviceMode',
					width: '0%',
					align: 'left',
					valign: 'middle',
					sortable: false,
					visible: false
				}, { 
					field: 'circleName',
					title: 'Area',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						var html = '';
						
						if(value == 'emergency' || value == 'national') {
							if(row.serviceMode == 'MooD') 
								html += '<span style="cursor: pointer;" onclick="callNationalSubScheduleData(this, \'' + row.serviceId + '\', ' + JSON.stringify(row).replace(/\"/gi, "\'") + ')"><i class="fa fa-plus-square"></i> ' + value + '</span> ';
							else
								html = value;
						} else {
							if(row.subCnt > 0) 
								html += '<span style="cursor: pointer;" onclick="callSubScheduleData(this, \'' + row.layerDiv + '\', \'' + row.psaid + '\')"><i class="fa fa-plus-square"></i> ' + value + '</span> ';
							else
								html = value;	
						}
						
						return html;
					}
				}, {
					field: 'serviceId',
					title: 'Service ID',
					width: '13%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						if(value != undefined && value != '') {
							var onair = row.onAirYn == 'Y'? 'onair' : ''; 
	 						var html = '<i class="ondisp ' + onair + '"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup(\'' + row.onAirYn + '\', \'' + row.service + '\', \'' + row.fileURI + '\',  ' + JSON.stringify(row).replace(/\"/gi, "\'") + ')">' + row.serviceId + '</a>';	
						} else {
							var html = '';
						}
						 						
						return html;
					}
				}, {
					field: 'serviceName',
					title: 'Service Name',
					width: '12%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						if(value != undefined && value != '') {
							if($("#circleId").val() == '') {
								//Emergency_schedule
								if(row.emergencyYn == 'Y') {
									var html='<a href="javascript:void(0);" style="color:#ed5565;" onclick="moveScheduleDetail(\'' + row.scheduleId + '\')">' + value + '</a>';	
								} 
								//National Schedule
								else {
									var html='<a href="javascript:void(0);" onclick="moveScheduleDetail(\'' + row.scheduleId + '\')">' + value + '</a>';
								}
							} else if($("#circleId").val() != '' && row.circleName != 'national') {
								var html='<a href="javascript:void(0);" onclick="moveScheduleDetail(\'' + row.scheduleId + '\')">' + value + '</a>';
							} else {
								var html = value;
							}
						} else {
							var html = '';
						}
						
						return html;
					}
				}, {
					field: 'service',
					title: 'Service Type',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}, {
					field: 'scheduleType',
					title: 'Schedule Type',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					visible: false
				}, {
					field: 'scheduleStart',
					title: 'Start Time',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}, {
					field: 'scheduleStop',
					title: 'Stop Time',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}, {
					field: 'gbr',
					title: 'GBR',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}, {
					field: 'fecRatio',
					title: 'FEC (%)',
					width: '5%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						if(value != undefined) var html = value + '%'
						else var html = '';
						
						return html;
					}
				}, {
					field: 'deleveryType',
					title: 'Delevery Type',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}, {
					field: 'viewers',
					title: '#of Viewers',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
				}]
			});
		}
		
		function callSubScheduleData(targetObj, layerDiv, psaid) {
			if($(targetObj).find("i.fa-plus-square").length > 0) {
				targetObj = $(targetObj).parents("tr")[0];
				
				var tableHtml = '';
				tableHtml += '<tr name="sub' + $(targetObj).attr("data-index") + '" style="display: table-row;"><td colspan="12">';
				tableHtml += '<table class="table table-striped">';
				tableHtml += '<colgroup><col style="width: 11.8%;"><col style="width: 12%;">';
				tableHtml += '<col><col><col><col><col><col><col><col></colgroup>';
				
				$.ajax({
				    url : "/dashbd/api/getRegionalSubSchedule.do",
				    type: "POST",
				    data : { 
				    	layerDiv : layerDiv,
				    	psaid : psaid,
				    	searchServiceType : $("#searchServiceType").val(),
				    	searchSchedule : $("form input[type='radio'][name='searchSchedule']:checked").val(),
				    	searchDateFrom : $("#searchDateFrom").val(),
				    	searchDateTo : $("#searchDateTo").val(), 
				    	searchType : $("#searchType").val(),
				    	searchKeyword : $("#searchKeyword").val(),
				    	choiceTreeStr : $("#choiceTreeStr").val()
				    },
				    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				    success : function(responseData) {
				        $("#ajax").remove();
				        var data = JSON.parse(responseData).resultList;
				        
				        tableHtml += '<thead><tr><th>' + (data[0].layerDiv == 'hotspot'? 'City' : 'Hotspot') + '</th><th>Service ID</th><th>Service Name</th><th>Service type</th>';
						tableHtml += '<th>Start time</th><th>Stop time</th><th>GBR</th><th>FEC (%)</th>';
						tableHtml += '<th>Delivery Type</th><th>#of Viewers</th></tr></thead>';
						tableHtml += '<tbody>';
				        
				        for(var i=0; i < data.length; i++) {
				        	var row = data[i];
				        	var nRow = data[i+1];
				        	
				        	tableHtml += '<tr>';
				        	
				        	var proceedYn = true;
				        	if(row.subCnt > 0) {
				        		if(i != data.length-1 && (row.layerDiv == 'hotspot'? row.cityName : row.hotspotName) == (row.layerDiv == 'hotspot'? nRow.cityName : nRow.hotspotName)) 
				        			proceedYn = false;
				        	} else 
				        		proceedYn = false;
				        		
							if(proceedYn) {
								tableHtml += '<td><span style="cursor: pointer;" onclick="callSubScheduleData(this, \'hotspot\', \'' 
									+ row.cityId + '\')"><i class="fa fa-plus-square"></i> ' + (row.layerDiv == 'hotspot'? row.cityName : row.hotspotName) + '</span></td>';
							} else {
								tableHtml += '<td>' + (row.layerDiv == 'hotspot'? row.cityName : row.hotspotName) + "</td>"
							}
							
							var onair = row.onAirYn == 'Y'? 'onair' : ''; 
							tableHtml += '<td><i class="ondisp ' + onair + '"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup(\'' + row.onAirYn + '\', \'' + row.service + '\', \'' + row.fileURI + '\', ' + JSON.stringify(row).replace(/\"/gi, "\'") + ')">' + row.serviceId + '</a></td>'; 						
							tableHtml += '<td><a href="javascript:void(0);" onclick="moveScheduleDetail(\'' + row.scheduleId + '\')">' + row.serviceName + '</a></td>';
							tableHtml += '<td>' + row.service + '</td>';
// 							tableHtml += '<td>' + row.scheduleType + '</td>';
							tableHtml += '<td>' + row.scheduleStart + '</td>';
							tableHtml += '<td>' + row.scheduleStop + '</td>';
							tableHtml += '<td>' + row.gbr + '</td>';
							tableHtml += '<td>' + row.fecRatio + '%</td>';
							tableHtml += '<td>' + row.deleveryType + '</td>';
							tableHtml += '<td>' + row.viewers + '</td>';
							tableHtml += '</tr>';
				        }
				       	
				        tableHtml += '</tbody></table></td></tr>';
						$(targetObj).after(tableHtml);
						$(targetObj).find("i.fa-plus-square").addClass("fa-minus-square");
						$(targetObj).find("i.fa-plus-square").removeClass("fa-plus-square");
				    },
			        error : function(xhr, status, error) {
			        	swal({
			                title: "Fail !",
			                text: "Error"
			            });
			        }
				});
				
			} else if($(targetObj).find("i.fa-minus-square").length > 0) {
				targetObj = $(targetObj).parents("tr")[0]
				
				var trSubName = 'sub' + $(targetObj).attr("data-index");
				$("#table").find("tr[name='" + trSubName + "']").remove();
				$(targetObj).find("i.fa-minus-square").addClass("fa-plus-square");
				$(targetObj).find("i.fa-minus-square").removeClass("fa-minus-square");
			}
		}
		
		function callNationalSubScheduleData(targetObj, serviceId, rowData) {
			if($(targetObj).find("i.fa-plus-square").length > 0) {
				targetObj = $(targetObj).parents("tr")[0];
				
				var tableHtml = '';
				tableHtml += '<tr name="nationalSub' + $(targetObj).attr("data-index") + '" style="display: table-row;"><td colspan="12">';
				tableHtml += '<table class="table table-striped">';
				tableHtml += '<colgroup><col style="width: 11.8%;"><col style="width: 12%;">';
				tableHtml += '<col><col><col><col><col><col><col><col></colgroup>';
				
				$.ajax({
				    url : "/dashbd/api/getNationalSubSchedule.do",
				    type: "POST",
				    data : { 
				    	serviceId : serviceId,
				    	rowData : JSON.stringify(rowData).replace(/\"/gi, "\'")
				    },
				    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				    success : function(responseData) {
				        $("#ajax").remove();
				        var data = JSON.parse(responseData).resultList;
				        
			        	tableHtml += '<thead><tr><th>Area</th><th>Service ID</th><th>Service Name</th><th>Service type</th>';
						tableHtml += '<th>Start time</th><th>Stop time</th><th>GBR</th><th>FEC (%)</th>';
						tableHtml += '<th>Delivery Type</th><th>#of Viewers</th></tr></thead>';
						tableHtml += '<tbody>';
				        
				        for(var i=0; i < data.length; i++) {
				        	var row = data[i];
				        	tableHtml += '<tr>';
							tableHtml += '<td>' + row.circleName + "</td>"
							var onair = row.onAirYn == 'Y'? 'onair' : '';
							tableHtml += '<td><i class="ondisp ' + onair + '"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup(\'' + row.onAirYn + '\', \'' + row.service + '\', \'' + row.fileURI + '\', ' + JSON.stringify(row).replace(/\"/gi, "\'") + ')">' + row.serviceId + '</a></td>'; 						
							tableHtml += '<td><a href="javascript:void(0);" onclick="moveScheduleDetail(\'' + row.scheduleId + '\')">' + row.serviceName + '</a></td>';
							tableHtml += '<td>' + row.service + '</td>';
// 							tableHtml += '<td>' + row.scheduleType + '</td>';
							tableHtml += '<td>' + row.scheduleStart + '</td>';
							tableHtml += '<td>' + row.scheduleStop + '</td>';
							tableHtml += '<td>' + row.gbr + '</td>';
							tableHtml += '<td>' + row.fecRatio + '%</td>';
							tableHtml += '<td>' + row.deleveryType + '</td>';
							tableHtml += '<td>' + row.viewers + '</td>';
							tableHtml += '</tr>';
				        }
				       	
				        tableHtml += '</tbody></table></td></tr>';
						$(targetObj).after(tableHtml);
						$(targetObj).find("i.fa-plus-square").addClass("fa-minus-square");
						$(targetObj).find("i.fa-plus-square").removeClass("fa-plus-square");
				    },
			        error : function(xhr, status, error) {
			        	swal({
			                title: "Fail !",
			                text: "Error"
			            });
			        }
				});
				
			} else if($(targetObj).find("i.fa-minus-square").length > 0) {
				targetObj = $(targetObj).parents("tr")[0]
				
				var trSubName = 'nationalSub' + $(targetObj).attr("data-index");
				$("#table").find("tr[name='" + trSubName + "']").remove();
				$(targetObj).find("i.fa-minus-square").addClass("fa-plus-square");
				$(targetObj).find("i.fa-minus-square").removeClass("fa-minus-square");
			}
		}
		
		
		function choiceServiceArea() {
			$(".jstree li[role=treeitem]").each(function () {
			     $(".jstree").jstree('select_node', this);
			});
			
			$("#circleModal").modal('show');
		}
		
		function jsTreeSetting(openAllYn) {
			$.getScript( "/dashbd/resourcesRenew/js/plugins/jsTree/jstree.min.js" )
				.done(function( script, textStatus ) {
					$.ajax({
					    url : "/dashbd/api/getTreeNodeData.do",
					    type: "POST",
					    data : { 
					    	gruop_id : '',
					    	searchType : $("#searchType").val(),
					    	searchInput : $("#search-input").val(),
					    	circle_id : $("#circleId").val()
					    },
					    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
					    success : function(responseData) {
					        $("#ajax").remove();
					        var data = JSON.parse(responseData);
					         
					        if(data.resultList.length != 0) {
						        $("#treeNode").jstree("destroy").empty();
						        treeInit(data, openAllYn);
					        } else {
					        	swal({title:"Not Found !", text:"Please enter the keyword", type:"warning"}, function() {
					        		$("#search-input").val('');
					        		$("#searchType").val('');
					    		})
					        }
					        
					    },
				        error : function(xhr, status, error) {
				        	swal({
				                title: "Fail !",
				                text: "Error"
				            });
				        }
					});
				});
		}
		
		//jsTree Init Function
		function treeInit(data, openAllYn) {
			var treeData = data.resultList;
			for(var i=0; i < treeData.length; i++) {
				var node = treeData[i];
				
				var tempChildCntStr = ' (<span name="childCnt">' + node.childCnt + '</span> ' + node.childCntName + ')';
				if(tempChildCntStr == ' (<span name="childCnt"></span> )') tempChildCntStr = '';
				
				//root를 그려줌(Circles)
				if(i == 0) {
					$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + tempChildCntStr + '</li></ul>');
					continue;
				}
				
				//현재 붙여넣어 줄 노드의 구분값을 판단하여 그에 따른 상위 노드만 모아서 부모 노드를 찾음
				var divClass = '';
				if(node.node_div == 'circle') divClass='root';
				else if(node.node_div == 'city') divClass='circle';
				else if(node.node_div == 'hotspot') divClass='city';
				
				for(var j=0; j < $('#treeNode li.' + divClass).length; j++) {
					var compareNode = $('#treeNode li.' + divClass)[j];
					
					if($(compareNode).attr("data-init") == node.pnode_id) { 
						var liStr = '<li class="' + node.node_div + '" title="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' 
									+ node.latitude + '" data-lng="' + node.longitude + '" data-band="' + node.bandwidth + '">' + node.name + tempChildCntStr + '</li>';
						
						if($(compareNode).html().indexOf("ul") == -1) {
							$(compareNode).append('<ul>' + liStr + '</ul>');
						} else {
							$($(compareNode).find("ul")[0]).append(liStr);
						}
						
						break;
					}
				}
			}
			
			$("#treeNode")
				.bind('before_open.jstree', function(evt, data) {
					$(".jstree-icon.jstree-themeicon").remove();
					
					if(data.node.openYn == undefined) {
						$("#" + data.node.id + " li[role=treeitem]").each(function () {
						     $(".jstree").jstree('select_node', this);
						});	
					}
					
					data.node.openYn = 'Y';
				})
				.bind('ready.jstree', function(e, data) {
					$(".jstree-icon.jstree-themeicon").remove();
					arrangeTreeSearchData();
			    }).jstree({
			    	"checkbox" : {
			  	      "keep_selected_style" : false,
			  	      "three_state": false
			  	    },
				    "plugins" : [ "checkbox"]
				  });
			
			if(!openAllYn) {
				//제일 처음 노드 오픈
				$("#treeNode").jstree("open_node", $("#treeNode .root"));
			} else {
				$("#treeNode").jstree("open_all");
			}
		}

		$(document).on("keydown", "#search-input", function(event) {
			//Enter입력시에만 조회
			if(event.keyCode == 13) {
				searchTreeNode();
		    }
		})

		function searchTreeNode() {
			var searchString = $("#search-input").val();
			
			if(searchString == '') {
				$("#searchType").val('');
				jsTreeSetting(false);
			} else {
				jsTreeSetting(true);
			}
		}

		//검색시 childCnt 정리 메소드
		function arrangeTreeSearchData() {
			var searchResultList = $("#treeNode li");
			
			for(var i=0; i < searchResultList.length; i++) {
				var searchNode = $(searchResultList[i]);
				
				//일부 arae만 나올 경우는 모두 오픈
				if($("#circleId").val() != '') 
					$("#treeNode").jstree("open_all");
				
				if(!searchNode.hasClass("hotspot")) searchNode.find("span[name='childCnt']")[0].innerHTML = $(searchNode.find("ul")[0]).children().length;
			}
		}
		
		function choiceArea() {
			var circleListStr = '';
			var cityListStr = '';
			var hotspotListStr = '';
			
			var allNode = $("#treeNode ul li").not(".root");			
			for(var i=0; i < allNode.length; i++) {
				var tempObj = $(allNode[i]);
				
				if($(tempObj.find("a")[0]).hasClass("jstree-clicked")) {
					if(tempObj.hasClass("circle")) {
						circleListStr += ',' + tempObj.attr("data-init").substring(1);
					} else if(tempObj.hasClass("city")) {
						cityListStr += ',' + tempObj.attr("data-init").substring(tempObj.attr("data-init").indexOf("B")+1);
					} else if(tempObj.hasClass("hotspot")) {
						hotspotListStr += ',' + tempObj.attr("data-init").substring(tempObj.attr("data-init").indexOf("C")+1);
					}
				}
			}
			
			circleListStr = circleListStr.substring(1);
			cityListStr = cityListStr.substring(1); 
			hotspotListStr = hotspotListStr.substring(1); 
			
			var param = {
				circleListStr : circleListStr,
				cityListStr : cityListStr,
				hotspotListStr : hotspotListStr
			}
			
			$("#choiceTreeStr").val(JSON.stringify(param));
			$("#circleModal").modal("hide");
		}
		
		function deselectArea() {
			$(".jstree li[role=treeitem]").each(function () {
			     $(".jstree").jstree('deselect_node', this);
			});
		}
		
		function callDetailLayerPopup(onAirYn, serviceType, reqUrl, row) {
			if(onAirYn == 'Y' && serviceType == 'streaming') {
				$("#streamingArea").show();
				$("#infoArea").hide();
				$(".modal-title").text("Streaming Info");
// 				var url = "http://dash.edgesuite.net/dash264/TestCases/1c/qualcomm/2/MultiRate.mpd";	샘플동영상 
				var url = reqUrl;
				alert(url);
				var player = dashjs.MediaPlayer().create();
				player.initialize(document.querySelector("#Video1"), url, true);
				$("video")[0].play();
			} else {
				$("#streamingArea").hide();
				$("#infoArea").show();
				$(".modal-title").text("Service Info");
				
				var name = (row.circleName != undefined)? row.circleName : ((row.cityName != undefined)? row.cityName : row.hotspotName);    
				
				var innerHtml = '';
				innerHtml += '<div class="col-lg-12">';
					innerHtml += '<div class="form-group">';
						innerHtml += '<label class="col-sm-12" style="font-size: 20px;">' + name + ' Schedule</label>';
					innerHtml += '</div>';
					innerHtml += '<div class="form-group">';
						innerHtml += '<label class="col-sm-3">Service</label>';
						innerHtml += '<div class="col-sm-9">' + row.serviceId + '</div>';
					innerHtml += '</div>';
					innerHtml += '<div class="form-group">';
						innerHtml += '<label class="col-sm-3">Service Type</label>';
						innerHtml += '<div class="col-sm-9">' + row.service + '</div>';
					innerHtml += '</div>';
					innerHtml += '<div class="form-group">';
						innerHtml += '<label class="col-sm-3">Service Name</label>';
						innerHtml += '<div class="col-sm-9">' + row.serviceName + '</div>';
					innerHtml += '</div>';
					innerHtml += '<div class="form-group">';
						innerHtml += '<label class="col-sm-3">SAID List</label>';
						innerHtml += '<div class="col-sm-9">' + row.said + '</div>';
					innerHtml += '</div>';
				innerHtml += '</div>';
				$("#infoArea").html(innerHtml);
			}
			
			$("#serviceModal").modal('show');		
		}
		
		function moveScheduleDetail(scheduleId) {
			location.href = '/dashbd/view/schedule.do?id=' + scheduleId;
		}
	</script>
</body>
</html>
