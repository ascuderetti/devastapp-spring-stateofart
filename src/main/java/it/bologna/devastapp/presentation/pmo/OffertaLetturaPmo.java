package it.bologna.devastapp.presentation.pmo;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * 
 * @author Emanuele Barrano
 * 
 *         Pmo per la lettura delle offerte
 * 
 */

public class OffertaLetturaPmo extends BasePmo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9069797719870231596L;

	private String titolo;
	private String descrizione;
	private ProdottoPmo prodotto;
	private DateTime dataInizio;
	private DateTime dataFine;
	private Integer quantita;
	private Integer quantitaRimanente;
	private BigDecimal prezzoUnitarioPieno;
	private BigDecimal prezzoUnitarioScontato;
	private LocalePmo locale;

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

	public ProdottoPmo getProdotto() {
		return prodotto;
	}

	public void setProdotto(ProdottoPmo prodotto) {
		this.prodotto = prodotto;
	}

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public Integer getQuantitaRimanente() {
		return quantitaRimanente;
	}

	public void setQuantitaRimanente(Integer quantitaRimanente) {
		this.quantitaRimanente = quantitaRimanente;
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

	public LocalePmo getLocale() {
		return locale;
	}

	public void setLocale(LocalePmo locale) {
		this.locale = locale;
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

}
