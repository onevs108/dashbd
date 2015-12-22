
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
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<option><a href="javascript:getServiceAreaByBmSc(1, ' + data[i].id + ');">' + data[i].name + '</a></option>';
            }

            $("#service_area").empty();
            $("#bmsc").empty();
            $("#bmsc").append(options);
        }
    });
}

// operator 조회
function getServiceAreaOperator(page)
{
	$.ajax({
        url : "/dashbd/api/getServiceAreaOperator.do",
        type: "get",
        data : { "page" : page },
        success : function(responseData){
            $("#ajax").remove();
            var data = JSON.parse(responseData);
            var dataLen = data.length;
            var options = "";
            for(var i=0; i<dataLen; i++){
            	options += '<li><a href="javascript:getServiceAreaBmSc(1, ' + data[i].id + ');">' + data[i].name + '</a></li>';
            }

            $("#operator").empty();
            $("#operator").append(options);
        }
    });
}


var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 37.489052, lng: 127.066331},
    zoom: 8
  });
  
  var enb_datas;

  $.ajax({
          url : "/dashbd/api/getServiceAreaEnbAp.do",
          type: "get",
          data : { "serviceAreaId" : getParameter('serviceAreaId') },
          success : function(responseData){
              $("#ajax").remove();
              enb_datas = JSON.parse(responseData);
              var dataLen = enb_datas.length;
              var options = "";
              var idx = 0;
              for(var i=0; i<dataLen; i++){
            	  if( enb_datas[i].serviceAreaId == getParameter('serviceAreaId') ) {
            		  //options += '<li><a href="/dashbd/resources/serviceAreaMgmt.do?serviceAreaId=' + enb_datas[i].enbApId + '">' + enb_datas[i].enbApName + '</a></li>';
            		  options += '<li><a href="javascript:moveToEnb(' + enb_datas[i].latitude + ', ' + enb_datas[i].longitude + ');">' + enb_datas[i].enbApName + '</a></li>';
            	  }
              }

              $("#enb_ap_in_service_area").empty();
              $("#enb_ap_in_service_area").append(options);
              
              var markers = [];
              
              for (var i = 0; i < enb_datas.length; i++) {
                var latLng = new google.maps.LatLng(enb_datas[i].latitude, enb_datas[i].longitude);
                var marker;
                if( enb_datas[i].serviceAreaId == getParameter('serviceAreaId') ) {
                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
                } else if( enb_datas[i].serviceAreaId == '' ) {
                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
                } else {
                	marker = new google.maps.Marker({'position': latLng, map: map, icon : '/dashbd/resources/img/icon/enb_red.png'});
                }
                
                google.maps.event.addListener(marker, 'click', function() {
                	   if (!selecting) return;
                	   var id = this.id;
                	   var index = selectedMarkers.indexOf(id);
                	   if (index>-1) {
                	     this.setIcon('https://maps.gstatic.com/mapfiles/ms2/micons/red-dot.png');
                	     selectedMarkers.splice(index, 1);
                	   } else {
                	     selectedMarkers.push(id);             
                	     this.setIcon('https://maps.gstatic.com/mapfiles/ms2/micons/blue-dot.png');
                	   }
                	});
                
                //markers.push(marker);
              }
              
              
              //var mcOptions = {gridSize: 0, averageCenter:false};
              //var markerCluster = new MarkerClusterer(map, markers, mcOptions);
              //alert(markerCluster.getGridSize());
          }
      });
  
}