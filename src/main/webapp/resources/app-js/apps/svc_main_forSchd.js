
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

$(document).ready(function()
{
	
	//getServiceAreaBmSc(1, $('#operator option:selected').val());
    $('#operator').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
		$("#viewProgram").hide();
		drawServiceAreaByBmSc($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
		//inbo add START
		callTimetable($('#bmsc option:selected').val(), g_ServiceAreaId);
		//inbo add END
    });
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
            $("#ajax").remove();
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
