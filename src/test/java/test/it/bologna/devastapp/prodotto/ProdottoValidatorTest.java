package test.it.bologna.devastapp.prodotto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.ProdottoValidator;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class ProdottoValidatorTest extends AbstractMapperValidatorTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProdottoValidatorTest.class);

	@Autowired
	ProdottoValidator prodottoValidator;

	@Test
	public void testValidaProdottoKo() {

		/*
		 * 1) Preapara dati di test
		 */

		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		// condizione di errore
		prodottoPmo.setDescrizione(null);
		/*
		 * 2) Chiamata al metodo da testare
		 */
		prodottoValidator.validaProdotto(prodottoPmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = prodottoPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

	}

	@Test
	public void testValidaProdottoOk() {

		/*
		 * 1) Preapara dati di test
		 */

		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();

		/*
		 * 2) Chiamata al metodo da testare
		 */

		prodottoValidator.validaProdotto(prodottoPmo);

		/*
		 * 3) Assert
		 */
		assertNull("Numero Business Signal Errato",
				prodottoPmo.getListaBusinessSignal());

	}
}
