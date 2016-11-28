package test.it.bologna.devastapp.prodotto;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.bologna.devastapp.business.ProdottoService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.ProdottoGestoreController;
import it.bologna.devastapp.presentation.RestUrl;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaRicercaLista;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import test.it.bologna.devastapp.AbstractPresentationWebTest;

import com.fasterxml.jackson.core.type.TypeReference;

public class ProdottoGestoreControllerTest extends AbstractPresentationWebTest {

	@Autowired
	ProdottoService prodottoService;
	@Autowired
	ProdottoRepository prodottoRepository;

	@Autowired
	ProdottoGestoreController prodottoController;

	public static final String URL_LISTA = RestUrl.GESTORE + RestUrl.PRODOTTO
			+ RestUrl.LISTA;

	@Test
	public void testListaProdotti() throws Exception {

		Prodotto birra = ProdottoTestData.getProdotto();
		birra.setDescrizione("birra");

		Prodotto cicchetto = ProdottoTestData.getProdotto();
		cicchetto.setDescrizione("cicchetto");

		prodottoRepository.save(birra);
		prodottoRepository.save(cicchetto);

		/*
		 * 0. Imposto il Controller da testare nel mockMvc
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(prodottoController)
				.setMessageConverters(mappingJackson2HttpMessageConverter)
				.build();

		/*
		 * 1. Dati di Test - Creo la richiesta JSON da dare al controller
		 */

		RequestBuilder richiesta = MockMvcRequestBuilders.get(URL_LISTA);

		String rispostaJson = this.mockMvc.perform(richiesta)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		// ****** MAPPING da JSON a OBJ******//
		// Tramite un metodo di utilita' mappo il JSON su il corrispettivo
		// Oggetto di risposta (RispostaInserimentoSingolo e' la classe
		// specifica del campo "dati" interno a BaseRispostaJson)
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaDaStringaJson(
				rispostaJson,
				new TypeReference<RispostaRicercaLista<ProdottoPmo>>() {
				});

		/*
		 * 2. ASSERT sul JSON di risposta
		 */
		// BaseRispostaJson
		assertCampiComuniBaseRispostaJson(risposta,
				SignalDescriptor.PRODOTTO_GET_LISTA_GLOBALE_OK, true);

		@SuppressWarnings("unchecked")
		RispostaRicercaLista<ProdottoPmo> dati = (RispostaRicercaLista<ProdottoPmo>) risposta
				.getDati();

		List<ProdottoPmo> listaProdotti = dati.getLista();

		assertEquals(2, listaProdotti.size());
	}

}
