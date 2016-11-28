package test.it.bologna.devastapp.movimentilocale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.Utente;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class MovimentiLocaleRepositoryTest extends AbstractRepositoryTest {

	// REPOSITORY PER CREAZIONE DATI DI TEST ->
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	UtenteRepository utenteRepository;
	// <-//

	// REPOSITORY DA TESTARE ->
	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;

	// <-//

	@Test
	public void inserimentoAggiornamentoOk() {

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
		 * 1.3 Movimento Locale
		 */
		MovimentiLocale movLoc = MovimentiLocaleTestData.getMovimentiLocale(
				utente.getId(), locale.getId(), Stato.FOLLOW);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		// Inserimento
		MovimentiLocale movimento = movimentiLocaleRepository.save(movLoc);

		/*
		 * Assert Inserimento
		 */
		assertNotNull(movimento.getId());
		// Aggiornamento
		movimento.setStato(Stato.CHECK);
		movimento = movimentiLocaleRepository.save(movimento);

		/*
		 * Assert Aggiornamento
		 */
		assertNotNull(movimento.getId());
		assertEquals(Stato.CHECK, movimento.getStato());
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void inserimentoAggiornamentoKo() {

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
		 * 1.3 Movimento Locale
		 */
		MovimentiLocale movLoc = MovimentiLocaleTestData.getMovimentiLocale(
				utente.getId(), locale.getId(), Stato.FOLLOW);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		// Inserimento
		MovimentiLocale movimento = movimentiLocaleRepository.save(movLoc);

		/*
		 * Assert Inserimento
		 */
		assertNotNull(movimento.getId());
		// Inserimento stesso record
		MovimentiLocale movLoc1 = MovimentiLocaleTestData.getMovimentiLocale(
				utente.getId(), locale.getId(), Stato.FOLLOW);
		movimento = movimentiLocaleRepository.save(movLoc1);

	}

	@Test
	public void findByIdsLocali() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Locale
		 */
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione1 = PosizioneTestData.getPosizione();
		Posizione posizione2 = PosizioneTestData.getPosizione();
		// Locale
		Locale locale1 = localeRepository.save(LocaleTestData.getLocale(
				gestore, posizione1));
		Locale locale2 = localeRepository.save(LocaleTestData.getLocale(
				gestore, posizione2));

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Locale
		 */
		MovimentiLocale movLoc1 = MovimentiLocaleTestData.getMovimentiLocale(
				utente.getId(), locale1.getId(), Stato.FOLLOW);
		MovimentiLocale movLoc2 = MovimentiLocaleTestData.getMovimentiLocale(
				utente.getId(), locale2.getId(), Stato.FOLLOW);

		// Inserimento
		MovimentiLocale movimento1 = movimentiLocaleRepository.save(movLoc1);
		MovimentiLocale movimento2 = movimentiLocaleRepository.save(movLoc2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<Long> idsLocali = new ArrayList<Long>();
		idsLocali.add(locale1.getId());
		idsLocali.add(locale2.getId());

		List<MovimentiLocale> listaMovimenti = movimentiLocaleRepository
				.findByIdLocaleIsIn(idsLocali);

		// ******************//
		// ** 3 - ASSERT **//
		// ****************//
		assertEquals(2, listaMovimenti.size());

		MovimentiLocale movResp1 = listaMovimenti.get(0);
		assertEquals(movimento1.getId(), movResp1.getId());

		MovimentiLocale movResp2 = listaMovimenti.get(1);
		assertEquals(movimento2.getId(), movResp2.getId());

	}
}
