package test.it.bologna.devastapp.movimentiofferta;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.mapper.MovimentiOffertaMapper;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class MovimentiOffertaMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	MovimentiOffertaMapper movimentiOffertaMapper;

	@Test
	public void testMovimentiOffertaPmoToMovimentiOfferta() {

		/*
		 * 1) Preapara dati di test
		 */
		MovimentoUtentePmo movimentiOffertaPmo = MovimentiOffertaTestData
				.getMovimentoUtentePmo(1L, 1L);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		MovimentiOfferta movimentiOfferta = movimentiOffertaMapper.pmoToEntity(
				movimentiOffertaPmo, Stato.FOLLOW);

		/*
		 * 3) Assert
		 */
		assertMovimentiOffertaEntityToMovimentiOffertaPmo(movimentiOfferta,
				movimentiOffertaPmo, Stato.FOLLOW);
	}

	@Test
	public void testMovimentiOffertaToCheckFollowOffertaPmo() {
		/*
		 * 1) Preapara dati di test
		 */
		MovimentiOfferta movimentiOfferta = MovimentiOffertaTestData
				.getMovimentiOfferta(1L, 1L, Stato.FOLLOW);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		MovimentoUtentePmo movimentiOffertaPmo = movimentiOffertaMapper
				.entityToPmo(movimentiOfferta);

		/*
		 * 3) Assert
		 */
		assertMovimentiOffertaEntityToMovimentiOffertaPmo(movimentiOfferta,
				movimentiOffertaPmo, Stato.FOLLOW);

	}

	public void assertMovimentiOffertaEntityToMovimentiOffertaPmo(
			MovimentiOfferta entity, MovimentoUtentePmo pmo, Stato stato) {
		assertEquals("campo non mappato correttamente", entity.getId(),
				pmo.getId());
		assertEquals("campo non mappato correttamente", entity.getIdOfferta(),
				pmo.getIdOggetto());
		assertEquals("campo non mappato correttamente", entity.getStato(),
				stato);
		assertEquals("campo non mappato correttamente", entity.getIdUtente(),
				pmo.getIdUtente());
	}
}
