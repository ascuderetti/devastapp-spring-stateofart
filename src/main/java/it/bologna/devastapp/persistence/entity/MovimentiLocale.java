package it.bologna.devastapp.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * The persistent class for the movimenti_locale database table.
 */
@Entity
public class MovimentiLocale extends AbstractEntity {

	private static final long serialVersionUID = -6678132269596053915L;

	@Column(nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private Stato stato;

	private Long idUtente;

	private Long idLocale;

	public MovimentiLocale() {
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public Long getIdLocale() {
		return idLocale;
	}

	public void setIdLocale(Long idLocale) {
		this.idLocale = idLocale;
	}

	public Long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

}