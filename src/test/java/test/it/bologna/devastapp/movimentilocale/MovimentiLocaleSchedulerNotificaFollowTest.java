package test.it.bologna.devastapp.movimentilocale;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import it.bologna.devastapp.business.MovimentiLocaleService;
import it.bologna.devastapp.business.notifications.NotificheGateway;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractSchedulerServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class MovimentiLocaleSchedulerNotificaFollowTest extends
		AbstractSchedulerServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	MovimentiLocaleService movimentiLocaleService;

	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST ->
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	NotificheGateway notificheGateway;

	// <-//

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testSchedulerOffertaOnline() throws InterruptedException {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		gestoreRepository.flush();
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);

		// Offerta
		Offerta offerta = OffertaTestData
				.getOffertaPubblicata(prodotto, locale);
		offerta = offertaRepository.save(offerta);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		// Attendo che lo scheduler si attivi
		Thread.sleep(60000);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		verify(notificheGateway, times(1)).notificaOffertaOnlineToFollowLocale(
				anyListOf(Offerta.class));

	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testSchedulerNessunaOffertaOnline() throws InterruptedException {

		// Attendo che lo scheduler si attivi
		Thread.sleep(3000);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		verify(notificheGateway, times(0)).notificaOffertaOnlineToFollowLocale(
				anyListOf(Offerta.class));

	}
}
