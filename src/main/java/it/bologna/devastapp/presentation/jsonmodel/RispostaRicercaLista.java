package it.bologna.devastapp.presentation.jsonmodel;

import it.bologna.devastapp.presentation.pmo.BasePmo;

import java.util.List;

/**
 * 
 * @author ascuderetti@gmail.com
 *
 * @param <Pmo>
 */
public class RispostaRicercaLista<Pmo extends BasePmo> extends BaseRispostaDati {

	private List<Pmo> lista;

	public List<Pmo> getLista() {
		return lista;
	}

	public void setLista(List<Pmo> lista) {
		this.lista = lista;
	}

}
