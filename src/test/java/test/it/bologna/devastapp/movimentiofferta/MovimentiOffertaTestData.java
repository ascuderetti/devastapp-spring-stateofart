package test.it.bologna.devastapp.movimentiofferta;

import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Stato;
import test.it.bologna.devastapp.utente.MovimentoUtenteTestData;

public class MovimentiOffertaTestData extends MovimentoUtenteTestData {

	public static MovimentiOfferta getMovimentiOfferta(Long idUtente,
			Long idOfferta, Stato stato) {

		MovimentiOfferta movimentiOfferta = new MovimentiOfferta();
		movimentiOfferta.setIdUtente(idUtente);
		movimentiOfferta.setIdOfferta(idOfferta);
		movimentiOfferta.setStato(stato);

		return movimentiOfferta;

	}

}
