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
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.api.payments.Payment;
import com.paypal.core.rest.OAuthTokenCredential;

@Service
@Transactional(readOnly = true)
public class PagamentoServiceMockImpl implements PagamentoService {

	private static final Logger LOG = LoggerFactory
			.getLogger(PagamentoServiceMockImpl.class);

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
	public PagamentoServiceMockImpl(Payment payment,
			OAuthTokenCredential oAuth, String host) {
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
		 * STEP 1 & 2 :Get an access token and Create a payment MOCKATO
		 */

		pmo.setPagamentoID(Definitions.Paypal.ID_PAYPAL_PAYMENT_FAKE);
		pmo.setDataPagamento(DateTime.now());
		pmo.setStato(Definitions.Paypal.CREATED);

		if (Definitions.Paypal.CREATED.equals(pmo.getStato())) {
			pmo.setRedirectURL(Definitions.Paypal.REDIRECT_PAYPAL_FAKE);
		} else if (Definitions.Paypal.EXPIRED.equals(pmo.getStato())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAYPAL_CREAZIONE_EXPIRED_EB);
		} else if (Definitions.Paypal.FAILED.equals(pmo.getStato())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAYPAL_CREAZIONE_FAILED_EB);
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
		// TODO rimettere
		// pagamentoValidator.validaPagamento(host, pmo);

		/*
		 * 2) Se valido avvio la chiamata a paypal altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(pmo)) {
			return pmo;
		}

		/*
		 * STEP 4 : authorize payment MOCKATO
		 */

		pmo.setStato(Definitions.Paypal.APPROVED);
		pmo.setDataPagamento(DateTime.now());

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
				offertaRepository.updateStatoById(offerta.getId(),
						StatoOfferta.PUBBLICATA);
				// offertaRepository.save(offerta);
			}

			/*
			 * Mapping da Entita' a PMO
			 */

			pmo = pagamentoMapper.entityToPmo(pagamento);
		}

		return pmo;
	}

}
