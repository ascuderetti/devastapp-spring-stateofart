package test.it.bologna.devastapp.utente;

import it.bologna.devastapp.persistence.entity.TipoTelefono;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.UtentePmo;

public class UtenteTestData {

	static public String ID_TELEFONO = "idTelefonoUnique";

	public static Utente getUtente(String idTelefonoUnique) {

		Utente utente = new Utente();
		utente.setIdTelefono(idTelefonoUnique);
		utente.setTipoTelefono(TipoTelefono.IPHONE);

		return utente;

	}

	public static UtentePmo getUtentePmo() {

		UtentePmo utentePmo = new UtentePmo();
		utentePmo.setIdTelefono(ID_TELEFONO);
		utentePmo.setTipoTelefono(TipoTelefono.IPHONE.name());

		return utentePmo;
	}
}
