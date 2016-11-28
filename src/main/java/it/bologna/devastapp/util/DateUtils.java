package it.bologna.devastapp.util;

import it.bologna.devastapp.persistence.entity.Offerta;

public final class DateUtils {

	/**
	 * http://en.wikipedia.org/wiki/EN_28601
	 */
	public static String DATE_TIME_OFFSET_ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * @param offerta
	 * @return true se l'offerta e' online altrimenti false
	 */
	public static boolean isOnlineNow(Offerta offerta) {

		if (offerta.getDataInizio().isAfterNow()
				|| offerta.getDataFine().isBeforeNow()) {
			// OFFLINE
			return false;
		}
		// ONLINE
		return true;
	}

}
