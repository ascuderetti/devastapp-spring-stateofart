package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.OffertaService;
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
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.OffertaGestoreController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

import com.fasterxml.jackson.core.type.TypeReference;

public class OffertaGestoreControllerAggiornamentoTest extends
		AbstractPresentationWebTest {

	// CONTROLLER DA TESTARE
	@Autowired
	OffertaGestoreController offertaController;
	// <-//

	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	OffertaService offertaService;

	OffertaScritturaPmo offertaPmoInserita;
	OffertaScritturaPmo offertaPmoStatoPubblicata;

	@Before
	public void setUpBefore() {

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		Prodotto prodotto = ProdottoTestData.getProdotto();
		Prodotto prodottoResponse = prodottoRepository.save(prodotto);

		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(prodottoResponse.getId());

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		Posizione posizione = PosizioneTestData.getPosizione();
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		Locale locale = LocaleTestData.getLocale(gestoreResponse, posizione);

		Locale localeResponse = localeRepository.save(locale);

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		localePmo.setId(localeResponse.getId());

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);

		offertaPmoInserita = offertaService.createOfferta(offertaPmo);

		Offerta offertaStatoPubblicata = OffertaTestData.getOfferta(
				prodottoResponse, localeResponse);
		offertaStatoPubblicata.setStatoOfferta(StatoOfferta.PUBBLICATA);
		offertaRepository.save(offertaStatoPubblicata);

		offertaPmoStatoPubblicata = OffertaTestData.getOffertaPmo(localePmo,
				prodottoPmo);
		offertaPmoStatoPubblicata.setId(offertaStatoPubblicata.getId());

	}

	@Test
	public void testAggiornamentoController() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		// Aggiorno la quantia' dell'offerta

		offertaPmoInserita.setQuantita(10);

		// Converto l'oggetto in JSON
		String richiestaJson = objectMapper
				.writeValueAsString(offertaPmoInserita);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */

		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.put(RestUrl.GESTORE + RestUrl.OFFERTA + RestUrl.AGGIORNA)
				.content(richiestaJson).contentType(MediaType.APPLICATION_JSON);

		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)

		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.OFFERTA_GLOBALE_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo id null", rispostaIns.getId());
		assertNull("Signal non null", rispostaIns.getListaBusinessSignal());

	}

	@Test
	public void testAggiornamentoControllerKO() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		// Aggiorno la quantita' dell'offerta con un valore negativo

		offertaPmoInserita.setQuantita(-1);

		// Converto l'oggetto in JSON
		String richiestaJson = objectMapper
				.writeValueAsString(offertaPmoInserita);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */

		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.put(RestUrl.GESTORE + RestUrl.OFFERTA + RestUrl.AGGIORNA)
				.content(richiestaJson).contentType(MediaType.APPLICATION_JSON);

		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)

		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Lista NON null", rispostaIns.getListaBusinessSignal());

		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaIns.getListaBusinessSignal();
		assertTrue(listaBs.size() > 0);
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.OFFERTA_QUANTITA_MINOREUGUALEAZERO);

	}

}
