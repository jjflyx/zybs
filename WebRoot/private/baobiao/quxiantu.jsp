%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<title>图表统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="script/jquery-1.4.2.min.js"></script>
<script src="script/highcharts.js"></script>
<script type="text/javascript">
(function($) { // encapsulate jQuery
    $(document).ready(function() {
        var jsonXData = [];
        var jsonyD1 = [];
        var jsonyD2 = [];
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
                            renderTo: 'container',
                            type: 'line',
                            marginRight: 220,
                            marginBottom: 25
                        },
                        title: {
                            text: '数据曲线图表',
                            x: -20 //center
                        },
                        xAxis: { //X轴数据
                            categories: jsonXData
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
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'top',
                            x: -20,
                            y: 200,
                            borderWidth: 0
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
})(jQuery);
</script>
<body>
	<div style="margin: 0 2em">
		<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
	</div>
</body>
</html>