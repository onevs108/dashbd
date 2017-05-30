<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- s : POPUP - Add Operator -->
<div class="modal inmodal" id="areaBandwidthModal" tabindex="-1" role="dialog" aria-hidden="true" style="width: 100%;" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
				<h5 class="modal-title">Service Area Bandwidth</h5>
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
								<div class="col-sm-4" id="serviceAreaTree"></div>
								<div class="col-sm-8">
									<div class="well">
										<b>Bandwidth Usage for <span id="saname"></span></b>
										<table class="table table-bordered">
											<tr class="active">
												<td class="active" style="text-align: center;">Bandwidth Configured</td>
												<td class="active" style="text-align: center;">Remaining Bandwidth</td>
												<td class="active" style="text-align: center;">Allocated Bandwidth</td>
											</tr>
											<tr class="active">
												<td class="active">
													<input type="text" class="form-control input-sm" id="bandwidth" name="bandwidth" value="" disabled="">
												</td>
												<td class="active">
													<input type="text" class="form-control input-sm" id="remainedBandwidth" name="remainedBandwidth" value="" disabled="">
												</td>
												<td class="active">
													<input type="text" class="form-control input-sm" id="usedBandwidth" name="usedBandwidth" value="" disabled="">
												</td>
											</tr>
										</table>
										<b>Service list taking the bandwidth</b>
										<table class="table table-bordered">
											<tr class="active">
												<td class="active" style="text-align: center;">Service ID</td>
												<td class="active" style="text-align: center;">Service Name</td>
												<td class="active" style="text-align: center;">Service Type</td>
												<td class="active" style="text-align: center;">Service Mode</td>
												<td class="active" style="text-align: center;">GBR</td>
											</tr>
											<tbody id="bandwidthInfoList">
											</tbody>
										</table>
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
