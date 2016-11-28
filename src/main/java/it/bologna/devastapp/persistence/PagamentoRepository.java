package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Pagamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

	@Override
	@Transactional(readOnly = true)
	public Pagamento findOne(Long id);

	@Override
	@Transactional
	public <S extends Pagamento> S save(S pagamento);

}
