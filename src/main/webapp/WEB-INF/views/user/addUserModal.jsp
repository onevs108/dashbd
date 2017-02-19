<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
									<label class="col-sm-6 control-label">Select Group</label>
									<div class="col-sm-6">
										<select name="status" onchange="changeGroup(this, 'modal_circle_area')" id="modal-grade-id" class="input-sm form-control input-s-sm">
										<option value="">Group</option>
										<c:choose>
											<c:when test="${USER.grade == 13}">
												<c:forEach var="row" items="${gradeList}">
													<option value="${row.id}">${row.name}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
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
											</c:otherwise>
										</c:choose>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div id="modal_circle_area" class="col-lg-6" style="display:none">
								<div class="form-group">
									<label class="col-sm-6 control-label">Select Circle</label>
									<div class="col-sm-6">
										<select class="form-control" id="modal-circle-id" onchange="getTownListFromCircle(this)">
											<option value="">Circle</option>
											<c:forEach items="${circleList}" var="circle">
												<option value="${circle.circle_id}">${circle.circle_name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div id="modal_circle_group" class="col-lg-6">
								<label class="col-sm-6 control-label">Circle Group</label>
								<div class="col-sm-6">
                                   	<select class="input-sm form-control input-s-sm" id="form-circle" <c:if test="${USER.grade != 13}">disabled="disabled"</c:if>>
                                   		<option value="">Circle Group</option>
									</select>
                                </div>
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<div class="input-group" style="padding: 0 0 0 0;">
									<input type="text" placeholder="User ID" class="form-control" id="form-user-id" onkeyup="javascript:checkUserId=false;"> 
									<span class="input-group-btn">
										<button type="button" id="form-check-btn" onclick="doCheckId()" class="btn btn-primary">Check</button>
									</span>
								</div>
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Last Name"
									class="form-control" id="form-last-name" name="form-last-name">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="password" placeholder="Password"
									class="form-control" id="form-password" name="form-password">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="First Name"
									class="form-control" id="form-first-name" name="form-first-name">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-6 m-b">
								<input type="password" placeholder="Confirm Password"
									class="form-control" id="form-confirm-password" name="form-confirm-password">
							</div>
							<div class="col-lg-6 m-b">
								<input type="text" placeholder="Department"
									class="form-control" id="form-department" name="form-department">
							</div>
						</div>
						<!-- // row -->

						<div class="row">
							<div class="col-lg-12 m-b">
								<textarea class="form-control" rows="6" placeholder="Memo" id="form-memo" name="form-memo"></textarea>
							</div>
						</div>
						<!-- // row -->
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" id="addBtn" onclick="doInsert('add')" class="btn btn-primary">Add</button>
					<button type="button" id="editBtn" onclick="doInsert('edit')" class="btn btn-primary">Edit</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- e : POPUP - Add Operator -->

<script type="text/javascript">
	
</script>