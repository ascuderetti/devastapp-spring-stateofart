package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import it.bologna.devastapp.util.DateUtils;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class OffertaSearchRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(OffertaSearchRepositoryTest.class);

	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	OffertaRepository offertaRepository;

	@Before
	public void setUpBefore() {

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
		// offertaRepository.save(offertaPiazzaMaggiore);

	}

	@Test
	public void testTrovaOffertaPagataNelRaggioDi1Km() {

		Posizione piazzaMaggiore = PosizioneTestData.getPiazzaMaggiore();

		List<Offerta> results = offertaRepository.findOffertaOnlineByLocationAndDistance(
				piazzaMaggiore, 1);

		LOG.info("Offerte trovate: " + results.size());

		for (Offerta offerta : results) {
			LOG.info("Offerta trovata: " + offerta.getDescrizione());
		}

		// Assert deve trovare quella di piazza verdi
		assertEquals(1, results.size());
		Offerta offertaTrovata = results.get(0);
		assertEquals("Offerta piazzaVerdi", offertaTrovata.getDescrizione());
		assertTrue("L'offerta non e' on-line",
				DateUtils.isOnlineNow(offertaTrovata));

	}

	@After
	public void tearDownAfter() throws Exception {

		// Elimina tutte le Offerte dall'indice

		// FullTextEntityManager fullTextEntityManager = Search
		// .getFullTextEntityManager(entityManager);
		// fullTextEntityManager.purgeAll(Offerta.class);
		// fullTextEntityManager.flushToIndexes();
		//
		// TestDefinitionAndUtils.pulisciTabelleTargetDb();

	}

}
