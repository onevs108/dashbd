<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
<script src="/dashbd/resources/app-js/apps/schedule_add_main_map.js"></script>

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
		                            <h5>Area & Town Management</h5>
		                            <div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
		                        </div>
		                        <div class="ibox-content">
									<div class="row">
										<div class="col-lg-12">
											<div class="table-responsive">
												<div class="india">
													<jsp:include page="circleImage.jsp" />
													<div id="map" class="google-map" style="display: none; width:604px; height:709px;"></div>
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