package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.GestoreService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.GestorePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = RestUrl.GESTORE + RestUrl.GESTORE)
public class GestoreGestoreController {

	@Autowired
	GestoreService gestoreService;

	@RequestMapping(value = RestUrl.LISTA, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson getGestoriAll(
			HttpServletResponse response) {

		List<GestorePmo> listaGestoriPmo = gestoreService.getListaGestori();

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonRicercaLista(listaGestoriPmo, null,
						SignalDescriptor.OFFERTA_GLOBALE_KO,
						SignalDescriptor.OFFERTA_GLOBALE_OK);

		return risposta;
	}
}
