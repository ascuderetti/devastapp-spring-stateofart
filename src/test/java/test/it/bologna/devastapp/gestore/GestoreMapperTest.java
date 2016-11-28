package test.it.bologna.devastapp.gestore;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.mapper.GestoreMapper;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.pmo.GestorePmo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.locale.LocaleTestData;

public class GestoreMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	GestoreMapper gestoreMapper;

	@Test
	public void tesGestoreToGestorePmo() {

		/*
		 * 1) Preapara dati di test
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Locale locale = LocaleTestData.getLocale(gestore, null);
		List<Locale> listaLocali = new ArrayList<Locale>();
		listaLocali.add(locale);
		gestore.setListaLocali(listaLocali);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);

		/*
		 * 3) Assert
		 */

		assertEquals("Non ha mappato le liste", 1, gestorePmo.getListaLocali()
				.size());

	}
	// @Test
	// public void testOffertaPmoToOfferta() {
	//
	// /*
	// * 1) Preapara dati di test
	// */
	//
	// OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
	// LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
	// PosizioneTestData.getPosizionePmo()), ProdottoTestData
	// .getProdottoPmo());
	//
	// /*
	// * 2) Chiamata al metodo da testare
	// */
	//
	// Offerta offerta = offertaMapper.offertaPmoToOfferta(offertaPmo);
	//
	// /*
	// * 3) Assert
	// */
	//
	// /*
	// * TODO date
	// */
	// assertOffertaEntityEqualsOffertaPmo(offerta, offertaPmo);
	//
	// }

}
