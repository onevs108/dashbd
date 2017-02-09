<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%= new java.util.Date() %>" />

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="common/head.jsp" />
</head>
<body>
	<div id="wrapper" class="main">
		<!-- sidebar -->
		<jsp:include page="common/leftTab.jsp" />
		
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<jsp:include page="common/header.jsp" />	
			<!-- s : wrapper -->
			<div class="wrapper wrapper-content">
				<div class="main-sch">
					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-title">
									<h5>National Schedule</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
								</div>
								<!-- // ibox-title -->

								<div class="row">
									<div class="col-lg-12">
										<div class="ibox">
											<div class="ibox-content">
												<div class="row">
													<form class="form-horizontal">
														<div class="form-group col-lg-4">
															<label class="col-sm-6 control-label">Service
																Type</label>
															<div class="col-sm-6">
																<select class="form-control">
																	<option value="0">Option 1</option>
																	<option value="1">Option 2</option>
																	<option value="2">Option 3</option>
																	<option value="3">Option 4</option>
																</select>
															</div>
														</div>
														<div class="form-group col-lg-4">
															<label class="col-sm-6 control-label">Service
																Class</label>
															<div class="col-sm-6">
																<select class="form-control">
																	<option value="0">Option 1</option>
																	<option value="1">Option 2</option>
																	<option value="2">Option 3</option>
																	<option value="3">Option 4</option>
																</select>
															</div>
														</div>
													</form>
												</div>
											</div>
											<!-- // ibox-content -->
										</div>
										<!-- // ibox -->
									</div>
									<!-- // col -->
								</div>
								<!-- // row -->

								<div class="iboxGroup">
									<div class="row">
										<div class="col-lg-8">
											<div class="ibox">
												<div class="ibox-content">
													<div class="row">
														<figure class="col-lg-6 movie">
															<iframe width="355" height="200" src=""
																style="max-width: 495px;" frameborder="0"
																allowfullscreen=""></iframe>
															<!-- src 안에 영상주소 --->
														</figure>
														<div class="col-lg-6">
															<ul class="list-group clear-list">
																<li class="list-group-item fist-item"><span
																	class="pull-right"> Streaming </span> <span
																	class="label label-primary">1</span> Service Type :</li>
																<li class="list-group-item text-info"><strong>
																		<span class="pull-right"> News Room </span> <span
																		class="label label-primary">2</span> Service Name :
																</strong></li>
																<li class="list-group-item"><span
																	class="pull-right"> Jio News </span> <span
																	class="label label-primary">3</span> Service Class :</li>
																<li class="list-group-item"><span
																	class="pull-right"> <i class="fa fa-user"></i>
																		1240
																</span> <span class="label label-primary">4</span> Num Viewers
																	:</li>
																<li class="list-group-item"><span
																	class="pull-right"> Unicast </span> <span
																	class="label label-primary">5</span> Service Mode :</li>
																<li class="list-group-item"><span
																	class="pull-right"> 24:23 </span> <span
																	class="label label-primary">6</span> Time Remaining :</li>
															</ul>
														</div>
													</div>
												</div>
												<!-- // ibox-content -->
											</div>
											<!-- // ibox -->
										</div>
										<!--// row -->
										
										<div class="col-lg-4 profile-content today">
											<div class="ibox">
												<div class="ibox-content">
													<h2 class="text-center"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}" /></h2>
													<div id="clock" class="light">
														<div class="digits"></div>
													</div>
													<h3 class="font-bold no-margins text-center total">
														<div class="panel panel-primary">
															<div class="panel-heading">Total Users</div>
															<div class="panel-body">
																<p class="text-info">123</p>
															</div>
														</div>
													</h3>
												</div>
												<!-- // ibox-content -->
											</div>
											<!-- // ibox -->
										</div>
										<!--// col -->
									</div>
									<!-- // row -->
								</div>
								<!-- // iboxGroup -->
							</div>
							<!-- e : ibox -->
						</div>
						<!-- // col -->
					</div>
					<!-- // row -->

					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-content">
									<div class="timetable"></div>
								</div>
							</div>
						</div>
					</div>
					<!--// row -->

					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-title">
									<h5>Regional Schedule</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content">
									<div class="row">
										<form class="form-horizontal">
											<div class="col-lg-4 form-group">
												<label class="col-sm-6 control-label">Service Type</label>
												<div class="col-sm-6">
													<select
														class="input-sm form-control input-s-sm inline col-sm-10">
														<option value="0">Option 1</option>
														<option value="1">Option 2</option>
														<option value="2">Option 3</option>
														<option value="3">Option 4</option>
													</select>
												</div>
											</div>
											<div class="col-lg-4 form-group">
												<label class="col-sm-6 control-label">Service Class</label>
												<div class="col-sm-6">
													<select
														class="input-sm form-control input-s-sm inline col-sm-10">
														<option value="0">Option 1</option>
														<option value="1">Option 2</option>
														<option value="2">Option 3</option>
														<option value="3">Option 4</option>
													</select>
												</div>
											</div>
										</form>
									</div>
									<div class="table-responsive tb_tpl">
										<table class="table table-striped">
											<colgroup>
												<col>
												<col style="width: 11.8%;">
												<col style="width: 19.5%;">
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col style="width: 100px;">
											</colgroup>
											<thead>
												<tr>
													<th></th>
													<th>Circle Name</th>
													<th>Service Name</th>
													<th></th>
													<th></th>
													<th>Service Class</th>
													<th>Service ID</th>
													<th>Start</th>
													<th>Stop</th>
													<th>Users</th>
													<th>Bandwidth</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><i class="fa fa-plus-square"></i></td>
													<td>Asam</td>
													<td>2002 World cup Final</td>
													<td>S</td>
													<td>M</td>
													<td>Jio Sports</td>
													<td>BCP1</td>
													<td>9:00:01</td>
													<td>10:59:55</td>
													<td>287</td>
													<td>
														<div class="progress progress-mini">
															<div style="width: 68%;" class="progress-bar"></div>
														</div>
													</td>
												</tr>

												<!-- s : Slide Content -->
												<tr>
													<td colspan="11">
														<table class="table table-striped">
															<colgroup>
																<col style="width: 15.6%;">
																<col style="width: 19.5%;">
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col style="width: 100px;">
															</colgroup>
															<thead>
																<tr>
																	<th>City</th>
																	<th>Service Name</th>
																	<th></th>
																	<th></th>
																	<th>Service Class</th>
																	<th>Service ID</th>
																	<th>Start</th>
																	<th>Stop</th>
																	<th>Users</th>
																	<th>Bandwidth</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
												<!-- e : Slide Content -->

												<tr>
													<td><i class="fa fa-plus-square"></i></td>
													<td>Asam</td>
													<td>2002 World cup Final</td>
													<td>S</td>
													<td>M</td>
													<td>Jio Sports</td>
													<td>BCP1</td>
													<td>9:00:01</td>
													<td>10:59:55</td>
													<td>287</td>
													<td>
														<div class="progress progress-mini">
															<div style="width: 68%;" class="progress-bar"></div>
														</div>
													</td>
												</tr>

												<!-- s : Slide Content -->
												<tr>
													<td colspan="11">
														<table class="table table-striped">
															<colgroup>
																<col style="width: 15.6%;">
																<col style="width: 19.5%;">
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col style="width: 100px;">
															</colgroup>
															<thead>
																<tr>
																	<th>City</th>
																	<th>Service Name</th>
																	<th></th>
																	<th></th>
																	<th>Service Class</th>
																	<th>Service ID</th>
																	<th>Start</th>
																	<th>Stop</th>
																	<th>Users</th>
																	<th>Bandwidth</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>jerhat</td>
																	<td>24Horus</td>
																	<td>S</td>
																	<td>U</td>
																	<td>Jio Drama</td>
																	<td>BCP1</td>
																	<td>9:00:01</td>
																	<td>10:59:55</td>
																	<td>287</td>
																	<td>
																		<div class="progress progress-mini">
																			<div style="width: 68%;" class="progress-bar"></div>
																		</div>
																	</td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
												<!-- e : Slide Content -->

											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- // main-sch -->
			</div>
			<!-- e : wrapper -->
			<jsp:include page="common/footer.jsp" />	
		</div>
	</div>
	
	<!-- Mainly scripts -->
	<script src="/dashbd/resources/newPublish/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="/dashbd/resources/newPublish/js/plugins/video/responsible-video.js"></script>
	<script	src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.0.0/moment.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/digitalclock/script.js"></script>

	<!-- Time Table --> 
	<script src="/dashbd/resources/newPublish/js/plugins/timetable/timetable.min.js"></script>
	
	<script>
		var timetable = new Timetable();
	
		timetable.setScope(9, 3)
	
		timetable.addLocations([ 'Rotterdam', 'Madrid', 'Los Angeles',
				'London', 'New York', 'Jakarta', 'Tokyo' ]);
	
		timetable.addEvent('Sightseeing', 'Rotterdam', new Date(2015, 7, 17, 9,
				00), new Date(2015, 7, 17, 11, 30), {
			url : '#'
		});
		timetable.addEvent('Zumba', 'Madrid', new Date(2015, 7, 17, 12),
				new Date(2015, 7, 17, 13), {
					url : '#'
				});
		timetable.addEvent('Zumbu', 'Madrid', new Date(2015, 7, 17, 13, 30),
				new Date(2015, 7, 17, 15), {
					url : '#'
				});
		timetable.addEvent('Lasergaming', 'London', new Date(2015, 7, 17, 17,
				45), new Date(2015, 7, 17, 19, 30), {
			class : 'vip-only',
			data : {
				maxPlayers : 14,
				gameType : 'Capture the flag'
			}
		});
		timetable.addEvent('All-you-can-eat grill', 'New York', new Date(2015,
				7, 17, 21), new Date(2015, 7, 18, 1, 30), {
			url : '#'
		});
		timetable.addEvent('Hackathon', 'Tokyo', new Date(2015, 7, 17, 11, 30),
				new Date(2015, 7, 17, 20)); // options attribute is not used for this event
		timetable.addEvent('Tokyo Hackathon Livestream', 'Los Angeles',
				new Date(2015, 7, 17, 12, 30), new Date(2015, 7, 17, 16, 15)); // options attribute is not used for this event
		timetable.addEvent('Lunch', 'Jakarta', new Date(2015, 7, 17, 9, 30),
				new Date(2015, 7, 17, 11, 45), {
					url : '#'
				});
		timetable.addEvent('Cocktails', 'Rotterdam', new Date(2015, 7, 18, 00,
				00), new Date(2015, 7, 18, 02, 00), {
			class : 'vip-only'
		});
	
		var renderer = new Timetable.Renderer(timetable);
		renderer.draw('.timetable');
		
		$(document).ready(
			function() {
				function toggleTable() {
					var toggleBtn = $('.tb_tpl > table > tbody > tr:even'), detail = $('.tb_tpl > table > tbody > tr:odd');
					detail.hide();
	
					toggleBtn.click(function(e) {
						if ($(this).find(".fa").hasClass(
								"fa-plus-square")) {
							$(this).find(".fa").removeClass(
									"fa-plus-square");
							$(this).find(".fa").addClass(
									"fa-minus-square");
						} else {
							$(this).find(".fa").removeClass(
									"fa-minus-square");
							$(this).find(".fa").addClass(
									"fa-plus-square");
						}
						e.preventDefault();
						$(this).next('tr').toggle();
					});
				}
	
				toggleTable();
				getMenuList('MAIN');
		});
	</script>
</body>
</html>
