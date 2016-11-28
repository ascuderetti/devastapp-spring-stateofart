package test.it.bologna.devastapp.gestore;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.GestoreService;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.pmo.GestorePmo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class GestoreServiceTest extends AbstractServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(GestoreServiceTest.class);

	// SERVICE DA TESTARE
	@Autowired
	GestoreService gestoreService;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;

	// <-//

	@Test
	public void testListaGestoreOk() {

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);
		Locale localeUno = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeUno);
		Locale localeDue = LocaleTestData.getLocale(gestore,
				PosizioneTestData.getCasaZacconi());
		localeRepository.save(localeDue);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<GestorePmo> listaGestoriResponse = gestoreService
				.getListaGestori();

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(1, listaGestoriResponse.size());
		assertEquals(2, listaGestoriResponse.get(0).getListaLocali().size());

	}

}
