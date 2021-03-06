<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- s : POPUP - Add Operator -->
<div class="modal inmodal" id="circleModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="choiceArea()">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">Choice Area</h4>
			</div>
			<form role="form" id="form" class="form-horizontal" action="javascript:void(0);">
				<input type="hidden" id="circleId" name="circleId" value="${sessionScope.USER.circleId}">
				<div class="modal-body">
					<fieldset>
						<div class="row">
							<div class="col-lg-12">
								<div class="col-xs-4">
									<div class="form-group">
										<select id="searchType" name="searchType" class="form-control">
		                                    <option value="">Select</option>
		                                    <option value="circle">Area</option>
		                                    <option value="city">City</option>
		                                    <option value="circleCity">Area &amp; City</option>
		                                    <option value="hotspot">Hotspot</option>
		                                    <option value="said">SAID</option>
		                                </select>
									</div>
								</div>
								<div class="col-xs-8">
									<div class="form-group">
										<div class="input-group">
											<input type="text" id="search-input" name="search-input" class="form-control">
											<span class="input-group-btn">
												<button type="button" onclick="searchTreeNode()" class="btn btn-primary">Search</button>
												<button type="button" onclick="deselectArea()" class="btn btn-white" style="margin-left: 5px;">De-Select</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12" style="overflow: auto; height: 500px;">
								<div id="treeNode"></div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" id="addBtn" onclick="choiceArea()" class="btn btn-primary">Choice</button>
					<button type="button" class="btn btn-white" data-dismiss="modal" onclick="choiceArea()">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- e : POPUP - Add Operator -->

<script type="text/javascript">
	
</script>