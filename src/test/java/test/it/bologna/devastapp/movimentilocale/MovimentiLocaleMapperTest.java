package test.it.bologna.devastapp.movimentilocale;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.mapper.MovimentiLocaleMapper;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class MovimentiLocaleMapperTest extends AbstractMapperValidatorTest {

	@Autowired
	MovimentiLocaleMapper movimentiLocaleMapper;

	@Test
	public void testPmoToEntity() {

		/*
		 * 1) Preapara dati di test
		 */
		MovimentoUtentePmo pmo = MovimentiLocaleTestData.getMovimentoUtentePmo(
				1L, 1L);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		MovimentiLocale entity = movimentiLocaleMapper.pmoToEntity(pmo,
				Stato.FOLLOW);

		/*
		 * 3) Assert
		 */
		assertMovimentiLocaleEntityToMovimentoUtentePmo(entity, pmo,
				Stato.FOLLOW);
	}

	@Test
	public void testEntityToPmo() {
		/*
		 * 1) Preapara dati di test
		 */
		MovimentiLocale entity = MovimentiLocaleTestData.getMovimentiLocale(1L,
				1L, Stato.FOLLOW);

		/*
		 * 2) Chiamata al metodo da testare
		 */
		MovimentoUtentePmo pmo = movimentiLocaleMapper.entityToPmo(entity);

		/*
		 * 3) Assert
		 */
		assertMovimentiLocaleEntityToMovimentoUtentePmo(entity, pmo,
				Stato.FOLLOW);

	}

	public void assertMovimentiLocaleEntityToMovimentoUtentePmo(
			MovimentiLocale entity, MovimentoUtentePmo pmo, Stato stato) {
		assertEquals("campo non mappato correttamente", entity.getId(),
				pmo.getId());
		assertEquals("campo non mappato correttamente", entity.getIdLocale(),
				pmo.getIdOggetto());
		assertEquals("campo non mappato correttamente", entity.getStato(),
				stato);
		assertEquals("campo non mappato correttamente", entity.getIdUtente(),
				pmo.getIdUtente());
	}
}
