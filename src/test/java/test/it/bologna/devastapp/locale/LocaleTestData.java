package test.it.bologna.devastapp.locale;

import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

public class LocaleTestData {

	static public String DESCRIZIONE = "Descrizione";
	static public String NOME = "Nome";

	public static Locale getLocale(Gestore gestore, Posizione posizione) {

		Locale locale = new Locale();
		locale.setDescrizione(DESCRIZIONE);
		locale.setNome(NOME);

		locale.setIdGestore(gestore.getId());
		locale.setPosizione(posizione);
		locale.setFidelizzazione(true);

		return locale;

	}

	public static LocalePmo getLocalePmo(GestorePmo gestorePmo,
			PosizionePmo posizionePmo) {

		LocalePmo localePmo = new LocalePmo();
		localePmo.setDescrizione(DESCRIZIONE);
		localePmo.setNome(NOME);
		localePmo.setIdGestore(gestorePmo.getId());
		localePmo.setPosizione(posizionePmo);
		localePmo.setFidelizzazione(true);

		return localePmo;
	}

}
