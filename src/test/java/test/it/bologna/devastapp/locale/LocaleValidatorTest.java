package test.it.bologna.devastapp.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.LocaleValidator;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class LocaleValidatorTest extends AbstractMapperValidatorTest {

	@Autowired
	LocaleValidator localeValidator;

	@Test
	public void testValidaLocaleKo() {

		/*
		 * 1) Preapara dati di test
		 */

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();

		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		// condizione di errore
		posizionePmo.setCap(null);
		posizionePmo.setIndirizzo(null);
		posizionePmo.setCitta(null);
		posizionePmo.setNumero(null);
		posizionePmo.setLatitudine(null);
		posizionePmo.setLongitudine(null);

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		// condizione di errore
		localePmo.setDescrizione(null);
		localePmo.setIdGestore(null);
		// localePmo.setId(Long.valueOf(1L));
		localePmo.setNome("");

		/*
		 * 2) Chiamata al metodo da testare
		 */
		localeValidator.validaInserimentoLocale(localePmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = localePmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 9, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_GESTORE_NON_PRESENTE);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_NOME_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_DESCRIZIONE_OBB_EB);
		// LOCALE_POSIZIONE_E001 commentata per via del TODO
		// sull'implementazione del LocaleValidatorImpl
		// assertBusinessSignal(localePmo,
		// SignalDescriptor.LOCALE_POSIZIONE_E001);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_INDIRIZZO_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_NUMERO_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_CAP_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_CITTA_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_LONGITUDINE_OBB_EB);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_LATITUDINE_OBB_EB);
	}

	@Test
	public void testValidaLocalePosizioneNullaKo() {

		/*
		 * 1) Preapara dati di test
		 */

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		// condizione di errore
		PosizionePmo posizionePmo = null;

		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		localeValidator.validaInserimentoLocale(localePmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = localePmo
				.getListaBusinessSignal();
		assertEquals("Numero Business Signal Errato", 1, listaBusSignal.size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.LOCALE_POSIZIONE_OBB_EB);

	}

	@Test
	public void testValidaLocaleOk() {

		/*
		 * 1) Preapara dati di test
		 */

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		gestorePmo.setId(Long.valueOf(1L));
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		localeValidator.validaInserimentoLocale(localePmo);

		/*
		 * 3) Assert
		 */
		assertNull("Numero Business Signal Errato",
				localePmo.getListaBusinessSignal());

	}
}
