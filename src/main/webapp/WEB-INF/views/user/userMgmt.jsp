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
																		<c:choose>
																			<c:when test="${USER.grade == 13 or USER.grade == 1 or USER.grade == 2 or USER.grade == 3}">
																				<select name="status" id="search-operator-id"  onchange="changeGroup(this, 'main_circle_area')" class="input-sm form-control input-s-sm">
																					<option value="">All</option>
																					<c:forEach var="row" items="${gradeList}">
																						<option value="${row.id}">${row.name}</option>
																					</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<select name="status" id="search-operator-id"  onchange="changeGroup(this, 'main_circle_area')" class="input-sm form-control input-s-sm" readonly>
																				<c:forEach items="${gradeList}" var="row">
																					<c:choose>
																						<c:when test="${USER.grade == row.id}">
																							<option value="${row.id}" selected="selected">${row.name}</option>
																						</c:when>
																						<c:otherwise>
																							<option value="${row.id}" disabled>${row.name}</option>
																						</c:otherwise>
																					</c:choose>
																				</c:forEach>
																			</c:otherwise>
																		</c:choose>
																		</select>
																	</div>
																</div>
															</div>
															<!-- // col -->
															
															<div id="main_circle_area" class="col-lg-6" <c:if test="${USER.grade !=  9999}">style="display:none;"</c:if>>
																<div class="form-group">
																	<label class="col-sm-6 control-label">Circle</label>
																	<div class="col-sm-6">
																		<select class="form-control" id="search-circle-id" <c:if test="${USER.grade ==  9999}">readonly</c:if>>
																			<option value="" <c:if test="${USER.grade == 9999}">disabled</c:if>>All</option>
																			<c:forEach items="${circleList}" var="circle">
																				<c:choose>
																					<c:when test="${USER.grade == 9999}">
																						<c:choose>
																							<c:when test="${circle.circle_name == USER.circleName}">
																								<option value="${circle.circle_name}" selected>${circle.circle_name}</option>
																							</c:when>
																							<c:otherwise>
																								<option value="${circle.circle_name}" disabled>${circle.circle_name}</option>		
																							</c:otherwise>
																						</c:choose>
																					</c:when>
																					<c:otherwise>
																						<option value="${circle.circle_name}">${circle.circle_name}</option>		
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
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
																<select class="input-sm form-control input-s-sm" id="search-column">
							                                        <option value="all">All</option>
							                                        <option value="userId">ID</option>
							                                        <option value="name">Name</option>
							                                        <option value="department">Department</option>
							                                    </select>
															</div>
														</div>
														<div class="col-xs-9" id="search-keyword-area">
						                                    <div class="input-group">
					                                    		<input type="text" placeholder="Search" class="input-sm form-control" id="search-keyword" onkeydown="javascript:if(event.keyCode == 13) $('#go-search').click();"> 
					                                    		<span class="input-group-btn">
						                                            <button type="button" class="btn btn-sm btn-primary" id="go-search"> Search</button>
						                                        </span>
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
				                            <div class="table-responsive">
				                            	<table class="table table-bordered" id="table"></table>
				                            </div>
				                            <!-- // tb_tpl -->
											<div class="hr-line-dashed"></div>
											<div class="text-right">
												<button class="btn btn-primary" type="button" onclick="userFormAccess('add')" data-toggle="modal" data-target="#myModal">Add User</button>
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
		<jsp:include page="../common/footer.jsp" />
	</div>
</div>
<jsp:include page="addUserModal.jsp" />

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
