package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Stato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovimentiOffertaRepository extends
		JpaRepository<MovimentiOfferta, Long>, MovimentiOffertaRepositoryCustom {

	@Override
	@Transactional
	public <S extends MovimentiOfferta> S save(S movimentiOfferta);

	@Transactional(readOnly = true)
	List<MovimentiOfferta> findByIdOffertaAndIdUtenteAndStato(Long idOfferta,
			Long idUtente, Stato stato);

	@Transactional(readOnly = true)
	public List<MovimentiOfferta> findByIdOffertaAndStato(Long idOfferta,
			Stato stato);

	@Override
	@Transactional
	public void delete(MovimentiOfferta movimentiOfferta);

}
