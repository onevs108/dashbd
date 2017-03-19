$(document).ready(function() {
	getMenuList('LOG_MGMT');
	
	$('#data_1.input-group.date').datepicker({
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        calendarWeeks: true,
        autoclose: true
    });
	$('#data_2.input-group.date').datepicker({
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        calendarWeeks: true,
        autoclose: true
    });
	$("#operatorLog").show();
	$("div[name='scheduleLog']").hide();
	$("#others").hide();
});

function tabChange(tabDiv) {
	$(".nav.nav-tabs li").removeClass("active");
	$(".nav.nav-tabs li[name=tab" + tabDiv + "]").addClass("active");
	selectLogData();
	if(tabDiv == 1){
		$("#operatorLog").show();
		$("div[name='scheduleLog']").hide();
		$("#others").hide();
	} else if(tabDiv == 2) {
		$("#operatorLog").hide();
		$("div[name='scheduleLog']").hide();
		$("#others").show();
	} else if(tabDiv == 3) {
		$("#operatorLog").hide();
		$("div[name='scheduleLog']").show();
		$("#others").hide();
	} else if(tabDiv == 4) {
		$("#operatorLog").hide();
		$("div[name='scheduleLog']").hide();
		$("#others").show();
	}
}

function selectLogData() {
	$("#tabDiv").val($(".nav.nav-tabs li.active").attr("name").replace("tab", ""));
	
	$.ajax({
	    url : "/dashbd/api/selectLogDate.do",
	    type: "POST",
	    data : $("#logForm").serialize(),
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        $("#tab-body ul li").remove();
	        
	        if(data.resultList.length > 0) {
	        	for(var i=0; i < data.resultList.length; i++) {
		        	var result = data.resultList[i];
		        	$("#tab-body ul").append("<li>" + result.reqMsg + "</li>");
		        }
	        } else {
	        	$("#tab-body ul").append("<li>No Data</li>");
	        }
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
}