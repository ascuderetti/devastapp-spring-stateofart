package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.PagamentoService;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import javax.servlet.http.HttpServletRequest;
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
public class PagamentoGestoreController extends AbstractController {

	@Autowired
	PagamentoService pagamentoService;

	@RequestMapping(value = RestUrl.ACQUISTA, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson acquista(
			HttpServletResponse response, @RequestBody PagamentoPmo pagamentoPmo) {

		/*
		 * Chiamata al Service (aprire una transazione gia in partenza????)
		 */
		pagamentoPmo = pagamentoService.createPagamento(pagamentoPmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaJsonPagamento(
				pagamentoPmo, SignalDescriptor.PAGAMENTO_PAYPAL_CREATO_KO,
				SignalDescriptor.PAGAMENTO_PAYPAL_CREATO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.ORDINE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson confermaOrdine(
			HttpServletResponse response, HttpServletRequest request,
			@RequestBody PagamentoPmo pagamentoPmo) {

		String payerID = request.getParameter(Definitions.Paypal.PAYER_ID);
		pagamentoPmo.setPayerID(payerID);

		/*
		 * Chiamata al Service
		 */
		PagamentoPmo pagamentoVisualizzato = pagamentoService
				.visualizzaOrdine(pagamentoPmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaJsonPagamento(
				pagamentoVisualizzato,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_KO,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.CANCELLA, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson cancellaOrdine(
			HttpServletResponse response, @RequestBody PagamentoPmo pagamentoPmo) {

		/*
		 * Chiamata al Service
		 */
		PagamentoPmo pagamentoCancellato = pagamentoService
				.visualizzaOrdine(pagamentoPmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaJsonPagamento(
				pagamentoCancellato,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_KO,
				SignalDescriptor.PAGAMENTO_PAYPAL_VISUALIZZATO_OK);

		return risposta;
	}

	@RequestMapping(value = RestUrl.PAGA, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson paga(HttpServletResponse response,
			@RequestBody PagamentoPmo pagamentoPmo) {

		/*
		 * Chiamata al Service
		 */
		PagamentoPmo pagamentoEffettuato = pagamentoService
				.eseguiPagamento(pagamentoPmo);

		/*
		 * Creo Risposta JSON
		 */
		BaseRispostaJson risposta = JsonResponseUtil.creaRispostaJsonPagamento(
				pagamentoEffettuato,
				SignalDescriptor.PAGAMENTO_PAYPAL_ESEGUITO_KO,
				SignalDescriptor.PAGAMENTO_PAYPAL_ESEGUITO_OK);

		return risposta;
	}
}
