
$(document).ready(function() {
    $('#yearContent').change(function(){
    	$('#monthContent').val("");
    	getDatabaseList()
    });
    $('#monthContent').change(function(){
    	if($("#yearContent").val() == ""){
    		alert("Please Month Selected.")
    		$("#monthContent").val("");
    		return false;
    	}
    	getDatabaseList()
    });
});

function openBackupModal() {
	var nowDate = new Date();
	var newYear = nowDate.getFullYear();
	var newMonth = nowDate.getMonth()+1;
	var newDate = nowDate.getDate();

	if(newMonth.toString.length == "1"){
		newMonth = "0"+newMonth;
	}
	if(newDate.toString.length == "1"){
		newDate = "0"+newDate;
	}
	var fileName = "backup-"+newYear+"-"+newMonth+"-"+newDate+"-"+nowDate.getHours()+""+nowDate.getMinutes()+".dump";
	if (confirm('All data will be backuped up in the '+ fileName +' File?')) {
		$.ajax({
			url: '/dashbd/resources/systemDbBackup.do',
			method: 'POST',
			dataType: 'json',
			data: {
				fileName: fileName
			},
			success: function(data, textStatus, jqXHR) {
				if(data.result == "-1"){
					alert(data.reqMsg);
				}else{
					alert("Database Backup Success.!");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert(errorThrown + textStatus);
				return false;
			}
		});
	}
}

function openRestoreModal(backupId, backupFileName) {
	if (confirm('All the data in the database will be erased and will be restored with '+backupFileName+' file?')) {
		$.ajax({
			url: '/dashbd/resources/systemDbRestore.do',
			method: 'POST',
			dataType: 'json',
			data: {
				backupId: backupId
			},
			success: function(data, textStatus, jqXHR) {
				if(data.result == "-1"){
					alert(data.reqMsg);
				}else{
					alert("Database Restore Success.!");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert(errorThrown + textStatus);
				return false;
			}
		});
	}
}

function getDatabaseList() {
	$('#table').bootstrapTable('destroy');
	var table = $('#table').bootstrapTable({
		method: 'post',
		url: '/dashbd/resources/systemDblist.do',
		contentType: 'application/json',
		dataType: 'json',
		queryParams: function(params) {
			params['searchYear'] = $('#yearContent').val();
			params['searchMonth'] = $('#monthContent').val();
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
		minimumCountColumns: 6,
		clickToSelect: false,
		columns: [{
			field: 'backupId',
			title: 'backupId',
			visible: false
		}, {
			field: 'rownum',
			title: 'No',
			width: '10%',
			align: 'center',
			valign: 'middle',
			sortable: false
		}, {
			field: 'backupCreatedAt',
			title: 'Backup Date',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'backupFileName',
			title: 'File Name',
			width: '20%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'backupFilePath',
			title: 'File Path',
			width: '30%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'backupCreatedId',
			title: 'Backup User',
			width: '15%',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: '',
			title: 'Command',
			width: '10%',
			align: 'right',
			valign: 'middle',
			sortable: false,
			formatter: function(value, row, index) {
				var html = '<button type="button" onclick="openRestoreModal(\'' + row.backupId + '\', \'' + row.backupFileName + '\')" class="btn btn-success btn-xs button-edit">Restore DB</button> '
				return html;
			}
		}]
	});
}