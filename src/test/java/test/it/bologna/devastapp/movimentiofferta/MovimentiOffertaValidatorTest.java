package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.BusinessSignal;
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

public class MovimentiOffertaValidatorTest extends AbstractMapperValidatorTest {

	@Autowired
	MovimentiOffertaValidator movimentiOffertaValidator;

	@Test
	public void testFollowQuantitaKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentiOffertaPmo
		MovimentoUtentePmo movimentiOffertaPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		/*
		 * CONDIZIONE DI ERRORE
		 */
		int numCheck = quantitaIniziale;
		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaFollowOfferta(movimentiOffertaPmo,
				offerta, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movimentiOffertaPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_QUANTITA_ESAURITA_EB);
	}

	@Test
	public void testFollowOffertaOfflineInizioKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentiOffertaPmo
		MovimentoUtentePmo movimentiOffertaPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		int numCheck = quantitaIniziale - 5;
		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataInizio(new DateTime().plus(Hours.ONE));
		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaFollowOfferta(movimentiOffertaPmo,
				offerta, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movimentiOffertaPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);
	}

	@Test
	public void testFollowOffertaOfflineFineKoBs() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentiOffertaPmo
		MovimentoUtentePmo movimentiOffertaPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		int numCheck = quantitaIniziale - 5;

		/*
		 * CONDIZIONE DI ERRORE
		 */
		offerta.setDataFine(new DateTime().minus(Hours.TWO));
		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaFollowOfferta(movimentiOffertaPmo,
				offerta, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movimentiOffertaPmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.CHECK_FOLLOW_OFFERTA_SCADUTA_EB);
	}

	@Test
	public void testFollowOffertaOk() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// MovimentoOffertaPmo
		MovimentoUtentePmo movimentiOffertaPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(Long.valueOf(1L));
		int quantitaIniziale = 10;
		offerta.setQuantita(quantitaIniziale);
		int numCheck = quantitaIniziale - 5;

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		movimentiOffertaValidator.validaFollowOfferta(movimentiOffertaPmo,
				offerta, numCheck);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		List<BusinessSignal> listaBusSignal = movimentiOffertaPmo
				.getListaBusinessSignal();
		assertNull("Numero Business Signal Errato", listaBusSignal);
	}
}
