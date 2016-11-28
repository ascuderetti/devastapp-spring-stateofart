package it.bologna.devastapp.presentation.jsonmodel;

import it.bologna.devastapp.business.signal.TipoMessaggio;

import java.io.Serializable;

/**
 * Model: modella informazioni comuni di una risposta al client
 * 
 * @author ascuderetti
 * 
 */
public class BaseRispostaJson implements Serializable {

	/** Generated UID */
	private static final long serialVersionUID = -1175957040211241319L;

	/** True se l'operazione e' andata a buon fine, false altrimenti */
	private boolean successo;

	/** Tipologia di gravita' dell'errore */
	private TipoMessaggio tipoMessaggio;

	/** Messaggio informativo */
	private String messaggio;

	/** Codice associato alla BusinessSignal */
	private String codice;

	/**
	 * Sorgente che ha generato la segnalazione (se è un campo sarà il nome del
	 * campo del PMO)
	 */
	private String sorgente;

	/**
	 * Dati di Business
	 */
	private Object dati;

	// *******************//
	// **** GET E SET ****//
	// *******************//

	public boolean isSuccesso() {
		return successo;
	}

	public void setSuccesso(boolean successo) {
		this.successo = successo;
	}

	public TipoMessaggio getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(TipoMessaggio tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}

	public Object getDati() {
		return dati;
	}

	public void setDati(Object dati) {
		this.dati = dati;
	}

}
