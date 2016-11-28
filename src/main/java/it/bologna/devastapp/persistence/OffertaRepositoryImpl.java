package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.QOfferta;
import it.bologna.devastapp.persistence.entity.StatoOfferta;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Repository
@Transactional(readOnly = true)
public class OffertaRepositoryImpl implements OffertaRepositoryCustom {

	Logger LOG = LoggerFactory.getLogger(OffertaRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Offerta> findOffertaOnlineByLocationAndDistance(
			Posizione posizione, double distanza) {

		/*
		 * 1) Ottenere l'entity manager di Hibernate Search
		 */
		FullTextEntityManager fullTextentityManager = Search
				.getFullTextEntityManager(entityManager);

		/*
		 * 2) Ottenere il query builder per l'entita' sulla quale fare la
		 * ricerca
		 */
		// QueryBuilder qb = fullTextentityManager.getSearchFactory()
		// .buildQueryBuilder().forEntity(Offerta.class).get();

		QueryBuilder qbLocale = fullTextentityManager.getSearchFactory()
				.buildQueryBuilder().forEntity(Locale.class).get();

		/*
		 * 3) Creare la query temporale
		 */

		DateTime now = DateTime.now();

		LOG.info("DateTime now:" + now);

		/*
		 * 4) Creare la query spaziale
		 */
		Query localiViciniQuery = qbLocale.spatial()
				.onCoordinates("posizione.location").within(distanza, Unit.KM)
				.ofLatitude(posizione.getLatitudine())
				.andLongitude(posizione.getLongitudine()).createQuery();

		/*
		 * 3) Creare la query stato offerta PAGATA
		 */

		FullTextQuery hibQueryLocale = fullTextentityManager
				.createFullTextQuery(localiViciniQuery, Locale.class);

		List<Locale> resultsLocali = hibQueryLocale.getResultList();

		if (resultsLocali.size() == 0) {
			return Collections.emptyList();
		}

		QOfferta qOfferta = QOfferta.offerta;

		JPAQuery jpaQuery = new JPAQuery(entityManager);
		List<Offerta> result = jpaQuery
				.from(qOfferta)
				.where(qOfferta.statoOfferta.eq(StatoOfferta.PUBBLICATA)
						.and(qOfferta.locale.in(resultsLocali))
						.and(qOfferta.dataInizio.before(now))
						.and(qOfferta.dataFine.after(now))).list(qOfferta);

		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateStatoById(Long id, StatoOfferta statoOfferta) {

		QOfferta qOfferta = QOfferta.offerta;

		JPAUpdateClause jpaUpdateClause = new JPAUpdateClause(entityManager,
				qOfferta);

		jpaUpdateClause.where(qOfferta.id.eq(id))
				.set(qOfferta.statoOfferta, statoOfferta).execute();

	}

}
