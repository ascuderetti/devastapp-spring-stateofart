package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.presentation.pmo.BasePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

public interface PagamentoValidator {

	// Richiamato dal corrispondente Service
	// controlla i campi e poi chiama il mapper per mettere i pmo sull'entity

	public void validaAcquisto(String webAppHost, PagamentoPmo pmo);

	public void validaOrdine(String webAppHost, PagamentoPmo pmo,
			BasePmo pmoPerSignal);

	public void validaPagamento(String webAppHost, PagamentoPmo pmo);
}
