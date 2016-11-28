package test.it.bologna.devastapp.movimentilocale;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ MovimentiLocaleRepositoryTest.class,
		MovimentiLocaleMapperTest.class, MovimentiLocaleServiceTest.class,
		LocaleAppControllerFollowTest.class })
public class MovimentiLocaleTestSuite {

}
