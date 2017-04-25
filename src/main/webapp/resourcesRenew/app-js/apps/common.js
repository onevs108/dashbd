	function outMsgForAjax(result){
		var resultCode = result.resultInfo.resultCode
		var resultMsg = result.resultInfo.resultMsg
		var bRet = 1;
		if (resultCode == 1000 || resultCode == 200) {
			alert(resultMsg);
		} else {
			alert("errorcode="+resultCode+",\n msg="+resultMsg);
			bRet = 0;
		}
		
		return bRet;
	}
	
	function getTimeStamp() {
		var d = new Date();
		var s =
		    leadingZeros(d.getFullYear(), 4) + '-' +
		    leadingZeros(d.getMonth() + 1, 2) + '-' +
		    leadingZeros(d.getDate(), 2) + ' ' +
		
		    leadingZeros(d.getHours(), 2) + ':' +
		    leadingZeros(d.getMinutes(), 2) + ':' +
		    leadingZeros(d.getSeconds(), 2);
		return s;
	}

	function leadingZeros(n, digits) {
		var zero = '';
		n = n.toString();
		
		if (n.length < digits) {
		  for (i = 0; i < digits - n.length; i++)
		    zero += '0';
		}
		return zero + n;
	}