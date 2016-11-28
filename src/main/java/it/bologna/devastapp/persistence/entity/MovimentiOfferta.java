package it.bologna.devastapp.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * The persistent class for the movimenti_offerta database table.
 * 
 * Questa properties:<br>
 * targetAuditMode = RelationTargetAuditMode.NOT_AUDITED<br>
 * 
 * Indica che le tutte entity annidate NON devono essere "Auditate". In questo
 * caso l'unica entity riferita è:
 * 
 * @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
 * @JoinColumn(name = "ID_UTENTE", nullable = false) private Utente utente;
 * 
 */

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class MovimentiOfferta extends AbstractEntity {

	private static final long serialVersionUID = -7796836522577183727L;

	@Column(nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private Stato stato;

	// TODO:COMMENTATO perche' inzialmente si pensava di registrare l'utente
	// solo quando faceva il follow/check, non in fase di startup dell'app.
	//
	// Decommentare se si torna indietro altrimenti eliminare
	//
	// -----------
	// bi-directional many-to-one association to Utente (cascade = {
	// CascadeType.PERSIST, CascadeType.MERGE })
	// @ManyToOne
	// @JoinColumn(name = "ID_UTENTE", nullable = false)
	// private Utente utente;
	// ----------

	private Long idUtente;

	private Long idOfferta;

	public MovimentiOfferta() {
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public Long getIdOfferta() {
		return idOfferta;
	}

	public void setIdOfferta(Long idOfferta) {
		this.idOfferta = idOfferta;
	}

	public Long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

}