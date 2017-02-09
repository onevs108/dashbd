<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<jsp:include page="../common/head.jsp" />
</head>
<body>

<div id="wrapper">
	<!-- sidebar -->
	<jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
    	<jsp:include page="../common/header.jsp" />	
    	
		<!-- content body -->
        <div class="wrapper wrapper-content">
			
            <!-- Operator Mgmt -->
            <div class="row">
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
	                            <div class="col-md-12 pull-right">
	                            	<div class="col-sm-3"><h3><strong>National Wise Group</strong></h3></div>
	                                <button type="button" class="btn btn-primary pull-right" id="modal-open-btn">Add</button>
	                                <div class="modal inmodal" id="form-modal" tabindex="-1" role="dialog" aria-hidden="true">
	                                    <div class="modal-dialog">
	                                        <div class="modal-content animated fadeIn">
	                                            <div class="modal-header">
	                                                <button type="button" class="close" id="modal-cancel-icon-btn"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	                                                <i class="fa fa-folder-open-o modal-icon"></i>
	                                                <h4 class="modal-title" id="modal-title">Create New Group</h4>
	                                            </div>
	                                            <div class="modal-body">
	                                                <form method="get" class="form-horizontal">
					                                    <div class="form-group">
					                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Group Name</label>
					                                    	<div class="input-group" id="form-operator-name-input-area">
					                                    		<input type="text" id="form-operator-name" class="form-control">
														    	<span class="input-group-btn"><button class="btn btn-default" id="check-name-btn" type="button">Check</button></span>
					                                    	</div>
													    </div> 
	                                                    <div class="form-group">
	                                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Description</label>
	                                                        <div class="col-sm-8"><input type="text" class="form-control" id="form-operator-description" style="height:200px"></div>
	                                                    </div>
	                                                    <div class="form-group">
	                                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Select Menus to grant</label>
	                                                        <div class="col-sm-8">
	                                                        	<c:forEach var="row" items="${permissionList}" varStatus="status">
	                                                        	<div class="checkbox checkbox-primary">
																	<input type="checkbox" name="permission" value="${row.id}">
																	<label for="checkbox${status.count}"> ${row.name}</label>
																</div>
																</c:forEach>
	                                                        </div>
	                                                    </div>
	                                                </form>
	                                            </div>
	                                            <div class="modal-footer">
	                                                <button type="button" class="btn btn-white" id="modal-cancel-btn">Cancel</button>
	                                                <button type="button" class="btn btn-primary" id="modal-add-btn">OK</button>
	                                            </div>
	                                        </div>
	                                    </div>
	                                </div>
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
            
            <div class="row">
				<div class="col-lg-12">
	                <div class="ibox float-e-margins">
	                    <div class="ibox-content">
	                    	<div class="row" style="padding-top:20px">
	                            <div class="col-md-12 pull-right">
	                            	<div class="col-sm-2"><h3><strong>Regional Group</strong></h3></div>
	                            	<div class="col-sm-3">
	                                    <select class="input-sm form-control input-s-sm" id="circleSelect" style="padding: 0px 0px 0px 0px;">
	                                    	<option value="none">Select</option>
	                                		<c:forEach var="row" items="${circleList}">
	                                   			<option value="${row.circle_name}">${row.circle_name}</option>
	                                   		</c:forEach>
	                                    </select>
	                                </div>
	                                <button type="button" class="btn btn-primary pull-right" id="modal-open-btn2">Add</button>
	                                <div class="modal inmodal" id="form-modal2" tabindex="-1" role="dialog" aria-hidden="true">
	                                    <div class="modal-dialog">
	                                        <div class="modal-content animated fadeIn">
	                                            <div class="modal-header">
	                                                <button type="button" class="close" id="modal-cancel-icon-btn2"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	                                                <i class="fa fa-folder-open-o modal-icon"></i>
	                                                <h4 class="modal-title" id="modal-title2">Create New Group</h4>
	                                            </div>
	                                            <div class="modal-body">
	                                                <form method="get" class="form-horizontal">
					                                    <div class="form-group">
					                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Group Name</label>
					                                    	<div class="input-group" id="form-operator-name-input-area2">
					                                    		<input type="text" id="form-operator-name2" class="form-control">
					                                    		<input type="hidden" id="form-circle-name2" class="form-control">
														    	<span class="input-group-btn"><button class="btn btn-default" id="check-name-btn2" type="button">Check</button></span>
					                                    	</div>
													    </div>
	                                                    <div class="form-group">
	                                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Description</label>
	                                                        <div class="col-sm-8"><input type="text" class="form-control" id="form-operator-description2" style="height:200px"></div>
	                                                    </div>
	                                                    <div class="form-group">
	                                                    	<label class="col-sm-4 control-label"><i class="fa fa-check text-importance"></i> Select Menus to grant</label>
	                                                        <div class="col-sm-8">
	                                                        	<c:forEach var="row" items="${permissionList}" varStatus="status">
	                                                        	<div class="checkbox checkbox-primary">
																	<input type="checkbox" name="permission2" value="${row.id}">
																	<label for="checkbox${status.count}"> ${row.name}</label>
																</div>
																</c:forEach>
	                                                        </div>
	                                                    </div>
	                                                </form>
	                                            </div>
	                                            <div class="modal-footer">
	                                                <button type="button" class="btn btn-white" id="modal-cancel-btn2">Cancel</button>
	                                                <button type="button" class="btn btn-primary" id="modal-add-btn2">OK</button>
	                                            </div>
	                                        </div>
	                                    </div>
	                                </div>
	                            </div>
	                        </div>
	                    	<div class="hr-line-dashed"></div>
	                    
							<div class="table-responsive">
                            	<table class="table table-bordered" id="table2"></table>
                            </div>
	                    </div><!-- end ibox-content -->
	                </div>
	            </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

<script src="js/jquery.cookie.js"></script>
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="js/modules/operator.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('OPERATOR_MGMT');
		getOperatorList();
		$("#circleSelect").on("change", function(e){
			getOperatorList2(this.value);
		});
		
		$("input[name='permission']").click(checkSuperAdmin);
		$("input[name='permission2']").click(checkSuperAdmin);
		
	});
	
	function checkSuperAdmin() {
		var selector = "permission"
		if(this.name == "permission2") {
			selector += "2";
		}
		if(this.value == "13") {
			if($(this).is(":checked")) {
				$("input[name='"+selector+"']").attr("disabled", "disabled");
				$("input[name='"+selector+"']").prop("checked", false);
				$(this).prop("checked", true);
	 			this.removeAttribute("disabled");
			}else{
				$("input[name='"+selector+"']").removeAttr("disabled", "disabled");	
			}
		}
	}
	
</script>

</body>
</html>
