<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="deleteAbortModal">
	<input type="hidden" id="url">
	<div class="modal-dialog" role="document">
	<div class="modal-content" style="width: 710px;margin-top: 30%;">
	<div id="wrapper" class="sub01">
		<div class="wrapper wrapper-content">
			<div class="circleTown">
				<div class="row">
	                <div class="col-md-12">
	                	<div class="col-md-12">
		                	<div class="ibox">
		                		<div class="row">
			                        <div class="ibox-title">
			                            <h5>Choice Option</h5>
			                        </div>
			                        <div class="ibox" align="center">
										<button type="button" class="btn btn-primary" id="btnAbort">Abort</button>
										<button type="button" class="btn btn-primary" id="btnDelete">Delete</button>
										<button type="button" class="btn btn-primary" id="btnCancel">Cancel</button>
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
		$("#btnAbort").click(function(){
			deleteAction($("#url").val(), "Abort");
		});
		$("#btnDelete").click(function(){
			deleteAction($("#url").val(), "Delete");
		});
		$("#btnCancel").click(function(){
			$("#deleteAbortModal").modal('hide');
		});
	});
	
</script>