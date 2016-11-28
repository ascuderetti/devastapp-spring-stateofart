package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.util.DateUtils;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import org.springframework.stereotype.Component;

@Component
public class MovimentiOffertaValidatorImpl extends MovimentoUtenteCommonValidatorImpl
		implements MovimentiOffertaValidator {

	@Override
	public void validaFollowOfferta(MovimentoUtentePmo checkFollowOffertaPmo,
			Offerta offerta, long numCheck) {

		validaErroriBusinessComuni(checkFollowOffertaPmo, offerta, numCheck);

	}

	@Override
	public void validaCheckOfferta(MovimentoUtentePmo checkFollowOffertaPmo,
			Offerta offerta, boolean checkGiaPresente, long numCheckSuOfferta) {

		// l'utente ha gia' checkato questa offerta
		if (checkGiaPresente) {
			throw new ErroreSistema(SignalDescriptor.CHECK_OFFERTA_GIA_CHECK_ES);
		}

		validaErroriBusinessComuni(checkFollowOffertaPmo, offerta,
				numCheckSuOfferta);

	}

	private void validaErroriBusinessComuni(
			MovimentoUtentePmo checkFollowOffertaPmo, Offerta offerta,
			long numCheckOfferta) {

		// L'offerta e' OFFLINE
		if (!DateUtils.isOnlineNow(offerta)) {
			MessaggiBusinessSignalUtils.aggiungiSignal(checkFollowOffertaPmo,
					SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);
			return;
		}

		long numOfferteRimanenti = offerta.getQuantita() - numCheckOfferta;

		// La quantita' del prodotto dell'offerta e' esaurita
		if (numOfferteRimanenti == 0) {
			MessaggiBusinessSignalUtils.aggiungiSignal(checkFollowOffertaPmo,
					SignalDescriptor.CHECK_FOLLOW_OFFERTA_QUANTITA_ESAURITA_EB);
			return;
		}

	}

}
