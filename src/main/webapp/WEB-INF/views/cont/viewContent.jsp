<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Mgmt</title>
    <link href="../resourcesRenew/css/bootstrap.min.css" rel="stylesheet">
    <link href="../resourcesRenew/css/style.css" rel="stylesheet">
    <link href="../resourcesRenew/css/animate.css" rel="stylesheet">
    <link href="../resourcesRenew/font-awesome/css/font-awesome.css" rel="stylesheet">

    
    <!-- Mainly scripts -->
	<script src="../resourcesRenew/js/jquery-2.1.1.js"></script>
	<script src="../resourcesRenew/js/jquery.form.js"></script>
	<script src="../resourcesRenew/js/jquery-ui-1.10.4.min.js"></script>
	
	<script src="../resourcesRenew/js/bootstrap.min.js"></script>
	<script src="../resourcesRenew/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="../resourcesRenew/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="../resourcesRenew/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	
	<!-- Custom and plugin javascript -->
	<script src="../resourcesRenew/app-js/apps/common.js"></script>
	
	<script src="../resources/js/common.js"></script>
	
<script>

$(document).ready(function() {
	getMenuList('CONTENTS_MGMT');
	
	
	$("#btnList").click(function(){
		
		document.location = "/dashbd/resources/contents_mgmt.html#page/1";
	})
	
	
	$("#btnCancel").click(function(){
		document.location = "/dashbd/resources/contents_mgmt.html#page/1";
	})
	
})


</script>
</head>
<body>
<div id="wrapper">

    <!-- sidebar -->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
        	<ul class="nav metismenu" id="side-menu">
             <li class="nav-header">
					<div class="dropdown profile-element">
						<a href="/dashbd/resources/main.do"><img src="/dashbd/resources/img/logo_small.png"></a>
					</div>
					<div class="logo-element">
						<img src="/dashbd/resources/img/logo2.png">
					</div>
				</li>
			</ul>
        </div>
    </nav><!-- sidebar end -->

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">

	<div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
            <form role="search" class="navbar-form-custom" action="search_results.html">
                <div class="form-group">
                    <input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
                </div>
            </form>
        </div><!-- end navbar-header -->
        
            <ul class="nav navbar-top-links navbar-right">
                <li>
                    <a>
                    <i class="fa fa-user"></i><span id="navbar-user-name"></span>
                    </a>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="mailbox.html">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="profile.html">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="grid_options.html">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="notifications.html">
                                    <strong>See All Alerts</strong>
                                    <i class="fa fa-angle-right"></i>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>

                <li>
                    <a href="/dashbd/out">
                        <i class="fa fa-sign-out"></i> Log out
                    </a>
                </li>
                <li>
                    <a href="login.html">
                        <img src="../resources/img/samsung_small.png">
                    </a>
                </li>
            </ul>
        </nav>
	</div><!-- end border-bottom -->


	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-12">
			<h2>Create New Content</h2>
			<ol class="breadcrumb">
			    <li>
				   <a href="index.html">Home</a>
			    </li>
			    <li class="active">
				   <strong>Create New Content</strong>
			    </li>
			</ol>
		</div>
	</div><!-- end row wrapper border-bottom white-bg page-heading -->
            
            
	<div class="wrapper wrapper-content">
		<form class="form-horizontal" id="insertForm" name="insertForm" method="post" enctype="multipart/form-data" action="addContentOK.do" >
		<div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div class="row">
						   <div class="col-sm-8 b-r">
						   
							   <div class="form-group"><label class="col-sm-3 control-label">type</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="type" name="type" required="required" value="${mapContent.type}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Title</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="title" name="title" required="required" value="${mapContent.title}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Age restriction</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="age_restriction" name="age_restriction" required="required" value="${mapContent.age_restriction}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">duration</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="duration" name="duration" required="required" value="${mapContent.duration}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">bitrate</label>
							   	<div class="col-sm-9"><input type="text" id="bitrate" name="bitrate" class="form-control" required="required" value="${mapContent.bitrate}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Director</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="director" name="director" value="${mapContent.director}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Actors</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="actors" name="actors" value="${mapContent.actors}"></div>
							   </div>
							   <div class="form-group"><label class="col-sm-3 control-label">Description</label>
							   	<div class="col-sm-9"><input type="text" class="form-control" id="description" name="description" value="${mapContent.description}"></div>
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
									  <img src="${thumnail.path}" width="80%">
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
							    <div class="col-md-3">
								   <div class="product-imitation">
									  <img src="${preview.path}" width="80%">
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
								<button type="button" class="btn btn-w-m btn-default" id="btnCancel">Cancel</button>
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
