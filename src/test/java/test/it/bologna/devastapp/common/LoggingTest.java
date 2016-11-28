package test.it.bologna.devastapp.common;

import it.bologna.devastapp.persistence.entity.Offerta;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.it.bologna.devastapp.AbstractMapperValidatorTest;
import test.it.bologna.devastapp.offerta.OffertaTestData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class LoggingTest extends AbstractMapperValidatorTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoggingTest.class);

	@Test
	public void testLog() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		// Identa l'output (mette gli a capo)
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		Offerta off = OffertaTestData.getOfferta();
		LOG.info("TEST LOG RICORSIVO:\n" + objectMapper.writeValueAsString(off));

	}
}
