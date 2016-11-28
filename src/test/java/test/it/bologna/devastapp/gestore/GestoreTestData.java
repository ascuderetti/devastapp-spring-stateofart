package test.it.bologna.devastapp.gestore;

import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.presentation.pmo.GestorePmo;

public class GestoreTestData {

	static public String EMAIL = "gestoreTest@gmail.com";

	public static Gestore getGestore() {

		Gestore gestore = new Gestore();
		gestore.setMail(EMAIL);

		return gestore;
	}

	public static GestorePmo getGestorePmo() {

		GestorePmo gestorePmo = new GestorePmo();
		gestorePmo.setMail(EMAIL);

		return gestorePmo;
	}
}
