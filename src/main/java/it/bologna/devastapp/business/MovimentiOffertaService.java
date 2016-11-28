package it.bologna.devastapp.business;

import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

public interface MovimentiOffertaService {

	public MovimentoUtentePmo followOfferta(
			MovimentoUtentePmo checkFollowOffertaPmo);

	public MovimentoUtentePmo checkOfferta(
			MovimentoUtentePmo checkFollowOffertaPmo);
}
