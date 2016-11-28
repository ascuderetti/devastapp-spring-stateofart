package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.TipoTelefono;
import it.bologna.devastapp.persistence.entity.Utente;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import test.it.bologna.devastapp.gestore.GestoreTestData;
import test.it.bologna.devastapp.locale.LocaleTestData;
import test.it.bologna.devastapp.offerta.OffertaTestData;
import test.it.bologna.devastapp.posizione.PosizioneTestData;
import test.it.bologna.devastapp.prodotto.ProdottoTestData;
import test.it.bologna.devastapp.utente.UtenteTestData;
import test.it.bologna.devastapp.util.TestDefinitionAndUtils;

/**
 * Test Enver Audit di {@link MovimentiOfferta}
 * 
 * @author ascuderetti
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-repository-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class MovimentiOffertaRepositoryAuditTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovimentiOffertaRepositoryAuditTest.class);

	@PersistenceContext
	EntityManager entityManager;

	// REPOSITORY DA TESTARE
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;
	// <-//

	// REPOSITORY PER CREAZIONE DATI DI TEST
	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	LocaleRepository localeRepository;
	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	UtenteRepository utenteRepository;

	// <-//

	// Dati di Test

	MovimentiOfferta followUtente1;

	@BeforeTransaction
	public void setUpBeforeTransaction() {

		// logic to verify the initial state before a transaction is started

		// ********************************//
		// ** 1 - DATI DI TEST ************//
		// ********************************//

		/*
		 * 1.1 Offerta
		 */
		// Prodotto
		Prodotto prodotto = prodottoRepository.save(ProdottoTestData
				.getProdotto());
		// Gestore
		Gestore gestore = gestoreRepository.save(GestoreTestData.getGestore());
		// Posizione
		Posizione posizione = PosizioneTestData.getPosizione();
		// Locale
		Locale locale = LocaleTestData.getLocale(gestore, posizione);
		locale = localeRepository.save(locale);
		// Offerta
		Offerta offerta = OffertaTestData.getOfferta(prodotto, locale);
		offerta = offertaRepository.save(offerta);

		// ***************************************//
		// ** 2 - CHIAMATA AL METODO - CON AUDIT **//
		// ***************************************//

		/*
		 * 2.1 Inserisco un Movimento Offerta in Follow (questo è un insert)
		 */
		Utente utente1 = UtenteTestData.getUtente("ID_TELEFONO");
		String idTel1 = "1";
		utente1.setIdTelefono(idTel1);
		utente1.setTipoTelefono(TipoTelefono.IPHONE);
		utente1 = utenteRepository.save(utente1);
		followUtente1 = MovimentiOffertaTestData.getMovimentiOfferta(
				utente1.getId(), offerta.getId(), Stato.FOLLOW);

		movimentiOffertaRepository.save(followUtente1);
		/*
		 * 2.2 Effettuo il Check (questa è una modifica)
		 */
		followUtente1.setStato(Stato.CHECK);
		movimentiOffertaRepository.save(followUtente1);

	}

	@Before
	public void setUpTestDataWithinTransaction() {
		// set up test data within the transaction
	}

	/**
	 * Test Audit<br>
	 * <ul>
	 * <li>1. Effettuo un Follow</li>
	 * <li>2. Modifico il Follow in Check</li>
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void movimentiOffertaAuditTest() {

		// ****************************************//
		// ** RECUPERO DATI DI AUDIT E ASSERT *****//
		// ****************************************//
		AuditReader auditReader = AuditReaderFactory.get(entityManager);
		AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(
				MovimentiOfferta.class, false, true);

		List<Object[]> auditMovimentiOfferta = query.getResultList();
		assertEquals(2, auditMovimentiOfferta.size());

		/*
		 * Audit 1
		 */
		Object[] audit1 = auditMovimentiOfferta.get(0);
		MovimentiOfferta mov1 = (MovimentiOfferta) audit1[0];
		DefaultRevisionEntity revEntity1 = (DefaultRevisionEntity) audit1[1];
		RevisionType revType1 = (RevisionType) audit1[2];

		LOG.info("Audit1 => Id Mov: " + mov1.getId() + "_Azione: "
				+ revType1.toString() + "_Data: "
				+ revEntity1.getRevisionDate());
		// Assert
		assertEquals(followUtente1.getId(), mov1.getId());
		assertEquals(Stato.FOLLOW, mov1.getStato());
		assertEquals(RevisionType.ADD, revType1);

		/*
		 * Audit 2
		 */
		Object[] audit2 = auditMovimentiOfferta.get(1);
		MovimentiOfferta mov2 = (MovimentiOfferta) audit2[0];
		DefaultRevisionEntity revEntity2 = (DefaultRevisionEntity) audit2[1];
		RevisionType revType2 = (RevisionType) audit2[2];

		LOG.info("audit2 => Id Mov: " + mov2.getId() + "_Azione: "
				+ revType2.toString() + "_Data: "
				+ revEntity2.getRevisionDate());
		// Assert
		assertEquals(followUtente1.getId(), mov2.getId());
		assertEquals(Stato.CHECK, mov2.getStato());
		assertEquals(RevisionType.MOD, revType2);

	}

	@After
	public void tearDownWithinTransaction() throws Exception {
		// execute "tear down" logic within the transaction

	}

	@AfterTransaction
	public void tearDownAfterTransaction() throws Exception {
		// logic to verify the final state after transaction has rolled back
		TestDefinitionAndUtils.pulisciTabelleTargetDb();
	}

}
