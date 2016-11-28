package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.TestController;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
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

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RIFERIMENTI:<br>
 * - Libro: "pro_spring_mvc_with_spring_web_flow.pdf" pagina 305<br>
 * CAPITOLO 4-5-6-7
 * 
 * - Mapper JsonToObj e viceversa:
 * http://www.journaldev.com/2324/jackson-json-processing
 * -api-in-java-example-tutorial
 * 
 * @author ascuderetti
 * 
 */
public class TestControllerTest extends AbstractPresentationWebTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(TestControllerTest.class);

	@Autowired
	private TestController testController;

	/*
	 * ESEMPIO TEST SU CONTROLLER
	 */
	@Test
	public void getTestRichiestaKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(testController).build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		// ESEMPIO:
		// Per creare una richiesta JSON:
		// a. Recupero il PMO su cui verra' mappata la richiesta
		//
		// LocalePmo pmo = LocaleTestData.getLocalePmo(
		// GestoreTestData.getGestorePmo(),
		// PosizioneTestData.getPosizionePmo());
		//
		// **********************
		//
		// b. Lo trasformo tramite Jackson in una stringa JSON
		//
		// ObjectWriter ow = new
		// ObjectMapper().writer().withDefaultPrettyPrinter();
		//
		// String json = ow.writeValueAsString(pmo);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */

		/**
		 * <ul>
		 * 
		 * <li>1. Esegue una richiesta GET con parametri in querystring:
		 * get(RestUrl.ROOT_TEST + "/richiestaKo").param( "position", "44"))</li>
		 * 
		 * <li>2. Assert sull'header della risposta
		 * HTTP:.andExpect(status().isOk())
		 * .andExpect(content().contentType(MediaType.APPLICATION_JSON))</li>
		 * 
		 * <li>3. Ritorna il body della risposta:
		 * .andReturn().getResponse().getContentAsString();</li>
		 * 
		 * </ul>
		 */
		// Effettua la chiamata, effettua delle assert sulla risposta e
		// memorizza su rispostaJson il corpo della risposta JSON
		String rispostaJson = this.mockMvc
				.perform(
						get(RestUrl.ROOT_TEST + "/richiestaKo").param(
								"position", "44")).andExpect(status().isOk())
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
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaIns = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaIns);
		assertNull("Campo NON null", rispostaIns.getId());
		AbstractMapperValidatorTest.assertBusinessSignal(
				rispostaIns.getListaBusinessSignal(),
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

	}

	// Test in Ignore perche' i mockMvc per i test su controller non gestiscono
	// ancora i @ControllerAdvice (forse in Spring 4...)
	@Test
	@Ignore("Non e' ancora possibile testare i ControllerAdvice")
	public void getTestEccioneRuntimeKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(testController).build();

		/*
		 * 2. Richiamo il metodo da testare
		 */

		String rispostaJson = this.mockMvc
				.perform(post(RestUrl.ROOT_TEST + "/eccezioneRunTime"))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		/*
		 * Recupero corpo risposta
		 */
		// TODO

		/*
		 * 2. ASSERT sul JSON di risposta TODO
		 */

	}

	@Test
	public void testProvaData() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(testController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();
		/*
		 * 1. Preparo i dati
		 */
		String json = "{\"dataInizio\" : \"2013-12-28T11:11:11.333+08:00\"}";

		/*
		 * 2. Richiamo il metodo da testare
		 */
		// Creo la richiesta
		RequestBuilder richiesta = MockMvcRequestBuilders
				.post(RestUrl.ROOT_TEST + "/provadata").content(json)
				.contentType(MediaType.APPLICATION_JSON);

		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		/*
		 * Recupero corpo risposta
		 */
		OffertaScritturaPmo pmo = objectMapper.readValue(rispostaJson,
				OffertaScritturaPmo.class);

		LOG.info("Data Inizio Converita dopo il mapping da Json a Java, viene preso come il Timezone impostato nel file di configurazione 'Europe/Rome' (+01:00), infatti l'ora e'11-7=4:"
				+ pmo.getDataInizio());
		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		DateTime dataRitornataZ = new DateTime(2013, 12, 28, 4, 11, 11, 333,
				DateTimeZone.forID("Europe/Rome"));
		LOG.info("Data Attesa: " + dataRitornataZ);
		assertEquals(dataRitornataZ, pmo.getDataInizio());

	}

	@Test
	public void testI18n() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(testController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();
		/*
		 * 1. Preparo i dati
		 */

		/*
		 * 2. Richiamo il metodo da testare
		 */
		// Creo la richiesta
		RequestBuilder richiesta = MockMvcRequestBuilders.get(
				RestUrl.ROOT_TEST + "/i18n").contentType(
				MediaType.APPLICATION_JSON);

		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

	}
}
