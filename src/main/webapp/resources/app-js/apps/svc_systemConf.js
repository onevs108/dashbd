$(document).ready(function(){
	$("#appView1").css("background-color", "00EAFF");
});

function editTomcat(gubun){
	alert(gubun + " 서버 정보 변경");
}

function editDbStatus(gubun){
	alert(gubun + " DB 서버 정보 변경");
}

function moveActiveServer(targetServer, selfServer){
	if(confirm("APP #"+targetServer + " 서버로 변경하시겠습니까?")){
		
		$("#viewApp"+targetServer).show();
		$("#viewApp"+selfServer).hide();

		
		$("#appView"+targetServer).css("background-color", "00EAFF");
		$("#appView"+selfServer).css("background-color", "");
		
		$('input:radio[name=tomRadio][value='+targetServer+']').click();
		$('input:radio[name=dbRadio][value='+targetServer+']').click();
	}
}
