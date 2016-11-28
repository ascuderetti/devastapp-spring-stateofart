package test.it.bologna.devastapp.offerta;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ OffertaGestoreControllerAggiornamentoTest.class,
		OffertaGestoreControllerInserimentoTest.class, OffertaMapperTest.class,
		OffertaRepositoryTest.class, OffertaSearchRepositoryTest.class,
		OffertaServiceTest.class, OffertaValidatorTest.class,
		OffertaLetturaServiceTest.class, OffertaAppControllerRicercaTest.class })
public class OffertaTestSuite {

}
