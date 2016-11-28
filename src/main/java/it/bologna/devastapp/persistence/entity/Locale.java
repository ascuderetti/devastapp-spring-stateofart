package it.bologna.devastapp.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * Model: Locale
 * 
 * @author fbusacca
 * 
 */
@Table
@Entity
@Indexed
public class Locale extends AbstractEntity {

	private static final long serialVersionUID = 2563181364621152370L;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "ID_POSIZIONE")
	@IndexedEmbedded
	private Posizione posizione;

	private Long idGestore;

	private String nome;

	private Boolean fidelizzazione;

	// @Lob
	// @Basic(fetch = FetchType.LAZY)
	// private byte[] logo;

	private String descrizione;

	public Posizione getPosizione() {
		return posizione;
	}

	public void setPosizione(Posizione posizione) {
		this.posizione = posizione;
	}

	public Long getIdGestore() {
		return idGestore;
	}

	public void setIdGestore(Long idGestore) {
		this.idGestore = idGestore;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	// public byte[] getLogo() {
	// return logo;
	// }
	//
	// public void setLogo(byte[] logo) {
	// this.logo = logo;
	// }

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Boolean getFidelizzazione() {
		return fidelizzazione;
	}

	public void setFidelizzazione(Boolean fidelizzazione) {
		this.fidelizzazione = fidelizzazione;
	}
}
