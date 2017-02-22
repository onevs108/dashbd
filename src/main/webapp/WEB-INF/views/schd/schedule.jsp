<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <link href="../resourcesRenew/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="../resourcesRenew/css/plugins/iCheck/custom.css" rel="stylesheet">

    <!-- Mainly scripts -->
	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
	<script src="../resourcesRenew/js/jquery.form.js"></script>
	<script src="../resourcesRenew/js/bootstrap.min.js"></script>
	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="../resourcesRenew/js/jsrender.min.js"></script>
	
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
	
	<!-- Page-Level Scripts -->
	<script>
		var serviceType = "${mapSchedule.service}";
		$(document).ready(function() {
			getMenuList('SCHEDULE_MGMT');
			if(serviceType == "streaming") 
			{
				
			}
			else
			{
				$("#serviceAreaRow").append($("#serviceArea").render());
				$("div[name='bcType_streaming2']").hide();
				addServiceAreaEvent(0);
			}
			$("#FileRepair").change();
			$("#receptionReport").change();
			
			$('.circle-map .circle-item').on({
				'mouseenter' : function(e){
					$(this).addClass('hover');
					$('.circle-map > img').addClass('hover');
					$($(".circle-map").find("img")[0]).addClass("hover");
				},
				'mouseleave' : function(e){
					$(this).removeClass('hover');
					$('.circle-map > img').removeClass('hover');
					$($(".circle-map").find("img")[0]).removeClass("hover");
				},
				'mousedown' : function(e) {
					var circle_id = $(this).attr("data-init");
					var circle_name = $(this).find("span small").text().replace(" Telecom Circle", "");
					var latitude = $(this).attr("data-lat");
					var longitude = $(this).attr("data-lng");
					//좌클릭
					if(e.button == 0)
					{
						moveCityList(circle_id, circle_name, latitude, longitude);
					}
					//우클릭
					else if (e.button == 2) 
					{
						addSaidFromMap(circle_id);
					}
				},
				'contextmenu' : function(e) {
					return false;
				}
			});
			
		});
		
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
			var ctIdx = $("button[name='addContent']").index(e);	//스케쥴 갯수(index)
			var content = $($("div[name='content']")[ctIdx]).children().last();
			$(content).after(content.clone());
			$($("#contentLength")[ctIdx]).val($($("div[name='content']")[ctIdx]).children().length);
			addContentRemoveEvent();
		}
		
 	</script>

	<style>
	ul
	{
	    list-style-type: none;
	}
	</style> 
	
</head>
<body>
<div id="wrapper">
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
            	<li class="nav-header">
				<div class="dropdown profile-element" align="center">
					<a href="/dashbd/resources/main.do"><img src="/dashbd/resources/img/logo_small.png"></a>
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
					<img src="../resources/img/samsung_small.png">
				</li>
            </ul>
        </nav>
	</div><!-- end border-bottom -->

	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-12">
			<h2><strong>Schedule Mgmt</strong></h2>
			<ol class="breadcrumb">
			    <li>
				   <a href="index.html">Home</a>
			    </li>
			    <li class="active">
				   <strong>Schedule Management</strong>
			    </li>
			</ol>
		</div>
	</div><!-- end row wrapper border-bottom white-bg page-heading -->
            
	<div class="wrapper wrapper-content">
	<form class="form-horizontal" id="frmScheduleReg" name="frmScheduleReg" action="scheduleReg.do" method="post">
    <input type="hidden" id="id" name="id" value="${mapSchedule.id}">
<%--     <input type="hidden" id="contentId" name="contentId" value="${mapSchedule.contentId}"> --%>
    <input type="hidden" id="BCID" name="BCID" value="${mapSchedule.BCID}">
    <input type="hidden" id="bmscId" name="bmscId" value="${mapSchedule.bmscId}"/>
    <input type="hidden" id="serviceAreaId" name="serviceAreaId" value="${mapSchedule.serviceAreaId}"/>
    <input type="hidden" id="searchDate" name="searchDate" value="${mapSchedule.searchDate}"/>
    
	<div class="row">
		<!-- eEPG for ESPN time -->
		<div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h3>Schedule for Service Area ${mapSchedule.serviceAreaId}</h3>
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
	                        		<input type="hidden" id=serviceType" name="serviceType" value="${mapSchedule.service}">
	                        		<select class="input-sm form-control input-s-sm" disabled>            	
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
	                       	    	<input type="text" class="form-control" id="retrieveInterval" name="retrieveInterval" alt='retrieveInterval'>
	                       	    </div>
                       	    </div>
                        </div><!-- end form-group -->
                    </div>
                    <div class="ibox-content">
                        <div class="row">
                            <form method="get" class="form-horizontal">
                            <div class="form-group">
						  		<label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i> Service Name</label>
                                    <div class="col-sm-4"><input type="text" class="form-control" id="name" name="name" alt='service name' value="${mapSchedule.service_name}"></div>
                                <label class="col-sm-2 control-label">Language</label>
                                    <div class="col-sm-4">
										<c:if test="${empty mapSchedule.BCID}">
						                	<select class="input-sm form-control input-s-sm" id="serviceLanguage" name="serviceLanguage">
			                    		</c:if>
		                        		<c:if test="${not empty mapSchedule.BCID}">
		                        			<input type="hidden" id=serviceLanguage" name="serviceLanguage" value="${mapSchedule.language}">
		                        			<select class="input-sm form-control input-s-sm" disabled>            	
		                        		</c:if>
		                        	    	    <option value="en" <c:if test="${mapSchedule.language eq 'fileDownload'}"> selected</c:if>>en</option>
	                                    </select>
                                    </div>
                             </div>
                             
                             <div class="form-group">
                                 <label class="col-sm-2 control-label">Service class</label>
                             	    <div class="col-sm-4"><input type="text" class="form-control" id="serviceClass" name="serviceClass" alt='serviceClass' value="${mapSchedule.serviceClass}"></div>
                             	 <label class="col-sm-2 control-label"><i class="fa fa-check text-importance"></i> Service id</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" id="serviceId" name="serviceId" required="required" value="${mapSchedule.serviceId}">
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
                                                        <div class="col-sm-9"><input type="text" class="form-control" id="GBR" name="GBR" required="required" value="${mapSchedule.GBR}"></div>
                                                    </div>
                                                    <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> QCI</label>
                                                        <div class="col-sm-9"><input type="text" class="form-control" id="QCI" name="QCI" required="required" value="${mapSchedule.QCI}"></div>
                                                    </div>
                                                    <div class="form-group"><label class="col-sm-3 control-label">ARP</label>
                                                        <div class="col-sm-8" style="padding:10px;margin-left:14px;background:#eee">
                                                            <div class="form-group"><label class="col-sm-6 control-label"><i class="fa fa-check text-importance"></i> Level</label>
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
	                                    <div class="col-sm-5"><input type="text" class="form-control" id="schedule_stop" name="schedule_stop" value="${mapSchedule.schedule_stop}"></div>
	                                </div>
	                                <div class="col-sm-1">
	                                    <div class="form-group">
	                                    	<!-- 스케쥴 버튼 추가버튼 -->
	                                    	<button type="button" class="btn btn-xs btn-primary" style="margin:7px 0 0 13px" onclick="addFileSchedule()">
	                                    		<input type="hidden" class="form-control" id="contentLength" name="contentLength" value="1">
	                                    		<i class="fa fa-plus"></i>	
	                                    	</button>
	                                    </div>
	                                </div>
<!-- 	                                <div class="hr-line-dashed"></div> -->
			                        <div name="bcType_streaming2" <c:if test="${empty mapSchedule.service || mapSchedule.service == 'FileDownload'}">style="display:none"</c:if>>
			                            <div class="form-group"><label class="col-sm-2 control-label" style="margin-left: 10px;">ContentSet</label>
			                                <div class="col-sm-8">
			                                    <div class="form-group">
			                                    	<div class="row">
					                                	<label class="col-sm-2 control-label">Service Area</label>
					                                    <div class="col-sm-6">
					                                    	<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;background-color: gainsboro;" readonly>
					                                    </div>
					                                    <c:if test="${empty mapSchedule.BCID}">
					                                    	<div class="row">
					                                    		<div class="col-sm-2">
						                                    		<input type="hidden" class="form-control" id="saidDefault" name="saidDefault"  value="${mapSchedule.serviceAreaId}">
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
                                    	<input type="hidden" name="contentId" value="${mapSchedule.contentId}">
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
                                                                    <input type="checkbox" class="onoffswitch-checkbox" id="FileRepair" name="FileRepair" <c:if test="${mapSchedule.FileRepair == 'on'}">checked</c:if>>
                                                                    <label class="onoffswitch-label" for="FileRepair">
                                                                        <span class="onoffswitch-inner"></span>
                                                                        <span class="onoffswitch-switch"></span>
                                                                    </label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Offset Time</label>
                                                        <div class="col-sm-9"><input type="text" class="form-control input-sm" id="frOffsetTime" name="frOffsetTime" value="${mapSchedule.frOffsetTime}" <c:if test="${mapSchedule.FileRepair == 'off'}">disabled</c:if>></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Random Time</label>
                                                        <div class="col-sm-9"><input type="text" class="form-control input-sm" id="frRandomTime" name="frRandomTime" value="${mapSchedule.frRandomTime}" <c:if test="${mapSchedule.FileRepair == 'off'}">disabled</c:if>></div>
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
                                                            <select class="input input-sm form-control" id="reportType" name="reportType" onchange="changePercentage();" <c:if test="${mapSchedule.receptionReport == 'off'}">disabled</c:if>>
                                                                <option value="RAck">RAck</option>
                                                                <option value="StaR-all">StaR-all</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Sample Percentage</label>
                                                        <div class="col-sm-9"><input type="text" class="form-control input-sm" id="samplePercentage" name="samplePercentage" value="${mapSchedule.samplePercentage}" <c:if test="${mapSchedule.receptionReport == 'off'}">disabled</c:if>></div>
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
                                </div>
                            </div>
                        <div class="form-group">
				                        	<div class="col-sm-5"></div>
				                        	<div class="col-sm-">
					                        	<c:if test="${empty mapSchedule.BCID}">
					                        	<button class="col-sm-2 btn btn-success" type="button" id="btnOK" style="margin-left:10px;margin-top:10px">OK
					                        	</c:if>
					                        	<c:if test="${not empty mapSchedule.BCID}">
					                        	<button class="col-sm-2 btn btn-success" type="submit" id="btnUPDATE" style="margin-left:10px;margin-top:10px">UPDATE            	
					                        	</c:if>            	
					                        	</button>
					                        	
					                        	<button class="col-sm-2 btn btn-success" type="button" id="btnDelete" name="btnDelete" style="margin-left:10px;margin-top:10px">Delete</button>
					                        	
					                        	<button class="col-sm-2 btn btn-success" type="button" id="btnCancel" name="btnCancel" style="margin-left:10px;margin-top:10px">Cancel</button>
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
                	<button type="button" class="btn btn-xs btn-primary" style="margin:7px 0 0 13px" onclick="addFileSchedule()">
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
	                      	<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;background-color: gainsboro;" readonly>
	                      </div>
	                      <c:if test="${empty mapSchedule.BCID}">
	                      	<div class="row">
	                      		<div class="col-sm-2">
	                       		<input type="hidden" class="form-control" id="saidDefault" name="saidDefault"  value="${mapSchedule.serviceAreaId}">
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
			<input type="text" class="form-control" id="saidList" name="saidList" style="height: 75px;" readonly>
		</div>
		<c:if test="${empty mapSchedule.BCID}">
			<div class="row">
				<div class="col-sm-2">
					<input type="hidden" class="form-control" id="saidDefault" name="saidDefault" value="${mapSchedule.serviceAreaId}"> 
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
</script>


