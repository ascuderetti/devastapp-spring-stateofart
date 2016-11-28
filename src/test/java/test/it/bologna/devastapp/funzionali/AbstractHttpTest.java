package test.it.bologna.devastapp.funzionali;

import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.util.DateUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-service-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public abstract class AbstractHttpTest {

	// Valorizzato in fase di setUp (con il jsession id)
	public String HEADER_SET_COOKIE = "";

	public static final String URL_BASE_DEPLOY_LOCALE = "http://localhost:8080/devastapp";
	public static final String URL_BASE_DEPLOY_SVIL_CLOUDBEES = "";
	public static final String URL_BASE_DEPLOY = URL_BASE_DEPLOY_LOCALE;

	public static final RestTemplate restTemplate = new RestTemplate();
	public static final ObjectMapper objectMapper = new ObjectMapper();

	// configurazioni objectMapper
	static {

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new JodaModule());
		objectMapper.setDateFormat(new SimpleDateFormat(
				DateUtils.DATE_TIME_OFFSET_ISO_8601_FORMAT));
	}

	public <T> T getDatiFromBaseRispostaJson(BaseRispostaJson baseRispostaJson,
			Class<T> classeCampoDati) throws JsonParseException,
			JsonMappingException, JsonProcessingException, IOException {

		return (T) objectMapper.readValue(
				objectMapper.writeValueAsString(baseRispostaJson.getDati()),
				classeCampoDati);
	}

}
