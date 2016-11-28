package test.it.bologna.devastapp.pagamento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.PagamentoValidator;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class PagamentoValidatorTest extends AbstractMapperValidatorTest {

	static public String HOST_DEVASTAPP = "http://localhost:8080/devastapp";

	@Autowired
	PagamentoValidator pagamentoValidator;

	// ********************************//
	// ** VALIDA ACQUISTO TEST ********//
	// ********************************//

	@Test
	public void testValidaAcquistoOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);
	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoIdGestoreNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		/*
		 * CONDIZIONE DI ERRORE
		 */
		gestorePmo.setId(null);
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoTitoloNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setTitoloAcquisto(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoImportoNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setImportoTotale(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoImportoNegativoKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setImportoTotale(new BigDecimal(-20.00));

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	@Test
	public void testValidaAcquistoImportoZeroOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setImportoTotale(new BigDecimal(0.00));

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);
	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoListaOffertaVuotaKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));

		List<Long> list = new ArrayList<Long>();
		/*
		 * CONDIZIONE DI ERRORE
		 */
		// nessuna offerta aggiunta nella lista

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaAcquistoListaOffertaNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));

		/*
		 * CONDIZIONE DI ERRORE
		 */
		List<Long> list = null;

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoAcquista(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaAcquisto(HOST_DEVASTAPP, pagamentoPmo);

	}

	// ********************************//
	// ** VALIDA ORDINE TEST ********//
	// ********************************//

	@Test
	public void testValidaOrdineOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);
	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdineAccessTokenNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setAccessToken(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdineDataPagamentoNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setDataPagamento(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdinePagamentoIDNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setPagamentoID(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdineRedirectURLNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setRedirectURL(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdineRedirectURLNonValidaKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setRedirectURL(Definitions.Paypal.REDIRECT);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testValidaOrdineStatoNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

	}

	@Test
	public void testValidaOrdineStatoApprovedKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(PagamentoTestData.STATO_APPROVED);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);

	}

	@Test
	public void testValidaOrdineStatoFailedKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(PagamentoTestData.STATO_FAILED);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);

	}

	@Test
	public void testValidaOrdineStatoExpiredKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoOrdine(
				list, gestorePmo);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setStato(PagamentoTestData.STATO_EXPIRED);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		pagamentoValidator.validaOrdine(HOST_DEVASTAPP, pagamentoPmo,
				pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);

	}

	// ********************************//
	// ** VALIDA PAGAMENTO TEST *******//
	// ********************************//

	@Test
	public void testValidaPagamentoOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				list, gestorePmo);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaPagamento(HOST_DEVASTAPP, pagamentoPmo);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = pagamentoPmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);
	}

	@Test(expected = ErroreSistema.class)
	public void testValidaPagamentoPayerNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		// GestorePmo
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// PosizionePmo
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// ProdottoPmo
		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));
		// LocalePmo
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));
		// OffertaPmo
		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmoPagamento(
				list, gestorePmo);

		/*
		 * CONDIZIONE DI ERRORE
		 */
		pagamentoPmo.setPayerID(null);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		pagamentoValidator.validaPagamento(HOST_DEVASTAPP, pagamentoPmo);

	}
}
