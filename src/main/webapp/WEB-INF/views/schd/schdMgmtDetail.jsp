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
            
            <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Schedule Mgmt </h5>
<!--                             <div id="calendarTrash" style="float: right; padding-top: 5px; padding-right: 5px; padding-left: 5px;"><span class="ui-icon ui-icon-trash"><img src="../resourcesRenew/img/trash.png"/></span></div> -->
                            <div class="ibox-tools">
                                <a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                <a class="close-link"><i class="fa fa-times"></i></a>
                            </div>
                        </div>
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
