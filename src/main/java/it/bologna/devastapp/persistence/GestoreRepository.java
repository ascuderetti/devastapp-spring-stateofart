package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Gestore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GestoreRepository extends JpaRepository<Gestore, Long> {

	// è il DAO

	@Override
	@Transactional(readOnly = true)
	public Gestore findOne(Long id);

	@Override
	@Transactional
	public <S extends Gestore> S save(S gestore);
}
