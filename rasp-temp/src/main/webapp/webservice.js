

function getTemp(){
	var url ="ws/temp";
	 sendRequest(url);
}

function sendRequest(url){
	$.get( url, function( data ) {
		$("#resultat").text(data);
		});
}
