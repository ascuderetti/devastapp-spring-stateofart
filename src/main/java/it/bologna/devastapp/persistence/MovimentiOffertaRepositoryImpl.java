package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.QMovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Stato;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

@Repository
@Transactional(readOnly = true)
public class MovimentiOffertaRepositoryImpl implements
		MovimentiOffertaRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long countMovimentiOffertaByIdOffertaByStato(Long idOfferta,
			Stato stato) {

		QMovimentiOfferta qMovOfferta = QMovimentiOfferta.movimentiOfferta;

		JPAQuery query = new JPAQuery(entityManager);

		long numeroCheck = query
				.from(qMovOfferta)
				.where(qMovOfferta.idOfferta.eq(idOfferta).and(
						qMovOfferta.stato.eq(stato))).count();

		return numeroCheck;
	}

	@Override
	public boolean existMovimentiOffertaByIdOffertaAndidUtenteAndStato(
			Long idOfferta, Long idUtente, Stato stato) {

		QMovimentiOfferta qMovOfferta = QMovimentiOfferta.movimentiOfferta;

		JPAQuery query = new JPAQuery(entityManager);

		// se l'utente e' null non ha senso fare la query..
		if (idUtente == null) {
			return false;
		}

		boolean exist = query
				.from(qMovOfferta)
				.where(qMovOfferta.idOfferta.eq(idOfferta)
						.and(qMovOfferta.stato.eq(stato))
						.and(qMovOfferta.idUtente.eq(idUtente))).exists();

		return exist;
	}
}
