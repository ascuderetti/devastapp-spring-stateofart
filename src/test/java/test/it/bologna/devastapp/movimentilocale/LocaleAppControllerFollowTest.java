package test.it.bologna.devastapp.movimentilocale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.LocaleAppController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 
 * @author ascuderetti
 * 
 */
public class LocaleAppControllerFollowTest extends AbstractPresentationWebTest {

	// CONTROLLER DA TESTARE
	@Autowired
	private LocaleAppController localeAppController;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	// <-//

	/**
	 * 
	 * Test - FOLLOW LOCALE OK
	 */
	@Test
	public void testFollowOk() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(localeAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Locale
		 */
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Utente - Da inserire
		 */
		MovimentoUtentePmo movPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utente.getId(), locale.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.LOCALE + RestUrl.FOLLOW)
				.content(richiestaJson).contentType(MediaType.APPLICATION_JSON);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		String rispostaJson = this.mockMvc.perform(richiestaHttp)
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
				SignalDescriptor.LOCALE_FOLLOW_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo id null", rispostaCheck.getId());
		assertNull("Signal non null", rispostaCheck.getListaBusinessSignal());

	}

}
