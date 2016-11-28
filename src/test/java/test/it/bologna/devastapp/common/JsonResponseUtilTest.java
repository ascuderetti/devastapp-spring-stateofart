package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.signal.TipoMessaggio;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.pmo.Test1Pmo;
import it.bologna.devastapp.presentation.pmo.Test2Pmo;
import it.bologna.devastapp.presentation.pmo.Test3Pmo;
import it.bologna.devastapp.util.JsonResponseUtil;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

/**
 * Test per i metodi della classe {@link JsonResponseUtil}
 * 
 * @author ascuderetti
 * 
 */
public class JsonResponseUtilTest extends AbstractMapperValidatorTest {

	@Autowired
	private MessageSource messageSource;

	@Test
	public void rispostaJsonInserimentoSingoloOk() {

		/*
		 * 1) Preapara dati di test
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(11.3322323);
		pos1.setLongitude(1232.42354);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(1L);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		/*
		 * 2) Chiamata al metodo da testare
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		/*
		 * 3) Assert
		 */
		assertTrue(risposta.isSuccesso());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK.getCodice(),
				risposta.getCodice());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK.getMessaggio(),
				risposta.getMessaggio());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK.getSorgente(),
				risposta.getSorgente());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK
						.getTipoMessaggio(),
				risposta.getTipoMessaggio());
		assertTrue(risposta.getDati() instanceof RispostaInserimentoSingolo);
		RispostaInserimentoSingolo rispInsSing = (RispostaInserimentoSingolo) risposta
				.getDati();
		assertNotNull(rispInsSing.getId());
		assertEquals(off1.getId(), rispInsSing.getId());
		assertNull(rispInsSing.getListaBusinessSignal());
		assertEquals(off1.getListaBusinessSignal(),
				rispInsSing.getListaBusinessSignal());

	}

	@Test
	public void rispostaJsonInserimentoSingoloKo() {
		/*
		 * 1) Preapara dati di test
		 */

		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(11.3322323);
		pos1.setLongitude(1232.42354);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		MessaggiBusinessSignalUtils.aggiungiSignal(off1,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		/*
		 * 3) Assert
		 */
		assertFalse(risposta.isSuccesso());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO.getCodice(),
				risposta.getCodice());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO.getMessaggio(),
				risposta.getMessaggio());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO.getSorgente(),
				risposta.getSorgente());
		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO
						.getTipoMessaggio(),
				risposta.getTipoMessaggio());
		assertTrue(risposta.getDati() instanceof RispostaInserimentoSingolo);
		RispostaInserimentoSingolo rispInsSing = (RispostaInserimentoSingolo) risposta
				.getDati();
		assertNull(rispInsSing.getId());
		assertEquals(off1.getId(), rispInsSing.getId());
		assertNotNull(rispInsSing.getListaBusinessSignal());
		assertEquals(off1.getListaBusinessSignal(),
				rispInsSing.getListaBusinessSignal());

	}

	@Test
	public void rispostaJsonI18nKO_EN() {

		/*
		 * 1) Preapara dati di test
		 */

		BaseRispostaJson risposta = new BaseRispostaJson();
		risposta.setMessaggio(SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO
				.getMessaggio());

		RispostaInserimentoSingolo ris = new RispostaInserimentoSingolo();
		List<BusinessSignal> list = new ArrayList<BusinessSignal>();
		BusinessSignal e1 = new BusinessSignal(null,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB.getMessaggio(),
				TipoMessaggio.ERRORE_BUSINESS, null);
		list.add(e1);

		// BusinessSignal e2 = new BusinessSignal(null, "",
		// TipoMessaggio.ERRORE_BUSINESS, null);
		// list.add(e2);

		ris.setListaBusinessSignal(list);
		risposta.setDati(ris);
		risposta.setTipoMessaggio(TipoMessaggio.ERRORE_BUSINESS);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		risposta = JsonResponseUtil.i18nTranslator(risposta, Locale.US,
				messageSource);

		/*
		 * 3) Assert
		 */
		// assertFalse(risposta.isSuccesso());

		assertEquals("Fix the error", risposta.getMessaggio());

		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO
						.getTipoMessaggio(),
				risposta.getTipoMessaggio());
		assertTrue(risposta.getDati() instanceof RispostaInserimentoSingolo);
		RispostaInserimentoSingolo rispInsSing = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull(rispInsSing.getListaBusinessSignal());

		for (BusinessSignal bs : rispInsSing.getListaBusinessSignal()) {
			assertEquals("The field description is required", bs.getMessaggio());
		}
	}

	@Test
	public void rispostaJsonI18nKO_IT() {

		/*
		 * 1) Preapara dati di test
		 */

		BaseRispostaJson risposta = new BaseRispostaJson();
		risposta.setMessaggio(SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO
				.getMessaggio());

		RispostaInserimentoSingolo ris = new RispostaInserimentoSingolo();
		List<BusinessSignal> list = new ArrayList<BusinessSignal>();
		BusinessSignal e1 = new BusinessSignal(null,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB.getMessaggio(),
				TipoMessaggio.ERRORE_BUSINESS, null);
		list.add(e1);

		ris.setListaBusinessSignal(list);
		risposta.setDati(ris);
		risposta.setTipoMessaggio(TipoMessaggio.ERRORE_BUSINESS);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		risposta = JsonResponseUtil.i18nTranslator(risposta, Locale.ITALY,
				messageSource);

		/*
		 * 3) Assert
		 */
		// assertFalse(risposta.isSuccesso());

		assertEquals("Correggi gli errori", risposta.getMessaggio());

		assertEquals(
				SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO
						.getTipoMessaggio(),
				risposta.getTipoMessaggio());
		assertTrue(risposta.getDati() instanceof RispostaInserimentoSingolo);
		RispostaInserimentoSingolo rispInsSing = (RispostaInserimentoSingolo) risposta
				.getDati();

		assertNotNull(rispInsSing.getListaBusinessSignal());

		for (BusinessSignal bs : rispInsSing.getListaBusinessSignal()) {
			assertEquals("Il campo descrizione e'' obbligatorio",
					bs.getMessaggio());
		}

	}
}
