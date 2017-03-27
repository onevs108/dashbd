
var default_service_area = "<div class=\"ibox-title\"><h5>Service Area  </h5></div>";
default_service_area += "<div class=\"ibox-content\">";
default_service_area += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
default_service_area += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">Description</th></tr></thead>";
default_service_area += "<tbody>";
default_service_area += "<tr>";
default_service_area += "<td></td>";
default_service_area += "<td></td>";
default_service_area += "</tr>";
default_service_area += "</tbody>";
default_service_area += "<tfoot>";
default_service_area += "<tr>";
default_service_area += "<td colspan=\"2\">";
default_service_area += "</td>";
default_service_area += "</tr>";
default_service_area += "</tfoot>";
default_service_area += "</table>";
default_service_area += "</div>";

var glovalSaid = "";

$(document).ready(function()
{
	$('#scheduleSearch').click(function(){
		callTimetable(undefined, glovalSaid, $("input[name='radio']:checked").val());
	});
	//getServiceAreaBmSc(1, $('#operator option:selected').val());
    $('#operator').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
		$("#viewProgram").hide();
		drawServiceAreaByBmSc($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
		//inbo add START
    });
    
    $("#selectCircle").on("change", function(e) {
    	var url = "";
    	var type = $("input[name='radio']:checked").val();
    	if(type == "group"){
    		url = "getGroupListFromCircleId.do";
    	}else{
    		url = "/dashbd/hotspot/getCityListFromCircleId.do";
    	}
    	
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var circleId = array[0];
    	var circleName = array[1];
    	if(circleId == ""){
    		g_ServiceAreaId = "";
    		$("#selectCity").html("");
        	$("#selectHotspot").html("");
    		return;
    	}
    	$.ajax({
    		url : url,
    		type: "post",
    		data : { "circleId" : circleId },
    		success : function(data) {
    			var json = JSON.parse(data).result;
    			var html = "<option value=''>Select City</option>";
    			if(type == "group"){
    				html = "<option value=''>Select Group</option>";
    				for (var i = 0; i < json.length; i++) {
                		html += "<option value='"+json[i].group_id+"^"+json[i].group_name+"^"+json[i].circle_id+"'>"+json[i].group_name+"</option>";
    				}
    			}else{
    				for (var i = 0; i < json.length; i++) {
                		html += "<option value='"+json[i].city_id+"^"+json[i].city_name+"^"+json[i].latitude+"^"+json[i].longitude+"'>"+json[i].city_name+"</option>";
    				}
    			}
    			glovalSaid = circleId;
    			callTimetable(undefined, circleId, "area");
    			g_ServiceAreaId = circleId;
            	$("#selectCity").html(html);
            	$("#selectHotspot").html("");
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "Network Error"
    			});
    		}
    	});
    });
    
    $("#selectCity").on("change", function(e){
    	var url = "";
    	var type = $("input[name='radio']:checked").val();
    	if(type == "group"){
    		url = "getGroupSaidList.do";
    	}else{
    		url = "/dashbd/hotspot/getHotSpotListFromCityId.do";
    	}
    	
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var cityId = array[0];
    	var cityName = array[1];
    	if(cityId == ""){
    		g_ServiceAreaId = "";
    		g_ServiceGroupId = "";
    		$("#selectCircle").val("");
    		$("#selectHotspot").html("");
    		return;
    	}
    	$.ajax({
    		url : url,
    		type: "post",
    		data : { "cityId" : cityId },
    		success : function(data) {
    			var json = JSON.parse(data).result;
    			if(type == "group"){
//    				var said = ""
//    				for (var i = 0; i < json.length; i++) {
//    					if(i == json.length-1){
//    						said += json[i].sub_said;
//    					}else{
//    						said += json[i].sub_said + ","
//    					}
//					}
    				if(json.length == 0){
    					alert("Group's element is not exist !");
    					$("#selectCity").val("");
    					return;
    				}
    				glovalSaid = cityId;
    				callTimetable(undefined, cityId, "group");
    				g_ServiceGroupId = cityId;
    	    	} 
    			else 
    	    	{
        			var html = "<option value=''>Select Hotspot</option>";
        			for (var i = 0; i < json.length; i++) {
        				html += "<option value='"+json[i].hotspot_id+"^"+json[i].hotspot_name+"'>"+json[i].hotspot_name+"</option>";
        			}
        			glovalSaid = cityId;
        			callTimetable(undefined, cityId, "area");
        			g_ServiceAreaId = cityId;
        			$("#selectHotspot").html(html);
    	    	}
    		},
    		error : function(xhr, status, error){
    			swal({
    				title: "Fail !",
    				text: "Network Error"
    			});
    		}
    	});
    });
    
    $("#selectHotspot").on("change", function(e){
    	var array = e.target[e.target.selectedIndex].value.split("^");
    	var hotspotId = array[0];
    	var hotspotName = array[1];
    	if(hotspotId == ""){
    		g_ServiceAreaId = "";
    		g_ServiceGroupId = "";
    		$("#selectCircle").val("");
    		$("#selectCity").val("");
    		$("#selectHotspot").html("");
    		return;
    	}
    	glovalSaid = hotspotId;
		callTimetable(undefined, hotspotId, "area");
		g_ServiceAreaId = hotspotId;
    });
    
    $("input[name='radio']").click(function() {
    	var radioType = $("input[name='radio']:checked").val();
    	if(userGrade == 9999) {
    		$("#emergency").hide();
			$("#national").hide();
		}
    	if(radioType == "group")
    	{
    		$("#selectHotspot").hide();
    		$("#selectHotspotLabel").hide();
    		$("#selectCityLabel").html("Group");
    		$("#selectArea").show();
    		if(glovalSaid != ""){
    			$('#scheduleSearch').click();
    		}
    	}
    	else if(radioType == "area")
    	{
    		$("#selectHotspot").show();
    		$("#selectHotspotLabel").show();
    		$("#selectCityLabel").html("City");
    		$("#selectArea").show();
    	}
    	else
    	{
    		$("#selectArea").hide();
    		$('#scheduleSearch').click();
    	}
		
    	$("#selectCircle").val("");
		$("#selectCity").val("");
		$("#selectCity").html("");
    	$("#selectHotspot").html("");
    });
    
    if(userGrade == 9999) {
    	$($("input[name='radio']")[2]).click();
    	setTimeout(() => {
    		$("#selectHotspot").show();
    		$("#selectHotspotLabel").show();
    		$("#selectCityLabel").html("City");
    		$("#selectArea").show();
    		$("#selectCircle option:eq(1)").attr("selected", "selected");
    		$("#selectCircle").change();
    	}, 500);
    }else{
    	setTimeout(() => {
        	$($("input[name='radio']")[1]).click();
    	}, 500);
    }
});

var perPage = 15;
var listPageCount = 10;

// Service Area 조회 by bmsc id
function getServiceAreaByBmSc(page, bmscId)
{
	$.ajax({
        url : "/dashbd/api/serviceAreaByBmSc.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<li><a href="/dashbd/resources/serviceAreaMgmt.do?serviceAreaId=' + data[i].serviceAreaId + '">' + data[i].serviceAreaId + '</a></li>';
            }

            $("#service_area").empty();
            $("#service_area").append(options);
            
            // Pagination
            var totalCount = data[0].totalCount;
            if(totalCount > perPage) {
            	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
            	//alert(totalPageCount);
            	
            	var pageination = '';
                pageination += '<div class="text-center">';
                pageination += '<ul class="pagination">';
                if( page == 1 )
                {
                	pageination += '<li class="disabled"><a href="javascript:getServiceAreaByBmSc(' + (page-1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + (page-1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                
                if(totalPageCount > listPageCount) {
                	for(var i = page, j = 0; i <= totalPageCount && j < listPageCount ; i++, j++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + i + ',' + bmscId + ');">' + i + '</a></li>';
                    	}
                    }
                }
                else {
                	for(var i = 1; i <= totalPageCount && i <= listPageCount ; i++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + i + ',' + bmscId + ');">' + i + '</a></li>';
                    	}
                    }
                }
                
                if( page == totalPageCount ) {
                	pageination += '<li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmSc(' + (page+1) + ',' + bmscId + ');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
    			pageination += '</ul>';
    			pageination += '</div>';
    			
    			$("#service_area").append(pageination);
            }
            
        }
    });
}

// BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId)
{
	$.ajax({
        url : "/dashbd/api/serviceAreaBmScByOperator.do",
        type: "get",
        data : { "page" : page, "operatorId" : operatorId },
        //data : { "page" : page, "operatorId" : 1 },
        success : function(responseData){
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value="">Select BM-SC</option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $("#service_area").empty();
            $("#service_area").append(default_service_area);
            $("#bmsc").empty();
            $("#bmsc").append(options);
        }
    });
}
