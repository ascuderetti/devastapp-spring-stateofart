package test.it.bologna.devastapp.pagamento;

import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.mapper.PagamentoMapper;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class PagamentoMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	PagamentoMapper pagamentoMapper;

	@Test
	public void testPagamentoToPagamentoPmo() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto prodotto = ProdottoTestData.getProdottoEsistente();
		Gestore gestore = GestoreTestData.getGestore();
		gestore.setId(Long.valueOf(1L));
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale.setId(Long.valueOf(1L));
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta.setId(Long.valueOf(1L));
		offerta.setDataInizio(new DateTime());
		offerta.setDataFine(offerta.getDataInizio().plus(Hours.ONE));

		prodotto.setId(Long.valueOf(1L));
		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setId(Long.valueOf(2L));
		offerta1.setDataInizio(new DateTime());
		offerta1.setDataFine(offerta.getDataInizio().plus(Hours.ONE));
		offerta1.setQuantita(200);

		List<Offerta> list = new ArrayList<Offerta>();
		list.add(offerta);
		list.add(offerta1);

		Pagamento pagamento = PagamentoTestData.getPagamento(list);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		PagamentoPmo pagamentoPmo = pagamentoMapper
				.entityToPmo(pagamento);

		/*
		 * 3) Assert
		 */
		assertPagamentoEntityEqualPagamentoPmo(pagamento, pagamentoPmo);

	}

	@Test
	public void testPagamentoToPagamentoPmoGestoreNull() {

		/*
		 * 1) Preapara dati di test
		 */

		Prodotto prodotto = ProdottoTestData.getProdottoEsistente();
		Gestore gestore = GestoreTestData.getGestore();
		// condizione di errore
		gestore.setId(null);
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale.setId(Long.valueOf(1L));
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta.setId(Long.valueOf(1L));
		offerta.setDataInizio(new DateTime());
		offerta.setDataFine(offerta.getDataInizio().plus(Hours.ONE));

		prodotto.setId(Long.valueOf(1L));
		Offerta offerta1 = OffertaTestData.getOfferta(prodotto, locale);
		offerta1.setId(Long.valueOf(2L));
		offerta1.setDataInizio(new DateTime());
		offerta1.setDataFine(offerta.getDataInizio().plus(Hours.ONE));
		offerta1.setQuantita(200);

		List<Offerta> list = new ArrayList<Offerta>();
		list.add(offerta);
		list.add(offerta1);

		Pagamento pagamento = PagamentoTestData.getPagamento(list);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		PagamentoPmo pagamentoPmo = pagamentoMapper
				.entityToPmo(pagamento);

		/*
		 * 3) Assert
		 */
		assertNull("campo non mappato correttamente",
				pagamentoPmo.getIdGestore());

	}

	@Test
	public void testPagamentoPmoToPagamento() {

		/*
		 * 1) Preapara dati di test
		 */
		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();

		ProdottoPmo prodottoPmo = ProdottoTestData.getProdottoPmo();
		prodottoPmo.setId(Long.valueOf(1L));

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);
		localePmo.setId(Long.valueOf(1L));

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo.setId(Long.valueOf(1L));
		OffertaScritturaPmo offertaPmo1 = OffertaTestData.getOffertaPmo(
				localePmo, prodottoPmo);
		offertaPmo1.setId(Long.valueOf(2L));

		List<Long> list = new ArrayList<Long>();
		list.add(offertaPmo.getId());
		list.add(offertaPmo1.getId());

		PagamentoPmo pagamentoPmo = PagamentoTestData.getPagamentoPmo(list,
				gestorePmo);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		Pagamento pagamento = pagamentoMapper
				.pmoToEntity(pagamentoPmo);

		/*
		 * 3) Assert
		 */
		assertPagamentoEntityEqualPagamentoPmo(pagamento, pagamentoPmo);
	}
}
