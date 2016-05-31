
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
        getServiceAreaBmScEnbsCount($('#operatorEnbs option:selected').val(), "", "eNbsCount");
    });
    
    $('#operatorBroadContent').change(function() {
        getServiceAreaBmSc(1, $('#operatorBroadContent option:selected').val(), true, "bmscBroadContent");
    });

    
    $('#bmscEnbs').change(function() {
    	getServiceAreaBmScEnbsCount($('#operatorEnbs option:selected').val(), $('#bmscEnbs option:selected').val(), "eNbsCount");
    });
    
    $('#operatorServiceArea').change(function() {
        getServiceAreaBmSc(1, $('#operatorServiceArea option:selected').val(), true, "bmscServiceArea");
        getServiceAreaBmScServiceAreaCount($('#operatorServiceArea option:selected').val(), "", "serviceAreaCount");
    });
    
    $('#bmscServiceArea').change(function() {
    	getServiceAreaBmScServiceAreaCount($('#operatorServiceArea option:selected').val(), $('#bmscServiceArea option:selected').val(), "serviceAreaCount");
    });
    
    $('#bmscContent').change(function() {
        getServiceAreaByBmSc($('#bmscContent option:selected').val(), true);
    });
    
    $('#yearContent').change(function(){
    	$('#monthContent').val("");
    	$('#dayContent').html('<option value="">--ALL--</option>');
    });
    
    $('#yearBroadContentS').change(function(){
    	$('#monthBroadContentS').val("");
    	$('#dayBroadContentS').html('<option value="">--ALL--</option>');
    });
    
    $('#yearBroadContentE').change(function(){
    	$('#monthBroadContentE').val("");
    	$('#dayBroadContentE').html('<option value="">--ALL--</option>');
    });
    
    $('#monthBroadContentS').change(function(){
    	if($("#yearBroadContentS").val() == ""){
    		alert("Please Month Selected.")
    		$("#monthBroadContentS").val("");
    		return false;
    	}else{
    		updateNumberOfBroadContentSDays(); 
        	$('#dayBroadContentS').val("");
    	}    	
    });
    
    $('#monthBroadContentE').change(function(){
    	if($("#yearBroadContentE").val() == ""){
    		alert("Please Month Selected.")
    		$("#monthBroadContentE").val("");
    		return false;
    	}else{
    		updateNumberOfBroadContentEDays(); 
        	$('#dayBroadContentE').val("");
    	}    	
    });
    
    var checkDate = new Date();
    updateNumberOfBroadContentSDays("F");
    updateNumberOfBroadContentEDays("F");
    
    function updateNumberOfBroadContentSDays(type){
        $('#dayBroadContentS').html('<option value="">--ALL--</option>');
        month = $('#monthBroadContentS').val();
        year = $('#yearBroadContentS').val();
        days = daysInMonth(month, year);
        for(i=1; i < days+1 ; i++){
        	if(type == "F"){
        		if(i == checkDate.getDate()){
        			if(i < 10){
        				$('#dayBroadContentS').append('<option value="0'+i+'" selected>'+i+'</option>');
        			}else{
        				$('#dayBroadContentS').append('<option value="'+i+'" selected>'+i+'</option>');
        			}
        		}else{
        			if(i < 10){
        				$('#dayBroadContentS').append('<option value="0'+i+'">'+i+'</option>');
        			}else{
        				$('#dayBroadContentS').append('<option value="'+i+'">'+i+'</option>');
        			}
        		}
        	}else{
    			if(i < 10){
    				$('#dayBroadContentS').append('<option value="0'+i+'">'+i+'</option>');
    			}else{
    				$('#dayBroadContentS').append('<option value="'+i+'">'+i+'</option>');
    			}
        	}
        }
    }

    function updateNumberOfBroadContentEDays(type){
        $('#dayBroadContentE').html('<option value="">--ALL--</option>');
        month = $('#monthBroadContentE').val();
        year = $('#yearBroadContentE').val();
        days = daysInMonth(month, year);

        for(i=1; i < days+1 ; i++){
        	if(type == "F"){
        		if(i == checkDate.getDate()){
        			if(i < 10){
        				$('#dayBroadContentE').append('<option value="0'+i+'" selected>'+i+'</option>');
        			}else{
        				$('#dayBroadContentE').append('<option value="'+i+'" selected>'+i+'</option>');
        			}
        		}else{
        			if(i < 10){
        				$('#dayBroadContentE').append('<option value="0'+i+'">'+i+'</option>');
        			}else{
        				$('#dayBroadContentE').append('<option value="'+i+'">'+i+'</option>');
        			}
        		}
        	}else{
    			if(i < 10){
    				$('#dayBroadContentE').append('<option value="0'+i+'">'+i+'</option>');
    			}else{
    				$('#dayBroadContentE').append('<option value="'+i+'">'+i+'</option>');
    			}
        	}
        }
    }
    
    $('#operatorInterTff').change(function() {
        getServiceAreaBmSc(1, $('#operatorInterTff option:selected').val(), true, "bmscInterTff");
        getInterTffList();
    });
    
    $('#bmscInterTff').change(function() {
    	getInterTffList();
    });
    
    $('#yearInterTff').change(function() {
    	if($("#yearInterTff").val() == ""){
    		$('#monthInterTff').val("");
        	$('#dayInterTff').html('<option value="">--ALL--</option>');
    	}    	
    	getInterTffList();
    });
    
    $('#monthInterTff').change(function() {
    	if($("#yearInterTff").val() == ""){
    		alert("Please Month Selected.")
    		$("#monthInterTff").val("");
    		return false;
    	}
    	if($("#monthInterTff").val() == ""){
    		$('#dayInterTff').html('<option value="">--ALL--</option>');
    		return false;
    	}
    	updateNumberTrafficOfDays();
    	getInterTffList();
    });
    
    $('#dayInterTff').change(function() {
    	if($("#yearInterTff").val() == ""){
    		alert("Please Year Selected.")
    		return false;
    	}
    	if($("#monthInterTff").val() == ""){
    		alert("Please Month Selected.")
    		return false;
    	}
    	getInterTffList();
    });
    
    $('#monthContent').change(function(){
    	if($("#yearContent").val() == ""){
    		alert("Please Month Selected.")
    		$("#monthContent").val("");
    		return false;
    	}else{
            updateNumberOfDays(); 
        	$('#dayContent').val("");
    	}    	
    });
    
    $('#yearIncomTff').change(function(){
    	if($("#yearIncomTff").val() == ""){
    		$('#monthIncomTff').val("");
        	$('#dayIncomTff').html('<option value="">--ALL--</option>');
    	}
    	getIncomTffList();
    });
    
    $('#monthIncomTff').change(function(){
    	if($("#yearIncomTff").val() == ""){
    		alert("Please Month Selected.")
    		return false;
    	}
    	if($("#monthIncomTff").val() == ""){
    		$('#dayIncomTff').html('<option value="">--ALL--</option>');
    		return false;
    	}
		updateNumberIncomTffOfDays(); 
		getIncomTffList();
    });
    
    $('#dayIncomTff').change(function() {
    	if($("#yearIncomTff").val() == ""){
    		alert("Please Year Selected.")
    		return false;
    	}
    	if($("#monthIncomTff").val() == ""){
    		alert("Please Month Selected.")
    		return false;
    	}
    	getIncomTffList();
    });
    
    getPermissionList(null);
    
    if($('#operatorEnbs option:selected').val() != ""){
        getServiceAreaBmSc(1, $('#operatorEnbs option:selected').val(), true, "bmscEnbs");
        getServiceAreaBmScEnbsCount($('#operatorEnbs option:selected').val(), "", "eNbsCount");
    }else{
    	getServiceAreaBmScEnbsCount("", "", "eNbsCount");
    }

    if($('#operatorEnbs option:selected').val() != ""){
        getServiceAreaBmSc(1, $('#operatorServiceArea option:selected').val(), true, "bmscServiceArea");
        getServiceAreaBmScServiceAreaCount($('#operatorServiceArea option:selected').val(), "", "serviceAreaCount");
    }else{
        getServiceAreaBmScServiceAreaCount("", "", "serviceAreaCount");
    }
    
    getInterTffList();
    getIncomTffList();
    
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

	$('#go-search').click(doSearch);

	$('.pagination-detail').hide();
});

function doSearch() {
	if($('#yearBroadContentS option:selected').val() == ""){
		alert("Please Search Start Year Selected.")
		return false;
	}
	if($('#yearBroadContentE option:selected').val() == ""){
		alert("Please Search End Year Selected.")
		return false;
	}
	if($('#yearBroadContentS option:selected').val() > $('#yearBroadContentE option:selected').val()){
		alert("Search Start Year End Year is greater than.")
		return false;
	}
	if($('#monthBroadContentS option:selected').val() > $('#monthBroadContentE option:selected').val()){
		alert("Search Start Month End Month is greater than.")
		return false;
	}
	if($('#dayBroadContentS option:selected').val() > $('#dayBroadContentE option:selected').val()){
		alert("Search Start Day End Day is greater than.")
		return false;
	}

	getBroadCastList();
}

function getInterTffList() {
	$('#interTffTable').bootstrapTable('destroy');
	// 테이블 생성
	var table = $('#interTffTable').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/getInterTrafficList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['searchOperator'] = $('#operatorInterTff').val();
			params['searchBmsc'] = $('#bmscInterTff').val();
			params['searchYear'] = $('#yearInterTff').val();
			params['searchMonth'] = $('#monthInterTff').val();
			params['searchDay'] = $('#dayInterTff').val();
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
			field: 'rownum',
			title: 'No',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'reqType',
			title: 'Service Type',
			width: '28%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'successCount',
			title: 'Success',
			width: '19%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'failCount',
			title: 'Error',
			width: '16%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'totPercentage',
			title: 'Percentage',
			width: '24%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				return value+"%";
			}
		}]
	});
}

function getIncomTffList() {
	$('#incomTffTable').bootstrapTable('destroy');
	// 테이블 생성
	var table = $('#incomTffTable').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/getIncomingTrafficList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['searchYear'] = $('#yearIncomTff').val();
			params['searchMonth'] = $('#monthIncomTff').val();
			params['searchDay'] = $('#dayIncomTff').val();
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
			field: 'rownum',
			title: 'No',
			width: '13%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'reqType',
			title: '#Req',
			width: '28%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'successCount',
			title: 'Success',
			width: '19%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'failCount',
			title: 'Error',
			width: '16%',
			align: 'center',
			valign: 'middle',
			sortable: true,
		}, {
			field: 'totPercentage',
			title: 'Percentag',
			width: '24%',
			align: 'center',
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index) {
				return value+"%";
			}
		}]
	});
}

function updateNumberOfDays(){
    $('#dayContent').html('<option value="">--ALL--</option>');
    month = $('#monthContent').val();
    year = $('#yearContent').val();
    days = daysInMonth(month, year);

    for(i=1; i < days+1 ; i++){
            $('#dayContent').append($('<option />').val(i).html(i));
    }
}

function updateNumberTrafficOfDays(){
    $('#dayInterTff').html('<option value="">--ALL--</option>');
    month = $('#monthInterTff').val();
    year = $('#yearInterTff').val();
    days = daysInMonth(month, year);

    for(i=1; i < days+1 ; i++){
    	if(i < 10){
    		$('#dayInterTff').append($('<option />').val("0"+i).html(i));
    	}else{
    		$('#dayInterTff').append($('<option />').val(i).html(i));
    	}
    }
}

function updateNumberIncomTffOfDays(){
    $('#dayIncomTff').html('<option value="">--ALL--</option>');
    month = $('#monthIncomTff').val();
    year = $('#yearIncomTff').val();
    days = daysInMonth(month, year);

    for(i=1; i < days+1 ; i++){
    	if(i < 10){
    		$('#dayIncomTff').append($('<option />').val("0"+i).html(i));
    	}else{
    		$('#dayIncomTff').append($('<option />').val(i).html(i));
    	}
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
	if(operatorId == ""){
    	$("#"+bmscType).empty();
        $("#"+bmscType).append('<option value="">--ALL--</option>');
	}else{
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
}

//eNBs Sum
function getServiceAreaBmScEnbsCount(operatorId, bmscId, eNbsCount)
{
	$.ajax({
      url : "/dashbd/api/serviceAreaBmScByEnbsCount.do",
      type: "get",
      data : { "operatorId" : operatorId, "bmscId" : bmscId },
      success : function(responseData){
          $("#ajax").remove();
          var data = JSON.parse(responseData);
          $("#eNbsCount").html("<h2><strong>"+setComma(data.result.count)+" eNBs</strong></h2>");
      }
  });
}

//Service Area Sum
function getServiceAreaBmScServiceAreaCount(operatorId, bmscId, eNbsCount)
{
	$.ajax({
    url : "/dashbd/api/serviceAreaBmScByServiceAreaCount.do",
    type: "get",
    data : { "operatorId" : operatorId, "bmscId" : bmscId },
    success : function(responseData){
        $("#ajax").remove();
        var data = JSON.parse(responseData);
        $("#serviceAreaCount").html("<h2><strong>"+setComma(data.result.count)+" Service Areas</strong></h2>");
    }
	});
}

function setComma(n) {
    var reg = /(^[+-]?\d+)(\d{3})/;
    n += "";
    while (reg.test(n)) n = n.replace(reg, "$1" + "," + "$2");
    return n;
}
function getServiceAreaByBmSc(bmscId, isUpload) {
	if(bmscId == ""){
    	$("#serviceContent").empty();
        $("#serviceContent").append('<option value="">--ALL--</option>');
	}else{
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
}

function isEmpty(value) {
    return (value === undefined || value == null || value.length <= 0) ? true : false;
}


function getBroadCastList() {
	$('#broadCastTable').bootstrapTable('destroy');
	// 테이블 생성
	var table = $('#broadCastTable').bootstrapTable({
		method: 'post',
		url: '/dashbd/api/getBroadCastList.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['searchOperator'] = $('#operatorBroadContent').val();
			params['searchBmsc'] = $('#bmscBroadContent').val();
			params['searchSYear'] = $('#yearBroadContentS').val();
			params['searchSMonth'] = $('#monthBroadContentS').val();
			params['searchSDay'] = $('#dayBroadContentS').val();
			params['searchEYear'] = $('#yearBroadContentE').val();
			params['searchEMonth'] = $('#monthBroadContentE').val();
			params['searchEDay'] = $('#dayBroadContentE').val();
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
		minimumCountColumns: 10,
		clickToSelect: false,
		columns: [
	                [
	                    {
	                        title: 'Title',
	            			field: 'contentName',
	                        rowspan: 2,
	                        align: 'center',
	                        valign: 'middle',
	                        sortable: true,
	            			formatter: function(value, row, index) {
	            				return "<a href=/dashbd/view/viewContent.do?id="+row.contentId+">"+value+"</a>";
	            			}
	                    }, {
	                        title: 'Category',
	                        field: 'serviceCategory',
	                        rowspan: 2,
	                        align: 'center',
	                        valign: 'middle',
	                        sortable: true
	                    }, {
	                        title: 'ServiceTime',
	                        colspan: 2,
	                        align: 'center',
		                    valign: 'middle'
	                    }, {
	                        title: 'Type',
	                        field: 'serviceType',
	                        rowspan: 2,
	                        align: 'center',
		                    valign: 'middle',
		                    sortable: true
	                    }, {
	                        title: 'BM-SC',
	                        field: 'bmscName',
	                        rowspan: 2,
	                        align: 'center',
		                    valign: 'middle',
		                    sortable: true
	                    }, {
	                        title: 'Service Area Name [Id]',
	                        field: 'serviceAreaName',
	                        rowspan: 2,
	                        align: 'center',
		                    valign: 'middle',
		                    sortable: true,
	            			formatter: function(value, row, index) {
	            				return value+" ["+row.serviceAreaId+"]";
	            			}
	                    }
	                ],
	                [
	                    {
	                        title: 'Start',
	                        field: 'startDate',
	                        align: 'center',
		                    valign: 'middle',
		                    sortable: true
	                    }, {
	                        title: 'End',
	                        field: 'endDate',
	                        align: 'center',
		                    valign: 'middle',
		                    sortable: true
	                    }
	                ]
	            ]
	});
}

$(function () {
    var scripts = [
            location.search.substring(1) || '../resources/app-js/apps/bootstrap-table/bootstrap-table.js'
            ,'../resources/js/plugins/footable/footable.all.min.js'
            //,'../resources/app-js/apps/bootstrap-table/tableExport.js'
            //,'../resources/app-js/apps/bootstrap-table/bootstrap-table-editable.js'
            //,'../resources/app-js/apps/bootstrap-table/bootstrap-editable.js'
        ],
        eachSeries = function (arr, iterator, callback) {
            callback = callback || function () {};
            if (!arr.length) {
                return callback();
            }
            var completed = 0;
            var iterate = function () {
                iterator(arr[completed], function (err) {
                    if (err) {
                        callback(err);
                        callback = function () {};
                    }
                    else {
                        completed += 1;
                        if (completed >= arr.length) {
                            callback(null);
                        }
                        else {
                            iterate();
                        }
                    }
                });
            };
            iterate();
        };

    eachSeries(scripts, getScript, getBroadCastList);
});

function getScript(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.src = url;

    var done = false;
    // Attach handlers for all browsers
    script.onload = script.onreadystatechange = function() {
        if (!done && (!this.readyState ||
                this.readyState == 'loaded' || this.readyState == 'complete')) {
            done = true;
            if (callback)
                callback();

            // Handle memory leak in IE
            script.onload = script.onreadystatechange = null;
        }
    };

    head.appendChild(script);

    // We handle everything using the script element injection
    return undefined;
}