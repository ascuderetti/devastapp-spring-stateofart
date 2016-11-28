package test.it.bologna.devastapp.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BusinessSignalTest.class, CoordinateTest.class,
		JodaTimeTest.class, JsonResponseUtilTest.class, MapperTest.class,
		LoggingTest.class, TestControllerTest.class })
public class CommonTestSuite {

}
