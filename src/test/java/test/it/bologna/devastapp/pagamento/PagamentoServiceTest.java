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
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.util.TestDefinitionAndUtils;

import com.paypal.api.payments.Payment;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

/**
 * @PowerMock va in conflitto con SpringJUnit4ClassRunner quindi abbiamo dovuto
 *            creare un contesto ad hoc e recuperare i bean a mano dal contesto
 *            (vedi setUp)
 * @author ascuderetti
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { OAuthTokenCredential.class })
@PowerMockIgnore("javax.management.*")
@Ignore("DB in LOCK sul test; indagare")
public class PagamentoServiceTest {

	@PersistenceContext
	private EntityManager entityManager;

	// SERVICE DA TESTARE ->

	PagamentoService pagamentoService;

	// Dipendenza Service da Mockare:

	Payment payment;
	OAuthTokenCredential oAuth;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST ->

	GestoreMapper gestoreMapper;

	PagamentoMapper pagamentoMapper;

	GestoreRepository gestoreRepository;

	LocaleRepository localeRepository;

	OffertaRepository offertaRepository;

	ProdottoRepository prodottoRepository;

	// <-//

	@SuppressWarnings("resource")
	@Before
	public void setUp() throws PayPalRESTException {

		// open/read the application context file
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/spring-service-pagamento-test.xml");

		prodottoRepository = ctx.getBean(ProdottoRepository.class);
		offertaRepository = ctx.getBean(OffertaRepository.class);
		localeRepository = ctx.getBean(LocaleRepository.class);
		gestoreRepository = ctx.getBean(GestoreRepository.class);
		pagamentoMapper = ctx.getBean(PagamentoMapper.class);
		gestoreMapper = ctx.getBean(GestoreMapper.class);
		pagamentoService = (PagamentoService) ctx.getBean("pagamentoService");

		payment = (Payment) ctx.getBean("payment");
		oAuth = (OAuthTokenCredential) ctx.getBean("oAuth");

		when(oAuth.getAccessToken()).thenReturn(PagamentoTestData.ACCESS_TOKEN);
	}

	/**
	 * 
	 * Test - CREA PAGAMENTO
	 * {@link PagamentoService#createPagamento(it.bologna.devastapp.presentation.pmo.PagamentoPmo)}
	 * 
	 * @throws PayPalRESTException
	 */
	@Test
	public void testCreaPagamentoOk() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentCreate(Definitions.Paypal.CREATED);
		when(payment.create(any(APIContext.class))).thenReturn(paymentFake);

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
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.createPagamento(pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		// Pagamento
		assertNull(pagamentoPmo.getListaBusinessSignal());
		assertNull(pagamentoPmo.getId());
		assertNotNull(pagamentoPmo.getAccessToken());
		assertNotNull(pagamentoPmo.getTitoloAcquisto());
		assertNotNull(pagamentoPmo.getImportoTotale());
		assertNotNull(pagamentoPmo.getDataPagamento());
		assertNotNull(pagamentoPmo.getPagamentoID());
		assertNotNull(pagamentoPmo.getStato());
		assertEquals(PagamentoTestData.STATO_CREATED, pagamentoPmo.getStato());
		assertNotNull(pagamentoPmo.getRedirectURL());

	}

	@Test(expected = ErroreSistema.class)
	public void testCreaPagamentoEXPIRED() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentCreate(Definitions.Paypal.EXPIRED);
		when(payment.create(any(APIContext.class))).thenReturn(paymentFake);

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
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.createPagamento(pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testCreaPagamentoFAILED() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		Payment paymentFake = PagamentoTestData
				.getPaymentCreate(Definitions.Paypal.FAILED);
		when(payment.create(any(APIContext.class))).thenReturn(paymentFake);

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
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.createPagamento(pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testCreaPagamentoEXCEPTION() throws PayPalRESTException {

		// ********************************//
		// ******* MOCK PAYPAL ************//
		// ********************************//
		doThrow(new PayPalRESTException("createPagamento in Exception")).when(
				payment).create(any(APIContext.class));

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
		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				listPmo, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoPmo = pagamentoService.createPagamento(pagamentoPmo);

	}

	@After
	public void tearDown() throws Exception {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(entityManager);
		fullTextEntityManager.purgeAll(Locale.class);
		fullTextEntityManager.flushToIndexes();

		TestDefinitionAndUtils.pulisciTabelleTargetDb();

	}
}
