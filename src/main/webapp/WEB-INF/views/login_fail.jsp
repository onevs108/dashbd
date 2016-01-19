<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>

	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="/dashbd/resources/js/jquery-2.1.1.js"></script>

	<script type="text/javascript">
	var COMMON_SERVER_ERROR	= 9999;
	
	$(document).ready(function() {
		var cause = '${cause}' * 1;
		
		if (cause == COMMON_SERVER_ERROR) {
			alert('Server Error! Please try again.');
			history.back();
		}
		else {
			alert('Incorrect account.');
			location.href='/dashbd/out';
		}
	});
	
	</script>
</head>
<body>
</body>
</html>