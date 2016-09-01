var g_bmscId = null;

var default_service_area = "<div class=\"ibox-title\"><h5>Service Area  </h5></div>";
default_service_area += "<div class=\"ibox-content\">";
default_service_area += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
default_service_area += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID</th><th class=\"footable-sortable\">SA_NAME</th></tr></thead>";
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
	getServiceAreaByBmScCity(1,$('#bmsc option:selected').val(), '', '');
    $('#operator').change(function(){
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc').change(function(){
    	$("#divBandWidth").hide();
    	getServiceAreaByBmScCity(1,$('#bmsc option:selected').val(), '', '');
    	refreshEmbms();
    	callScheduleTable($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
    });
    
    callScheduleTable($('#bmsc option:selected').val(), $('#bmsc option:selected').text());
    refreshEmbms();	
    
    $('#modal-add-btn').click(doAdd);
	$('#modal-cancel-btn').click(doModalCancel);
	$('#modal-cancel-icon-btn').click(doModalCancel);
	$('#modal-edit-cancel-btn').click(doModalEditCancel);
	$('#modal-edit-cancel-icon-btn').click(doModalEditCancel);
    $('#modal-edit-btn').click(doEdit);
});

function moveToEnb2(bmscId, serviceAreaId)
{
	$("#schedule_summary").empty();
	setActiveContents(bmscId, serviceAreaId);
	console.log("moveToEnb 1");
	
	$("#schedule_waiting_summary").empty();
	console.log("moveToEnb 2");
	setWaitContents(bmscId, serviceAreaId);
	
	console.log("moveToEnb 3");
	$("#divBandWidth").show();
	$("#bandwidth").empty();
	bandWidth(bmscId, serviceAreaId);
}
function playVideo(id, url){
	console.log('id=',id, ',url=',url);
    var player = dashjs.MediaPlayer().create();
    player.initialize(document.querySelector("#video_" + id), url, false);
}
function setActiveContents(bmscId, serviceAreaId){
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByServiceArea.do",
		type: "get",
		async:false,
		data : { "bmscId" : bmscId, "activeContent":"1" , "serviceAreaId" : serviceAreaId},
		success : function(responseData){
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
				//content += "<iframe src=\"" + datas[i].url +"\" width='90%' height='120px' frameborder='0' allowfullscreen></iframe>"
				//content += "<div><video data-dashjs-player autoplay src=\"" + datas[i].url +"\" controls></video></div>"
				//content += "<div><video data-dashjs-player autoplay src=\"" + datas[i].url +"\" controls></video></div>"
				content += "<div><video id=\"video_" + i +"\" controls></video></div>"
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
				content += "<i class=\"fa fa-search\"></i> No Content is Acailable<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}
			
            $("#schedule_summary").append(content);
            
        	for (var i = 0; i < datas.length; i++) {
        		playVideo(i, datas[i].url);
        	}
            
            
		}
	});
}
function bandWidth(bmscId, serviceAreaId){
	
	console.log('here0');
	
	$.ajax({
		url : "/dashbd/api/bandwidthByServiceArea.do",
		type: "get",
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId},
		success : function(responseData){
			console.log('here1');
			var bandwidth_data = JSON.parse(responseData);
			console.log('here2');		
			//var content = "<h2>" + bandwidth_data.GBRSum + " % is being used</h2>";
			//content += "<div class=\"progress progress-big\">";
			//content += "<div style=\"width:" + bandwidth_data.GBRSum + "%;\" class=\"progress-bar\"></div>";
			//content += "</div>";
			//console.log('here3');	

			c3.generate({
                bindto: '#bandwidth',
                data:{
                    columns: [
                        ['data', bandwidth_data.GBRSum]
                    ],

                    type: 'gauge'
                },
                color:{
                    pattern: ['#1ab394', '#BABABA']

                }
            });
            console.log('here4');	
		},
		error : function(request, status, error) {
			//alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
	
}

function setWaitContents(bmscId, serviceAreaId){
	console.log("setWaitContents 1");
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByServiceArea.do",
		type: "get",
		async:false,
		data : { "bmscId" : bmscId, "serviceAreaId" : serviceAreaId , "activeContent":"0"},
		success : function(responseData){
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
				//content += "<iframe src=\"" + datas[i].url +"\" width='90%' height='120px' frameborder='0' allowfullscreen></iframe>"
				content += "<div><video id=\"video_w" + i +"\" controls></video></div>"
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
				content += "<i class=\"fa fa-search\"></i> No Content is Acailable<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}
            $("#schedule_waiting_summary").append(content);
            
        	for (var i = 0; i < datas.length; i++) {
        		playVideo('w' + i, datas[i].url);
        	}
            
		}
	});
	
	console.log("setWaitContents 2");
}


function getServiceAreaByBmScCity(page, bmscId, city, toSearchTxt)
{
	var selectedCity = encodeURIComponent(city);

	$.ajax({
        url : "/dashbd/api/serviceAreaByBmScCity.do",
        type: "get",
        data : { "page" : page, "bmscId" : bmscId, "city" : selectedCity, "toSearchTxt" : encodeURIComponent(toSearchTxt) },
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success : function(responseData){
            //$("#ajax").remove();
            datas = JSON.parse(responseData);
            var dataLen = datas.length;
            var options = "";
            var idx = 0;
            //options += "<div class=\"ibox-title\"><h5>Service Area for " + city + "</h5></div>";
            
            //메인페이지 Service Areas for BM-SC 영역
			options += "<div class=\"ibox-content\">";
			options += "<div class=\"input-group\"><input type=\"text\" class=\"form-control\" id=\"toSearchTxt\" name=\"toSearchTxt\" value=\""+toSearchTxt+"\" placeholder=\"SA_ID or SA_NAME\" />";
			options += "<span class=\"input-group-btn\">";
			options += '<button type="button" class="btn btn-primary" onclick="javascript:searchToServiceArea(\'' + bmscId + '\', \'' + city + '\');" id="toSearchBtn">Search</button>';
			options += "</span>";
			options += "</div>";
			options += "</div>";
			options += "<div class=\"ibox-content\">";
			options += "<table class=\"footable table table-stripped toggle-arrow-tiny\" data-page-size=\"10\">";
			options += "<thead><tr><th class=\"footable-sortable footable-sorted\">SA_ID<span class='footable-sort-indicator'></span></th><th class=\"footable-sortable\">SA_NAME<span class='footable-sort-indicator'></span></th></tr></thead>";
			options += "<tbody>";

            for(var i = 0; i < dataLen; i++ ) {
            	//options += '<li><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');">' + datas[i].serviceAreaId + '</a></li>';
            	//options += '<tbody><tr><td>' + datas[i].serviceAreaId + '</td><td>' + datas[i].serviceAreaName + '</td></tr></tbody>';
            	//options += '<ul class="service_area_box list-inline"><a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ');"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></a></ul>';
            	//options += '<a href="javascript:moveToEnb(' + datas[i].bmscId + ', ' + datas[i].serviceAreaId + ', ' + datas[i].serviceAreaName + ');"><ul class="service_area_box list-inline"><li>' + datas[i].serviceAreaId + '</li><li>' + datas[i].serviceAreaName + '</li></ul></a>';
            	
            	if( i % 2 == 0 ) {
            		options += "<tr class=\"footable-even\" style=\"display: table-row;\"><td>";
            	} else {
            		options += "<tr class=\"footable-odd\" style=\"display: table-row;\"><td>";
            	}
            	options += "<span class=\"footable-toggle\"></span>";
            	options += "<a href=\"javascript:moveToEnb2(" + datas[i].bmscId + ", " + datas[i].serviceAreaId + ");\">";
            	options += datas[i].serviceAreaId;
            	options += " (" + datas[i].totalCount + ")";
            	options += "</a>";
				options += "</td><td>";
				options += datas[i].serviceAreaName;
				options += "</td></tr>";
				
            }

            options += "</tbody>";
            
            if(dataLen > 10) {
            	//alert(dataLen);
            	options += "<tfoot><tr><td colspan=\"2\" style=\"padding-top: 20px;\"><ul class=\"pagination pull-right\"></ul></td></tr></tfoot>";
            }
            
            options += "</table></div>";
        
            //alert(options);
            $("#service_area").empty();
            $("#service_area").append(options);
            
            $('.footable').footable();
            //$('.footable2').footable();
            
            /*
            // Pagination
            var totalCount = datas[0].totalCount;
            if(totalCount > perPage) {
            	var totalPageCount = Math.ceil(totalCount / perPage); // 마지막 페이지
            	//alert(totalPageCount);
            	
            	var pageination = '';
                pageination += '<div class="text-center">';
                pageination += '<ul class="pagination">';
                if( page == 1 )
                {
                	pageination += '<li class="disabled"><a href="javascript:getServiceAreaByBmScCity(' + (page-1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + (page-1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
                }
                
                if(totalPageCount > listPageCount) {
                	for(var i = page, j = 0; i <= totalPageCount && j < listPageCount ; i++, j++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + i + ',' + bmscId + ', \'' + city + '\');">' + i + '</a></li>';
                    	}
                    }
                }
                else {
                	for(var i = 1; i <= totalPageCount && i <= listPageCount ; i++) {
                    	if( i == page ) {
                    		pageination += '<li class="active"><a href="#">' + i + '</a></li>';
                    	}
                    	else {
                    		pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + i + ',' + bmscId + ', \'' + city + '\');">' + i + '</a></li>';
                    	}
                    }
                }
                
                
                if( page == totalPageCount ) {
                	pageination += '<li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
                else {
                	pageination += '<li><a href="javascript:getServiceAreaByBmScCity(' + (page+1) + ',' + bmscId + ', \'' + city + '\');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
                }
    			pageination += '</ul>';
    			pageination += '</div>';
    			
    			//$("#service_area").append(pageination);
            }
            */
        }
    });
}

function searchToServiceArea(bmscId, city){
	getServiceAreaByBmScCity("1", bmscId, city, $("#toSearchTxt").val());
}

function callScheduleTable(bmscId, bmscName){
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByBmsc.do",
		type: "get",
		data : { "bmscId" : bmscId, "activeContent" : "1" },
		success : function(responseData){
			//$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += "<div class=\"file-box\">";
				//김범길 제거 임시 : content += "<div class=\"file\">";
				//김범길 제거 임시 : content += "<span class=\"corner\"></span>";
				//content += "<div class=\"image\">";
				//content += "<img alt=\"image\" class=\"img-responsive\" src=\""+ datas[i].thumbnail + "\">";
				//content += '</div>';
				//content += "<div class=\"progress progress-mini\">";
				//content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				//김범길 제거 임시 : content += "</div>";
				//content += "<iframe src=\"" + datas[i].url +"\" width='90%' height='120px' frameborder='0' allowfullscreen></iframe>"
				//김범길 제거 하단 추가함 임시 : content += "<div><video id=\"video_" + i +"\" controls></video></div>"
				content += "<div><video id=\"video_" + i +"\" controls><source src=\"/dashbd" + datas[i].url + "\" type=\"video/mp4\" /></video></div>"
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
				content += "<i class=\"fa fa-search\"></i> No Content is Acailable<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}

			$("#schedule_summary_service_area_id").empty();
            $("#schedule_summary_service_area_id").append('(' + bmscName + ')');
            
			$("#schedule_summary").empty();
            $("#schedule_summary").append(content);
            
           //김범길 제거 하단 임시 : 
//        	for (var i = 0; i < datas.length; i++) {
//        		playVideo(i, datas[i].url);
//        	}
		}
	});
	
	$.ajax({
		url : "/dashbd/api/scheduleSummaryByBmsc.do",
		type: "get",
		data : { "bmscId" : bmscId, "activeContent" : "0" },
		success : function(responseData){
			//$("#ajax").remove();
			datas = JSON.parse(responseData);
			var dataLen = datas.length;
			var options = "";
			var content = "";
			for (var i = 0; i < datas.length; i++) {
				content += "<div class=\"file-box\">";
				//김범길 제거 임시 : content += "<div class=\"file\">";
				//김범길 제거 임시 : content += "<span class=\"corner\"></span>";
				//content += "<div class=\"image\">";
				//content += "<img alt=\"image\" class=\"img-responsive\" src=\""+ datas[i].thumbnail + "\">";
				//content += '</div>';
				//content += "<div class=\"progress progress-mini\">";
				//content += "<div style=\"width: " + datas[i].progressRate + "%;\" class=\"progress-bar\"></div>";
				//김범길 제거 임시 : content += "</div>";
				//김범길 제거 임시  하단 추가: content += "<div><video id=\"video_w" + i +"\" controls></video></div>"
				content += "<div><video id=\"video_w" + i +"\" controls><source src=\"/dashbd" + datas[i].url + "\" type=\"video/mp4\" /></video></div>"
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
				content += "<i class=\"fa fa-search\"></i> No Content is Acailable<br/>";
				content += "</p>";
				content += "<small></small>";
				content += "</div>";
			}

			
			$("#schedule_waiting_summary").empty();
            $("#schedule_waiting_summary").append(content);
            
            //김범길 제거 임시 : 
//          for (var i = 0; i < datas.length; i++) {
//        		playVideo('w'+i, datas[i].url);
//        	}
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
				//alert("request=" +request +",status=" + status + ",error=" + error);
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
	//console.log('modeal load..');
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



function doEdit() {

	var embmsId = $('#embmsId').val();
	var serverName = $('#frm_md_edit_serer_name').val();
	var protocol = $('#frm_md_edit_protocol').val();
	var IPAddress = $('#frm_md_edit_IPAddress').val();
	var loginId = $('#frm_md_edit_loginId').val();
	var password = $('#frm_md_edit_password').val();
	var command = $('#frm_md_edit_command').val();
	
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
		id: embmsId,
		serverName: serverName,
		IPAddress: IPAddress,
		protocol: protocol,
		loginId: loginId,
		password: password,
		command: command
	}
	
	updateEmbms(data);
}

function updateEmbms(data) {
	$.ajax({
		url: '/dashbd/api/bmsc/embmsUpdate.do',
		method: 'POST',
		dataType: 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				alert('SUCCESS !.');
				refreshEmbms();	//함수만들어야 함.
				closeEditModal();
				
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

function insertEmbms(data) {
	$.ajax({
		url: '/dashbd/api/bmsc/embmsInsert.do',
		method: 'POST',
		dataType: 'json',
		data: data,
		success: function(data, textStatus, jqXHR) {
			if (data.result) { // 성공
				alert('SUCCESS !.');
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
				alert('SUCCESS !.');
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

function editEmbms(embmsId){
	var param = {
			embmsId :  embmsId
		};
	$.ajax({
		type : "POST",
		url : "/dashbd/embms/selectEmbmsView.do",
		data : param,
		async : false,
		dataType : "json",
		success : function( data ) {
			$('#embmsId').val(data.contents[0].id);
			$('#frm_md_edit_serer_name').val(data.contents[0].serverName);
			$('#frm_md_edit_IPAddress').val(data.contents[0].IPAddress);
			$('#frm_md_edit_protocol').val(data.contents[0].protocol);
			$('#frm_md_edit_loginId').val(data.contents[0].loginId);
			$('#frm_md_edit_password').val(data.contents[0].password);
			$('#frm_md_edit_command').val(data.contents[0].command);
			$('#modal-edit-title').html('Edit Server');
			$('#form-edit-modal').modal('show');
		},
		error : function(request, status, error) {
			//alert("request=" +request +",status=" + status + ",error=" + error);
		}
	});
}
function doModalCancel() {
	g_bmscId = null;
	closeModal();
}

function doModalEditCancel() {
	g_bmscId = null;
	closeEditModal();
}

function refreshEmbms(){
	var size = $('#bmsc option').size();
	$("#embmsList").empty();
	
	var bmscId = $('#bmsc').val();
	var bmscName = $("#bmsc option:selected").text();
	var param = {
			bmscId :  bmscId
		};
	$.ajax({
		type : "POST",
		url : "/dashbd/resources/mainembmsList.do",
		data : param,
		async : false,
		dataType : "json",
		success : function( data ) {
			if(data.permissionembs == "OK"){
				embmsList(data.contents, bmscId, bmscName);
			}else{
				embmsListView(data.contents, bmscId, bmscName);
			}
		},
		error : function(request, status, error) {
			embmsListView("", bmscId, bmscName);
		}
	});
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
	for ( var i=0; i < data.length; i++) {
		var $td = $("<td width='10%'/>");
		var $btn = $("<button/>");
		var $h4 = $("<h4/>");
		var $i = $("<i/>");
		var $img = $("<img/>");
		var $btnEdit = $("<button/>");
		var $iEdit = $("<i/>");
		
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

		$btnEdit.attr("class","btn btn-sm button-edit");
		$btnEdit.attr("type","button");
		$btnEdit.attr("onclick","editEmbms(" + embmsId +")");
		$iEdit.attr("class","fa fa-cog");
		
		$img.attr("src","img/server_network.png");
		$img.attr("width","50px");
		$h4.attr("class","text-center");
		$h4.attr("style","height:20px;");
		$h4.html(serverName + '(' + session + ')');
		
		$td.attr("style", "text-align:center;")
		
		$btn.append($i);
		$td.append($btn);
		$btnEdit.append($iEdit);
		$td.append($btnEdit);
		$td.append($("<br/>"));
		$td.append("<i class='fa fa-desktop' style='font-size: 4rem;' />");
		//$td.append($img);
		$td.append($h4);
		$tr.append( $td );
	}
	
	var $td_desc = $("<td/>");
	
	if (diffSession){
		$td_desc.html("<font style='font-size: 25px;color: red;'><b>[Error] [" + tmpSession +"]</b></font>")
	}else{
		$td_desc.html("eMBMS Session[" + tmpSession +"]")
	}
	
	$tr.append( $td_desc );
	$list.append( $tr );
}


function embmsListView(data, bmscId, bmscName){

	var $list = $("#embmsList");
	
	var $tr = $("<tr/>");
	
	var $td_header = $("<td width='20%'/>");
	var $h4_header = $("<h4/>");
	var $i_header = $("<i/>");
	
	var $i_plus= $("<i/>");
	
	$td_header.attr("style", "text-align:center;")
	$h4_header.attr("class","text-center");
	$h4_header.html(bmscName );
	
	$td_header.append($h4_header);
	
	$tr.append( $td_header );
		
	var tmpSession ="";
	var diffSession = false;
	for ( var i=0; i < data.length; i++) {
		var $td = $("<td width='10%'/>");
		var $h4 = $("<h4/>");
		var $i = $("<i/>");
		var $img = $("<img/>");
		var $iEdit = $("<i/>");
		
		var embmsId = data[i].id;
		var serverName = data[i].serverName;
		var session = data[i].session;
		
		if (i > 0){
			if (session != tmpSession || session == '-1')
				diffSession = true;
		}
		tmpSession = session;
		
		$img.attr("src","img/server_network.png");
		$img.attr("width","50px");
		$h4.attr("class","text-center");
		$h4.attr("style","height:20px;");
		$h4.html(serverName + '(' + session + ')');
		
		$td.attr("style", "text-align:center;")
		
		$td.append($("<br/>"));
		$td.append($img);
		$td.append($h4);
		$tr.append( $td );
	}
	
	var $td_desc = $("<td/>");
	
	if (diffSession){
		$td_desc.html("<font style='font-size: 25px;color: red;'><b>[Error] [" + tmpSession +"]</b></font>")
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

function closeEditModal() {
	$('#frm_md_edit_serer_name').val('');
	$('#frm_md_edit_protocol').val('');
	$('#frm_md_edit_loginId').val('');
	$('#frm_md_edit_password').val('');
	$('#frm_md_edit_command').val('');
	$('#form-edit-modal').modal('hide');
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
            //$("#ajax").remove();
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
            //$("#ajax").remove();
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