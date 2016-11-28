package test.it.bologna.devastapp.initdb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-repository-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class InitLuceneIndexTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Ignore("Serve ad indicizzare il DB")
	public void indexDB() {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(entityManager);
		MassIndexer mi = fullTextEntityManager.createIndexer();

		try {
			mi.startAndWait();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

}
