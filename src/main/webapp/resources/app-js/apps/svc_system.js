
$(document).ready(function()
{    
    $('#operatorPermission').change(function() {
    	if($('#operatorPermission option:selected').val() == ""){
        	getPermissionList(null);
    	}else{
        	getPermissionList($('#operatorPermission option:selected').val());
    	}
    });
    
    $('#operatorContent').change(function() {
        getServiceAreaBmSc(1, $('#operatorContent option:selected').val(), true, "bmscContent");
    });
    
    $('#operatorEnbs').change(function() {
        getServiceAreaBmSc(1, $('#operatorEnbs option:selected').val(), true, "bmscEnbs");
    });
    
    $('#operatorServiceArea').change(function() {
        getServiceAreaBmSc(1, $('#operatorServiceArea option:selected').val(), true, "bmscServiceArea");
    });
    
    $('#bmscContent').change(function() {
        getServiceAreaByBmSc($('#bmscContent option:selected').val(), true);
    });
    
    $('#yearContent').change(function(){
    	$('#monthContent').val("");
    	$('#dayContent').html('<option value="">--ALL--</option>');
    });
    
    $('#monthContent').change(function(){
        updateNumberOfDays(); 
    	$('#dayContent').val("");
    });
    
    getPermissionList(null);
    
    $("#contentChart").height(50*6)
    new Chartist.Bar('#contentChart', {
		labels: ["Band of Brothers", "Batman Begins", "General Lee", "Batman Begins", "My Refriegrator", "Star Wars"],
		series: [
		         [210, 187, 150, 118, 98, 11]
		]
	},{
        seriesBarDistance: 10,
        reverseData: true,
        horizontalBars: true,
        axisY: {
            offset: 70
        }
    });
});

function updateNumberOfDays(){
    $('#dayContent').html('<option value="">--ALL--</option>');
    month = $('#monthContent').val();
    year = $('#yearContent').val();
    days = daysInMonth(month, year);

    for(i=1; i < days+1 ; i++){
            $('#dayContent').append($('<option />').val(i).html(i));
    }
}

//helper function
function daysInMonth(month, year) {
    return new Date(year, month, 0).getDate();
}

function getPermissionList(operatorId) {
	var labelsData = [];
	var seriesData = [];

	$('#permissionTable').bootstrapTable('destroy');
	// 테이블 생성
	var table = $('#permissionTable').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/getPermissionList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['operatorId'] = operatorId
			return params;
		},
		cache: false,
		pagination: true,
		sidePagination: 'server',
		pageNumber: 1,
		pageSize: 10,
		search: false,
		showHeader: true,
		showColumns: false,
		showRefresh: false,
		minimumCountColumns: 2,
		clickToSelect: false,
		columns: [{
			field: 'permissionId',
			title: 'PERMIOSSION ID',
			visible: false
		}, {
			field: 'permissionName',
			title: 'Permission',
			width: '50%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				labelsData.push(value);
				return value;
			}
		}, {
			field: 'permissionCount',
			title: 'of Users',
			width: '50%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				seriesData.push(value);
				return value;
			}
		}]
	});
	$('#permissionTable').on('load-success.bs.table', function (res) {
		$("#permissionChart").height(50*$('#permissionTable').bootstrapTable('getData').length)
		new Chartist.Bar('#permissionChart', {
			labels: labelsData,
			series: [
			         seriesData
			]
		},{
	        seriesBarDistance: 10,
	        reverseData: true,
	        horizontalBars: true,
	        axisY: {
	            offset: 70
	        }
	    });

		labelsData = [];
		seriesData = [];
	});
}

//BmSc 조회 by operator id
function getServiceAreaBmSc(page, operatorId, isUpload, bmscType)
{	
	$.ajax({
        url : "/dashbd/api/serviceAreaBmScByOperator.do",
        type: "get",
        data : { "page" : page, "operatorId" : operatorId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value="">--ALL--</option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }
            
            if( isUpload ) {
            	$("#"+bmscType).empty();
                $("#"+bmscType).append(options);
            }
        }
    });
}

function getServiceAreaByBmSc(bmscId, isUpload) {
	$.ajax({
        url : "/dashbd/api/getServiceAreaCountByBmSc.do",
        type: "get",
        data : { "bmscId" : bmscId },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = '<option value="">--ALL--</option>';
            for(var i=0; i<dataLen; i++){
            	options += '<option value="' + data[i].bmsc_id + '">' + data[i].city + '</option>';
            }
            
            if( isUpload ) {
            	$("#serviceContent").empty();
                $("#serviceContent").append(options);
            }
        }
    });
}

function isEmpty(value) {
    return (value === undefined || value == null || value.length <= 0) ? true : false;
}
