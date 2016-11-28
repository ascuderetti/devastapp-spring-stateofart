package it.bologna.devastapp.business;

import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaResponse;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import java.util.List;

public interface OffertaService {

	public OffertaScritturaPmo createOfferta(OffertaScritturaPmo offertaPmo);

	public OffertaScritturaPmo updateOfferta(OffertaScritturaPmo offertaPmo);

	public OffertaLetturaResponse getListaOfferteOnlineByRaggioAndPosizione(
			PosizionePmo posizionePmo);

	/**
	 * 
	 * @return le offerte con data inizio minore dei "now", data fine maggiore
	 *         di "now" e in stato "PUBBLICATA"
	 */
	public List<Offerta> getListaOfferteOnline();

}
