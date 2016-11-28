package test.it.bologna.devastapp.gestore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GestoreRepositoryTest.class,
		GestoreGestoreControllerTest.class, GestoreServiceTest.class,
		GestoreMapperTest.class })
public class GestoreTestSuite {

}
