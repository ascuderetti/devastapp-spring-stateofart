package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.presentation.pmo.LocalePmo;

public interface LocaleValidator {
	// Richiamato dal corrispondente Service
	// controlla i campi e poi chiama il mapper per mettere i pmo sull'entity

	public void validaInserimentoLocale(LocalePmo locale);

	public void validaAggiornaLocale(LocalePmo locale);

	void validafindByGestore(Long idGestore);
}
