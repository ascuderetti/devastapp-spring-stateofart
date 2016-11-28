package test.it.bologna.devastapp.funzionali;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

/**
 * @author ascuderetti@gmail.com
 * 
 */
public class InserisciLocaleHttpTest extends AbstractGestoreLoginHttpTest {

	@Test
	public void inserisciLolcale() throws Exception {

		/*
		 * 1. Dati di Test - Richiesta HTTP
		 */
		LocalePmo pmo = LocaleTestData.getLocalePmo(
				GestoreTestData.getGestorePmo(),
				PosizioneTestData.getPosizionePmo());

		/*
		 * Richiesta
		 */
		// header
		HttpHeaders headers = getHeaderJsonLogin();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

		/*
		 * Richiesta 1
		 */
		// MultiValueMap<String, String> map = new LinkedMultiValueMap<String,
		// String>();
		// map.add(USER_KEY, USER_VALUE);
		// map.add(PWD_KEY, PWD_VALUE);
		// HttpEntity<MultiValueMap<String, String>> requestEntity = new
		// HttpEntity<MultiValueMap<String, String>>(
		// map, headers);
		//
		// /*
		// * Chiamata HTTP
		// */
		// ResponseEntity<Void> response = restTemplate.exchange(URL_BASE_DEPLOY
		// + RestUrl.LOGIN, HttpMethod.POST, requestEntity, Void.class);
		// assertEquals(HttpStatus.OK, response.getStatusCode());

		/*
		 * RICHIESTA 2
		 */
		// MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String,
		// Object>();
		// parts.add("name 1", "value 1");
		// parts.add("name 2", "value 2+1");
		// parts.add("name 2", "value 2+2");
		// Resource logo = new
		// ClassPathResource("/org/springframework/http/converter/logo.jpg");
		// parts.add("logo", logo);
		// Source xml = new StreamSource(new
		// StringReader("<root><child/></root>"));
		// parts.add("xml", xml);
		//
		// template.postForLocation("http://example.com/multipart", parts);

		/*
		 * 2. Chiamata a servizio rest da testare
		 */
		ResponseEntity<BaseRispostaJson> response = null;

		// response = restTemplate.postForEntity(URL_BASE_DEPLOY +
		// RestUrl.GESTORE
		// + RestUrl.PRODOTTO + RestUrl.INSERISCI, requestEntity,
		// BaseRispostaJson.class);

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
