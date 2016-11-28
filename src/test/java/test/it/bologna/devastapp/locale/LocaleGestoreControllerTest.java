package test.it.bologna.devastapp.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.LocaleService;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.LocaleGestoreController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.jsonmodel.RispostaRicercaLista;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class LocaleGestoreControllerTest extends AbstractPresentationWebTest {

	// CONTROLLER DA TESTARE
	@Autowired
	private LocaleGestoreController localeController;
	// <-//

	// REPOSITORY O SERVICE PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleService localeService;
	@Autowired
	LocaleRepository localeRepository;

	public static final String URL_LISTA_PER_GESTORE = RestUrl.GESTORE
			+ RestUrl.LOCALE + RestUrl.LISTA;

	@Test
	public void ricercaPerGestore() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		// Gestore
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		// Locale
		Locale locale1 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());
		Locale locale2 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		localeRepository.save(locale1);
		localeRepository.save(locale2);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		List<Locale> listaLocaliResponse = new ArrayList<Locale>();

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.get(URL_LISTA_PER_GESTORE)
				.param("idGestore", gestoreResponse.getId().toString())
				.contentType(MediaType.APPLICATION_JSON);
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
				rispostaJson,
				new TypeReference<RispostaRicercaLista<LocalePmo>>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.LOCALE_LISTA_OK, true);

		@SuppressWarnings("unchecked")
		RispostaRicercaLista<LocalePmo> dati = (RispostaRicercaLista<LocalePmo>) risposta
				.getDati();
		List<LocalePmo> listaLocali = dati.getLista();
		assertEquals("Numero locali non reperito correttamente", 2,
				listaLocali.size());
	}

	@Test
	public void inserimentoLocaleTestSenzaFile() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();

		String json = ow.writeValueAsString(localePmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.post(RestUrl.GESTORE + RestUrl.LOCALE + RestUrl.INSERISCI)
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
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.LOCALE_INSERIMENTO_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getId());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}

	@Test
	@Ignore("In attesa si implementi l'upload del file")
	public void inserimentoLocaleTest() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		// b. Lo trasformo tramite Jackson in una stringa JSON

		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();

		String json = ow.writeValueAsString(localePmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */
		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.post(RestUrl.GESTORE + RestUrl.LOCALE + RestUrl.INSERISCI)
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
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.LOCALE_INSERIMENTO_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNotNull("Campo null", rispostaIns.getId());
		assertNull("Lista NON null", rispostaIns.getListaBusinessSignal());
	}

	@Test
	@Ignore("In attesa si implementi l'upload del file")
	public void inserimentoLocaleTestKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		localePmo.setNome(null);
		// b. Lo trasformo tramite Jackson in una stringa JSON

		String json = objectMapper.writeValueAsString(localePmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */

		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc
				.perform(
						post(
								RestUrl.GESTORE + RestUrl.LOCALE
										+ RestUrl.INSERISCI).contentType(
								MediaType.APPLICATION_JSON).content(json))
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
				SignalDescriptor.LOCALE_INSERIMENTO_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNull("Campo null", rispostaIns.getId());
		assertNotNull("Lista NON null", rispostaIns.getListaBusinessSignal());

		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaIns.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.LOCALE_NOME_OBB_EB);
	}

	@Test
	public void aggiornaLocaleTestOk() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper
				.writeValueAsString(localePmoResponse);

		// Richiesta PUT
		// Creo la richiesta HTTP
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.GESTORE + RestUrl.LOCALE + "/"
						+ localePmoResponse.getId() + RestUrl.AGGIORNA)
				.content(richiestaJson).contentType(MediaType.APPLICATION_JSON);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		String rispostaJson = this.mockMvc.perform(richiestaHttp)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		/*
		 * MAPPING da JSON a OBJ
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.LOCALE_AGGIORNAMENTO_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaAgg = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaAgg);
		assertNotNull("Campo NULL", rispostaAgg.getId());
		assertTrue(localePmoResponse.getId().equals(rispostaAgg.getId()));
		assertNull("Campo NON null", rispostaAgg.getListaBusinessSignal());
	}

	@Test
	public void aggiornaLocaleTestKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);

		/*
		 * 1.1 Creo la richiesta HTTP
		 */

		/*
		 * CONDIZIONE DI ERRORE
		 */
		localePmoResponse.setNome(null);

		// json
		String richiestaJson = objectMapper
				.writeValueAsString(localePmoResponse);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.GESTORE + RestUrl.LOCALE + "/"
						+ localePmoResponse.getId() + RestUrl.AGGIORNA)
				.content(richiestaJson).contentType(MediaType.APPLICATION_JSON);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		String rispostaJson = this.mockMvc.perform(richiestaHttp)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		/*
		 * MAPPING da JSON a OBJ
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson, new TypeReference<RispostaInserimentoSingolo>() {
				});

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.LOCALE_AGGIORNAMENTO_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaAgg = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull(rispostaAgg);
		assertNotNull(rispostaAgg.getId());

		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaAgg.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.LOCALE_NOME_OBB_EB);
	}
}
