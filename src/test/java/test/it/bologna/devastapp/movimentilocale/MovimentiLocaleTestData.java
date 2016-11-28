package test.it.bologna.devastapp.movimentilocale;

import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Stato;
import test.it.bologna.devastapp.utente.MovimentoUtenteTestData;

public class MovimentiLocaleTestData extends MovimentoUtenteTestData {

	public static MovimentiLocale getMovimentiLocale(Long idUtente,
			Long idLocale, Stato stato) {

		MovimentiLocale movimentiLocale = new MovimentiLocale();
		movimentiLocale.setIdUtente(idUtente);
		movimentiLocale.setIdLocale(idLocale);
		movimentiLocale.setStato(stato);

		return movimentiLocale;

	}

}
