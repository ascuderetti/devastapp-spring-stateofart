package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.StatoOfferta;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OffertaRepository extends JpaRepository<Offerta, Long>,
		OffertaRepositoryCustom {

	@Override
	@Transactional
	public <S extends Offerta> S save(S offerta);

	@Override
	public List<Offerta> findAll();

	@Transactional(readOnly = true)
	public List<Offerta> findByDataInizioBeforeAndDataFineAfterAndStatoOfferta(
			DateTime inizioBefore, DateTime fineAfter, StatoOfferta statoOfferta);

}
