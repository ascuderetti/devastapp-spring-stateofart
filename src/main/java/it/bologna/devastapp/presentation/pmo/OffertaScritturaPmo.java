package it.bologna.devastapp.presentation.pmo;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class OffertaScritturaPmo extends BasePmo {

	private static final long serialVersionUID = -4027726455716073948L;
	private String titolo;
	private String descrizione;
	private Long idProdotto;
	private DateTime dataInizio;
	private DateTime dataFine;
	private Integer quantita;
	private BigDecimal prezzoUnitarioPieno;
	private BigDecimal prezzoUnitarioScontato;
	private Long idLocale;

	// private StatoOfferta statoOfferta;

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public BigDecimal getPrezzoUnitarioPieno() {
		return prezzoUnitarioPieno;
	}

	public void setPrezzoUnitarioPieno(BigDecimal prezzoUnitarioPieno) {
		this.prezzoUnitarioPieno = prezzoUnitarioPieno;
	}

	public BigDecimal getPrezzoUnitarioScontato() {
		return prezzoUnitarioScontato;
	}

	public void setPrezzoUnitarioScontato(BigDecimal prezzoUnitarioScontato) {
		this.prezzoUnitarioScontato = prezzoUnitarioScontato;
	}

	public DateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(DateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public DateTime getDataFine() {
		return dataFine;
	}

	public void setDataFine(DateTime dataFine) {
		this.dataFine = dataFine;
	}

	public Long getIdLocale() {
		return idLocale;
	}

	public void setIdLocale(Long idLocale) {
		this.idLocale = idLocale;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Long idProdotto) {
		this.idProdotto = idProdotto;
	}

	// public void setStatoOfferta(StatoOfferta statoOfferta) {
	// this.statoOfferta = statoOfferta;
	// }
	//
	// public StatoOfferta getStatoOfferta() {
	// return statoOfferta;
	// }
}
