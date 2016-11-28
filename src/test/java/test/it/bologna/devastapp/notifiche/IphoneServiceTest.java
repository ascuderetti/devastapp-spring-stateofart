package test.it.bologna.devastapp.notifiche;

import it.bologna.devastapp.business.notifications.IphoneServiceImpl;
import it.bologna.devastapp.business.notifications.TelefonoService;
import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.persistence.entity.TipoTelefono;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import test.it.bologna.devastapp.AbstractServiceTest;

public class IphoneServiceTest extends AbstractServiceTest {

	// SERVICE DA TESTARE ->
	@Autowired
	@Qualifier(IphoneServiceImpl.KEY_SPRING_IPHONE_SERVICE)
	TelefonoService telefonoService;

	// <-//

	private static final Logger LOG = LoggerFactory
			.getLogger(IphoneServiceTest.class);

	@Test
	public void inviaNotifica() {

		NotificaTelefono notifica = new NotificaTelefono(TipoTelefono.ANDROID,
				"test", null);

		telefonoService.inviaNotifica(notifica);

	}
}
