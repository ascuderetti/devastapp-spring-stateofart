package test.it.bologna.devastapp.offerta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.validator.LocaleValidator;
import it.bologna.devastapp.business.validator.OffertaValidator;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;

public class OffertaValidatorTest extends AbstractMapperValidatorTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(OffertaValidatorTest.class);

	@Autowired
	OffertaValidator offertaValidator;
	@Autowired
	LocaleValidator localeValidator;

	@Test
	public void testValidazioneKo() {

		/*
		 * 1) Preapara dati di test
		 */

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), ProdottoTestData
						.getProdottoPmo());
		// condizione di errore
		offertaPmo.setDataInizio(null);
		offertaPmo.setDataFine(null);
		offertaPmo.setDescrizione(null);
		offertaPmo.setIdLocale(null);
		offertaPmo.setQuantita(null);
		offertaPmo.setPrezzoUnitarioPieno(null);
		offertaPmo.setIdProdotto(null);
		offertaPmo.setTitolo(null);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = offertaPmo
				.getListaBusinessSignal();
		LOG.info("Signal Size: " + listaBusSignal.size());

		assertEquals(offertaPmo.getListaBusinessSignal().size(), 8);

		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E001);
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E002);
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E003);
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E004);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.OFFERTA_CAMPO_PREZZO_OBBLIGATORIO);
		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.OFFERTA_CAMPO_PRODOTTO_OBBLIGATORIO);
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E007);
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_E008);

	}

	@Test
	public void testValidaOffertaDataFineAntecedenteDataInizio() {

		/*
		 * 1) Preapara dati di test
		 */

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), ProdottoTestData
						.getProdottoPmo());
		// condizione ko
		offertaPmo.setDataInizio(new DateTime(2014, 1, 1, 21, 0));

		LOG.info("Data inizio: " + offertaPmo.getDataInizio());

		offertaPmo.setDataFine(new DateTime(2014, 1, 1, 20, 0));

		LOG.info("Data fine: " + offertaPmo.getDataFine());

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = offertaPmo
				.getListaBusinessSignal();
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_DATA_FINE_MAGGIORE_DATA_FINE);

	}

	@Test
	public void testValidaOffertaPeriodoMaggioreDiUnOra() {

		/*
		 * 1) Preapara dati di test
		 */

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), ProdottoTestData
						.getProdottoPmo());
		// condizione ko
		offertaPmo.setDataInizio(new DateTime(2014, 1, 1, 20, 0));

		LOG.info("Data inizio: " + offertaPmo.getDataInizio());

		offertaPmo.setDataFine(new DateTime(2014, 1, 1, 22, 0));

		LOG.info("Data fine: " + offertaPmo.getDataFine());
		LOG.info("Differenza ore: "
				+ offertaPmo.getDataInizio().plus(Hours.ONE));

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = offertaPmo
				.getListaBusinessSignal();
		assertBusinessSignal(listaBusSignal, SignalDescriptor.OFFERTA_DATA_FINE_MAGGIORATA_DI_UN_ORA);

	}

	@Test
	public void testValidaOffertaOk() {

		/*
		 * 1) Preapara dati di test
		 */

		LocalePmo localePmo = LocaleTestData.getLocalePmo(
				GestoreTestData.getGestorePmo(),
				PosizioneTestData.getPosizionePmo());
		localePmo.setId(1L);

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, ProdottoTestData.getProdottoPmo());

		offertaPmo.setIdProdotto(ProdottoTestData.ID_PRODOTTO);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 3) Assert
		 */
		assertNull("Numero Business Signal Errato",
				offertaPmo.getListaBusinessSignal());

	}

	@Test
	public void testQuantitaNonInserita() {

		/*
		 * 1) Preapara dati di test
		 */

		LocalePmo localePmo = LocaleTestData.getLocalePmo(
				GestoreTestData.getGestorePmo(),
				PosizioneTestData.getPosizionePmo());
		localePmo.setId(1L);

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				localePmo, ProdottoTestData.getProdottoPmo());

		// condizione di errore
		offertaPmo.setQuantita(0);
		offertaPmo.setIdProdotto(ProdottoTestData.ID_PRODOTTO);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 3) Assert
		 */
		List<BusinessSignal> listaBusSignal = offertaPmo
				.getListaBusinessSignal();
		LOG.info("Signal Size: " + listaBusSignal.size());

		assertEquals(1, offertaPmo.getListaBusinessSignal().size());

		assertBusinessSignal(listaBusSignal,
				SignalDescriptor.OFFERTA_QUANTITA_MINOREUGUALEAZERO);

	}

	@Test(expected = ErroreSistema.class)
	public void testAggiornamentoOffertaIdNullKo() {

		/*
		 * 1) Preapara dati di test
		 */

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), ProdottoTestData
						.getProdottoPmo());

		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaAggiornaOfferta(offerta, offertaPmo, offertaPmo);

	}

	@Test(expected = ErroreSistema.class)
	public void testAggiornamentoOffertaStatoPubblicataKo() {

		/*
		 * 1) Preapara dati di test
		 */

		OffertaScritturaPmo offertaPmo = OffertaTestData.getOffertaPmo(
				LocaleTestData.getLocalePmo(GestoreTestData.getGestorePmo(),
						PosizioneTestData.getPosizionePmo()), ProdottoTestData
						.getProdottoPmo());

		Offerta offerta = OffertaTestData.getOfferta();
		offerta.setId(1L);
		offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);

		/*
		 * 2) Chiamata al metodo da testare
		 */

		offertaValidator.validaAggiornaOfferta(offerta, offertaPmo, offertaPmo);

	}

}
