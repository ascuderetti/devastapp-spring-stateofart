package test.it.bologna.devastapp.pagamento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.PagamentoRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class PagamentoRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(PagamentoRepositoryTest.class);

	// REPOSITORY DA TESTARE
	@Autowired
	PagamentoRepository pagamentoRepository;

	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;

	// <-//

	@Before
	public void setUpBefore() {

	}

	@Test
	public void testInserisciPagamento() {

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

		// Offerta offerta1 = OffertaTestData.getOfferta(prodottoResponse,
		// localeResponse);
		// offerta1.setQuantita(200);

		Offerta offertaResponse = offertaRepository.save(offerta);

		// Offerta offertaResponse1 = offertaRepository.save(offerta1);
		offertaResponse.setDescrizione("prova modifica");
		List<Offerta> list = new ArrayList<Offerta>();
		list.add(offertaResponse);
		// list.add(offertaResponse1);

		Pagamento pagamento = PagamentoTestData.getPagamento(list);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Pagamento pagamentoResponse = pagamentoRepository.save(pagamento);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNotNull("Id null", pagamentoResponse.getId());

		pagamentoResponse = pagamentoRepository.findOne(pagamentoResponse
				.getId());
		LOG.info("pagamentoResponse.id: " + offertaResponse.getId());
		LOG.info("pagamentoResponse.offerta.quantita: "
				+ pagamentoResponse.getOfferte().get(0).getQuantita());

		Offerta offertaRetrieved = offertaRepository.findOne(offertaResponse
				.getId());

		assertEquals(offertaRetrieved.getDescrizione(), pagamentoResponse
				.getOfferte().get(0).getDescrizione());
	}

	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testInserisciPagamentoOffertaNull() {

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

		List<Offerta> list = new ArrayList<Offerta>();
		list.add(offerta);

		Pagamento pagamento = PagamentoTestData.getPagamento(list);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoRepository.save(pagamento);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// InvalidDataAccessApiUsageException
	}

}
