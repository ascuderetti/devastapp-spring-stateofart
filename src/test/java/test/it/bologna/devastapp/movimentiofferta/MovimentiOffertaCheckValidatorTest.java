package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.MovimentiOffertaValidator;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.offerta.OffertaTestData;

public class MovimentiOffertaCheckValidatorTest extends
		AbstractMapperValidatorTest {

	@Autowired
	MovimentiOffertaValidator movimentiOffertaValidator;

	@Test
	public void testCheckQuantitaKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movOffpmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, Long.valueOf(1L));
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));

		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		boolean checkGiaFatto = false;
		/*
		 * CONDIZIONE DI ERRORE
		 */
		int numCheck = quantitaIniziale;
		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaCheckOfferta(movOffpmo, offerta,
				checkGiaFatto, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movOffpmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_QUANTITA_ESAURITA_EB);
	}

	@Test
	public void testCheckOffertaOfflineInizioKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoUtentePmo
		MovimentoUtentePmo movOffpmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));

		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		boolean checkGiaFatto = false;
		int numCheck = quantitaIniziale - 5;

		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataInizio(new DateTime().plus(Hours.ONE));

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaCheckOfferta(movOffpmo, offerta,
				checkGiaFatto, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movOffpmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);

	}

	@Test
	public void testCheckOffertaOfflineFineKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movOffpmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));

		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		boolean checkGiaFatto = false;
		int numCheck = quantitaIniziale - 5;

		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataFine(new DateTime().minus(Hours.TWO));
		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaCheckOfferta(movOffpmo, offerta,
				checkGiaFatto, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movOffpmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);

	}

	@Test(expected = ErroreSistema.class)
	public void testCheckOffertaGiaCheck() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movOffpmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, Long.valueOf(1L));
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		int numCheck = quantitaIniziale - 5;

		/*
		 * CONDIZIONE DI ERRORE
		 */
		boolean checkGiaFatto = true;

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaCheckOfferta(movOffpmo, offerta,
				checkGiaFatto, numCheck);

	}

	@Test
	public void testCheckOffertaOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movOffpmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		boolean checkGiaFatto = false;
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		int numCheck = quantitaIniziale - 5;

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaCheckOfferta(movOffpmo, offerta,
				checkGiaFatto, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movOffpmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);

	}
}
