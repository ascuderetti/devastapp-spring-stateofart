package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Prodotto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

	@Override
	@Transactional(readOnly = true)
	public Prodotto findOne(Long id);

	@Override
	@Transactional(readOnly = true)
	public List<Prodotto> findAll();

	@Override
	@Transactional
	public <S extends Prodotto> S save(S arg0);

}
