package test.it.bologna.devastapp.notifiche;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.it.bologna.devastapp.util.TestDefinitionAndUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-notifiche-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
// @Transactional => commentato, spiegazione su javadoc
// NotificheGatewayFollowOffertaTest
public abstract class AbstractNotificheTest {

	@After
	public void tearDown() throws Exception {

		TestDefinitionAndUtils.pulisciTabelleTargetDb();

	}

}
