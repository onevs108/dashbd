<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	
<!-- 	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	
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

function delContentImage(id, target){
	
	if (!confirm("The only " + target +" will be deleted. do you want this??"))
		return;
	
	var param = {
			id : id
		};
	
	$.ajax({
		type : "POST",
		url : "delContentImage.do",
		data : param,
		dataType : "json",
		success : function( data ) {
			outMsgForAjax(data);
			location.reload();
		},
		error : function(request, status, error) {
			alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}

</script>
</head>
<body>
<div id="wrapper">

    <!-- sidebar -->
    <jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
		<c:import url="/resources/header.do"></c:import>
	<div class="wrapper wrapper-content">
		<form class="form-horizontal" id="insertForm" name="insertForm" method="post" enctype="multipart/form-data" action="editContentOK.do" >
		<input type="hidden" id="id" name="id" value="${params.id}" />
		<div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div class="row">
						   <div class="col-sm-8 b-r">
						      <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Title</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="title" name="title" required="required" value="${mapContent.title}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Category</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="category" name="category" required="required"  value="${mapContent.category}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Running Time(sec)</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="duration" name="duration" required="required" value="${mapContent.duration}"></div>
							   </div>
						<!--        <div class="form-group"><label class="col-sm-3 control-label">bitrate</label>
							   	<div class="col-sm-9"><input type="text" id="bitrate" name="bitrate" class="form-control" required="required"></div>
							   </div>  -->
							   <div class="form-group"><label class="col-sm-3 control-label">Director</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="director" name="director" value="${mapContent.director}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Actors</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="actors" name="actors" value="${mapContent.actors}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Description</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="description" name="description" value="${mapContent.description}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label"><i class="fa fa-check text-importance"></i> Type(Content Mime type)</label>
							   	<div class="col-sm-9">
							   		<select id="type" name="type" class="input-sm form-control input-s-sm inline" required="required">
										<option value="streaming" <c:if test="${mapContent.type == 'streaming'}">selected</c:if>>Streaming</option>
										<option value="file" <c:if test="${mapContent.type == 'file'}">selected</c:if>>File</option>
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
                                    <div class="col-sm-8"><input type="text" id="url" name="url" placeholder="https://www.naver.com/movie/abc.mp4" class="form-control" value="${mapContent.url}"></div>
                                </div>	
                            </form>
                        </div><!-- end centents ibox-content -->

                        <br><br>
				    <div class="ibox-title">
                            <h3>Thumbnails</h3>
                        </div>
                        <div class="ibox-content">
                        <br>
					        <c:forEach items="${thumnails}" var="thumnail" varStatus="idx">
					 	    
					 	     	<c:if test="${idx.index mod 4 == 0 && idx.index != 0}"> 
  					 	     		</div>
  					 	     	</c:if>
					 	     	<c:if test="${idx.index mod 4 == 0 }">
									<div class="row">	    
							    </c:if>
							    <div class="col-md-3">
								   <div class="product-imitation">
									  <img src="${thumnail.path}" width="80%">
								   </div>
								   
								   <div class="product-desc">
									  <a href="javascript:delContentImage('${thumnail.id}', 'thumbnail')">
									  	<span class="product-close">
										 <i class="fa fa-close"></i>
									  	</span>
									  </a>
								   </div>
							    </div>
							</c:forEach>
							<c:if test="${fn:length(thumnails) > 0}">
								</div>
							</c:if>
							<br>
						<div class="row"><div class="col-md-12"><input type="file" name="ThumbnailsFiles" id="ThumbnailsFiles"/></div>
					    </div>
						<br>
				    </div><!-- end thumbnail ibox-content -->
                    <br>
				    <div class="ibox-title">
                        <h3>Previews</h3>
                        </div>
                        <div class="ibox-content"><br>
					  		<c:forEach items="${previews}" var="preview" varStatus="idx">
					 	     	<c:if test="${idx.index mod 4 == 0 && idx.index != 0}"> 
  					 	     		</div>
  					 	     	</c:if>
					 	     	<c:if test="${idx.index mod 4 == 0 }">
									<div class="row">	    
							    </c:if>
							    <div class="col-md-1">
								   <div class="product-imitation">
								   	  <a href="javascript:delContentImage('${preview.id}','preview')"><i class="fa fa-file-movie-o" style="font-size:50px"></i></a>
								   </div>
							    </div>
							</c:forEach>
							<c:if test="${fn:length(previews) > 0}">
								</div>
							</c:if>
						<br>
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
