package it.bologna.devastapp.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Field;
import org.joda.time.DateTime;

/**
 * Model: Offerta
 * 
 * @author EB
 * 
 */
@Table(name = "offerta")
@Entity
public class Offerta extends AbstractEntity {

	/** Generated serial UID */
	private static final long serialVersionUID = -3458738008199745281L;

	private String titolo;
	@Field
	private String descrizione;
	@ManyToOne
	@JoinColumn(name = "ID_PRODOTTO")
	private Prodotto prodotto;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime dataInizio;
	private DateTime dataFine;
	private Integer quantita;
	private BigDecimal prezzoUnitarioPieno;
	private BigDecimal prezzoUnitarioScontato;
	@ManyToOne
	@JoinColumn(name = "ID_LOCALE")
	private Locale locale;
	@Enumerated(EnumType.STRING)
	private StatoOfferta statoOfferta;

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

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
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

	public void setPrezzoUnitarioScontato(BigDecimal prezzoOfferta) {
		this.prezzoUnitarioScontato = prezzoOfferta;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public StatoOfferta getStatoOfferta() {
		return statoOfferta;
	}

	public void setStatoOfferta(StatoOfferta statoOfferta) {
		this.statoOfferta = statoOfferta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
