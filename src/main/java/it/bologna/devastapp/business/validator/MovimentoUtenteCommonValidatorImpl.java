package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.springframework.stereotype.Component;

@Component
public class MovimentoUtenteCommonValidatorImpl implements MovimentoUtenteCommonValidator {

	public void validaErroriSistemaPmo(MovimentoUtentePmo movimentoUtente) {

		// Id dell'oggetto da follow/checkare deve essere valorizzato!
		if (movimentoUtente.getIdOggetto() == null) {

			throw new ErroreSistema(
					SignalDescriptor.FOLLOW_CHECK_SENZA_ID_OGGETTO_ES);
		}

		// L' id utente deve essere NON null
		if (movimentoUtente.getIdUtente() == null) {
			throw new ErroreSistema(
					SignalDescriptor.FOLLOW_CHECK_SENZA_ID_UTENTE_ES);

		}
	}

}
