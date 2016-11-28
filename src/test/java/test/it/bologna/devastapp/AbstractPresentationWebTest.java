package test.it.bologna.devastapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import test.it.bologna.devastapp.util.TestDefinitionAndUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-presentation-web-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@WebAppConfiguration
public abstract class AbstractPresentationWebTest {

	// ****************************************//
	// ** Componenti per Test su Controller **//
	// ***************************************//

	// WebApplicationContext (simula i dispatcher)
	@Autowired
	protected WebApplicationContext wac;
	@PersistenceContext
	private EntityManager entityManager;

	// Permette di effettuare chiamate HTTP
	protected MockMvc mockMvc;

	// Converter da settare sul mockMvc per gestire il mapping jsonToObj di
	// DateTime (TimeZone di Default +01:00)
	@Autowired
	protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	// ObjectMapper json<->Obj con Timezone di Default +01:00
	protected ObjectMapper objectMapper;

	// <-//

	// ******************//
	// ** SetUp Comune **//
	// *****************//
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
	}

	// *******************//
	// ** ASSERT Comuni **//
	// *******************//

	/**
	 * @param rispostaRicevuta
	 *            BaseRispostaJson ricevuta dal controller
	 * @param signalAttesa
	 * @param attesoSuccesso
	 */
	public static void assertCampiComuniBaseRispostaJson(
			BaseRispostaJson rispostaRicevuta, SignalDescriptor signalAttesa,
			boolean attesoSuccesso) {

		assertEquals("Campo successo errato", attesoSuccesso,
				rispostaRicevuta.isSuccesso());
		assertEquals("Campo tipoMessaggio errato",
				signalAttesa.getTipoMessaggio(),
				rispostaRicevuta.getTipoMessaggio());
		assertEquals("Campo messaggio errato", signalAttesa.getMessaggio(),
				rispostaRicevuta.getMessaggio());
		assertEquals("Campo codice errato", signalAttesa.getCodice(),
				rispostaRicevuta.getCodice());
		assertNull("Campo sorgente NON null", rispostaRicevuta.getSorgente());
	}

	@After
	public void tearDown() throws Exception {

		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(entityManager);
		fullTextEntityManager.purgeAll(Locale.class);
		fullTextEntityManager.flushToIndexes();

		TestDefinitionAndUtils.pulisciTabelleTargetDb();

	}

}
