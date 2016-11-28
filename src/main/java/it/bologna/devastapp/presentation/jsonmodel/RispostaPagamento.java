package it.bologna.devastapp.presentation.jsonmodel;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * 
 * Modella la risposta di un pagamento
 * 
 * @author fbusacca
 * 
 */
public class RispostaPagamento extends BaseRispostaDati {

	/**
	 * id del Pagamento Offerta salvato su db
	 */
	private Long id;

	/**
	 * id del Pagamento Offerta creato da Paypal
	 */
	private String pagamentoID;

	/**
	 * id del Pagatore registrato su Paypal
	 */
	private String payerID;

	/**
	 * URL web di re-direction al sistema di pagamento protetto express-checkout
	 * di paypal
	 */
	private String redirectURL;

	/**
	 * Denominazione del prodotto acquistato tramite transazione paypal
	 */
	private String titoloAcquisto;

	/**
	 * Data del prodotto acquistato tramite transazione paypal
	 */
	private DateTime dataPagamento;

	/**
	 * Importo totale del prodotto acquistato tramite transazione paypal
	 */
	private BigDecimal importoTotale;

	/**
	 * id del Gestore del Pagamento salvato su db
	 */
	private Long idGestore;

	/**
	 * token valido necessario per accedere alle api paypal
	 */
	private String accessToken;

	/**
	 * stato del Pagamento
	 */
	private String stato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPagamentoID() {
		return pagamentoID;
	}

	public void setPagamentoID(String pagamentoID) {
		this.pagamentoID = pagamentoID;
	}

	public String getPayerID() {
		return payerID;
	}

	public void setPayerID(String payerID) {
		this.payerID = payerID;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
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

	public Long getIdGestore() {
		return idGestore;
	}

	public void setIdGestore(Long idGestore) {
		this.idGestore = idGestore;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
}
