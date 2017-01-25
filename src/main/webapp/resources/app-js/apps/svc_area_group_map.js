
var red = '#FF0000';
var blue = '#1c84c6';
var gray = '#c2c2c2';
var white = '#FFFFFF';
var black = '#000000';

$(document).ready(function() {
	//저장버튼 숨김처리
	$(".proccess-btn").hide();
});

function jsTreeSetting() {
	$.getScript( "/dashbd/resources/js/plugins/jsTree/jstree.min.js" )
		.done(function( script, textStatus ) {
			var trList = $("#group_area tr");
			var choiceYn = false;
			var gruop_id = '';
			
			for(var i=0; i < trList.length; i++) {
				var tempTr = $(trList[i]);
				
				if(tempTr.attr("choiceYn") == 'Y') {
					gruop_id = tempTr.attr("data-init");
					choiceYn = true;
					break;
				}
			}
			
			if(choiceYn) {
				$.ajax({
				    url : "/dashbd/api/getTreeNodeData.do",
				    type: "POST",
				    data : { 
				    	gruop_id : gruop_id
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
			} else {
				alert("Choice Row");
			}
		});
}

//jsTree Init Function
function treeInit(data) {
	var treeData = data.resultList;
	for(var i=0; i < treeData.length; i++) {
		var node = treeData[i];
		
		if(i == 0) {
			$('#treeNode').append('<ul><li class="' + node.node_div + '" data-init="' + node.node_id + '">' + node.name + '</li></ul>');
			continue;
		}
		
		var divClass = '';
		if(node.node_div == 'circle') divClass='root';
		else if(node.node_div == 'city') divClass='circle';
		else if(node.node_div == 'hotspot') divClass='city';
		
		for(var j=0; j < $('#treeNode li.' + divClass).length; j++) {
			var compareNode = $('#treeNode li.' + divClass)[j];
			
			if($(compareNode).attr("data-init") == node.pnode_id) {
				var liStr = '<li class="' + node.node_div + '" data-init="' + node.node_id + '" data-lat="' + node.latitude + '" data-lng="' + node.longitude + '">' + node.name + "</li>";
				
				if($(compareNode).html().indexOf("ul") == -1) {
					$(compareNode).append('<ul>' + liStr + '</ul>');
				} else {
					$($(compareNode).find("ul")[0]).append(liStr);
				}
				
				break;
			}
		}
	}
	
	$('#treeNode').jstree({"checkbox" : {
	      "keep_selected_style" : false
	       },
	      'plugins':["checkbox"]
	    });
	
	//저장 취소 버튼 표시
	$(".proccess-btn").show();
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
            title: "Alert !",
            text: "Choice Circle Value"
        });
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
		            	location.reload();
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
    });
})