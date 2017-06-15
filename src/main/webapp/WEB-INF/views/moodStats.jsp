<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Flot Charts</title>

    <link href="/dashbd/resources/newPublish/css/bootstrap.min.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/dashbd/resources/newPublish/css/animate.css" rel="stylesheet">
    <link href="/dashbd/resources/newPublish/css/style.css" rel="stylesheet">

</head>

<body>

    <div id="wrapper">

        <div id="page-wrapper" class="gray-bg" style="min-height: 100px !impotant;">
        <div class="row border-bottom">

        </div>
        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>Bar Chart Example <small>With custom colors.</small></h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                <i class="fa fa-wrench"></i>
                            </a>
                            <ul class="dropdown-menu dropdown-user">
                                <li><a href="#">Config option 1</a>
                                </li>
                                <li><a href="#">Config option 2</a>
                                </li>
                            </ul>
                            <a class="close-link">
                                <i class="fa fa-times"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content">
	                    <div class="flot-chart">
	                        <div class="flot-chart-content" id="flot-bar-chart"></div>
	                    </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5><span id="serviceId"></span>Mood History</h5>
                    </div>
                    <div class="ibox-content">
						<div id="legend-area" align="right">
                    	</div>
                        <div class="flot-chart">
                            <div class="flot-chart-content" id="flot-line-chart"></div>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            <div class="row">
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Pie Chart Example</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-user">
                                    <li><a href="#">Config option 1</a>
                                    </li>
                                    <li><a href="#">Config option 2</a>
                                    </li>
                                </ul>
                                <a class="close-link">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="flot-chart">
                                <div class="flot-chart-pie-content" id="flot-pie-chart"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Live Chart Example</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-user">
                                    <li><a href="#">Config option 1</a>
                                    </li>
                                    <li><a href="#">Config option 2</a>
                                    </li>
                                </ul>
                                <a class="close-link">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">

                            <div class="flot-chart">
                                <div class="flot-chart-content" id="flot-line-chart-moving"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Multiple Axes Line Chart Example </h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-user">
                                    <li><a href="#">Config option 1</a>
                                    </li>
                                    <li><a href="#">Config option 2</a>
                                    </li>
                                </ul>
                                <a class="close-link">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="flot-chart">
                                <div class="flot-chart-content" id="flot-line-chart-multi"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



    <!-- Mainly scripts -->
    <script src="/dashbd/resources/newPublish/js/jquery-2.1.1.js"></script>
    <script src="/dashbd/resources/newPublish/js/bootstrap.min.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- Flot -->
    <script src="/dashbd/resources/newPublish/js/plugins/flot/jquery.flot.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/flot/jquery.flot.resize.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/flot/jquery.flot.pie.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/flot/jquery.flot.time.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/dashbd/resources/newPublish/js/inspinia.js"></script>
    <script src="/dashbd/resources/newPublish/js/plugins/pace/pace.min.js"></script>

    <!-- Flot demo data -->
    <script src="/dashbd/resources/newPublish/js/demo/flot-demo.js"></script>

</body>

<script type="text/javascript">

	var moodJson = ${moodData};

	$(document).ready(function(){
		$('#page-wrapper').css("min-height", "100px");
		for (var i = $(".close-link").length-1; i > -1; i--) {
			$($(".close-link")[i]).click();
		}
		drawGraph();
		setLegend();
	});
	
	function setLegend() {
		$($(".legendColorBox").children()[0]).css({"border": "0px", "padding": "1px"});
		$($(".legendColorBox").children()[1]).css({"border": "0px", "padding": "1px"});
		$($(".legendColorBox").children().children()[0]).css({"width": "10px", "border": "1px solid rgb(0,205,255)"});
		$($(".legendColorBox").children().children()[1]).css({"width": "10px", "border": "1px solid rgb(244,164,96)"});
		$($(".legendColorBox")[0]).before($($(".legendLabel")[0]));
		$($(".legendColorBox")[1]).before($($(".legendLabel")[1]));
		$($(".legendLabel")[0]).append("&nbsp;");
		$($(".legendColorBox")[0]).after('<td class="background"><div style="border:1px solid #ccc;padding:1px"><div style="width: 8px; height: 0px; border: 4px solid rgb(255, 255, 255); overflow: hidden;"></div></div></td>')
		$($(".legendColorBox")[1]).after('<td class="background"><div style="border:0px solid #ccc;padding:1px"><div style="width: 10px; height: 0px; border: 5px solid rgb(170, 170, 170); overflow: hidden;"></div></div></td>')
	}
	
	function drawGraph() {
	    var barOptions = {
	    	series: {
	    		// Data 선
	            lines: {
	                show: true,
	                lineWidth: 1,
	                fill: true,
	                fillColor: {
	                    colors: [{
	                        opacity: 0.0
	                    }, {
	                        opacity: 0.0
	                    }]
	                }
	            }
	        },
	        xaxis: {
	            mode: "time"
// 	            tickColor: "#123123"				//X축 선 컬러
	        },
	        colors: ["#1ab394"],
	        grid: {
	            color: "#999999",
	            hoverable: true,
	            clickable: true,
	            tickColor: "#D4D4D4",
	            backgroundColor: "#F9FFFF",
	            borderWidth:0,
	            markings: function (axes) {
	                var markings = [];
	                for (var i = 0; i < moodJson.length-1; i++) {
	                	var tempValFrom = moodJson[i].insertDate.split(":");
	                	var tempValTo = moodJson[i+1].insertDate.split(":");
	                	var begin = Date.UTC(9999,12,31,tempValFrom[0], tempValFrom[1], tempValFrom[2]);
	                	var end = Date.UTC(9999,12,31,tempValTo[0], tempValTo[1], tempValTo[2]);
	                	if(moodJson[i].mode == "Unicast"){
	                		markings.push({ xaxis: { from: begin, to: end}, color: "#aaaaaa"});
	                	}
	                	else
	                	{
	                		markings.push({ xaxis: { from: begin, to: end}, color: "#FFFFFF"});
	                	}
	                }
	                return markings;
	            }
	        },
	        legend: {
	            show: true,
	            container: $("#legend-area"),
	            position: "ne"
	        },
	        tooltip: true,
	        tooltipOpts: {
	            content: "x: %x, y: %y"
	        },
	        colors: ["#00CDFF", "#F4A460"]				//라인 색상
	    };
	    
	    var countUC = [];
	    var countBC = [];
	    
	    if(moodJson.length < 2){
	    	alert("the value of data is not exsit!");
	    	window.close();
	    }
	    
	    for (var i = 0; i < moodJson.length; i++) {
	    	var tempVal = moodJson[i].insertDate.split(":")
	    	countUC[i] = [Date.UTC(9999,12,31,tempVal[0], tempVal[1], tempVal[2]), moodJson[i].countUC];
	    	countBC[i] = [Date.UTC(9999,12,31,tempVal[0], tempVal[1], tempVal[2]), moodJson[i].countBC];
		}
	    
	    var ucData = {
	        label: "UC",
	        data: countUC
	    };
	    var bcData = {
	        label: "BC",
	        data: countBC
	    };
	    $.plot($("#flot-line-chart"), [ucData, bcData], barOptions);
	}
</script>

</html>
