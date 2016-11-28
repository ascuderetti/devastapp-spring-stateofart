package test.it.bologna.devastapp.notifiche;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.notifications.CreaNotificheTelefonoService;
import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
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
import it.bologna.devastapp.persistence.entity.Utente;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.movimentilocale.MovimentiLocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;

public class CreaNotificheTelefonoServiceTest extends AbstractServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	CreaNotificheTelefonoService creaNotificheTelefonoService;

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
	UtenteRepository utenteRepository;
	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;

	// <-//

	@Test
	public void creaNotificheToFollowLocale() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		List<Offerta> listaOfferteOnline = new ArrayList<Offerta>();

		/*
		 * 1.1 Locale
		 */
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione1 = PosizioneTestData.getPosizione();
		Posizione posizione2 = PosizioneTestData.getPosizione();
		// Locale1
		Locale locale1 = LocaleTestData.getLocale(gestore, posizione1);
		locale1 = localeRepository.save(locale1);
		// Locale2
		Locale locale2 = LocaleTestData.getLocale(gestore, posizione2);
		locale2 = localeRepository.save(locale2);

		/*
		 * 1.2 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Offerta1
		Offerta offerta1 = OffertaTestData.getOffertaPubblicata(prodotto,
				locale1);
		offerta1 = offertaRepository.save(offerta1);
		// Offerta2
		Offerta offerta2 = OffertaTestData.getOffertaPubblicata(prodotto,
				locale2);
		offerta2 = offertaRepository.save(offerta2);
		/*
		 * 1.2 Utente
		 */
		// Utente1
		Utente utenteFollowLocale1 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_1"));
		// Utente2
		Utente utenteFollowLocale2 = utenteRepository.save(UtenteTestData
				.getUtente("ID_TELEFONO_2"));
		/*
		 * 1.3 Movimento Utente - Da inserire
		 */
		MovimentiLocale follow1 = MovimentiLocaleTestData.getMovimentiLocale(
				utenteFollowLocale1.getId(), locale1.getId(), Stato.FOLLOW);

		MovimentiLocale follow2 = MovimentiLocaleTestData.getMovimentiLocale(
				utenteFollowLocale2.getId(), locale2.getId(), Stato.FOLLOW);

		follow1 = movimentiLocaleRepository.save(follow1);
		follow2 = movimentiLocaleRepository.save(follow2);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		listaOfferteOnline.add(offerta1);
		listaOfferteOnline.add(offerta2);

		List<NotificaTelefono> listaNotifiche = creaNotificheTelefonoService
				.creaNotificheToFollowLocale(listaOfferteOnline);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertEquals(2, listaNotifiche.size());
		/*
		 * TODO
		 */

	}
}
