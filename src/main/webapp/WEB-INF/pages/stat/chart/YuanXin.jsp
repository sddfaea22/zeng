<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
       <link rel="stylesheet" href="${pageContext.request.contextPath }/components/newamChart/style.css" type="text/css">
        <script src="${pageContext.request.contextPath }/components/newamChart/amcharts/amcharts.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/newamChart/amcharts/pie.js" type="text/javascript"></script>

        <script>
            var chart;
            var chartData =${jsonData2}
            /* var chartData = [
                {
                    "country": "United States",
                    "visits": 9252
                },
                {
                    "country": "China",
                    "visits": 1882
                },
                {
                    "country": "Japan",
                    "visits": 1809
                },
                {
                    "country": "Germany",
                    "visits": 1322
                },
                {
                    "country": "United Kingdom",
                    "visits": 1122
                },
                {
                    "country": "France",
                    "visits": 1114
                },
                {
                    "country": "India",
                    "visits": 984
                },
                {
                    "country": "Spain",
                    "visits": 711
                }
            ]; */


            AmCharts.ready(function () {
                // PIE CHART
                chart = new AmCharts.AmPieChart();

                // title of the chart
                chart.addTitle("Visitors countries", 16);

                chart.dataProvider = chartData;
                chart.titleField = "country";
                chart.valueField = "visits";
                chart.sequencedAnimation = true;
                chart.startEffect = "elastic";
                chart.innerRadius = "30%";
                chart.startDuration = 2;
                chart.labelRadius = 15;
                chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
                // the following two lines makes the chart 3D
                chart.depth3D = 10;
                chart.angle = 15;

                // WRITE
                chart.write("chartdiv");
            });
        </script>
    </head>

    <body>
        <div id="chartdiv" style="width:600px; height:400px;"></div>
    </body>

</html>