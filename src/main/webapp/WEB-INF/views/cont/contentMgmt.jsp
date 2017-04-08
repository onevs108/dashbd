<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<!--     <meta charset="utf-8"> -->
<!--     <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
<!--     <title>Content Mgmt</title> -->

<!-- 	<link href="/dashbd/resources/newPublish/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- 	<link href="/dashbd/resources/newPublish/font-awesome/css/font-awesome.css" rel="stylesheet"> -->
<!-- 	<link href="/dashbd/resources/newPublish/css/animate.css" rel="stylesheet"> -->
<!-- 	<link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet"> -->
	
<!-- 	<!-- Toastr style -->
<!-- 	<link href="/dashbd/resources/newPublish/css/plugins/toastr/toastr.min.css" rel="stylesheet"> -->
	
	<link href="../resources/css/custom.css" rel="stylesheet">
	
<!-- 	<!-- Sweet Alert -->
<!-- 	<link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet"> -->
	<jsp:include page="../common/head.jsp" />
<!-- 	<script src="../resources/js/jquery-2.1.1.js"></script> -->
	<script src="../resources/js/jquery.cookie.js"></script>
<!-- 	<script src="../resources/js/bootstrap.min.js"></script> -->
	<script src="../resources/js/bootstrap-table.js"></script>
<!-- 	<script src="../resources/js/plugins/metisMenu/jquery.metisMenu.js"></script> -->
<!-- 	<script src="../resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script> -->
<!-- 	<script src="../resources/js/common.js"></script> -->
	
	<script src="../resources/js/modules/content-list.js"></script>
</head>

<body>

<div id="wrapper">

    <!-- sidebar -->
    <jsp:include page="../common/leftTab.jsp" />

    <!-- content -->
    <div id="page-wrapper" class="gray-bg">

        <!-- content header -->
        <c:import url="/resources/header.do"></c:import>
        <div class="wrapper wrapper-content">

            <!-- User Mgmt -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-content">
                            <div class="row" id="search-area">
                            	<div class="col-sm-2">
                                    <select class="input-sm form-control input-s-sm" id="search-type">
                                    	<option value="">All</option>
                                		<option value="streaming">Streaming</option>
                                   		<option value="file">File</option>
                                    </select>
                                </div>
                            	<div class="col-sm-2">
                                    <select class="input-sm form-control input-s-sm" id="search-column">
                                		<option value="title">Title</option>
                                   		<option value="category">Category</option>
                                    </select>
                                </div>
                                <div class="col-sm-3" id="search-keyword-area">
                                    <div class="input-group"><input type="text" placeholder="Search" class="input-sm form-control" id="search-keyword"> <span class="input-group-btn">
                                            <button type="button" class="btn btn-sm btn-primary" id="go-search"> Search</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-sm-3 pull-right text-right">
                                    <button type="button" class="btn btn-primary btn-sm" id="btn-add-user">Add</button>
                                </div>
                            </div>
                            <div class="table-responsive">
                            	<table class="table table-bordered" id="table"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- content body end -->
    </div><!-- content end -->
</div><!-- wrapper end -->

<script type="text/javascript">
	$(document).ready(function() {
		getMenuList('CONTENTS_MGMT');
		getContentList('${isBack}' == 'true' ? true : false, false);
	});
</script>
</body>
</html>
