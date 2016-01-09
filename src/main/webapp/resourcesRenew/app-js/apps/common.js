	function outMsgForAjax(result){
		var resultCode = result.resultInfo.resultCode
		var resultMsg = result.resultInfo.resultMsg
		var bRet = 1;
		if (resultCode == 1000) {
			alert(resultMsg);
		} else {
			alert("errorcode="+resultCode+",\n msg="+resultMsg);
			bRet = 0;
		}
		
		return bRet;
	}