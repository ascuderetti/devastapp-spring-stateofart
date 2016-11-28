package test.it.bologna.devastapp.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Posizione;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class LocaleRepositoryTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(LocaleRepositoryTest.class);

	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;

	@Test
	public void testLocaleFindByGestore() {

		// Gestore
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		// Locale
		Locale locale1 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());
		Locale locale2 = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		localeRepository.save(locale1);
		localeRepository.save(locale2);

		List<Locale> localeList = localeRepository
				.findByIdGestore(gestoreResponse.getId());

		assertEquals(2, localeList.size());

	}

	@Test
	public void testInserimentoLocaleGestoreEsistente() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// Gestore
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		// Locale
		Locale locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Locale localeResponse = localeRepository.save(locale);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//
		assertNotNull("Id null", localeResponse.getId());
	}

	@Test
	public void testInserimentoLocaliConStessaPosizione() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		// Gestore
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		// Locale
		Locale locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		Locale locale1 = localeRepository.save(locale);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//
		locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		Locale locale2 = localeRepository.save(locale);

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNotNull("Id null", locale2.getId());
		assertNotEquals(locale1.getId(), locale2.getId());

		assertEquals(locale1.getDescrizione(), locale2.getDescrizione());
		assertEquals(locale1.getPosizione().getCitta(), locale2.getPosizione()
				.getCitta());
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testInserimentoLocaleSenzaGestoreKo() {
		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		Gestore gestore = GestoreTestData.getGestore();

		Posizione posizione = PosizioneTestData.getPosizione();
		Locale locale = LocaleTestData.getLocale(gestore, posizione);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		localeRepository.save(locale);

	}

	@Test
	public void testAggiornamentoLocale() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//
		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());

		Locale locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());
		Locale localeInserito = localeRepository.save(locale);
		assertNotNull("Id null", localeInserito.getId());

		// Mofiche
		String descrizioneMod = "descrizione modificata";
		localeInserito.setDescrizione(descrizioneMod);
		String cittaMod = "Modica";
		localeInserito.getPosizione().setCitta(cittaMod);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Locale localeAggiornato = localeRepository.save(localeInserito);
		LOG.info("localeResponse ID: " + localeAggiornato.getId());

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNotNull("Id null", localeAggiornato.getId());

		assertEquals(localeInserito.getDescrizione(),
				localeAggiornato.getDescrizione());

		assertTrue(EqualsBuilder.reflectionEquals(
				localeInserito.getIdGestore(), localeAggiornato.getIdGestore()));
		assertTrue(EqualsBuilder.reflectionEquals(
				localeInserito.getPosizione(), localeAggiornato.getPosizione()));
		assertTrue(EqualsBuilder.reflectionEquals(localeInserito,
				localeAggiornato, "posizione", "gestore"));

	}

	@Test
	public void testAggiornamentoLocaleConGestoreModificato() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());

		Locale locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		Locale localeInserito = localeRepository.save(locale);
		assertNotNull("Id null", localeInserito.getId());

		// modifiche
		String descrizioneMod = "descrizione modificata";
		localeInserito.setDescrizione(descrizioneMod);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Locale localeAggiornato = localeRepository.save(localeInserito);
		LOG.info("localeResponse ID: " + localeAggiornato.getId());

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNotNull("Id null", localeAggiornato.getId());
		assertEquals(localeInserito.getDescrizione(),
				localeAggiornato.getDescrizione());

		assertTrue(localeInserito.getIdGestore().equals(
				localeAggiornato.getIdGestore()));
		assertTrue(EqualsBuilder.reflectionEquals(
				localeInserito.getPosizione(), localeAggiornato.getPosizione()));
		assertTrue(EqualsBuilder.reflectionEquals(localeInserito,
				localeAggiornato, "posizione"));
	}

	@Test
	public void testAggiornamentoLocaleEGestore() {

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		Gestore gestoreResponse = gestoreRepository.save(GestoreTestData
				.getGestore());
		LOG.info("gestoreResponse.id: " + gestoreResponse.getId());

		Locale locale = LocaleTestData.getLocale(gestoreResponse,
				PosizioneTestData.getPosizione());

		Locale localeInserito = localeRepository.save(locale);
		assertNotNull("Id null", localeInserito.getId());

		Locale localeInseritoPreMod = localeRepository.findOne(localeInserito
				.getId());

		// modifiche
		String descrizioneMod = "descrizione modificata";
		localeInserito.setDescrizione(descrizioneMod);
		String cittaMod = "Modica";
		localeInserito.getPosizione().setCitta(cittaMod);
		Gestore gestoreMod = GestoreTestData.getGestore();
		gestoreMod.setId(gestoreResponse.getId());
		String mailMod = "mofica@mail.com";
		gestoreMod.setMail(mailMod);
		localeInserito.setIdGestore(gestoreMod.getId());

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO DA TESTARE **//
		// ***************************************//

		Locale localeAggiornato = localeRepository.save(localeInserito);
		LOG.info("localeResponse ID: " + localeAggiornato.getId());

		// *******************//
		// ** 3 - ASSERT *****//
		// *******************//

		assertNotNull("Id null", localeAggiornato.getId());

		assertFalse(localeInseritoPreMod.getDescrizione().equals(
				localeAggiornato.getDescrizione()));

		assertFalse(EqualsBuilder.reflectionEquals(localeInseritoPreMod,
				localeAggiornato));

		assertTrue(EqualsBuilder.reflectionEquals(
				localeInseritoPreMod.getIdGestore(),
				localeAggiornato.getIdGestore()));

		assertTrue(EqualsBuilder.reflectionEquals(
				localeInseritoPreMod.getIdGestore(),
				localeAggiornato.getIdGestore()));

		assertFalse(EqualsBuilder.reflectionEquals(
				localeInseritoPreMod.getPosizione(),
				localeAggiornato.getPosizione()));
	}
}
