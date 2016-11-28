package test.it.bologna.devastapp.locale;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LocaleGestoreControllerTest.class,
		LocaleMapperTest.class, LocaleRepositoryTest.class,
		LocaleServiceTest.class, LocaleValidatorTest.class })
public class LocaleTestSuite {

}
