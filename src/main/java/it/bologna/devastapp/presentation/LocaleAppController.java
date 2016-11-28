package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.MovimentiLocaleService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
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
@RequestMapping(value = RestUrl.APP + RestUrl.LOCALE)
public class LocaleAppController {

	@Autowired
	MovimentiLocaleService movimentiLocaleService;

	@RequestMapping(value = RestUrl.FOLLOW, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson seguiLocale(
			HttpServletResponse response,
			@RequestBody MovimentoUtentePmo movimentoUtente) {

		MovimentoUtentePmo followLocale = movimentiLocaleService
				.followLocale(movimentoUtente);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(followLocale,
						SignalDescriptor.LOCALE_FOLLOW_KO,
						SignalDescriptor.LOCALE_FOLLOW_OK);

		return risposta;
	}
}
