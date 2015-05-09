
var wsUri = "ws://" + document.location.hostname + ":" + document.location.port + document.location.pathname + "websocket/temp";
//var wsUri = "ws://localhost:8080/gpio-led/websocket/ledstatus";
var websocket = new WebSocket(wsUri);

websocket.onopen = function(evt) { 
	console.log("open");
	onOpen(evt); };
websocket.onmessage = function(evt) { 
	console.log("message");
	onMessage(evt); };
websocket.onerror = function(evt) {
	console.log("error");
	onError(evt); };



function onOpen() {
    console.log("Connected to " + wsUri);
}

function onMessage(evt) {
	console.log(evt.data);

	var data = JSON.parse(evt.data);
	$('#resultat').text(data.temperature+"°C");
	var chart = $('#container').highcharts();
	var date = new Date(data.date);
	chart.series[0].addPoint([date.getTime(), data.temperature]);
}

function onError(evt) {
   console.log('ERROR:' + evt.data);
}


$(function () {
	 Highcharts.setOptions({
	        global: {
	            timezoneOffset: new Date().getTimezoneOffset()
	        }
	    });


    $('#container').highcharts({
	title : {
		text : 'Temperature',
		x : -20
	},

	xAxis : {
		type : 'datetime',
		dateTimeLabelFormats : { 
			month : '%e. %b',
			year : '%b'
		},
		title : {
			text : 'Date'
		}
	},
	yAxis : {
		title : {
			text : 'Temperature (°C)'
		},
		plotLines : [ {
			value : 0,
			width : 1,
			color : '#808080'
		} ]
	},
	tooltip : {
		valueSuffix : '°C'
	},
	legend : {
		layout : 'vertical',
		align : 'right',
		verticalAlign : 'middle',
		borderWidth : 0
	},
	series : [ {
		name : 'rasp',
		data : []

	} ]
});
});