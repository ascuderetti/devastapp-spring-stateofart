package test.it.bologna.devastapp.pagamento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.mapper.GestoreMapper;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.PagamentoGestoreController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaPagamento;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.core.rest.APIContext;

public class PagamentoGestoreControllerTest extends AbstractPresentationWebTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(PagamentoGestoreControllerTest.class);

	public static final String URL_ACQUISTA = RestUrl.GESTORE + RestUrl.OFFERTA
			+ RestUrl.ACQUISTA;

	public static final String URL_ORDINE = RestUrl.GESTORE + RestUrl.OFFERTA
			+ RestUrl.ORDINE;

	public static final String URL_CANCELLA = RestUrl.GESTORE + RestUrl.OFFERTA
			+ RestUrl.CANCELLA;

	public static final String URL_PAGA = RestUrl.GESTORE + RestUrl.OFFERTA
			+ RestUrl.PAGA;

	// CONTROLLER DA TESTARE
	@Autowired
	PagamentoGestoreController pagamentoGestoreController;
	// <-//

	// REPOSITORY O SERVICE PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreMapper gestoreMapper;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;

	// Dipendenza Service da Mockare:
	@Autowired
	Payment payment;

	// <-//

	@Test
	public void acquistaTestOK() throws Exception {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentCreate(Definitions.Paypal.CREATED);
		when(payment.create(any(APIContext.class))).thenReturn(paymentFake);

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da creare
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_ACQUISTA)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_CREATO_OK, true);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getTitoloAcquisto());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}

	@Test
	public void confermaOrdineTestOK() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da confermare ordine
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				listPmo, gestorePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_ORDINE)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_OK, true);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getTitoloAcquisto());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}

	@Test
	public void confermaOrdineTestKO() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da confermare ordine
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				listPmo, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(Definitions.Paypal.APPROVED);

		// b. Lo trasformo tramite Jackson in una stringa JSON
		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_ORDINE)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_KO, false);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNull("Campo null", rispostaIns.getId());
		assertNotNull("Lista NON null", rispostaIns.getListaBusinessSignal());

		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaIns.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);
	}

	@Test
	public void cancellaOrdineTestOK() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da cancellare
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoCancella(
				listPmo, gestorePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_CANCELLA)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_OK, true);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getTitoloAcquisto());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}

	@Test
	public void cancellaOrdineTestKO() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da cancellare
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoCancella(
				listPmo, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(Definitions.Paypal.APPROVED);

		// b. Lo trasformo tramite Jackson in una stringa JSON
		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_CANCELLA)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_KO, false);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNull("Campo null", rispostaIns.getId());
		assertNotNull("Lista NON null", rispostaIns.getListaBusinessSignal());

		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaIns.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);
	}

	@Test
	public void pagaTestOK() throws Exception {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentExecute(Definitions.Paypal.APPROVED);
		when(
				payment.execute(any(APIContext.class),
						any(PaymentExecution.class))).thenReturn(paymentFake);

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(pagamentoGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				listPmo, gestorePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		String json = objectMapper.writeValueAsString(pagamentoPmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders.post(URL_PAGA)
				.content(json).contentType(MediaType.APPLICATION_JSON);
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaPagamento>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PAGAMENTO_PAYPAL_ESEGUITO_OK, true);

		// RispostaPagamento - recupero i dati della risposta JSON
		RispostaPagamento rispostaIns = (RispostaPagamento) risposta.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getId());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}
}
