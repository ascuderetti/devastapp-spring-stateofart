package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepositoryCustom;
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

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

/**
 * 
 * @author ascuderetti
 */
public class MovimentiOffertaCheckRepositoryTest extends AbstractRepositoryTest {

	// REPOSITORY DA TESTARE
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;
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

	// <-//

	/*
	 * Dati di Test comuni
	 */
	Offerta offerta;

	@Before
	public void setUp() {

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
		offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta = offertaRepository.save(offerta);

	}

	/**
	 * Test
	 * {@link MovimentiOffertaRepository#findByIdOffertaAndUtenteIdAndStato(Long, Long, Stato)}
	 */
	@Test
	public void findByIdOffertaAndUtenteIdAndStatoTest() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta - setup
		 */

		/*
		 * 1.2 Utente
		 */
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
				.getMovimentiOfferta(utenteClone.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta = movimentiOffertaRepository.save(movimentoOfferta);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<MovimentiOfferta> listaMovOff = movimentiOffertaRepository
				.findByIdOffertaAndIdUtenteAndStato(offerta.getId(), idUtente,
						Stato.CHECK);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals("Campo errato", 1, listaMovOff.size());
		// MovimentoOfferta
		MovimentiOfferta movOffRecuperato = listaMovOff.get(0);
		assertTrue("Campi diversi", EqualsBuilder.reflectionEquals(
				movimentoOfferta, movOffRecuperato, "utente"));
		assertTrue("Campi diversi", EqualsBuilder.reflectionEquals(
				movimentoOfferta.getIdUtente(), movOffRecuperato.getIdUtente(),
				"utente", "movimentiOffertas"));

		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente, Stato.CHECK));
		assertFalse(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente, Stato.FOLLOW));

	}

	/**
	 * Test
	 * {@link MovimentiOffertaRepository#findByIdOffertaAndStato(Long, Stato)}
	 */
	@Test
	public void findByIdOffertaAndStatoNullTest() {
		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta - setUp
		 */

		/*
		 * 1.2 Utente
		 */
		// utente1
		Utente utente1 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		Long idUtente1 = utente1.getId();
		Utente utenteClone1 = UtenteTestData.getUtente("ID_TELEFONO");
		utenteClone1.setId(idUtente1);
		utenteClone1.setTipoTelefono(utente1.getTipoTelefono());
		// utente2
		Utente utente2 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_2"));
		Long idUtente2 = utente2.getId();
		Utente utenteClone2 = UtenteTestData.getUtente("ID_TELEFONO_2");
		utenteClone2.setId(idUtente2);
		utenteClone2.setTipoTelefono(utente2.getTipoTelefono());
		// utente3
		Utente utente3 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_3"));
		Long idUtente3 = utente2.getId();
		Utente utenteClone3 = UtenteTestData.getUtente("ID_TELEFONO_3");
		utenteClone3.setId(idUtente3);
		utenteClone3.setTipoTelefono(utente3.getTipoTelefono());

		/*
		 * 1.3 Movimento Offerta
		 */
		// utente 1
		MovimentiOfferta movimentoOfferta1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone1.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta1 = movimentiOffertaRepository.save(movimentoOfferta1);

		// utente 2
		MovimentiOfferta movimentoOfferta2 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone2.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta2 = movimentiOffertaRepository.save(movimentoOfferta2);

		// utente 3
		MovimentiOfferta movimentoOfferta3 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone3.getId(), offerta.getId(),
						Stato.FOLLOW);
		movimentoOfferta3 = movimentiOffertaRepository.save(movimentoOfferta3);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<MovimentiOfferta> listaMovOff = movimentiOffertaRepository
				.findByIdOffertaAndStato(offerta.getId(), Stato.CHECK);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(2, listaMovOff.size());
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente1, Stato.CHECK));
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente2, Stato.CHECK));
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente3, Stato.FOLLOW));

	}

	@Test
	public void findByIdOffertaAndUtenteIdNullAndStatoTest() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta - setUp
		 */

		/*
		 * 1.2 Utente
		 */
		// utente1
		Utente utente1 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		Long idUtente1 = utente1.getId();
		Utente utenteClone1 = UtenteTestData.getUtente("ID_TELEFONO");
		utenteClone1.setId(idUtente1);
		utenteClone1.setTipoTelefono(utente1.getTipoTelefono());
		// utente2
		Utente utente2 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_2"));
		Long idUtente2 = utente2.getId();
		Utente utenteClone2 = UtenteTestData.getUtente("ID_TELEFONO_2");
		utenteClone2.setId(idUtente2);
		utenteClone2.setTipoTelefono(utente2.getTipoTelefono());

		/*
		 * 1.3 Movimento Offerta
		 */
		// utente 1
		MovimentiOfferta movimentoOfferta1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone1.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta1 = movimentiOffertaRepository.save(movimentoOfferta1);

		// utente 2
		MovimentiOfferta movimentoOfferta2 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone2.getId(), offerta.getId(),
						Stato.FOLLOW);
		movimentoOfferta2 = movimentiOffertaRepository.save(movimentoOfferta2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<MovimentiOfferta> listaMovOff = movimentiOffertaRepository
				.findByIdOffertaAndIdUtenteAndStato(null, null, Stato.CHECK);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(0, listaMovOff.size());

	}

	/**
	 * Test
	 * {@link MovimentiOffertaRepositoryCustom#countMovimentiOffertaByIdOffertaByStato(Long, Stato)}
	 */
	@Test
	public void countNumCheckOffertaOK() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta creata nel setup
		 */

		/*
		 * 1.2 Utente
		 */
		// utente1
		Utente utente1 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		Long idUtente1 = utente1.getId();
		Utente utenteClone1 = UtenteTestData.getUtente("ID_TELEFONO");
		utenteClone1.setId(idUtente1);
		utenteClone1.setTipoTelefono(utente1.getTipoTelefono());
		// utente2
		Utente utente2 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_2"));
		Long idUtente2 = utente2.getId();
		Utente utenteClone2 = UtenteTestData.getUtente("ID_TELEFONO_2");
		utenteClone2.setId(idUtente2);
		utenteClone2.setTipoTelefono(utente2.getTipoTelefono());
		// utente3
		Utente utente3 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_3"));
		Long idUtente3 = utente2.getId();
		Utente utenteClone3 = UtenteTestData.getUtente("ID_TELEFONO_3");
		utenteClone3.setId(idUtente3);
		utenteClone3.setTipoTelefono(utente3.getTipoTelefono());

		/*
		 * 1.3 Movimento Offerta
		 */
		// utente 1
		MovimentiOfferta movimentoOfferta1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone1.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta1 = movimentiOffertaRepository.save(movimentoOfferta1);

		// utente 2
		MovimentiOfferta movimentoOfferta2 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone2.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta2 = movimentiOffertaRepository.save(movimentoOfferta2);

		// utente 3
		MovimentiOfferta movimentoOfferta3 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone3.getId(), offerta.getId(),
						Stato.FOLLOW);
		movimentoOfferta3 = movimentiOffertaRepository.save(movimentoOfferta3);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Long numCheck = movimentiOffertaRepository
				.countMovimentiOffertaByIdOffertaByStato(offerta.getId(),
						Stato.CHECK);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(2L, numCheck.longValue());
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente1, Stato.CHECK));
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente2, Stato.CHECK));
		assertTrue(movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
						offerta.getId(), idUtente3, Stato.FOLLOW));

	}

	/**
	 * Test
	 * {@link MovimentiOffertaRepositoryCustom#countMovimentiOffertaByIdOffertaByStato(Long, Stato)}
	 */
	@Test
	public void countNumCheckOffertaKO() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta creata nel setup
		 */

		/*
		 * 1.2 Utente
		 */
		// utente1
		Utente utente1 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO"));
		Long idUtente1 = utente1.getId();
		Utente utenteClone1 = UtenteTestData.getUtente("ID_TELEFONO");
		utenteClone1.setId(idUtente1);
		utenteClone1.setTipoTelefono(utente1.getTipoTelefono());
		// utente2
		Utente utente2 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_2"));
		Long idUtente2 = utente2.getId();
		Utente utenteClone2 = UtenteTestData.getUtente("ID_TELEFONO_2");
		utenteClone2.setId(idUtente2);
		utenteClone2.setTipoTelefono(utente2.getTipoTelefono());
		/*
		 * 1.3 Movimento Offerta
		 */
		// utente 1
		MovimentiOfferta movimentoOfferta1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone1.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta1 = movimentiOffertaRepository.save(movimentoOfferta1);

		// utente 2
		MovimentiOfferta movimentoOfferta2 = MovimentiOffertaTestData
				.getMovimentiOfferta(utenteClone2.getId(), offerta.getId(),
						Stato.CHECK);
		movimentoOfferta2 = movimentiOffertaRepository.save(movimentoOfferta2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Long numCheck = movimentiOffertaRepository
				.countMovimentiOffertaByIdOffertaByStato(offerta.getId() + 5L,
						Stato.CHECK);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(0L, numCheck.longValue());

	}
}
