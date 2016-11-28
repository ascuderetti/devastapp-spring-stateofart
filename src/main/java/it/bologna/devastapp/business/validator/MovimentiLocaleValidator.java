package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

public interface MovimentiLocaleValidator extends
		MovimentoUtenteCommonValidator {

	public void validaFollowLocale(MovimentoUtentePmo followLocalePmo);

}
