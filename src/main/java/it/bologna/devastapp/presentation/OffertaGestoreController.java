package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.OffertaService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = RestUrl.GESTORE + RestUrl.OFFERTA)
public class OffertaGestoreController extends AbstractController {

	@Autowired
	OffertaService offertaService;

	@RequestMapping(value = RestUrl.INSERISCI, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	BaseRispostaJson inserisci(HttpServletResponse response,
			@RequestBody OffertaScritturaPmo offertaPmo) {

		OffertaScritturaPmo offerta = offertaService.createOfferta(offertaPmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(offerta,
						SignalDescriptor.OFFERTA_GLOBALE_KO,
						SignalDescriptor.OFFERTA_GLOBALE_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.AGGIORNA, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	BaseRispostaJson aggiorna(HttpServletResponse response,
			@RequestBody OffertaScritturaPmo offertaPmo) {

		OffertaScritturaPmo offerta = offertaService.updateOfferta(offertaPmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(offerta,
						SignalDescriptor.OFFERTA_GLOBALE_KO,
						SignalDescriptor.OFFERTA_GLOBALE_OK);

		return risposta;
	}

}
