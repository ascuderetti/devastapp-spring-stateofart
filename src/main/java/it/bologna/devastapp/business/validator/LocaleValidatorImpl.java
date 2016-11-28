package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class LocaleValidatorImpl implements LocaleValidator {

	@Override
	public void validafindByGestore(Long idGestore) {

		if (idGestore == null) {
			// TODO da sistemare
			throw new ErroreSistema(null);
		}

	}

	@Override
	public void validaInserimentoLocale(LocalePmo localePmo) {

		if (localePmo.getId() != null) {

			throw new ErroreSistema(SignalDescriptor.INSERIMENTO_CON_ID_ES);
		}

		validaCampiLocale(localePmo);
	}

	@Override
	public void validaAggiornaLocale(LocalePmo localePmo) {

		if (localePmo.getId() == null) {

			throw new ErroreSistema(SignalDescriptor.AGGIORNAMENTO_SENZA_ID_ES);
		}
		validaCampiLocale(localePmo);
	}

	private void validaCampiLocale(LocalePmo localePmo) {

		// La descrizione deve essere NON null e NON vuota
		if (StringUtils.isEmpty(localePmo.getDescrizione())) {
			MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
					SignalDescriptor.LOCALE_DESCRIZIONE_OBB_EB);
		}

		// Il nome deve essere NON null e NON vuota
		if (StringUtils.isEmpty(localePmo.getNome())) {
			MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
					SignalDescriptor.LOCALE_NOME_OBB_EB);
		}

		// Il locale DEVE essere associato ad un gestore
		if (localePmo.getIdGestore() == null) {
			MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
					SignalDescriptor.LOCALE_GESTORE_NON_PRESENTE);
		}

		// Il locale DEVE avere un indirizzo
		if (localePmo.getPosizione() == null) {
			MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
					SignalDescriptor.LOCALE_POSIZIONE_OBB_EB);
		} else {

			PosizionePmo temp = localePmo.getPosizione();
			if (temp.getId() != null) {
				// TODO
			}

			if (temp.getLatitudine() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_LATITUDINE_OBB_EB);
			}
			if (temp.getLongitudine() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_LONGITUDINE_OBB_EB);
			}
			if (temp.getNumero() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_NUMERO_OBB_EB);
			}
			if (temp.getIndirizzo() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_INDIRIZZO_OBB_EB);
			}
			if (temp.getCap() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_CAP_OBB_EB);
			}
			if (temp.getCitta() == null) {
				MessaggiBusinessSignalUtils.aggiungiSignal(localePmo,
						SignalDescriptor.LOCALE_POSIZIONE_CITTA_OBB_EB);
			}
		}

		// TODO LOGO OBBLIGATORIO???
		// Il locale DEVE avere un logo associato
	}
}
