package test.it.bologna.devastapp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.it.bologna.devastapp.common.CommonTestSuite;
import test.it.bologna.devastapp.locale.LocaleTestSuite;
import test.it.bologna.devastapp.movimentilocale.MovimentiLocaleTestSuite;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestSuite;
import test.it.bologna.devastapp.notifiche.NotificheTestSuite;
import test.it.bologna.devastapp.offerta.OffertaTestSuite;
import test.it.bologna.devastapp.pagamento.PagamentoTestSuite;
import test.it.bologna.devastapp.prodotto.ProdottoTestSuite;
import test.it.bologna.devastapp.utente.UtenteTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CommonTestSuite.class, LocaleTestSuite.class,
		MovimentiOffertaTestSuite.class, NotificheTestSuite.class,
		OffertaTestSuite.class, ProdottoTestSuite.class,
		PagamentoTestSuite.class, MovimentiLocaleTestSuite.class,
		UtenteTestSuite.class })
public class UnitariIntegrazioneSuite {

}
