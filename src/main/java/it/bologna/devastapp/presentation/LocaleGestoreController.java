package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.LocaleService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(value = RestUrl.GESTORE + RestUrl.LOCALE)
public class LocaleGestoreController {

	@Autowired
	LocaleService localeService;

	/**
	 * Metodo provvisorio per test ricieste con upload file
	 * 
	 * @param request
	 * @param test
	 * @param file
	 * @return
	 */
	@RequestMapping(value = RestUrl.INSERISCI + "Imm", method = RequestMethod.POST, consumes = {
			"multipart/form-data", "multipart/mixed" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson inserisciImm(
			MultipartHttpServletRequest request,
			@RequestPart(value = "jsonString", required = false) String test,
			@RequestPart("file") Part file) {
		// LocalePmo localePmo
		String orgName = file.getContentType();

		LocalePmo locale = localeService.createLocale(null);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(locale,
						SignalDescriptor.LOCALE_INSERIMENTO_KO,
						SignalDescriptor.LOCALE_INSERIMENTO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.INSERISCI, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson inserisci(
			HttpServletResponse response, @RequestBody LocalePmo localePmo) {

		LocalePmo localePmoRisposta = localeService.createLocale(localePmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(localePmoRisposta,
						SignalDescriptor.LOCALE_INSERIMENTO_KO,
						SignalDescriptor.LOCALE_INSERIMENTO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.AGGIORNA, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson aggiorna(
			HttpServletResponse response, @RequestBody LocalePmo localePmo) {

		LocalePmo locale = localeService.updateLocale(localePmo);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(locale,
						SignalDescriptor.LOCALE_AGGIORNAMENTO_KO,
						SignalDescriptor.LOCALE_AGGIORNAMENTO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.LISTA, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson listaByGestore(
			HttpServletResponse response, Long idGestore) {

		List<LocalePmo> localeList = localeService.findByGestore(idGestore);

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonRicercaLista(localeList, null, null,
						SignalDescriptor.LOCALE_LISTA_OK);

		return risposta;
	}
}
