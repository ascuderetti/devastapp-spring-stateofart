package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.OffertaService;
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
import it.bologna.devastapp.presentation.OffertaAppController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaRicercaLista;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaPmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
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

import test.it.bologna.devastapp.AbstractPresentationWebTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

import com.fasterxml.jackson.core.type.TypeReference;

public class OffertaAppControllerRicercaTest extends
		AbstractPresentationWebTest {

	// CONTROLLER DA TESTARE
	@Autowired
	OffertaAppController offertaController;
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

	public static final String URL_LISTA_ONLINE = RestUrl.APP + RestUrl.OFFERTA
			+ RestUrl.ONLINE + RestUrl.LISTA;

	public void setUpTestRicercaOffertaController() {

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		Posizione dueTorri = PosizioneTestData.getDueTorri();
		Posizione piazzaVerdi = PosizioneTestData.getPiazzaVerdi();
		Posizione casaZacconi = PosizioneTestData.getCasaZacconi();
		Posizione piazzaMaggiore = PosizioneTestData.getPiazzaMaggiore();
		Posizione celticDruid = PosizioneTestData.getCelticDruid();

		Gestore gestore = GestoreTestData.getGestore();
		gestoreRepository.save(gestore);
		Locale localeDueTorri = LocaleTestData.getLocale(gestore, dueTorri);
		Locale localePiazzaVerdi = LocaleTestData.getLocale(gestore,
				piazzaVerdi);
		Locale localeCasaZacconi = LocaleTestData.getLocale(gestore,
				casaZacconi);
		Locale localePiazzaMaggiore = LocaleTestData.getLocale(gestore,
				piazzaMaggiore);

		Locale localeCelticDruid = LocaleTestData.getLocale(gestore,
				celticDruid);

		localeRepository.save(localeDueTorri);
		localeRepository.save(localePiazzaVerdi);
		localeRepository.save(localeCasaZacconi);
		localeRepository.save(localePiazzaMaggiore);
		localeRepository.save(localeCelticDruid);

		Prodotto prodotto = ProdottoTestData.getProdotto();
		prodottoRepository.save(prodotto);

		// Offerta vicina ma con data futura
		Offerta offertaDueTorri = OffertaTestData.getOffertaPubblicata(
				prodotto, localeDueTorri);
		offertaDueTorri.setDescrizione("Offerta dueTorri");
		DateTime traDueOre = new DateTime().plus(Hours.TWO);
		offertaDueTorri.setDataInizio(traDueOre.plus(Hours.ONE));

		// Offerta vicina on-line
		Offerta offertaPiazzaVerdi = OffertaTestData.getOffertaPubblicata(
				prodotto, localePiazzaVerdi);
		offertaPiazzaVerdi.setDescrizione("Offerta piazzaVerdi");

		// Offerta vicina on-line NON_PAGATA
		Offerta offertaCelticDruid = OffertaTestData.getOffertaPubblicata(
				prodotto, localeCelticDruid);
		offertaCelticDruid.setStatoOfferta(StatoOfferta.CREATA);
		offertaCelticDruid.setDescrizione("Offerta celticDruid");

		// Offerta lontana
		Offerta offertaCasaZacconi = OffertaTestData.getOffertaPubblicata(
				prodotto, localeCasaZacconi);
		offertaCasaZacconi.setDescrizione("Offerta casaZacconi");

		Offerta offertaPiazzaMaggiore = OffertaTestData.getOffertaPubblicata(
				prodotto, localePiazzaMaggiore);
		offertaPiazzaMaggiore.setDescrizione("Offerta piazzaMaggiore");

		offertaRepository.save(offertaDueTorri);
		offertaRepository.save(offertaPiazzaVerdi);
		offertaRepository.save(offertaCasaZacconi);
		offertaRepository.save(offertaCelticDruid);

	}

	@Test
	public void testRicercaController() throws Exception {

		setUpTestRicercaOffertaController();

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(offertaController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		PosizionePmo posizionePzaMaggiorePmo = PosizioneTestData
				.getPosizionePmo(PosizioneTestData.LATITUDINE_PZA_MAGGIORE,
						PosizioneTestData.LONGITUDINE_PZA_MAGGIORE);

		/*
		 * 2. Richiamo il metodo da testare facendo una richiesta HTTP e faccio
		 * delle assert sullo stato e header HTTP di ritorno
		 */

		// Creo la richiesta HTTP
		RequestBuilder richiesta = MockMvcRequestBuilders
				.get(URL_LISTA_ONLINE)
				.param("latitudine",
						PosizioneTestData.LATITUDINE_PZA_MAGGIORE.toString())
				.param("longitudine",
						PosizioneTestData.LONGITUDINE_DUE_TORRI.toString());

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
				new TypeReference<RispostaRicercaLista<OffertaLetturaPmo>>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.OFFERTA_LISTA_OK, true);

		// Recupero i dati della risposta JSON
		@SuppressWarnings("unchecked")
		RispostaRicercaLista<OffertaLetturaPmo> dati = (RispostaRicercaLista<OffertaLetturaPmo>) risposta
				.getDati();

		List<OffertaLetturaPmo> listaOfferte = dati.getLista();
		assertEquals("Offerte non trovate", 1, listaOfferte.size());

	}

}
