package test.it.bologna.devastapp.notifiche;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import it.bologna.devastapp.business.notifications.AndroidServiceImpl;
import it.bologna.devastapp.business.notifications.IphoneServiceImpl;
import it.bologna.devastapp.business.notifications.NotificheGateway;
import it.bologna.devastapp.business.notifications.TelefonoService;
import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.TipoTelefono;
import it.bologna.devastapp.persistence.entity.Utente;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentilocale.MovimentiLocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

/**
 * 
 * TEST DI INTEGRAZIONE.<br>
 * 
 * Test di tutto il contesto spring-integration, coinvolto nella chiamata a
 * {@link NotificheGateway#notificaCheckToFollowOfferta(Offerta)}.
 * 
 * Con Mockati i servizi: {@link AndroidServiceImpl} e {@link IphoneServiceImpl}
 * 
 * Il Test fallisce se lascio @Transactional in tutto il test perche' durante la
 * transazione aperta viene creato un nuovo thread che va su DB e resta in
 * pending in attesa che la trasazione chiuda, ma la transazione chiude quando
 * il test finisce! (stallo) => per cui ho eliminato il @Transactional e messo
 * un {@link this#tearDown()} in cui si eliminano tutti i dati dal DB.
 * 
 * @author ascuderetti
 * 
 */
@DirtiesContext
public class NotificheGatewayFollowLocaleTest extends AbstractNotificheTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(NotificheGatewayFollowLocaleTest.class);

	// SERVICE DA TESTARE [punto di ingresso]
	@Autowired
	NotificheGateway notificheGateway;
	// <-//

	/*
	 * BEAN MOCKATO nel context di TEST
	 */
	@Autowired
	@Qualifier(AndroidServiceImpl.KEY_SPRING_ANDROID_SERVICE)
	TelefonoService androidService;

	/*
	 * BEAN MOCKATO nel context di TEST
	 */
	@Autowired
	@Qualifier(IphoneServiceImpl.KEY_SPRING_IPHONE_SERVICE)
	TelefonoService iphoneService;

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;
	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;
	@Autowired
	UtenteRepository utenteRepository;

	@Test
	public void testNotificaFollowLocale() throws Exception {

		// Loggando i thread verifico in base alla configurazioni dei canali del
		// contesto di spring integration, la creazione di nuovi thread
		LOG.info("THREAD ID:" + Thread.currentThread().getId());

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
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		// Follow 1
		Utente utente1 = UtenteTestData.getUtente("ID_TEL_1");
		utente1.setTipoTelefono(TipoTelefono.IPHONE);
		utente1 = utenteRepository.save(utente1);

		Utente utente2 = UtenteTestData.getUtente("ID_TEL_2");
		utente2.setTipoTelefono(TipoTelefono.ANDROID);
		utente2 = utenteRepository.save(utente2);

		/*
		 * 1.3 Movimento Locale
		 */
		MovimentiLocale follow1 = MovimentiLocaleTestData.getMovimentiLocale(
				utente1.getId(), locale.getId(), Stato.FOLLOW);
		movimentiLocaleRepository.save(follow1);

		MovimentiLocale follow2 = MovimentiLocaleTestData.getMovimentiLocale(
				utente2.getId(), locale.getId(), Stato.FOLLOW);
		movimentiLocaleRepository.save(follow2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<Offerta> listaOfferteOnline = new ArrayList<Offerta>();
		listaOfferteOnline.add(offerta);

		notificheGateway
				.notificaOffertaOnlineToFollowLocale(listaOfferteOnline);

		// Attendo che tutti i thread completino
		Thread.sleep(4000);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		verify(androidService, times(1)).inviaNotifica(
				any(NotificaTelefono.class));
		verify(iphoneService, times(1)).inviaNotifica(
				any(NotificaTelefono.class));

	}

	@Test
	public void testNotificaNessunFollowLocale() throws Exception {

		// Loggando i thread verifico in base alla configurazioni dei canali del
		// contesto di spring integration, la creazione di nuovi thread
		LOG.info("THREAD ID:" + Thread.currentThread().getId());

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
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta = offertaRepository.save(offerta);

		/*
		 * 1.2 Utente
		 */
		// Follow 1
		Utente utente1 = UtenteTestData.getUtente("ID_TEL_1");
		utente1.setTipoTelefono(TipoTelefono.IPHONE);
		utente1 = utenteRepository.save(utente1);

		Utente utente2 = UtenteTestData.getUtente("ID_TEL_2");
		utente2.setTipoTelefono(TipoTelefono.ANDROID);
		utente2 = utenteRepository.save(utente2);

		/*
		 * 1.3 Movimento Locale
		 */
		// ..Nessuno

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		List<Offerta> listaOfferteOnline = new ArrayList<Offerta>();
		listaOfferteOnline.add(offerta);

		notificheGateway
				.notificaOffertaOnlineToFollowLocale(listaOfferteOnline);

		// Attendo che tutti i thread completino
		Thread.sleep(4000);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		verify(androidService, times(0)).inviaNotifica(
				any(NotificaTelefono.class));
		verify(iphoneService, times(0)).inviaNotifica(
				any(NotificaTelefono.class));

	}

}
