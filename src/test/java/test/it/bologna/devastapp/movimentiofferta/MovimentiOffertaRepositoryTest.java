package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class MovimentiOffertaRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovimentiOffertaRepositoryTest.class);

	// REPOSITORY PER CREAZIONE DATI DI TEST ->
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
	// <-//

	// REPOSITORY DA TESTARE ->
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	// <-//

	@Test
	public void cancellaMovimentiOffertaNonUtenteTest() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto prodotto = ProdottoTestData.getProdotto();
		LOG.info("prodotto.id: " + prodotto.getId());
		Prodotto prodottoResponse = prodottoRepository.save(prodotto);
		LOG.info("prodottoResponse.id: " + prodottoResponse.getId());

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestoreResponse, posizione);

		Locale localeResponse = localeRepository.save(locale);

		Offerta offerta = OffertaTestData.getOfferta(prodottoResponse,
				localeResponse);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Utente utente = UtenteTestData.getUtente("ID_TELEFONO");
		utente = utenteRepository.save(utente);
		MovimentiOfferta movimentiOfferta = MovimentiOffertaTestData
				.getMovimentiOfferta(utente.getId(), offertaResponse.getId(),
						Stato.FOLLOW);

		MovimentiOfferta movimentiOffertaResponse = movimentiOffertaRepository
				.save(movimentiOfferta);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		movimentiOffertaRepository.delete(movimentiOffertaResponse);

		/*
		 * 3) Assert
		 */

		movimentiOffertaResponse = movimentiOffertaRepository
				.findOne(movimentiOffertaResponse.getId());
		assertNull("L'oggetto NON e' stato cancellato",
				movimentiOffertaResponse);

	}

	@Test
	public void inserimentoAggiornamentoOk() {

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
		Offerta offerta = offertaRepository.save(OffertaTestData.getOfferta(
				prodotto, locale));

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Offerta
		 */
		MovimentiOfferta movOff = MovimentiOffertaTestData.getMovimentiOfferta(
				utente.getId(), offerta.getId(), Stato.FOLLOW);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		// Inserimento
		MovimentiOfferta movimento = movimentiOffertaRepository.save(movOff);

		/*
		 * Assert Inserimento
		 */
		assertNotNull(movimento.getId());
		// Aggiornamento
		movimento.setStato(Stato.CHECK);
		movimento = movimentiOffertaRepository.save(movimento);

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
		Offerta offerta = offertaRepository.save(OffertaTestData.getOfferta(
				prodotto, locale));

		/*
		 * 1.2 Utente
		 */
		Utente utente = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));

		/*
		 * 1.3 Movimento Offerta
		 */
		MovimentiOfferta movOff = MovimentiOffertaTestData.getMovimentiOfferta(
				utente.getId(), offerta.getId(), Stato.FOLLOW);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		// Inserimento
		MovimentiOfferta movimento = movimentiOffertaRepository.save(movOff);

		/*
		 * Assert Inserimento
		 */
		assertNotNull(movimento.getId());
		// Inserimento stesso record
		MovimentiOfferta movOff1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utente.getId(), locale.getId(),
						Stato.FOLLOW);
		movimento = movimentiOffertaRepository.save(movOff1);

	}
}
