package test.it.bologna.devastapp.common;

import it.bologna.devastapp.business.notifications.IphoneServiceImpl;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class APNTest {

	private static final Logger LOG = LoggerFactory.getLogger(APNTest.class);

	@Test
	public void test() {

		String absPath1 = "proto1.p12";
		Resource rsrc = new ClassPathResource("certificati/proto1.p12");
		try {
			absPath1 = rsrc.getFile().getAbsolutePath();
		} catch (IOException e) {
			LOG.error("Messaggio di errore: " + e.getLocalizedMessage(), e);
		}

		ApnsService service = APNS.newService().withCert(absPath1, "proto1")
				.withSandboxDestination().build();

		String payload = APNS.newPayload()
				.alertBody("son giniazzo prova async 3")
				.customField("staminchia", "rossa").build();
		service.push(IphoneServiceImpl.idTokenNocchio, payload);

		LOG.info("FINE INVIO NOTIFICA - IPHONE");

	}

}
