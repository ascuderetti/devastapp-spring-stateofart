package it.bologna.devastapp.business.signal;

/**
 * Classe che modella una segnalazione di Business (validazione errata o
 * segnalazioni generiche).
 * 
 * @author ascuderetti
 * 
 */
public class BusinessSignal {

	/** Tipologia di gravita' dell'errore */
	private TipoMessaggio tipoMessaggio;

	/** Codice associato alla BusinessSignal */
	private String codice;

	/** Messaggio informativo */
	private String messaggio;
	/**
	 * Sorgente che ha generato la segnalazione (se è un campo sarà il nome del
	 * campo del PMO)
	 */
	private String sorgente;

	/**
	 * Id univoco associato alla signal settato a
	 * Long.toString(System.currentTimeMillis()) dal costruttore. Serve per
	 * recuperare la signal nei LOG in modo univoco.
	 */
	private String uniqueId;

	/**
	 * Costruttore
	 */
	public BusinessSignal(String codice, String messaggio,
			TipoMessaggio tipoMessaggio, String sorgente) {

		this.tipoMessaggio = tipoMessaggio;
		this.codice = codice;
		this.messaggio = messaggio;
		this.sorgente = sorgente;
		this.uniqueId = Long.toString(System.currentTimeMillis());
	}

	/**
	 * Costruttore di default
	 */
	public BusinessSignal() {
	}

	// *******************//
	// **** GET E SET ****//
	// *******************//

	public String getCodice() {
		return codice;
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

	public String getUniqueId() {
		return uniqueId;
	}

	public TipoMessaggio getTipoMessaggio() {
		return tipoMessaggio;
	}

}
