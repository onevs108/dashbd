$.blockUI();

var map;
var modalMap;
//var circles = [];
var cities = [];
var hotspots = [];
var modalCities = [];

var default_lat = 24;
var default_lng = 82.975;
var default_zoom = 5;
var activeInfoWindow;
var default_center_lat = 24;
var default_center_lng = 83.975;

var red = '#ed5565';
var blue = '#1c84c6';
var gray = '#c2c2c2';
var white = '#FFFFFF';
var black = '#000000';

//최근 클릭된 InfoWindow를 담는 변수
var tempInfoWindow;
//마지막으로 클릭된 상위 Object를 담는 변수(circle)
var upperCircle;
//마지막으로 클릭된 상위 Object를 담는 변수(city)
var upperObj;

//현재 줌레벨을 담고있는 변수 선언
var currentZoomLevel = 'circle';

$(document).ready(function()
{
	jsTreeSetting(false);
    //json 형태로 변환
//    circlemap = JSON.parse(circlemap);
});

//처음으로 map을 resize했는지 여부 판단
var firstMapYn = true; 
function tabChange(tabDiv) {
	if(tabDiv == '1') {
		$($("ul.nav.nav-tabs")[0]).addClass("active");
		$($("ul.nav.nav-tabs")[1]).removeClass("active");
		$("#tab-1").addClass("active");
		$("#tab-2").removeClass("active");
		$("#map").hide();
		$(".circle-map").hide();
//		$("#treeNode").show();
//		$(".search-group").show();
		jsTreeSetting(false);
	} else if(tabDiv == '2') {
		$($("ul.nav.nav-tabs")[1]).addClass("active");
		$($("ul.nav.nav-tabs")[0]).removeClass("active");
		$("#tab-1").removeClass("active");
		$("#tab-2").addClass("active");
		$(".circle-map").show();
		$("#mapDescriptArea").text('Click the area t to view the Cities');
		$("#map").hide();
//		$("#treeNode").hide(); 
//		$(".search-group").hide();
		
//		circleClear();
//		getNewCircleList();
	}
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
			    	searchInput : $("#search-input").val()
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
			        
			        $.unblockUI();
			    },
		        error : function(xhr, status, error) {
		        	$.unblockUI();
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
	(function ($, undefined) {
        "use strict";
        
        $.jstree.plugins.inp = function (options, parent) {
            this.bind = function () {
                parent.bind.call(this);
                this.element
                    .on("change.jstree", ".jstree-inp", $.proxy(function (e) {
                            // do something with $(e.target).val()

                        }, this));
            };
            this.teardown = function () {
                if(this.settings.questionmark) {
                    this.element.find(".jstree-inp").remove();
                }
                parent.teardown.call(this);
            };
            //tree를 그릴때 input box 삽입
            this.redraw_node = function(obj, deep, callback) {
                obj = parent.redraw_node.call(this, obj, deep, callback);
                
                var customDiv = document.createElement('DIV');
            	customDiv.setAttribute('name', 'customDiv');
            	customDiv.style.marginRight = '50px';
            	customDiv.style.float = 'right';
            	
            	var customDiv2 = document.createElement('DIV');
            	customDiv2.setAttribute('name', 'customDiv2');
            	customDiv2.style.float = 'right';
                
                if(!$(obj).hasClass('root')) {
                	var marginDefault = '20px';
                	var componentMargin = '2px';
                	
                	var nodeLevel;
                	if($(obj).hasClass('circle')) nodeLevel = 'Circle';
                	else if($(obj).hasClass('city')) nodeLevel = 'City';
                	else if($(obj).hasClass('hotspot')) nodeLevel = 'Hotspot';
                	
                	var inp0 = document.createElement('INPUT');
                	inp0.setAttribute('type','text');
                	inp0.setAttribute('name','name');
                	inp0.setAttribute('placeholder', nodeLevel + ' Name');
                	inp0.className = "jstree-inp";
                	inp0.style.width = '200px';
                	inp0.style.height = '20px';
                	inp0.style.border = '1px solid #e5e6e7';
                	inp0.style.fontSize = "14px";
                	var inp1 = document.createElement('INPUT');
                    inp1.setAttribute('type','text');
                    inp1.setAttribute('name','said');
                    inp1.setAttribute('title', 'said'); 
                    inp1.setAttribute('readonly', 'readonly');
                    inp1.setAttribute('onblur', 'validationCheck(\'number\', this)');
                    inp1.className = "jstree-inp input-sm";
                    inp1.style.width = '120px';
                    inp1.style.height = '20px';
                    inp1.style.marginLeft = componentMargin;
                    inp1.style.border = '1px solid #e5e6e7';
                    inp1.style.fontSize = "14px";
                    var inp2 = document.createElement('INPUT');
                    inp2.setAttribute('type','text');
                    inp2.setAttribute('name','lat');
                    inp2.setAttribute('title', 'latitude');
                    inp2.setAttribute('onblur', 'validationCheck(\'number\',  this)');
                    inp2.className = "jstree-inp input-sm";
                    inp2.style.width = '120px';
                    inp2.style.height = '20px';
                    inp2.style.marginLeft = componentMargin;
                    inp2.style.border = '1px solid #e5e6e7';
                    inp2.style.fontSize = "14px";
                    var inp3 = document.createElement('INPUT');
                    inp3.setAttribute('type','text');
                    inp3.setAttribute('name','lng');
                    inp3.setAttribute('title', 'longitude');
                    inp3.setAttribute('onblur', 'validationCheck(\'number\',  this)');
                    inp3.className = "jstree-inp input-sm";
                    inp3.style.width = '120px';
                    inp3.style.height = '20px';
                    inp3.style.marginLeft = componentMargin;
                    inp3.style.border = '1px solid #e5e6e7';
                    inp3.style.fontSize = "14px";
                    var inp4 = document.createElement('INPUT');
                    inp4.setAttribute('type','text');
                    inp4.setAttribute('name','bandwidth');
                    inp4.setAttribute('title', 'bandwidth');
                    inp4.setAttribute('onblur', 'validationCheck(\'number\',  this)');
                    inp4.className = "jstree-inp input-sm";
                    inp4.style.width = '120px';
                    inp4.style.height = '20px';
                    inp4.style.marginLeft = marginDefault;
                    inp4.style.border = '1px solid #e5e6e7';
                    inp4.style.fontSize = "14px";
                    
                    var btnGruop = document.createElement('div');
                    btnGruop.className = 'btn-group';
                    
                    var btn1 = document.createElement('BUTTON');
                    btn1.setAttribute('type','button');
                    btn1.setAttribute('onclick', 'callSetLocationModalMap(this, \'serviceArea\', \'' + nodeLevel.toLowerCase() + '\', \'' + $(obj).attr("data-lat") + '\', \'' + $(obj).attr("data-lng") + '\')');
                    btn1.className = "btn-white btn btn-xs";
                    btn1.style.marginLeft = componentMargin;
                    btn1.style.color = 'rgb(255,255,255)';
                    btn1.style.backgroundColor = '#1ab394';
                    btn1.textContent = 'Map';
                    
                    var btn2 = document.createElement('BUTTON');
                    btn2.setAttribute('type','button');
                    btn2.setAttribute('onclick', 'serviceAreaProccess(\'tree\', \'edit\', this)');
                    btn2.className = "btn-white btn btn-xs";
                    btn2.style.marginLeft = componentMargin;
                    btn2.style.color = 'rgb(255,255,255)';
                    btn2.style.backgroundColor = '#337ab7';
                    btn2.textContent = 'Edit';
                    
                    var btn3 = document.createElement('BUTTON');
                    btn3.setAttribute('type','button');
                    btn3.setAttribute('onclick', 'serviceAreaProccess(\'tree\', \'delete\', this)');
                    btn3.className = "btn-white btn btn-xs";
                    btn3.style.marginLeft = componentMargin;
                    btn3.style.color = 'rgb(255,255,255)';
                    btn3.style.backgroundColor = red;
                    btn3.textContent = 'Delete';
                    
                    var btn4 = document.createElement('BUTTON');
                    btn4.setAttribute('type','button');
                    btn4.setAttribute('onclick', 'serviceAreaProccess(\'tree\', \'add\', this)');
                    btn4.className = "btn-white btn btn-xs";
                    btn4.style.marginLeft = componentMargin;
                    btn4.style.marginRight = '50px';
                    btn4.textContent = 'Add';
                    
                    //해당 아이디 값 셋팅
                    var said;
                    
                    //신규추가 노드일 경우 수행
                    if($(obj).hasClass('newNode')) {
                    	$(inp1).removeAttr("readonly");
                    	inp1.setAttribute('value', '');
                    	inp1.setAttribute('placeholder', 'SAID');
                        inp2.setAttribute('value', '');
                        inp2.setAttribute('placeholder', 'Latitude');
                        inp3.setAttribute('value', '');
                        inp3.setAttribute('placeholder', 'Longitude');
                        inp4.setAttribute('value', '');
                        inp4.setAttribute('placeholder', 'Bandwidth');
                    } else {
                    	if($(obj).hasClass('circle')) {
                        	said = $(obj).attr("data-init").substring($(obj).attr("data-init").indexOf('A')+1);
                        	inp2.setAttribute('readonly', 'readonly');
                        	inp3.setAttribute('readonly', 'readonly');
//                        	inp4.setAttribute('readonly', 'readonly');
//                        	btn1.setAttribute('disabled','disabled');
//                        	btn2.setAttribute('disabled','disabled');
                        	btn3.setAttribute('disabled','disabled');
                        } else if($(obj).hasClass('city')) {
                        	said = $(obj).attr("data-init").substring($(obj).attr("data-init").indexOf('B')+1);
                        } else if($(obj).hasClass('hotspot')) {
                        	said = $(obj).attr("data-init").substring($(obj).attr("data-init").indexOf('C')+1);
                        }
                    	
                        //아이디값 및 위도 경도 값 셋팅(트리 노드 뒤쪽의 input에 각각 넣어줌
                        inp1.setAttribute('value', said);
                        inp2.setAttribute('value', $(obj).attr("data-lat"));
                        inp3.setAttribute('value', $(obj).attr("data-lng"));
                        inp4.setAttribute('value', $(obj).attr("data-band"));
                    }
                    
                    //신규 노드일 경우 Add 버튼 추가 그 외에는 edit, delete 버튼 추가
                    if($(obj).hasClass('newNode')) {
                    	$($(obj).find("a")[0]).remove();
                    	$(obj).append(inp0);
                    	$(customDiv).append(inp4);
                    	$(customDiv).append(inp1);
                    	$(customDiv).append(inp2);
                    	$(customDiv).append(inp3);
                    	$(btnGruop).append(btn1);
                    	$(btnGruop).append(btn4); 
                    	$(customDiv).append(btnGruop);
                    } else {
                    	$(customDiv).append(inp4);
                    	$(customDiv).append(inp1);
                    	$(customDiv).append(inp2);
                    	$(customDiv).append(inp3);
                    	$(btnGruop).append(btn1);
                    	$(btnGruop).append(btn2);
                    	$(btnGruop).append(btn3);
                    	$(customDiv).append(btnGruop);
                    }
                    
                    //최종적으로 input과 button을 노드에 붙임
                    $(obj).append(customDiv);
                    //ul div 위치에 따라 어그러 지기 때문에 구성 요소 재 정렬
                    if($(obj).find("ul").length > 0) {
                    	$(obj).append($(obj).find("ul")[0]);
                    }
                    
                } else {
                	var lab1 = document.createElement('LABEL');
                	lab1.style.height = '20px';
                	lab1.style.marginRight = '50px';
                	lab1.style.marginBottom = '0px';
                	lab1.textContent = 'Bandwidth';
                	var lab2 = document.createElement('LABEL');
                	lab2.style.height = '20px';
                	lab2.style.marginRight = '90px';
                	lab2.style.marginBottom = '0px';
                	lab2.textContent = 'SAID';
                	var lab3 = document.createElement('LABEL');
                	lab3.style.height = '20px';
                	lab3.style.marginRight = '70px';
                	lab3.style.marginBottom = '0px';
                	lab3.textContent = 'Latitude';
                	var lab4 = document.createElement('LABEL');
                	lab4.style.height = '20px';
                	lab4.style.marginRight = '215px';
                	lab4.style.marginBottom = '0px';
                	lab4.textContent = 'Longitude';
                	
                	$(customDiv2).append(lab1);
                	$(customDiv2).append(lab2);
                	$(customDiv2).append(lab3);
                	$(customDiv2).append(lab4);
                	$(obj).append(customDiv2);
                }
                return obj;
            };
        };
    })(jQuery);
	
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
					//첫 노드일 경우 가상 노드를 주어 새롭게 추가할 수 있도록 함(newNode는 상위 노드의 위도 경도 값을 가짐)
					var firstNode = '<li class="newNode ' + node.node_div + '" data-init="" data-lat="' + treeData[i-1].latitude + '" data-lng="' + treeData[i-1].longitude + '" data-band=""></li>';
					if(node.node_div == 'circle') firstNode = ''; //circle은 firstNode 안 넣어줌
					$(compareNode).append('<ul>' + firstNode + liStr + '</ul>');
				} else {
					$($(compareNode).find("ul")[0]).append(liStr);
				}
				
				break;
			}
		}
	}
	
	for(var i=0; i < $("#treeNode li.circle").not(".newNode").length; i++) {
		var tempCircle = $($("#treeNode li.circle").not(".newNode")[i]);
		
		//서클 아래에 도시가 하나도 없을 경우 newNode 추가
		var newCityNode = '<ul><li class="newNode city" data-init="" data-lat="' + $(tempCircle).attr("data-lat") + '" data-lng="' + $(tempCircle).attr("data-lng") + '" data-band=""></li></ul>';
		
		if(tempCircle.find("ul").length == 0) {
			tempCircle.append(newCityNode);
		}
	}
	
	for(var i=0; i < $("#treeNode li.city").not(".newNode").length; i++) {
		var tempCity = $($("#treeNode li.city").not(".newNode")[i]);
		
		//도시 아래에 핫스팟이 하나도 없을 경우 newNode 추가
		var newHotspotNode = '<ul><li class="newNode hotspot" data-init="" data-lat="' + $(tempCity).attr("data-lat") + '" data-lng="' + $(tempCity).attr("data-lng") + '" data-band=""></li></ul>';
		
		if(tempCity.find("ul").length == 0) {
			tempCity.append(newHotspotNode);
		}
	}
	
	$("#treeNode")
		.bind('before_open.jstree', function(evt, data) {
			$(".jstree-icon.jstree-themeicon").remove();
		})
		.bind('ready.jstree', function(e, data) {
			$(".jstree-icon.jstree-themeicon").remove();
			
			if($("#search-input").val() != '') arrangeTreeSearchData();
	    }).jstree({
		    "conditionalselect" : function (node, event) {
		      return false;
		    },
		    "plugins" : [ "conditionalselect" , "nohover", "inp"]
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
	var searchResultList = $("#treeNode li").not(".newNode");
	
	for(var i=0; i < searchResultList.length; i++) {
		var searchNode = $(searchResultList[i]);
		
		if(i == 0) searchNode.find("span[name='childCnt']")[0].innerHTML = $(searchNode.find("ul")[0]).children().length;
		else if(!searchNode.hasClass("hotspot")) searchNode.find("span[name='childCnt']")[0].innerHTML = $(searchNode.find("ul")[0]).children().length-1;
	}
}

//메인 화면의 모달 로드
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: default_lat, lng: default_lng},
		zoom: default_zoom
	});
	
//	var mouseDownPos, gribBoundingBox = null,
//	    mouseIsDown = 0;
//	var themap = map;
	
	google.maps.event.addListener(map, 'zoom_changed', function() {
		currentZoomLevel = checkZoomLevel(this.zoom);
		
		//줌 레벨이 변경될 경우 기존에 열린 infowindow는 닫아줌
		if(tempInfoWindow != undefined) 
			tempInfoWindow.close();
		
		//circle이 보이는 줌 레벨보다 멀어질 경우 circleList 그림
		if(currentZoomLevel == 'circle') {
			cityClear('cities');
			hotspotClear();
			
			$("#map").hide();
			$(".circle-map").show();
			$("#mapDescriptArea").text('Click the area t to view the Cities');
//				drawServiceAreaByBmSc();
		} else if(currentZoomLevel == 'city') {
//			circleClear();
			hotspotClear();
			
			if(cities.length == 0) {
				drawServiceAreaByCity(upperCircle);
			}
		} else if(currentZoomLevel == 'hotspot') {
//			circleClear();
			cityClear('cities');
		}
	});
	
	//맵 클릭 이벤트 부여
	google.maps.event.addListener(map, 'click', function(event) {
	    var contentString = makeInfoWindow('add', event);
		
		//이전에 열린 infoWindow가 있을 경우 닫아줌
		if(tempInfoWindow != undefined)
			tempInfoWindow.close();
		
		var infowindow = new google.maps.InfoWindow({ content: contentString
						, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
		infowindow.open(map, this);
		tempInfoWindow = infowindow
	});
	
	// 처음 로딩 시 지도에 표시해주는 부분
//	drawServiceAreaByBmSc();
}

function makeInfoWindow(div, object) {
	var contentString = '';
	contentString = '<div>';
	contentString = '<form name="serviceAreaForm">'; 
	contentString += '<input type="hidden" name="proccessDiv">';
	contentString += '<input type="hidden" name="currentZoomLevel">';
	if(currentZoomLevel != 'circle') {
		contentString += '<input type="hidden" name="upper_said">';
		contentString += '<input type="hidden" name="upper_name">';
	}
	contentString += '<table>';
	contentString += '<tbody>';
	contentString += '<tr>';
	var upperChar = currentZoomLevel.substring(0,1).toUpperCase();
	contentString += '<td>' + upperChar + currentZoomLevel.substring(1) + '</td>';
	var name = '';
	if(div == 'edit') name = object.name;
	contentString += '<td colspan="2"><input type="text" style="width:100%" name="name" value="' + name + '"></td>';
	contentString += '</tr>';
	contentString += '<tr>';
	contentString += '<td>SAID</td>';
	var said = '';
	if(div == 'edit') said = object.said;
	contentString += '<td colspan="2"><input type="text" style="width:100%" name="said" value="' + said + '" ' + (div == "edit"? "readonly" : "") + ' onblur="validationCheck(\'number\', this)"></td>';
	contentString += '</tr>';
	if(currentZoomLevel != 'circle') {
		var bandwidth = '';
		if(div == 'edit') bandwidth = object.bandwidth;
		contentString += '<tr>';
		contentString += '<td>Bandwidth</td>';
		contentString += '<td colspan="2"><input type="text" style="width:100%" name="bandwidth" value="' + bandwidth + '" onblur="validationCheck(\'number\', this)"></td>';
		contentString += '</tr>';
	}
	contentString += '<tr>';
	contentString += '<td>Latitude</td>';
	var lat,lng;
	if(div == 'add') {
		lat =  object.latLng.lat();
		lng =  object.latLng.lng();
	}
	else if(div == 'edit') {
		if(object.center != undefined) {
			lat = object.center.lat();
			lng = object.center.lng();
		} else {
			lat = object.position.lat();
			lng = object.position.lng();
		}
	}
	contentString += '<td><input type="text" name="lat" value="' + lat + '" readonly></td>';
	if(div == 'edit')
		contentString += '<td rowspan="2"><button type="button" class="btn btn-success btn-xs button-edit" style="padding:6px;" onclick="callSetLocationModalMap(this, \'serviceArea\', \'' + currentZoomLevel + '\', ' + lat + ', ' + lng + ')">Reset<br>Location</button></td>';
	contentString += '</tr>';
	contentString += '<tr>';
	contentString += '<td>Longitude</td>';
	contentString += '<td><input type="text" name="lng" value="' + lng + '" readonly></td>';
	contentString += '</tr>';
	contentString += '</tbody>';
	contentString += '</table>';
	contentString += '</form>';
	contentString += '</div>';
	contentString += '<div style="text-align:center; margin-top:3px">';
	
	if(div == 'add') {
		contentString += '<button type="button" class="btn btn-success btn-xs button-edit" onclick="serviceAreaProccess(\'map\', \'add\', this)">Add</button>';
	} else if(div == 'edit') {
		contentString += '<button type="button" class="btn-white btn btn-xs" style="color: rgb(255, 255, 255); background-color: rgb(51, 122, 183);" onclick="serviceAreaProccess(\'map\', \'edit\', this)">Edit</button>';
		contentString += '<button type="button" class="btn-white btn btn-xs" style="color: rgb(255, 255, 255); background-color: rgb(237, 85, 101);" proccess-btn" onclick="serviceAreaProccess(\'map\', \'delete\', this)">Delete</button>';
	}
	
	contentString += '</div>';
	
	return contentString; 
}

//현재 줌레벨을 기준으로 어느 데이터를 보여줄지 판단하는 메소드
function checkZoomLevel(zoom) {
	var zoomLevel = '';
	
	//circle level
	if(zoom < 6) {
		zoomLevel = 'circle';
		map.setOptions({ maxZoom: 5 });
	} 
	//city level
	else if(zoom < 10) {
		zoomLevel = 'city';
		map.setOptions({ maxZoom: 9 });
	} 
	//hotspot level
	else {
		zoomLevel = 'hotspot';
		map.setOptions({ maxZoom: 20 });
	}
	
	return zoomLevel;
}

google.maps.event.addDomListener(window, 'load', initMap);

//function circleClear() {
//	for(var i=0; i < circles.length; i++) {
//		//서클 클리어
//		var tempCircle = circles[i];
//		tempCircle.setMap(null);
//	}
//	
//	circles = [];
//}

//도시 삭제(메인 페이지와 모달 페이지를 구분하여 삭제)
function cityClear(targetCity) {
	if(targetCity == 'cities') {
		for(var i=0; i < cities.length; i++) {
			var tempCity = cities[i];
			tempCity.setMap(null);
		}
		
		cities = [];
	} else {
		for(var i=0; i < modalCities.length; i++) {
			var tempCity = modalCities[i];
			tempCity.setMap(null);
		}
		
		modalCities = [];
	}
}

//핫스팟 삭제
function hotspotClear() {
	for(var i=0; i < hotspots.length; i++) {
		//서클 클리어
		var tempHotspot = hotspots[i];
		tempHotspot.setMap(null);
	}
	
	hotspots = [];
}

//변경된 서클 리스트를 가지고오는 메소드
//function getNewCircleList() {
//	$.ajax({
//	    url : "/dashbd/api/getNewCircleList.do",
//	    type: "POST",
//	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//	    success : function(responseData) {
//	    	$("#ajax").remove();
//	        var data = JSON.parse(responseData);
//	        
//	        circlemap = data;
//	        drawServiceAreaByBmSc();
//	    }, 
//	    error : function(xhr, status, error) {
//        	swal({
//                title: "Fail !",
//                text: "Error"
//            });
//        }
//	});
//}

//서클 리스트를 지도상에 그려주는 메소드
//function drawServiceAreaByBmSc() {
//	for (var circle in circlemap) {
//		//위도 경도를 숫자값으로 변경하여 셋팅
//		circlemap[circle].center.lat = Number(circlemap[circle].center.lat);
//		circlemap[circle].center.lng = Number(circlemap[circle].center.lng);
//		
//	    // Add the circle for this circle to the map.
//		var cityCircle = new google.maps.Circle({
//	      strokeColor: red,
//	      strokeOpacity: 0.8,
//	      strokeWeight: 2,
//	      fillColor: red,
//	      fillOpacity: 0.35,
//	      map: map,
//	      center: circlemap[circle].center,
//	      radius: Math.sqrt(circlemap[circle].population) * 100,
//	      said : circlemap[circle].circle_id,
//	      name : circlemap[circle].circle_name
//	    });
//	    
//	    cityCircle.addListener('click', function() {
//	    	//선택된 object를 전역변수에 담음(추후 추가시에 부모 노드로 사용)
//	    	upperObj = this;
//	    	drawServiceAreaByCity(this);
//		});
//	    
//	    cityCircle.addListener('rightclick', function(event) {
//	    	var contentString = makeInfoWindow('edit', this);
//			
//			//이전에 열린 infoWindow가 있을 경우 닫아줌
//			if(tempInfoWindow != undefined)
//				tempInfoWindow.close();
//			
//			var infowindow = new google.maps.InfoWindow({ content: contentString
//							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
//			infowindow.open(map, this);
//			tempInfoWindow = infowindow
//		});
//	    
//	    circles.push(cityCircle);
//	}
//}

//도시 리스트를 찍어주는 메소드
function drawServiceAreaByCity(circle) {
	$(".circle-map").hide();
	$("#map").show();
	
	google.maps.event.trigger(map, "resize");
	
	$.ajax({
	    url : "/dashbd/api/getCitiesInCircle.do",
	    type: "POST",
//	    async:false,
	    data : { 
	    	circle_id : circle.said
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        //도시 기본 줌 사이즈로 셋팅
	        map.setZoom(9);
	        //서클의 위도 경도로 이동
	        map.setCenter(new google.maps.LatLng(circle.lat, circle.lng));
	        
	        for (var city in data) {
	        	var cityCircle = new google.maps.Marker({
		  	  	      map: map,
		  	  	      position: data[city].center,
		  	  	      icon : {
		  	  	    	  url:'/dashbd/resources/img/icon/ico_number_1_3.png',
		  	  	    	  labelOrigin: new google.maps.Point(17, 17)
		  	  	      },
		  	  	      title: 'City Name : ' + data[city].city_name + "\nSAID : " + data[city].city_id,
			  	  	  said:data[city].city_id,
			  	  	  name:data[city].city_name,
			  	  	  bandwidth:data[city].bandwidth,
			  	  	  label: {
			  	  		  text: ''+ data[city].hotspotCnt,
			  	  		  fontSize: '14px',
			  	  	  }
		  	  	});
	        	
	        	//매인 맵에 city circle을 넣어줄 경우
//	        	if(targetCity == 'cities') {
	        		cities.push(cityCircle);
	        		
	        		cityCircle.addListener('click', function() {
	        			//선택된 object를 전역변수에 담음(추후 추가시에 부모 노드로 사용)
	        	    	upperObj = this;
	        			drawServiceAreaByHotspot(this);
	        		});
	        		
	        		cityCircle.addListener('rightclick', function(event) {
	        	    	var contentString = makeInfoWindow('edit', this);
	        			
	        			//이전에 열린 infoWindow가 있을 경우 닫아줌
	        			if(tempInfoWindow != undefined)
	        				tempInfoWindow.close();
	        			
	        			var infowindow = new google.maps.InfoWindow({ content: contentString
	        							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
	        			infowindow.open(map, this);
	        			tempInfoWindow = infowindow
	        		});
	        		
	        		//오른쪽을 클릭할 경우 infoWindow 뿌려줌 
//	        		cityCircle.addListener('rightclick', function() {
//	        			for(var i=0; i < $("#area_group tr").length; i++) {
//	        				var tempTr = $("#area_group tr")[i];
//	        				
//	        				if($(tempTr).attr("choiceYn") == 'Y') {
//	        					var contentString = '';
//	    	        			if(this.fillColor == 'gray' || this.fillColor == 'blue') {
//	    	        			   contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'add\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Add to ' + $(tempTr).find("td").text() + '</a>';
//	    	        			} else {
//	    	        				contentString = '<a href="javascript:void(0);" onclick="addAndDeleteCityInServiceGroup(\'delete\', \'' + $(tempTr).attr("data-init") + '\', \'' + this.city_id + '\')">Delete From ' + $(tempTr).find("td").text() + '</a>';
//	    	        			}
//	    	        			
//	    	        			//선택된 city obj를 전역변수에 담음(color변경을 위해)
//	    	        			tempCityObj = this;
//	    	        			
//	    	        			//이전에 열린 infoWindow가 있을 경우 닫아줌
//	    	        			if(tempInfoWindow != undefined)
//	    	        				tempInfoWindow.close();
//	    	        			
//	    	        			var infowindow = new google.maps.InfoWindow({ content: contentString
//    	        								, position: new google.maps.LatLng(this.center.lat(), this.center.lng()) });
//	    	        			infowindow.open(map, this);
//	    	        			tempInfoWindow = infowindow
//	        				}
//	        			}
//	        		});
//	        	}
	        	//팝업 맵에 city circle을 넣어줄 경우
//	        	else {
//	        		modalCities.push(cityCircle);
//	        		
//	        		//오른쪽 마우스 클릭 이벤트 적용
//	        		cityCircle.addListener('click', function() {
//	        			if(this.fillColor == 'gray') 	        				
//	        				this.setOptions({strokeColor:'red', fillColor:'red'})
//	        			else 
//	        				this.setOptions({strokeColor:'gray', fillColor:'gray'})
//	        		});
//	        	}
	        }
	        
	        var displayText = 'Current Area : ' + upperCircle.name.replace(' Telecom Circle', '') + '\n';
	        displayText += 'Number of Cities : ' + cities.length + '\n';
	        displayText += 'Click the city to view the hotspots.\n Click empty space to add a city';
	        $("#mapDescriptArea").text(displayText);
			$("#mapDescriptArea").html($("#mapDescriptArea").html().replace(/\n/g,'<br/>'));
	    },
        error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
}

//핫스팟 리스트를 찍어주는 메소드
function drawServiceAreaByHotspot(city) {
	$.ajax({
	    url : "/dashbd/api/getHotspotsInCities.do",
	    type: "POST",
	    async:false,
	    data : { 
	    	city_id : city.said
	    },
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success : function(responseData) {
	        $("#ajax").remove();
	        var data = JSON.parse(responseData);
	        
	        //도시 기본 줌 사이즈로 셋팅
	        map.setZoom(14);
	        //서클의 위도 경도로 이동
	        map.setCenter(new google.maps.LatLng(city.position.lat(), city.position.lng()));
	        
	        for (var hotspot in data) {
	        	var hotspotMarker = new google.maps.Marker({
	  	  	      map: map,
	  	  	      position: data[hotspot].center,
	  	  	      icon : '/dashbd/resources/img/icon/enb_blue_on.png',
	  	  	      title: 'Hot Spot Name : ' + data[hotspot].hotspot_name + '\nSAID : ' + data[hotspot].hotspot_id,
		  	  	  said:data[hotspot].hotspot_id,
		  	  	  name:data[hotspot].hotspot_name,
		  	  	  bandwidth:data[hotspot].bandwidth
	  	  	    });
	        	
	        	hotspotMarker.addListener('rightclick', function(event) {
	    	    	var contentString = makeInfoWindow('edit', this);
	    			
	    			//이전에 열린 infoWindow가 있을 경우 닫아줌
	    			if(tempInfoWindow != undefined)
	    				tempInfoWindow.close();
	    			
	    			var infowindow = new google.maps.InfoWindow({ content: contentString
	    							, position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()) });
	    			infowindow.open(map, this);
	    			tempInfoWindow = infowindow
	    		});
	        	
	        	hotspots.push(hotspotMarker);
	        }
	        
	        var displayText = 'Current City : ' + upperObj.name + '\n';
	        displayText += 'Number of Hotspotes : ' + hotspots.length + '\n';
	        displayText += 'Click empty space to add a hotspot';
	        $("#mapDescriptArea").text(displayText);
			$("#mapDescriptArea").html($("#mapDescriptArea").html().replace(/\n/g,'<br/>'));
	    },
	    error : function(xhr, status, error) {
        	swal({
                title: "Fail !",
                text: "Error"
            });
        }
	});
}

//circle, city, hotspot을 추가, 수정, 삭제할 경우 사용하는 메소드
function serviceAreaProccess(tabDiv, div, treeBtn) {
	//필수값이 모두 입력되어 있을 경우에는 이 값을 변경하지 않아 ajax 수행
	var ajaxYn = true;
	// 서버단으로 던지는 데이터셋
	var sendData;
	var alertMsg;
	
	//Map에서 추가 수정 삭제가 일어날 경우
	if(tabDiv == 'map') {
		//form에 셋팅
		$("form[name='serviceAreaForm'] input[name='proccessDiv']").val(div); 
		$("form[name='serviceAreaForm'] input[name='currentZoomLevel']").val(currentZoomLevel);
		
		if(currentZoomLevel != 'circle') {
			if(currentZoomLevel == 'city') {
				//city, hotspot 마냥 상위 노드가 필요할 경우에만 셋팅
				if(upperCircle != undefined) {
					$("form[name='serviceAreaForm'] input[name='upper_said']").val(upperCircle.said);
					$("form[name='serviceAreaForm'] input[name='upper_name']").val(upperCircle.name);
				}
			} else if(currentZoomLevel == 'hotspot') {
				if(upperObj != undefined) {
					$("form[name='serviceAreaForm'] input[name='upper_said']").val(upperObj.said);
					$("form[name='serviceAreaForm'] input[name='upper_name']").val(upperObj.name);
				}
			}
		}
		
		$.each($("form[name='serviceAreaForm'] input"), function(index, obj) {
			if($(obj).val() == '') {
				$(obj).focus();
				ajaxYn = false;
				return false;
			}
		});
		
		if(ajaxYn) {
			sendData = $("form[name='serviceAreaForm']").serialize();
			alertMsg = "You are now " + treeBtn.innerHTML.toLowerCase() +"ing the \"" + $("form[name='serviceAreaForm'] input[name='name']").val() + "\" as the " + currentZoomLevel.substring(0,1).toUpperCase() + currentZoomLevel.substring(1) + " of \"" 
			+ $("form[name='serviceAreaForm'] input[name='upper_name']").val()
			+ "\" at the \"" + $("form[name='serviceAreaForm'] input[name='lat']").val() + "\",\"" + $("form[name='serviceAreaForm'] input[name='lng']").val() + "\". Continue?";
		}
	} 
	//Tree에서 추가 수정 삭제가 일어날 경우
	else if(tabDiv == 'tree') {
		var objLevel = $($(treeBtn).parents("li")[0]);
		if(objLevel.hasClass('circle')) objLevel = 'circle';
		else if(objLevel.hasClass('city')) objLevel = 'city';
		else if(objLevel.hasClass('hotspot')) objLevel = 'hotspot';
		
		var blankObjName;
		
		if(treeBtn.innerHTML.toLowerCase() == 'add' && $($(treeBtn).parents("li")[0]).find("input[name='name']").val() == '') {
			blankObjName = 'name';
			$($(treeBtn).parents("li")[0]).find("input[name='name']").focus();
		}
		
		$.each($($(treeBtn).parents("div[name='customDiv']")).find("input"), function(index, obj) {
			if($(obj).val() == '') {
				if(obj.name == 'lat') blankObjName = 'latitude';
				else if(obj.name == 'lng') blankObjName = 'longitude';
				else blankObjName = obj.name;
				$(obj).focus();
				ajaxYn = false;
				return false;
			}
		});
		
		if(ajaxYn) {
			var name;
			
			if(treeBtn.innerHTML.toLowerCase() == 'add') {
				name = $($(treeBtn).parents("li")[0]).find("input[name='name']")[0].value;
			} else {
				name = $($($(treeBtn).parents("li")[0]).find("a")[0]).text();
			}
			
			sendData = {
				'proccessDiv' : treeBtn.innerHTML.toLowerCase(),
				'currentZoomLevel' : objLevel,
				'said' : $(treeBtn).parent().siblings("input[name='said']").val(),
				'name' : name,
				'lat' : $(treeBtn).parent().siblings("input[name='lat']").val(),
				'lng' : $(treeBtn).parent().siblings("input[name='lng']").val(),
				'bandwidth' : $(treeBtn).parent().siblings("input[name='bandwidth']").val(),
				'upper_said' : ($($(treeBtn).parents("li")[0]).hasClass("circle"))? '' : $($(treeBtn).parents("li")[1]).find("input[name='said']")[0].value,
				'upper_name' : ($($(treeBtn).parents("li")[0]).hasClass("circle"))? '' : $($($(treeBtn).parents("li")[1]).find("a")[0]).text()
			}
			
			alertMsg = "You are now " + treeBtn.innerHTML.toLowerCase() +"ing the \"" + name + "\" as the " + objLevel.substring(0,1).toUpperCase() + objLevel.substring(1) + " of \"" + $($($(treeBtn).parents("li")[1]).find("a")[0]).text()
			+ "\" at the \"" + $(treeBtn).parent().siblings("input[name='lat']").val() + "\",\"" + $(treeBtn).parent().siblings("input[name='lng']").val() + "\". Continue?";
		}
	}
	
	if(ajaxYn) {
		swal({
			  title: "Are you sure?",
			  text: alertMsg,
			  type: "warning",
			  showCancelButton: true,
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "Yes",
			  closeOnConfirm: false
			},
			function(){
				$.ajax({
				    url : "/dashbd/api/serviceAreaProccess.do",
				    type: "POST",
				    data : sendData,
				    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				    success : function(responseData) {
				        $("#ajax").remove();
				        var data = JSON.parse(responseData);
				        
				        if(data.resultCode == 'S') {
				        	swal({title:"Success !", text:"Success", type:"success"}, function() {
				        		if(tabDiv == 'map') {
					        		if(currentZoomLevel == 'circle') {
					        			circleClear();
//						        			getNewCircleList();
					        			google.maps.event.trigger(map, "resize");
					        			map.setCenter( new google.maps.LatLng( default_center_lat, default_center_lng ) );
						        	} else if(currentZoomLevel == 'city') {
						        		tempInfoWindow.close(); //InfoWindow 닫아줌
						        		cityClear('cities');
						        		drawServiceAreaByCity(upperCircle);
						        	} else if(currentZoomLevel == 'hotspot') {
						        		tempInfoWindow.close(); //InfoWindow 닫아줌
						        		hotspotClear();
						        		drawServiceAreaByHotspot(upperObj);
						        	}
					        	} 
					        	//tree의 경우 add되거나 delete될 때 해당 노드를 지워주거나 추가해줘야 함
					        	else if(tabDiv == 'tree') {
					        		if(treeBtn.innerHTML.toLowerCase() == 'add') {
					        			jsTreeSetting(false);
					        		} else if(treeBtn.innerHTML.toLowerCase() == 'delete') {
					        			$($(treeBtn).parents("li")[0]).remove();
					        		}
					        		
//						        		circleClear();
//						        		getNewCircleList();
					        	}
							})
				        } 
				        else if(data.resultCode == 'E') {
				        	swal({
				                title: "Exist Code Value",
				                text: "Exist SAID Code Value"
				            });
				        }
				        else {
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
			});
	} else {
		swal({
          title: "Fail !",
          text: "Please Insert the " + blankObjName + " of " + objLevel
        });
	}
}