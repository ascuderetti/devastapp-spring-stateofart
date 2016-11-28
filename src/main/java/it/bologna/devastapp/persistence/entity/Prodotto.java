package it.bologna.devastapp.persistence.entity;

import javax.persistence.Entity;

@Entity
public class Prodotto extends AbstractEntity {

	private static final long serialVersionUID = 7145024444581684163L;

	private String descrizione;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
