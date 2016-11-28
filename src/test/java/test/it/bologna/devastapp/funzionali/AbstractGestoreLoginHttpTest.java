package test.it.bologna.devastapp.funzionali;

import static org.junit.Assert.assertEquals;
import it.bologna.devastapp.presentation.RestUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class AbstractGestoreLoginHttpTest extends AbstractHttpTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractGestoreLoginHttpTest.class);

	public static final String USER_KEY = "user";
	public static final String USER_VALUE = "gestore";
	public static final String PWD_KEY = "password";
	public static final String PWD_VALUE = "gestore";

	@Before
	public void setUp() {

		LOG.info("setUp - LOGIN");
		/*
		 * Login
		 */
		// Prepare acceptable media type
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.ALL);

		// Prepare header
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);

		// Richiesta
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(USER_KEY, USER_VALUE);
		map.add(PWD_KEY, PWD_VALUE);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
				map, headers);

		/*
		 * Chiamata HTTP
		 */
		ResponseEntity<Void> response = restTemplate.exchange(URL_BASE_DEPLOY
				+ RestUrl.LOGIN, HttpMethod.POST, requestEntity, Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		/*
		 * Risposta HTTP - recupero jsession dalla risposta
		 */
		List<String> jsession = response.getHeaders().get("Set-Cookie");
		HEADER_SET_COOKIE = jsession.get(0);
		// Nota: per recuperare solo "jsessionid = " aggiungere:
		// .get(0).split(";")[0];
		// HEADER_SET_COOKIE = HEADER_SET_COOKIE + ";";
	}

	@After
	public void tearDown() {

		LOG.info("tearDown - LOGOUT");

		/*
		 * Logout
		 */
		// Prepare acceptable media type
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.ALL);

		// Prepare header
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		headers.set("Cookie", HEADER_SET_COOKIE);

		// Request
		HttpEntity<Void> requestEntity = new HttpEntity<Void>(null,
				getHeaderJsonLogin());

		ResponseEntity<Void> response = restTemplate.exchange(URL_BASE_DEPLOY
				+ RestUrl.LOGOUT, HttpMethod.POST, requestEntity, Void.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
	}

	public HttpHeaders getHeaderJsonLogin() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Cookie", HEADER_SET_COOKIE);
		return headers;
	}

}
