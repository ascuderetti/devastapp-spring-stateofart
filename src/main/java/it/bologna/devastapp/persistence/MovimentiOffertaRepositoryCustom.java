package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Stato;

public interface MovimentiOffertaRepositoryCustom {

	/**
	 * Data un offerta conta il numero di check o follow
	 * 
	 * @param idOfferta
	 * @param stato
	 * @return numero movimenti con stato passato in input
	 */
	public long countMovimentiOffertaByIdOffertaByStato(Long idOfferta,
			Stato stato);

	public boolean existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
			Long idOfferta, Long idUtente, Stato stato);

}
