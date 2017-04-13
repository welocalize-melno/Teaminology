var ChartRender = function($j) {
	
	var twoDLineCharts = [];
	var twoDPieCharts = [];
	
	return{
		
		twoDLineChart : function(chartId, chartHeight, chartWidth, divId, dataXml){
			FusionCharts.setCurrentRenderer('javascript');
			
		      var myChart = new FusionCharts( "FusionCharts/Line.swf", 
		    		  chartId, chartHeight, chartWidth, "0", "1" );
		      twoDLineCharts.push(myChart);
		      myChart.setXMLData(dataXml);
		      
		       myChart.render(divId);
		},
		twoDPieChart : function(chartId, chartHeight, chartWidth, divId, dataXml){ 
			 FusionCharts.setCurrentRenderer('javascript');

			 var myChart = new FusionCharts( "FusionCharts/Pie2D.swf", 
		    		  "chartId"+chartId + Math.floor(Math.random() * (new Date()).getTime() + 1)
		    		  , chartHeight, chartWidth, "0", "1" );
		      twoDPieCharts.push(myChart);
		      myChart.setXMLData(dataXml);
		      
		       myChart.render(divId);
			
		},
		destroyTwoDPieChart: function(){
			for(var count =0; count < twoDPieCharts.length; count++){
				twoDPieCharts[count].dispose();
			}
			twoDPieCharts=[];
		},
		twoDMultiLine : function(chartId, chartHeight, chartWidth, divId, dataXml){
			FusionCharts.setCurrentRenderer('javascript');

		      var myChart = new FusionCharts( "FusionCharts/MSLine.swf", 
		    		  chartId, chartHeight, chartWidth );
		      
		      myChart.setXMLData(dataXml);
		      
		       myChart.render(divId);
		}
		
	};
	
	
	
}(jQuery);