package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.BasePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import java.util.List;

public interface OffertaValidator {

	public void validaInserisciOfferta(OffertaScritturaPmo offertaPmo);

	public void validaAggiornaOfferta(Offerta offertaDaAggiornare,
			OffertaScritturaPmo offertaPmo, BasePmo pmoPerSignal);

	public List<OffertaScritturaPmo> validaRestituisciListaOfferte(
			PosizionePmo posizionePmo);

	public void validaStatoOffertaDaAggiornare(Offerta offertaDaAggiornare);
}
