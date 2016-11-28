package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

public interface MovimentiOffertaValidator {

	public void validaFollowOfferta(MovimentoUtentePmo checkFollowOffertaPmo,
			Offerta offerta, long numCheck);

	public void validaCheckOfferta(MovimentoUtentePmo checkFollowOffertaPmo,
			Offerta offerta, boolean checkGiaPresente, long numCheckSuOfferta);

	public void validaErroriSistemaPmo(MovimentoUtentePmo checkFollowOffertaPmo);
}
