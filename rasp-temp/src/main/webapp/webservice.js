
var list = [ ];
function getTemp(){
	var url ="ws/temp";
	$.get( url, function( data ) {
		$("#resultat").text(data);
		});
	
}

var url ="ws/temp/json";
$.getJSON( url, function( data ) {
	list.push(data);
	console.log(list);
	graph();
	});
window.setInterval(function(){
	
	var url ="ws/temp/json";
	$.getJSON( url, function( data ) {
		$("#resultat").text(data.temperature);
		list.push(data);
		console.log(list);
		graph();
		});
}, 5000);


function convert(listData){
	var array = [];
	for(i in listData){
		var data = listData[i];
		array.push([data.date, data.temperature]);
		
	}
	return array;
}
var graph = function () {
	
	
	
    $('#container').highcharts({
        title: {
            text: 'Temperature',
            x: -20
        },
       
 
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
                month: '%e. %b',
                year: '%b'
            },
            title: {
                text: 'Date'
            }
        },
        yAxis: {
            title: {
                text: 'Temperature (°C)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'rasp',
            data: convert(list)
                 
        }]
    });
}