$(document).ready(function() {
	$.blockUI();
	jsTreeSetting();
	
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

function choiceServiceArea() {
	$(".jstree li[role=treeitem]").each(function () {
	     $(".jstree").jstree('select_node', this);
	});
	
	$("#circleModal").modal('show');
}

function jsTreeSetting(openAllYn) {
	$.getScript( "/dashbd/resourcesRenew/js/plugins/jsTree/jstree.min.js" )
		.done(function( script, textStatus ) {
			$.ajax({
			    url : "/dashbd/api/getTreeNodeData.do",
			    type: "POST",
			    data : { 
			    	gruop_id : '',
			    	searchType : $("#searchType").val(),
			    	searchInput : $("#search-input").val(),
			    	circle_id : $("#circleId").val()
			    },
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			    success : function(responseData) {
			        $("#ajax").remove();
			        var data = JSON.parse(responseData);
			         
			        if(data.resultList.length != 0) {
				        $("#treeNode").jstree("destroy").empty();
				        treeInit(data, openAllYn);
			        } else {
			        	swal({title:"Not Found !", text:"Please enter the keyword", type:"warning"}, function() {
			        		$("#search-input").val('');
			        		$("#searchType").val('');
			    		})
			        }
			        
			    },
		        error : function(xhr, status, error) {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
			});
		});
}

//jsTree Init Function
function treeInit(data, openAllYn) {
	var treeData = data.resultList;
	for(var i=0; i < treeData.length; i++) {
		var node = treeData[i];
		
		var tempChildCntStr = ' (<span name="childCnt">' + node.childCnt + '</span> ' + node.childCntName + ')';
		if(tempChildCntStr == ' (<span name="childCnt"></span> )') tempChildCntStr = '';
		
		//root를 그려줌(Circles)
		if(i == 0) {
			$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + tempChildCntStr + '</li></ul>');
			continue;
		}
		
		//현재 붙여넣어 줄 노드의 구분값을 판단하여 그에 따른 상위 노드만 모아서 부모 노드를 찾음
		var divClass = '';
		if(node.node_div == 'circle') divClass='root';
		else if(node.node_div == 'city') divClass='circle';
		else if(node.node_div == 'hotspot') divClass='city';
		
		for(var j=0; j < $('#treeNode li.' + divClass).length; j++) {
			var compareNode = $('#treeNode li.' + divClass)[j];
			
			if($(compareNode).attr("data-init") == node.pnode_id) { 
				var liStr = '<li class="' + node.node_div + '" title="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' 
							+ node.latitude + '" data-lng="' + node.longitude + '" data-band="' + node.bandwidth + '">' + node.name + tempChildCntStr + '</li>';
				
				if($(compareNode).html().indexOf("ul") == -1) {
					$(compareNode).append('<ul>' + liStr + '</ul>');
				} else {
					$($(compareNode).find("ul")[0]).append(liStr);
				}
				
				break;
			}
		}
	}
	
	$("#treeNode")
		.bind('before_open.jstree', function(evt, data) {
			$(".jstree-icon.jstree-themeicon").remove();
			
			if(data.node.openYn == undefined) {
				$("#" + data.node.id + " li[role=treeitem]").each(function () {
				     $(".jstree").jstree('select_node', this);
				});	
			}
			
			data.node.openYn = 'Y';
		})
		.bind('ready.jstree', function(e, data) {
			$(".jstree-icon.jstree-themeicon").remove();
			 arrangeTreeSearchData();
			 $.unblockUI();
	    }).jstree({
	    	"checkbox" : {
	  	      "keep_selected_style" : false,
	  	      "three_state": false
	  	    },
		    "plugins" : [ "checkbox"]
		  });
	
	if(!openAllYn) {
		//제일 처음 노드 오픈
		$("#treeNode").jstree("open_node", $("#treeNode .root"));
	} else {
		$("#treeNode").jstree("open_all");
	}
}

$(document).on("keydown", "#search-input", function(event) {
	//Enter입력시에만 조회
	if(event.keyCode == 13) {
		searchTreeNode();
    }
})

function searchTreeNode() {
	var searchString = $("#search-input").val();
	
	if(searchString == '') {
		$("#searchType").val('');
		jsTreeSetting(false);
	} else {
		jsTreeSetting(true);
	}
}

//검색시 childCnt 정리 메소드
function arrangeTreeSearchData() {
	var searchResultList = $("#treeNode li");
	
	for(var i=0; i < searchResultList.length; i++) {
		var searchNode = $(searchResultList[i]);
		
		//일부 arae만 나올 경우는 모두 오픈
		if($("#circleId").val() != '') 
			$("#treeNode").jstree("open_all");
		
		if(!searchNode.hasClass("hotspot")) searchNode.find("span[name='childCnt']")[0].innerHTML = $(searchNode.find("ul")[0]).children().length;
	}
}

function choiceArea() {
	var choiceObjectStr = '';
	
	var allNode = $("#treeNode ul li").not(".root");			
	for(var i=0; i < allNode.length; i++) {
		var tempObj = $(allNode[i]);
		
		if($(tempObj.find("a")[0]).hasClass("jstree-clicked")) {
			if(tempObj.hasClass("circle")) {
				choiceObjectStr += ',' + tempObj.attr("data-init").substring(1);
			} else if(tempObj.hasClass("city")) {
				choiceObjectStr += ',' + tempObj.attr("data-init").substring(tempObj.attr("data-init").indexOf("B")+1);
			} else if(tempObj.hasClass("hotspot")) {
				choiceObjectStr += ',' + tempObj.attr("data-init").substring(tempObj.attr("data-init").indexOf("C")+1);
			}
		}
	}
	
	choiceObjectStr = choiceObjectStr.substring(1);
	
	var param = {
		choiceObjectStr : choiceObjectStr
	}
	
	$("#choiceTreeStr").val(JSON.stringify(param));
	$("#circleModal").modal("hide");
}

function deselectArea() {
	$(".jstree li[role=treeitem]").each(function () {
	     $(".jstree").jstree('deselect_node', this);
	});
}