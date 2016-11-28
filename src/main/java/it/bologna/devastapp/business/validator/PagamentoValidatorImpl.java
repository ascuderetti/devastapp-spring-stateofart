package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.pmo.BasePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PagamentoValidatorImpl implements PagamentoValidator {

	@Override
	public void validaAcquisto(String webAppHost, PagamentoPmo pmo) {

		if (CollectionUtils.isEmpty(pmo.getListaIdOfferta())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_LISTA_OFFERTA_VUOTA_ES);
		}

		// Il pagamento DEVE essere associato ad un gestore
		if (pmo.getIdGestore() == null) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_GESTORE_NON_PRESENTE_ES);
		}

		// DEVE essere presente un titolo di acquisto
		if (StringUtils.isEmpty(pmo.getTitoloAcquisto())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_TITOLO_ACQUISTO_NON_PRESENTE_ES);
		}

		// L'importo totale non deve essere null o minore-uguale a zero
		if (pmo.getImportoTotale() == null
				|| pmo.getImportoTotale().compareTo(BigDecimal.ZERO) < 0) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_IMPORTO_TOTALE_NON_VALIDO_ES);
		}
	}

	// TODO inserire lista di ids delle offerte e verificarla

	@Override
	public void validaOrdine(String webAppHost, PagamentoPmo pmo,
			BasePmo pmoPerSignal) {

		validaAcquisto(webAppHost, pmo);

		// DEVE essere presente l'id della transazione
		if (StringUtils.isEmpty(pmo.getPagamentoID())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAGAMENTO_ID_NON_PRESENTE_ES);
		}

		// DEVE essere presente la pagina di redirect
		if (StringUtils.isEmpty(pmo.getRedirectURL())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_REDIRECT_URL_NON_PRESENTE_ES);
		}

		// DEVE essere presente la pagina di redirect ordina o cancella - TODO
		// if (!((webAppHost + RestUrl.GESTORE + RestUrl.OFFERTA +
		// RestUrl.ORDINE)
		// .equalsIgnoreCase(pmo.getRedirectURL()) || (webAppHost
		// + RestUrl.GESTORE + RestUrl.OFFERTA + RestUrl.CANCELLA)
		// .equalsIgnoreCase(pmo.getRedirectURL()))) {
		// throw new ErroreSistema(
		// SignalDescriptor.PAGAMENTO_REDIRECT_URL_NON_VALIDA_ES);
		// }

		// Il token per accedere alle api paypal DEVE essere presente
		if (StringUtils.isEmpty(pmo.getAccessToken())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_ACCESS_TOKEN_NON_PRESENTE_ES);
		}

		// La data pagamento non DEVE essere nulla
		// non controlliamo il valore della data dovuto alla possibile
		// disincronizzazione dei server in gioco
		if (pmo.getDataPagamento() == null
		/* || pmo.getDataPagamento().isAfterNow() */) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_DATA_NON_VALIDA_ES);
		}

		// Lo stato del pagamento non DEVE essere null
		if (StringUtils.isEmpty(pmo.getStato())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_STATO_NON_PRESENTE_ES);
		}

		// Lo stato del pagamento DEVE essere CREATED o CANCELLED
		if (Definitions.Paypal.APPROVED.equalsIgnoreCase(pmo.getStato())
				|| Definitions.Paypal.FAILED.equalsIgnoreCase(pmo.getStato())
				|| Definitions.Paypal.EXPIRED.equalsIgnoreCase(pmo.getStato())) {
			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.PAGAMENTO_STATO_NON_VALIDO_EB);
		}
	}

	@Override
	public void validaPagamento(String webAppHost, PagamentoPmo pmo) {

		validaOrdine(webAppHost, pmo, pmo);

		// DEVE essere presente l' id del pagatore
		if (StringUtils.isEmpty(pmo.getPayerID())) {
			throw new ErroreSistema(
					SignalDescriptor.PAGAMENTO_PAYER_ID_NON_PRESENTE_ES);
		}

	}

}
