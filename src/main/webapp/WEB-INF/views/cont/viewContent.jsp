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
    <link href="../resourcesRenew/css/style.css" rel="stylesheet">
<!--     <link href="../resourcesRenew/css/animate.css" rel="stylesheet"> -->
<!--     <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet"> -->
<!-- 	<link href="../resources/css/custom.css" rel="stylesheet"> -->
    
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
	<!--
	 dash.all.debug.js
	 dash.all.min.js
	 -->
	<script src="../resources/js/dash.all.debug.js"></script>
	
	<link href="../resources/css/sampleVideo.css" rel="stylesheet" />
	<script src="../resources/js/sampleVideo.js"></script>
<script>

$(document).ready(function() {
	getMenuList('CONTENTS_MGMT');
	
	$("#btnList").click(function(){
		document.location = "/dashbd/view/content.do";
	})
	
	
	$("#btnCancel").click(function(){
		document.location = "/dashbd/view/content.do";
	})
	
})


</script>
</head>
<body>
<div id="wrapper">

    <!-- sidebar -->
	<jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">
		<c:import url="/resources/header.do"></c:import>
        
<!--         <div class="row wrapper border-bottom white-bg page-heading"> -->
<!-- 			<div class="col-lg-12"> -->
<!-- 				<h2><strong>Contents Mgmt</strong></h2> -->
<!-- 				<ol class="breadcrumb"> -->
<!-- 				    <li> -->
<!-- 					   <a href="/dashbd/resources/main.do">Home</a> -->
<!-- 				    </li> -->
<!-- 				    <li class="active"> -->
<!-- 					   <strong>Contents Mgmt</strong> -->
<!-- 				    </li> -->
<!-- 				</ol> -->
<!-- 			</div> -->
<!-- 		</div>end row wrapper border-bottom white-bg page-heading -->

            
	<div class="wrapper wrapper-content">
		<form class="form-horizontal" id="insertForm" name="insertForm" method="post" enctype="multipart/form-data" action="addContentOK.do" >
		<div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div class="row">
						   <div class="col-sm-7 b-r">
						      <div class="form-group"><label class="col-sm-3 control-label">Title</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="title" name="title" required="required" value="${mapContent.title}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Category</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="category" name="category" required="required"  value="${mapContent.category}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Age restriction</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="age_restriction" name="age_restriction" required="required" value="${mapContent.age_restriction}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Running Time(sec)</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="duration" name="duration" required="required" value="${mapContent.duration}" disabled></div>
							   </div>
						<!-- 	   <div class="form-group"><label class="col-sm-3 control-label">bitrate</label>
							   	<div class="col-sm-9"><input type="text" id="bitrate" name="bitrate" class="form-control" required="required" value="${mapContent.bitrate}" disabled></div>
							   </div>  -->
							   <div class="form-group"><label class="col-sm-3 control-label">Director</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="director" name="director" value="${mapContent.director}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Actors</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="actors" name="actors" value="${mapContent.actors}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Description</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="description" name="description" value="${mapContent.description}" disabled></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">type</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="type" name="type" required="required" value="${mapContent.type}" disabled></div>
							   </div>
						   </div>
						   <div class="col-sm-5" style="padding:10px 10px 10px 10px;">
						   <!-- 
						<iframe src="${mapContent.url}" width="100%" height="400px" frameborder="0" allowfullscreen></iframe>
						    -->
						    <%-- <div align="center">
						    	<c:forEach items="${previews}" var="preview" varStatus="idx">
								    <video width="500" height="360" controls="controls" autoplay>
									    <source src="http://dash.edgesuite.net/dash264/TestCases/1c/qualcomm/2/MultiRate.mpd"/>
									</video>
								</c:forEach>
							 </div> --%>
							<div style="vertical-align: middle;">
								<video data-dashjs-player src="http://dash.edgesuite.net/dash264/TestCases/1c/qualcomm/2/MultiRate.mpd" controls></video>
							</div>
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
                            <h3>Contents URL</h3>
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
									  <img src="/dashbd/${thumnail.path}" width="80%">
								   </div>
							    </div>
							</c:forEach>
							<c:if test="${fn:length(thumnails) > 0}">
								</div>
							</c:if>
						<br>
				    </div><!-- end thumbnail ibox-content -->

                        <br>
                        
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
								   	  <i class="fa fa-file-movie-o" style="font-size:50px"></i>
								   </div>
							    </div>
							</c:forEach>
							<c:if test="${fn:length(previews) > 0}">
								</div>
							</c:if>
					    <br>
				    </div><!-- end Previews ibox-content -->
                        </div><!-- end centents ibox-content -->
                    </div>
					    <div class="row">
					    		<div class="col-md-12 text-center" style="padding:10px 0 30px;">
								<button type="button" class="btn btn-w-m btn-primary" id="btnList">LIST</button>
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
