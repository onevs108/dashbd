<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>main</title>
	
	<link href="/dashbd/resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="/dashbd/resources/font-awesome/css/font-awesome.css" rel="stylesheet">
	
	<link href="/dashbd/resources/css/style.css" rel="stylesheet">
	
	<!-- Login page css -->
	<link href="/dashbd/resources/css/login.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="/dashbd/resources/css/plugins/footable/footable.core.css" rel="stylesheet">
    
    <script src="/dashbd/resources/js/jquery-2.1.1.js"></script>
	<script src="/dashbd/resources/js/bootstrap.min.js"></script>
	
	<!-- Page-Level Scripts -->
	<script>
		$(document).ready(function() {
		});
		
		function onClickLoginBtn() {
			var userId = $('#userId').val();
			var password = $('#password').val();
			var operatorId = $('#operatorId').val();
			
			if (userId == null || userId.length == 0) {
				alert('Please input your ID.');
				return false;
			}
			
			if (password == null || password.length == 0) {
				alert('Please input your password.');
				return false;
			}
			
			if (operatorId == null || operatorId.length == 0) {
				alert('Please select your operator.');
				return false;
			}
			
			$('#loginForm').submit();
		}
		
		$(function() {
			// login button click event
			$('#loginBtn').click(onClickLoginBtn);
			
			// enter on password event 
			$('#password').keypress(function() {
				 var keyupEvent = event || window.event;
				    if (keyupEvent.keyCode == 13) {
				    	onClickLoginBtn();
				}
			});
		});
		
	</script>
</head>

<body>

	<div id="wrapper">
		<img id="loginTitle" align="middle" src="/dashbd/resources/img/logo_small.png">
		<form class="form-signin" id="loginForm" action="/dashbd/authentication" method="post">
			<label for="inputEmail" class="sr-only">ID</label>
			<input type="text" id="userId" name="userId" class="form-control" placeholder="ID" required autofocus>
			<label for="inputPassword" class="sr-only">Password</label>
			<input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
			<select class="form-control" id="operatorId" name="operatorId">
				<option value="">Please select your operator</option>
				<option value="-1">Super Admin</option>
				<c:forEach items="${operatorList}" var="operator">
					<option value="${operator.id}">${operator.name}</option>
				</c:forEach>
			</select>
			<button class="btn btn-lg btn-primary btn-block" type="button" id="loginBtn">Login</button>
		</form>
	</div><!-- end wrapper -->

</body>
</html>
