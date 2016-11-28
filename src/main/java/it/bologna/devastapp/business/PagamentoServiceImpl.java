package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.PagamentoMapper;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.OffertaValidator;
import it.bologna.devastapp.business.validator.PagamentoValidator;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.PagamentoRepository;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.DateMapper;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

@Service
@Transactional(readOnly = true)
public class PagamentoServiceImpl implements PagamentoService {

	private static final Logger LOG = LoggerFactory
			.getLogger(PagamentoServiceImpl.class);

	@Autowired
	PagamentoValidator pagamentoValidator;
	@Autowired
	OffertaValidator offertaValidator;

	@Autowired
	PagamentoMapper pagamentoMapper;

	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	PagamentoRepository pagamentoRepository;

	Payment payment;
	OAuthTokenCredential oAuth;
	String host;

	/**
	 * Construttore
	 */
	@Autowired
	public PagamentoServiceImpl(Payment payment, OAuthTokenCredential oAuth,
			String host) {
		this.payment = payment;
		this.oAuth = oAuth;
		this.host = host;
	}

	@Override
	@Transactional
	public PagamentoPmo createPagamento(PagamentoPmo pmo) {

		/*
		 * 1) Validazione Pmo
		 */
		pagamentoValidator.validaAcquisto(host, pmo);

		/*
		 * 2) Se valido chiamo offertaValidator altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(pmo)) {
			return pmo;
		}

		// List<Long> offertaIDs = FilterUtil.extractIDs(pmo.getListaOfferta());

		List<Offerta> listaOfferte = offertaRepository.findAll(pmo
				.getListaIdOfferta());

		for (Offerta offerta : listaOfferte) {
			offertaValidator.validaStatoOffertaDaAggiornare(offerta);
		}

		// Pagamento pagamento = pagamentoMapper.pagamentoPmoToPagamento(pmo);

		/* ******************************* */
		/* START COMMUNICATING WITH PAYPAL */
		/* ******************************* */

		/*
		 * STEP 1 :Get an access token.
		 */
		try {

			String accessToken = oAuth.getAccessToken();
			pmo.setAccessToken(accessToken);

			/*
			 * STEP 3 : Create a payment by constructing a payment object using
			 * access token.
			 */

			APIContext apiContext = new APIContext(accessToken);

			Amount amount = new Amount();
			amount.setCurrency(Definitions.Paypal.CURRENCY_EUR);
			amount.setTotal(pmo.getImportoTotale().toString());

			Transaction transaction = new Transaction();
			transaction.setDescription("Creating a payment: "
					+ pmo.getTitoloAcquisto());
			transaction.setAmount(amount);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			Payer payer = new Payer();
			payer.setPaymentMethod(Definitions.Paypal.PAYPAL);

			payment.setIntent(Definitions.Paypal.SALE);
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl(host + RestUrl.GESTORE + RestUrl.OFFERTA
					+ RestUrl.CANCELLA);
			redirectUrls.setReturnUrl(host + RestUrl.GESTORE + RestUrl.OFFERTA
					+ RestUrl.ORDINE);
			payment.setRedirectUrls(redirectUrls);

			Payment createdPayment = payment.create(apiContext);

			pmo.setPagamentoID(createdPayment.getId());
			pmo.setDataPagamento(DateTimeFormat.forPattern(
					DateMapper.DATE_TIME_OFFSET_ISO_8601_FORMAT).parseDateTime(
					createdPayment.getCreateTime()));
			pmo.setStato(createdPayment.getState());

			if (Definitions.Paypal.CREATED.equals(pmo.getStato())) {
				for (Links link : createdPayment.getLinks()) {
					if (link.getMethod().equals(Definitions.Paypal.REDIRECT)) {
						pmo.setRedirectURL(link.getHref());
						break;
					}
				}
			} else if (Definitions.Paypal.EXPIRED.equals(pmo.getStato())) {
				throw new ErroreSistema(
						SignalDescriptor.PAGAMENTO_PAYPAL_CREAZIONE_EXPIRED_EB);
			} else if (Definitions.Paypal.FAILED.equals(pmo.getStato())) {
				throw new ErroreSistema(
						SignalDescriptor.PAGAMENTO_PAYPAL_CREAZIONE_FAILED_EB);
			}

		} catch (PayPalRESTException e) {
			LOG.error(e.getLocalizedMessage(), e);
			// TODO Implementare modo per aggiungere signal con messaggio
			// passato da sistema third-party
			// throw new
			// ErroreSistema(MessaggiBusinessSignalUtils.getMessage(SignalDescriptor.PAGAMENTO_PAYPAL_ERRORE_COMUNICAZIONE_ES,
			// e.getLocalizedMessage())
			// SignalDescriptor.PAGAMENTO_PAYPAL_ERRORE_FILE_CONFIGURAZIONE_ES+
			// ": " + e.getLocalizedMessage());
		}

		return pmo;
	}

	@Override
	@Transactional
	public PagamentoPmo visualizzaOrdine(PagamentoPmo pmo) {

		/*
		 * 1) Validazione Pmo
		 */
		pagamentoValidator.validaOrdine(host, pmo, pmo);

		/*
		 * 2) Valido chiamo e chiamo return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(pmo)) {
			return pmo;
		}

		/*
		 * STEP 4 : Return order resume page before paying authorization
		 * Visualizzare una pagina con bottone PAY NOW o OK (cancella) TODO il
		 * web client deve creare una pagina web con le informazioni nel pmo di
		 * ritorno
		 */

		return pmo;
	}

	@Override
	@Transactional
	public PagamentoPmo eseguiPagamento(PagamentoPmo pmo) {

		/*
		 * 1) Validazione Pmo
		 */
		pagamentoValidator.validaPagamento(host, pmo);

		/*
		 * 2) Se valido avvio la chiamata a paypal altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(pmo)) {
			return pmo;
		}

		/*
		 * STEP 4 : authorize payment after user clicked PAY NOW button
		 */

		try {
			APIContext apiContext = new APIContext(pmo.getAccessToken());

			payment.setId(pmo.getPagamentoID());
			PaymentExecution paymentExecute = new PaymentExecution();
			paymentExecute.setPayerId(pmo.getPayerID());
			// TODO implementare timeout dell'execute payment per gestire dubbia
			payment = payment.execute(apiContext, paymentExecute);
			pmo.setStato(payment.getState());
			pmo.setDataPagamento(DateTimeFormat.forPattern(
					DateMapper.DATE_TIME_OFFSET_ISO_8601_FORMAT).parseDateTime(
					payment.getUpdateTime()));
			LOG.info("Payment: " + payment);
		} catch (PayPalRESTException e) {
			LOG.error(e.getLocalizedMessage(), e);
			// TODO Implementare modo per aggiungere signal con messaggio
			// passato da sistema third-party
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAYPAL_ERRORE_TRANSAZIONE_ES);
		}

		// pmo = pagamentoMapper.paymentToPagamentoPmo(payment);

		if (Definitions.Paypal.EXPIRED.equals(pmo.getStato())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAYPAL_EXPIRED_EB);
		} else if (Definitions.Paypal.FAILED.equals(pmo.getStato())) {
			throw new ErroreSistema(SignalDescriptor.PAGAMENTO_PAYPAL_FAILED_EB);
		} else if (Definitions.Paypal.APPROVED.equals(pmo.getStato())) {

			Pagamento pagamento = pagamentoMapper.pmoToEntity(pmo);

			/*
			 * 3) Scrittura su repository
			 */

			pagamento = pagamentoRepository.save(pagamento);

			for (Offerta offerta : pagamento.getOfferte()) {
				// offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);
				// offertaRepository.save(offerta);
				offertaRepository.updateStatoById(offerta.getId(),
						StatoOfferta.PUBBLICATA);
			}

			/*
			 * Mapping da Entita' a PMO
			 */

			pmo = pagamentoMapper.entityToPmo(pagamento);
		}

		return pmo;
	}
}
