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
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
	<script src="../resources/js/jquery-2.1.1.js"></script>
	<style type="text/css">
		.main .main-sch .tb_tpl1 #table > tbody>tr > td > table thead th {
		    padding: 3px 8px;
		    background-color: #6f6f6f;
		    text-align : left;
		}
		.main .main-sch .tb_tpl1 #table > tbody > tr > td> table {
		    border-top: solid 2px #000;
		    border-bottom: solid 1px #000; 
		}
	</style>
	
	<jsp:include page="common/head.jsp" />
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
					<div class="pull-right">
						<span>Num Sessions:</span>
						<span>
							<span class="onDisp on1"></span><!-- 활성화일때 클래스 on1, on2 -->
							<span class="onDisp"></span>
						</span>
					</div>
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
										<form class="form-horizontal">
											<input type="hidden" id="circleListStr" value="all">
											<div class="col-lg-8">
												<div class="form-group">
													<label class="col-sm-2 control-label">Service Type</label>
													<div class="col-sm-10">
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
													<label class="col-sm-2 control-label">Schedule</label>
													<div class="col-sm-10">
														<div class="row">
															<div class="col-lg-12">
																<div class="m-b">
																	<label class="checkbox-inline i-checks" onclick="radioCheck('')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="" name="searchSchedule" style="position: absolute; opacity: 0;" checked><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> All</label>
																	<label class="checkbox-inline i-checks" onclick="radioCheck('onair')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="onair" name="searchSchedule" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> On-Air</label>
																	<label class="checkbox-inline i-checks" onclick="radioCheck('today')"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="today" name="searchSchedule" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> Today</label>
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
													<label class="col-sm-2 control-label">Search</label>
													<div class="col-sm-10">
														<div class="input-group">
															<input type="text" placeholder="Keyword" id="searchKeyword" class="form-control" onkeydown="javascript:if(event.keyCode == 13) searchRegionalSchedule(false);">
															<span class="input-group-btn">
																<button type="button" class="btn btn-primary" onclick="searchRegionalSchedule(false)">검색</button>
															</span>
														</div>
													</div>
												</div>
											</div>
											<div class="col-lg-4 profile-content today">
												<h2 class="text-left"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}" /></h2>
												<div id="clock" class="light">
													<div class="digits"></div>
												</div>
												<h3 class="font-bold no-margins text-left total">
													<div class="panel panel-primary">
														<div class="panel-heading">Total Users</div>
														<div class="panel-body">
															<p class="text-info">${total_users}</p>
														</div>
													</div>
												</h3>
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
									<h5>Regional Schedule</h5>
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
													<th>Circle</th>
													<th>Service ID</th>
													<th>Service Name</th>
													<th>Service type</th>
													<th>Schedule Type</th>
													<th>Start time</th>
													<th>Stop time</th>
													<th>GBR</th>
													<th>FEC (%)</th>
													<th>Delivery Type</th>
													<th>#of Viewers</th>
												</tr>
											</thead>
											<tbody>
<%-- 												<c:forEach var="obj" items="${resultList}" varStatus="status"> --%>
<%-- 													<c:choose> --%>
<%-- 														<c:when test="${obj.onAirYn == 'Y'}"> --%>
<%-- 															<c:set value="onair" var="onairYn"/> --%>
<%-- 														</c:when> --%>
<%-- 														<c:otherwise> --%>
<%-- 															<c:set value="" var="onairYn"/> --%>
<%-- 														</c:otherwise> --%>
<%-- 													</c:choose> --%>
													<tr name="tr${status.index}">
														<td><c:if test="${obj.subCnt > 0}"><span style="cursor: pointer;" onclick="callSubScheduleData(this, '${obj.layerDiv}', '${obj.psaid}')"><i class="fa fa-plus-square"></i></span></c:if></td>
														<td name="circleName">${obj.circleName}</td>
														<td name="serviceId"><i class="ondisp ${onairYn}"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup('${obj.scheduleType}', '${obj.serviceId}')">${obj.serviceId}</a></td>
														<td name="serviceName">${obj.serviceName}</td>
														<td name="service">${obj.service}</td>
														<td name="scheduleType">${obj.scheduleType}</td>
														<td name="scheduleStart">${obj.scheduleStart}</td>
														<td name="scheduleStop">${obj.scheduleStop}</td>
														<td name="gbr">${obj.gbr}</td>
														<td name="fecRatio">${obj.fecRatio}%</td>
														<td name="deleveryType">${obj.deleveryType}</td>
														<td name="viewers">${obj.viewers}</td>
													</tr>												
<%-- 												</c:forEach> --%>
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
	<script src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.0.0/moment.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/digitalclock/script.js"></script>
	<!-- iCheck -->
    <script src="/dashbd/resources/newPublish/js/plugins/iCheck/icheck.min.js"></script>
    <!-- Data picker -->
	<script src="/dashbd/resources/newPublish/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	
	<script src="js/jquery.cookie.js"></script>
	<script>
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
			if(value != '') {
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
				
				if(tempObj.circleName == tempNextObj.circleName
					&& $(trList[i]).find("i").hasClass("fa-plus-square")) {
					$(trList[i]).find("i.fa.fa-plus-square").remove();
				}
			}
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
					params.searchKeyword = $("#searchKeyword").val();
					params.circleListStr = $("#circleListStr").val();
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
				pageSize: 10,
				search: false,
				showHeader: true,
				showColumns: false,
				showRefresh: false,
				minimumCountColumns: 3,
				clickToSelect: false,
				columns: [{
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
					field: 'circleName',
					title: 'Circle',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						var html = '';
						if(row.subCnt > 0) 
							html += '<span style="cursor: pointer;" onclick="callSubScheduleData(this, \'' + row.layerDiv + '\', \'' + row.psaid + '\')"><i class="fa fa-plus-square"></i>' + value + '</span> ';
						else
							html = value;
						
						return html;
					}
				}, {
					field: 'serviceId',
					title: 'Service ID',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true,
					formatter: function(value, row, index) {
						if(value != undefined && value != '') {
							var onair = row.onAirYn == 'Y'? 'onair' : ''; 
	 						var html = '<i class="ondisp ' + onair + '"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup(\'' + row.service + '\', \'' + row.serviceId + '\')">' + row.serviceId + '</a>';	
						} else {
							var html = '';
						}
						 						
						return html;
					}
				}, {
					field: 'serviceName',
					title: 'Service Name',
					width: '10%',
					align: 'left',
					valign: 'middle',
					sortable: true
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
					sortable: true
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
					width: '10%',
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
				    	searchKeyword : $("#searchKeyword").val()
				    },
				    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				    success : function(responseData) {
				        $("#ajax").remove();
				        var data = JSON.parse(responseData).resultList;
				        
				        tableHtml += '<thead><tr><th>' + (data[0].layerDiv == 'hotspot'? 'City' : 'Hotspot') + '</th><th>Service ID</th><th>Service Name</th><th>Service type</th>';
						tableHtml += '<th>Schedule Type</th><th>Start time</th><th>Stop time</th><th>GBR</th><th>FEC (%)</th>';
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
							tableHtml += '<td><i class="ondisp ' + onair + '"></i> <a style="cursor: pointer;" onclick="callDetailLayerPopup(\'' + row.onAirYn + '\', \'' + row.service + '\', \'' + row.serviceId + '\')">' + row.serviceId + '</a></td>'; 						
							
							tableHtml += '<td>' + row.serviceName + '</td>';
							tableHtml += '<td>' + row.service + '</td>';
							tableHtml += '<td>' + row.scheduleType + '</td>';
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
			} else {
				
			}
		}
		
		function choiceServiceArea() {
			$("#circleModal").modal('show');
		}
		
		function jsTreeSetting() {
			$.getScript( "/dashbd/resources/js/plugins/jsTree/jstree.min.js" )
				.done(function( script, textStatus ) {
					$.ajax({
					    url : "/dashbd/api/getTreeNodeData.do",
					    type: "POST",
					    data : { 
					    	circle_id : ''
					    },
					    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
					    success : function(responseData) {
					        $("#ajax").remove();
					        var data = JSON.parse(responseData);
					        
					        $("#treeNode").jstree("destroy").empty();
					        treeInit(data);
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
		function treeInit(data) {
			var treeData = data.resultList;
			for(var i=0; i < treeData.length; i++) {
				var node = treeData[i];
				
				//root를 그려줌(Circles)
				if(i == 0) {
					$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + '</li></ul>');
					continue;
				}
				
				//현재 붙여넣어 줄 노드의 구분값을 판단하여 그에 따른 상위 노드만 모아서 부모 노드를 찾음
				var divClass = '';
				if(node.node_div == 'circle') divClass='root';
				else divClass='circle';
				
				for(var j=0; j < $('#treeNode li.' + divClass).length; j++) {
					var compareNode = $('#treeNode li.' + divClass)[j];
					
					if($(compareNode).attr("data-init") == node.pnode_id) {
						
						var liStr = '<li class="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' + node.latitude + '" data-lng="' + node.longitude + '">' + node.name + '</li>';
						
						if($(compareNode).html().indexOf("ul") == -1) {
							$(compareNode).append('<ul>' + liStr + '</ul>');
						} else {
							$($(compareNode).find("ul")[0]).append(liStr);
						}
						
						break;
					}
				}
			}
			
			$('#treeNode').bind('ready.jstree', function (event, data) {
				$('#treeNode ul li').each(function() {
			        $("#treeNode").jstree('check_node', $(this));
			    });
			})
			.jstree({"checkbox" : {
			      "keep_selected_style" : false,
// 			      "three_state": false
			       },
			      'plugins':["checkbox"]
			    });
			
			//제일 처음 노드 오픈
			$("#treeNode").jstree("open_node", $($("#treeNode li")[0]));
		}
		
		function choiceArea() {
			var circleListStr = '';
			
			var allNode = $("#treeNode ul li").not(".root");			
			for(var i=0; i < allNode.length; i++) {
				var tempObj = $(allNode[i]);
				
				if(tempObj.find("a").hasClass("jstree-clicked")) {
					circleListStr += ',' + tempObj.attr("data-init").substring(1);
				}
			}
			
			circleListStr = circleListStr.substring(1); 
			$("#circleListStr").val(circleListStr);
			
			$("#circleModal").modal("hide");
		}
		
		function callDetailLayerPopup(onAirYn, serviceType, serviceId) {
			if(onAirYn == 'Y' && serviceType == 'streaming') {
				$("#streamingArea").show();
				$("#infoArea").hide();
				$(".modal-title").text("Streaming Info");
			} else {
				$("#streamingArea").hide();
				$("#infoArea").show();
				$(".modal-title").text("Service Info");
			}
			
			$("#serviceModal").modal('show');		
		}
	</script>
</body>
</html>
