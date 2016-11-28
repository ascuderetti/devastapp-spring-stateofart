package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import it.bologna.devastapp.business.mapper.UtenteMapper;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.OffertaAppController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.presentation.pmo.UtentePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
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
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 
 * @author ascuderetti
 * 
 */
public class OffertaAppControllerCheckFollowTest extends
		AbstractPresentationWebTest {

	// CONTROLLER DA TESTARE
	@Autowired
	private OffertaAppController offertaAppController;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	UtenteMapper utenteMapper;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	// <-//

	/**
	 * 
	 * Test - CHECK OK con nuovo movimento e nuovo utente
	 * 
	 * {@link OffertaAppController#checkOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckConNuovoMovimentoNuovoUtenteOk() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		UtentePmo utentePmo = utenteMapper.entityToPmo(utente);

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utentePmo.getId(), offerta.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.CHECK).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.CHECK_OFFERTA_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo id null", rispostaCheck.getId());
		assertNull("Signal non null", rispostaCheck.getListaBusinessSignal());

	}

	/**
	 * 
	 * Test - CHECK OK con un aggiornamento movimento e utente
	 * 
	 * {@link OffertaAppController#checkOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckConAggMovimentoEUtenteOk() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente - Da Aggiornare
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		UtentePmo utentePmo = UtenteTestData.getUtentePmo();
		utentePmo.setId(utente.getId());
		/*
		 * 1.3 Movimento Offerta - Da aggiornare
		 */
		MovimentiOfferta movOff = movimentiOffertaRepository
				.save(MovimentiOffertaTestData.getMovimentiOfferta(
						utente.getId(), offerta.getId(), Stato.FOLLOW));
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utentePmo.getId(), offerta.getId());
		movOffPmo.setId(movOff.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.CHECK).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.CHECK_OFFERTA_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo id null", rispostaCheck.getId());
		assertNull("Signal non null", rispostaCheck.getListaBusinessSignal());

		/*
		 * Assert su DB
		 */
		long numeroRecordMovOff = movimentiOffertaRepository.count();
		long numeroRecordUtente = utenteRepository.count();
		assertTrue(numeroRecordMovOff == 1L);
		assertTrue(numeroRecordUtente == 1L);
		movimentiOffertaRepository.findByIdOffertaAndIdUtenteAndStato(
				offerta.getId(), utente.getId(), Stato.CHECK);

	}

	/**
	 * 
	 * Test - CHECK KO con un nuovo movimento e un nuovo utente
	 * 
	 * {@link OffertaAppController#checkOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckConNuovoMovimentoNuovoUtenteKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataInizio(DateTime.now().plus(Hours.ONE));

		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utente.getId(), offerta.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.CHECK).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.CHECK_OFFERTA_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();
		assertNotNull(rispostaCheck);
		assertNull(rispostaCheck.getId());
		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaCheck.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);

	}

	/**
	 * 
	 * Test - CHECK KO con un aggiornamento movimento e utente
	 * 
	 * {@link OffertaAppController#checkOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckConAggMovimentoEUtenteKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setQuantita(0);
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente - Da Aggiornare
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		UtentePmo utentePmo = UtenteTestData.getUtentePmo();
		utentePmo.setId(utente.getId());
		/*
		 * 1.3 Movimento Offerta - Da aggiornare
		 */
		MovimentiOfferta movOff = movimentiOffertaRepository
				.save(MovimentiOffertaTestData.getMovimentiOfferta(
						utente.getId(), offerta.getId(), Stato.FOLLOW));
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utentePmo.getId(), offerta.getId());
		movOffPmo.setId(movOff.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.CHECK).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.CHECK_OFFERTA_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo NULL", rispostaCheck);
		assertNotNull("Campo NULL", rispostaCheck.getId());
		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaCheck.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_QUANTITA_ESAURITA_EB);

		/*
		 * Assert su DB
		 */
		long numeroRecordMovOff = movimentiOffertaRepository.count();
		long numeroRecordUtente = utenteRepository.count();
		assertTrue(numeroRecordMovOff == 1L);
		assertTrue(numeroRecordUtente == 1L);

	}

	/**
	 * 
	 * Test - FOLLOW OK con nuovo movimento e nuovo utente
	 * 
	 * {@link OffertaAppController#seguiOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testFollowConNuovoMovimentoNuovoUtenteOk() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		UtentePmo utentePmo = utenteMapper.entityToPmo(utente);

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utentePmo.getId(), offerta.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */

		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.FOLLOW).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.OFFERTA_FOLLOW_OK, true);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull("Campo id null", rispostaCheck.getId());
		assertNull("Signal non null", rispostaCheck.getListaBusinessSignal());

	}

	/**
	 * 
	 * Test - FOLLOW KO con un nuovo movimento e un nuovo utente
	 * 
	 * {@link OffertaAppController#seguiOfferta(javax.servlet.http.HttpServletResponse, it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testFollowConNuovoMovimentoNuovoUtenteKo() throws Exception {

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaAppController).build();

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

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
		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataInizio(DateTime.now().plus(Hours.ONE));

		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(utente.getId(), offerta.getId());

		/*
		 * 1.4 Creo la richiesta HTTP
		 */
		// json
		String richiestaJson = objectMapper.writeValueAsString(movOffPmo);

		// Richiesta PUT
		RequestBuilder richiestaHttp = MockMvcRequestBuilders
				.put(RestUrl.APP + RestUrl.OFFERTA + "/" + offerta.getId()
						+ RestUrl.FOLLOW).content(richiestaJson)
				.contentType(MediaType.APPLICATION_JSON);

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
				SignalDescriptor.OFFERTA_FOLLOW_KO, false);

		// RispostaInserimentoSingolo - recupero i dati della risposta JSON
		RispostaInserimentoSingolo rispostaCheck = (RispostaInserimentoSingolo) risposta
				.getDati();
		assertNotNull(rispostaCheck);
		assertNull(rispostaCheck.getId());
		// ListaBusinessSignal
		List<BusinessSignal> listaBs = rispostaCheck.getListaBusinessSignal();
		assertEquals(1, listaBs.size());
		AbstractMapperValidatorTest.assertBusinessSignal(listaBs,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);

	}
}
