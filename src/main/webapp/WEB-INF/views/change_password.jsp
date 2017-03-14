<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>SeSM</title>

    <link href="/dashbd/resources/newPublish/css/bootstrap.min.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/dashbd/resources/newPublish/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet">
	
	<!-- Sweet Alert -->
	<link href="/dashbd/resources/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	
	<!-- Sweet alert -->
	<script src="/dashbd/resources/js/plugins/sweetalert/sweetalert.min.js"></script>
	
</head>

<body class="gray-bg">
    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <img src="/dashbd/resources/newPublish/img/common/img_logo.png" alt=""><h3>SeSM Change Password</h3>
            <form class="m-t" role="form" id="passwordForm" action="javascript:void(0);" method="post" autocomplete="off">
                <div class="form-group">
                    <input type="hidden" id="userId" name="userId" class="form-control" placeholder="Username" value="${userId}" required>
                </div>
                <div class="form-group">
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password" required autofocus>
                </div>
                <div class="form-group">
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Confirm Password" required>
                </div>
                <button type="submit" id="changeBtn" class="btn btn-primary block full-width m-b" onclick="changePassword()">Change Password</button>
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
		
		function changePassword() {
			if($("#password").val() != $("#confirmPassword").val()) {
				swal({title:"Fail !", text:"Passwords do not match!! Confirm Password!", type:"warning"});
			} else if($("#userId").val() == '') {
				swal({title:"Fail !", text:"Failed!! Please you report to admin!", type:"warning"});
			} else {
				$.ajax({
					url: '/dashbd/changePassword.do',
					method: 'POST',
					dataType: 'json',
					data: $("#passwordForm").serialize()
					,
					success: function(data, textStatus, jqXHR) {
						if (data.resultCode == 'S') { // 성공
							swal({title:"Success !", text:"Success", type:"success"}, function() {
								location.href="/dashbd/login.do";
							});
						}
						else { // 실패
							swal("Fail !", "Failed!! Please you report to admin!", "warning");
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						swal("Fail !", errorThrown + textStatus, "warning");
						return false;
					}
				});
			}
		}
	</script>
</body>
</html>