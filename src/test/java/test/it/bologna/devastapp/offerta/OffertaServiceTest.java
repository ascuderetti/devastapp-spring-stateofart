package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.OffertaService;
import it.bologna.devastapp.business.mapper.UtenteMapper;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class OffertaServiceTest extends AbstractServiceTest {

	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	OffertaService offertaService;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	UtenteMapper utenteMapper;

	OffertaScritturaPmo offertaPmoDaInserire;
	OffertaScritturaPmo offertaPmoDaModificare;
	OffertaScritturaPmo offertaPmoDaModificareStatoPubblicata;

	@Before
	public void setUp() {

		/*
		 * 1) Preapara dati di test
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

		offertaPmoDaInserire = OffertaTestData.getOffertaPmo(localePmo,
				prodottoPmo);

		Offerta offerta = OffertaTestData.getOfferta(prodottoResponse,
				localeResponse);
		offertaRepository.save(offerta);

		Offerta offertaStatoPubblicata = OffertaTestData.getOfferta(
				prodottoResponse, localeResponse);
		offertaStatoPubblicata.setStatoOfferta(StatoOfferta.PUBBLICATA);
		offertaRepository.save(offertaStatoPubblicata);

		offertaPmoDaModificare = OffertaTestData.getOffertaPmo(localePmo,
				prodottoPmo);
		offertaPmoDaModificare.setId(offerta.getId());

		offertaPmoDaModificareStatoPubblicata = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmoDaModificareStatoPubblicata.setId(offertaStatoPubblicata
				.getId());

	}

	@Test
	public void testInserisciOffertaOk() {

		/*
		 * 2) Chiamata al metodo da testare
		 */

		OffertaScritturaPmo offertaPmoResponse = offertaService
				.createOfferta(offertaPmoDaInserire);

		/*
		 * 3) Assert
		 */

		assertEquals("Offerta inserita con stato errato", StatoOfferta.CREATA,
				offertaRepository.findOne(offertaPmoResponse.getId())
						.getStatoOfferta());
		assertNotNull("Id null", offertaPmoResponse.getId());

	}

	@Test
	public void testInserisciOffertaKo() {

		// condizione di errore
		offertaPmoDaInserire.setDataInizio(null);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		OffertaScritturaPmo offertaPmoResponse = offertaService
				.createOfferta(offertaPmoDaInserire);

		/*
		 * 3) Assert
		 */

		assertNull("Id null", offertaPmoResponse.getId());
		assertNotNull("BusinessSignal presente: ",
				offertaPmoResponse.getListaBusinessSignal());

	}

	@Test
	public void testAggiornaOffertaOk() {

		offertaPmoDaModificare.setQuantita(999);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		OffertaScritturaPmo offertaPmoResponse = offertaService
				.updateOfferta(offertaPmoDaModificare);

		/*
		 * 3) Assert
		 */

		assertEquals("Aggiornamento non riuscito", new Integer(999),
				offertaPmoResponse.getQuantita());

		assertNull("BusinessSignal presente: ",
				offertaPmoResponse.getListaBusinessSignal());

	}

	@Test
	public void testAggiornaOffertaKo() {

		// condizione di errore

		offertaPmoDaModificare.setQuantita(0);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaService.updateOfferta(offertaPmoDaModificare);

		/*
		 * 3) Assert
		 */

		assertNotNull("BusinessSignal non presente",
				offertaPmoDaModificare.getListaBusinessSignal());

	}

	@Test(expected = ErroreSistema.class)
	public void testAggiornaOffertaStatoPubblicataKo() {

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaService.updateOfferta(offertaPmoDaModificareStatoPubblicata);

	}

}
