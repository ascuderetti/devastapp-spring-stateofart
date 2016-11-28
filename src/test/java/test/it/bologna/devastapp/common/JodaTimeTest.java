package test.it.bologna.devastapp.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JodaTimeTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(JodaTimeTest.class);

	@Test
	public void jodaTimeTest() {

		DateTime dataInizio = new DateTime(2014, 01, 01, 20, 0, 0, 0);
		LOG.info("dataInizio " + dataInizio);

		DateTime dataFine = new DateTime(2014, 01, 01, 21, 0, 0, 0);
		LOG.info("dataFine " + dataInizio);

		Interval durataOra = new Interval(dataInizio, dataFine);
		LOG.info("durataOra " + durataOra);

		Period period = new Period(dataInizio, dataFine);
		LOG.info("period " + period);

		assertEquals(dataInizio.plus(Hours.ONE).getHourOfDay(),
				dataFine.getHourOfDay());

		// Duration duration = new Duration(dataInizio, dataFine);
		// duration.get
	}

	@Test
	@Ignore("era una prova per settare i secondi e millisecondi a zero")
	public void setSecondsZero() {

		final DateTimeFormatter formatter2 = DateTimeFormat
				.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		// Asia/Taipei local time with time zone
		final String data = "2014-01-28T21:13:23.234+08:00";
		// dataInizio.withZone(newZone);
		final DateTime dataInizio = formatter2.parseDateTime(data);

		DateTime dataModificata = dataInizio.minuteOfHour().setCopy(0);
		dataModificata = dataModificata.secondOfMinute().setCopy(0);
		dataModificata = dataModificata.millisOfSecond().setCopy(0);

		DateTime now = new DateTime();
		now = now.secondOfMinute().setCopy(0);
		now = now.millisOfSecond().setCopy(0);
		int minuti = now.minuteOfHour().get();
		int response = (minuti % 15);
		minuti = minuti - response;
		now = now.minus(Minutes.minutes(now.minuteOfHour().get() % 15));

	}

	@Test
	public void getDateTimeItalyNowTest() {

		final DateTimeFormatter formatter2 = DateTimeFormat
				.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		// Asia/Taipei local time with time zone
		final String data = "2014-01-28T21:00:00.000+08:00";
		// dataInizio.withZone(newZone);
		final DateTime dataInizio = formatter2.parseDateTime(data);

		LOG.info("dataInizio " + dataInizio);

		final String data2 = "2014-01-28T23:00:00.000+08:00";
		final DateTime dataFine = formatter2.parseDateTime(data2);
		LOG.info("dataFine " + dataFine);

		// L'offerta e' a Taipei ed e' valida tra le 21 e le 23 ora locale

		Interval intervallo = new Interval(dataInizio, dataFine);
		LOG.info("intervallo ore " + intervallo);

		// Il server e' in Italia e la funzione di current time
		// restituira' l'ora locale italiana con un +01:00 come timezone
		final String data3 = "2014-01-28T15:00:00.000+01:00";
		final DateTime now = formatter2.parseDateTime(data3);

		LOG.info("Data di adesso " + now);
		assertTrue(intervallo.contains(now));

		// Se l'ora del follow calcolata dal server in Italia fosse 2 ore in più
		// del precedente test, non sarebbe dentro l'intervallo
		final String data4 = "2014-01-28T17:00:00.000+01:00";
		final DateTime now2 = formatter2.parseDateTime(data4);

		LOG.info("Data di adesso " + now2);
		assertTrue(!intervallo.contains(now2));
	}

	@Test
	public void getDateTimeNoServerTimeTest() {

		final DateTimeFormatter formatter2 = DateTimeFormat
				.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		// Asia/Taipei local time with time zone
		final String data = "2014-01-28T21:00:00.000+08:00";
		// DateTimeZone newZone = DateTimeZone.forID("Asia/Taipei");
		// dataInizio.withZone(newZone);
		final DateTime dataInizio = formatter2.withOffsetParsed()
				.parseDateTime(data);

		LOG.info("dataInizio " + dataInizio);

		final String data2 = "2014-01-28T23:00:00.000+08:00";
		final DateTime dataFine = formatter2.withOffsetParsed().parseDateTime(
				data2);
		LOG.info("dataFine " + dataFine);

		// L'offerta e' a Taipei ed e' valida tra le 21 e le 23 ora locale

		Interval intervallo = new Interval(dataInizio, dataFine);
		LOG.info("intervallo ore " + intervallo);

		// Il server e' in Italia e la funzione di current time
		// restituira' l'ora locale italiana con un +01:00 come timezone
		final String data3 = "2014-01-28T15:00:00.000+01:00";
		final DateTime now = formatter2.parseDateTime(data3);

		LOG.info("Data di adesso " + now);
		assertTrue(intervallo.contains(now));

		// Se l'ora del follow calcolata dal server in Italia fosse 2 ore in più
		// del precedente test, non sarebbe dentro l'intervallo
		final String data4 = "2014-01-28T17:00:00.000+01:00";
		final DateTime now2 = formatter2.parseDateTime(data4);

		LOG.info("Data di adesso " + now2);
		assertTrue(!intervallo.contains(now2));
	}
}
