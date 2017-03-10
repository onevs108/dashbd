<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="modal fade" id="serviceClassList">
	<div class="modal-dialog" role="document" style="margin-left: 10%">
	<div class="modal-content" style="width: 1010px;">
	<div id="wrapper" class="sub01">

		<div class="wrapper wrapper-content">
			<!--
			 You need to include this script on any page that has a Google Map.
			 When using Google Maps on your own site you MUST signup for your own API key at:
			 https://developers.google.com/maps/documentation/javascript/tutorial#api_key
			 After your sign up replace the key in the URL below..
			-->
<!-- 			<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70"></script> -->
			<div class="circleTown">
				<div class="row">
	                <div class="col-md-12">
	                	<div class="col-md-12">
		                	<div class="ibox">
		                		<div class="row">
			                        <div class="ibox-title">
			                            <h5>serviceClass List</h5>
			                            <i class="fa fa-times" style="cursor:pointer;float: right;" onclick="closeClassModal()"></i>
			                        </div>
		                        </div>
		                        <hr style="margin-top: 0px;">
		                        <div id="classFirst" class="row">
	                                <div class="col-md-4">
	                                	<input type="text" id="className" class="form-control input-sm" placeholder="Service Class" value="">
	                                </div>
	                                <div class="col-md-4">
	                                	<input type="text" id="description" class="form-control input-sm" placeholder="description" value="">
	                                </div>
	                                <div class="col-md-2">
	                                	<button type="button" id="addClass" class="btn btn-block btn-primary btn-sm" onclick="addClass()">Add</button>
	                                </div>
				                </div>
				                <hr style="margin-top: 10px;">
		                        <div class="">
									<div class="row">
										<div class="col-lg-12">
											<div class="table-responsive">
												<div class="india">
													<input type="hidden" id="idx">
													<input type="hidden" id="type">
													<table class="footable table table-stripped toggle-arrow-tiny" style="margin:0;" id="classTable">
                                           				
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
		getServiceClassList();
		$("#form-title").keyup(function(e){
			if(e.keyCode == 13){
				getContentList();
			}
		});
		$("#form-category").keyup(function(e){
			if(e.keyCode == 13){
				getContentList();
			}
		});
		$("#go-search").on({
			"click" : function(){
				getContentList();
			}
		});
	});
	
</script>