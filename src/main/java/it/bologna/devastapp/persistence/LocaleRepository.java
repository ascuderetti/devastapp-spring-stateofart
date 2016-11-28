package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Locale;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocaleRepository extends JpaRepository<Locale, Long> {

	@Override
	@Transactional(readOnly = true)
	public Locale findOne(Long id);

	@Override
	@Transactional
	public <S extends Locale> S save(S locale);

	@Transactional(readOnly = true)
	public List<Locale> findByIdGestore(Long idGestore);

}
