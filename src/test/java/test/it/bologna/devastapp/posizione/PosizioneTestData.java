package test.it.bologna.devastapp.posizione;

import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

public class PosizioneTestData {

	public static String CITTA_VITTORIA = "Vittoria";
	public static String NUMERO_29 = "29";
	public static String INDIRIZZO_VIA_CACC = "LAllalero";
	public static String CAP_97019 = "97019";
	public static Double LONGITUDINE_4_4 = 36.951711;
	public static Double LATITUDINE_3_3 = 14.532585;
	public static Double LATITUDINE_PZA_MAGGIORE = 44.493702;
	public static Double LONGITUDINE_PZA_MAGGIORE = 11.343134;
	public static Double LATITUDINE_DUE_TORRI = 44.493702;
	public static Double LONGITUDINE_DUE_TORRI = 11.343134;

	public static Posizione getPosizione() {

		Posizione posizione = new Posizione();
		posizione.setLatitudine(LATITUDINE_3_3);
		posizione.setLongitudine(LONGITUDINE_4_4);
		posizione.setCitta(CITTA_VITTORIA);
		posizione.setCap(CAP_97019);
		posizione.setNumero(NUMERO_29);
		posizione.setIndirizzo(INDIRIZZO_VIA_CACC);

		return posizione;
	}

	public static Posizione getCasaZacconi() {

		// 44.504299,11.364492

		Posizione posizione = new Posizione();
		posizione.setLatitudine(44.504299);
		posizione.setLongitudine(11.364492);
		posizione.setCitta("Bologna");
		posizione.setCap("40127");
		posizione.setNumero("");
		posizione.setIndirizzo("via Zacconi, 4");

		return posizione;

	}

	public static Posizione getPiazzaMaggiore() {

		// Piazza Maggiore 44.493702,11.343134

		Posizione posizione = new Posizione();
		posizione.setLatitudine(LATITUDINE_PZA_MAGGIORE);
		posizione.setLongitudine(LONGITUDINE_PZA_MAGGIORE);
		posizione.setCitta("Bologna");
		posizione.setCap("40127");
		posizione.setNumero("");
		posizione.setIndirizzo("Piazza Maggiore");

		return posizione;

	}

	public static Posizione getPiazzaVerdi() {

		// Piazza Verdi 44.496207, 11.350776

		Posizione posizione = new Posizione();
		posizione.setLatitudine(44.496207);
		posizione.setLongitudine(11.350776);
		posizione.setCitta("Bologna");
		posizione.setCap("40127");
		posizione.setNumero("");
		posizione.setIndirizzo("Piazza Verdi");

		return posizione;
	}

	public static Posizione getDueTorri() {

		// Due Torri 44.494366, 11.346596

		Posizione posizione = new Posizione();
		posizione.setLatitudine(44.494366);
		posizione.setLongitudine(11.346596);
		posizione.setCitta("Bologna");
		posizione.setCap("40127");
		posizione.setNumero("");
		posizione.setIndirizzo("Due Torri");

		return posizione;
	}

	// 44.494953, 11.344184

	public static Posizione getCelticDruid() {

		Posizione posizione = new Posizione();
		posizione.setLatitudine(44.494953);
		posizione.setLongitudine(11.344184);
		posizione.setCitta("Bologna");
		posizione.setCap("40127");
		posizione.setNumero("");
		posizione.setIndirizzo("Celtic Druid");

		return posizione;
	}

	public static PosizionePmo getPosizionePmo() {

		PosizionePmo posizionePmo = new PosizionePmo();
		posizionePmo.setLatitudine(LATITUDINE_3_3);
		posizionePmo.setLongitudine(LONGITUDINE_4_4);
		posizionePmo.setCitta(CITTA_VITTORIA);
		posizionePmo.setCap(CAP_97019);
		posizionePmo.setNumero(NUMERO_29);
		posizionePmo.setIndirizzo(INDIRIZZO_VIA_CACC);

		return posizionePmo;
	}

	public static PosizionePmo getPosizionePmo(Double latitudine,
			Double longitudine) {

		PosizionePmo posizionePmo = new PosizionePmo();
		posizionePmo.setLatitudine(latitudine);
		posizionePmo.setLongitudine(longitudine);

		return posizionePmo;
	}
}
