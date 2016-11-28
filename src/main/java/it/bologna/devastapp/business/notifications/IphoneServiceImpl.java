package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.business.notifications.model.NotificaTelefono;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.exceptions.InvalidSSLConfig;

@Service(IphoneServiceImpl.KEY_SPRING_IPHONE_SERVICE)
public class IphoneServiceImpl implements TelefonoService {

	public static String idTokenNocchio = "f86b52f5bb19f912dbc0dbaf78f879852e63bb7d4299b55c24ef926475fe2779";

	/**
	 * Chiave del Bean
	 */
	public final static String KEY_SPRING_IPHONE_SERVICE = "iphoneService";

	private static final Logger LOG = LoggerFactory
			.getLogger(IphoneServiceImpl.class);

	// http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#resources-as-dependencies
	Resource certificato;
	String keyStorePwd;

	public IphoneServiceImpl(Resource certificato, String keyStorePwd) {

		this.certificato = certificato;
		this.keyStorePwd = keyStorePwd;
	}

	@Override
	public void inviaNotifica(NotificaTelefono notifica) {

		LOG.info("THREAD ID:" + Thread.currentThread().getId());

		/*
		 * TODO 1. Mapping da NotificaTelefono a model dati iPhone
		 */
		// "f86b52f5 bb19f912 dbc0dbaf 78f87985 2e63bb7d 4299b55c 24ef9264 75fe2779"
		// notifica.getIdTelefono();
		String token = idTokenNocchio;

		// notifica.getSegnalazione();
		String payload = APNS.newPayload()
				.alertBody("Il Boteco ha inserito una nuova offerta!").build();

		/*
		 * 2. Chiamata APNS
		 */
		// TODO withSandboxDestination e' solo per TEST?! - qual'è quella di
		// produzione
		ApnsService service;
		try {

			service = APNS.newService()
					.withCert(certificato.getInputStream(), keyStorePwd)
					.withSandboxDestination().build();

		} catch (InvalidSSLConfig e) {

			/*
			 * TODO creare Errore Sistema
			 */
			// ErroreSistema errSslconfing = new ErroreSistema(null);

			RuntimeException errSslconfing = new RuntimeException(
					e.getLocalizedMessage(), e);

			throw errSslconfing;

		} catch (IOException e) {

			RuntimeException errFile = new RuntimeException(
					e.getLocalizedMessage(), e);

			throw errFile;
		}
		/*
		 * Invio notifica
		 */
		service.push(token, payload);

		/*
		 * 3. Gestione Risposta (eliminare logicamente o fisicamente i device
		 * inattivi) - Task da fare in Async
		 */

		// Map<String, Date> inactiveDevices = service.getInactiveDevices();
		// for (String deviceToken : inactiveDevices.keySet()) {
		// Date inactiveAsOf = inactiveDevices.get(deviceToken);
		//
		// }
		LOG.info("FINE INVIO NOTIFICA - IPHONE");
	}

}
