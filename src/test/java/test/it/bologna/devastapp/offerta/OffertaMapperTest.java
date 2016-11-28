package test.it.bologna.devastapp.offerta;

import it.bologna.devastapp.business.mapper.OffertaMapper;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class OffertaMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	OffertaMapper offertaMapper;

	@Test
	public void testOffertaToOffertaPmo() {

		/*
		 * 1) Preapara dati di test
		 */

		Offerta offerta = OffertaTestData.getOfferta();

		/*
		 * 2) Chiamata al metodo da testare
		 */

		OffertaScritturaPmo offertaPmo = offertaMapper
				.entityToScritturaPmo(offerta);

		/*
		 * 3) Assert
		 */

		assertOffertaEntityEqualsOffertaPmo(offerta, offertaPmo);

	}

	@Test
	public void testOffertaPmoToOfferta() {

		/*
		 * 1) Preapara dati di test
		 */

		ProdottoPmo prodPmo = ProdottoTestData.getProdottoPmo();
		prodPmo.setId(1L);

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), prodPmo);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		Offerta offerta = offertaMapper.scritturaPmoToEntity(offertaPmo);

		/*
		 * 3) Assert
		 */

		assertOffertaEntityEqualsOffertaPmo(offerta, offertaPmo);

	}
}
