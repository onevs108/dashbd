<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<link href="../resourcesRenew/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
	
	<!-- FooTable -->
	<link href="../resourcesRenew//css/plugins/footable/footable.core.css" rel="stylesheet">
	
	<jsp:include page="common/head.jsp" />
	
	<style type="text/css">
	.labels {
		color: red;
		background-color: white;
		font-family: "Lucida Grande", "Arial", sans-serif;
		font-size: 12px;
		font-weight: bold;
		text-align: center;
		width: 40px;     
		border: 1px solid red;
		white-space: nowrap;
	}
	.timeline {
	     position: absolute;    
	     border: 2px dotted blue;
	     width: 1px;
	     margin: 0;
	     padding: 0;
	     z-index: 9;
	     height: auto;
 	}
 	.custom-table{
 	}
 	.custom-table .checkbox label::before {
	    content: "";
	    display: inline-block;
	    position: absolute;
	    width: 17px;
	    height: 17px;
	    left: 0;
	    margin-left: -20px;
	    border: 1px solid #cccccc;
	    border-radius: 50%;
	    background-color: #fff;
	    -webkit-transition: border 0.15s ease-in-out;
	    -o-transition: border 0.15s ease-in-out;
	    transition: border 0.15s ease-in-out;
	}
	.custom-table .checkbox label::after {
	    display: inline-block;
	    position: absolute;
	    content: " ";
	    width: 11px;
	    height: 11px;
	    left: 3px;
	    top: 3px;
	    margin-left: -20px;
	    border-radius: 50%;
	    background-color: #555555;
		-webkit-transform: scale(.6, .6);
		-ms-transform: scale(.6, .6);
		-o-transform: scale(.6, .6);
		transform: scale(.6, .6);
		-webkit-transition: -webkit-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		-moz-transition: -moz-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		-o-transition: -o-transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
		transition: transform border-radius 0.1s cubic-bezier(0.8, -0.33, 0.2, 1.33);
	}
	.custom-table input[type="checkbox"] + label::after {
	    background-color: #d9534f;

	    border-radius:0;
	}
	.custom-table input[type="checkbox"]:checked + label::after {
		background-color: #0066FF;
		border-radius:50%;
		-webkit-transform: scale(1, 1);
		-ms-transform: scale(1, 1);
		-o-transform: scale(1, 1);
		transform: scale(1, 1);
	}
	.checkbox input[type="checkbox"]:checked + label::after {
		content:"";
	}
	.checkbox input[type="checkbox"]:checked + label::after, .checkbox input[type="radio"]:checked + label::after {
	    font-family: "FontAwesome";
	    content: "";
	}
 	.checkbox input[type="checkbox"]:checked + label::after, .checkbox input[type="radio"]:checked + label::after{
 	}
 	
 	
 	
 	/* CSS used here will be applied after bootstrap.css */
		.big-chart {
		  width:100%;
		  height:220px;
		}
    </style>
</head>
<body>
<div id="wrapper">
	<jsp:include page="common/leftTab.jsp" />
	<div id="page-wrapper" class="gray-bg">
		<c:import url="/resources/header.do"></c:import>
		<div class="wrapper wrapper-content">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
					        <h5>System Config</h5>
					        <div class="ibox-tools">
					            <a class="collapse-link">
					                <i class="fa fa-chevron-up"></i>
					            </a>
					        </div>
					    </div>
					    <div class="ibox-content">
					    	<div class="row">
					            <div class="col-sm-12" style="width:100%">
					                <table class="table2 custom-table" style="text-align: center;width:100%;">
					                	<thead>
					                		<tr height="70px;">
												<th width="20%"></th>
												<th width="15%" style="text-align: center;">Active Server</th>
												<th width="20%"></th>
												<th width="15%" style="text-align: center;">Standby Server</th>
												<th width="20%"></th>
											</tr>
					                	</thead>
										<tbody>
											<tr>
												<td width="20%"></td>
												<td width="10%" id="appView1" style="text-align:center;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-center" style="height:20px;">Tomcat Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="tomCheck1" name="tomCheck1" value="1" onclick="checkTomcat(1);" disabled/>
														<label></label>
													</div>
													</h4>
													<br/><br/>
													<img src="img/mysql2.png" width="50px">
													<h4 class="text-center" style="height:20px;">MySQL Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="dbCheck1" name="dbCheck1" value="1" onclick="checkDatabase(1);" disabled/>
														<label></label>
													</div>
													</h4>
												</td>
												<td width="20%">
													<div id="viewApp1">
														Turn Over <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer(2, 1)">▷▶</button>
													</div>
													<div id="viewApp2">
														Turn Over <br><br>
														<button class="btn btn-sm button-edit" type="button" onclick="moveActiveServer(1, 2)">◀◁</button>
													</div>
												</td>
												<td width="10%" id="appView2" style="text-align:center;">
													<img src="img/server_network.png" width="50px">
													<h4 class="text-center" style="height:20px;">Tomcat Status
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="tomCheck2" name="tomCheck2" value="2" onclick="checkTomcat(2);" disabled/>
														<label></label>
													</div>
													</h4>
													<br/><br/>
													<img src="img/mysql2.png" width="50px">
													<h4 class="text-center" style="height:20px;">MySQL Status 
													<div class="checkbox checkbox-inline">
														<input type="checkbox" id="dbCheck2" name="dbCheck2" value="2" onclick="checkDatabase(2);" disabled/>
														<label></label>
													</div>
													</h4>
												</td>
												<td width="20%"></td>
											</tr>
										</tbody>
									</table>
									<div class="hr-line-dashed"></div>
									<div>
										<div class="container">
										  <div class="col-xs-6 col-xs-offset-3">
										    <h4 class="text-left">CPU<span id="finalCpu" style="margin-left:30px"></span></h4>
										    <div id="chart1" class="big-chart"></div>
										  </div>
										</div>
									</div>
									<div style="margin-top:40px">
										<div class="container">
										  <div class="col-xs-6 col-xs-offset-3">
										    <h4 class="text-left">Memory<span id="finalMemory" style="margin-left:30px"></span></h4>
										    <div id="chart2" class="big-chart"></div>
										  </div>
										</div>
									</div>
									<div style="margin-top:40px">
										<div class="container">
										  <div class="col-xs-6 col-xs-offset-3">
										  	<h4 class="text-left">HDD<span id="finalMemory" style="margin-left:30px"></span></h4>
										  	<div id="hddStr" style="font-size: 18px;"></div>
										  </div>
										</div>
									</div>
					            </div>
					         </div>
				        </div>
					</div>
				</div>
			</div>
		</div>	
	</div><!-- end page-wrapper -->
</div><!-- end wrapper -->

<script src="js/plugins/chartist/chartist.min.js"></script>
	<script src="/dashbd/resources/app-js/apps/svc_systemConf.js"></script>
<!-- 	<script src="/dashbd/resources/js/common.js"></script> -->
	
	<!-- FooTable -->
    <script src="../resources/js/plugins/footable/footable.all.min.js"></script>
	<script type="text/javascript">
		var plot;
	    var plot2;
	    var updateInterval = 10000;
	
		$(document).ready(function() {
			$('.footable').footable();
			$('.footable2').footable();
			getMenuList('SYSTEM_CONF_MGMT');
			
			if('${sessionCntsessionHostNameCnt}' == 'nexdream'){
				
			}else{
				$("#viewApp1").show();
				$("#viewApp2").hide();
				$("#appView1").css("opacity", "1");
				$("#appView2").css("opacity", "0.6");
				
				$('#tomCheck1').prop("checked", true);
				$('#dbCheck1').prop("checked", true);
				$('#tomCheck1').removeAttr("disabled");
				$('#dbCheck1').removeAttr("disabled");
				
				$('#tomCheck2').attr("disabled", true);
				$('#dbCheck2').attr("disabled", true);
				$("#tomCheck2").prop("checked",false);
				$("#dbCheck2").prop("checked",false);
			}
			
			 getChartData();
		});
			
		function getChartData() {
			$.ajax({
			    url : "/dashbd/api/getSystemLogData.do",
			    type: "POST",
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			    success : function(responseData) {
			        $("#ajax").remove();
			        var data = JSON.parse(responseData);
			        
			        var cpuLogArray = new Array();
			        for(var i=0; i < data.cupResultList.length; i++) {
			        	var tempVal = data.cupResultList[i][0].split(":");
			        	cpuLogArray.push({
			        		0 : Date.UTC(9999,12,31,tempVal[0], tempVal[1], tempVal[2]),
			        		1 : data.cupResultList[i][1]
			        	})
			        }
			        
			        var memoryLogArray = new Array();
					for(var i=0; i < data.memoryResultList.length; i++) {
						var tempVal = data.memoryResultList[i][0].split(":");
						memoryLogArray.push({
			        		0 : Date.UTC(9999,12,31,tempVal[0], tempVal[1], tempVal[2]),
			        		1 : data.memoryResultList[i][1]
			        	})
			        }
					
					var hddLogArray = new Array();
					var hddInfoStr ="<table style='width:800px;'>";
					for(var i=0; i < data.hddResultList.length; i++) {
						var tempVal = data.hddResultList[i];
						hddInfoStr += "<tr>";
						if(i==0) {
							for(var j=1; j < 7; j++) {
								hddInfoStr += "<th>" + tempVal[j] + "</th>";
							}
						} else {
							for(var j=1; j < 7; j++) {
								hddInfoStr += "<td>" + tempVal[j] + "</td>";
							}
						}
						hddInfoStr += "</tr>";
					}
					hddInfoStr += "</table>";
			        
			        $("#finalCpu").text(data.finalCpu + "%");
			        $("#finalMemory").text(data.finalMemory + "%");
			        $("#hddStr").empty();
			        $("#hddStr").append(hddInfoStr);
			        
			        $.getScript('//cdnjs.cloudflare.com/ajax/libs/flot/0.8.2/jquery.flot.min.js',function(){
			        	$.getScript('//cdnjs.cloudflare.com/ajax/libs/flot/0.8.2/jquery.flot.time.min.js',function(){
				    	  // setup plots
					      var options = {
					        grid:{borderColor:'#ccc'},
					        series:{shadowSize:0,color:"#33ff33"},
					        yaxis:{min:0,max:100},
					        xaxis:{
					        	show:true,
					        	mode:"time"
					        }
					      };
				    	  
				    	  plot = $.plot($("#chart1"), [ cpuLogArray ], options);
					      plot2 = $.plot($("#chart2"), [ memoryLogArray ], options);
					      
					      setTimeout(function() {
					    	  getChartData();
					      }, updateInterval);
			        	});
					});// end getScript
			    },
		        error : function(xhr, status, error) {
		        	swal({
		                title: "Fail !",
		                text: "Error"
		            });
		        }
			});
		}
</script>

</body>
</html>
