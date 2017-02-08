<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Login</title>

    <link href="/dashbd/resources/newPublish/css/bootstrap.min.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/dashbd/resources/newPublish/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <h3>Dashboard Sign in</h3>
            <form class="m-t" role="form" id="loginForm" action="/dashbd/authentication" method="post" autocomplete="off">
                <div class="form-group">
                    <input type="text" id="userId" name="userId" class="form-control" placeholder="Username" required autofocus>
                </div>
                <div class="form-group">
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
                </div>
                <button type="submit" id="loginBtn" class="btn btn-primary block full-width m-b">Login</button>
            </form>
        </div>
    </div>

    <!-- Mainly scripts -->
    <script src="/dashbd/resources/newPublish/js/jquery-2.1.1.js"></script>
    <script src="/dashbd/resources/newPublish/js/bootstrap.min.js"></script>
    
    <script src="/dashbd/resources/newPublish/js/jquery-2.1.1.js"></script>
	<script src="/dashbd/resources/newPublish/js/bootstrap.min.js"></script>
	<!-- Page-Level Scripts -->
	<script>
		$(document).ready(function() {
		});
		
		function onClickLoginBtn() {
			var userId = $('#userId').val();
			var password = $('#password').val();
// 			var operatorId = $('#operatorId').val();
			
			if (userId == null || userId.length == 0) {
				alert('Please input your ID.');
				return false;
			}
			
			if (password == null || password.length == 0) {
				alert('Please input your password.');
				return false;
			}
			
// 			if (operatorId == null || operatorId.length == 0) {
// 				alert('Please select your operator.');
// 				return false;
// 			}
			
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
</body>
</html>