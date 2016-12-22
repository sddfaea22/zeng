<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
        <script src="${pageContext.request.contextPath }/components/newamChart/amcharts/amcharts.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/newamChart/amcharts/serial.js" type="text/javascript"></script>
        <script>
            var chart;
            var chartData =${jsonData3}
            	
           /*  var chartData = [
                {
                    "country": "Czech Republic",
                    "litres": 156.9,
                    "short": "CZ"
                },
                {
                    "country": "Ireland",
                    "litres": 131.1,
                    "short": "IR"
                },
                {
                    "country": "Germany",
                    "litres": 115.8,
                    "short": "DE"
                },
                {
                    "country": "Australia",
                    "litres": 109.9,
                    "short": "AU"
                },
                {
                    "country": "Austria",
                    "litres": 108.3,
                    "short": "AT"
                },
                {
                    "country": "UK",
                    "litres": 99,
                    "short": "UK"
                },
                {
                    "country": "Belgium",
                    "litres": 93,
                    "short": "BE"
                }
            ]; */

            AmCharts.ready(function () {
                // SERIAL CHART
                var chart = new AmCharts.AmSerialChart();
                chart.dataProvider = chartData;
                chart.categoryField = "country";
                chart.startDuration = 2;
                // change balloon text color
                chart.balloon.color = "#000000";

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.gridAlpha = 0;
                categoryAxis.axisAlpha = 0;
                categoryAxis.labelsEnabled = false;

                // value
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.gridAlpha = 0;
                valueAxis.axisAlpha = 0;
                valueAxis.labelsEnabled = false;
                valueAxis.minimum = 0;
                chart.addValueAxis(valueAxis);

                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.balloonText = "[[category]]: [[value]]";
                graph.valueField = "litres";
                graph.descriptionField = "short";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 1;
                graph.fillColors = ["#ffe78e", "#bf1c25"];
                graph.labelText = "[[description]]";
                graph.balloonText = "[[category]]: [[value]] Litres";
                chart.addGraph(graph);

                chart.creditsPosition = "top-right";

                // WRITE
                chart.write("chartdiv");
            });
        </script>
    </head>

    <body>
        <div id="chartdiv" style="width: 820px; height: 400px;"></div>
    </body>

</html>