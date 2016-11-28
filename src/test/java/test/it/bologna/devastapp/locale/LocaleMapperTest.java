package test.it.bologna.devastapp.locale;

import it.bologna.devastapp.business.mapper.LocaleMapper;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class LocaleMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	LocaleMapper localeMapper;

	@Test
	public void testLocaleToLocalePmo() {

		/*
		 * 1) Preapara dati di test
		 */

		Gestore gestore = GestoreTestData.getGestore();
		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestore, posizione);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		LocalePmo localePmo = localeMapper.localeToLocalePmo(locale);

		/*
		 * 3) Assert
		 */
		assertLocaleEntityEqualLocalePmo(locale, localePmo);
	}

	@Test
	public void testLocalePmoToLocale() {

		/*
		 * 1) Preapara dati di test
		 */

		GestorePmo gestorePmo = GestoreTestData.getGestorePmo();
		PosizionePmo posizionePmo = PosizioneTestData.getPosizionePmo();
		LocalePmo localePmo = LocaleTestData.getLocalePmo(gestorePmo,
				posizionePmo);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		Locale locale = localeMapper.localePmoToLocale(localePmo);

		/*
		 * 3) Assert
		 */
		assertLocaleEntityEqualLocalePmo(locale, localePmo);
	}
}
