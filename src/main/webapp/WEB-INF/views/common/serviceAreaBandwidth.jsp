<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- s : POPUP - Add Operator -->
<div class="modal inmodal" id="areaBandwidthModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="choiceArea()">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">Service Area Bandwidth</h4>
			</div>
			<form role="form" id="form" class="form-horizontal" action="javascript:void(0);">
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
												<button type="button" onclick="searchServiceAreaTreeNode()" class="btn btn-primary">Search</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12" style="overflow: auto; height: 500px;">
								<div class="col-sm-6" id="serviceAreaTree"></div>
								<div class="col-sm-6">
									<div class="well">
	                                   <div class="form-group">
	                                       <label class="control-label">Bandwidth</label>
	                                       <div class=""><input type="text" class="form-control input-sm" id="bandwidth" name="bandwidth" value="" disabled=""></div>
	                                   </div>
	                                   <div class="form-group">
	                                       <label class="control-label">Used Bandwidth</label>
	                                       <div class=""><input type="text" class="form-control input-sm" id="usedBandwidth" name="usedBandwidth" value="" disabled=""></div>
	                                   </div>
	                                   <div class="form-group">
	                                       <label class="control-label">Remained Bandwidth</label>
	                                       <div class=""><input type="text" class="form-control input-sm" id="remainedBandwidth" name="remainedBandwidth" value="" disabled=""></div>
	                                   </div>
	                            	</div>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- e : POPUP - Add Operator -->

<script type="text/javascript">
	
</script>