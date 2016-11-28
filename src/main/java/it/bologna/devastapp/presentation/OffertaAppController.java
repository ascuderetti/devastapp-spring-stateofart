package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.MovimentiOffertaService;
import it.bologna.devastapp.business.OffertaService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaResponse;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
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
@RequestMapping(value = RestUrl.APP + RestUrl.OFFERTA)
public class OffertaAppController extends AbstractController {

	@Autowired
	MovimentiOffertaService movimentiOffertaService;
	@Autowired
	OffertaService offertaService;

	@RequestMapping(value = RestUrl.ID + RestUrl.FOLLOW, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson seguiOfferta(
			HttpServletResponse response,
			@RequestBody MovimentoUtentePmo checkFollowOffertaPmo) {

		MovimentoUtentePmo checkFollowOfferta = movimentiOffertaService
				.followOfferta(checkFollowOffertaPmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(checkFollowOfferta,
						SignalDescriptor.OFFERTA_FOLLOW_KO,
						SignalDescriptor.OFFERTA_FOLLOW_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.ID + RestUrl.CHECK, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson checkOfferta(
			HttpServletResponse response,
			@RequestBody MovimentoUtentePmo checkOffertaPmo) {

		/*
		 * Chiamata al Service
		 */
		MovimentoUtentePmo checkOffertaResponse = movimentiOffertaService
				.checkOfferta(checkOffertaPmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(checkOffertaResponse,
						SignalDescriptor.CHECK_OFFERTA_KO,
						SignalDescriptor.CHECK_OFFERTA_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.ONLINE + RestUrl.LISTA, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson listaOfferta(
			HttpServletResponse response, PosizionePmo posizionePmo) {

		/*
		 * Chiamata al Service
		 */
		OffertaLetturaResponse letturaOffertaResponse = offertaService
				.getListaOfferteOnlineByRaggioAndPosizione(posizionePmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonRicercaLista(
						letturaOffertaResponse.getListLetturaOffertaPmo(),
						letturaOffertaResponse.getPosizionePmo(),
						SignalDescriptor.OFFERTA_LISTA_KO,
						SignalDescriptor.OFFERTA_LISTA_OK);

		return risposta;
	}

}
