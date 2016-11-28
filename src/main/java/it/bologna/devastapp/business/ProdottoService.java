package it.bologna.devastapp.business;

import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.util.List;

public interface ProdottoService {

	public ProdottoPmo createProdotto(ProdottoPmo prodotto);

	public List<ProdottoPmo> getProdotti();

}
