<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<jsp:include page="../common/head.jsp" />
</head>
<body>
<div id="wrapper">
	<!-- sidebar -->
	<jsp:include page="../common/leftTab.jsp" />
	
	<div id="page-wrapper" class="gray-bg dashbard-1">
		<jsp:include page="../common/header.jsp" />	
		
		<!-- content body -->
        <div class="wrapper wrapper-content">
        	<div class="operatorA">
        		<div class="row">
					<div class="col-lg-12">
						<div class="ibox">
							<div class="ibox-title">
								<h5>Operator Management</h5>
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
													<div class="col-lg-7">
														<div class="row">
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-6 control-label">Group</label>
																	<div class="col-sm-6">
																		<select class="form-control">
																			<option value="0">Option 1</option>
																			<option value="1">Option 2</option>
																			<option value="2">Option 3</option>
																			<option value="3">Option 4</option>
																		</select>
																	</div>
																</div>
															</div>
															<!-- // col -->
	
															<div class="col-lg-6">
																<div class="form-group">
																	<label class="col-sm-6 control-label">Circle</label>
																	<div class="col-sm-6">
																		<select class="form-control">
																			<option value="0">Option 1</option>
																			<option value="1">Option 2</option>
																			<option value="2">Option 3</option>
																			<option value="3">Option 4</option>
																		</select>
																	</div>
																</div>
															</div>
															<!-- // col -->
														</div>
													</div>
	
													<div class="col-lg-5">
														<div class="col-xs-3">
															<div class="form-group">
																<select class="form-control">
																	<option value="0">Option 1</option>
																	<option value="1">Option 2</option>
																	<option value="2">Option 3</option>
																	<option value="3">Option 4</option>
																</select>
															</div>
														</div>
														<div class="col-xs-9">
															<div class="form-group">
																<div class="input-group">
																	<input type="text" placeholder="Search"
																		class="form-control"> <span
																		class="input-group-btn">
																		<button type="button" class="btn btn-primary">Go!</button>
																	</span>
																</div>
															</div>
														</div>
													</div>
													<!-- // col -->
												</form>
											</div>
											<!-- // row -->
										</div>
										<!-- // ibox-content -->
									</div>
									<!-- // ibox -->
								</div>
								<!-- // col -->
							</div>
							<!-- // row -->
							
				            <!-- User Mgmt -->
				            <div class="row">
				                <div class="col-lg-12">
				                    <div class="ibox float-e-margins">
<!-- 				                        <div class="ibox-title"> -->
<!-- 				                            <h5>User Mgmt</h5> -->
<!-- 				                            <div class="ibox-tools"> -->
<!-- 				                                <a class="collapse-link"><i class="fa fa-chevron-up"></i></a> -->
<!-- 				                                <a class="close-link"><i class="fa fa-times"></i></a> -->
<!-- 				                            </div> -->
<!-- 				                        </div> -->
				                        <div class="ibox-content">
				                            <div class="row" id="search-area">
				                            	<div class="col-sm-3">
													<c:choose>
														<c:when test="${USER.grade == 13}">
															<select name="status" id="search-operator-id" class="input-sm form-control input-s-sm">
																<option value="">All</option>
																<c:forEach var="row" items="${gradeList}">
																	<option value="${row.id}">${row.name}</option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<select name="status" id="search-operator-id" class="input-sm form-control input-s-sm" disabled="disabled">
																<c:forEach items="${operatorList}" var="operator">
																	<c:choose>
																		<c:when test="${USER.operatorId == operator.id}">
																			<option value="${operator.id}" selected="selected">${operator.name}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${operator.id}">${operator.name}</option>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</select>
														</c:otherwise>
													</c:choose>
												</div>
				                                <div class="col-sm-3">
				                                    <select class="input-sm form-control input-s-sm" id="search-column">
				                                        <option value="all">All</option>
				                                        <option value="name">Name</option>
				                                        <option value="userId">ID</option>
				                                        <option value="department">Department</option>
<!-- 				                                        <option value="operator">Operator</option> -->
				                                    </select>
				                                </div>
				                                <div class="col-sm-3" id="search-keyword-area">
				                                    <div class="input-group"><input type="text" placeholder="Search" class="input-sm form-control" id="search-keyword"> <span class="input-group-btn">
				                                            <button type="button" class="btn btn-sm btn-primary" id="go-search"> Search</button>
				                                        </span>
				                                    </div>
				                                </div>
<!-- 				                                <div class="col-sm-3 pull-right text-right"> -->
<!-- 				                                    <button type="button" class="btn btn-primary btn-sm" id="btn-add-user">Add</button> -->
<!-- 				                                </div> -->
				                            </div>
				                            <div class="table-responsive">
				                            	<table class="table table-bordered" id="table"></table>
				                            </div>
				                            <!-- // tb_tpl -->
											<div class="hr-line-dashed"></div>
											<div class="text-right">
												<button class="btn btn-white" type="submit" data-toggle="modal" data-target="#myModal">Add Group</button>
												<button class="btn btn-primary" type="submit" data-toggle="modal" data-target="#myModal">Add User</button>
											</div>
				                        </div>
				                    </div>
				                </div>
	           				</div>
            			</div>
        			</div>
    			</div> <!-- operatorA row end -->
			</div><!-- operatorA end -->
		</div> <!-- content body end -->
	</div>
</div>

<!-- s : POPUP - Add Operator -->
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
				<i class="fa fa-laptop modal-icon"></i>
				<h4 class="modal-title">Add Operator</h4>
			</div>
			<form role="form" id="form" class="form-horizontal">
				<div class="modal-body">
					<fieldset>
						<div class="row">
							<div class="col-lg-6">
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Area</label>
									<div class="col-sm-6">
										<select class="form-control">
											<option value="0">Option 1</option>
											<option value="1">Option 2</option>
											<option value="2">Option 3</option>
											<option value="3">Option 4</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-lg-6">
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Group</label>
									<div class="col-sm-6">
										<select class="form-control">
											<option value="0">Option 1</option>
											<option value="1">Option 2</option>
											<option value="2">Option 3</option>
											<option value="3">Option 4</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<div class="input-group" style="padding: 0 0 0 0;">
									<input type="text" placeholder="User ID" class="form-control" name="user"> 
									<span class="input-group-btn">
										<button type="button" class="btn btn-primary">Check</button>
									</span>
								</div>
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Last Name"
									class="form-control" name="last">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Password"
									class="form-control" name="pw">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="First Name"
									class="form-control" name="first">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Confirm Password"
									class="form-control" name="confirm">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Department"
									class="form-control" name="dep">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-12 m-b">
								<textarea class="form-control" rows="6" placeholder="Memo"></textarea>
							</div>
						</div>
						<!-- // row -->
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary">Save changes</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- e : POPUP - Add Operator -->

<script src="js/jquery.cookie.js"></script>
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="js/modules/user-list.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('USER_MGMT');
		getUserList('${isBack}' == 'true' ? true : false, false);
	});
</script>

</body>
</html>
