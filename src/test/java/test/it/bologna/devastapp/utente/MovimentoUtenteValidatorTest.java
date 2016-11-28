package test.it.bologna.devastapp.utente;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.validator.MovimentoUtenteCommonValidatorImpl;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.junit.Test;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.movimentiofferta.MovimentiOffertaTestData;

public class MovimentoUtenteValidatorTest extends AbstractMapperValidatorTest {

	// Esistono più implementazioni che estendono l'interfaccia -
	// MovimentoUtenteCommonValidator per cui istanzio direttamente
	// l'implementazione da testare
	MovimentoUtenteCommonValidatorImpl movimentoUtenteValidator = new MovimentoUtenteCommonValidatorImpl();

	@Test(expected = ErroreSistema.class)
	public void testIdOggettoNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movUte = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, null);// idOfferta
													// null

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentoUtenteValidator.validaErroriSistemaPmo(movUte);

	}

	@Test(expected = ErroreSistema.class)
	public void testCheckUtenteNullKo() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movUte = MovimentiOffertaTestData
				.getMovimentoUtentePmo(null, Long.valueOf(1L));

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentoUtenteValidator.validaErroriSistemaPmo(movUte);

	}

}
