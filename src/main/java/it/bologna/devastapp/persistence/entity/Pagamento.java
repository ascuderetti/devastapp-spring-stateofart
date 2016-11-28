package it.bologna.devastapp.persistence.entity;

import it.bologna.devastapp.util.DateTimeBridge;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.joda.time.DateTime;

/**
 * The persistent class for the pagamento database table.
 * 
 * @author FB
 */

@Entity
@Table(name = "pagamento")
public class Pagamento extends AbstractEntity {

	private static final long serialVersionUID = -875137968504071829L;

	// @ManyToOne
	// @JoinColumn(name = "ID_GESTORE", nullable = false)
	private Long idGestore;

	@Column(name = "ID_PAYPAL_PAYMENT", length = 140)
	private String idPaypalPayment;

	@Column(name = "ID_PAYPAL_PAYER", length = 140)
	private String idPaypalPayer;

	@Column(name = "TITOLO_ACQUISTO", length = 140)
	private String titoloAcquisto;

	@Field
	@FieldBridge(impl = DateTimeBridge.class)
	@Column(name = "DATA_PAGAMENTO")
	private DateTime dataPagamento;

	@Column(name = "IMPORTO_TOTALE", precision = 10, scale = 2)
	private BigDecimal importoTotale;

	// bi-directional many-to-one association to DettaglioPagamento
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "DETTAGLIO_PAGAMENTO", joinColumns = @JoinColumn(name = "ID_PAGAMENTO"), inverseJoinColumns = @JoinColumn(name = "ID_OFFERTA"))
	private List<Offerta> offerte;

	public Long getIdGestore() {
		return idGestore;
	}

	public void setIdGestore(Long idGestore) {
		this.idGestore = idGestore;
	}

	public String getTitoloAcquisto() {
		return titoloAcquisto;
	}

	public void setTitoloAcquisto(String titoloAcquisto) {
		this.titoloAcquisto = titoloAcquisto;
	}

	public DateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(DateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public String getIdPaypalPayment() {
		return idPaypalPayment;
	}

	public void setIdPaypalPayment(String idPaypalPayment) {
		this.idPaypalPayment = idPaypalPayment;
	}

	public String getIdPaypalPayer() {
		return idPaypalPayer;
	}

	public void setIdPaypalPayer(String idPaypalPayer) {
		this.idPaypalPayer = idPaypalPayer;
	}

	public List<Offerta> getOfferte() {
		return offerte;
	}

	public void setOfferte(List<Offerta> offerte) {
		this.offerte = offerte;
	}

}
