package test.it.bologna.devastapp.offerta;

import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public final class OffertaTestData {

	static public String DESCRIZIONE = "Descrizione (1)";
	static public String TITOLO = "Titolo (1)";
	static int QUANTITA_100 = 100;

	public static Offerta getOfferta() {

		Prodotto prodotto = ProdottoTestData.getProdottoEsistente();
		Gestore gestore = GestoreTestData.getGestore();
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);

		return offerta;

	}

	public static Offerta getOfferta(Prodotto prodotto, Locale locale) {

		Offerta offerta = new Offerta();
		offerta.setDescrizione(DESCRIZIONE);
		offerta.setTitolo(TITOLO);
		offerta.setQuantita(QUANTITA_100);
		offerta.setProdotto(prodotto);
		DateTime now = new DateTime();
		now = now.secondOfMinute().setCopy(0);
		now = now.millisOfSecond().setCopy(0);
		now = now.minus(Minutes.minutes(now.minuteOfHour().get() % 15));
		offerta.setDataInizio(now);
		offerta.setDataFine(offerta.getDataInizio().plus(Hours.ONE));
		offerta.setLocale(locale);
		offerta.setPrezzoUnitarioPieno(BigDecimal.valueOf(5.00));
		offerta.setPrezzoUnitarioScontato(BigDecimal.valueOf(3.00));

		return offerta;
	}

	public static Offerta getOffertaPubblicata(Prodotto prodotto, Locale locale) {

		Offerta offerta = getOfferta(prodotto, locale);
		offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);

		return offerta;
	}

	public static OffertaScritturaPmo getOffertaPmo(LocalePmo localePmo,
			ProdottoPmo prodottoPmo) {

		OffertaScritturaPmo offertaPmo = new OffertaScritturaPmo();
		offertaPmo.setDescrizione(DESCRIZIONE);
		offertaPmo.setIdLocale(localePmo.getId());
		offertaPmo.setQuantita(1);
		offertaPmo.setTitolo(TITOLO);
		offertaPmo.setIdProdotto(prodottoPmo.getId());
		DateTime now = new DateTime();
		now = now.secondOfMinute().setCopy(0);
		now = now.millisOfSecond().setCopy(0);
		now = now.minus(Minutes.minutes(now.minuteOfHour().get() % 15));
		offertaPmo.setDataInizio(now);
		offertaPmo.setDataFine(offertaPmo.getDataInizio().plus(Hours.ONE));
		offertaPmo.setPrezzoUnitarioPieno(BigDecimal.valueOf(5.00));
		offertaPmo.setPrezzoUnitarioScontato(BigDecimal.valueOf(3.00));

		return offertaPmo;

	}
}
