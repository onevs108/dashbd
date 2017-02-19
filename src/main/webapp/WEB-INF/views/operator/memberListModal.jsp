<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- s : POPUP - MEMBER LIST -->
<div class="modal inmodal fade" id="memberListModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
			</div>
			<div class="modal-body white-bg">
				<h3>National Group</h3>
				<div class="tb_tpl table-responsive">
					<table id="table3" class="table table-striped">
						<colgroup>
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
								<th>Group</th>
								<th>ID</th>
								<th>Last Name</th>
								<th>First Name</th>
								<th>Department</th>
								<th>Command</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="obj" items="${initMemberList}" varStatus="status">
								<tr>
									<td>N/A</td>
									<td>${gruopName}</td>
									<td>${obj.userId}</td>
									<td>${obj.lastName}</td>
									<td>${obj.firstName}</td>
									<td>Dev</td>
									<td>
										<button type="button" class="btn btn-danger btn-xs">Remove from Group</button>
									</td>
								</tr>
                      			<li class="list-group-item"><div class="i-select"><label> <input type="checkbox" value="${obj.userId}" checked> <span>${iname}</span></label></div></li>	
                      		</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- // tb_tpl -->
				
				<div class="row p-m">
					<div class="text-center"><button type="button" class="btn btn-primary"><i class="fa fa-arrow-circle-o-up"></i> Add selected operator to Administrator Group</button></div>
				</div>
				
				<h3>Other Operators</h3>
				<div class="row">
					<form class="form-horizontal">
						<div class="col-lg-6">
							<div class="form-group">
								<label class="col-sm-6 control-label">Group</label>
								<div class="col-sm-6">
									<select id="gradeList" class="form-control">
										<option value="">All</option>
										<c:forEach var="row" items="${gradeList}">
											<option value="${row.id}">${row.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<!-- // col -->
						<div class="col-lg-6">
							<div class="col-xs-3">
								<div class="form-group">
									<select class="form-control">
										<option value="id">ID</option>
										<option value="first">First Name</option>
										<option value="last">Last Name</option>
										<option value="department">Department</option>
									</select>
								</div>
							</div>
							<div class="col-xs-9">
								<div class="form-group">
									<div class="input-group">
										<input type="text" placeholder="Search" class="form-control">
										<span class="input-group-btn">
											<button type="button" class="btn btn-primary">Search</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<!-- // col -->
					</form>
				</div>
				<div class="tb_tpl table-responsive">
					<table id="table4" class="table table-striped">
						<colgroup>
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
								<th>Group</th>
								<th>ID</th>
								<th>Last Name</th>
								<th>First Name</th>
								<th>Department</th>
								<th>Command</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>N/A</td>
								<td>Administrator Group</td>
								<td>wkim2580</td>
								<td>Kim</td>
								<td>Wonhyeung</td>
								<td>Dev</td>
								<td><input type="checkbox" checked class="i-checks" name="input[]"></td>
							</tr>
							<tr>
								<td>N/A</td>
								<td>Administrator Group</td>
								<td>wkim2580</td>
								<td>Kim</td>
								<td>Wonhyeung</td>
								<td>Dev</td>
								<td><input type="checkbox" class="i-checks" name="input[]"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- // tb_tpl -->
			</div>

<!-- 			<div class="modal-footer"> -->
<!-- 				<button type="button" class="btn btn-white" data-dismiss="modal">Close</button> -->
<!-- 				<button type="button" class="btn btn-primary">Save changes</button> -->
<!-- 			</div> -->
		</div>
	</div>
</div>
<!-- e : POPUP - MEMBER LIST -->
