<div class="modal fade" id="circleCiryPop">
	<div class="modal-dialog" role="document">
	<div class="modal-content" style="width: 710px;">
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
		                        <div class="ibox-title">
		                            <h5>Circle & Town Management</h5>
		                            <div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
		                        </div>
		                        <div class="ibox-content">
		                        	<div class="row">
		                        		<form class="form-horizontal">
			                        		<div class="col-lg-6">
												<div class="col-xs-6">
													<div class="form-group">
														<select class="form-control">
															<option value="0">Circle</option>
															<option value="1">Option 2</option>
															<option value="2">Option 3</option>
															<option value="3">Option 4</option>
														</select>
													</div>
												</div>
												<div class="col-xs-3">
													<div class="form-group">
														<div class="input-group">
															<select data-placeholder="Choose a Country..." class="chosen-select" multiple style="width:250px;" tabindex="4">
												                <option value="">Select</option>
												                <option value="United States">United States</option>
												            </select>
															<span class="input-group-btn">
																<button type="button" class="btn btn-primary">Go!</button>
															</span>
														</div>
													</div>
												</div>
											</div>
											<!-- // col -->
										</form>
		                        	</div>
									<div class="row">
										<div class="col-lg-12">
											<div class="table-responsive">
												<div class="india">
													<jsp:include page="circleImage.jsp" />
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
		// Add slimscroll to element
//         $('.scroll_content').slimscroll({ 
//             height: '700px'
//         })
        
		$('.circle-map .circle-item').on({
			'mouseenter' : function(e){
				$(this).addClass('hover');
				$('.circle-map > img').addClass('hover');
			},
			'mouseleave' : function(e){
				$(this).removeClass('hover');
				$('.circle-map > img').removeClass('hover');
			}
		});
		$('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });
        $('.i-select').iCheck({
            checkboxClass: 'icheckbox_square',
            radioClass: 'iradio_square',
        });
		$('.demo3').click(function() {
			swal({
				title : "Are you sure?",
				text : "Do you really want to delete the Schedule Manager Group?",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "Continue",
				closeOnConfirm : false
			},
			function() {
				swal(
						"Deleted!",
						"Group Deleted Successfully.",
						"success");
			});
		});
	
		var config = {
            '.chosen-select'           : {},
            '.chosen-select-deselect'  : {allow_single_deselect:true},
            '.chosen-select-no-single' : {disable_search_threshold:10},
            '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
            '.chosen-select-width'     : {width:"95%"}
            }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }
	})
		
	function moveToCircle(circleName) {
		alert("Move to "+circleName);
	}
	
</script>