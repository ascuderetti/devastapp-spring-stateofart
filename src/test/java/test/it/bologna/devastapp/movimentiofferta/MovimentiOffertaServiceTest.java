package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.business.MovimentiOffertaService;
import it.bologna.devastapp.business.mapper.UtenteMapper;
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
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.presentation.pmo.UtentePmo;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class MovimentiOffertaServiceTest extends AbstractServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	MovimentiOffertaService movimentiOffertaService;

	// <-//

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
	@Autowired
	UtenteMapper utenteMapper;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	// <-//

	/**
	 * 
	 * Test - CHECK OK
	 * {@link MovimentiOffertaService#checkOfferta(it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckOffertaOk() {

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
		int quantitaInit = offerta.getQuantita();

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

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movOffPmo = movimentiOffertaService.checkOfferta(movOffPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Movimento Offerta
		assertNull(movOffPmo.getListaBusinessSignal());
		assertNotNull(movOffPmo.getId());
		assertEquals(movOffPmo.getIdOggetto(), offerta.getId());
		// Utente
		assertNotNull(movOffPmo.getIdUtente());

		/*
		 * Verifico che la quantità dell'offerta è rimasta uguale e ci sia un
		 * check
		 */
		offerta = offertaRepository.findOne(offerta.getId());
		assertTrue(quantitaInit == offerta.getQuantita().intValue());

		long numCheck = movimentiOffertaRepository
				.countMovimentiOffertaByIdOffertaByStato(offerta.getId(),
						Stato.CHECK);
		assertTrue(1L == numCheck);

	}

	/**
	 * 
	 * Test - CHECK KO
	 * {@link MovimentiOffertaService#checkOfferta(it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testCheckOffertaKo() {

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
		int quantitaInit = offerta.getQuantita();
		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataInizio(new DateTime().plus(Hours.ONE));
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, offerta.getId());

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movOffPmo = movimentiOffertaService.checkOfferta(movOffPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Movimento Offerta
		assertNotNull(movOffPmo.getListaBusinessSignal());
		assertNull(movOffPmo.getId());
		assertEquals(movOffPmo.getIdOggetto(), offerta.getId());
		assertNotNull(movOffPmo.getIdUtente());

		/*
		 * Verifico che la quantità dell'offerta NON è stata Decrementata di 1
		 */
		offerta = offertaRepository.findOne(offerta.getId());
		assertTrue(quantitaInit == offerta.getQuantita().intValue());

	}

	/**
	 * 
	 * Test - FOLLOW OK
	 * {@link MovimentiOffertaService#followOfferta(it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testFollowOffertaOk() {

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

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movOffPmo = movimentiOffertaService.followOfferta(movOffPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Movimento Offerta
		assertNull(movOffPmo.getListaBusinessSignal());
		assertNotNull(movOffPmo.getId());
		assertEquals(movOffPmo.getIdOggetto(), offerta.getId());
		assertNotNull(movOffPmo.getIdUtente());
	}

	/**
	 * 
	 * Test - FOLLOW KO
	 * {@link MovimentiOffertaService#followOfferta(it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo)}
	 */
	@Test
	public void testFollowOffertaKo() {

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
		offerta.setDataInizio(new DateTime().plus(Hours.ONE));
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.3 Movimento Offerta - Da inserire
		 */
		MovimentoUtentePmo movOffPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, offerta.getId());

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		movOffPmo = movimentiOffertaService.followOfferta(movOffPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Movimento Offerta
		assertNotNull(movOffPmo.getListaBusinessSignal());
		assertNull(movOffPmo.getId());
		assertEquals(movOffPmo.getIdOggetto(), offerta.getId());
		assertNotNull(movOffPmo.getIdUtente());
	}
}
