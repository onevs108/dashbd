<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<link href="../resourcesRenew/css/custom.css" rel="stylesheet">
    <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/datetimepicker/jquery.datetimepicker.min.css" rel="stylesheet">

    <!-- Mainly scripts -->
	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
	<script src="../resourcesRenew/js/jquery.form.js"></script>
	<script src="/dashbd/resources/js/jquery.cookie.js"></script>
	<script src="../resourcesRenew/js/bootstrap.min.js"></script>
	<script src="../resourcesRenew/js/bootstrap-table.js"></script>
	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="../resourcesRenew/js/jsrender.min.js"></script>
	<script src="../resources/newPublish/js/plugins/filestyle/bootstrap-filestyle.js"> </script>
	
	<!-- FooTable -->
	<script src="../resourcesRenew/js/plugins/footable/footable.all.min.js"></script>
	<script src="../resourcesRenew/js/plugins/sweetalert/sweetalert.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/js/plugins/pace/pace.min.js"></script>
	<script src="../resourcesRenew/js/plugins/chosen/chosen.jquery.js"></script>
	<script src="../resourcesRenew/js/plugins/iCheck/icheck.min.js"></script>
	<script src="../resourcesRenew/js/plugins/pace/pace.min.js"></script>
	<script src="../resourcesRenew/js/inspinia.js"></script>
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	<script src="../resourcesRenew/app-js/apps/schedule.js"></script>
	
	<script src="../resources/js/common.js"></script>
	<link href="/dashbd/resources/css/plugins/jsTree/style.min.css" rel="stylesheet">
	<script src="/dashbd/resources/js/plugins/jsTree/jstree.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/blockUI/blockUI.js"></script>
	<script src="../resourcesRenew/js/plugins/datetimepicker/jquery.datetimepicker.full.min.js"></script>
	
 	<!-- ax5ui -->
<!-- 	<link href="../resourcesRenew/css/plugins/ax5ui/ax5formatter.css" rel="stylesheet"> -->
<!-- 	<script src="../resourcesRenew/js/plugins/ax5ui/ax5core.js"></script> -->
<!-- 	<script src="../resourcesRenew/js/plugins/ax5ui/ax5formatter.js"></script> -->
	
	<!-- Page-Level Scripts -->
	<script>
		var serviceType = "${mapSchedule.service}";
		var contentJson = ${contentListJson};
		var viewMode = "${mode}";
		var contentsType = "${contentsType}";
		$(document).ready(function() {
			getMenuList('SCHEDULE_MGMT');
			if($("#moodLocation").val() == "") {
				$("#moodLocation").val("MBMS SAI");
			}
			$("#schedule_start").blur(function(){
				if($("#serviceType").val() == "fileDownLoad"){
					checkScheduleStartTime();
				}
				scheduleTimeSync(this);
			});
			$($("input[name='deliveryInfo_start']")[0]).blur(function(){
				if($("#serviceType").val() == "fileDownLoad"){
					checkScheduleEndTime();
				}
				scheduleTimeSync(this);
				return false;
			});
			$($("input[name='deliveryInfo_end']")[0]).blur(function(){
				if($("#serviceType").val() == "fileDownLoad"){
					checkScheduleEndTime();
				}
				scheduleTimeSync(this);
				return false;
			});
			$("#schedule_start, input[name='deliveryInfo_start'], input[name='deliveryInfo_end']").datetimepicker({
				format:'Y-m-d H:i:s',
			});
			$("button[name='addSchedule']").hide();
			
			if(viewMode == "update") {
				if("${mapSchedule.reportType}" != ""){
					$("#reportType").val("${mapSchedule.reportType}");	
				}
				if("${mapSchedule.fileRepair}" == "on"){
					$("#fileRepair").prop('checked', true);
				}
				
				if("${mapSchedule.receptionReport}" == "on"){
					$("#receptionReport").prop('checked', true);
				}
				
				if("${mapSchedule.reportClientId}" == "on"){
					$("#reportClientId").prop('checked', true);
				}
				
				for (var i = 0; i < contentJson.length; i++) {
					if(i != 0) {
						$("button[name='addContent']").click();	
					}
					$($("input[name='contentId']")[i]).val(contentJson[i].content_id);
					$($("input[name='fileURI']")[i]).val(contentJson[i].url);
					$($("input[name='deliveryInfo_start']")[i]).val(contentJson[i].start_time);
					$($("input[name='deliveryInfo_end']")[i]).val(contentJson[i].end_time);
					$($("input[name='duration']")[i]).val(contentJson[i].duration);
				}
				
				if(contentJson.length > 0) {
					if(contentJson[0].r12mpdURI != undefined){
						$($("input[name='r12mpdURI']")[0]).val(contentJson[0].r12mpdURI);
						var pattern = contentJson[0].bcBasePattern.split(",");
						for (var i = 0; i < pattern.length; i++) {
							if(i != 0) {
								$("#addBcPattern").click();
							}
							$($("input[name='bcBasePattern']")[i]).val(pattern[i]);
						}
					}
				}
				
				$("#serviceMode").val("${mapSchedule.serviceMode}");
				$("#serviceMode").change();
				$("#serviceClass").val("${mapSchedule.serviceClass}");
				$("#fileUpload").remove();
				$("#fileUpload_F").remove();
				$("#bcSaidList").val("${mapSchedule.bcServiceArea}");
			}
			else
			{
				scheduleTimeSync($("#schedule_start")[0]);
			}
			
			$("#fileRepair").change();
			$("#receptionReport").change();
			$("#reportClientId").change();
			
			$("#searchContentStream").click(searchStreaming);
		});
		
		function checkRequireValue() {
			$("#serviceType").prop('disabled', true);
			$("#serviceId").prop('disabled', true);
			$("#serviceClass").prop('disabled', true);
			var now = getTimeStamp();
			if(now >= $("#schedule_start").val()) {	 //시작 시간이 지난 경우
				$("#serviceMode").prop('disabled', true);
				$("#GBR").prop('disabled', true);
				$("#QCI").prop('disabled', true);
				$("#fecType").prop('disabled', true);
				$("#fecRatio").prop('disabled', true);
				$("#segmentAvailableOffset").prop('disabled', true);
				$("#mpdURI").prop('disabled', true);
				$("#searchContentStream").remove();
			}
			$("#newId").remove();
			$("#newClass").remove();
		}
		
		function searchStreaming() {
			$("#type").val("streaming");
			$("#contentList").modal();
		}
		
		function addFileSchedule() {
			$("#fileDownloads").append($("#scheduleForm").render());
			addScheduleRemoveEvent();
			addContentRemoveEvent();
			if(serviceType == "fileDownload")
			{
				$("div[name='bcType_streaming2']").hide();
			}
			else if($("#serviceType").val() == "carouselMultiple") 
			{
				$("div[name='contentStartStop']").hide();
				$("div[name='bcType_streaming2']").hide();
			}
			else if($("#serviceType").val() == "carouselSingle") 
			{
				$("div[name='contentPlus']").hide();	
				$("div[name='contentStartStop']").hide();	
				$("div[name='bcType_streaming2']").hide();
			}
			else if($("#serviceType").val() == "streaming") 
			{
				$("div[name='bcType_streaming2']").show();
				$("div[name='bcType_fileDownload']").hide();
				addServiceAreaEvent($("div[name='bcType_streaming2']").length-1);
			}
		}
		
		function addFileContent(e) {
			var deliveryIdx = $("input[name='deliveryInfo_start']").length;
			var ctIdx = $("button[name='addContent']").index(e);					//스케쥴 갯수(index)
			var content = $($("div[name='content']")[ctIdx]).children().last();
			$(content).after(content.clone());
			var lastContent = $($("div[name='content']")[ctIdx]).children().last();
			$(lastContent).find("input[name='fileURI']").val("");
			$(lastContent).find("input[name='deliveryInfo_start']").val("");
			$(lastContent).find("input[name='deliveryInfo_end']").val("");
			$($("#contentLength")[ctIdx]).val($($("div[name='content']")[ctIdx]).children().length);
			addContentRemoveEvent();
			addSearchContentEvent($($("#contentLength")[ctIdx]).val()-1);
			
			$("input[name='deliveryInfo_start'], input[name='deliveryInfo_end']").datetimepicker({
				format:'Y-m-d H:i:s',
			});
			$($("input[name='deliveryInfo_start']")[deliveryIdx]).blur(function(){
				if($("#serviceType").val() == "fileDownLoad"){
					checkScheduleEndTime();
				}
				scheduleTimeSync($(lastContent).find("input[name='deliveryInfo_start']")[0]);
				return false;
			});
			$($("input[name='deliveryInfo_end']")[deliveryIdx]).blur(function(){
				if($("#serviceType").val() == "fileDownLoad"){
					checkScheduleEndTime();
				}
				scheduleTimeSync($(lastContent).find("input[name='deliveryInfo_end']")[0]);
				return false;
			});
		}
		
		function removePattern(e) {
			var idx = $("i[name='removePattern']").index(e) + 1;
			$($("div[name='bcPattern']")[idx]).remove();
		}
		
		function checkScheduleStartTime() {
			var scheduleS = $("#schedule_start").val();
			var contentS = $($("input[name='deliveryInfo_start']")[0]).val();
			if(scheduleS > contentS){
				alert("schedule_start check");
				$("#schedule_start").val("");
			}
		}
		
		function checkScheduleEndTime() {
			var scheduleE = $("#schedule_stop").val();
			var contentE = $($("input[name='deliveryInfo_end']")[0]).val();
			if(scheduleE < contentE){
				alert("deliveryInfo_end check");
				$("#schedule_stop").val("");
			}
		}
				
		$(window).load(function(){
			if(viewMode == "update" && "${mapSchedule.BCID}" != null && "${mapSchedule.BCID}" != '' ){
				checkRequireValue();	
			}
		})
 	</script>

	<style>
	ul
	{
	    list-style-type: none;
	}
	.text-importance {
		color: red;
	}
	</style>
	
</head>
<body>
<div id="wrapper">
    <jsp:include page="../common/leftTab.jsp" />
	<div id="page-wrapper" class="gray-bg">
	<c:import url="/resources/header.do"></c:import>
	<div class="wrapper wrapper-content">
	<form class="form-horizontal" id="frmScheduleReg" name="frmScheduleReg" action="scheduleReg.do" method="post">
    <input type="hidden" id="id" name="id" value="${mapSchedule.id}">
    <input type="hidden" id="type" name="type" value="${type}">
    <input type="hidden" id="BCID" name="BCID" value="${mapSchedule.BCID}">
    <input type="hidden" id="bmscId" name="bmscId" value="${mapSchedule.bmscId}"/>
    <input type="hidden" id="serviceAreaId" name="serviceAreaId" value="${mapSchedule.serviceAreaId}"/>
    <input type="hidden" id="serviceGroupId" name="serviceGroupId" value="${mapSchedule.serviceGroupId}"/>
    <input type="hidden" id="searchDate" name="searchDate" value="${mapSchedule.searchDate}"/>
    <input type="hidden" id="saidDefault" name="saidDefault"  value="${mapSchedule.serviceAreaId}">
    <input type="hidden" id="circleId" name="circleId" value="${sessionScope.USER.circleId}">
    
	<div class="row">
		<!-- eEPG for ESPN time -->
		<div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h3>Schedule for Service
                    	<c:choose>
                    		<c:when test="${mapSchedule.nationalYN == 'Y' and mapSchedule.emergencyYN == 'Y'}">Emergency</c:when>
                    		<c:when test="${mapSchedule.nationalYN == 'Y'}">National</c:when>
                    		<c:otherwise>
                    			<c:if test="${empty mapSchedule.serviceGroupId}">Area ${mapSchedule.serviceAreaId}</c:if>
                    			<c:if test="${not empty mapSchedule.serviceGroupId}">Group ${mapSchedule.serviceGroupId}</c:if>
                    		</c:otherwise>
                    	</c:choose> 
                    </h3>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="form-group">
                            <label class="col-sm-2 text-right"><h4 style="padding-top:5px"><i class="fa fa-circle text-success"></i> Select</h4></label>
                            <div class="col-sm-4">
                                <c:if test="${empty mapSchedule.BCID}">
					                <select class="input-sm form-control input-s-sm" id="serviceType" name="serviceType">
		                    	</c:if>
	                        	<c:if test="${not empty mapSchedule.BCID}">
	                        		<select id="serviceType" name="serviceType" class="input-sm form-control input-s-sm" disabled>            	
	                        	</c:if>
                       	        	   <option value="fileDownload" <c:if test="${mapSchedule.service eq 'fileDownload'}"> selected</c:if>>File Download</option>
                                       <option value="streaming" <c:if test="${mapSchedule.service eq 'streaming'}">selected</c:if>>Streaming</option>
                                       <option value="carouselMultiple" <c:if test="${mapSchedule.service eq 'carouselMultiple'}">selected</c:if>>Carousel Multiple Files</option>
                                       <option value="carouselSingle" <c:if test="${mapSchedule.service eq 'carouselSingle'}">selected</c:if>>Carousel Single File</option>
                                	</select>
                            </div>
                            <div id="interval" style="display: none; margin-right: 35px;">
	                            <label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i>Retrieve Interval</label>
	                       	    <div class="col-sm-3">
	                       	    	<input type="text" class="form-control" id="retrieveInterval" name="retrieveInterval" alt='retrieveInterval' value="${mapSchedule.retrieve_interval}">
	                       	    </div>
                       	    </div>
                       	    <div id="serviceModeArea" style="display: none; margin-right: 35px;">
	                            <label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i>service Mode</label>
	                       	    <div class="col-sm-3">
	                       	    	<select class="input-sm form-control input-s-sm" id="serviceMode" name="serviceMode">
	                       	    		<option value="BC">BC (Broadcast Only)</option>
                                    	<option value="SC">SC (Service Continuity)</option>
                                    	<option value="MooD">MooD</option>
	                       	    	</select>
	                       	    </div>
                       	    </div>
                        </div><!-- end form-group -->
                    </div>
                    <div class="ibox-content">
                        <div class="row">
                            <form method="get" class="form-horizontal">
                            <div class="form-group">
						  		<label class="col-sm-2 control-label">Service Name</label>
                                <div class="col-sm-4"><input type="text" class="form-control" id="name" name="name" alt='service name' value="${mapSchedule.service_name}"></div>
                             </div>
                            <div class="form-group">
						  		<label class="col-sm-2 control-label" style="font-size : 12px;"><i class="fa fa-check text-importance"></i>Service Name Lang</label>
                                <div class="col-sm-4">
									<c:if test="${empty mapSchedule.BCID}">
					                	<select class="input-sm form-control input-s-sm" id="serviceNameLanguage" name="serviceNameLanguage">
		                    		</c:if>
	                        		<c:if test="${not empty mapSchedule.BCID}">
	                        			<input type="hidden" id=serviceNameLanguage" name="serviceNameLanguage" value="${mapSchedule.serviceNameLanguage}">
	                        			<select class="input-sm form-control input-s-sm" disabled>
	                        		</c:if>
                        	    	    <option value="EN"<c:if test="${mapSchedule.language eq 'fileDownload'}"> selected</c:if>>EN</option>
                        	    	    <option value="KR">KR</option>
                        	    	    <option value="FR">FR</option>
                                    </select>
                                </div>
                                <label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i>Service Lang</label>
                                <div class="col-sm-4">
									<c:if test="${empty mapSchedule.BCID}">
					                	<select class="input-sm form-control input-s-sm" id="serviceLanguage" name="serviceLanguage">
		                    		</c:if>
	                        		<c:if test="${not empty mapSchedule.BCID}">
	                        			<input type="hidden" id=serviceLanguage" name="serviceLanguage" value="${mapSchedule.language}">
	                        			<select class="input-sm form-control input-s-sm" disabled>            	
	                        		</c:if>
                        	    	    <option value="EN"<c:if test="${mapSchedule.language eq 'fileDownload'}"> selected</c:if>>EN</option>
                        	    	    <option value="KR">KR</option>
                        	    	    <option value="FR">FR</option>
                                    </select>
                                </div>
                             </div>
                             <div class="form-group">
                             	<label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i>Service Class</label>
                        	    <div class="col-sm-3">
                        	    	<select type="text" class="form-control" id="serviceClass" name="serviceClass" alt='serviceClass'>
                        	    		<c:forEach var="row" items="${serviceClassList}">
                        	    			<option value="${row.class_name}">${row.class_name}</option>
                        	    		</c:forEach>
                        	    	</select>
                        	    </div>
                        	    <div class="col-sm-1">
                        	    	<c:if test="${userGrade != 9999}">
                       	    			<button id="newClass" type="button" class="btn btn-block btn-default btn-sm">New</button>
                        	    	</c:if>
                        	    </div>
                             	<label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i> Service Id</label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control" id="serviceId" name="serviceId" required="required" value="${mapSchedule.serviceId}">
                                </div>
                                <div class="col-sm-1">
                   	    			<button id="newId" type="button" class="btn btn-block btn-default btn-sm" onclick="openServiceIdModal()">Select</button>
                        	    </div>
                             </div>
                                <div class="hr-line-dashed" style="margin-top:-10px; padding-bottom:15px;"></div>
                                <div class="form-group"><label class="col-sm-2 control-label">Transfer Config</label>
                                    <div class="col-sm-10">
                                        <div class="col-md-6">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <h3><i class="fa fa-check text-success">Qos</i></h3>
                                                </div>
                                                <div class="panel-body">
                                                    <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> GBR</label>
                                                        <div class="col-sm-9">
                                                        	<input type="text" class="form-control" id="GBR" name="GBR" required="required" value="${mapSchedule.GBR}">
                                                        </div>
                                                    </div>
                                                    <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> QCI</label>
                                                        <div class="col-sm-9">
                                                        	<input type="text" class="form-control" id="QCI" name="QCI" required="required" value="${mapSchedule.QCI}">
                                                        </div>
                                                    </div>
                                                    <div class="form-group"><label class="col-sm-3 control-label">ARP</label>
                                                        <div class="col-sm-8" style="padding:10px;margin-left:14px;background:#eee">
                                                            <div class="form-group"><label class="col-sm-6 control-label">Level</label>
                                                                <div class="col-sm-6"><input type="text" class="form-control" id="level" name="level" required="required" value="${mapSchedule.level}"></div>
                                                            </div>
                                                            <label class="col-sm-6 control-label">PreEmptionCapabiity</label>
                                                   			<div class="col-sm-6 swich">
                                                                  <div class="onoffswitch" style="margin-left: 74px;">
                                                                      <input type="checkbox" class="onoffswitch-checkbox"  id="preEmptionCapabiity" name="preEmptionCapabiity" <c:if test="${mapSchedule.preEmptionCapabiity == 'on'}">checked</c:if>>
                                                                      <label class="onoffswitch-label" for="preEmptionCapabiity">
                                                                          <span class="onoffswitch-inner"></span>
                                                                          <span class="onoffswitch-switch"></span>
                                                                      </label>
                                                                  </div>
                                                            </div>
															<br>
                                                            <label class="col-sm-6 control-label">PreEmptionVulnerability</label>
                                                            <div class="col-sm-6 swich">
                                                              <div class="onoffswitch" style="margin-left: 74px;">
                                                                     <input type="checkbox" class="onoffswitch-checkbox"  id="preEmptionVulnerability" name="preEmptionVulnerability" <c:if test="${mapSchedule.preEmptionVulnerability == 'on'}">checked</c:if>>
                                                                     <label class="onoffswitch-label" for="preEmptionVulnerability">
                                                                         <span class="onoffswitch-inner"></span>
                                                                         <span class="onoffswitch-switch"></span>
                                                                     </label>
                                                                 </div>
                                                             </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    <div class="col-md-6">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <h3><i class="fa fa-check text-success"> FEC</i></h3>
                                            </div>
                                            <div class="panel-body">
                                                <div class="form-group"><label class="col-sm-6 control-label"><i class="fa fa-check text-importance"></i> Type</label>
                                                    <div class="col-sm-6">
                                                      <select class="input form-control"  id="fecType" name="fecType">
                                                          <option value="NoFEC" <c:if test="${mapSchedule.fecType eq 'NoFEC'}"> selected</c:if>>NoFEC</option>
                                                          <option value="Raptor" <c:if test="${mapSchedule.fecType eq 'Raptor'}"> selected</c:if>>Raptor</option>
                                                          <option value="RaptorQ" <c:if test="${mapSchedule.fecType eq 'RaptorQ'}"> selected</c:if>>RaptorQ</option>
                                                          <option value="RSLDPC" <c:if test="${mapSchedule.fecType eq 'RSLDPC'}"> selected</c:if>>RSLDPC</option>
                                                      </select>
                                                    </div>
                                                </div>
                                                <div class="form-group"><label class="col-sm-6 control-label"><i class="fa fa-check text-importance"></i> Ratio</label>
                                                    <div class="col-sm-6"><input type="text" class="form-control" id="fecRatio" name="fecRatio" value="${mapSchedule.fecRatio}" <c:if test="${mapSchedule.fecType eq 'NoFEC'}"> disabled</c:if>>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="bcType_streaming" <c:if test="${empty mapSchedule.service || mapSchedule.service == 'fileDownload'}">style="display:none"</c:if>>
                                                	<label class="col-sm-6" style="padding-bottom:6px"><i class="fa fa-check text-importance"></i> Segmentation Available Offset</label>
                                                    <div class="col-sm-6"><input type="text" class="form-control" id="segmentAvailableOffset" name="segmentAvailableOffset" value="${mapSchedule.segmentAvailableOffset}"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </div>
                                <div class="hr-line-dashed"></div>
                                
                                <div id="serviceAreaRow">
                                	
                                </div>
                            </form>
                        </div>
                    </div><!-- end ibox-content -->
                    <div id="fileDownloads" class="ibox float-e-margins">
                        <div id="fileDownload" class="ibox-content" style="background:#eee">
                            <div class="ibox-tools">
                                <a class="close-schedule">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                            <div class="row">
	                            <div class="form-group"><label class="col-sm-2 control-label">Schedule</label>
	                                <div class="col-sm-8">
	                                    <label class="col-sm-1 control-label">Start</label>
	                                        <div class="col-sm-5"><input type="text" class="form-control" id="schedule_start" name="schedule_start" value="${mapSchedule.schedule_start}"></div>
	                                    <label class="col-sm-1 control-label">Stop</label>
	                                    <div class="col-sm-5"><input type="text" class="form-control" id="schedule_stop" name="schedule_stop" value="${mapSchedule.schedule_stop}" readonly></div>
	                                </div>
	                                <div class="col-sm-1">
	                                    <div class="form-group">
	                                    	<!-- 스케쥴 버튼 추가버튼 -->
	                                    	<button type="button" name="addSchedule" class="btn btn-xs btn-primary" style="margin:7px 0 0 13px" onclick="addFileSchedule()">
	                                    		<input type="hidden" class="form-control" id="contentLength" name="contentLength" value="1">
	                                    		<i class="fa fa-plus"></i>	
	                                    	</button>
	                                    </div>
	                                </div>
<!-- 	                                <div class="hr-line-dashed"></div> -->
			                        <div name="bcType_streaming2" <c:if test="${empty mapSchedule.service || mapSchedule.service == 'FileDownload'}">style="display:none"</c:if>>
			                            <div class="form-group">
			                            	<label class="col-sm-3 control-label" style="margin-top: 15px;width: 18%;">ContentSet</label>
			                                <div class="col-sm-8" style="margin-top: 15px;">
			                                    <div class="form-group">
			                                    	<div class="row">
					                                	<label class="col-sm-2 control-label">Service Area</label>
					                                    <div class="col-sm-6">
					                                    	<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;background-color: white;">
					                                    </div>
					                                    <c:if test="${type == 'area'}">
					                                    	<div class="row">
					                                    		<div class="col-sm-2">
							                                    	<input type="text" class="form-control" id="said" name="said" required="required" value="">
							                                    </div>
						                                    	<div class="col-sm-2"> 
							                                    	<button type="button" id="saidAdd" name="saidAdd" class="btn btn-block btn-default">Add</button>
							                                    </div>
							                                    <div class="col-sm-4">
							                                        <button type="button" id="mapAdd" name="mapAdd" class="btn btn-block btn-default">Add Service Area with Map</button>
							                                    </div>
													        </div>
					                                    </c:if>
					                                </div>
			                                    </div>
			                                    <div id="fileUpload" class="form-group">
			                                    	<div class="row">
			                                    		<div class="col-sm-8"> 
			                                    			<form id="uploadFileForm" method="post" enctype="multipart/form-data">
					                                    		<input type="file" id="fileId" name="fileId" class="filestyle" data-buttonBefore="true">
					                                    	</form>
					                                    </div>
				                                        <div class="col-sm-4"> 
					                                    	<button type="button" id="uploadFile" name="uploadFile" onclick="" class="btn btn-block btn-default">Upload File</button>
					                                    </div>
			                                    	</div>
			                                    </div>
			                                    <div class="form-group">
			                                    	<div class="row">
			                                    		<input type="hidden" id="contentSetId" name="contentSetId" value="${mapSchedule.contentId}">
			                                    		<label class="col-sm-2 pull-left" style="padding:7px 0 0 35px">mpd</label>
				                                        <div class="col-sm-8">
				                                        <c:if test="${empty mapSchedule.BCID}">
				                                        	<input type="text" class="form-control" id="mpdURI" name="mpdURI" value="${mapContentUrl.url}">
				                                        </c:if>
				                                        <c:if test="${not empty mapSchedule.BCID}">
					                                        <input type="text" class="form-control" id="mpdURI" name="mpdURI" value="${mapSchedule.mpdURI}">
				                                        </c:if>
				                                        </div>
				                                        <div class="col-sm-2">
				                                        	<button type="button" id="searchContentStream" style="margin-top: 4px;margin-left: 5px;width: 50px;" style="margin-top: 5px;" class="btn btn-primary btn-xs">Search</button>
				                                        </div>
			                                    	</div>
			                                    </div>
			                                </div>
			                            </div>
			                            <div id="moodArea" class="form-group">
			                            	<label class="col-sm-3 control-label" style="margin-top: 15px;width: 18%;">MooD</label>
			                                <div class="col-sm-8" style="margin-top: 15px;">
			                                	<div class="form-group">
			                                    	<div class="row">
			                                    		<label class="col-sm-2 pull-left" style="padding:7px 0 0 25px">r12mpdURI</label>
				                                        <div class="col-sm-8">
				                                        	<input type="text" class="form-control" id="r12mpdURI" name="r12mpdURI" value="${mapContentUrl.url}">
				                                        </div>
			                                    	</div>
			                                    	<div name="bcPattern" class="row">
			                                    		<label class="col-sm-2 pull-left" style="padding:7px 0 0 25px">bcBasePattern</label>
				                                        <div class="col-sm-8">
				                                        	<input type="text" class="form-control" id="bcBasePattern" name="bcBasePattern" value="">
				                                        </div>
				                                        <button type="button" id="addBcPattern" title="Create new cluster" class="btn btn-primary btn-sm">
					                                		<i class="fa fa-plus"></i> <span class="bold"></span>
					                                	</button>
			                                    	</div>
			                                    </div>
			                                    <div id="bcServiceArea" class="form-group">
			                                    	<div class="row">
					                                	<label class="col-sm-2 control-label">bcServiceArea</label>
					                                    <div class="col-sm-6">
					                                    	<input type="text" class="form-control" id="bcSaidList" name="bcSaidList" placeholder="" style="height: 75px;background-color: gainsboro;background-color: white;" readonly>
					                                    </div>
<%-- 					                                    <c:if test="${type == 'area'}"> --%>
				                                    	<div class="row">
				                                    		<div class="col-sm-2">
						                                    	<input type="text" class="form-control" id="bcSaid" name="bcSaid" value="">
						                                    </div>
					                                    	<div class="col-sm-2"> 
						                                    	<button type="button" id="bcSaidAdd" name="bcSaidAdd" class="btn btn-block btn-default">Add</button>
						                                    </div>
						                                    <div class="col-sm-4">
						                                        <button type="button" id="bcMapAdd" name="bcMapAdd" class="btn btn-block btn-default" style="display: none;">Add bcServiceArea with Map</button>
						                                    </div>
												        </div>
<%-- 												        </c:if> --%>
					                                </div> 
			                                    </div>
			                                </div>
			                                <div class="row" style="margin-left: -9%;"> 
	                                    		<label class="col-sm-2 control-label col-sm-offset-2">UC Threshold</label>
		                                        <div class="col-sm-2">
		                                        	<input type="text" class="form-control" id="UCThreshold" name="UCThreshold" value="${mapSchedule.UCThreshold}">
		                                        </div>
	                                    		<label class="col-sm-2 control-label">BC Threshold</label>
		                                        <div class="col-sm-2">
		                                        	<input type="text" class="form-control" id="BCThreshold" name="BCThreshold" value="${mapSchedule.BCThreshold}">
		                                        </div>
			                                </div>
			                            </div>
			                        </div>
	                            </div>
                                <div id="bcType_fileDownload" name="bcType_fileDownload" <c:if test="${not empty mapSchedule.service && mapSchedule.service == 'streaming'}"> style="display:none"</c:if>>
	                                <div class="form-group">
	                                <label class="col-sm-2 control-label">Content</label>
	                                <div class="col-sm-9">
	                                	<div name="contentPlus">
		                                	<button type="button" name="addContent" title="Create new cluster" class="btn btn-primary btn-sm" onclick="addFileContent(this)">
		                                		<i class="fa fa-plus"></i> <span class="bold"></span>
		                                	</button>
	                                	</div>
	                                </div>
                                </div>
                                <div class="form-group">
                                    <div name="content" class="col-sm-10 col-sm-offset-2">
                                        <ul class="schedule-list">
                                        	<div class="ibox-tools">
				                                <a class="close-content">
				                                    <i class="fa fa-times"></i>
				                                </a>
				                            </div>
                                            <li>
                                                <div class="well" style="margin-bottom: 0px;">
                                                    <div class="form-group">
                                                        <label class="col-md-2 control-label">File URI</label>
                                                        <div class="col-md-9">
	                                                        <input type="hidden" name="contentId" value="${mapSchedule.contentId}">
	                                                        <input type="hidden" name="duration" value="${mapSchedule.duration}">
	                                                        <c:if test="${empty mapSchedule.BCID}">
	                                                         	<input type="text" class="form-control input-sm m-b-xs" id="fileURI" name="fileURI" value="${mapContentUrl.url}">
	                                                        </c:if>
	                                                        <c:if test="${not empty mapSchedule.BCID}">
	                                                        	<input type="text" class="form-control input-sm m-b-xs" id="fileURI" name="fileURI" value="${mapSchedule.fileURI}">
	                                                        </c:if>
                                                        </div>
                                                       	<button type="button" name="searchContent" style="margin-top: 4px;margin-left: 5px;width: 50px;" style="margin-top: 5px;" class="col-sm-1 btn btn-primary btn-xs">Search</button>
                                                        <div name="contentStartStop">
	                                                        <label class="col-md-2 control-label">Start</label>
	                                                        <div class="col-md-4">
	                                                        <c:if test="${empty mapSchedule.BCID}">
	                                                        	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_start" name="deliveryInfo_start" value="${mapSchedule.start_date}"></div>
	                                                        </c:if>
	                                                        <c:if test="${not empty mapSchedule.BCID}">
	                                                        	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_start" name="deliveryInfo_start" value="${mapSchedule.deliveryInfo_start}"></div>
	                                                        </c:if>
	                                                        <label class="col-md-2 control-label">Stop</label>
	                                                        <div class="col-md-4">
	                                                        <c:if test="${empty mapSchedule.BCID}">
	                                                        	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_end" name="deliveryInfo_end" value="${mapSchedule.end_date}"></div>
	                                                        </c:if>
	                                                        <c:if test="${not empty mapSchedule.BCID}">
	                                                        	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_end" name="deliveryInfo_end" value="${mapSchedule.deliveryInfo_end}"></div>
	                                                        </c:if>
                                                        </div>
                                                        <!-- 
                                                        <div class="col-md-12 text-right"><button type="button" class="btn btn-danger btn-sm">Content Delete</button></div>
                                                         -->
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <!-- 
                                    <div class="col-sm-12 text-right"><button type="button" class="btn btn-danger">Schedule Delete</button></div>
                                    -->
                                 </div>
                            	</div>
                            </div>
                        </div><!-- end ibox-content -->
                    </div>
                        <div class="hr-line-dashed"></div>
                           <div class="form-group"><label class="col-sm-2 control-label">Associated Delivery</label>
                               <div class="col-sm-8">
                                   <div class="well">
                                   <div id="bcType_fileDownload2" <c:if test="${not empty mapSchedule.service && mapSchedule.service == 'streaming'}"> style="display:none"</c:if>>
                                   <div class="form-group">
                                       <label class="col-md-3 control-label">File Repair</label>
                                       <div class="col-md-9">
                                           <div class="swich m-b-n">
                                               <div class="onoffswitch">
                                                   <input type="checkbox" class="onoffswitch-checkbox" id="fileRepair" name="fileRepair" <c:if test="${mapSchedule.fileRepair == 'on'}">checked</c:if>>
                                                   <label class="onoffswitch-label" for="fileRepair">
                                                       <span class="onoffswitch-inner"></span>
                                                       <span class="onoffswitch-switch"></span>
                                                    </label>
                                               </div>
                                           </div>
                                       </div>
                                   </div>
                                   
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Offset Time</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="frOffsetTime" name="frOffsetTime" value="${mapSchedule.frOffsetTime}" <c:if test="${mapSchedule.fileRepair == 'off'}">disabled</c:if>></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Random Time</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="frRandomTime" name="frRandomTime" value="${mapSchedule.frRandomTime}" <c:if test="${mapSchedule.fileRepair == 'off'}">disabled</c:if>></div>
                                   </div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-md-3 control-label">Reception Report</label>
                                       <div class="col-md-9">
                                           <div class="swich m-b-n">
                                               <div class="onoffswitch">
                                                   <input type="checkbox" class="onoffswitch-checkbox" id="receptionReport" name="receptionReport" <c:if test="${mapSchedule.receptionReport == 'on'}">checked</c:if>>
                                                   <label class="onoffswitch-label" for="receptionReport">
                                                       <span class="onoffswitch-inner"></span>
                                                       <span class="onoffswitch-switch"></span>
                                                   </label>
                                               </div>
                                           </div>
                                       </div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Report Type</label>
                                       <div class="col-sm-9">
                                           <select class="input input-sm form-control" id="reportType" name="reportType" onchange="changePercentage();" <c:if test="${mapSchedule.receptionReport == null}">disabled</c:if>>
                                               <option value="RAck">RAck</option>
                                               <option value="StaR-all">StaR-all</option>
                                           </select>
                                       </div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Sample Percentage</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="samplePercentage" name="samplePercentage" value="${mapSchedule.samplePercentage}"></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Offset Time</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="offsetTime" name="offsetTime" value="${mapSchedule.offsetTime}" <c:if test="${mapSchedule.receptionReport == 'off'}">disabled</c:if>></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Random Time</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="randomTime" name="randomTime" value="${mapSchedule.randomTime}" <c:if test="${mapSchedule.receptionReport == 'off'}">disabled</c:if>></div>
                                   </div>
                            	</div>
                            	<div id="consumptionReport" class="well">
                                   <div class="form-group">
                                   		<label class="col-md-3 control-label">Consumption Report</label>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Location</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="moodLocation" name="moodLocation" value="${mapSchedule.moodLocation}"></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-md-3 control-label">Report Client ID</label>
                                       <div class="col-md-9">
                                           <div class="swich m-b-n">
                                               <div class="onoffswitch">
                                                   <input type="checkbox" class="onoffswitch-checkbox" id="reportClientId" name="reportClientId" <c:if test="${mapSchedule.receptionReport == 'on'}">checked</c:if>>
                                                   <label class="onoffswitch-label" for="reportClientId">
                                                       <span class="onoffswitch-inner"></span>
                                                       <span class="onoffswitch-switch"></span>
                                                    </label>
                                               </div>
                                           </div>
                                       </div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Report Interval</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="moodReportInterval" name="moodReportInterval" value="${mapSchedule.moodReportInterval}" <c:if test="${mapSchedule.reportClientId == 'off'}">disabled</c:if>></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Offset Time</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="moodOffsetTime" name="moodOffsetTime" value="${mapSchedule.moodOffsetTime}" <c:if test="${mapSchedule.reportClientId == 'off'}">disabled</c:if>></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Random Time Period</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="moodRandomTimePeriod" name="moodRandomTimePeriod" value="${mapSchedule.moodRandomTimePeriod}" <c:if test="${mapSchedule.reportClientId == 'off'}">disabled</c:if>></div>
                                   </div>
                                   <div class="form-group">
                                       <label class="col-sm-3 control-label">Sample Percentage</label>
                                       <div class="col-sm-9"><input type="text" class="form-control input-sm" id="moodSamplePercentage" name="moodSamplePercentage" value="${mapSchedule.moodSamplePercentage}" <c:if test="${mapSchedule.reportClientId == 'off'}">disabled</c:if>></div>
                                   </div>
                            	</div>
                            </div>
                     		<div class="form-group">
	                        	<div class="col-sm-5"></div>
	                        	<div class="col-sm-">
		                        	<c:if test="${empty mapSchedule.BCID}">
		                        	<button class="col-sm-2 btn btn-success" type="button" id="btnOK" style="width:90px;margin-left:10px;margin-top:10px">OK
		                        	</c:if>
		                        	<c:if test="${not empty mapSchedule.BCID}">
		                        	<button class="col-sm-2 btn btn-success" type="button" id="btnUPDATE" style="width:90px;margin-left:10px;margin-top:10px">UPDATE            	
		                        	</c:if>            	
		                        	</button>
		                        	<button class="col-sm-2 btn btn-success" type="button" id="btnAbort" name="btnAbort" style="width:90px;margin-left:10px;margin-top:10px">Abort</button>
		                        	<button class="col-sm-2 btn btn-success" type="button" id="btnDelete" name="btnDelete" style="width:90px;margin-left:10px;margin-top:10px">Delete</button>
		                        	<button class="col-sm-2 btn btn-success" type="button" id="btnCancel" name="btnCancel" style="width:90px;margin-left:10px;margin-top:10px">Cancel</button>
	                        	</div>
						    </div>
                        </div>
                    </div><!-- end ibox-content -->
                </div><!-- end ibox-content -->
            </div>
		</div>
		<!-- end eEPG for ESPN time -->
		<c:import url="../common/circleTownMgmt.jsp"></c:import>
	</div>	
	</form>
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->
	<jsp:include page="contentList.jsp"></jsp:include>
	<jsp:include page="serviceClassList.jsp"></jsp:include>
	<jsp:include page="serviceIdList.jsp"></jsp:include>
</div><!-- end wrapper -->

</body>
</html>
<script id="scheduleForm" type="text/x-jsrender">
	<div id="fileDownload" class="ibox-content" style="background:#eee">
       <div class="ibox-tools">
           <a class="close-schedule">
               <i class="fa fa-times"></i>
           </a>
       </div>
       <div class="row">
        <div class="form-group"><label class="col-sm-2 control-label">Schedule</label>
            <div class="col-sm-8">
                <label class="col-sm-1 control-label">Start</label>
                    <div class="col-sm-5"><input type="text" class="form-control" id="schedule_start" name="schedule_start" value="${mapSchedule.schedule_start}"></div>
                <label class="col-sm-1 control-label">Stop</label>
                <div class="col-sm-5"><input type="text" class="form-control" id="schedule_stop" name="schedule_stop" value="${mapSchedule.schedule_stop}"></div>
            </div>
            <div class="col-sm-1">
                <div class="form-group">
                	<!-- 스케쥴 버튼 추가버튼 -->
                	<button type="button" name="addSchedule" class="btn btn-xs btn-primary" style="margin:7px 0 0 13px" onclick="addFileSchedule()">
                		<input type="hidden" class="form-control" id="contentLength" name="contentLength" value="1">
                		<i class="fa fa-plus"></i>	
                	</button>
                </div>
            </div>
			<div name="bcType_streaming2" <c:if test="${empty mapSchedule.service || mapSchedule.service == 'FileDownload'}">style="display:none"</c:if>>
	            <div class="form-group"><label class="col-sm-2 control-label" style="margin-left: 10px;">ContentSet</label>
	                <div class="col-sm-8">
	                    <div class="form-group">
	                    	<div class="row">
	                  	<label class="col-sm-2 control-label">Service Area</label>
	                      <div class="col-sm-6">
	                      	<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;background-color: gainsboro;">
	                      </div>
	                      <c:if test="${empty mapSchedule.BCID}">
	                      	<div class="row">
	                      		<div class="col-sm-2">
	                        	<input type="text" class="form-control" id="said" name="said" required="required" value="">
	                        </div>
	                       	<div class="col-sm-2"> 
	                        	<button type="button" id="saidAdd" name="saidAdd" class="btn btn-block btn-default">Add</button>
	                        </div>
	                        <div class="col-sm-4">
	                            <button type="button" id="mapAdd" name="mapAdd" class="btn btn-block btn-default">Add Service Area with Map</button>
	                        </div>
	  						</div>
	                      </c:if>
	                  </div>
	                    </div>
	                    <div class="form-group">
	                    	<label class="pull-left" style="padding:7px 0 0 35px">mpd
	                    	</label>
	                        <div class="col-sm-10">
	                        <c:if test="${empty mapSchedule.BCID}">
	                        	<input type="text" class="form-control" id="mpdURI" name="mpdURI" value="${mapContentUrl.url}">
	                        </c:if>
	                        <c:if test="${not empty mapSchedule.BCID}">
	                         <input type="text" class="form-control" id="mpdURI" name="mpdURI" value="${mapSchedule.mpdURI}">
	                        </c:if>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
        </div>
           <div id="bcType_fileDownload" name="bcType_fileDownload" <c:if test="${not empty mapSchedule.service && mapSchedule.service == 'streaming'}"> style="display:none"</c:if>>
            <div class="form-group">
            <label class="col-sm-2 control-label">Content</label>
            <div class="col-sm-9">
				<div name="contentPlus">
            	<button type="button" name="addContent" title="Create new cluster" class="btn btn-primary btn-sm" onclick="addFileContent(this)">
            		<i class="fa fa-plus"></i> <span class="bold"></span>
            	</button>
				</div>
            </div>
           </div>
           <div class="form-group">
               <div name="content" class="col-sm-10 col-sm-offset-2">
                   <ul class="schedule-list">
                   	<div class="ibox-tools">
               <a class="close-content">
                   <i class="fa fa-times"></i>
               </a>
           </div>
                       <li>
                           <div class="well" style="margin-bottom: 0px;">
                               <div class="form-group">
                                   <label class="col-md-2 control-label">File URI</label>
                                   <div class="col-md-10">
                                    <c:if test="${empty mapSchedule.BCID}">
                                    	<input type="text" class="form-control input-sm m-b-xs" id="fileURI" name="fileURI" value="${mapContentUrl.url}">
                                    </c:if>
                                   	<c:if test="${not empty mapSchedule.BCID}">
                                   		<input type="text" class="form-control input-sm m-b-xs" id="fileURI" name="fileURI" value="${mapSchedule.fileURI}">
                                   	</c:if>
                                   </div>
								   <div name="contentStartStop">	
                                   <label class="col-md-2 control-label">Start</label>
                                   <div class="col-md-4">
                                   <c:if test="${empty mapSchedule.BCID}">
                                   	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_start" name="deliveryInfo_start" value="${mapSchedule.start_date}"></div>
                                   </c:if>
                                   <c:if test="${not empty mapSchedule.BCID}">
                                   	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_start" name="deliveryInfo_start" value="${mapSchedule.deliveryInfo_start}"></div>
                                   </c:if>
                                   	
                                   <label class="col-md-2 control-label">Stop</label>
                                   <div class="col-md-4">
                                   <c:if test="${empty mapSchedule.BCID}">
                                   	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_end" name="deliveryInfo_end" value="${mapSchedule.end_date}"></div>
                                   </c:if>
                                   <c:if test="${not empty mapSchedule.BCID}">
                                   	<input type="text" class="form-control input-sm m-b-sm" id="deliveryInfo_end" name="deliveryInfo_end" value="${mapSchedule.deliveryInfo_end}"></div>
                                   </c:if>
									</div>
                                   <!-- 
                                   <div class="col-md-12 text-right"><button type="button" class="btn btn-danger btn-sm">Content Delete</button></div>
                                    -->
		
                               </div>
                           </div>
                       </li>
                   </ul>
               </div>
               <!-- 
               <div class="col-sm-12 text-right"><button type="button" class="btn btn-danger">Schedule Delete</button></div>
               -->
            </div>
       	</div>
</script>

<script id="serviceArea" type="text/x-jsrender">
	<div class="row" id="addServiceArea">
		<label class="col-sm-2 control-label">Service Area</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;">
		</div>
		<c:if test="${empty mapSchedule.BCID and type == 'area'}">
			<div class="row">
				<div class="col-sm-2">
					<input type="text" class="form-control" id="said" name="said" required="required" value="">
				</div>
				<div class="col-sm-2">
					<button type="button" id="saidAdd" name="saidAdd" class="btn btn-block btn-default">Add</button>
				</div>
				<div class="col-sm-4">
					<button type="button" id="mapAdd" name="mapAdd" class="btn btn-block btn-default">Add Service Area with Map</button>
				</div>
			</div>
		</c:if>
	</div>
	<div id="fileUpload_F" class="form-group row">
		<div class="row">
			<div class="col-sm-6 col-sm-offset-2" style="padding-right: 20px;"> 
				<form id="uploadFileForm" method="post" enctype="multipart/form-data" style="padding-left: 10px;">
	  				<input type="file" id="fileId" name="fileId" class="filestyle" data-buttonBefore="true">
	  			</form>
	 		</div>
	    	<div class="col-sm-4"> 
	  			<button type="button" id="uploadFile" name="uploadFile" onclick="" class="btn btn-block btn-default" style="margin-left: -5px;">Upload File</button>
	  		</div>
		</div>
	</div>
</script>

<script id="bcPatternArea" type="text/x-jsrender">
	<div name="bcPattern" class="row">
   		<label class="col-sm-2 pull-left" style="padding:7px 0 0 25px"></label>
        <div class="col-sm-8">
        	<input type="text" class="form-control" name="bcBasePattern" value="">
        </div>
        <i class="fa fa-times" name="removePattern" onclick="removePattern(this)" style="cursor: pointer;margin-top: 10px;" ></i><span class="bold"></span>
   	</div>
</script>


