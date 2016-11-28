package test.it.bologna.devastapp.gestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.GestoreGestoreController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaRicercaLista;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

import com.fasterxml.jackson.core.type.TypeReference;

public class GestoreGestoreControllerTest extends AbstractPresentationWebTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(GestoreGestoreControllerTest.class);

	// CONTROLLER DA TESTARE
	@Autowired
	GestoreGestoreController gestoreGestoreController;
	// <-//

	// REPOSITORY O SERVICE PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;

	public static final String URL_LISTA = RestUrl.GESTORE + RestUrl.GESTORE
			+ RestUrl.LISTA;

	@Before
	public void setUpBefore() {

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Locale localeUno = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeUno);
		Locale localeDue = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeDue);

	}

	@Test
	public void testListaGestori() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(gestoreGestoreController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		RequestBuilder richiesta = MockMvcRequestBuilders.get(URL_LISTA);

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
				new TypeReference<RispostaRicercaLista<GestorePmo>>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.OFFERTA_GLOBALE_OK, true);

		// RispostaRicercaLista - recupero i dati della risposta JSON
		@SuppressWarnings("unchecked")
		RispostaRicercaLista<GestorePmo> dati = (RispostaRicercaLista<GestorePmo>) risposta
				.getDati();
		List<GestorePmo> listaGestori = dati.getLista();

		assertNotNull("Campo NULL", dati.getLista());
		assertEquals(1, listaGestori.size());
		List<LocalePmo> listaLocali = listaGestori.get(0).getListaLocali();
		assertEquals(2, listaLocali.size());
		assertNull(dati.getListaBusinessSignal());

	}
}
