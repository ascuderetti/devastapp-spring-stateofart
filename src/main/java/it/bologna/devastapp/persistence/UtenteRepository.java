package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Utente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

	@Override
	@Transactional(readOnly = true)
	public Utente findOne(Long id);

	@Override
	@Transactional
	public <S extends Utente> S save(S entity);

}
