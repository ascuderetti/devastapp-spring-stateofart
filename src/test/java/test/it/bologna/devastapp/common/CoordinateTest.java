package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertTrue;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.util.DistanzaUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractRepositoryTest;
import test.it.bologna.devastapp.posizione.PosizioneTestData;

public class CoordinateTest extends AbstractRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(CoordinateTest.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	LocaleRepository localeRepository;

	@Autowired
	GestoreRepository gestoreRepository;

	@Autowired
	ProdottoRepository prodottoRepository;

	@Autowired
	OffertaRepository offertaRepository;

	@Test
	public void testCoordinate() {

		// Piazza Verdi 44.496207, 11.350776
		// Due Torri 44.494366, 11.346596

		PosizionePmo piazzaVerdi = PosizioneTestData.getPosizionePmo();
		piazzaVerdi.setLatitudine(44.496207);
		piazzaVerdi.setLongitudine(11.350776);

		PosizionePmo dueTorri = PosizioneTestData.getPosizionePmo();
		dueTorri.setLatitudine(44.494366);
		dueTorri.setLongitudine(11.346596);

		int distanza = DistanzaUtil.distanzaCoordinate(piazzaVerdi, dueTorri);

		LOG.info("Distanza in m: " + distanza);

		assertTrue("Distanza = 0", distanza > 0);

	}

}
