<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <script src="script/jquery-1.4.2.min.js" type="text/javascript"></script>
    <script src="script/highcharts.js" type="text/javascript"></script>
    <script src="script/exporting.js" type="text/javascript"></script>
</head>
<body>
    <form id="form1">
		<div>
				<script type="text/javascript">
				/*获取json数据开始*/
				//定义变量
				$(document).ready(function() {
				    var jsonXData = [];
				    var jsonyD1 = [];
				    var jsonyD2 = [];
				
				    //获取数据
				    $.ajax({
				        url: 'chart',
				        cache: false,
				        async: false,
				        success: function(data) {
				            var json = eval("(" + data + ")");
				            if (json.Rows.length > 0) {
				                for (var i = 0; i < json.Rows.length; i++) {
				                    var rows = json.Rows[i];
				                    var Time = rows.time;
				                    var SumCount = rows.sumCount;
				                    var IpCount = rows.ipCount;
				                    jsonXData.push(Time); //赋值
				                    jsonyD1.push(parseInt(SumCount));
				                    jsonyD2.push(parseInt(IpCount));
				                } //for
				                var chart;
				                chart = new Highcharts.Chart({
				                    chart: {
				                        renderTo: 'containerliuliang',
				                        //放置图表的容器
				                        plotBackgroundColor: null,
				                        plotBorderWidth: null,
				                        defaultSeriesType: 'column' //图表类型line, spline, area, areaspline, column, bar, pie , scatter 
				                    },
				                    title: {
				                        text: 'JQuery柱状图演示'
				                    },
				                    xAxis: { //X轴数据
				                        categories: jsonXData,
				                        lineWidth: 2,
				                        labels: {
				                            align: 'right',
				                            style: {
				                                font: 'normal 13px 宋体'
				                            }
				                        }
				                    },
				                    yAxis: { //Y轴显示文字
				                        lineWidth: 2,
				                        title: {
				                            text: '浏览量/次'
				                        }
				                    },
				                    tooltip: {
				                        formatter: function() {
				                            return '<b>' + this.x + '</b><br/>' + this.series.name + ': ' + Highcharts.numberFormat(this.y, 0);
				                        }
				                    },
				                    plotOptions: {
				                        column: {
				                            dataLabels: {
				                                enabled: true
				                            },
				                            enableMouseTracking: true //是否显示title
				                        }
				                    },
				                    series: [{
				                        name: '总流量',
				                        data: jsonyD1
				                    },
				                    {
				                        name: 'IP流量',
				                        data: jsonyD2
				                    }]
				                });
				                //$("tspan:last").hide(); //把广告删除掉
				            } //if
				        }
				    });
				});      
				</script>
            <div id="containerliuliang">
            </div>
        </div>
    </form>
</body>
</html>

