<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%= new java.util.Date() %>" />

<!DOCTYPE html>
<html>
<head>
	<link href="/dashbd/resources/newPublish/css/plugins/iCheck/custom.css" rel="stylesheet">
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
									<h5>SeSM Main</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
									</div>
								</div>
								<!-- // ibox-title -->
								
								<div class="ibox-content">
									<div class="row">
										<form class="form-horizontal">
											<div class="col-lg-8">
												<div class="form-group">
													<label class="col-sm-2 control-label">Service Type</label>
													<div class="col-sm-10">
														<div class="input-group">
															<select class="input-sm form-control input-s-sm inline">
																<option value="0">All</option>
																<option value="1">Streaming</option>
																<option value="2">File download</option>
																<option value="3">Carousel – Multiple Files</option>
																<option value="3">Carousel – Single File</option>
															</select>
															<span class="input-group-btn">
																<a href="#" class="btn btn-w-m btn-link"><i class="fa fa-link"></i> <u>Select Service Area</u></a>
															</span>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">Schedule</label>
													<div class="col-sm-10">
														<div class="row">
															<div class="col-lg-12">
																<div class="m-b">
																	<label class="checkbox-inline i-checks"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="option1" name="a" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> All</label>
																	<label class="checkbox-inline i-checks"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="option1" name="a" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> On-Air</label>
																	<label class="checkbox-inline i-checks"><div class="iradio_square-green" style="position: relative;"><input type="radio" value="option1" name="a" style="position: absolute; opacity: 0;"><ins class="iCheck-helper" style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"></ins></div> Today</label>
																</div>
															</div>
														</div>
														
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">From</label>
																	<div class="col-sm-10">
																		<div class="input-group date" id="data_1">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" value="03/04/2014">
										                                </div>
																	</div>
																</div>
															</div>
														</div>
														
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-2 control-label">To</label>
																	<div class="col-sm-10">
																		<div class="input-group date" id="data_2">
										                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" value="03/04/2014">
										                                </div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">Search</label>
													<div class="col-sm-10">
														<div class="input-group">
															<input type="text" placeholder="Keyword" class="form-control">
															<span class="input-group-btn">
																<button type="button" class="btn btn-primary">검색</button>
															</span>
														</div>
													</div>
												</div>
											</div>
											<div class="col-lg-4 profile-content today">
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
										</form>
									</div>
								</div> <!-- // ibox-content -->
							</div> <!-- ibox -->
						</div> <!-- // col -->
					</div> <!-- // row -->
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
									<div class="table-responsive tb_tpl">
										<table class="table table-striped">
											<colgroup>
												<col>
												<col style="width: 9.5%;">
												<col style="width: 12%;">
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
												<col>
											</colgroup>
											<thead>
												<tr>
													<th></th>
													<th>Circle</th>
													<th>Service ID</th>
													<th>Service Name</th>
													<th>Service type</th>
													<th>Schedule Type</th>
													<th>Start time</th>
													<th>Stop time</th>
													<th>GBR</th>
													<th>FEC (%)</th>
													<th>Delivery Type</th>
													<th>#of Viewers</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><i class="fa fa-plus-square"></i></td>
													<td>Mumbai</td>
													<td><i class="ondisp"></i> urn”3gpp:jio:82</td>
													<td>SonySiz</td>
													<td>Streaming</td>
													<td>Single Sessi0n</td>
													<td>2017=01-01T09:00:00</td>
													<td>2017=01=01T12:00:000</td>
													<td>1.2Mbps</td>
													<td>14%</td>
													<td>Unicats</td>
													<td>14,093</td>
												</tr>

												<!-- s : Slide Content -->
												<tr style="display: none;">
													<td colspan="12">
														<table class="table table-striped">
															<colgroup>
																<col style="width: 11.8%;">
																<col style="width: 12%;">
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
																<col>
															</colgroup>
															<thead>
																<tr>
																	<th>Circle</th>
																	<th>Service ID</th>
																	<th>Service Name</th>
																	<th>Service type</th>
																	<th>Schedule Type</th>
																	<th>Start time</th>
																	<th>Stop time</th>
																	<th>GBR</th>
																	<th>FEC (%)</th>
																	<th>Delivery Type</th>
																	<th>#of Viewers</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td>Mumbai</td>
																	<td><i class="ondisp"></i> urn”3gpp:jio:82</td>
																	<td>SonySiz</td>
																	<td>Streaming</td>
																	<td>Single Sessi0n</td>
																	<td>2017=01-01T09:00:00</td>
																	<td>2017=01=01T12:00:000</td>
																	<td>1.2Mbps</td>
																	<td>14%</td>
																	<td>Unicats</td>
																	<td>14,093</td>
																</tr>
																<tr>
																	<td>Mumbai</td>
																	<td><i class="ondisp onair"></i> urn”3gpp:jio:82</td>
																	<td>SonySiz</td>
																	<td>Streaming</td>
																	<td>Single Sessi0n</td>
																	<td>2017=01-01T09:00:00</td>
																	<td>2017=01=01T12:00:000</td>
																	<td>1.2Mbps</td>
																	<td>14%</td>
																	<td>Unicats</td>
																	<td>14,093</td>
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
				</div> <!-- // main-sch -->
			</div> <!-- e : wrapper -->
			<jsp:include page="common/footer.jsp" />	
		</div>
	</div>
	
	<!-- Mainly scripts -->
	<!-- Mainly scripts -->
	<script src="/dashbd/resources/newPublish/js/jquery-2.1.1.js"></script>
	<script src="/dashbd/resources/newPublish/js/bootstrap.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="/dashbd/resources/newPublish/js/inspinia.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/pace/pace.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/video/responsible-video.js"></script>
	<script src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.0.0/moment.min.js"></script>
	<script src="/dashbd/resources/newPublish/js/plugins/digitalclock/script.js"></script>
	<!-- iCheck -->
    <script src="/dashbd/resources/newPublish/js/plugins/iCheck/icheck.min.js"></script>
    <!-- Data picker -->
	<script src="/dashbd/resources/newPublish/js/plugins/datapicker/bootstrap-datepicker.js"></script>

	<script>
		$(document).ready(function() {
			$('#data_1.input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });
			$('#data_2.input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });
			
			$('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
			
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
