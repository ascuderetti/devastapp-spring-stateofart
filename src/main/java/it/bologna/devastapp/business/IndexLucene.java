package it.bologna.devastapp.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;

@Service
public class IndexLucene {

	@PersistenceContext
	EntityManager entityManager;

	public void indexLucene() throws InterruptedException {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(entityManager);
		MassIndexer massIndexer = fullTextEntityManager.createIndexer();

		try {

			massIndexer.startAndWait();

		} catch (InterruptedException e) {

			throw e;

		}

	}

}
