package it.bologna.devastapp.business;

import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

public interface PagamentoService {

	public PagamentoPmo createPagamento(PagamentoPmo pmo);

	public PagamentoPmo visualizzaOrdine(PagamentoPmo pmo);

	public PagamentoPmo eseguiPagamento(PagamentoPmo pmo);
}
