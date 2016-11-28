package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.ProdottoService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = RestUrl.GESTORE + RestUrl.PRODOTTO)
public class ProdottoGestoreController extends AbstractController {

	@Autowired
	ProdottoService prodottoService;

	@RequestMapping(value = RestUrl.INSERISCI, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson inserisci(
			HttpServletResponse response, @RequestBody ProdottoPmo prodottoPmo) {

		ProdottoPmo prodotto = prodottoService.createProdotto(prodottoPmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(prodotto,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.LISTA, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson listaProdotti(
			HttpServletResponse response) {

		List<ProdottoPmo> listaProdotto = prodottoService.getProdotti();

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonRicercaLista(listaProdotto, null, null,
						SignalDescriptor.PRODOTTO_GET_LISTA_GLOBALE_OK);

		return risposta;
	}
}
