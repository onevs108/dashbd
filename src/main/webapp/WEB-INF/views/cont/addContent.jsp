<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/head.jsp" />
<!--     <meta charset="utf-8"> -->
<!--     <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
<!--     <title>Schedule Mgmt</title> -->
<!--     <link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/style.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/css/animate.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet"> -->
    <link href="../resources/css/custom.css" rel="stylesheet">
    
<!--     Mainly scripts -->
<!-- 	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script> -->
	<script src="../resourcesRenew/js/jquery.form.js"></script>
	<script src="../resourcesRenew/js/jquery-ui-1.10.4.min.js"></script>
	
<!-- 	<script src="../resourcesRenew/js/bootstrap.min.js"></script> -->
<!-- 	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script> -->
<!-- 	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script> -->
	<script src="../resourcesRenew/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	
<!-- 	<!-- FooTable -->
		
<!-- 	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	
<!-- 	<!-- Page-Level Scripts -->
<!-- 	<script src="../resources/js/common.js"></script> -->
	
<script>

$(document).ready(function() {
	getMenuList('CONTENTS_MGMT');
	
	ctrl.initialize();
	$("#btnCancel").click(function(){
		document.location = "/dashbd/view/content.do";
	})
})

var ctrl = {
	initialize : function() {
		$("#insertForm").ajaxForm({
			dataType : "json",
			beforeSubmit : function(data, frm, opt) {
				
				/* if (!confirm(confirmMsg)) {
					return false;
				} */
			},
			success : function(result) {
				if (outMsgForAjax(result))
					document.location = "/dashbd/view/content.do";
				else
					;
			},
			error : function(request, status, error) {
				alert('err=' + error);
			}
		});
		
	}
};


</script>
</head>
<body>
<div id="wrapper">
	<jsp:include page="../common/leftTab.jsp" />
    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
		<c:import url="/resources/header.do"></c:import>
	<div class="wrapper wrapper-content">
		<form class="form-horizontal" id="insertForm" name="insertForm" method="post" enctype="multipart/form-data" action="addContentOK.do" >
		<div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div class="row">
						   <div class="col-sm-8 b-r">
						      <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Title</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="title" name="title" required="required"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Category</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="category" name="category" required="required"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Age Restriction</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="age_restriction" name="age_restriction" required="required"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Running Time(sec)</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="duration" name="duration" required="required"></div>
							   </div>
						<!--        <div class="form-group"><label class="col-sm-3 control-label">bitrate</label>
							   	<div class="col-sm-9"><input type="text" id="bitrate" name="bitrate" class="form-control" required="required"></div>
							   </div>  -->
							   <div class="form-group"><label class="col-sm-3 control-label">Director</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="director" name="director"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Actors</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="actors" name="actors"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Description</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="description" name="description"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Type</label>
							   	<div class="col-sm-9">
							   		<select id="type" name="type" class="input-sm form-control input-s-sm inline" required="required">
										<option value="streaming" selected>Streaming</option>
										<option value="file">File</option>
									</select>
							   	</div>
							   </div>
		
						   </div>
						   <div class="col-sm-4" style="padding:70px 0;">
								<p class="text-center">
                                    		<a href=""><i class="fa fa-file big-icon"></i></a>
                                		</p>
						   </div>
					    </div>
					</div>
                    </div>
			 </div>
			 
			 <div class="col-lg-12">
                    <div class="ibox float-e-margins">
				    <div class="ibox-content">
				    <br>
					  <div class="ibox-title">
                            <h3><i class="fa fa-check text-importance"></i> Contents URL</h3>
                        </div>
				    <div class="ibox-content">
					  <h4>Enter the URL where the contents located</h4>
                            <form method="get" class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-sm-8"><input type="text" id="url" name="url" placeholder="https://www.naver.com/movie/abc.mp4" class="form-control"></div>
                                </div>	
                            </form>
                        </div><!-- end centents ibox-content -->

                        <br><br>
				    <div class="ibox-title">
                            <h3>Thumbnails</h3>
                        </div>
                        <div class="ibox-content"><br>
					    <div class="row">
						    <div class="col-md-12">
								<input type="file" name="ThumbnailsFiles" id="ThumbnailsFiles"/>
						    </div>
					    </div>
				    </div><!-- end thumbnail ibox-content -->

                        <br><br>
				    <div class="ibox-title">
                            <h3>Previews</h3>
                        </div>
                        <div class="ibox-content"><br>
					    <div class="row">
						    <div class="col-md-12">
				    		   <input type="file" name="preViewFiles" id="preViewFiles"/>
						    </div>
					    </div>
					    <br>
				    </div><!-- end Previews ibox-content -->
                        </div><!-- end centents ibox-content -->
                    </div>
					    <div class="row">
					    		<div class="col-md-12 text-center" style="padding:10px 0 30px;">
								<button type="button" class="btn btn-w-m btn-default" id="btnCancel">Cancel</button>
								<button type="submit" class="btn btn-w-m btn-primary">OK</button>
							</div>
					    </div>
			 </div><!-- end Previews col-lg-12 -->
            </div><!-- row -->
	</form>
	</div><!-- end wrapper wrapper-content -->

	</div><!-- end page-wrapper -->
</div><!-- wrapper end -->
</body>
</html>
