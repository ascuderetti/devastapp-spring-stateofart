package test.it.bologna.devastapp.prodotto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.bologna.devastapp.business.ProdottoService;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;

public class ProdottoServiceTest extends AbstractServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProdottoServiceTest.class);

	@Autowired
	ProdottoService prodottoService;
	@Autowired
	ProdottoRepository prodottoRepository;

	@Test
	public void findAllProdottiTestOk() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto birra = ProdottoTestData.getProdotto();
		birra.setDescrizione("birra");

		Prodotto cicchetto = ProdottoTestData.getProdotto();
		cicchetto.setDescrizione("cicchetto");

		prodottoRepository.save(birra);
		prodottoRepository.save(cicchetto);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		List<ProdottoPmo> listaProdottiResposne = prodottoService.getProdotti();

		/*
		 * 3) Assert
		 */

		assertEquals("Lista prodotti non ritornata correttamente", 2,
				listaProdottiResposne.size());

	}

	@Test
	public void inserisciProdottoTestOk() {

		/*
		 * 1) Preapara dati di test
		 */

		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();

		/*
		 * 2) Chiamata al metodo da testare
		 */

		ProdottoPmo prodottoPmoResponse = prodottoService
				.createProdotto(prodottoPmo);
		LOG.info("prodottoResponse ID: " + prodottoPmoResponse.getId());

		/*
		 * 3) Assert
		 */

		assertNotNull("Id null", prodottoPmoResponse.getId());

	}

}
