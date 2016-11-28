package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ProdottoValidatorImpl implements ProdottoValidator {

	@Override
	public void validaProdotto(ProdottoPmo prodottoPmo) {

		// La descrizione deve essere NON null e NON vuota
		if (StringUtils.isEmpty(prodottoPmo.getDescrizione())) {

			MessaggiBusinessSignalUtils.aggiungiSignal(prodottoPmo,
					SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB,
					Definitions.Prodotto.PMO_PROP_descrizione);

		}

	}
}
