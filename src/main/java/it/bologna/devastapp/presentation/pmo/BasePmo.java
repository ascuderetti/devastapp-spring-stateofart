package it.bologna.devastapp.presentation.pmo;

import it.bologna.devastapp.business.signal.BusinessSignal;

import java.io.Serializable;
import java.util.List;

/**
 * Classe padre di tutti i PMO, contiene la lista di BusinessSignal e relativi
 * metodi di utilita'.
 * 
 * @author ascuderetti
 * 
 */
public abstract class BasePmo implements Serializable {

	/** Generated serial UID */
	private static final long serialVersionUID = -6589763233231656344L;

	/** Campo ID */
	private Long id;

	/**
	 * Lista segnalazioni di Business
	 */
	private List<BusinessSignal> listaBusinessSignal;

	// *******************//
	// **** GET E SET ****//
	// *******************//

	public List<BusinessSignal> getListaBusinessSignal() {
		return listaBusinessSignal;
	}

	public void setListaBusinessSignal(List<BusinessSignal> listaBusinessSignal) {
		this.listaBusinessSignal = listaBusinessSignal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
