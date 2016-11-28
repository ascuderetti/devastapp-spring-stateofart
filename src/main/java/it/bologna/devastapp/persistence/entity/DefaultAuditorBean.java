package it.bologna.devastapp.persistence.entity;

import org.springframework.data.domain.AuditorAware;

public class DefaultAuditorBean implements AuditorAware<String> {

	/**
	 * Qua andrà l'user che effettua la modifica (preso in sessione)
	 */
	@Override
	public String getCurrentAuditor() {

		return "DEVASTAPP";
	}

}
