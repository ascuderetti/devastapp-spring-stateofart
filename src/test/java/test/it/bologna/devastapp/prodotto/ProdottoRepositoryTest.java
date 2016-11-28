package test.it.bologna.devastapp.prodotto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Prodotto;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import test.it.bologna.devastapp.AbstractRepositoryTest;

public class ProdottoRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProdottoRepositoryTest.class);

	@Autowired
	ProdottoRepository prodottoRepository;

	@Test
	@Rollback(false)
	public void prodottoSaveTest() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto prodotto = ProdottoTestData.getProdotto();
		// Prodotto prodotto = prodottoRepository.findOne(2L);
		// prodotto.setDescrizione("testAudit1");

		/*
		 * 2) Chiamata al metodo da testare
		 */

		Prodotto prodottoResponse = prodottoRepository.save(prodotto);
		LOG.info("prodottoResponse ID: " + prodottoResponse.getId());
		/*
		 * 3) Assert
		 */
		assertNotNull("Id null", prodottoResponse.getId());

		prodottoResponse = prodottoRepository.findOne(prodottoResponse.getId());
		assertEquals(prodotto.getDescrizione(),
				prodottoResponse.getDescrizione());

	}
}
