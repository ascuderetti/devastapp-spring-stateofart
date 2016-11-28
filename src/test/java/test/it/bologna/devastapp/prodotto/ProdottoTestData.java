package test.it.bologna.devastapp.prodotto;

import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

public class ProdottoTestData {

	public static String DESCRIZIONE = "Descrizione prodotto esistente";
	public static String DESCRIZIONE_NUOVA = "Descrizione prodotto nuovo";
	public static Long ID_PRODOTTO = new Long(0);

	public static Prodotto getProdottoEsistente() {

		Prodotto prodotto = new Prodotto();
		prodotto.setDescrizione(DESCRIZIONE);
		prodotto.setId(ID_PRODOTTO);

		return prodotto;
	}

	public static Prodotto getProdotto() {

		Prodotto prodotto = new Prodotto();
		prodotto.setDescrizione(DESCRIZIONE_NUOVA);

		return prodotto;
	}

	public static ProdottoPmo getProdottoPmo() {

		ProdottoPmo prodottoPmo = new ProdottoPmo();
		prodottoPmo.setDescrizione(DESCRIZIONE);

		return prodottoPmo;
	}

}
