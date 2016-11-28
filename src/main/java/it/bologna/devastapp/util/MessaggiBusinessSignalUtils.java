package it.bologna.devastapp.util;

import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.business.signal.TipoMessaggio;
import it.bologna.devastapp.log.LoggingAspect;
import it.bologna.devastapp.presentation.pmo.BasePmo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MessaggiBusinessSignalUtils {

	private static final Logger LOG = LoggerFactory
			.getLogger(MessaggiBusinessSignalUtils.class);

	/**
	 * Metodo di supporto, aggiunge una segnalazione al PMO
	 * 
	 * @param pmo
	 *            a cui aggiungere la signal
	 * @param segnalazione
	 *            enum {@link SignalDescriptor} che contiene codice, messaggio,
	 *            tipoMessaggio e sorgente dell'errore
	 * @param parametriMessaggio
	 *            campo opzionale, contiene evenutali argomenti parametri del
	 *            messaggio
	 */
	public static void aggiungiSignal(BasePmo pmo,
			SignalDescriptor segnalazione, Object... parametriMessaggio) {

		// Loggo la signal ->
		if (LOG.isDebugEnabled()) {
			try {
				LOG.debug(LoggingAspect.LOG_KEY_SIGNAL
						+ LoggingAspect.objectMapperPerLog
								.writeValueAsString(segnalazione)
						+ " - "
						+ pmo.getClass().getSimpleName()
						+ LoggingAspect.objectMapperPerLog
								.writeValueAsString(pmo));

			} catch (JsonProcessingException e) {
				LOG.error("LOGGING - JsonProcessingException: " + e);
			} catch (Exception e) {
				LOG.error(LoggingAspect.LOGGING_ASPECT_ERRORE_GENERICO + e);
			}
		}
		// <-//

		// Inizializzo la lista se null
		if (pmo.getListaBusinessSignal() == null) {
			pmo.setListaBusinessSignal(new ArrayList<BusinessSignal>());
		}

		// creo la business signal
		BusinessSignal bSignal = new BusinessSignal(segnalazione.getCodice(),
				getMessage(segnalazione.getMessaggio(), parametriMessaggio),
				segnalazione.getTipoMessaggio(), segnalazione.getSorgente());

		// la aggiungo alla lista delle signal del pmo
		pmo.getListaBusinessSignal().add(bSignal);
	}

	/**
	 * Confronta due BusinessSignal, se sono uguali torna true, altrimenti
	 * false.
	 * 
	 * Controlla che siano uguali tutti i campi tranne @link
	 * {@link BusinessSignal#getUniqueId()} che deve essere univoco per ogni BS
	 * 
	 * @param bs1
	 * @param bs2
	 * @return
	 */
	public static boolean isBusinessSignalsEquals(BusinessSignal bs1,
			BusinessSignal bs2) {

		if (ObjectUtils.equals(bs1.getCodice(), bs2.getCodice())
				&& ObjectUtils.equals(bs1.getMessaggio(), bs2.getMessaggio())
				&& ObjectUtils.equals(bs1.getSorgente(), bs2.getSorgente())
				&& EqualsBuilder.reflectionEquals(bs1.getTipoMessaggio(),
						bs2.getTipoMessaggio())) {
			return true;
		}

		return false;
	}

	/**
	 * Metodo di utilita' che verifica che la lista delle Signal NON contenga
	 * segnalazioni di tipo {@link TipoMessaggio#ERRORE_BUSINESS}
	 * 
	 * @return
	 */
	public static boolean isValidPmo(BasePmo pmo) {

		List<BusinessSignal> listaBusinessSignal = pmo.getListaBusinessSignal();

		if (listaBusinessSignal != null) {

			// Se esiste almeno una signal con tipo Error, allora il PMO non e'
			// valido
			for (BusinessSignal temp : listaBusinessSignal) {
				if (TipoMessaggio.ERRORE_BUSINESS.compareTo(temp
						.getTipoMessaggio()) == 0) {
					return false;
				}

			}

		}

		return true;
	}

	/**
	 * 
	 * @param pattern
	 *            :string "template" con parametri in formato {0}, {1}
	 *            ecc..Esempio, "Il campo {0} e' obbligatorio"
	 * @param args
	 *            :argomenti che andranno a sostituire il valore dei paramentri
	 *            {0}, {1} ecc..in base all'ordine
	 * @return String: stringa completa
	 */
	public static String getMessage(String pattern, Object... args) {
		MessageFormat mf = new MessageFormat(pattern, Locale.ITALY);

		return mf.format(args);
	};
}
