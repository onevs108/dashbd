
function moveActiveServer(targetServer, selfServer){
	moveActiveMsg(targetServer, selfServer);
	return;
	if(confirm("APP #"+targetServer + " 서버로 변경하시겠습니까?")){
		var param = {
				serverTarget : "moveServer",
				serverType : targetServer
			};
		$.ajax({
			type : "POST",
			url : "/dashbd/resources/systemConfControll.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				if(data.resultInfo.sessionCnt == "-1"){
					alert(data.resultInfo.resultMsg);
				}else{
					alert("It has been successfully processed.");
					moveActiveMsg(targetServer, selfServer)
				}
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	}
}

function moveActiveMsg(targetServer, selfServer){
	
	$("#viewApp" + targetServer).show();
	$("#viewApp" + selfServer).hide();
	$("#appView" + targetServer).css("background-color", "00EAFF");
	$("#appView" + selfServer).css("background-color", "");

	$("#tomCheck" + targetServer).prop("checked",true);
	$("#dbCheck" + targetServer).prop("checked",true);
	$("#tomCheck" + selfServer).prop("checked",false);
	$("#dbCheck" + selfServer).prop("checked",false);

	$('#tomCheck' + targetServer).removeAttr("disabled");
	$('#dbCheck' + targetServer).removeAttr("disabled");
	$('#tomCheck' + selfServer).attr("disabled", true);
	$('#dbCheck' + selfServer).attr("disabled", true);
	
}
function checkTomcat(serverType){
	if($("#tomCheck" + serverType).is(':checked')){
		if(confirm("Are you sure you want to start the Tomcat Server?")){
			var param = {
					serverTarget : "TomcatStart",
					serverType : serverType
				};
			$.ajax({
				type : "POST",
				url : "/dashbd/resources/systemConfControll.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					if(data.resultInfo.sessionCnt == "-1"){
						alert(data.resultInfo.resultMsg);
						$("#tomCheck" + serverType).prop("checked",true);
					}else{
						alert("It has been successfully processed.");
					}
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		}
	}else{
		if(confirm("Are you sure you want to stop the Tomcat Server?")){
			var param = {
					serverTarget : "TomcatStop",
					serverType : serverType
				};
			$.ajax({
				type : "POST",
				url : "/dashbd/resources/systemConfControll.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					if(data.resultInfo.sessionCnt == "-1"){
						alert(data.resultInfo.resultMsg);
						$("#tomCheck" + serverType).prop("checked",true);
					}else{
						alert("It has been successfully processed.");
					}
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		}
	}
}

function checkDatabase(serverType){
	if($("#dbCheck" + serverType).is(':checked')){
		if(confirm("Are you sure you want to start the Database Server?")){
			var param = {
					serverTarget : "DataBaseStart",
					serverType : serverType
				};
			$.ajax({
				type : "POST",
				url : "/dashbd/resources/systemConfControll.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					if(data.resultInfo.sessionCnt == "-1"){
						alert(data.resultInfo.resultMsg);
						$("#dbCheck" + serverType).prop("checked",true);
					}else{
						alert("It has been successfully processed.");
					}
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		}
	}else{
		if(confirm("Are you sure you want to stop the Database Server?")){
			var param = {
					serverTarget : "DataBaseStop",
					serverType : serverType
				};
			$.ajax({
				type : "POST",
				url : "/dashbd/resources/systemConfControll.do",
				data : param,
				dataType : "json",
				success : function( data ) {
					if(data.resultInfo.sessionCnt == "-1"){
						alert(data.resultInfo.resultMsg);
						$("#dbCheck" + serverType).prop("checked",true);
					}else{
						alert("It has been successfully processed.");
					}
				},
				error : function(request, status, error) {
					alert("request=" +request +",status=" + status + ",error=" + error);
				}
			});
		}
	}
}
