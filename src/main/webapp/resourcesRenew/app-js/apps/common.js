	function outMsgForAjax(result){
		var resultCode = result.resultInfo.resultCode
		var resultMsg = result.resultInfo.resultMsg

		if (resultCode == 1000) {
			alert(resultMsg);
			return true;
		} else {
			alert("errorcode="+resultCode+",\n msg="+resultMsg);
			return false;
		}
	}