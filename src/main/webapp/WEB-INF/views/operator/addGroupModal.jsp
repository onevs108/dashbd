<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- s : POPUP - ADD GROUP -->
<div class="addGroup modal inmodal fade" id="addGroupModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title text-left">Add National Group</h4>
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span
						class="sr-only">Close</span>
				</button>
			</div>
			<form role="form" id="form" class="form-horizontal">
				<input type="hidden" id="groupId" name="groupId" value="${operator.id}">
				<div class="modal-body">
					<fieldset>
						<div class="row">
							<div class="col-lg-5">
								<div class="form-group">
									<label class="col-sm-4 control-label">Group Name</label>
									<div class="col-sm-8">
										<input type="text" placeholder="" class="form-control" name="groupName" value="${operator.name}">
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-4 control-label">Description</label>
									<div class="col-sm-8">
										<textarea name="groupDescription" class="form-control" rows="6">${operator.description}</textarea>
									</div>
								</div>
								
								<div class="col-sm-12">
									<div class="form-group">
                                              <label for="product_name">Select Menus to grant</label>
                                              <div class="grant scroll-y white-bg">
                                              	<ul class="list-group menuArea">
                                              		<c:forEach var="obj" items="${permissionList}" varStatus="status">
                                              			<li class="list-group-item"><div class="i-checks"><label> <input type="checkbox" value="${obj.id}" <c:if test="${obj.checkYn == 'Y'}">checked</c:if>> <i></i> ${obj.name}</label></div></li>
                                              		</c:forEach>
                                              	</ul>
                                              </div>
									</div>
								</div>
							</div><!-- // col -->
							
							<div class="col-lg-7">
								<div class="col-sm-12">
									<div class="form-group">
                                        <label for="product_name">Member</label>
                                        <div class="Member scroll-y white-bg">
                                        	<ul class="list-group select-list initMemberArea">
                                        		<c:forEach var="obj" items="${initMemberList}" varStatus="status">
                                        			<c:set value="${obj.userId} (${obj.lastName} ${obj.firstName})" var="iname"/>
                                        			<li class="list-group-item"><div class="i-select"><label> <input type="checkbox" value="${obj.userId}" checked> <span>${iname}</span></label></div></li>	
                                        		</c:forEach>
                                        	</ul>
                                        </div>
									</div>
									
									<div class="row p-xxs">
										<div class="text-center">
											<button class="btn btn-info btn-circle btn-lg" type="button" onclick="addMmeber('addGroupModal')"><i class="fa fa-arrow-up"></i></button>
											<button class="btn btn-info btn-circle btn-lg" type="button" onclick="deleteMmeber('addGroupModal')"><i class="fa fa-arrow-down"></i></button>
										</div>
									</div>
									
									<div class="form-group">
                                              <label for="product_name">User</label>
                                              <div class="row">
                                              	<div class="col-lg-12">
												<div class="col-xs-3">
													<div class="form-group">
														<select class="form-control">
															<option value="0">ID</option>
															<option value="1">Option 2</option>
															<option value="2">Option 3</option>
															<option value="3">Option 4</option>
														</select>
													</div>
												</div>
												<div class="col-xs-9">
													<div class="form-group">
														<div class="input-group">
															<input type="text" placeholder="Search" class="form-control"> <span class="input-group-btn">
																<button type="button" class="btn btn-primary">Search</button>
															</span>
														</div>
													</div>
												</div>
											</div><!-- // col -->
										</div><!-- // row -->
										
										
                                              <div class="Member scroll-y white-bg">
                                              	<ul class="list-group select-list addMemberArea">
                                              		<c:forEach var="obj" items="${otherMemberList}" varStatus="status">
                                              			<c:set value="${obj.userId} (${obj.lastName} ${obj.firstName})" var="oname"/>
                                              			<li class="list-group-item"><div class="i-select"><label> <input type="checkbox" value="${obj.userId}"> <span><c:out value="${oname}"/></span></label></div></li>
                                              		</c:forEach>
                                              	</ul>
                                              </div>
									</div>
								</div>
							</div>
						</div><!-- // row -->
						
					</fieldset>
				</div>
			</form>

			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">Cancel</button>
				<c:choose>
					<c:when test="${accessDiv == 'add'}">
						<button type="button" class="btn btn-primary" onclick="proccessGroup('${groupDiv}', 'add')">Add</button>	
					</c:when>
					<c:when test="${accessDiv == 'edit'}">
						<button type="button" class="btn btn-primary" onclick="proccessGroup('${groupDiv}', 'edit')">Save</button>	
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</div>
<!-- e : POPUP - ADD GROUP -->