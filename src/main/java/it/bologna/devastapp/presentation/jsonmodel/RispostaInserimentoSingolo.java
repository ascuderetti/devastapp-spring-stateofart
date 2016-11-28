package it.bologna.devastapp.presentation.jsonmodel;

/**
 * 
 * Modella la risposta di un inserimento singolo
 * 
 * @author ascuderetti
 * 
 */
public class RispostaInserimentoSingolo extends BaseRispostaDati {

	/** id oggetto inserito, null se l'operazione non e' andata a buon fine */
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
