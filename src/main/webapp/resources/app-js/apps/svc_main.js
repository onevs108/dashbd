var g_bmscId = null;

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
	getServiceAreaByBmScCity(1,$('#bmsc option:selected').val(), '');
    $('#operator').change(function(){
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
    	getServiceAreaByBmScCity(1,$('#bmsc option:selected').val(), '');
    	refreshEmbms();
    	callScheduleTable($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
    });
    
    callScheduleTable($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
    refreshEmbms();	
    
    $('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
});


function callScheduleTable(bmscId, bmscName){

	$.ajax({
		url : "/dashbd/api/scheduleSummaryByBmsc.do",
		type: "get",
		data : { "bmscId" : bmscId, "activeContent" : "1" },
		success : function(responseData){
			$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += "<div class=\"file-box\">";
				content += "<div class=\"file\">";
				content += "<span class=\"corner\"></span>";
				//content += "<div class=\"image\">";
				//content += "<img alt=\"image\" class=\"img-responsive\" src=\""+ datas[i].thumbnail + "\">";
				//content += '</div>';
				//content += "<div class=\"progress progress-mini\">";
				//content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<iframe src=\"" + datas[i].url +"\" width='90%' height='120px' frameborder='0' allowfullscreen></iframe>"
				content += "<small>[" + datas[i].category + "]</small> ";
				content += datas[i].scheduleName;
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> " + datas[i].serviceType + "</h5>";
				content += "<small>Remaining: " + datas[i].leftTime + "</small> ";
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}
			
			if(datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No Service is available<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}

			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append('(' + bmscName + ')');
            
			$("#schedule_summary").empty();
            $("#schedule_summary").append(content);
		}
	});
	
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByBmsc.do",
		type: "get",
		data : { "bmscId" : bmscId, "activeContent" : "0" },
		success : function(responseData){
			$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += "<div class=\"file-box\">";
				content += "<div class=\"file\">";
				content += "<span class=\"corner\"></span>";
				//content += "<div class=\"image\">";
				//content += "<img alt=\"image\" class=\"img-responsive\" src=\""+ datas[i].thumbnail + "\">";
				//content += '</div>';
				//content += "<div class=\"progress progress-mini\">";
				//content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				content += "</div>";
				content += "<iframe src=\"" + datas[i].url +"\" width='90%' height='120px' frameborder='0' allowfullscreen></iframe>"
				content += "<small>[" + datas[i].category + "]</small> ";
				content += datas[i].scheduleName;
				content += "<div class=\"file-name\">";
				content += "<h5 class=\"text-navy\"><i class=\"fa fa-desktop\"></i> " + datas[i].serviceType + "</h5>";
				content += "<small>Remaining: " + datas[i].leftTime + "</small> ";
				content += "</div>";
				content += "</div>";
				content += "</div>";
			}
			
			if(datas.length == 0) {
				content += "<div class=\"nothumbnail\">";
				content += "<p>";
				content += "<i class=\"fa fa-search\"></i> No Service is available<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}

			
			$("#schedule_waiting_summary").empty();
            $("#schedule_waiting_summary").append(content);
		}
	});
}
function callTimetable(bmscId, serviceAreaId_val){
	var param = {
			bmscId : bmscId
			, serviceAreaId : serviceAreaId_val
		};
		
		$.ajax({
			type : "POST",
			url : "/dashbd/views/schd/getScheduleForMain.do",
			data : param,
			dataType : "json",
			success : function( data ) {
				setContents(data);
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
}
function setContents(data){
	var contents = data.contents;
	
	for ( var i=0; i < contents.length; i++) {
		contents[i].NAME;
	}
}

function openModal(bmscId) {
	g_bmscId = bmscId;
	console.log('modeal load..');
	$('#form-operator-id').val($('#search-operator-id').val());
	$('#modal-title').html('Add Server');
	$('#form-modal').modal('show');
}



function doAdd() {
	
	var serverName = $('#frm_md_serer_name').val();
	var protocol = $('#frm_md_protocol').val();
	var IPAddress = $('#frm_md_IPAddress').val();
	var loginId = $('#frm_md_loginId').val();
	var password = $('#frm_md_password').val();
	var command = $('#frm_md_command').val();
	
	if (serverName == null || serverName.length == 0) {
		alert('Please input the serverName');
		return false;
	}
	
	if (protocol == null || protocol.length == 0) {
		alert('Please input protocol');
		return false;
	}
	if (IPAddress == null || IPAddress.length == 0) {
		alert('Please input IPAddress');
		return false;
	}
	
	
	if (loginId == null || loginId.length == 0) {
		alert('Please input the loginId');
		return false;
	}
	
	if (password == null || password.length == 0) {
		alert('Please input the password');
		return false;
	}
	
	if (command == null || command.length == 0) {
		alert('Please input the command');
		return false;
	}
	
	var data = {
		bmscId: g_bmscId, // Edit도 doAdd() 함수를 타기 때문에 글로벌 변수에 null을 세팅해 null을 준다.
		serverName: serverName,
		protocol: protocol,
		IPAddress: IPAddress,
		loginId: loginId,
		password: password,
		command: command
	}
	
	insertEmbms(data);
}

function insertEmbms(data) {
	$.ajax({
		url: '/dashbd/api/bmsc/embmsInsert.do',
		method: 'POST',
		dataType: 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				//$('#table').bootstrapTable('destroy');
				refreshEmbms();	//함수만들어야 함.
				closeModal();
				
			}
			else { // 실패
				alert('Failed!! Please you report to admin!');
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown + textStatus);
			return false;
		}
	});
}

function delEmbms(embmsId) {
	if (!confirm('Do you really want to delete the embms?')) {
		return;
	}
	var param = {
			embmsId : embmsId
		};
	$.ajax({
		url: '/dashbd/api/bmsc/embmsDel.do',
		method: 'POST',
		dataType: 'json',
		data: param,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				refreshEmbms();	//함수만들어야 함.
			}
			else { // 실패
				alert('Failed!! Please you report to admin!');
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown + textStatus);
			return false;
		}
	});
}

function doModalCancel() {
	g_bmscId = null;
	closeModal();
}

function refreshEmbms(){
	var size = $('#bmsc option').size();
	$("#embmsList").empty();
	
	for (var i = 0 ; i< size; i++){
		var bmscId = $('#bmsc option:eq(' + i + ')').val();
		var bmscName = $('#bmsc option:eq(' + i + ')').text();
		
		console.log('bmscId=' + bmscId + ', bmscName=' + bmscName);
		
		var param = {
				bmscId :  bmscId
			};
		$.ajax({
			type : "POST",
			url : "/dashbd/api/bmsc/embmsList.do",
			data : param,
			async : false,
			dataType : "json",
			success : function( data ) {
				embmsList(data.contents, bmscId, bmscName);
			},
			error : function(request, status, error) {
				alert("request=" +request +",status=" + status + ",error=" + error);
			}
		});
	}
}

function embmsList(data, bmscId, bmscName){

	var $list = $("#embmsList");
	
	var $tr = $("<tr/>");
	
	var $td_header = $("<td width='20%'/>");
	var $btn_header = $("<button/>");
	var $h4_header = $("<h4/>");
	var $i_header = $("<i/>");
	
	var $btn_plus = $("<button/>");
	var $i_plus= $("<i/>");
	
	$td_header.attr("style", "text-align:center;")
	$btn_header.attr("class","btn btn-sm btn-default");
	$btn_header.attr("type","button");
	$i_header.attr("class","fa fa-desktop");
	$h4_header.attr("class","text-center");
	$h4_header.html(bmscName );
	
	$btn_plus.attr("class","btn btn-primary btn-sm");
	$btn_plus.attr("type","button");
	$btn_plus.attr("onclick","openModal(" + bmscId + ")");
	$i_plus.attr("class","fa fa-plus");
	$btn_plus.append($i_plus);
	
	$btn_header.append($i_header);
	$td_header.append($btn_header);
	$td_header.append($h4_header);
	$td_header.append($btn_plus);
	
	$tr.append( $td_header );
	
		
	var tmpSession ="";
	var diffSession = false;
	for ( var i=0; i<data.length; i++) {
		var $td = $("<td width='10%'/>");
		var $btn = $("<button/>");
		var $h4 = $("<h4/>");
		var $i = $("<i/>");
		var $img = $("<img/>");
		
		var embmsId = data[i].id;
		var serverName = data[i].serverName;
		var session = data[i].session;
		
		if (i > 0){
			if (session != tmpSession || session == '-1')
				diffSession = true;
		}
		tmpSession = session;
		
		$btn.attr("class","btn btn-sm btn-default");
		$btn.attr("type","button");
		$btn.attr("onclick","delEmbms(" + embmsId +")");
		$i.attr("class","fa fa-close");
		$img.attr("src","img/server_network.png");
		$img.attr("width","80%");
		$h4.attr("class","text-center");
		$h4.html(serverName + '(' + session + ')');
		
		$td.attr("style", "text-align:right;")
		
		$btn.append($i);
		$td.append($btn);
		$td.append($img);
		$td.append($h4);
		$tr.append( $td );
	}
	
	var $td_desc = $("<td/>");
	
	if (diffSession){
		$td_desc.html("Error Different Session[" + tmpSession +"]")
	}else{
		$td_desc.html("eMBMS Session[" + tmpSession +"]")
	}
	
	$tr.append( $td_desc );
	$list.append( $tr );


}

function closeModal() {
	$('#frm_md_serer_name').val('');
	$('#frm_md_protocol').val('');
	$('#frm_md_loginId').val('');
	$('#frm_md_password').val('');
	$('#frm_md_command').val('');
	$('#form-modal').modal('hide');
}

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
