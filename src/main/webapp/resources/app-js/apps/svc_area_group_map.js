
var red = '#FF0000';
var blue = '#1c84c6';
var gray = '#c2c2c2';
var white = '#FFFFFF';
var black = '#000000';

//전역 서비스 영역 그룹 아이디
var serviceAreaGroupId;

$(document).ready(function() {
	//저장버튼 숨김처리
	$(".proccess-btn").hide();
});

function jsTreeSetting() {
	$.getScript( "/dashbd/resources/js/plugins/jsTree/jstree.min.js" )
		.done(function( script, textStatus ) {
			var trList = $("#group_area tr");
			var group_id = '';
			
			for(var i=0; i < trList.length; i++) {
				var tempTr = $(trList[i]);
				
				if(tempTr.attr("choiceYn") == 'Y') {
					group_id = tempTr.attr("data-init");
					break;
				}
			}
			
			//Tree 데이터를 불러오기 전에 전역변수에 할당
			serviceAreaGroupId = group_id;
			
			$.ajax({
			    url : "/dashbd/api/getTreeNodeData.do",
			    type: "POST",
			    data : { 
			    	group_id : group_id,
			    	circle_id : $("#search-circle").val()
			    },
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			    success : function(responseData) {
			        $("#ajax").remove();
			        var data = JSON.parse(responseData);
			        
			        $("#treeNode").jstree("destroy").empty();
			        treeInit(data);
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

var checkNodeList;
//jsTree Init Function
function treeInit(data) {
	checkNodeList = ''; //체크된 노드 전역변수 초기화
	var treeData = data.resultList;
	for(var i=0; i < treeData.length; i++) {
		var node = treeData[i];
		
		//root를 그려줌(Circles)
		if(i == 0) {
			$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + '</li></ul>');
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
				//체크된 노드가 있다면 전역변수에 넣어주고 jstree가 준비되면 노드를 열어주면서 체크함
				if(node.checkYn == 1) checkNodeList += ',' + node.node_id;
				
				var liStr = '<li class="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' + node.latitude + '" data-lng="' + node.longitude + '">' + node.name + '</li>';
				
				if($(compareNode).html().indexOf("ul") == -1) {
					$(compareNode).append('<ul>' + liStr + '</ul>');
				} else {
					$($(compareNode).find("ul")[0]).append(liStr);
				}
				
				break;
			}
		}
	}
	
	$('#treeNode').bind('ready.jstree', function (event, data) { 
		//체크된 노드의 id를 토대로 최상위 부모부터 내려와서 열고 체크해줌
		var tempCheckNodeList = checkNodeList.substring(1).split(',');
		for(var i=0; i < tempCheckNodeList.length; i++) {
			var tempHotspotNode = tempCheckNodeList[i];
			checkNode(tempHotspotNode);
		}
	})
//	.bind('changed.jstree', function(evt, data) {
//		if(data.node.li_attr.class == 'hotspot') { 
//			data.instance.set_icon(data.node, '/dashbd/resources/img/icon/ico_number_1_3.png')
//		}
//	})
	.jstree({"checkbox" : {
	      "keep_selected_style" : false
	       },
	      'plugins':["checkbox"]
	    });
	
	//제일 처음 노드 오픈
	$("#treeNode").jstree("open_node", $($("#treeNode li")[0]));
	//저장 취소 버튼 표시
	$(".proccess-btn").show();
}

//최하위 노드를 토대로 단계적으로 노드를 열고 최하위 노드를 체크하는 메소드
function checkNode(leafNode) {
	var circleId = leafNode.substring(0, leafNode.indexOf("B"));
	var cityId = leafNode.substring(leafNode.indexOf("B"), leafNode.indexOf("C"));
	var hotspotId = leafNode.substring(leafNode.indexOf("C"));
	
	for(var i=0; i < $("#treeNode li.circle").length; i++) {
		var tempNode = $($("#treeNode li.circle")[i]);
		
		if(tempNode.attr("data-init") == circleId) {
			if(tempNode.attr("aria-expanded") != 'true') {
				$("#treeNode").jstree("open_node", tempNode);
				break;
			}
		}
	}
	
	for(var i=0; i < $("#treeNode li.city").length; i++) {
		var tempNode = $($("#treeNode li.city")[i]);
		
		if(tempNode.attr("data-init") == (circleId + cityId)) {
			if(tempNode.attr("aria-expanded") != 'true') {
				$("#treeNode").jstree("open_node", tempNode);
				break;
			}
		}
	}
	
	for(var i=0; i < $("#treeNode li.hotspot").length; i++) {
		var tempNode = $($("#treeNode li.hotspot")[i]);
		
		if(tempNode.attr("data-init") == (circleId + cityId + hotspotId)) {
			if(tempNode.attr("aria-expanded") != 'true') {
				tempNode.find("a").click();
				break;
			}
		}
	}
}

//서비스 영억 그룹 테이블 선택시 발동하는 함수
function selectServiceAreaGroup(obj) {
	$(obj).css("background", blue);
	$(obj).css("color", white);
	$(obj).attr("choiceYn", 'Y');
	
	$(obj).siblings().css("background", white);
	$(obj).siblings().css("color", black);
	$(obj).siblings().attr("choiceYn", 'N');
	
	//선택된 gruop에 따른 city 리스트 조회
	jsTreeSetting();
}

//Circle select 박스 변경 이벤트
function changeCircle() {
	if($("#search-circle").val() != '') {
		//저장버튼 숨김처리
		$(".proccess-btn").hide();
		//기존 treeNode 삭제
		$("#treeNode").empty();
		
		$("#selectedCircle").text($("#search-circle option:selected").text() + ' Service Area Group');
		
		$.ajax({
		    url : "/dashbd/api/getServiceAreaGroupList.do",
		    type: "POST",
		    data : { 
		    	circle_id : $("#search-circle").val()
		    },
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		        
		        $("#group_area").empty();
		        
		        if( data.length != 0 ) {
		        	for(var i=0; i < data.length; i++) {
		        		$("#group_area").append('<tr onclick="selectServiceAreaGroup(this)" data-init="' + data[i].group_id + '" title="' + data[i].group_description + '"><td>' 
		        				+ data[i].group_name + '</td><td><button type="button" class="btn btn-primary4 btn-sm" onCLick="deleteServiceAreaGroup(this)">Delete</button></td></tr>');
			        }
		        } 
		        
		        $("#group_area").append('<tr><td><input type="text" /></td><td><button type="button" class="btn btn-primary4 btn-sm" onCLick="addServiceAreaGroup(this)">Add</button></td></tr>');
		        //콤보박스 변경시 트리 셋팅
		        jsTreeSetting();
		    },
	        error : function(xhr, status, error) {
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
		});
	} else {
		$("#selectedCircle").text('');
		$("#group_area").empty();
        $(".proccess-btn").hide();
        $("#treeNode").empty();
	}
}

//서비스 그룹 생성 메소드
function addServiceAreaGroup(obj) {
	var group_name = $(obj).parent().siblings().find("input").val();
	
	if(group_name != '') {
		$.ajax({
		    url : "/dashbd/api/insertServiceAreaGroup.do",
		    type: "POST",
		    data : { 
		    	circle_id : $("#search-circle").val(),
		    	group_name : group_name,
		    	group_description : group_name
		    },
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		        
		        if(data.resultCode == 'S') {
		        	swal({
		                title: "Success !",
		                text: "Success",
		                type:"success"
		            },
		            function() {
		            	//콤보박스 재조회
		            	changeCircle();
		            })
		        } else if(data.resultCode == 'E') {
		        	swal({
		                title: "Fail !",
		                text: "Exist Group Name"
		            });
		        } else {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
		    },
	        error : function(xhr, status, error) {
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
		});
	} else {
		swal({
            title: "Fail !",
            text: "Insert Gruop Name"
        });
	}
}

//서비스 그룹 삭제 메소드
function deleteServiceAreaGroup(obj) {
	var group_id = $(obj).parents("tr").attr("data-init");
	
	if(group_id != '') {
		$.ajax({
		    url : "/dashbd/api/deleteServiceAreaGroup.do",
		    type: "POST",
		    data : { 
		    	group_id : group_id
		    },
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		    success : function(responseData) {
		        $("#ajax").remove();
		        var data = JSON.parse(responseData);
		        
		        if(data.resultCode == 'S') {
		        	swal({
		                title: "Success !",
		                text: "Success",
		                type:"success"
		            },
		            function() {
		            	location.reload();
		            })
		        } else {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
		    },
	        error : function(xhr, status, error) {
	        	swal({
	                title: "Fail !",
	                text: "Error"
	            });
	        }
		});
	} else {
		swal({
            title: "Fail !",
            text: "Error"
        });
	}
}

//저장 버튼 클릭 이벤트
$(document).on("click", "#save-btn", function() {
	swal({
        title: "Are you sure?",
        text: "Do you want to save it?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes",
        cancelButtonText: "No",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
    	if(isConfirm) {
    		//체크된 핫스팟 아이디들을 불러옴
    		var resultStr = getCheckedHotspotData();
    		
    		if(resultStr != '') {
    			$.ajax({
    			    url : "/dashbd/api/saveServiceAreaGroupHotspot.do",
    			    type: "POST",
    			    data : { 
        				group_id : serviceAreaGroupId,
    			    	resultStr : resultStr
    			    },
    			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    			    success : function(responseData) {
    			        $("#ajax").remove();
    			        var data = JSON.parse(responseData);
    			        
    			        if(data.resultCode == 'S') {
    			        	swal({
    			                title: "Success !",
    			                text: "Success",
    			                type:"success"
    			            },
    			            function() {
    			            	
    			            })
    			        } else {
    			        	swal({
    			                title: "Fail !",
    			                text: "Error"
    			            });
    			        }
    			    },
    		        error : function(xhr, status, error) {
    		        	swal({
    		                title: "Fail !",
    		                text: "Error"
    		            });
    		        }
    			});
    		} else {
    			swal("Fail !", "Please select data", "error");
    		}
    	} else {
    		swal("Cancelled", "Your imaginary file is safe :)", "error");
    	} 
    });
})

//체크된 핫스팟 데이터를 string 연결로 리턴하는 메소드
function getCheckedHotspotData() {
	var tempList = [];
	
	var hotspotList = $("#treeNode li.hotspot");
	for(var i=0; i < hotspotList.length; i++) {
		var tempHotspot = $(hotspotList[i]);
		
		if(tempHotspot.attr("aria-selected") == 'true') {
			var tempObj = {
				"city_id" : tempHotspot.parent().parent().attr("data-init").substring(tempHotspot.parent().parent().attr("data-init").indexOf("B")+1),
				"hotspot_id" : tempHotspot.attr("data-init").substring(tempHotspot.attr("data-init").indexOf("C")+1)
			}
			
			tempList.push(tempObj);
		}
	}
	
	return (tempList.length > 0)? JSON.stringify(tempList) : '';
	
}