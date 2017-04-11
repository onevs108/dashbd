<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% request.setCharacterEncoding("utf-8"); %>

<html>

<head>
	<jsp:include page="common/head.jsp" />
	
	<style type="text/css">
		.jstree-node {
			font-size:14px
		}
		
		.circle-item .noHover {
		}
		
		#map .gm-style-iw {
		    overflow:hidden!important;
		    height:371px!important;
		}
	</style>
</head>
<body>
<div id="wrapper">
	<!-- sidebar -->
	<jsp:include page="common/leftTab.jsp" />
	
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<c:import url="/resources/header.do"></c:import>
		
		<!-- content body -->
		<div class="wrapper wrapper-content">
			<div class="serviceArea">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox">
							<div class="ibox-title">
	                            <h5>Service Area Management</h5>
	                            <div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
								</div>
	                        </div>
							<div class="ibox-content">
		                        <div class="row">
									<div class="col-lg-12">
										<div class="tabs-container">
											<ul class="nav nav-tabs">
											    <li class="active"><a href="#tab-1" data-toggle="tab" onclick="tabChange('1')">Tree View</a></li>
											    <li><a href="#tab-2" data-toggle="tab" onclick="tabChange('2')">Map View</a></li>
											</ul>
											<div class="tab-content">	
												<div id="tab-1" class="tab-pane active">
													<div class="panel-body">
														<form class="form-horizontal">
															<div class="row">
																<div class="col-lg-6"><a>
																	<div class="col-xs-4">
																		<div class="form-group">
																			<select id="searchType" name="searchType" class="form-control">
											                                    <option value="">Select</option>
											                                    <option value="circle">Area</option>
											                                    <option value="city">City</option>
											                                    <option value="circleCity">Area & City</option>
											                                    <option value="hotspot">Hotspot</option>
											                                    <option value="said">SAID</option>
											                                </select>
																		</div>
																	</div>
																	<div class="col-xs-8">
																		<div class="form-group">
																			<div class="input-group">
																				<input type="text" id="search-input" name='search-input' class="form-control">
																				<span class="input-group-btn">
																					<button type="button" onclick="searchTreeNode()" class="btn btn-primary">Search</button>
																				</span>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
															<div class="row">
																<div id="treeNode" class="tab-pane active"></div>
															</div>
														</form>								
													</div>
												</div>
												<div id="tab-2" class="tab-pane">
													<input type="hidden" id="circleId" name="circleId" value="${sessionScope.USER.circleId}"> 
													<div id="mapDescriptArea" style="margin:10 0 10 0">Click the area t to view the Cities</div>
													<jsp:include page="common/circleImage.jsp" />
													<div id="map" class="google-map" style="display:none; width:604px; height:709px;"></div>
												</div>
											</div>
										</div>
									</div>
		                        </div>
		                    </div><!-- end ibox-content -->
						</div><!-- end ibox float-e-margins -->
					</div>
				</div>
			</div>
		</div><!-- end wrapper wrapper-content -->
		<jsp:include page="common/footer.jsp" />
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

<!-- 위치지정 모달 팝업 페이지 호출 -->
<jsp:include page="common/setLocationModal.jsp" />

<!-- Mainly scripts -->
<!--    <script src="/dashbd/resources/js/jquery-2.1.1.js"></script> -->
<!--    <script src="/dashbd/resources/js/bootstrap.min.js"></script> -->
<!--    <script src="/dashbd/resources/js/plugins/metisMenu/jquery.metisMenu.js"></script> -->
<!--    <script src="/dashbd/resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script> -->

<!--    <!-- FooTable --> -->
<!--    <script src="/dashbd/resources/js/plugins/footable/footable.all.min.js"></script> -->

<!--    <!-- Custom and plugin javascript --> -->
<!--    <script src="/dashbd/resources/js/inspinia.js"></script> -->
<!--    <script src="/dashbd/resources/js/plugins/pace/pace.min.js"></script> -->

<!--    <!-- Sweet alert --> -->
<!--    <script src="/dashbd/resources/js/plugins/sweetalert/sweetalert.min.js"></script> -->
   
<!-- <script src="/dashbd/resources/app-js/config.js"></script> -->

<script src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo"></script>
<!-- <script src="/dashbd/resources/js/markerwithlabel.js"></script> -->
<script src="/dashbd/resources/app-js/apps/svc_area_main_map.js"></script>
   
<!-- <script src="/dashbd/resources/js/plugins/validate/jquery.validate.min.js"></script> -->
<!-- <script src="js/common.js"></script> -->
   
<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('SERVICE_AREA_MGMT');
		
		//circle image mouse over control
		$('.circle-map .circle-item').on({
			'mouseenter' : function(e){
				if($("#circleId").val() == '') {
					$(this).addClass('hover');
					$('.circle-map > img').addClass('hover');
					$($(".circle-map").find("img")[0]).addClass("hover");
				}
			},
			'mouseleave' : function(e){
				if($("#circleId").val() == '') {
					$(this).removeClass('hover');
					$('.circle-map > img').removeClass('hover');
					$($(".circle-map").find("img")[0]).removeClass("hover");
				}
			},
			'click' : function(e) {
				if($("#circleId").val() == '') {
					var circle_id = $(this).attr("data-init");
					var circle_name = $(this).find("span small").text().replace(" Telecom Area", "");
					var lat = $(this).attr("data-lat");
					var lng = $(this).attr("data-lng");
					
					//데이터 처리 후 이전 내용을 불러오기 위한 전역변수에 셋팅
					upperCircle = {
						said : circle_id,
						name : circle_name,
						lat : lat,
						lng : lng
					}
					
					drawServiceAreaByCity(upperCircle);
				} else {
					if($(this).attr("data-init") == $("#circleId").val()) {
						var circle_id = $(this).attr("data-init");
						var circle_name = $(this).find("span small").text().replace(" Telecom Area", "");
						var lat = $(this).attr("data-lat");
						var lng = $(this).attr("data-lng");
						
						//데이터 처리 후 이전 내용을 불러오기 위한 전역변수에 셋팅
						upperCircle = {
							said : circle_id,
							name : circle_name,
							lat : lat,
							lng : lng
						}
						
						drawServiceAreaByCity(upperCircle);
					}
				}
			}
		});
	});
</script>
</body>
</html>
