package test.it.bologna.devastapp.gestore;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class GestoreRepositoryTest extends AbstractServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(GestoreRepositoryTest.class);

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;

	// <-//

	@Test
	public void testListaGestoriOk() {

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Locale localeUno = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeUno);
		Locale localeDue = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeDue);

		// ***************************************//
		// ** 1 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Gestore gestoreByOneResponse = gestoreRepository.findOne(gestore
				.getId());

		// *******************//
		// ** 2 - ASSERT *****//
		// *******************//

		assertEquals(2, gestoreByOneResponse.getListaLocali().size());

	}

}
