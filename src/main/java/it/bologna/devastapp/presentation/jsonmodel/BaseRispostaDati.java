package it.bologna.devastapp.presentation.jsonmodel;

import it.bologna.devastapp.business.signal.BusinessSignal;

import java.util.List;

public abstract class BaseRispostaDati {

	/** lista di segnalazioni legate all'operazione */
	private List<BusinessSignal> listaBusinessSignal;

	public List<BusinessSignal> getListaBusinessSignal() {
		return listaBusinessSignal;
	}

	public void setListaBusinessSignal(List<BusinessSignal> listaBusinessSignal) {
		this.listaBusinessSignal = listaBusinessSignal;
	}

}
