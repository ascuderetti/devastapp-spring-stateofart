package test.it.bologna.devastapp.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.LocaleService;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class LocaleServiceTest extends AbstractServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(LocaleServiceTest.class);

	// SERVICE DA TESTARE
	@Autowired
	LocaleService localeService;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	GestoreRepository gestoreRepository;

	@Autowired
	LocaleRepository localeRepository;

	// <-//

	@Test
	public void testFindByGestoreOk() {

		/*
		 * Dati di test
		 */

		// Gestore
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		// Locale
		Locale locale1 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());
		Locale locale2 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		localeRepository.save(locale1);
		localeRepository.save(locale2);

		List<LocalePmo> localePmoListResponse = localeService
				.findByGestore(gestoreResponse.getId());

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(2, localePmoListResponse.size());

	}

	@Test
	public void testInserisciLocaleOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);
		LOG.info("localeResponse ID: " + localePmoResponse.getId());

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNull(localePmoResponse.getListaBusinessSignal());
		assertNotNull("Id null", localePmoResponse.getId());

	}

	@Test
	public void testInserisciLocaleKo() {

		/*
		 * 1) Preapara dati di test
		 */

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		// condizione di errore
		localePmo.setNome(null);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);
		LOG.info("localeResponse ID: " + localePmoResponse.getId());

		/*
		 * 3) Assert
		 */

		assertNotNull(localePmoResponse.getListaBusinessSignal());
		assertNull("Id null", localePmoResponse.getId());

	}

	@Test
	public void testAggiornaLocaleOk() {
		/*
		 * 1) Preapara dati di test
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);

		// condizione di errore
		String nomeCambiato = "nome diverso";
		localePmoResponse.setNome(nomeCambiato);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		LocalePmo localePmoAggiornatoResponse = localeService
				.updateLocale(localePmoResponse);

		/*
		 * 3) Assert
		 */

		assertNull(localePmoAggiornatoResponse.getListaBusinessSignal());
		assertEquals("nome NON aggiornato", nomeCambiato,
				localePmoAggiornatoResponse.getNome());
	}

	@Test
	public void testAggiornaLocaleKo() {
		/*
		 * 1) Preapara dati di test
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Gestore gestoreResponse = gestoreRepository.save(gestore);

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(gestoreResponse.getId());

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		LocalePmo localePmoResponse = localeService.createLocale(localePmo);
		// condizione di errore
		localePmoResponse.setNome(null);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		LocalePmo localePmoAggiornatoResponse = localeService
				.updateLocale(localePmoResponse);

		/*
		 * 3) Assert
		 */

		assertNotNull(localePmoAggiornatoResponse.getListaBusinessSignal());
	}
}
