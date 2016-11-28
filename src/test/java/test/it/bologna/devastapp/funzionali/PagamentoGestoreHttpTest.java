package test.it.bologna.devastapp.funzionali;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.mapper.GestoreMapper;
import it.bologna.devastapp.business.mapper.PagamentoMapper;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.PagamentoRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaPagamento;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.pagamento.PagamentoTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

/**
 * @author FB
 * 
 */
public class PagamentoGestoreHttpTest extends AbstractGestoreLoginHttpTest {

	// REPOSITORY PER CREAZIONE DATI DI TEST

	@Autowired
	PagamentoRepository pagamentoRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;

	@Autowired
	GestoreMapper gestoreMapper;
	@Autowired
	PagamentoMapper pagamentoMapper;

	// <-//

	@Test
	public void pagamentoOK() throws Exception {

		/*
		 * 1. Dati di Test - Richiesta HTTP
		 */
		Prodotto prodotto = ProdottoTestData.getProdotto();

		Prodotto prodottoResponse = prodottoRepository.save(prodotto);

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestoreResponse, posizione);

		Locale localeResponse = localeRepository.save(locale);

		Offerta offerta = OffertaTestData.getOfferta(prodottoResponse,
				localeResponse);

		Offerta offerta1 = OffertaTestData.getOfferta(prodottoResponse,
				localeResponse);
		offerta1.setQuantita(200);

		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		List<Offerta> list = new ArrayList<Offerta>();
		list.add(offertaResponse);
		list.add(offertaResponse1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = null;// pagamentoMapper.offerteToListaIdOfferta(list);

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// Richiesta STEP1
		HttpEntity<PagamentoPmo> requestEntity = new HttpEntity<PagamentoPmo>(
				pagamentoPmo, getHeaderJsonLogin());

		/*
		 * 2. Chiamata a servizio rest da testare "acquista"
		 */
		ResponseEntity<BaseRispostaJson> response = null;

		response = restTemplate.postForEntity(URL_BASE_DEPLOY + RestUrl.GESTORE
				+ RestUrl.OFFERTA + RestUrl.ACQUISTA, requestEntity,
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
				SignalDescriptor.PAGAMENTO_PAYPAL_CREATO_OK, true);

		// BaseRispostaJson.dati
		RispostaPagamento rispIns = getDatiFromBaseRispostaJson(risposta,
				RispostaPagamento.class);
		// assertNotNull(rispIns.getId());
		assertNotNull(rispIns.getPagamentoID());
		assertNull(rispIns.getListaBusinessSignal());
		assertEquals(Definitions.Paypal.CREATED, rispIns.getStato());

		/*
		 * 4. Valorizzo il pmo con i dati proveniente dalla risposta della
		 * Richiesta STEP1
		 */
		pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdineHTTP(
				pagamentoPmo, rispIns);

		// Richiesta STEP2
		requestEntity = new HttpEntity<PagamentoPmo>(pagamentoPmo,
				getHeaderJsonLogin());

		/*
		 * 4.1 Chiamata a servizio rest da testare "confermaOrdine"
		 */

		response = restTemplate.postForEntity(pagamentoPmo.getRedirectURL(),
				requestEntity, BaseRispostaJson.class);

		/*
		 * 5. ASSERT
		 */

		// HTTP StatusCode e Headers
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders()
				.getContentType());

		// HTTP Body
		// BaseRispostaJson
		risposta = response.getBody();
		AbstractPresentationWebTest.assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_OK, true);

		// BaseRispostaJson.dati
		rispIns = getDatiFromBaseRispostaJson(risposta, RispostaPagamento.class);

		assertNotNull(rispIns.getPagamentoID());
		assertNotNull(rispIns.getPayerID());
		assertNull(rispIns.getListaBusinessSignal());

		/*
		 * 6. Valorizzo il pmo con i dati proveniente dalla risposta della
		 * Richiesta STEP2
		 */
		pagamentoPmo = PagamentoTestData.getPagamentoPmoPagaHTTP(pagamentoPmo,
				rispIns);

		// Richiesta STEP3
		requestEntity = new HttpEntity<PagamentoPmo>(pagamentoPmo,
				getHeaderJsonLogin());

		/*
		 * 6.1 Chiamata a servizio rest da testare "paga"
		 */

		response = restTemplate.postForEntity(URL_BASE_DEPLOY + RestUrl.GESTORE
				+ RestUrl.OFFERTA + RestUrl.ACQUISTA, requestEntity,
				BaseRispostaJson.class);

		/*
		 * 7. ASSERT
		 */

		// HTTP StatusCode e Headers
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders()
				.getContentType());

		// HTTP Body
		// BaseRispostaJson
		risposta = response.getBody();
		AbstractPresentationWebTest.assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_ESEGUITO_OK, true);

		// BaseRispostaJson.dati
		rispIns = getDatiFromBaseRispostaJson(risposta, RispostaPagamento.class);

		assertNotNull(rispIns.getId());
		assertNotNull(rispIns.getPagamentoID());
		assertNotNull(rispIns.getPayerID());
		assertNull(rispIns.getListaBusinessSignal());
		assertEquals(Definitions.Paypal.APPROVED, rispIns.getStato());
	}
}
