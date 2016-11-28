<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<script type="text/javascript" src="<c:url value="http://code.jquery.com/jquery.js" /> "></script>
<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" /> "></script>
<title>Home</title>
</head>
<body>
	<h1>Hello world!</h1>

	<P>The time on the server is ${serverTime}.</P>
		<button id="recOfferteOk"><spring:message code="test.message"/></button>
		<button id="recOfferteKo">TEST RISPOSTA INS SINGOLO KO</button>
		<button id="inviaNotifica">Invia Notifica</button>
		<button id="insLocale">Inserisci Locale</button>
	<script>
	
	$('#recOfferteOk').on('click', function(e) {
		$.getJSON("test/richiestaOk.json", { position: "44"}, function(json) {
			
				var objToString =  JSON.stringify(json);
				$('#jsonRitornato').text("");
				$('#jsonRitornato').append(objToString);
				
    			//alert("JSON Data - Offerta1: " + json[0].descrizione + "          "+"- Offerta2:" + json[1].descrizione);
    	});
	});
	$('#recOfferteKo').on('click', function(e) {
		$.getJSON("test/richiestaKo.json", { position: "44"}, function(json) {
			
				var objToString =  JSON.stringify(json);
				$('#jsonRitornato').text("");
				$('#jsonRitornato').append(objToString);
				
    			//alert("JSON Data - Offerta1: " + json[0].descrizione + "          "+"- Offerta2:" + json[1].descrizione);
    	});
	});
	
	$('#inviaNotifica').on('click', function(e) {
		$.post('test/notifica', function(json) {
				var objToString =  JSON.stringify(json);
				$('#jsonRitornato').text("");
			  $('#jsonRitornato').append(objToString);
			});
	});
	
	$('#insLocale').on('click', function(e) {
		$.post('locale/inserisci', {
			   "posizionePmo":{
				      "indirizzo":"piazza",
				      "numero":"12"
				   },
				   "idGestore":1,
				   "nome":"Genh",
				   "descrizione":"Prova Test desc"
				}, function(json) {
				var objToString =  JSON.stringify(json);
				$('#jsonRitornato').text("");
			  $('#jsonRitornato').append(objToString);
			});
	});
	
	</script>
	
	<form action="http://localhost:8080/devastapp/test/acquista" method="post" target="_top">
		<input type="hidden" name="cmd" value="_xclick">
		<input type="hidden" name="business" value="emanuele.barrano@gmail.com">
		<input type="hidden" name="lc" value="IT">
		<input type="hidden" name="item_name" value="TestPayPal">
		<input type="hidden" name="button_subtype" value="services">
		<input type="hidden" name="no_note" value="0">
		<input type="hidden" name="currency_code" value="EUR">
		<input type="hidden" name="shipping" value="0.00">
		<input type="hidden" name="bn" value="PP-BuyNowBF:btn_buynowCC_LG.gif:NonHostedGuest">
		<table>
			<tr>
				<td>
					<input type="hidden" name="on0" value="Offerta">Offerta
				</td>
			</tr>
			<tr>
				<td>
					<select name="os0">
						<option value="Singola">Singola E5,00 EUR</option>
						<option value="Pack 10">Pack 10 E30,00 EUR</option>
						<option value="Pack 20">Pack 20 E50,00 EUR</option>
					</select>
				</td>
			</tr>
		</table>
		<input type="hidden" name="currency_code" value="EUR">
		<input type="hidden" name="option_select0" value="Singola">
		<input type="hidden" name="option_amount0" value="5.00">
		<input type="hidden" name="option_select1" value="Pack 10">
		<input type="hidden" name="option_amount1" value="40.00">
		<input type="hidden" name="option_select2" value="Pack 20">
		<input type="hidden" name="option_amount2" value="60.00">
		<input type="hidden" name="option_index" value="0">
		<input type="image" src="https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - Il metodo rapido, affidabile e innovativo per pagare e farsi pagare.">
		<img alt="" border="0" src="https://www.paypalobjects.com/it_IT/i/scr/pixel.gif" width="1" height="1">
	</form>
	
	<div><p id="jsonRitornato"></p></div>
</body>
</html>
