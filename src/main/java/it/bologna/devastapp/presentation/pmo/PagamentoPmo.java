package it.bologna.devastapp.presentation.pmo;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

public class PagamentoPmo extends BasePmo {

	private static final long serialVersionUID = 7284096974924328198L;

	// Campi del validaAquisto
	private List<Long> listaIdOfferta;
	private Long idGestore;
	private String titoloAcquisto;
	private BigDecimal importoTotale;

	// Campi del validaOrdine
	private DateTime dataPagamento;
	private String pagamentoID;
	private String redirectURL;
	private String accessToken;
	private String stato;

	// Campi del validaPagamento
	private String payerID;

	public List<Long> getListaIdOfferta() {
		return listaIdOfferta;
	}

	public void setListaIdOfferta(List<Long> listaIdOfferta) {
		this.listaIdOfferta = listaIdOfferta;
	}

	public String getTitoloAcquisto() {
		return titoloAcquisto;
	}

	public void setTitoloAcquisto(String titoloAcquisto) {
		this.titoloAcquisto = titoloAcquisto;
	}

	public Long getIdGestore() {
		return idGestore;
	}

	public void setIdGestore(Long idGestore) {
		this.idGestore = idGestore;
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

	public String getPagamentoID() {
		return pagamentoID;
	}

	public void setPagamentoID(String pagamentoID) {
		this.pagamentoID = pagamentoID;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getPayerID() {
		return payerID;
	}

	public void setPayerID(String payerID) {
		this.payerID = payerID;
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
