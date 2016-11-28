package test.it.bologna.devastapp.utente;

import it.bologna.devastapp.business.mapper.UtenteMapper;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.UtentePmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class UtenteMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	UtenteMapper utenteMapper;

	@Test
	public void testUtenteToUtentePmo() {

		/*
		 * 1) Preapara dati di test
		 */

		Utente utente = UtenteTestData.getUtente("ID_TELEFONO");

		/*
		 * 2) Chiamata al metodo da testare
		 */
		UtentePmo utentePmo = utenteMapper.entityToPmo(utente);

		/*
		 * 3) Assert
		 */
		assertUtenteEntityEqualUtentePmo(utente, utentePmo);
	}

	@Test
	public void testUtentePmoToUtente() {

		/*
		 * 1) Preapara dati di test
		 */

		UtentePmo utentePmo = UtenteTestData.getUtentePmo();

		/*
		 * 2) Chiamata al metodo da testare
		 */
		Utente utente = utenteMapper.pmoToEntity(utentePmo);

		/*
		 * 3) Assert
		 */
		assertUtenteEntityEqualUtentePmo(utente, utentePmo);
	}
}
