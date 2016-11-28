package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class OffertaRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(OffertaRepositoryTest.class);

	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;

	Offerta offerta;

	@Before
	public void setUpBefore() {

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

		offerta = OffertaTestData.getOfferta(prodottoResponse, localeResponse);

	}

	@Test
	public void offertaConProdottoEsistenteSaveTest() {

		/*
		 * 1) Chiamata al metodo da testare
		 */

		Offerta offertaResponse = offertaRepository.save(offerta);

		/*
		 * 3) Assert
		 */

		assertNotNull("Id null", offertaResponse.getId());

		offertaResponse = offertaRepository.findOne(offertaResponse.getId());
		LOG.info("offertaResponse.id: " + offertaResponse.getId());
		LOG.info("offertaResponse.prodotto.id: "
				+ offertaResponse.getProdotto().getId());

		assertEquals(offerta.getDescrizione(), offertaResponse.getDescrizione());

	}

	@Test
	@Ignore("Test di esempio da utilizzare come riferimento futuro")
	public void testCercaOfferta() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto prodotto = ProdottoTestData.getProdotto();
		LOG.info("prodotto.id: " + prodotto.getId());
		Prodotto prodottoResponse = prodottoRepository.save(prodotto);
		LOG.info("prodottoResponse.id: " + prodottoResponse.getId());

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Posizione posizione = PosizioneTestData.getPiazzaVerdi();
		Locale locale = LocaleTestData.getLocale(gestoreResponse, posizione);

		Locale localeResponse = localeRepository.save(locale);

		Offerta offerta = OffertaTestData.getOfferta(prodottoResponse,
				localeResponse);
		offerta.setId(null);

		Posizione dueTorri = PosizioneTestData.getDueTorri();

		/*
		 * 2) Chiamata al metodo da testare
		 */

		Offerta offertaResponse = offertaRepository.save(offerta);

		// QOfferta qOfferta = QOfferta.offerta;
		//
		// NumberPath<Double> lat = qOfferta.locale.posizione.latitudine;
		// NumberPath<Double> lng = qOfferta.locale.posizione.longitudine;
		// NumberPath<Double> distance = qOfferta.locale.posizione.latitudine;
		//
		// NumberExpression<Double> formula = acos(
		// cos(radians(constant(dueTorri.getLatitudine())))
		// .multiply(cos(radians(lat)))
		// .multiply(
		// cos(radians(lng)).subtract(
		// radians(constant(dueTorri
		// .getLongitudine()))))
		// .add(sin(radians(constant(dueTorri.getLatitudine())))
		// .multiply(sin(radians(lat))))).multiply(3959);
		//
		// List<Offerta> listaOfferte1 = from(qOfferta).list(qOfferta);
		//
		// Iterable<Offerta> listaOfferte = null;
		//
		// for (Offerta offerta2 : listaOfferte) {
		// LOG.info("------------------------- " + offerta2.getDescrizione());
		//
		// }

		// List<Offerta> listaOfferte = MiniApi.from(qOfferta)
		// .where(formula.as(distance).lt(20)).list(qOfferta);
		//
		// List<Offerta> listaOfferte1 = MiniApi
		// .from(qOfferta)
		// .where(qOfferta.locale.posizione.in(new JPASubQuery()
		// .from(QPosizione.posizione).where(formula.lt(20))
		// .list(QPosizione.posizione))).list(qOfferta);

		// LOG.info("eurekaaaaaaaaaaaaaaaaaaaaaaaaa: " + listaOfferte1.size());

		// List<Offerta> offerte = qOfferta.in(new JPASubQuery()
		// .from(QPosizione.posizione).where(formula.lt(20))
		// .list(QPosizione.posizione)).list(qOfferta));

	}

	@Test
	public void testUpdateStato() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		offerta.setStatoOfferta(StatoOfferta.CREATA);

		offerta = offertaRepository.save(offerta);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		offertaRepository.updateStatoById(offerta.getId(),
				StatoOfferta.PUBBLICATA);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		Offerta offertaModificata = offertaRepository.findOne(offerta.getId());

		assertEquals(StatoOfferta.PUBBLICATA,
				offertaModificata.getStatoOfferta());

	}

	@Test
	public void testFindOfferteOnline() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// Offerta Online 1
		DateTime now = DateTime.now();
		offerta.setDataInizio(now);
		offerta.setDataFine(now.plusHours(1));
		offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);
		offertaRepository.save(offerta);

		// Offerta Online 2
		Offerta offertaOnline2 = OffertaTestData.getOfferta();
		offertaOnline2.setProdotto(offerta.getProdotto());
		offertaOnline2.setLocale(offerta.getLocale());
		offertaOnline2.setDataInizio(now);
		offertaOnline2.setDataFine(now.plusHours(1));
		offertaOnline2.setStatoOfferta(StatoOfferta.PUBBLICATA);
		offertaRepository.save(offertaOnline2);

		// Offerta NON online perche' statoOfferta Creata
		Offerta offertaOffline1 = OffertaTestData.getOfferta();
		offertaOffline1.setProdotto(offerta.getProdotto());
		offertaOffline1.setLocale(offerta.getLocale());
		offertaOffline1.setDataInizio(now);
		offertaOffline1.setDataFine(now.plusHours(1));
		offertaOffline1.setStatoOfferta(StatoOfferta.CREATA);
		offertaRepository.save(offertaOffline1);

		// Offerta NON online perche' fuori dal range temporale
		Offerta offertaOffline2 = OffertaTestData.getOfferta();
		offertaOffline2.setProdotto(offerta.getProdotto());
		offertaOffline2.setLocale(offerta.getLocale());
		offertaOffline2.setDataInizio(now.plusHours(1));
		offertaOffline2.setDataFine(now.plusHours(2));
		offertaOffline2.setStatoOfferta(StatoOfferta.CREATA);
		offertaRepository.save(offertaOffline2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<Offerta> listaOfferteOnline = offertaRepository
				.findByDataInizioBeforeAndDataFineAfterAndStatoOfferta(
						now.plusMinutes(15), now.plusMinutes(15),
						StatoOfferta.PUBBLICATA);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(2, listaOfferteOnline.size());

	}
}
