<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

<!--     <link href="css/bootstrap.min.css" rel="stylesheet"> -->
<!--     <link href="css/style.css" rel="stylesheet"> -->
<!--     <link href="css/animate.css" rel="stylesheet"> -->
<!--     <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet"> -->
<!--     <link href="css/custom.css" rel="stylesheet"> -->
<!--     <link href="font-awesome/css/font-awesome.css" rel="stylesheet"> -->
	 <jsp:include page="../common/head.jsp" />
    <link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
</head>

<body>

<div id="wrapper">

    <jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
    	<jsp:include page="../common/header.jsp" />
        <!-- content body -->
        <div class="wrapper wrapper-content">

            <!-- User Add -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>User Form</h5>
                            <div class="ibox-tools">
<!--                                 <a class="collapse-link"> -->
<!--                                     <i class="fa fa-chevron-up"></i> -->
<!--                                 </a> -->
<!--                                 <a class="close-link"> -->
<!--                                     <i class="fa fa-times"></i> -->
<!--                                 </a> -->
                            </div>
                        </div>
                        <div class="ibox-content">
                            <form class="form-horizontal">
                            	<div class="form-group">
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Grade</label>
									<div class="col-sm-3">
                                    	<select class="input-sm form-control input-s-sm" id="form-grade" <c:if test="${USER.grade != 13 && USER.grade != 1 && USER.grade != 2 && USER.grade != 3}">disabled="disabled"</c:if>>
											<c:forEach var="row" items="${gradeList}">
												<option value="${row.id}">${row.name}</option>
											</c:forEach>
										</select>
	                                </div>
                                </div>
                                <!-- Circle 가입 부분 -->
                                <div id="circleArea" class="form-group" style="display: none;">
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Select Circle</label>
                                    <div class="col-sm-3">
                                    	<c:choose>
											<c:when test="${USER.grade == 13 or USER.grade == 1 or USER.grade == 2 or USER.grade == 3 or USER.grade == 9999}"> <!-- admin 또는 Circle 소속일때 -->
												<select name="status" id="form-operator-id" class="input-sm form-control input-s-sm">
													<option value="">Please Select</option>
													<c:forEach items="${circleList}" var="row">
														<c:choose>
															<c:when test="${USER.operatorId == operator.id}">
																<option value="${row.circle_name}" selected="selected">${row.circle_name}</option>
															</c:when>
															<c:otherwise>
																<option value="${row.circle_name}">${row.circle_name}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</c:when>
										</c:choose>
                                    </div>
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Circle Group</label>
									<div class="col-sm-3">
                                    	<select class="input-sm form-control input-s-sm" id="form-circle" <c:if test="${USER.grade != 13 && USER.grade != 1 && USER.grade != 2 && USER.grade != 3}">disabled="disabled"</c:if>>
                                    		
										</select>
	                                </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> User ID</label>
                                    <div class="col-sm-2" id="form-user-id-input-area">
									    <input type="text" id="form-user-id" class="form-control">
								    </div>
								    <div class="col-sm-1" id="form-user-id-input-area">
									    <span class="input-group-btn"><button class="btn btn-default" id="check-id-btn" type="button">Check</button></span>
								    </div>
								    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Last Name</label>
                                    <div class="col-sm-3"><input type="text" id="form-last-name" class="form-control"></div>
                                </div>
                                <div class="form-group" id="form-password-area">
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Password</label>
                                    <div class="col-sm-3"><input type="password" id="form-password" class="form-control"></div>
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> First Name</label>
                                    <div class="col-sm-3"><input type="text" id="form-first-name" class="form-control"></div>
                                </div>
                                <div class="form-group" id="form-confirm-password-area">
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Confirm Password</label>
                                    <div class="col-sm-3"><input type="password" id="form-confirm-password" class="form-control"></div>
                                    <label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Department</label>
                                    <div class="col-sm-3"><input type="text" id="form-department" class="form-control"></div>
                                </div>
                                <div class="form-group" id="form-password-area">
                                    <label class="col-sm-3 control-label"><i class="fa text-importance"></i> Memo</label>
                                    <div class="col-sm-9"><textarea id="form-memo" class="form-control"></textarea></div>
                                </div>
                                <div class="form-group" id="form-registered-date-area">
                                    <label class="col-sm-3 control-label">Registered Date</label>
                                    <div class="col-sm-9"><input type="text" id="form-registered-date" disabled="disabled" class="form-control"></div>
                                </div> 
                                <div class="form-group" id="form-modified-date-area">
                                    <label class="col-sm-3 control-label">Modified Date</label>
                                    <div class="col-sm-9"><input type="text" id="form-modified-date" disabled="disabled" class="form-control"></div>
                                </div>
<!--                                 <div class="form-group" id="form-permission-area"> -->
<!--                                     <label class="col-sm-3 control-label">Permission</label> -->
<!--                                     <div class="col-sm-9"> -->
<!--                                     	<div id="form-permission-checkbox-area"> -->
<!-- 	                                    	<div class="row"> -->
<!-- 					                            <div class="col-md-12"> -->
<!-- 					                                <fieldset> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-user" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox1"> -->
<!-- 					                                            User Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-permission" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox2"> -->
<!-- 					                                            Permission Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-contents" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox3"> -->
<!-- 					                                            Contents Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-operator" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox4"> -->
<!-- 					                                            Operator Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-bmsc" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox5"> -->
<!-- 					                                            BMSC Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-service-area" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox6"> -->
<!-- 					                                            Service Area Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-enb" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox7"> -->
<!-- 					                                            eNB Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-schedule" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox8"> -->
<!-- 					                                            Schedule Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-system" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox9"> -->
<!-- 					                                            System Management -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                    <div class="checkbox checkbox-primary"> -->
<!-- 					                                        <input id="checkbox-permission-statistic" type="checkbox" disabled="disabled"> -->
<!-- 					                                        <label for="checkbox10"> -->
<!-- 					                                            System Statistic -->
<!-- 					                                        </label> -->
<!-- 					                                    </div> -->
<!-- 					                                </fieldset> -->
<!-- 					                            </div> -->
<!-- 											</div> -->
<!-- 		                                </div> -->
<!-- 	                                </div> -->
<!--                                 </div> -->
                                <div class="form-group action-btn-group">
                                    <div class="col-sm-4 col-sm-offset-8 text-right">
                                    	<input class="btn btn-white" type="button" id="cancel-btn" value="Cancel">
                                    	<input class="btn btn-success" type="button" id="save-btn" value="OK">
                                    	<input class="btn btn-success" type="button" id="list-btn" value="List">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- content body end -->
		
    </div><!-- content end -->
</div><!-- wrapper end -->

<script src="js/modules/user-form.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('USER_MGMT');
		$("#form-grade").on("change", function(e){
			if(e.target.value == "9999"){
				$("#circleArea").show();
			}else{
				$("#circleArea").hide();
			}
		});
		$("#form-operator-id").on("change", function(e){
			getTownListFromCircle(this.selectedOptions.item(0).text);
		});
		initForm('${flag}', '${userId}');
	});
</script>
</body>
</html>
