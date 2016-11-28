package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.OffertaService;
import it.bologna.devastapp.business.signal.ErroreSistema;
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
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaResponse;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class OffertaLetturaServiceTest extends AbstractServiceTest {

	@PersistenceContext
	EntityManager entityManager;

	// CONTROLLER DA TESTARE
	@Autowired
	OffertaService offertaService;
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
	MovimentiOffertaRepository movimentiOffertaRepository;
	@Autowired
	UtenteRepository utenteRepository;

	OffertaScritturaPmo offertaPmoDaInserire;
	OffertaScritturaPmo offertaPmoDaModificare;
	OffertaScritturaPmo offertaPmoDaModificareStatoPubblicata;

	@Before
	public void setUpBefore() {

		/*
		 * 1) Preapara dati di test
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
		Offerta offertaDueTorriPubblicataNonAttiva = OffertaTestData
				.getOffertaPubblicata(prodotto, localeDueTorri);
		offertaDueTorriPubblicataNonAttiva.setDescrizione("Offerta dueTorri");
		DateTime traDueOre = new DateTime().plus(Hours.TWO);
		offertaDueTorriPubblicataNonAttiva.setDataInizio(traDueOre
				.plus(Hours.ONE));

		// Offerta vicina on-line
		Offerta offertaPiazzaVerdiPubblicataAttiva = OffertaTestData
				.getOffertaPubblicata(prodotto, localePiazzaVerdi);
		offertaPiazzaVerdiPubblicataAttiva
				.setDescrizione("Offerta piazzaVerdi");

		// Offerta vicina on-line NON_PAGATA
		Offerta offertaCelticDruidNonPagata = OffertaTestData
				.getOffertaPubblicata(prodotto, localeCelticDruid);
		offertaCelticDruidNonPagata.setStatoOfferta(StatoOfferta.CREATA);
		offertaCelticDruidNonPagata.setDescrizione("Offerta celticDruid");

		// Offerta lontana
		Offerta offertaCasaZacconiLontana = OffertaTestData
				.getOffertaPubblicata(prodotto, localeCasaZacconi);
		offertaCasaZacconiLontana.setDescrizione("Offerta casaZacconi");

		offertaRepository.save(offertaDueTorriPubblicataNonAttiva);
		offertaRepository.save(offertaPiazzaVerdiPubblicataAttiva);
		offertaRepository.save(offertaCasaZacconiLontana);
		offertaRepository.save(offertaCelticDruidNonPagata);

		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		Long idUtente = utente.getId();
		Utente utenteClone = UtenteTestData.getUtente("ID_TELEFONO");
		utenteClone.setId(idUtente);
		utenteClone.setIdTelefono(utente.getIdTelefono());
		utenteClone.setTipoTelefono(utente.getTipoTelefono());

		/*
		 * 1.3 Movimento Offerta
		 */
		MovimentiOfferta movimentoOfferta = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone.getId(),
						offertaPiazzaVerdiPubblicataAttiva.getId(), Stato.CHECK);
		movimentoOfferta = movimentiOffertaRepository.save(movimentoOfferta);

	}

	@Test
	public void testListOffertaOk() {

		Posizione piazzaMaggiore = PosizioneTestData.getPiazzaMaggiore();

		/*
		 * 2) Chiamata al metodo da testare
		 */

		PosizionePmo posizionePiazzaMaggiorePmo = PosizioneTestData
				.getPosizionePmo();
		posizionePiazzaMaggiorePmo.setLatitudine(PosizioneTestData
				.getPiazzaMaggiore().getLatitudine());
		posizionePiazzaMaggiorePmo.setLongitudine(PosizioneTestData
				.getPiazzaMaggiore().getLongitudine());

		OffertaLetturaResponse letturaOffertaResponse = offertaService
				.getListaOfferteOnlineByRaggioAndPosizione(posizionePiazzaMaggiorePmo);

		/*
		 * 3) Assert
		 */

		assertEquals("Numero offerte trovate diverse da 1", 1,
				letturaOffertaResponse.getListLetturaOffertaPmo().size());
		assertEquals("Offerte rimanenti", 99, letturaOffertaResponse
				.getListLetturaOffertaPmo().get(0).getQuantitaRimanente()
				.intValue());

	}

	@Test(expected = ErroreSistema.class)
	public void testListOffertaKo() {

		PosizionePmo piazzaMaggiore = PosizioneTestData.getPosizionePmo(
				PosizioneTestData.LATITUDINE_PZA_MAGGIORE, null);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaService.getListaOfferteOnlineByRaggioAndPosizione(piazzaMaggiore);

	}

}
