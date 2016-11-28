package it.bologna.devastapp.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateMapper {

	private static final Logger LOG = LoggerFactory.getLogger(DateMapper.class);

	/**
	 * http://tools.ietf.org/html/rfc3339#section-5.6
	 */
	public static String DATE_TIME_OFFSET_ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	public String asString(DateTime date) {
		LOG.info("Convert the DateTime object: " + date + " to a String");
		return date != null ? date.toString(DateTimeFormat
				.forPattern(DATE_TIME_OFFSET_ISO_8601_FORMAT)) : null;
	}

	public DateTime asDate(String date) {
		LOG.info("Convert the String: " + date + " to a DateTime");
		return date != null ? DateTimeFormat.forPattern(
				DATE_TIME_OFFSET_ISO_8601_FORMAT).parseDateTime(date) : null;
	}
}
