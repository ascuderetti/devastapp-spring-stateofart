package test.it.bologna.devastapp.funzionali;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.presentation.pmo.UtentePmo;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

/**
 * @author ascuderetti@gmail.com
 * 
 */
public class OffertaAppHttpTest extends AbstractAppRestAuthHttpTest {

	@Test
	public void checkOk() throws Exception {

		/*
		 * 1. Dati di Test - Richiesta HTTP
		 */
		UtentePmo utentePmo = UtenteTestData.getUtentePmo();
		Long idOffertaToCheck = 1L;
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utentePmo.getId(), idOffertaToCheck);

		// Richiesta
		HttpEntity<MovimentoUtentePmo> requestEntity = new HttpEntity<MovimentoUtentePmo>(
				movOffPmo, getHeaderJsonBasicAuth());
		/*
		 * 2. Chiamata a servizio rest da testare
		 */
		ResponseEntity<BaseRispostaJson> response = null;

		response = restTemplate.exchange(URL_BASE_DEPLOY + RestUrl.APP
				+ RestUrl.OFFERTA + "/" + idOffertaToCheck + RestUrl.CHECK,
				HttpMethod.PUT, requestEntity, BaseRispostaJson.class);

		/*
		 * 3. ASSERT
		 */

		// HTTP StatusCode e Headers
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders()
				.getContentType());

		// HTTP Body
		// BaseRispostaJson
		BaseRispostaJson risposta = response.getBody();
		AbstractPresentationWebTest.assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.CHECK_OFFERTA_OK, true);

		// BaseRispostaJson.dati
		RispostaInserimentoSingolo rispDati = getDatiFromBaseRispostaJson(
				risposta, RispostaInserimentoSingolo.class);
		assertNotNull(rispDati.getId());
		assertNull(rispDati.getListaBusinessSignal());
	}
}
