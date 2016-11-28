package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.business.mapper.MapperStruct;
import it.bologna.devastapp.presentation.pmo.Test3Pmo;
import it.bologna.devastapp.presentation.pmo.TestEntity;
import it.bologna.devastapp.presentation.pmo.TestPmo;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;

public class MapperTest extends AbstractMapperValidatorTest {

	@Autowired
	MapperStruct mapperStructTest;

	@Test
	public void testMapper() {

		TestEntity es1 = new TestEntity();
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(11.3322323);
		pos1.setLongitude(1232.42354);
		es1.setDescription("Per mezz'ora birra a 1euro");
		es1.setPosition(pos1);

		es1.setQuantity(100);
		es1.setPrice(BigDecimal.valueOf(2.5));

		/*
		 * Chiamata Mapper
		 */

		TestPmo pmo = mapperStructTest.offertaToOffertaPmo(es1);

		assertEquals("LEGG", "Per mezz'ora birra a 1euro", pmo.getDescription());

	}
}
