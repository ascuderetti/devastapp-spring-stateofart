package it.bologna.devastapp.business;

import it.bologna.devastapp.presentation.pmo.LocalePmo;

import java.util.List;

public interface LocaleService {

	public LocalePmo createLocale(LocalePmo localePmo);

	public LocalePmo updateLocale(LocalePmo localePmo);

	public List<LocalePmo> findByGestore(Long idGestore);

}
