<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<title>SeSM Database Management</title>	
	<jsp:include page="../common/head.jsp" />
	
	<style type="text/css">
		td {
			font-size: 13px;
		}
		.th-inner {
			font-size: 13px;
		}
	</style>

</head>
<body>
<div id="wrapper">
	<jsp:include page="../common/leftTab.jsp" />
    <div id="page-wrapper" class="gray-bg">
    	<c:import url="/resources/header.do"></c:import>
		<!-- content body -->
        <div class="wrapper wrapper-content">
			
            <!-- Operator Mgmt -->
            <div class="row"> 
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                	<div class="ibox-title">
							<h5>Database Backup</h5>
						</div>
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
	                    		<label class="col-lg-3" style="">Database Auto backup</label>
	                    		<div class="col-lg-4 swich">
	                                <div class="onoffswitch">
	                                    <input type="checkbox" class="onoffswitch-checkbox" id="autoBackup" name="autoBackup" onchange="autoBackupChange()">
	                                    <label class="onoffswitch-label" for="autoBackup">
	                                        <span class="onoffswitch-inner"></span>
	                                        <span class="onoffswitch-switch"></span>
	                                    </label>
	                                </div>
                                </div>
	                    	</div>
	                    	<div class="row" style="padding-top:20px">
	                    		<label class="col-lg-3" style="">Database Auto backup Schedule</label>
	                    		<span id="backupSchedule" style="display: none;">
		                    		<label class="col-lg-2" style="width: 12.5%">Every day at :</label>
		                    		<div class="col-lg-2" style="margin-top: -7px;">
		                                <input type="text" class="form-control" id="backupTime" name="backupTime" value="${backupTime}" placeholder="hh:mi" data-ax5formatter-custom="01">
	                                </div>
	                                <div class="col-lg-2" style="margin-top: -5px;">
		                                <button type="button" class="btn btn-primary btn-sm" onclick="setBackupTime();">
											Set Time
		                                </button>
	                                </div>
                                </span>
	                    	</div>
	                    	<div class="row" style="padding-top:20px">
								<div class="col-lg-4">
	                                <h3>Backup List</h3>
	                            </div>
	                            <div class="col-lg-4 col-md-offset-4">
	                                <button type="button" class="btn btn-danger pull-right" onclick="deleteSelectedFile();">
										Delete Selected File
	                                </button>
	                                <button type="button" class="btn btn-primary pull-left" onclick="openBackupModal();">
										Backup Now
	                                </button>
	                            </div>
	                        </div>
	                    	<div class="hr-line-dashed"></div>
							<div class="table-responsive">
                            	<table class="table table-bordered" id="table"></table>
                            </div>
	                    </div><!-- end ibox-content -->
	                </div>
	            </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

<script src="js/jquery.cookie.js"></script>
<script src="/dashbd/resources/app-js/apps/svc_systemDb.js"></script>
<!-- <script src="js/common.js"></script> -->

<!-- ax5ui -->
<link href="../resourcesRenew/css/plugins/ax5ui/ax5formatter.css" rel="stylesheet">
<script src="../resourcesRenew/js/plugins/ax5ui/ax5core.js"></script>
<script src="../resourcesRenew/js/plugins/ax5ui/ax5formatter.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('SYSTEM_DB_MGMT');
		getDatabaseList();
		if("${autoYN}" == "Y"){
			$("#autoBackup").prop("checked", true);
			$("#autoBackup").change();	
		}
		$('[data-ax5formatter-custom="01"]').ax5formatter({
	        pattern: "custom",
	        getEnterableKeyCodes: function(obj){
	        	return jQuery.extend(
                    ax5.ui.formatter.formatter.ctrlKeys,
                    ax5.ui.formatter.formatter.numKeys
                );
	        },
	        getPatternValue: function (obj) {
	        	if(obj.value.length > 2){
	        		obj.value = obj.value.replace(obj.value.substr(2,1), ":");	//3번째 자리 replace
	        		obj.value = obj.value.substr(0,5);							//5자리 String 으로 만들기
	        	}
	        	if(obj.value.length < 3 && obj.value.indexOf(":") > -1){
	        		obj.value = "";
	        	}
        		return obj.value;
	        }
	    });
	});
</script>

</body>
</html>
