<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>

	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="/dashbd/resources/js/jquery-2.1.1.js"></script>
	
	<!-- Sweet Alert -->
	<link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	
	<!-- Sweet alert -->
	<script src="/dashbd/resources/js/plugins/sweetalert/sweetalert.min.js"></script>

	<script type="text/javascript">
	var COMMON_SERVER_ERROR	= 9999;
	var LOGIN_FAIL_CREDENTIALS_EXPIRED = 105;
	var LOGIN_FAIL_LOCKED = 106;
	var LOGIN_FAIL_MISMATCH_PASSWORD = 108;
	
	$(document).ready(function() {
		var cause = '${cause}' * 1;
		
		if (cause == COMMON_SERVER_ERROR) {
			swal({title:"Fail !", text:"Server Error! Please try again.", type:"warning"}, function() {
				history.back();
			});
		}
		else if (cause == LOGIN_FAIL_CREDENTIALS_EXPIRED) {
			swal({title:"Fail !", text:"Your Account has been reset! Please change your password!", type:"warning"}, function() {
				$("#initForm").submit();
			});
		}
		else if (cause == LOGIN_FAIL_LOCKED) { 
			swal({title:"Fail !", text:"Your Account is Locked! Please you report to admin!", type:"warning"}, function() {
				history.back();	
			});
		}
		else if(cause == LOGIN_FAIL_MISMATCH_PASSWORD) {
			swal({title:"Fail !", text:"Incorrect Password!", type:"warning"}, function() {
				history.back();	
			});
		}
		else {
			swal({title:"Fail !", text:"Incorrect Account", type:"warning"}, function() {
				location.href='/dashbd/login.do';
			});
		}
	});
	
	</script>
</head>
<body>
	<form id="initForm" action="/dashbd/change_password.do" method="POST" enctype="application/x-www-form-urlencoded">
		<input type="hidden" id="userId" name="userId" value="${userId}">
	</form>
</body>
</html>