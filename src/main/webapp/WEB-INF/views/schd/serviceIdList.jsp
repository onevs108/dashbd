<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="modal fade" id="serviceIdList">
	<div class="modal-dialog" role="document" style="margin: 10% 0% 0% 30%">
	<div class="modal-content" style="width: 600px;">
	<div id="wrapper" class="sub01">
		<div class="wrapper wrapper-content">
			<div class="circleTown">
				<div class="row">
	                <div class="col-md-12">
	                	<div class="col-md-12">
		                	<div class="ibox">
		                		<div class="row">
			                        <div class="ibox-title">
			                            <h4>Service ID Template<i class="fa fa-times" style="cursor:pointer;float: right;" onclick="closeIdModal()"></i></h4>
			                        </div>
		                        </div>
<!-- 		                        <hr style="margin-top: 0px;"> -->
<!-- 		                        <label>Select Service Id</label> -->
<!-- 		                        <div class="row"> -->
<%-- 									<input type="hidden" id="idIdx" value="${serviceIdIdx}"> --%>
<!-- 	                                <div class="col-md-8">col-md-offset-2 -->
<!--                              	    	<select type="text" class="form-control" id="selectServiceId" onchange="setSelectServiceId()"> -->
<%--                              	    		<c:forEach var="row" items="${serviceIdList}"> --%>
<%--                              	    			<option value="${row.id_name}">${row.id_name}</option> --%>
<%--                              	    		</c:forEach> --%>
<!--                              	    	</select> -->
<!--                              	    </div> -->
<!-- 	                                <div class="col-md-4"> -->
<!-- 	                                	<button type="button" id="btnOK_M" class="btn btn-block btn-primary btn-sm" onclick="setServiceId()">OK</button> -->
<!-- 	                                </div> -->
<!-- 				                </div> -->
				                <hr style="margin-top: 10px;">
<!-- 				                <label>Select Service Id</label> -->
		                        <div id="idFirst" class="row">
	                                <div class="col-md-8">
	                                	<input type="text" id="idName" class="form-control input-sm" placeholder="Service Id" value="">
	                                </div>
	                                <div class="col-md-4">
	                                	<button type="button" id="addId" class="btn btn-block btn-primary btn-sm" onclick="addId()">Add</button>
	                                </div>
				                </div>
		                        <div class="row">
		                        	<div class="col-md-12">
	                                	<h4 style="margin: 25px 5px -1px 5px;">List of Registered Template</h4>
	                                </div>
				                </div>
				                <hr style="margin-top: 10px;">
		                        <div class="">
									<div class="row">
										<div class="col-lg-12">
											<div class="table-responsive">
												<div class="india">
													<table class="footable table table-stripped toggle-arrow-tiny" style="margin:0;" id="idTable">
                                           				
                                   					</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div></div></div>
<script>

	$(document).ready(function(){
		getServiceIdList();
	});
	
	function setSelectServiceId() {
		var idx = 1;
		$.ajax({
			type : "POST",
			url : "getServiceIdIdx.do",
			data : {serviceId: $("#selectServiceId").val()},
			dataType : "json",
			success : function( data ) {
				idx = data.idx;
				$("#serviceId_M").val($("#selectServiceId").val()+":"+idx);
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	}
	
	function setServiceId() {
		$("#serviceId").val($("#selectServiceId").val());
		$("#serviceId").focus();
		$("#serviceIdList").modal('hide');
	}
	
</script>