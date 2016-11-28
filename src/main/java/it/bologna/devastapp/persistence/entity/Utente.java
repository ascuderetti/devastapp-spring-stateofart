package it.bologna.devastapp.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * The persistent class for the utente database table.
 * 
 */
@Entity
public class Utente extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_TELEFONO", nullable = false, length = 255)
	private String idTelefono;

	@Column(name = "TIPO_TELEFONO", nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private TipoTelefono tipoTelefono;

	// TODO: attenzione il FetchType.LAZY attualmente non funziona
	// vedi
	// https://kanbanflow.com/t/6d8f2e9ecdd998ad1e55a83918a23b58/lazyinizialitation
	// bi-directional many-to-one association to MovimentiOfferta
	// @OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	// private List<MovimentiOfferta> movimentiOffertas;

	public Utente() {
	}

	public String getIdTelefono() {
		return this.idTelefono;
	}

	public void setIdTelefono(String idTelefono) {
		this.idTelefono = idTelefono;
	}

	public TipoTelefono getTipoTelefono() {
		return tipoTelefono;
	}

	public void setTipoTelefono(TipoTelefono tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

}