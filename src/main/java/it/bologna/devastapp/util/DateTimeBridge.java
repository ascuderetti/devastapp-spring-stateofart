package it.bologna.devastapp.util;

import org.hibernate.search.bridge.StringBridge;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serve per indicizzare correttamente le date.
 * 
 * TODO Valutare se eliminarlo con i proddimi rilasci di hibernate
 * 
 * @author ascuderetti
 *
 */
public class DateTimeBridge implements StringBridge {

	private static final Logger LOG = LoggerFactory
			.getLogger(DateTimeBridge.class);

	@Override
	public String objectToString(Object object) {

		DateTime datetime = (DateTime) object;
		if (datetime == null) {
			return null;
		}
		String date = Long.toString(datetime.getMillis());
		return date;
	}

}
