package it.bologna.devastapp.presentation.pmo;

public class LocalePmo extends BasePmo {

	private static final long serialVersionUID = -6425545958605198502L;

	private PosizionePmo posizione;

	private Long idGestore;

	private String nome;

	private Boolean fidelizzazione;

	// private byte[] logo;

	private String descrizione;

	public PosizionePmo getPosizione() {
		return posizione;
	}

	public void setPosizione(PosizionePmo posizione) {
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
