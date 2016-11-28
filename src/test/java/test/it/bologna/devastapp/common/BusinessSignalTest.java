package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.signal.TipoMessaggio;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class BusinessSignalTest extends AbstractMapperValidatorTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(BusinessSignalTest.class);

	@Test
	public void businessSignalErrorTest() {
		String sorgente = "descrizione";
		String parametroMes1 = "descrizione";

		ProdottoPmo pmo = new ProdottoPmo();
		MessaggiBusinessSignalUtils.aggiungiSignal(pmo,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB, parametroMes1);

		/*
		 * ASSERT
		 */
		assertFalse(MessaggiBusinessSignalUtils.isValidPmo(pmo));
		assertEquals(pmo.getListaBusinessSignal().size(), 1);
		// Signal
		BusinessSignal signal = pmo.getListaBusinessSignal().get(0);
		assertEquals(SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB.name(),
				signal.getCodice());
		assertEquals("prodotto_descrizione_obb_eb", signal.getMessaggio());
		assertEquals(TipoMessaggio.ERRORE_BUSINESS, signal.getTipoMessaggio());
		assertEquals(sorgente, signal.getSorgente());
		assertNotNull(signal.getUniqueId());

		LOG.info("Id univoco Signal:" + signal.getUniqueId());
	}

	@Test
	public void businessSignalInfoTest() {
		String descrizione = "test";

		ProdottoPmo pmo = new ProdottoPmo();
		pmo.setDescrizione(descrizione);
		MessaggiBusinessSignalUtils.aggiungiSignal(pmo,
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK,
				pmo.getDescrizione());

		/*
		 * ASSERT
		 */
		assertTrue(MessaggiBusinessSignalUtils.isValidPmo(pmo));
		assertEquals(pmo.getListaBusinessSignal().size(), 1);
		// Signal
		BusinessSignal signal = pmo.getListaBusinessSignal().get(0);
		assertEquals(SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK.name(),
				signal.getCodice());
		assertEquals("Il prodotto e' stato inserito correttamente",
				signal.getMessaggio());
		assertEquals(TipoMessaggio.SUCCESSO, signal.getTipoMessaggio());
		assertNull(signal.getSorgente());
		assertNotNull(signal.getUniqueId());

		LOG.info("Id univoco Signal:" + signal.getUniqueId());
	}
}
