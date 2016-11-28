package test.it.bologna.devastapp;

import it.bologna.devastapp.persistence.entity.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.it.bologna.devastapp.util.TestDefinitionAndUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-service-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public abstract class AbstractServiceTest {

	@PersistenceContext
	private EntityManager entityManager;

	@After
	public void tearDown() throws Exception {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(entityManager);
		fullTextEntityManager.purgeAll(Locale.class);
		fullTextEntityManager.flushToIndexes();

		TestDefinitionAndUtils.pulisciTabelleTargetDb();

	}

}
