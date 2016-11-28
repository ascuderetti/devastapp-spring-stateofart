package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.springframework.stereotype.Component;

@Component
public class MovimentiLocaleValidatorImpl extends
		MovimentoUtenteCommonValidatorImpl implements MovimentiLocaleValidator {

	@Override
	public void validaFollowLocale(MovimentoUtentePmo followLocalePmo) {
		// nulla da validare..

	}

}
