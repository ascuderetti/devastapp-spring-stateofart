package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.business.notifications.model.NotificaTelefono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(AndroidServiceImpl.KEY_SPRING_ANDROID_SERVICE)
public class AndroidServiceImpl implements TelefonoService {

	/**
	 * Chiave del Bean
	 */
	public final static String KEY_SPRING_ANDROID_SERVICE = "androidService";

	private static final Logger LOG = LoggerFactory
			.getLogger(AndroidServiceImpl.class);

	@Override
	public void inviaNotifica(NotificaTelefono notifica) {

		LOG.info("THREAD ID:" + Thread.currentThread().getId());

		LOG.info("FINE - INVIO NOTIFICA ANDROID");

	}
}
