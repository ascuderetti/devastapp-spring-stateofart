package test.it.bologna.devastapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.presentation.pmo.UtentePmo;
import it.bologna.devastapp.util.CollectionUtils;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-mapper-validator-test.xml" })
public abstract class AbstractMapperValidatorTest {

	/**
	 * Metodo di utilita', verifica che un PMO abbia una determinataSignal
	 * 
	 * @param pmo
	 *            contenente la signal
	 * @param sorgenteAttesa
	 * @param signalDescrAtteso
	 * @param parametriMessaggio
	 */
	public static void assertBusinessSignal(
			List<BusinessSignal> listaBusSignal,
			SignalDescriptor signalDescrAtteso, Object... parametriMessaggio) {

		boolean bsTrovata = false;

		// Creo il messaggio di errore
		String messaggio = MessaggiBusinessSignalUtils.getMessage(
				signalDescrAtteso.getMessaggio(), parametriMessaggio);

		// Creo la business signal attesa
		BusinessSignal bsExpected = new BusinessSignal(
				signalDescrAtteso.getCodice(), messaggio,
				signalDescrAtteso.getTipoMessaggio(),
				signalDescrAtteso.getSorgente());

		assertNotNull("listaBusSignal NULL", listaBusSignal);

		// ciclo le signal
		for (BusinessSignal bs : listaBusSignal) {

			// verifico se la signal e' quella che cerco
			if (MessaggiBusinessSignalUtils.isBusinessSignalsEquals(bsExpected,
					bs)) {
				bsTrovata = true;
				break;
			}
		}

		assertTrue("BusinessSignal NON Trovata", bsTrovata);
	}

	public void assertLocaleEntityEqualLocalePmo(Locale entity, LocalePmo pmo) {
		assertEquals("campo non mappato correttamente", entity.getId(),
				pmo.getId());
		assertEquals("campo non mappato correttamente", entity.getIdGestore(),
				pmo.getIdGestore());
		assertNotNull("campo non mappato correttamente", pmo.getPosizione());
		assertEquals("campo non mappato correttamente", entity.getNome(),
				pmo.getNome());
		assertEquals("campo non mappato correttamente",
				entity.getDescrizione(), pmo.getDescrizione());
		assertEquals("campo non mappato correttamente",
				entity.getFidelizzazione(), pmo.getFidelizzazione());
	}

	public void assertOffertaEntityEqualsOffertaPmo(Offerta offerta,
			OffertaScritturaPmo offertaPmo) {
		assertEquals("Data fine non mappata", offerta.getDataFine(),
				offertaPmo.getDataFine());
		assertEquals("Data inizio non mappata", offerta.getDataInizio(),
				offertaPmo.getDataInizio());
		assertEquals("Descrizione non mappata", offerta.getDescrizione(),
				offertaPmo.getDescrizione());
		assertEquals("Id non mappato", offerta.getId(), offertaPmo.getId());
		assertEquals("Prezzo non mappato", offerta.getPrezzoUnitarioPieno(),
				offertaPmo.getPrezzoUnitarioPieno());
		assertEquals("Quantita' non mappata", offerta.getQuantita(),
				offertaPmo.getQuantita());
		assertEquals("Titolo non mappato", offerta.getTitolo(),
				offertaPmo.getTitolo());

		assertEquals("Prodotto non mappato", offerta.getProdotto().getId(),
				offertaPmo.getIdProdotto());
		assertEquals(offerta.getLocale().getId(), offertaPmo.getIdLocale());
	}

	public void assertUtenteEntityEqualUtentePmo(Utente utente,
			UtentePmo utentePmo) {
		assertEquals("Id non mappato", utente.getId(), utentePmo.getId());
		assertEquals("Id Telefono non mappato", utente.getIdTelefono(),
				utentePmo.getIdTelefono());
		assertEquals("Tipo Telefono non mappato", utente.getTipoTelefono()
				.toString(), utentePmo.getTipoTelefono());
	}

	public void assertPagamentoEntityEqualPagamentoPmo(Pagamento pagamento,
			PagamentoPmo pagamentoPmo) {
		assertEquals("Data pagamento non mappata",
				pagamento.getDataPagamento(), pagamentoPmo.getDataPagamento());
		assertEquals("Titolo acquisto non mappato",
				pagamento.getTitoloAcquisto(), pagamentoPmo.getTitoloAcquisto());
		assertEquals("Id Paypal Payer non mappato",
				pagamento.getIdPaypalPayer(), pagamentoPmo.getPayerID());
		assertEquals("Id non mappato", pagamento.getId(), pagamentoPmo.getId());
		assertEquals("Id Paypal Payment non mappato",
				pagamento.getIdPaypalPayment(), pagamentoPmo.getPagamentoID());
		assertEquals("Importo Totale non mappato",
				pagamento.getImportoTotale(), pagamentoPmo.getImportoTotale());
		assertEquals("Id Gestore non mappato", pagamento.getIdGestore(),
				pagamentoPmo.getIdGestore());

		for (Long id : pagamentoPmo.getListaIdOfferta()) {
			Offerta off = CollectionUtils.extractEntity(id,
					pagamento.getOfferte());
			assertEquals("Id non mappato", off.getId(), id);
		}
	}

}
