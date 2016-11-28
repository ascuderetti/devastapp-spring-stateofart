package test.it.bologna.devastapp.movimentiofferta;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ MovimentiOffertaCheckRepositoryTest.class,
		MovimentiOffertaCheckValidatorTest.class,
		MovimentiOffertaMapperTest.class,
		MovimentiOffertaRepositoryAuditTest.class,
		MovimentiOffertaRepositoryTest.class,
		MovimentiOffertaServiceTest.class, MovimentiOffertaValidatorTest.class,
		OffertaAppControllerCheckFollowTest.class })
public class MovimentiOffertaTestSuite {

}
