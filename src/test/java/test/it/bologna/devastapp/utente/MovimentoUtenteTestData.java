package test.it.bologna.devastapp.utente;

import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

public class MovimentoUtenteTestData {

	public static MovimentoUtentePmo getMovimentoUtentePmo(Long idUtente,
			Long idOggetto) {

		MovimentoUtentePmo movUtentePmo = new MovimentoUtentePmo();
		movUtentePmo.setIdUtente(idUtente);
		movUtentePmo.setIdOggetto(idOggetto);

		return movUtentePmo;
	}
}
