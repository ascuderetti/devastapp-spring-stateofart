package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.MovimentiLocale;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovimentiLocaleRepository extends
		JpaRepository<MovimentiLocale, Long> {

	@Override
	@Transactional(readOnly = false)
	public <S extends MovimentiLocale> S save(S movimentiLocale);

	@Override
	@Transactional(readOnly = false)
	public void delete(MovimentiLocale movimentiOfferta);

	@Transactional(readOnly = true)
	public List<MovimentiLocale> findByIdLocaleIsIn(List<Long> ids);

}
