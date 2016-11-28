package test.it.bologna.devastapp.funzionali;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;

public abstract class AbstractAppRestAuthHttpTest extends AbstractHttpTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractAppRestAuthHttpTest.class);

	public static final String USER = "app";
	public static final String PWD = "app";

	public static HttpHeaders getHeaderJsonBasicAuth() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		String auth = USER + ":" + PWD;
		byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
		headers.add("Authorization", "Basic "
				+ new String(encodedAuthorisation));

		return headers;

	}

	public static HttpHeaders getHeaderJsonDigestAuth(String auth) {

		// http://www.baeldung.com/resttemplate-digest-authentication
		/*
		 * TODO - Il nonce deve essere aggiornato ogni volta che il server
		 * risponde 401 con quello presente nell'header della risposta server
		 * (il nonce scade e viene ricreato ogni tot secondi) String
		 * headDigestAuth = response.getHeaders() //
		 * .get("WWW-Authenticate").get(0);
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		/*
		 * base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
		 * NONCE: expirationTime: The date and time when the nonce expires,
		 * expressed in milliseconds key: A private key to prevent modification
		 * of the nonce token
		 */
		headers.add(
				"Authorization",
				" Digest username=\"app\", realm=\"devastapp\", nonce=\"MTM5OTIyMDM0NDY1NDphM2Y4NWJkOTdmMjI3YjQxZjRkYTlkOTZiYWVhNzFhZg==\", uri=\"/devastapp/app/offerta/1/midevasto\", response=\"e02745f6357fa3a7fcc1e42e23609a7d\", opaque=\"\"");// +
		// new
		// String(encodedAuthorisation)s

		return headers;
	}
}
