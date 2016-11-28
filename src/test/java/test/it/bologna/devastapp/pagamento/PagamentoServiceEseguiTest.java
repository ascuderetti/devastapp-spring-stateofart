package test.it.bologna.devastapp.pagamento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.PagamentoService;
import it.bologna.devastapp.business.mapper.GestoreMapper;
import it.bologna.devastapp.business.mapper.PagamentoMapper;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import test.it.bologna.devastapp.AbstractServiceTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;

@DirtiesContext
public class PagamentoServiceEseguiTest extends AbstractServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	PagamentoService pagamentoService;

	// Dipendenza Service da Mockare:
	@Autowired
	Payment payment;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST ->
	@Autowired
	GestoreMapper gestoreMapper;
	@Autowired
	PagamentoMapper pagamentoMapper;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;

	// <-//

	@Before
	public void setUp() throws PayPalRESTException {

		// open/read the application context file
		// ClassPathXmlApplicationContext ctx = new
		// ClassPathXmlApplicationContext(
		// "classpath:/META-INF/spring/spring-service-test.xml");
		//
		// payment = (Payment) ctx.getBean("payment");
	}

	/**
	 * 
	 * Test - ESEGUI PAGAMENTO
	 * {@link PagamentoService#eseguiPagamento(it.bologna.devastapp.presentation.pmo.PagamentoPmo)}
	 * 
	 * @throws PayPalRESTException
	 */
	@Test
	@DirtiesContext
	public void testEseguiPagamentoOk() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentExecute(Definitions.Paypal.APPROVED);
		when(
				payment.execute(any(APIContext.class),
						any(PaymentExecution.class))).thenReturn(paymentFake);

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
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());
		// =
		// pagamentoMapper.offerteToListaIdOfferta(list);

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.eseguiPagamento(pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		List<Offerta> listaOffertePubblicate = offertaRepository
				.findAll(pagamentoPmo.getListaIdOfferta());

		for (Offerta off : listaOffertePubblicate) {
			assertEquals(StatoOfferta.PUBBLICATA, off.getStatoOfferta());
		}

		// Pagamento
		assertNull(pagamentoPmo.getListaBusinessSignal());
		assertNotNull(pagamentoPmo.getId());
		// assertNotNull(pagamentoPmo.getAccessToken());
		assertNotNull(pagamentoPmo.getTitoloAcquisto());
		assertNotNull(pagamentoPmo.getImportoTotale());
		assertNotNull(pagamentoPmo.getDataPagamento());
		assertNotNull(pagamentoPmo.getPagamentoID());
		// assertNotNull(pagamentoPmo.getStato());
		// assertEquals(PagamentoTestData.STATO_APPROVED,
		// pagamentoPmo.getStato());
		// assertNotNull(pagamentoPmo.getRedirectURL());

	}

	@Test(expected = ErroreSistema.class)
	@DirtiesContext
	public void testEseguiPagamentoEXPIRED() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentExecute(Definitions.Paypal.EXPIRED);
		when(
				payment.execute(any(APIContext.class),
						any(PaymentExecution.class))).thenReturn(paymentFake);

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
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());
		// =
		// pagamentoMapper.offerteToListaIdOfferta(list);

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.eseguiPagamento(pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	@DirtiesContext
	public void testEseguiPagamentoFAILED() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentExecute(Definitions.Paypal.FAILED);
		when(
				payment.execute(any(APIContext.class),
						any(PaymentExecution.class))).thenReturn(paymentFake);

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
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());
		// =
		// pagamentoMapper.offerteToListaIdOfferta(list);

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.eseguiPagamento(pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	@DirtiesContext
	public void testEseguiPagamentoEXCEPTION() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		doThrow(new PayPalRESTException("eseguiPagamento in Exception")).when(
				payment).execute(any(APIContext.class),
				any(PaymentExecution.class));
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
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		Offerta offertaResponse = offertaRepository.save(offerta);

		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setQuantita(200);

		Offerta offertaResponse1 = offertaRepository.save(offerta1);

		/*
		 * 1.2 Mapping Gestore e Offerte
		 */
		GestorePmo gestorePmo = gestoreMapper.gestoreToGestorePmo(gestore);
		List<Long> listPmo = new ArrayList<Long>();
		listPmo.add(offertaResponse.getId());
		listPmo.add(offertaResponse1.getId());
		// =
		// pagamentoMapper.offerteToListaIdOfferta(list);

		/*
		 * 1.3 Pagamento - Da inserire
		 */
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.eseguiPagamento(pagamentoPmo);

	}
}
