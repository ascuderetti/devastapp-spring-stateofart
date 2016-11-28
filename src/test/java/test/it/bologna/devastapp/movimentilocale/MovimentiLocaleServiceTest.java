package test.it.bologna.devastapp.movimentilocale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.MovimentiLocaleService;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class MovimentiLocaleServiceTest extends AbstractServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	MovimentiLocaleService movimentiLocaleService;

	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST ->
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;

	// <-//

	/**
	 * 
	 * Test - FOLLOW OK
	 * {@link MovimentiLocaleService#followLocale(MovimentoUtentePmo)}
	 */
	@Test
	public void testFollowLocaleOk() {

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

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movPmo = movimentiLocaleService.followLocale(movPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Movimento Offerta
		assertNull(movPmo.getListaBusinessSignal());
		assertNotNull(movPmo.getId());
		assertEquals(movPmo.getIdOggetto(), locale.getId());
		// Utente
		assertNotNull(movPmo.getIdUtente());

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testFollowLocaleIdUtenteNonEsisteKo() {

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
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, locale.getId());

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movPmo = movimentiLocaleService.followLocale(movPmo);

	}

}
