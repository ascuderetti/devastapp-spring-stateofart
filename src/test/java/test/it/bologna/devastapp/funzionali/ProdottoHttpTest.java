package test.it.bologna.devastapp.funzionali;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

/**
 * @author ascuderetti@gmail.com
 * 
 */
public class ProdottoHttpTest extends AbstractGestoreLoginHttpTest {

	@Test
	public void aggiungiProdotto() throws Exception {

		/*
		 * 1. Dati di Test - Richiesta HTTP
		 */
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();

		// Richiesta
		HttpEntity<ProdottoPmo> requestEntity = new HttpEntity<ProdottoPmo>(
				prodottoPmo, getHeaderJsonLogin());

		/*
		 * 2. Chiamata a servizio rest da testare
		 */
		ResponseEntity<BaseRispostaJson> response = null;

		response = restTemplate.postForEntity(URL_BASE_DEPLOY + RestUrl.GESTORE
				+ RestUrl.PRODOTTO + RestUrl.INSERISCI, requestEntity,
				BaseRispostaJson.class);

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
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK, true);

		// BaseRispostaJson.dati
		RispostaInserimentoSingolo rispIns = getDatiFromBaseRispostaJson(
				risposta, RispostaInserimentoSingolo.class);
		assertNotNull(rispIns.getId());
		assertNull(rispIns.getListaBusinessSignal());
	}
}
