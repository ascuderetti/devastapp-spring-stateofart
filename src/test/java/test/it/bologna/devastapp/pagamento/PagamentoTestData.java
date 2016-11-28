package test.it.bologna.devastapp.pagamento;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.presentation.jsonmodel.RispostaPagamento;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.DateMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

public final class PagamentoTestData {

	static public String TITOLO_ACQUISTO_PROVA = "titolo acquisto prova";

	static public BigDecimal IMPORTO_TOTALE_20EUR = new BigDecimal(20.00);
	static public DateTimeFormatter formatter = DateTimeFormat
			.forPattern(DateMapper.DATE_TIME_OFFSET_ISO_8601_FORMAT);

	static public String DATA_PAGAMENTO = "2014-11-09T19:12:23+0500";
	static public String ID_PAYPAL_PAYMENT = "PAY-5U6912489D785871JKOLPENA";
	static public String REDIRECT_URL_ORDINE = "http://localhost:8080/devastapp/gestore/offerta/ordine";
	static public String REDIRECT_URL_CANCELLA = "http://localhost:8080/devastapp/gestore/offerta/cancella";
	static public String ACCESS_TOKEN = "A015evfBoQj0qLa7rsQD.qRazhB5Gch7WdqkEcCysjId2eo";
	static public String STATO_CREATED = "created";
	static public String STATO_APPROVED = "approved";
	static public String STATO_FAILED = "failed";
	static public String STATO_CANCELED = "canceled";
	static public String STATO_EXPIRED = "expired";

	static public String ID_PAYPAL_PAYER = "JL68ZWGFFCEAS";
	static public String REDIRECT_PAYPAL_FAKE = "http://sandbox.paypal.fake.com";

	public static Pagamento getPagamento(List<Offerta> list) {

		Pagamento pagamento = new Pagamento();
		pagamento.setIdPaypalPayer(ID_PAYPAL_PAYER);
		pagamento.setIdPaypalPayment(ID_PAYPAL_PAYMENT);
		pagamento.setTitoloAcquisto(TITOLO_ACQUISTO_PROVA);
		pagamento.setImportoTotale(IMPORTO_TOTALE_20EUR);
		pagamento.setDataPagamento(formatter.parseDateTime(DATA_PAGAMENTO));
		pagamento.setIdGestore(list.get(0).getLocale().getIdGestore());

		// List<DettaglioPagamento> listaDettaglioPagamento = new
		// ArrayList<DettaglioPagamento>();
		//
		// for (Offerta off : list) {
		// DettaglioPagamento dp = new DettaglioPagamento();
		// dp.setOfferta(off);
		// listaDettaglioPagamento.add(dp);
		// }

		pagamento.setOfferte(list);

		return pagamento;
	}

	public static PagamentoPmo getPagamentoPmo(List<Long> list,
			GestorePmo gestorePmo) {

		PagamentoPmo pagamentoPmo = new PagamentoPmo();
		pagamentoPmo.setPayerID(ID_PAYPAL_PAYER);
		pagamentoPmo.setPagamentoID(ID_PAYPAL_PAYMENT);
		pagamentoPmo.setTitoloAcquisto(TITOLO_ACQUISTO_PROVA);
		pagamentoPmo.setImportoTotale(IMPORTO_TOTALE_20EUR);
		pagamentoPmo.setDataPagamento(new DateTime().plus(Hours.ONE));
		pagamentoPmo.setIdGestore(gestorePmo.getId());
		pagamentoPmo.setListaIdOfferta(list);

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoAcquista(List<Long> list,
			GestorePmo gestorePmo) {

		PagamentoPmo pagamentoPmo = new PagamentoPmo();
		pagamentoPmo.setTitoloAcquisto(TITOLO_ACQUISTO_PROVA);
		pagamentoPmo.setImportoTotale(IMPORTO_TOTALE_20EUR);
		pagamentoPmo.setIdGestore(gestorePmo.getId());
		pagamentoPmo.setListaIdOfferta(list);

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoOrdine(List<Long> list,
			GestorePmo gestorePmo) {

		PagamentoPmo pagamentoPmo = getPagamentoPmoAcquista(list, gestorePmo);

		pagamentoPmo.setAccessToken(ACCESS_TOKEN);
		pagamentoPmo.setPagamentoID(ID_PAYPAL_PAYMENT);
		pagamentoPmo.setDataPagamento(new DateTime().plus(Hours.ONE));
		pagamentoPmo.setRedirectURL(REDIRECT_URL_ORDINE);
		pagamentoPmo.setStato(STATO_CREATED);

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoPagamento(List<Long> list,
			GestorePmo gestorePmo) {

		PagamentoPmo pagamentoPmo = getPagamentoPmoOrdine(list, gestorePmo);
		pagamentoPmo.setPayerID(ID_PAYPAL_PAYER);

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoOrdineHTTP(
			PagamentoPmo pagamentoPmo, RispostaPagamento risp) {

		pagamentoPmo.setAccessToken(risp.getAccessToken());
		pagamentoPmo.setPagamentoID(risp.getPagamentoID());
		pagamentoPmo.setPayerID(risp.getPayerID());
		pagamentoPmo.setDataPagamento(risp.getDataPagamento());
		pagamentoPmo.setRedirectURL(risp.getRedirectURL());
		pagamentoPmo.setStato(risp.getStato());

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoCancella(List<Long> list,
			GestorePmo gestorePmo) {

		PagamentoPmo pagamentoPmo = getPagamentoPmoAcquista(list, gestorePmo);

		pagamentoPmo.setAccessToken(ACCESS_TOKEN);
		pagamentoPmo.setPagamentoID(ID_PAYPAL_PAYMENT);
		pagamentoPmo.setDataPagamento(new DateTime().plus(Hours.ONE));
		pagamentoPmo.setRedirectURL(REDIRECT_URL_CANCELLA);
		pagamentoPmo.setStato(STATO_CREATED);

		return pagamentoPmo;
	}

	public static PagamentoPmo getPagamentoPmoPagaHTTP(
			PagamentoPmo pagamentoPmo, RispostaPagamento risp) {

		pagamentoPmo.setDataPagamento(risp.getDataPagamento());
		pagamentoPmo.setStato(risp.getStato());

		return pagamentoPmo;
	}

	public static Payment getPaymentCreate(String state) {
		Payment payment = new Payment();
		payment.setCreateTime(PagamentoTestData.DATA_PAGAMENTO);
		payment.setId(PagamentoTestData.ID_PAYPAL_PAYMENT);
		payment.setState(state);
		List<Links> list = new ArrayList<Links>();
		Links link = new Links();
		link.setMethod(Definitions.Paypal.REDIRECT);
		link.setHref(REDIRECT_PAYPAL_FAKE);
		list.add(link);
		payment.setLinks(list);

		return payment;
	}

	public static Payment getPaymentExecute(String state) {

		Payment payment = getPaymentCreate(state);
		payment.setUpdateTime(PagamentoTestData.DATA_PAGAMENTO);
		return payment;
	}
}
