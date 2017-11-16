<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

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
        var jsonyD2 = [];
        $.ajax({
            url: 'chart',
            cache: false,
            async: false,
            success: function(data) {
                var json = eval("(" + data + ")");
                if (json.Rows.length > 0) {
                    for (var i = 0; i < json.Rows.length; i++) {
                       	var jsonyD1 = [];
                        var rows = json.Rows[i];
                        var Time = rows.time;
                        var SumCount = rows.sumCount;
                        jsonyD1.push(Time);
                        jsonyD1.push(parseInt(SumCount));
                        jsonyD2.push(jsonyD1);
                    } //for
                    var chart;
                    chart = new Highcharts.Chart({
                        chart: {
                            renderTo: 'container',
                            plotBackgroundColor: null,
                            plotBorderWidth: null,
                            plotShadow: false
                        },
                        title: {
                            text: '数据饼状图表'
                        },
                        tooltip: {
                            formatter: function() {
                                return '<b>' + this.point.name + '</b>: ' + this.percentage.toFixed(2) + ' %';
                            }
                        },
                        plotOptions: {
                            pie: {
                                allowPointSelect: true,
                                cursor: 'pointer',
                                dataLabels: {
                                    enabled: true,
                                    color: '#000000',
                                    connectorColor: '#000000',
                                    formatter: function() {
                                        return '<b>' + this.point.name + '</b>: ' + this.percentage.toFixed(2) + ' %';
                                    }
                                }
                            }
                        },
                        series: [{
                            type: 'pie',
                            name: 'pie',
                            data:   jsonyD2
                        }]
                    });
                }
            }
        });
    });
})(jQuery);
</script>
<body>
	<div style="margin: 0 1em">
		<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
	</div>
</body>
</html>