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
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.TipoTelefono;
import it.bologna.devastapp.persistence.entity.Utente;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;
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
// @DirtiesContext => serve per forzare il ricaricamento del contesto a spring,
// altrimenti i valori sulle assert restavano anche nei test seguenti!
@DirtiesContext
public class NotificheGatewayFollowOffertaTest extends AbstractNotificheTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(NotificheGatewayFollowOffertaTest.class);

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
	public void testNotificaCheckToFollow() throws Exception {

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
		 * 1.2 Movimento Offerta
		 */
		Long idOffertaChecked = offerta.getId();

		// Follow 3
		Utente utente3 = UtenteTestData.getUtente("ID_TELEFONO");
		String idTel3 = "3";
		utente3.setIdTelefono(idTel3);
		utente3.setTipoTelefono(TipoTelefono.IPHONE);
		utente3 = utenteRepository.save(utente3);
		MovimentiOfferta follow3 = MovimentiOffertaTestData
				.getMovimentiOfferta(utente3.getId(), idOffertaChecked,
						Stato.FOLLOW);
		movimentiOffertaRepository.save(follow3);

		// Checked
		Utente utenteCheck = UtenteTestData.getUtente("ID_TELEFONO");
		String idTelCheck = "Check";
		utenteCheck.setIdTelefono(idTelCheck);
		utenteCheck.setTipoTelefono(TipoTelefono.ANDROID);
		utenteCheck = utenteRepository.save(utenteCheck);
		MovimentiOfferta check = MovimentiOffertaTestData.getMovimentiOfferta(
				utenteCheck.getId(), idOffertaChecked, Stato.CHECK);
		movimentiOffertaRepository.save(check);

		// Follow 1
		Utente utente1 = UtenteTestData.getUtente("ID_TELEFONO");
		String idTel1 = "1";
		utente1.setIdTelefono(idTel1);
		utente1.setTipoTelefono(TipoTelefono.ANDROID);
		utente1 = utenteRepository.save(utente1);
		MovimentiOfferta follow1 = MovimentiOffertaTestData
				.getMovimentiOfferta(utente1.getId(), idOffertaChecked,
						Stato.FOLLOW);
		movimentiOffertaRepository.save(follow1);

		// Follow 2
		Utente utente2 = UtenteTestData.getUtente("ID_TELEFONO");
		String idTel2 = "2";
		utente2.setIdTelefono(idTel2);
		utente2.setTipoTelefono(TipoTelefono.ANDROID);
		utente2 = utenteRepository.save(utente2);
		MovimentiOfferta follow2 = MovimentiOffertaTestData
				.getMovimentiOfferta(utente2.getId(), idOffertaChecked,
						Stato.FOLLOW);
		movimentiOffertaRepository.save(follow2);

		// Follow 3 to Check
		follow3.setStato(Stato.CHECK);
		movimentiOffertaRepository.save(follow3);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		notificheGateway.notificaCheckToFollowOfferta(offerta);

		// Attendo che tutti i thread completino
		Thread.sleep(4000);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		verify(androidService, times(2)).inviaNotifica(
				any(NotificaTelefono.class));
		verify(iphoneService, times(0)).inviaNotifica(
				any(NotificaTelefono.class));

	}

}
