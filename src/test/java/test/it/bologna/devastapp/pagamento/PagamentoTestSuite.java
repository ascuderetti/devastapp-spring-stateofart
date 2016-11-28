package test.it.bologna.devastapp.pagamento;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ PagamentoValidatorTest.class, PagamentoMapperTest.class,
		PagamentoRepositoryTest.class, PagamentoServiceEseguiTest.class,
		PagamentoServiceTest.class, /* PagamentoGestoreControllerTest.class */})
public class PagamentoTestSuite {

}
