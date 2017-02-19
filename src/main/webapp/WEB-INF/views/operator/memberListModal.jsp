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
					<table class="table table-striped">
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
								<td>
									<button type="button" class="btn btn-danger btn-xs">Remove from Group</button>
								</td>
							</tr>
							<tr>
								<td>N/A</td>
								<td>Administrator Group</td>
								<td>wkim2580</td>
								<td>Kim</td>
								<td>Wonhyeung</td>
								<td>Dev</td>
								<td>
									<button type="button" class="btn btn-danger btn-xs">Remove from Group</button>
								</td>
							</tr>
						</tbody>
					</table>
					<!-- s : paging -->
					<div class="text-right">
						<div class="btn-group">
                               <button type="button" class="btn btn-white"><i class="fa fa-chevron-left"></i></button>
                               <button class="btn btn-white">1</button>
                               <button class="btn btn-white  active">2</button>
                               <button class="btn btn-white">3</button>
                               <button class="btn btn-white">4</button>
                               <button type="button" class="btn btn-white"><i class="fa fa-chevron-right"></i> </button>
                           </div>
                          </div>
					<!-- e : paging -->
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
					<table class="table table-striped">
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
					<!-- s : paging -->
					<div class="text-right">
						<div class="btn-group">
                               <button type="button" class="btn btn-white"><i class="fa fa-chevron-left"></i></button>
                               <button class="btn btn-white">1</button>
                               <button class="btn btn-white  active">2</button>
                               <button class="btn btn-white">3</button>
                               <button class="btn btn-white">4</button>
                               <button type="button" class="btn btn-white"><i class="fa fa-chevron-right"></i></button>
                           </div>
                          </div>
					<!-- e : paging -->
				</div>
				<!-- // tb_tpl -->
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div>
	</div>
</div>
<!-- e : POPUP - MEMBER LIST -->
